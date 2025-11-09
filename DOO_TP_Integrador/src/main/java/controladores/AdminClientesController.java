package controladores;

import dto.ClienteDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import servicios.AdminService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty; // Importar
import javafx.beans.property.SimpleStringProperty;  // Importar

/**
 * Controlador para adminClientes.fxml.
 * Lista todos los clientes registrados y permite gestionar clientes.
 */
public class AdminClientesController implements Initializable {

    @FXML private TableView<ClienteDto> tblClientes;
    @FXML private TableColumn<ClienteDto, Integer> colId;
    @FXML private TableColumn<ClienteDto, String> colDocumento;
    @FXML private TableColumn<ClienteDto, String> colNombre;
    @FXML private TableColumn<ClienteDto, String> colApellido;

    // ObservableList que contendrá los datos de ClienteDto para la tabla
    private final ObservableList<ClienteDto> listaClientes = FXCollections.observableArrayList();

    private final AdminService adminService = new AdminService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== Inicializando AdminClientesController ===");
        System.out.println("colId es null? " + (colId == null));
        System.out.println("colNombre es null? " + (colNombre == null));
        System.out.println("colDocumento es null? " + (colDocumento == null));
        System.out.println("colApellido es null? " + (colApellido == null));
        System.out.println("tblClientes es null? " + (tblClientes == null));
        
        // 1) Configuro las columnas para que lean las propiedades del DTO
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdCliente()).asObject());
        colDocumento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumDocumento()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));
        
        // Si tienes la columna de email en el FXML, descomenta esta línea:
        // colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // 2) Asigno la lista observable a la tabla
        tblClientes.setItems(listaClientes);

        // 3) Cargo los datos desde la base de datos a la tabla
        cargarClientesEnTabla();
        
        System.out.println("=== Inicialización completada ===");
    }

    /**
     * Llama al servicio para obtener todos los clientes y los asigna al TableView.
     */
    private void cargarClientesEnTabla() {
        System.out.println("=== Iniciando carga de clientes ===");
        
        try {
            // Llamar al servicio
            List<ClienteDto> clientes = adminService.listarClientes();
            
            System.out.println("Clientes obtenidos del servicio: " + clientes.size());
            
            // Debug: Mostrar información de los primeros clientes
            for (int i = 0; i < Math.min(3, clientes.size()); i++) {
                ClienteDto cliente = clientes.get(i);
                System.out.println("Cliente " + (i+1) + ": " + cliente.toString());
            }
            
            // Limpiar y agregar los nuevos datos
            listaClientes.clear();
            listaClientes.addAll(clientes);
            
            System.out.println("Items en listaClientes: " + listaClientes.size());
            System.out.println("Items en tabla: " + tblClientes.getItems().size());
            
            // Refrescar la tabla
            tblClientes.refresh();
            
            // Verificar que la tabla tenga los datos
            if (tblClientes.getItems().isEmpty()) {
                System.err.println("¡ADVERTENCIA! La tabla está vacía después de cargar los datos");
            } else {
                System.out.println("Tabla cargada exitosamente con " + tblClientes.getItems().size() + " elementos");
            }
            
        } catch (SQLException e) {
            System.err.println("Error SQL al cargar clientes: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaError("Error de Base de Datos", 
                "No se pudieron cargar los clientes desde la base de datos.\n" +
                "Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al cargar clientes: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaError("Error Inesperado", 
                "Ocurrió un error inesperado al cargar los clientes.\n" +
                "Error: " + e.getMessage());
        }
        
        System.out.println("=== Carga de clientes finalizada ===");
    }

    /**
     * Maneja el clic en "Agregar": abre ventana para agregar nuevo cliente.
     */
    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            // Set add mode and use static navigation to preserve MenuBar
            AdminNavigationData.setAddMode();
            AdminRootController.navigateToClientesForm();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo abrir el formulario de nuevo cliente.\n" + e.getMessage());
        }
    }

    /** ------------------------- EDIT y DELETE ya los tenías… ------------------------- */

    /** Método para “Volver” al menú principal (como ya lo tenías): */    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Use static navigation to preserve MenuBar and return to main menu
            AdminNavigationData.clearAll();
            AdminRootController.navigateToMenu();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo regresar al menú principal.\n" + e.getMessage());
        }
    }

    /** (Opcional) Si quieres un reload manual desde un botón “Recargar”: */
    @FXML
    private void handleReload(ActionEvent event) {
        cargarClientesEnTabla();
    }
    
    @FXML
    private void handleDelete(ActionEvent event) {
        cargarClientesEnTabla();
    }

    /**
     * MÉTODO LEGACY: Mantener compatibilidad con código existente
     * Maneja el clic en "Volver al Menú Principal": regresa a adminRoot.fxml.
     */
    @FXML
    private void onClickVolver(ActionEvent event) {
        handleBack(event);
    }

    /**
     * Actualiza la tabla recargando los datos desde la base de datos.
     */
    private void actualizarTabla() {
        cargarClientesEnTabla();
    }

    /**
     * Muestra una alerta de error.
     */
    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de información.
     */
    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de advertencia.
     */
    private void mostrarAlertaWarning(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Método para testing - verifica el estado de la tabla
     */
    public void debugTableState() {
        System.out.println("=== DEBUG: Estado de la tabla ===");
        System.out.println("Tabla nula: " + (tblClientes == null));
        System.out.println("Items en tabla: " + (tblClientes != null ? tblClientes.getItems().size() : "N/A"));
        System.out.println("Lista observable nula: " + (listaClientes == null));
        System.out.println("Items en lista observable: " + (listaClientes != null ? listaClientes.size() : "N/A"));
        System.out.println("Columnas configuradas: " + (tblClientes != null ? tblClientes.getColumns().size() : "N/A"));
        System.out.println("=== FIN DEBUG ===");
    }
}