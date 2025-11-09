package controladores;

import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import modelos.Factura;
import modelos.Venta;
import modelos.DetallesVenta; // si quisieras mostrar el detalle
import fachada.FachadaSistemaVentas;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import servicios.VentaService;

import ubp.doo.doo_tp_integrador.App; 

public class CashierConfirmController implements Initializable {

    @FXML private Label lblFechaEmitida;
    @FXML private Label lblTotalFactura;

    @FXML private TableView<DetallesVenta> tblDetalle;          // Opcional, sólo si quieres listar detalles
    @FXML private TableColumn<DetallesVenta, String> colProducto; // Ejemplo de columna
    @FXML private TableColumn<DetallesVenta, Integer> colCantidad; 
    @FXML private TableColumn<DetallesVenta, Float> colPrecio;    

    @FXML private Button btnFinish;
    @FXML private Button btnBackToMenu;

    private Factura factura;
    private Venta venta; // si quieres mostrar datos de DetalleVenta
    
    private final VentaService ventaService = new VentaService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Aquí podrías inicializar la TableView de DetalleVenta si la usas:
        if (tblDetalle != null) {
            colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
            colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        }
        
        // Check if we need to initialize with navigation data
        if (CashierNavigationData.hasConfirmDataForRestoration()) {
            try {
                FachadaSistemaVentas fachada = CashierNavigationData.getFachadaForConfirmation();
                Factura factura = CashierNavigationData.getFacturaForConfirmation();
                Venta venta = CashierNavigationData.getVentaForConfirmation();
                
                initData(fachada, factura, venta);
                
                // Clear navigation data after initialization
                CashierNavigationData.clearConfirmData();
            } catch (SQLException e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los datos de confirmación.");
            }
        }
    }

    /**
     * Llamado desde CashierPaymentController para pasar la Factura y Venta.
     */
    public void initData(FachadaSistemaVentas fachada, Factura factura, Venta venta) throws SQLException {
        this.factura = factura;
        this.venta   = venta;
        venta.setFactura(factura);

        // 2) Muestro la fecha formateada
        String fecha = factura.getFechaEmitida();
        lblFechaEmitida.setText(fecha);

        // 3) Muestro el total de la factura
        lblTotalFactura.setText(new java.text.DecimalFormat("$#,##0.00")
                                .format(factura.getTotalFactura()));

        // 4) Si quieres mostrar detalles de venta, llenas la tabla:
        if (tblDetalle != null) {
            List<DetallesVenta> listaDetalle = venta.getDetalles();
            tblDetalle.getItems().setAll(listaDetalle);
        }
        
        try {
            ventaService.insertarVenta(venta);
        } catch (Exception ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error DB", "No se pudo subir la Venta a la Base de Datos");
        }
    }
    
    private void mostrarAlerta(Alert.AlertType alertType, String titulo, String mensaje) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void finish(ActionEvent event) throws IOException {
        // Volvemos al menú principal usando navegación estática para preservar el MenuBar
        try{
            CashierNavigationData.clearAll();
            CashierRootController.navigateToMenu();
        }catch (Exception e) {
            e.printStackTrace();
            // Opcional: mostrar alerta de error
        }
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        // Regresa a la pantalla principal del cajero usando navegación estática para preservar el MenuBar
        try {
            CashierNavigationData.clearAll();
            CashierRootController.navigateToMenu();
        } catch (Exception e) {
            e.printStackTrace();
            // Opcional: mostrar alerta de error
        }
    }
}