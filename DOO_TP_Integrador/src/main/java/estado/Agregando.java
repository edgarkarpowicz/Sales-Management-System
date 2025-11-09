/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estado;

import modelos.DetallesVenta;
import modelos.MediosPago;
import modelos.Producto;
import modelos.Venta;

/**
 *
 * @author edgar
 */
public class Agregando implements EstadosVenta {
    private Venta venta;

    public Agregando(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        venta.agregarDetalleVenta(new DetallesVenta(prod, cant));
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        throw new IllegalStateException("No se puede comprobar en estado Agregando.");
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        throw new IllegalStateException("No se puede comprobar en estado Agregando.");
    }

    @Override
    public float calcularTotalCorrespondiente() {
        throw new IllegalStateException("No se puede calcular total en estado Agregando.");
    }

    @Override
    public void calcularVuelto() {
        throw new IllegalStateException("No se puede calcular vuelto en estado Agregando.");
    }

    @Override
    public void generarFactura() {
        throw new IllegalStateException("No se puede generar factura en estado Agregando.");
    }

    @Override
    public boolean pagarVenta() {
        throw new IllegalStateException("No se puede pagar en estado Agregando.");
    }

    @Override
    public void cancelarVenta() {
        venta.cambiarEstado(new RechazadaOCancelada(venta));
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("No se puede finalizar en estado Agregando.");
    }    @Override
    public void ingresarMedioPago(MediosPago m) {
        venta.setMedioPago(m);
        venta.cambiarEstado(new MedioPagoIngresado(venta));
    }

    @Override
    public String returnName() {
        return "Agregando";
    }
}
