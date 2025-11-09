/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author edgar
 */
public class Factura {
    private String fechaEmitida;
    private float totalFactura;

    public Factura(String fechaEmitida, float totalFactura) {
        this.fechaEmitida = fechaEmitida;
        this.totalFactura = totalFactura;
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
