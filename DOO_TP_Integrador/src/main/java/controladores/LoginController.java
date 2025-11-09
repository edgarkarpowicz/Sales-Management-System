/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ubp.doo.doo_tp_integrador.App; // Asegúrate de tener esta importación
import java.io.IOException;       // Asegúrate de tener esta importación
import dto.UsuarioDto;
import servicios.UsuarioService;

/**
 *
 * @author grupoLudeña
 */

public class LoginController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    private UsuarioService usuarioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error de Validación", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return;
        }

        UsuarioDto authenticatedUser = usuarioService.autenticarUsuario(username, password);

        if (authenticatedUser != null) {
            // Ya no solo mostramos la alerta, sino que navegamos
            mostrarAlerta("Login correcto", "Bienvenido, " + authenticatedUser.getLogin() + "!");
            App.setCurrentUser(authenticatedUser); 
            
            try {
                // Verificar si es Cajero o Administrativo
                if (usuarioService.esCajero(authenticatedUser.getIdUsuario())) {
                    App.setRoot("cashierRoot");
                } else if (usuarioService.esAdministrativo(authenticatedUser.getIdUsuario())) {
                    App.setRoot("adminRoot");
                } else {
                    mostrarAlerta("Rol no soportado", "El usuario no tiene un rol válido para acceder al sistema.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error de Navegación", "No se pudo cargar la pantalla correspondiente.\n" + e.getMessage());
            }
        } else {
            mostrarAlerta("Login incorrecto", "Usuario o contraseña inválidos.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        tfUsername.clear();
        pfPassword.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}