/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author grupoLudeña
 */

public class UsuarioDto {
    private int idUsuario;
    private int idPersona; // Foreign key to Persona table
    private String login;
    private String password;
    private String fechaUltAcceso;

    public UsuarioDto() {}

    public UsuarioDto(int idUsuario, int idPersona, String login, String password, String fechaUltAcceso) {
        this.idUsuario = idUsuario;
        this.idPersona = idPersona;
        this.login = login;
        this.password = password;
        this.fechaUltAcceso = fechaUltAcceso;
    }
    
    // Constructor to use when searching by ID
    public UsuarioDto(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getters and Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFechaUltAcceso() { return fechaUltAcceso; }
    public void setFechaUltAcceso(String fechaUltAcceso) { this.fechaUltAcceso = fechaUltAcceso; }

    @Override
    public String toString() {
        return "UsuarioDto{" +
                "idUsuario=" + idUsuario +
                ", idPersona=" + idPersona +
                ", login='" + login + '\'' +
                // Password typically not included in toString
                ", fechaUltAcceso='" + fechaUltAcceso + '\'' +
                '}';
    }
}
