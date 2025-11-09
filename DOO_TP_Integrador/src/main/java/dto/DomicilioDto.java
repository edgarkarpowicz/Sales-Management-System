/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author grupoLudeña
 */

public class DomicilioDto {
    private int idDomicilio;
    private String nomCalle;
    private int numVivienda;
    private int numDepartamento;
    private int codPostal;
    private String localidad;
    private String departamento;
    private String pais;

    public DomicilioDto() {}

    public DomicilioDto(int idDomicilio, String nomCalle, int numVivienda, int numDepartamento, int codPostal, String localidad, String departamento, String pais) {
        this.idDomicilio = idDomicilio;
        this.nomCalle = nomCalle;
        this.numVivienda = numVivienda;
        this.numDepartamento = numDepartamento;
        this.codPostal = codPostal;
        this.localidad = localidad;
        this.departamento = departamento;
        this.pais = pais;
    }
    
    public DomicilioDto(int idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    // Getters and Setters
    public int getIdDomicilio() { return idDomicilio; }
    public void setIdDomicilio(int idDomicilio) { this.idDomicilio = idDomicilio; }

    public String getNomCalle() { return nomCalle; }
    public void setNomCalle(String nomCalle) { this.nomCalle = nomCalle; }

    public int getNumVivienda() { return numVivienda; }
    public void setNumVivienda(int numVivienda) { this.numVivienda = numVivienda; }

    public int getNumDepartamento() { return numDepartamento; }
    public void setNumDepartamento(int numDepartamento) { this.numDepartamento = numDepartamento; }

    public int getCodPostal() { return codPostal; }
    public void setCodPostal(int codPostal) { this.codPostal = codPostal; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    @Override
    public String toString() {
        return "DomicilioDto{" +
                "idDomicilio=" + idDomicilio +
                ", nomCalle='" + nomCalle + '\'' +
                ", numVivienda=" + numVivienda +
                // ... other fields
                '}';
    }
}
