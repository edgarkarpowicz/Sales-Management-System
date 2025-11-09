/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import enums.Genero;
import java.util.List;

/**
 *
 * @author edgar
 */
public class Usuario extends Persona {
    
    private String login;
    private String password;
    private String fechaUltAcceso;

    public Usuario(String nombre, String apellido, String numDocumento, String tipoDocumento, String cuit, String condicionAfip, Genero genero, String fechaNacimiento, Domicilio domicilio, String email, List<String> telefonos) {
        super(nombre, apellido, numDocumento, tipoDocumento, cuit, condicionAfip, genero, fechaNacimiento, domicilio, email, telefonos);
    }
    
    public String getCargo() {
        return null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaUltAcceso() {
        return fechaUltAcceso;
    }

    public void setFechaUltAcceso(String fechaUltAcceso) {
        this.fechaUltAcceso = fechaUltAcceso;
    }
    
}
