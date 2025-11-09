package controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización del menú principal de administrador
    }

    @FXML
    private void onClickClientes(ActionEvent event) {
        try {
            AdminRootController.navigateToClientes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickProductos(ActionEvent event) {
        mostrarAlertaProximamente();
    }

    @FXML
    private void onClickCajeros(ActionEvent event) {
        mostrarAlertaProximamente();
    }

    @FXML
    private void onClickDescuentos(ActionEvent event) {
        mostrarAlertaProximamente();
    }

    @FXML
    private void onClickInformes(ActionEvent event) {
        mostrarAlertaProximamente();
    }

    @FXML
    private void onClickDashboard(ActionEvent event) {
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
