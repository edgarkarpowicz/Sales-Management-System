package controladores;

import dto.PersonaDto;
import dto.ClienteDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import servicios.AdminService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AdminClientesFormController implements Initializable {

    @FXML private TextField tfName;
    @FXML private ComboBox<String> cbDocType;
    @FXML private TextField tfDocNumber;
    @FXML private TextField tfCuit;
    @FXML private ComboBox<String> cbAfip;
    @FXML private ComboBox<String> cbGender;
    @FXML private DatePicker dpBirthdate;
    @FXML private TextField tfAddress;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPhone;

    private final AdminService adminService = new AdminService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== INICIALIZANDO CONTROLADOR ===");
        
        try {
            // Configurar ComboBox con valores
            ObservableList<String> tiposDoc = FXCollections.observableArrayList("DNI", "PASAPORTE", "LC", "LE", "CUIL", "CUIT");
            cbDocType.setItems(tiposDoc);
            cbDocType.setValue("DNI");
            System.out.println("✓ ComboBox tipos documento configurado");

            ObservableList<String> condicionesAfip = FXCollections.observableArrayList(
                "Responsable Inscripto", "Monotributista", "Exento", "Consumidor Final"
            );
            cbAfip.setItems(condicionesAfip);
            cbAfip.setValue("Consumidor Final");
            System.out.println("✓ ComboBox AFIP configurado");

            ObservableList<String> generos = FXCollections.observableArrayList("Masculino", "Femenino", "Otro");
            cbGender.setItems(generos);
            System.out.println("✓ ComboBox género configurado");

            // Configurar DatePicker
            dpBirthdate.setPromptText("dd/MM/yyyy");
            dpBirthdate.setConverter(new javafx.util.StringConverter<LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? formatter.format(date) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        try {
                            return LocalDate.parse(string, formatter);
                        } catch (DateTimeParseException e) {
                            System.err.println("Error parsing date: " + string);
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            });
            System.out.println("✓ DatePicker configurado");
            System.out.println("=== CONTROLADOR INICIALIZADO CORRECTAMENTE ===");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR EN INITIALIZE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        System.out.println("=== CANCELANDO ===");
        try {
            // Use static navigation to preserve MenuBar and return to clients list
            AdminNavigationData.clearAll();
            AdminRootController.navigateToClientes();
        } catch (Exception e) {
            System.err.println("❌ ERROR AL CANCELAR: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo regresar a la lista de clientes.\n" + e.getMessage());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        System.out.println("\n=== INICIANDO GUARDADO DE CLIENTE ===");
        
        try {
            // 1. Mostrar datos ingresados para debug
            mostrarDatosIngresados();
            
            // 2. Validar campos
            System.out.println("Paso 1: Validando campos...");
            if (!validarCampos()) {
                System.out.println("❌ Validación fallida");
                return;
            }
            System.out.println("✓ Validación exitosa");

            // 3. Procesar nombre
            System.out.println("Paso 2: Procesando nombre...");
            String nombreCompleto = tfName.getText().trim();
            String[] partes = nombreCompleto.split("\\s+", 2);
            String nombre = partes[0];
            String apellido = (partes.length > 1) ? partes[1] : "";
            System.out.println("Nombre: '" + nombre + "', Apellido: '" + apellido + "'");

            // 4. Validar documento único
            System.out.println("Paso 3: Validando documento único...");
            if (!validarDocumentoUnico()) {
                System.out.println("❌ Documento duplicado");
                return;
            }
            System.out.println("✓ Documento único");

            // 5. Crear PersonaDto
            System.out.println("Paso 4: Creando PersonaDto...");
            PersonaDto persona = crearPersonaDto(nombre, apellido);
            System.out.println("PersonaDto creado: " + persona.toString());

            // 6. Insertar cliente
            System.out.println("Paso 5: Insertando en base de datos...");
            ClienteDto nuevo = adminService.insertarCliente(persona);
            
            if (nuevo == null) {
                System.out.println("❌ AdminService.insertarCliente devolvió null");
                mostrarAlertaError("Error al Guardar", "No se pudo insertar el cliente en la base de datos.");
                return;
            }

            System.out.println("✓ Cliente insertado con ID: " + nuevo.getIdCliente());

            // 7. Mostrar éxito
            mostrarMensajeExito(nuevo.getIdCliente());

            // 8. Volver al listado
            System.out.println("Paso 6: Volviendo al listado...");
            volverAlListado(event);
            
            System.out.println("=== GUARDADO COMPLETADO EXITOSAMENTE ===\n");

        } catch (SQLException ex) {
            System.err.println("❌ ERROR SQL: " + ex.getMessage());
            System.err.println("SQL State: " + ex.getSQLState());
            System.err.println("Error Code: " + ex.getErrorCode());
            ex.printStackTrace();
            mostrarAlertaError("Error de Base de Datos", 
                "Error SQL: " + ex.getMessage() + 
                "\nSQL State: " + ex.getSQLState() +
                "\nError Code: " + ex.getErrorCode());
                
        } catch (Exception ex) {
            System.err.println("❌ ERROR GENERAL: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            ex.printStackTrace();
            mostrarAlertaError("Error Inesperado", 
                "Tipo: " + ex.getClass().getSimpleName() + 
                "\nMensaje: " + ex.getMessage());
        }
    }

    private void mostrarDatosIngresados() {
        System.out.println("--- DATOS INGRESADOS ---");
        System.out.println("Nombre: '" + (tfName.getText() != null ? tfName.getText() : "NULL") + "'");
        System.out.println("Tipo Doc: '" + cbDocType.getValue() + "'");
        System.out.println("Nro Doc: '" + (tfDocNumber.getText() != null ? tfDocNumber.getText() : "NULL") + "'");
        System.out.println("Email: '" + (tfEmail.getText() != null ? tfEmail.getText() : "NULL") + "'");
        System.out.println("CUIT: '" + (tfCuit.getText() != null ? tfCuit.getText() : "NULL") + "'");
        System.out.println("AFIP: '" + cbAfip.getValue() + "'");
        System.out.println("Género: '" + cbGender.getValue() + "'");
        System.out.println("Fecha: " + dpBirthdate.getValue());
        System.out.println("Dirección: '" + (tfAddress.getText() != null ? tfAddress.getText() : "NULL") + "'");
        System.out.println("Teléfono: '" + (tfPhone.getText() != null ? tfPhone.getText() : "NULL") + "'");
        System.out.println("------------------------");
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        // Validar campos obligatorios
        if (tfName == null || tfName.getText().trim().isEmpty()) {
            errores.append("- Nombre y apellido es obligatorio\n");
        }
        
        if (cbDocType.getValue() == null || cbDocType.getValue().isEmpty()) {
            errores.append("- Tipo de documento es obligatorio\n");
        }
        
        if (tfDocNumber == null || tfDocNumber.getText().trim().isEmpty()) {
            errores.append("- Número de documento es obligatorio\n");
        }
        
        if (tfEmail == null || tfEmail.getText().trim().isEmpty()) {
            errores.append("- Email es obligatorio\n");
        } else if (!esEmailValido(tfEmail.getText().trim())) {
            errores.append("- El formato del email no es válido\n");
        }

        // Validar CUIT si está presente
        if (tfCuit != null && !tfCuit.getText().trim().isEmpty()) {
            String cuit = tfCuit.getText().trim();
            if (!esCuitValido(cuit)) {
                errores.append("- El formato del CUIT no es válido (debe tener 11 dígitos)\n");
            }
        }

        if (errores.length() > 0) {
            System.out.println("❌ Errores de validación:\n" + errores.toString());
            mostrarAlertaError("Datos incompletos o incorrectos", errores.toString());
            return false;
        }

        return true;
    }

    private boolean validarDocumentoUnico() {
        try {
            String tipoDoc = cbDocType.getValue();
            String nroDoc = tfDocNumber.getText().trim();
            
            System.out.println("Validando documento: " + tipoDoc + " " + nroDoc);
            ClienteDto existente = adminService.buscarClientePorDocumento(tipoDoc, nroDoc);
            
            if (existente != null) {
                System.out.println("❌ Documento duplicado encontrado: " + existente.toString());
                mostrarAlertaError("Documento Duplicado", 
                    "Ya existe un cliente con el documento " + tipoDoc + " " + nroDoc);
                return false;
            }
            System.out.println("✓ Documento único verificado");
            
        } catch (SQLException e) {
            System.err.println("⚠️ Error validando documento: " + e.getMessage());
            // Continuar con la inserción aunque falle la validación
        }
        return true;
    }

    private PersonaDto crearPersonaDto(String nombre, String apellido) {
        PersonaDto persona = new PersonaDto();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTipoDocumento(cbDocType.getValue());
        persona.setNumDocumento(tfDocNumber.getText().trim());
        persona.setEmail(tfEmail.getText().trim());
        
        // Campos opcionales con verificación null
        if (tfCuit != null && !tfCuit.getText().trim().isEmpty()) {
            persona.setCuit(tfCuit.getText().trim());
        }
        
        if (cbAfip.getValue() != null) {
            persona.setCondicionAfip(cbAfip.getValue());
        }
        
        if (cbGender.getValue() != null) {
            persona.setGenero(cbGender.getValue());
        }
        
        // Fecha de nacimiento
        LocalDate fecha = dpBirthdate.getValue();
        if (fecha != null) {
            persona.setFechaNacimiento(fecha.toString());
            System.out.println("Fecha establecida: " + fecha.toString());
        }
        
        // Domicilio por ahora lo dejamos en 0
        persona.setIdDomicilio(0);
        
        return persona;
    }

    private boolean esEmailValido(String email) {
        boolean valido = email.contains("@") && email.contains(".") && email.length() > 5;
        System.out.println("Email '" + email + "' es válido: " + valido);
        return valido;
    }

    private boolean esCuitValido(String cuit) {
        boolean valido = cuit.matches("\\d{11}") || cuit.matches("\\d{2}-\\d{8}-\\d{1}");
        System.out.println("CUIT '" + cuit + "' es válido: " + valido);
        return valido;
    }

    private void mostrarMensajeExito(int idCliente) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText("Cliente creado correctamente");
        alert.setContentText("El cliente ha sido creado con ID: " + idCliente);
        alert.showAndWait();
    }

    private void volverAlListado(ActionEvent event) {
        try {
            // Use static navigation to preserve MenuBar and return to clients list
            AdminNavigationData.clearAll();
            AdminRootController.navigateToClientes();
        } catch (Exception ex) {
            System.err.println("❌ ERROR volviendo al listado: " + ex.getMessage());
            ex.printStackTrace();
            mostrarAlertaError("Error", "No se pudo regresar al listado de clientes.\n" + ex.getMessage());
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        System.out.println("=== VOLVIENDO AL MENÚ PRINCIPAL ===");
        try {
            // Use static navigation to preserve MenuBar and return to main menu
            AdminNavigationData.clearAll();
            AdminRootController.navigateToMenu();
        } catch (Exception e) {
            System.err.println("❌ ERROR al volver al menú: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaError("Error", "No se pudo regresar al menú principal.\n" + e.getMessage());
        }
    }

    /**
     * Initialize form with client data for editing
     */
    public void initDataForEdit(ClienteDto cliente) {
        // This method can be implemented when edit functionality is needed
        // For now, it's just a placeholder to avoid compilation errors
        System.out.println("Edit mode initialized for client: " + cliente.getIdCliente());
        // TODO: Populate form fields with client data
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}