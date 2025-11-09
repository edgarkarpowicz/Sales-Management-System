package controladores;

import fachada.FachadaSistemaVentas;
import modelos.Cajero;
import modelos.Cliente;
import modelos.Producto;
import modelos.Venta;
import servicios.VentaService;

import ubp.doo.doo_tp_integrador.App;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controlador de la vista cashierSale.fxml.
 * - Usa VentaService para obtener clientes y productos.
 * - Permite buscar un producto por código de barras.
 * - Agrega ítems a la Venta y luego pasa al flujo de pago.
 */
public class CashierSaleController implements Initializable {

    // ===================================================================
    //  FXML Components
    // ===================================================================
    @FXML private VBox salePane;

    /** ComboBox para seleccionar el cliente (Apellido, Nombre) */
    @FXML private ComboBox<Cliente> cbCustomer;

    /** TextField donde se ingresa el código de barras o búsqueda por nombre */
    @FXML private TextField tfSearch;

    @FXML private Button btnAddProduct;

    @FXML private TableView<SaleItem> tvItems;
    @FXML private TableColumn<SaleItem, String> colCode;
    @FXML private TableColumn<SaleItem, String> colName;
    @FXML private TableColumn<SaleItem, Number> colQty;
    @FXML private TableColumn<SaleItem, String> colPrice;
    @FXML private TableColumn<SaleItem, String> colSubtotal;

    @FXML private Label lblTotal;
    @FXML private Button btnProceedToPayment;
    @FXML private Button btnCancelSale;

    @FXML private TextField tfQuantity;

    // ===================================================================
    //  Internal State and Utilities
    // ===================================================================
    private static final DecimalFormat MONEY = new DecimalFormat("$#,##0.00");
    private final ObservableList<SaleItem> items = FXCollections.observableArrayList();
    private final VentaService ventaService = new VentaService();

    private FachadaSistemaVentas fachada;
    private Cajero cajero;
    private Venta ventaActual;    // ===================================================================
    //  Inyección de fachada y cajero (desde CashierRootController)
    // ===================================================================
    public void initData(FachadaSistemaVentas fachada, Cajero cajero) {
        this.fachada = fachada;
        this.cajero  = cajero;
    }

    /**
     * Método para restaurar el estado de una venta existente al volver desde la pantalla de pago
     */
    public void initDataWithExistingVenta(FachadaSistemaVentas fachada, Venta ventaExistente) {
        this.fachada = fachada;
        this.cajero = ventaExistente.getCajero();
        this.ventaActual = ventaExistente;
          // Seleccionar el cliente en el ComboBox
        cbCustomer.setValue(ventaExistente.getCliente());
          // Restaurar los items en la tabla desde la venta existente
        // Consolidar productos por código de barras para evitar duplicados
        items.clear();
        Map<String, Integer> productQuantityMap = new HashMap<>();
        
        // Primero, sumar todas las cantidades por producto usando código de barras
        for (var detalleVenta : ventaExistente.getDetalles()) {
            String codigoBarras = detalleVenta.getProducto().getCodigoBarras();
            int currentQuantity = productQuantityMap.getOrDefault(codigoBarras, 0);
            productQuantityMap.put(codigoBarras, currentQuantity + detalleVenta.getCantidad());
        }
        
        // Luego, crear los SaleItems consolidados
        for (var detalleVenta : ventaExistente.getDetalles()) {
            String codigoBarras = detalleVenta.getProducto().getCodigoBarras();
            if (productQuantityMap.containsKey(codigoBarras)) {
                // Solo agregar una vez por producto con la cantidad total
                items.add(new SaleItem(detalleVenta.getProducto(), productQuantityMap.get(codigoBarras)));
                productQuantityMap.remove(codigoBarras); // Remover para evitar duplicados
            }
        }
        
        // Actualizar el total
        updateTotal();
    }

    // ===================================================================
    //  Inicialización del controlador
    // ===================================================================    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        loadCustomers();

        // Permitir presionar Enter en el TextField para buscar/agregar producto
        tfSearch.setOnKeyPressed(this::handleSearchKey);
        
        // Check if we need to restore data from navigation (coming back from payment screen)
        if (CashierNavigationData.hasDataForRestoration()) {
            restoreDataFromNavigation();
        }
    }

    private void configurarTabla() {
        colCode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colQty.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCantidad()));
        colPrice.setCellValueFactory(d -> new SimpleStringProperty(MONEY.format(d.getValue().getPrecioUnitario())));
        colSubtotal.setCellValueFactory(d -> new SimpleStringProperty(MONEY.format(d.getValue().getSubtotal())));
        tvItems.setItems(items);
    }

    /**
     * Carga los clientes en cbCustomer usando VentaService.obtenerClientes().
     * Formatea cada cliente como "Apellido, Nombre".
     */
    private void loadCustomers() {
        List<Cliente> listaClientes = ventaService.obtenerClientes();
        cbCustomer.setItems(FXCollections.observableArrayList(listaClientes));

        cbCustomer.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getApellido() + ", " + c.getNombre());
                }
            }
        });

        cbCustomer.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cliente c) {
                return (c == null) ? null : c.getApellido() + ", " + c.getNombre();
            }
            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
    }

    // ===================================================================
    //  Eventos de UI (botones / teclas)
    // ===================================================================
    @FXML
    private void handleAddProduct(ActionEvent event) {
        processAddProduct();
    }
        
    private void addProduct(ActionEvent event) {
        processAddProduct();
    }

    private void handleSearchKey(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            processAddProduct();
        }
    }

    /**
     * Lógica para buscar el producto (por código de barras) y agregarlo a la venta:
     * 1) Si no existe ventaActual, valida que haya cliente seleccionado y genera la venta.
     * 2) Llama a ventaService.buscarProductoPorCodigo(codigoBarras).
     * 3) Si existe, invoca fachada.escanearProducto(...) y actualiza la tabla.
     * 4) Si no existe, muestra alerta de “producto no encontrado”.
     */
    private void processAddProduct() {
        String codigo = tfSearch.getText().trim();
        int cantidad = 1;
        try {
            String cantidadStr = tfQuantity.getText().trim();
            if (!cantidadStr.isEmpty()) {
                cantidad = Integer.parseInt(cantidadStr);
            }
            if (cantidad <= 0) cantidad = 1;
        } catch (NumberFormatException e) {
            cantidad = 1;
        }
        final int cantidadFinal = cantidad; // Solución para la lambda
        if (codigo.isEmpty()) {
            return;
        }

        // 1) Verificar producto existente en BD por su código de barras
        Producto producto = ventaService.buscarProductoPorCodigo(codigo);
        if (producto == null) {
            showAlert(Alert.AlertType.WARNING,
                      "Producto no encontrado",
                      "No se encontró ningún producto con el código de barras: " + codigo);
            return;
        }

        // 2) Si es la primera línea de la venta, verificar cliente seleccionado
        if (ventaActual == null) {
            Cliente clienteSeleccionado = cbCustomer.getValue();
            if (clienteSeleccionado == null) {
                showAlert(Alert.AlertType.INFORMATION,
                          "Cliente requerido",
                          "Por favor seleccione un cliente antes de iniciar la venta.");
                return;
            }
            ventaActual = fachada.generarVenta(clienteSeleccionado, cajero);
        }

        // 3) Registrar la línea en el dominio
        fachada.escanearProducto(producto, cantidadFinal, ventaActual);

        // 4) Actualizar vista: si ya existe el producto en la tabla, incrementar cantidad
        items.stream()
             .filter(item -> item.getCodigo().equals(codigo))
             .findFirst()
             .ifPresentOrElse(
                 item -> item.incrementarCantidad(cantidadFinal),
                 () -> items.add(new SaleItem(producto, cantidadFinal))
             );

        tvItems.refresh();
        updateTotal();
        tfSearch.clear();
        tfQuantity.setText("1");
    }    @FXML
    private void goToPayment(ActionEvent event) {
        if (ventaActual == null || items.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION,
                      "Venta incompleta",
                      "Debe agregar al menos un producto antes de continuar al pago.");
            return;
        }

        try {
            // Store data for the payment controller to use with the static navigation approach
            CashierNavigationData.setVentaForRestoration(ventaActual);
            CashierNavigationData.setCajeroForRestoration(cajero);
            CashierNavigationData.setFachadaForRestoration(fachada);
            
            // Use static navigation to preserve MenuBar
            CashierRootController.navigateToPayment();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                      "Error al cargar Pago",
                      "No se pudo abrir la pantalla de pago:\n" + e.getMessage());
        }
    }    @FXML
    private void cancelSale(ActionEvent event) {
        try {
            // Clear any navigation data and return to main cashier menu using static navigation to preserve MenuBar
            CashierNavigationData.clearAll();
            CashierRootController.navigateToMenu();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo volver al menú principal.");
        }
    }

    // ===================================================================
    //  Métodos auxiliares
    // ===================================================================
    private void updateTotal() {
        double total = items.stream().mapToDouble(SaleItem::getSubtotal).sum();
        lblTotal.setText(MONEY.format(total));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alerta = new Alert(type);
        alerta.setHeaderText(null);
        alerta.setTitle(title);
        alerta.setContentText(message);
        alerta.showAndWait();
    }

    /**
     * Restore sale data when coming back from payment screen using App.setRoot navigation
     */
    private void restoreDataFromNavigation() {
        if (CashierNavigationData.hasDataForRestoration()) {
            this.fachada = CashierNavigationData.getFachadaForRestoration();
            this.cajero = CashierNavigationData.getCajeroForRestoration();
            Venta ventaExistente = CashierNavigationData.getVentaForRestoration();
            
            if (ventaExistente != null) {
                this.ventaActual = ventaExistente;
                
                // Seleccionar el cliente en el ComboBox
                cbCustomer.setValue(ventaExistente.getCliente());
                
                // Restaurar los items en la tabla desde la venta existente
                // Consolidar productos por código de barras para evitar duplicados
                items.clear();
                Map<String, Integer> productQuantityMap = new HashMap<>();
                
                // Primero, sumar todas las cantidades por producto usando código de barras
                for (var detalleVenta : ventaExistente.getDetalles()) {
                    String codigoBarras = detalleVenta.getProducto().getCodigoBarras();
                    int currentQuantity = productQuantityMap.getOrDefault(codigoBarras, 0);
                    productQuantityMap.put(codigoBarras, currentQuantity + detalleVenta.getCantidad());
                }
                
                // Luego, crear los SaleItems consolidados
                for (var detalleVenta : ventaExistente.getDetalles()) {
                    String codigoBarras = detalleVenta.getProducto().getCodigoBarras();
                    if (productQuantityMap.containsKey(codigoBarras)) {
                        // Solo agregar una vez por producto con la cantidad total
                        items.add(new SaleItem(detalleVenta.getProducto(), productQuantityMap.get(codigoBarras)));
                        productQuantityMap.remove(codigoBarras); // Remover para evitar duplicados
                    }
                }
                
                // Actualizar el total
                updateTotal();
            }
            
            // Clear the navigation data after restoration
            CashierNavigationData.clearAll();
        }
    }
    
    // ===================================================================
    //  Clase interna para representar cada línea de producto en la tabla
    // ===================================================================
    public static class SaleItem {
        private final Producto producto;
        private int cantidad;

        public SaleItem(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public void incrementarCantidad() {
            this.cantidad++;
        }

        public void incrementarCantidad(int cant) {
            this.cantidad += cant;
        }

        // Getters para que TableView pueda renderizar cada columna
        public String getCodigo() {
            return producto.getCodigoBarras();
        }
        public String getNombre() {
            return producto.getNombre();
        }
        public int getCantidad() {
            return cantidad;
        }
        public double getPrecioUnitario() {
            return producto.getPrecio();
        }
        public double getSubtotal() {
            return cantidad * getPrecioUnitario();
        }
    }
}