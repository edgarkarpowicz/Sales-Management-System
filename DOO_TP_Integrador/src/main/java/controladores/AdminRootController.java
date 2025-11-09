package controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminRootController implements Initializable {
    @FXML private StackPane contentPane;
    @FXML private Label lblAdminName;
    
    // Static reference to the current AdminRootController instance for navigation
    private static AdminRootController currentInstance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentInstance = this;
        
        // Load the main menu by default
        try {
            showMenuView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ubp.doo.doo_tp_integrador.App.setCurrentUser(null); // Limpia el usuario de la sesión
        try {
            ubp.doo.doo_tp_integrador.App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            // Podrías agregar una alerta de error aquí si quieres
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                "No se pudo volver a la pantalla de login.\n" + e.getMessage()
            );
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Static navigation methods to be called from other controllers
     */
    public static void navigateToMenu() {
        if (currentInstance != null) {
            currentInstance.showMenuView();
        }
    }
    
    public static void navigateToClientes() {
        if (currentInstance != null) {
            currentInstance.showClientesView();
        }
    }
    
    public static void navigateToClientesForm() {
        if (currentInstance != null) {
            currentInstance.showClientesFormView();
        }
    }
    
    public static void navigateToProductos() {
        if (currentInstance != null) {
            currentInstance.showProductosView();
        }
    }
    
    public static void navigateToCajeros() {
        if (currentInstance != null) {
            currentInstance.showCajerosView();
        }
    }
    
    public static void navigateToDescuentos() {
        if (currentInstance != null) {
            currentInstance.showDescuentosView();
        }
    }
    
    public static void navigateToInformes() {
        if (currentInstance != null) {
            currentInstance.showInformesView();
        }
    }
    
    public static void navigateToDashboard() {
        if (currentInstance != null) {
            currentInstance.showDashboardView();
        }
    }
    
    /**
     * Instance methods to load different views into contentPane
     */
    private void showMenuView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ubp/doo/doo_tp_integrador/adminMenu.fxml"));
            Parent menuView = loader.load();
            contentPane.getChildren().setAll(menuView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showClientesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ubp/doo/doo_tp_integrador/adminClientes.fxml"));
            Parent clientesView = loader.load();
            contentPane.getChildren().setAll(clientesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showClientesFormView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ubp/doo/doo_tp_integrador/adminClientesForm.fxml"));
            Parent formView = loader.load();
            
            if (loader.getController() instanceof AdminClientesFormController) {
                AdminClientesFormController formController = loader.getController();
                if (AdminNavigationData.hasClienteForEdit()) {
                    // Initialize with client data for editing
                    formController.initDataForEdit(AdminNavigationData.getClienteForEdit());
                    AdminNavigationData.clearAll();
                }
                // For add mode, no additional initialization needed
            }
            contentPane.getChildren().setAll(formView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showProductosView() {
        mostrarAlertaProximamente();
    }
    
    private void showCajerosView() {
        mostrarAlertaProximamente();
    }
    
    private void showDescuentosView() {
        mostrarAlertaProximamente();
    }
    
    private void showInformesView() {
        mostrarAlertaProximamente();
    }
    
    private void showDashboardView() {
        mostrarAlertaProximamente();
    }
    
    private void mostrarAlertaProximamente() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("Esta sección estará disponible próximamente.");
        alert.showAndWait();
    }
}
