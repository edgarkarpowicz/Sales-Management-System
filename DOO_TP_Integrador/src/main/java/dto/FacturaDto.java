/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author edgar
 */
public class FacturaDto {
    private int idFactura;
    private String fechaEmitida;
    private float totalFactura;

    public FacturaDto(int idFactura, String fechaEmitida, float totalFactura) {
        this.idFactura = idFactura;
        this.fechaEmitida = fechaEmitida;
        this.totalFactura = totalFactura;
    }

    public FacturaDto(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getFechaEmitida() {
        return fechaEmitida;
    }

    public void setFechaEmitida(String fechaEmitida) {
        this.fechaEmitida = fechaEmitida;
    }

    public float getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(float totalFactura) {
        this.totalFactura = totalFactura;
    }
    
}
