/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author grupoLudeña
 */

public class PersonaDto {

    private int idPersona;
    private int idDomicilio; // Foreign key to Domicilio table
    private String nombre;
    private String apellido;
    private String numDocumento;
    private String tipoDocumento;
    private String cuit;
    private String condicionAfip;
    private String genero; // Consider storing as String or mapping to an enum if needed
    private String fechaNacimiento;
    private String email;
    // private List<String> telefonos; // For simplicity, TelefonosAsignados table handles this relationship.

    public PersonaDto() {}

    public PersonaDto(int idPersona, int idDomicilio, String nombre, String apellido,
                      String numDocumento, String tipoDocumento, String cuit,
                      String condicionAfip, String genero, String fechaNacimiento, String email) {
        this.idPersona = idPersona;
        this.idDomicilio = idDomicilio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numDocumento = numDocumento;
        this.tipoDocumento = tipoDocumento;
        this.cuit = cuit;
        this.condicionAfip = condicionAfip;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
    }
    
    // Constructor to use when searching by ID
    public PersonaDto(int idPersona) {
        this.idPersona = idPersona;
    }


    // Getters and Setters (as in your provided PersonaDto.java)
    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public int getIdDomicilio() { return idDomicilio; }
    public void setIdDomicilio(int idDomicilio) { this.idDomicilio = idDomicilio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNumDocumento() { return numDocumento; }
    public void setNumDocumento(String numDocumento) { this.numDocumento = numDocumento; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getCondicionAfip() { return condicionAfip; }
    public void setCondicionAfip(String condicionAfip) { this.condicionAfip = condicionAfip; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() { // As in your provided PersonaDto.java
        return "PersonaDto{" +
                "idPersona=" + idPersona +
                ", idDomicilio=" + idDomicilio +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", numDocumento='" + numDocumento + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", cuit='" + cuit + '\'' +
                ", condicionAfip='" + condicionAfip + '\'' +
                ", genero='" + genero + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}