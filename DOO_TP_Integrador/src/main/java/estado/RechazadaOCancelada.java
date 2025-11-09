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
public class RechazadaOCancelada implements EstadosVenta {

    private Venta venta;

    public RechazadaOCancelada(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public float calcularTotalCorrespondiente() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public void calcularVuelto() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public void generarFactura() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public boolean pagarVenta() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public void cancelarVenta() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }

    @Override
    public void ingresarMedioPago(MediosPago m) {
        throw new IllegalStateException("La Venta ya fue cancelada.");
    }
    
    @Override
    public String returnName() {
        return "RechazadaOCancelada";
    }
}
