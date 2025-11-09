package controladores;

import fachada.FachadaSistemaVentas;
import modelos.Venta;
import modelos.Cajero;
import modelos.MediosPago;
import modelos.Factura;

import estrategia.EstrategiaPagoContado;
import estrategia.EstrategiaPagoDebito;
import estrategia.EstrategiaPagoCredito;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.time.LocalDateTime;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ubp.doo.doo_tp_integrador.App; 

/**
 * Controlador de cashierPayment.fxml
 * Aplica el recargo correspondiente según el medio de pago seleccionado.
 */
public class CashierPaymentController implements Initializable {    @FXML private Label lblTotal;
    @FXML private VBox vboxPaidAmount;
    @FXML private Label lblPaidAmountLabel;
    @FXML private TextField tfPaidAmount;
    @FXML private HBox hboxChange;
    @FXML private Label lblChangeLabel;
    @FXML private Label lblChange;
    @FXML private ToggleGroup tgPayment;
    @FXML private RadioButton rbCash;
    @FXML private RadioButton rbDebit;
    @FXML private RadioButton rbCredit;
    @FXML private VBox vboxCardNumber;
    @FXML private TextField tfCardNumber;
    @FXML private Button btnBackToSale;
    @FXML private Button btnConfirmPayment;
    @FXML private Button btnCancelSale;

    private static final DecimalFormat MONEY = new DecimalFormat("$#,##0.00");
    private final DoubleProperty totalProperty = new SimpleDoubleProperty();    private FachadaSistemaVentas fachada;
    private Venta venta;
    private Cajero cajero;    /**
     * Este initData lo llamas desde CashierSaleController, pasándole la venta ya cargada.
     */
    public void initData(FachadaSistemaVentas fachada, Venta venta, Cajero cajero) {
        this.fachada = fachada;
        this.venta   = venta;
        this.cajero  = cajero;

        // 1) Por defecto, “Contado” (sin recargo)
        MediosPago medioInicial = new MediosPago(new EstrategiaPagoContado());
        venta.setMedioPago(medioInicial);

        // 2) Seteo totalProperty con el total base (0% recargo)
        totalProperty.set(venta.calcularTotalConRecargo());

        // 3) Selecciono visualmente el radio "Contado"
        rbCash.setSelected(true);
          // 4) Ocultar el campo de tarjeta por defecto (ya que "Contado" está seleccionado)
        hideCardNumberField();
        
        // 5) Mostrar campos de monto pagado por defecto (ya que "Contado" está seleccionado)
        showPaidAmountFields();
    }    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check if we need to initialize with navigation data
        if (CashierNavigationData.hasDataForRestoration()) {
            this.fachada = CashierNavigationData.getFachadaForRestoration();
            this.venta = CashierNavigationData.getVentaForRestoration();
            this.cajero = CashierNavigationData.getCajeroForRestoration();
            
            // Initialize the payment screen with the restored data
            if (this.venta != null) {
                // 1) Por defecto, "Contado" (sin recargo)
                MediosPago medioInicial = new MediosPago(new EstrategiaPagoContado());
                venta.setMedioPago(medioInicial);

                // 2) Seteo totalProperty con el total base (0% recargo)
                totalProperty.set(venta.calcularTotalConRecargo());

                // 3) Selecciono visualmente el radio "Contado"
                rbCash.setSelected(true);
                // 4) Ocultar el campo de tarjeta por defecto (ya que "Contado" está seleccionado)
                hideCardNumberField();
                
                // 5) Mostrar campos de monto pagado por defecto (ya que "Contado" está seleccionado)
                showPaidAmountFields();
            }
            
            // Clear navigation data after initialization
            CashierNavigationData.clearAll();
        }
        
        // 1) BIND para que lblTotal muestre el valor formateado de totalProperty
        lblTotal.textProperty().bind(
            Bindings.createStringBinding(
                () -> MONEY.format(totalProperty.get()),
                totalProperty
            )
        );

        // 2) Listener para detectar cuándo el usuario cambia de radio
        tgPayment.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                // 2.1) Creo un MediosPago nuevo para cambiar la estrategia
                MediosPago medioPago = null;

                if (rbCash.isSelected()) {
                    // “Contado” (0% recargo)
                    medioPago = new MediosPago(new EstrategiaPagoContado());
                    hideCardNumberField(); // Ocultar campo de tarjeta
                    showPaidAmountFields(); // Mostrar campos de monto y vuelto
                } else if (rbDebit.isSelected()) {
                    // "Débito" (ej. 0% recargo por defecto en EstrategiaPagoDebito)
                    medioPago = new MediosPago(new EstrategiaPagoDebito());
                    showCardNumberField(); // Mostrar campo de tarjeta
                    hidePaidAmountFields(); // Ocultar campos de monto y vuelto
                } else if (rbCredit.isSelected()) {
                    // "Crédito" (20% recargo por defecto en EstrategiaPagoCredito)
                    medioPago = new MediosPago(new EstrategiaPagoCredito());
                    showCardNumberField(); // Mostrar campo de tarjeta
                    hidePaidAmountFields(); // Ocultar campos de monto y vuelto
                }                // 2.2) Asigno a la venta la nueva estrategia
                venta.setMedioPago(medioPago);

                // 2.3) Llamar al método ingresarMedioPago del cliente para cambiar estado
                if (venta.getCliente() != null) {
                    try {
                        venta.getCliente().ingresarMedioPago(venta, medioPago);
                    } catch (IllegalStateException e) {
                        // Si ya se ingresó el medio de pago, solo actualizamos
                        venta.setMedioPago(medioPago);
                    }
                }

                // 2.4) Recalculo total con recargo y actualizo totalProperty
                totalProperty.set(venta.calcularTotalConRecargo());

                // 2.5) También actualizo el vuelto (si hay algo escrito en tfPaidAmount)
                updateChange();
            }
        });

        // 3) Cuando el usuario presiona ENTER en el TextField, confirmamos pago
        tfPaidAmount.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER) {
                try {
                    confirmPayment(null);
                } catch (SQLException ex) {
                    Logger.getLogger(CashierPaymentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });        // 4) Cada vez que cambie el texto, recalculamos el vuelto
        tfPaidAmount.textProperty().addListener((obs, oldVal, newVal) -> {
            updateChange();
        });
        
        // 5) Limitar la longitud del número de tarjeta a 19 dígitos
        tfCardNumber.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > 19) {
                tfCardNumber.setText(oldVal);
            }
        });
        
        btnConfirmPayment.setDisable(false);
        btnConfirmPayment.setOnAction(event -> {
            try {
                confirmPayment(event);
            } catch (SQLException ex) {
                Logger.getLogger(CashierPaymentController.class.getName()).log(Level.SEVERE, null, ex);
            }        });
    }

    @FXML
    private void confirmPayment(ActionEvent event) throws SQLException {
        // Validar número de tarjeta si es necesario
        if ((rbDebit.isSelected() || rbCredit.isSelected()) && vboxCardNumber.isVisible()) {
            String cardNumber = tfCardNumber.getText().trim();
            if (!cardNumber.isEmpty() && !isValidCardNumber(cardNumber)) {
                showAlert(Alert.AlertType.ERROR, "Número de tarjeta inválido", 
                         "El número de tarjeta debe tener entre 13 y 19 dígitos.");
                return;
            }
        }        double paid;
        if (rbCash.isSelected() && tfPaidAmount.isVisible()) {
            // Solo validar monto para pago en contado
            try {
                paid = Double.parseDouble(tfPaidAmount.getText());
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Importe inválido", "Ingrese un número válido.");
                return;
            }
            
            double totalConRecargo = totalProperty.get();
            if (paid < totalConRecargo) {
                showAlert(
                    Alert.AlertType.INFORMATION,
                    "Pago insuficiente",
                    "El importe ingresado es menor al total de la venta."
                );
                return;
            }
        } else {
            // Para crédito/débito, el pago es exacto
            paid = totalProperty.get();
        }// Crear el medio de pago final con número de tarjeta si corresponde
        MediosPago medioFinal = createPaymentMethod();
        venta.setMedioPago(medioFinal);

        // Llamar al método ingresarMedioPago a través del cliente para cambiar estado a MedioPagoIngresado
        if (venta.getCliente() != null) {
            try {
                venta.getCliente().ingresarMedioPago(venta, medioFinal);
            } catch (IllegalStateException e) {
                // Si ya se ingresó el medio de pago, solo actualizamos
                venta.setMedioPago(medioFinal);
            }
        }        // Ahora proceder con el pago desde el estado MedioPagoIngresado
        try {
            venta.getEstadoVenta().pagarVenta();
            // Después del pago exitoso, generar la factura para cambiar a estado Completada
            venta.getEstadoVenta().generarFactura();
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error de estado", 
                     "No se puede procesar el pago en el estado actual: " + e.getMessage());
            return;
        }        // Aquí iría la lógica para registrar la venta/pago en la fachada, 
        // por ej: fachada.registrarPago(venta, (float) paid);

        
        // --- La factura ya fue generada por el estado Pago, solo necesitamos obtenerla
        Factura factura = venta.getFactura();
        if (factura == null) {
            // Si por alguna razón no se generó la factura, crearla manualmente
            String fechaStr = LocalDateTime
                                  .now()
                                  .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            factura = new Factura(fechaStr, (float) totalProperty.get());
            venta.setFactura(factura);
        }        // Si tu Factura tiene ID autogenerado en BD, asígalo aquí (por ejemplo:  fachada.guardarVentaYDevolverFactura(venta))
        // factura.setIdFactura(idRetornadoPorBD);        // Navigate to confirmation screen using static navigation to preserve MenuBar
        try {
            // Store confirmation data for the confirmation controller
            CashierNavigationData.setConfirmationData(fachada, factura, venta);
            
            CashierRootController.navigateToConfirm();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la pantalla de confirmación.");
        }
    }

    private void updateChange() {
        try {
            double paid = Double.parseDouble(tfPaidAmount.getText());
            double change = paid - totalProperty.get();
            lblChange.setText(MONEY.format(change));
        } catch (NumberFormatException ex) {
            lblChange.setText("-");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }    /**
     * Helper method to get the Cajero instance
     */
    private Cajero getCajeroFromVenta() {
        return this.cajero;
    }
      @FXML
    private void backToSale(ActionEvent event) {
        // Navigate back to sale screen using static navigation to preserve MenuBar
        try {
            // Store the data needed for restoration in the navigation helper
            CashierNavigationData.setVentaForRestoration(this.venta);
            CashierNavigationData.setCajeroForRestoration(this.cajero);
            CashierNavigationData.setFachadaForRestoration(this.fachada);
            
            CashierRootController.navigateToSale();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo volver a la pantalla de venta.");
        }
    }    @FXML
    private void cancelSale(ActionEvent event) {
        // Clear any navigation data and return to main cashier menu using static navigation to preserve MenuBar
        try {
            CashierNavigationData.clearAll();
            CashierRootController.navigateToMenu();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo volver al menú principal.");
        }
    }/**
     * Muestra los campos de monto pagado y vuelto (para pago en contado)
     */
    private void showPaidAmountFields() {
        if (vboxPaidAmount != null) {
            vboxPaidAmount.setVisible(true);
            vboxPaidAmount.setManaged(true);
        }
        if (hboxChange != null) {
            hboxChange.setVisible(true);
            hboxChange.setManaged(true);
        }
    }    /**
     * Oculta los campos de monto pagado y vuelto (para tarjeta de crédito/débito)
     */
    private void hidePaidAmountFields() {
        if (vboxPaidAmount != null) {
            vboxPaidAmount.setVisible(false);
            vboxPaidAmount.setManaged(false);
        }
        if (hboxChange != null) {
            hboxChange.setVisible(false);
            hboxChange.setManaged(false);
        }
        // Limpiar los campos cuando se ocultan
        if (tfPaidAmount != null) {
            tfPaidAmount.clear();
        }
        if (lblChange != null) {
            lblChange.setText("-");
        }
    }

    /**
     * Muestra el campo de número de tarjeta
     */
    private void showCardNumberField() {
        vboxCardNumber.setVisible(true);
        vboxCardNumber.setManaged(true);
    }

    /**
     * Oculta el campo de número de tarjeta
     */
    private void hideCardNumberField() {        vboxCardNumber.setVisible(false);
        vboxCardNumber.setManaged(false);
        tfCardNumber.clear(); // Limpiar el campo cuando se oculta
    }

    /**
     * Valida el número de tarjeta para débito y crédito
     */
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        // Validación básica: solo números y longitud entre 13-19 dígitos
        String cleanCard = cardNumber.replaceAll("\\s+", "");
        return cleanCard.matches("\\d{13,19}");
    }

    /**
     * Crea el medio de pago con número de tarjeta si es necesario
     */
    private MediosPago createPaymentMethod() {
        if (rbCash.isSelected()) {
            return new MediosPago(new EstrategiaPagoContado());
        } else if (rbDebit.isSelected()) {
            String cardNumber = tfCardNumber.getText().trim();
            if (cardNumber.isEmpty()) {
                return new MediosPago(new EstrategiaPagoDebito());
            } else {
                return new MediosPago(new EstrategiaPagoDebito(), cardNumber);
            }
        } else if (rbCredit.isSelected()) {
            String cardNumber = tfCardNumber.getText().trim();
            if (cardNumber.isEmpty()) {
                return new MediosPago(new EstrategiaPagoCredito());
            } else {
                return new MediosPago(new EstrategiaPagoCredito(), cardNumber);
            }
        }
        return new MediosPago(new EstrategiaPagoContado());
    }
}
