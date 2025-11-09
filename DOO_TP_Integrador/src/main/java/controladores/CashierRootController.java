package controladores;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import ubp.doo.doo_tp_integrador.App; // Para App.getCurrentUser() y App.setRoot()

import fachada.FachadaSistemaVentas;
import modelos.Cajero;
import modelos.Domicilio;
import modelos.Persona;
import enums.Genero;

import dto.UsuarioDto;
import dto.PersonaDto;
import dto.DomicilioDto;
import dto.VentaDto;
import dto.ClienteDto;
import dto.FacturaDto;

import dao.ConexionSql; // Tu clase de conexión
import dao.UsuarioDao;
import dao.PersonaDao;
import dao.DomicilioDao; // Asegúrate que este DAO exista y funcione
import dao.VentaDao;
import dao.ClienteDao;
import dao.FacturaDao;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection; // Para la conexión a BD
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Para manejar excepciones de SQL
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List; // Para la lista de teléfonos
import java.util.ResourceBundle;

public class CashierRootController implements Initializable {

    @FXML private BorderPane root;
    @FXML private StackPane contentPane;
    @FXML private VBox salesPane;
    @FXML private TableView<SaleViewData> tvSales;    @FXML private TableColumn<SaleViewData, String> colSaleId;
    @FXML private TableColumn<SaleViewData, String> colInvoice;
    @FXML private TableColumn<SaleViewData, String> colDate;
    @FXML private TableColumn<SaleViewData, String> colClient;
    @FXML private TableColumn<SaleViewData, String> colTotal;
    @FXML private TableColumn<SaleViewData, String> colMethod;
    @FXML private TableColumn<SaleViewData, String> colCardNo;
    @FXML private TableColumn<SaleViewData, String> colStatus;
    @FXML private TableColumn<SaleViewData, String> colDiscount;
    @FXML private Button btnNewSale;
    @FXML private Label lblCashier;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("$#,##0.00");
    private ObservableList<SaleViewData> salesData = FXCollections.observableArrayList();

    private FachadaSistemaVentas fachadaSistemaVentas;
    private Cajero cajeroActual; // Este es modelos.Cajero
    
    // Static reference to the current CashierRootController instance for navigation
    private static CashierRootController currentInstance;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the current instance for static navigation methods
        currentInstance = this;
        
        this.fachadaSistemaVentas = new FachadaSistemaVentas();
        setupTableColumns();
        loadCashierInfoAndSetCajero(); // Se llama aquí para configurar el cajero
        loadSalesData(); // Se llama después para que pueda usar info del cajero si es necesario
    }private void setupTableColumns() {
        colSaleId.setCellValueFactory(cellData -> cellData.getValue().saleIdProperty());
        colInvoice.setCellValueFactory(cellData -> cellData.getValue().invoiceNumberProperty());
        colDate.setCellValueFactory(cellData -> cellData.getValue().dateTimeProperty());
        colClient.setCellValueFactory(cellData -> cellData.getValue().clientNameProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
        colMethod.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());
        colCardNo.setCellValueFactory(cellData -> cellData.getValue().cardNumberProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colDiscount.setCellValueFactory(cellData -> cellData.getValue().discountProperty());
    }

    private void loadCashierInfoAndSetCajero() {
        UsuarioDto loggedInUserDto = App.getCurrentUser();

        if (loggedInUserDto == null) {
            lblCashier.setText("Error: Sesión no iniciada");
            btnNewSale.setDisable(true);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No se pudo recuperar la información del usuario. Por favor, inicie sesión nuevamente.");
            // Opcionalmente, forzar la vuelta al login:
            // try { App.setRoot("login"); } catch (IOException e) { e.printStackTrace(); }
            return;
        }

        ConexionSql conSql = null;
        Connection conn = null;

        try {
            conSql = new ConexionSql();
            conn = conSql.getConnection();

            if (conn == null) {
                throw new SQLException("No se pudo obtener la conexión a la base de datos desde ConexionSql en Cashier_rootController.");
            }

            // Asumimos que loggedInUserDto ya tiene idUsuario e idPersona válidos.
            // Si necesitaras buscar el UsuarioDto completo por login (si App.getCurrentUser() solo diera el login):
            // UsuarioDao usuarioDao = new UsuarioDao(conn);
            // UsuarioDto fullUsuarioDto = usuarioDao.buscarPorLogin(loggedInUserDto.getLogin());
            // if (fullUsuarioDto == null) throw new RuntimeException("Usuario no encontrado en BD: " + loggedInUserDto.getLogin());
            // loggedInUserDto = fullUsuarioDto; // Actualizar con datos completos si fuera el caso

            PersonaDao personaDao = new PersonaDao(conn);
            DomicilioDao domicilioDao = new DomicilioDao(conn); // Asegúrate que DomicilioDao está implementado

            // 1. Buscar PersonaDto
            PersonaDto personaDto = personaDao.buscar(new PersonaDto(loggedInUserDto.getIdPersona()));
            if (personaDto == null) {
                throw new RuntimeException("No se encontró la información de Persona para el usuario ID: " + loggedInUserDto.getIdPersona());
            }

            // 2. Buscar DomicilioDto
            DomicilioDto domicilioDto = domicilioDao.buscar(new DomicilioDto(personaDto.getIdDomicilio()));
            if (domicilioDto == null) {
                throw new RuntimeException("No se encontró el Domicilio para la persona ID: " + personaDto.getIdPersona() + " (ID Domicilio: " + personaDto.getIdDomicilio() + ")");
            }

            // 3. Construir los modelos
            Domicilio domicilioModel = new Domicilio(
                    domicilioDto.getNomCalle(), domicilioDto.getNumVivienda(),
                    domicilioDto.getNumDepartamento(), domicilioDto.getCodPostal(),
                    domicilioDto.getLocalidad(), domicilioDto.getDepartamento(), domicilioDto.getPais()
            );
            
            List<String> telefonos = new ArrayList<>(); // Placeholder para teléfonos
            // Aquí iría la lógica para cargar teléfonos desde la BD si es necesario.

            Persona personaModel = new Persona(
                    personaDto.getNombre(), personaDto.getApellido(), personaDto.getNumDocumento(),
                    personaDto.getTipoDocumento(), personaDto.getCuit(), personaDto.getCondicionAfip(),
                    Genero.valueOf(personaDto.getGenero()), // Conversión de String a Enum
                    personaDto.getFechaNacimiento(), domicilioModel, personaDto.getEmail(),
                    telefonos 
            );

            this.cajeroActual = new Cajero(
                    personaModel.getNombre(), personaModel.getApellido(), personaModel.getNumDocumento(),
                    personaModel.getTipoDocumento(), personaModel.getCuit(), personaModel.getCondicionAfip(),
                    personaModel.getGenero(), personaModel.getFechaNacimiento(),
                    personaModel.getDomicilio(), personaModel.getEmail(), personaModel.getTelefonos()
            );
            this.cajeroActual.setLogin(loggedInUserDto.getLogin());
            // this.cajeroActual.setPassword("******"); // No almacenar/usar password en texto plano en el modelo
            this.cajeroActual.setFechaUltAcceso(loggedInUserDto.getFechaUltAcceso());


            lblCashier.setText(this.cajeroActual.getNombre() + " " + this.cajeroActual.getApellido());
            btnNewSale.setDisable(false);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            lblCashier.setText("Error: Datos género");
            btnNewSale.setDisable(true);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Datos", "El valor para 'género' del usuario no es válido: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            lblCashier.setText("Error: Conexión BD");
            btnNewSale.setDisable(true);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudieron cargar los datos del cajero: " + e.getMessage());
        } catch (RuntimeException e) { 
            e.printStackTrace();
            lblCashier.setText("Error: Datos Cajero");
            btnNewSale.setDisable(true);
            mostrarAlerta(Alert.AlertType.ERROR, "Error Interno", "No se pudieron cargar los datos completos del cajero: " + e.getMessage());
        } finally {
            if (conSql != null) {
                conSql.cerrar();
            }
        }
    }    private void loadSalesData() {
        salesData.clear();
        
        ConexionSql conSql = null;
        Connection conn = null;
        
        try {
            conSql = new ConexionSql();
            conn = conSql.getConnection();
            
            if (conn == null) {
                throw new SQLException("No se pudo obtener la conexión a la base de datos.");
            }
            
            // Create DAOs
            VentaDao ventaDao = new VentaDao(conn);
            ClienteDao clienteDao = new ClienteDao(conn);
            PersonaDao personaDao = new PersonaDao(conn);
            
            // Get all completed sales (estado = "Completada")
            List<VentaDto> ventasCompletadas = obtenerVentasCompletadas(conn);
            
            for (VentaDto venta : ventasCompletadas) {
                try {
                    // Get client information
                    ClienteDto clienteDto = clienteDao.buscar(new ClienteDto(venta.getIdCliente()));
                    String clientName = "Cliente Desconocido";
                    
                    if (clienteDto != null) {
                        PersonaDto personaDto = personaDao.buscar(new PersonaDto(clienteDto.getIdPersona()));
                        if (personaDto != null) {
                            clientName = personaDto.getNombre() + " " + personaDto.getApellido();
                        }
                    }
                    
                    // Get invoice information
                    FacturaDto facturaDto = obtenerFacturaPorId(conn, venta.getIdFactura());
                    String invoiceNumber = "F-" + String.format("%06d", venta.getIdFactura());
                    String dateTime = "Fecha N/D";
                    String totalAmount = "$0.00";
                    
                    if (facturaDto != null) {
                        dateTime = facturaDto.getFechaEmitida();
                        totalAmount = CURRENCY_FORMATTER.format(facturaDto.getTotalFactura());
                    }
                    
                    // Determine payment method from venta.getTipo()
                    String paymentMethod = mapearMedioPago(venta.getTipo());
                      // Format additional data for the new columns
                    String saleId = String.valueOf(venta.getIdVenta());
                    String cardNumber = formatCardNumber(venta.getNoTarjeta());
                    String status = venta.getEstado();
                    String discount = obtenerDescuentoInfo(conn, venta.getIdDescuento());
                    
                    // Add to sales data with all 9 columns
                    salesData.add(new SaleViewData(saleId, invoiceNumber, dateTime, clientName, 
                                                 totalAmount, paymentMethod, cardNumber, status, discount));
                    
                } catch (Exception e) {
                    System.err.println("Error al procesar venta ID " + venta.getIdVenta() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", 
                "No se pudieron cargar las ventas: " + e.getMessage());
              // Show fallback example data if database fails
            if (this.cajeroActual != null) {
                salesData.add(new SaleViewData("001", "F-EJ001", LocalDateTime.now().minusHours(1).format(DATETIME_FORMATTER), 
                    "Cliente Ejemplo 1", CURRENCY_FORMATTER.format(125.50), "Efectivo", "N/A", "Completada", "Sin descuento"));
                salesData.add(new SaleViewData("002", "F-EJ002", LocalDateTime.now().minusMinutes(20).format(DATETIME_FORMATTER), 
                    "Cliente Ejemplo 2", CURRENCY_FORMATTER.format(340.00), "Tarjeta Crédito", "**** **** **** 1234", "Completada", "5.0%"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error Interno", 
                "Error inesperado al cargar las ventas: " + e.getMessage());
        } finally {
            if (conSql != null) {
                conSql.cerrar();
            }
        }
        
        tvSales.setItems(salesData);
    }
    
    /**
     * Obtiene las ventas que están en estado "Completada" desde la base de datos
     */
    private List<VentaDto> obtenerVentasCompletadas(Connection conn) throws SQLException {
        List<VentaDto> ventasCompletadas = new ArrayList<>();
        String sql = "SELECT idVenta, idCliente, idCajero, idDescuento, idFactura, " +
                     "noTarjeta, tipo, estado FROM Venta WHERE estado = ? ORDER BY idVenta DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Completada");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer idDesc = rs.getObject("idDescuento") != null ? rs.getInt("idDescuento") : null;
                String nTarjeta = rs.getString("noTarjeta");
                
                VentaDto dto = new VentaDto(
                    rs.getInt("idVenta"),
                    rs.getInt("idCliente"),
                    rs.getInt("idCajero"),
                    idDesc,
                    rs.getInt("idFactura"),
                    nTarjeta,
                    rs.getString("tipo"),
                    rs.getString("estado")
                );
                ventasCompletadas.add(dto);
            }
        }
        
        return ventasCompletadas;
    }
    
    /**
     * Obtiene la información de una factura por su ID
     */
    private FacturaDto obtenerFacturaPorId(Connection conn, int idFactura) throws SQLException {
        String sql = "SELECT idFactura, fechaEmitida, totalFactura FROM Factura WHERE idFactura = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new FacturaDto(
                    rs.getInt("idFactura"),
                    rs.getString("fechaEmitida"),
                    rs.getFloat("totalFactura")
                );
            }
        }
        
        return null;
    }
      /**
     * Mapea el tipo de pago de la base de datos a un nombre legible
     */
    private String mapearMedioPago(String tipo) {
        if (tipo == null) return "N/D";
        
        switch (tipo.toLowerCase()) {
            case "contado":
            case "efectivo":
                return "Efectivo";
            case "credito":
            case "tarjeta_credito":
                return "Tarjeta Crédito";
            case "debito":
            case "tarjeta_debito":
                return "Tarjeta Débito";
            default:
                return tipo; // Return as-is if not recognized
        }
    }
    
    /**
     * Formatea el número de tarjeta para mostrar solo los últimos 4 dígitos
     */
    private String formatCardNumber(String noTarjeta) {
        if (noTarjeta == null || noTarjeta.trim().isEmpty()) {
            return "N/A";
        }
        // Mostrar solo los últimos 4 dígitos por seguridad
        if (noTarjeta.length() > 4) {
            return "**** **** **** " + noTarjeta.substring(noTarjeta.length() - 4);
        }
        return noTarjeta;
    }
    
    /**
     * Obtiene información del descuento aplicado a la venta
     */
    private String obtenerDescuentoInfo(Connection conn, Integer idDescuento) {
        if (idDescuento == null) {
            return "Sin descuento";
        }
        
        try {
            String sql = "SELECT tipo, valor FROM Descuento WHERE idDescuento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idDescuento);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    float valor = rs.getFloat("valor");
                    
                    if (tipo.contains("porcentual")) {
                        return String.format("%.1f%%", valor);
                    } else if (tipo.contains("monto")) {
                        return CURRENCY_FORMATTER.format(valor);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener descuento: " + e.getMessage());
        }
        
        return "Descuento aplicado";
    }    @FXML
    private void handleLogout(ActionEvent event) {        
        App.setCurrentUser(null); // Limpia el usuario de la sesión
        
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("DEBUG: Error en navegación a login: " + e.getMessage());
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo volver a la pantalla de login.");
        }
    }

    @FXML
    private void startSale(ActionEvent event) {
        if (this.cajeroActual == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Sesión", "No se ha podido identificar al cajero. Por favor, reinicie sesión.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("cashierSale.fxml"));
            Parent saleView = loader.load();

            if (loader.getController() instanceof CashierSaleController) {
                CashierSaleController saleController = loader.getController();
                saleController.initData(this.fachadaSistemaVentas, this.cajeroActual);
            } else {
                 System.err.println("CashierRootController: El controlador de cashierSale.fxml no es del tipo esperado (CashierSaleController).");
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(saleView);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Iniciar Venta", "No se pudo cargar la vista de venta: " + e.getMessage());
        }
    }

    public void showSalesList() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(salesPane);
        loadSalesData();
    }
    
    /**
     * Static navigation methods to be called from other controllers
     */
    public static void navigateToSale() {
        if (currentInstance != null) {
            currentInstance.showSaleView();
        }
    }
    
    public static void navigateToPayment() {
        if (currentInstance != null) {
            currentInstance.showPaymentView();
        }
    }
    
    public static void navigateToConfirm() {
        if (currentInstance != null) {
            currentInstance.showConfirmView();
        }
    }
    
    public static void navigateToMenu() {
        if (currentInstance != null) {
            currentInstance.showSalesList();
        }
    }
    
    /**
     * Instance methods to load different views into contentPane
     */
    private void showSaleView() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("cashierSale.fxml"));
            Parent saleView = loader.load();

            if (loader.getController() instanceof CashierSaleController) {
                CashierSaleController saleController = loader.getController();
                if (CashierNavigationData.hasDataForRestoration()) {
                    // Restore existing sale data
                    saleController.initDataWithExistingVenta(
                        CashierNavigationData.getFachadaForRestoration(), 
                        CashierNavigationData.getVentaForRestoration()
                    );
                    CashierNavigationData.clearAll();
                } else {
                    // Start new sale
                    saleController.initData(this.fachadaSistemaVentas, this.cajeroActual);
                }
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(saleView);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar venta", "No se pudo cargar la vista de venta: " + e.getMessage());
        }
    }
    
    private void showPaymentView() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("cashierPayment.fxml"));
            Parent paymentView = loader.load();            if (loader.getController() instanceof CashierPaymentController) {
                CashierPaymentController paymentController = loader.getController();
                if (CashierNavigationData.hasDataForRestoration()) {
                    // Initialize with navigation data
                    paymentController.initData(
                        CashierNavigationData.getFachadaForRestoration(), 
                        CashierNavigationData.getVentaForRestoration(),
                        CashierNavigationData.getCajeroForRestoration()
                    );
                    CashierNavigationData.clearAll();
                }
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(paymentView);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar pago", "No se pudo cargar la vista de pago: " + e.getMessage());
        }
    }
    
    private void showConfirmView() {        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("cashierConfirm.fxml"));
            Parent confirmView = loader.load();

            if (loader.getController() instanceof CashierConfirmController) {
                CashierConfirmController confirmController = loader.getController();
                if (CashierNavigationData.hasConfirmDataForRestoration()) {
                    // Initialize with confirmation data
                    confirmController.initData(
                        CashierNavigationData.getFachadaForConfirmation(),
                        CashierNavigationData.getFacturaForConfirmation(),
                        CashierNavigationData.getVentaForConfirmation()
                    );
                    CashierNavigationData.clearConfirmData();
                }
            }
            contentPane.getChildren().clear();
            contentPane.getChildren().add(confirmView);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar confirmación", "No se pudo cargar la vista de confirmación: " + e.getMessage());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Error al inicializar los datos de confirmación: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(Alert.AlertType alertType, String titulo, String mensaje) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }    public static class SaleViewData {
        private final StringProperty saleId;
        private final StringProperty invoiceNumber;
        private final StringProperty dateTime;
        private final StringProperty clientName;
        private final StringProperty totalAmount;
        private final StringProperty paymentMethod;
        private final StringProperty cardNumber;
        private final StringProperty status;
        private final StringProperty discount;

        public SaleViewData(String saleId, String invoiceNumber, String dateTime, String clientName, 
                           String totalAmount, String paymentMethod, String cardNumber, String status, String discount) {
            this.saleId = new SimpleStringProperty(saleId);
            this.invoiceNumber = new SimpleStringProperty(invoiceNumber);
            this.dateTime = new SimpleStringProperty(dateTime);
            this.clientName = new SimpleStringProperty(clientName);
            this.totalAmount = new SimpleStringProperty(totalAmount);
            this.paymentMethod = new SimpleStringProperty(paymentMethod);
            this.cardNumber = new SimpleStringProperty(cardNumber);
            this.status = new SimpleStringProperty(status);
            this.discount = new SimpleStringProperty(discount);
        }
        
        // Legacy constructor for backwards compatibility
        public SaleViewData(String invoiceNumber, String dateTime, String clientName, String totalAmount, String paymentMethod) {
            this("N/A", invoiceNumber, dateTime, clientName, totalAmount, paymentMethod, "N/A", "N/A", "N/A");
        }
        
        public StringProperty saleIdProperty() { return saleId; }
        public StringProperty invoiceNumberProperty() { return invoiceNumber; }
        public StringProperty dateTimeProperty() { return dateTime; }
        public StringProperty clientNameProperty() { return clientName; }
        public StringProperty totalAmountProperty() { return totalAmount; }
        public StringProperty paymentMethodProperty() { return paymentMethod; }
        public StringProperty cardNumberProperty() { return cardNumber; }
        public StringProperty statusProperty() { return status; }
        public StringProperty discountProperty() { return discount; }
    }
}