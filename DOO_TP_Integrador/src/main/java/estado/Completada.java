/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estado;

import modelos.MediosPago;
import modelos.Producto;
import modelos.Venta;

/**
 *
 * @author edgar
 */
public class Completada implements EstadosVenta {

    private Venta venta;

    public Completada(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public float calcularTotalCorrespondiente() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public void calcularVuelto() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public void generarFactura() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public boolean pagarVenta() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public void cancelarVenta() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("Venta ya completada.");
    }

    @Override
    public void ingresarMedioPago(MediosPago m) {
        throw new IllegalStateException("Venta ya completada.");
    }
    
    @Override
    public String returnName() {
        return "Completada";
    }
}
