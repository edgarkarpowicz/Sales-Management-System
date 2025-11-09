package dto;

/**
 * DTO extendido que incluye datos del Cliente y de la Persona asociada
 * @author grupoLudeña
 */
public class ClienteDto {
    // Campos originales de Cliente
    private int idCliente;
    private int idPersona;
    
    // Campos adicionales de Persona para mostrar en la tabla
    private String nombre;
    private String apellido;
    private String numDocumento;
    private String tipoDocumento;
    private String email;

    // Constructores
    public ClienteDto() {}

    public ClienteDto(int idCliente, int idPersona) {
        this.idCliente = idCliente;
        this.idPersona = idPersona;
    }
    
    // Constructor completo con datos de Persona
    public ClienteDto(int idCliente, int idPersona, String nombre, String apellido, 
                     String numDocumento, String tipoDocumento, String email) {
        this.idCliente = idCliente;
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numDocumento = numDocumento;
        this.tipoDocumento = tipoDocumento;
        this.email = email;
    }
    
    // Constructor para búsqueda por ID
    public ClienteDto(int idCliente) {
        this.idCliente = idCliente;
    }

    // Getters y Setters originales
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    // Getters y Setters de Persona
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNumDocumento() { return numDocumento; }
    public void setNumDocumento(String numDocumento) { this.numDocumento = numDocumento; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "ClienteDto{" +
                "idCliente=" + idCliente +
                ", idPersona=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", numDocumento='" + numDocumento + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}