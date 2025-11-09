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
public class Persona {

    private String nombre;
    private String apellido;
    private String numDocumento;
    private String tipoDocumento;
    private String cuit;
    private String condicionAfip;
    private Genero genero;
    private String fechaNacimiento;
    private Domicilio domicilio;
    private String email;
    private List<String> telefonos;

    public Persona() {
    }

    public Persona(String nombre, String apellido, String numDocumento, String tipoDocumento, String cuit, String condicionAfip, Genero genero, String fechaNacimiento, Domicilio domicilio, String email, List<String> telefonos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numDocumento = numDocumento;
        this.tipoDocumento = tipoDocumento;
        this.cuit = cuit;
        this.condicionAfip = condicionAfip;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.domicilio = domicilio;
        this.email = email;
        this.telefonos = telefonos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getCondicionAfip() {
        return condicionAfip;
    }

    public void setCondicionAfip(String condicionAfip) {
        this.condicionAfip = condicionAfip;
    }

    public Genero getGenero() {
        return genero;
    }

    public String getGeneroStr() {
        return genero.name();
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void setGenero(String genero) throws Exception {
        try {
            this.genero = Genero.valueOf(genero);
        } catch (Exception ex) {
            throw new Exception("No se pudo convertir el String a un Valor Valido del Enum Genero");
        }
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }
}
