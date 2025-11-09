/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author grupoLudeña
 */

public class CajeroDto {
    private int idCajero;
    private int idUsuario; // Foreign key to Usuario table

    public CajeroDto() {}

    public CajeroDto(int idCajero, int idUsuario) {
        this.idCajero = idCajero;
        this.idUsuario = idUsuario;
    }

    // Constructor to use when searching by ID
    public CajeroDto(int idCajero) {
        this.idCajero = idCajero;
    }
    
    // Getters and Setters (as in your provided CajeroDto.java)
    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() { // As in your provided CajeroDto.java
        return "CajeroDto{" +
                "idCajero=" + idCajero +
                ", idUsuario=" + idUsuario +
                '}';
    }
}