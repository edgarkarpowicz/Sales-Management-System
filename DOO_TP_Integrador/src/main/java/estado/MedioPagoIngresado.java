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
public class MedioPagoIngresado implements EstadosVenta {
    private Venta venta;

    public MedioPagoIngresado(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        throw new IllegalStateException("No se puede agregar producto una vez ingresado el medio de pago.");
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        return venta.comprobarSiCorrespondeRecargo();
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        return venta.comprobarSiCorrespondeDescuento();
    }

    @Override
    public float calcularTotalCorrespondiente() {
        return venta.calcularTotalConDescuentoYRecargo();
    }

    @Override
    public void calcularVuelto() {
        throw new IllegalStateException("No se puede calcular vuelto antes de pagar.");
    }

    @Override
    public void generarFactura() {
        throw new IllegalStateException("No se puede generar factura antes de pagar.");
    }

    @Override
    public boolean pagarVenta() {
        venta.cambiarEstado(new Pago(venta));
        return true;
    }

    @Override
    public void cancelarVenta() {
        venta.cambiarEstado(new RechazadaOCancelada(venta));
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("No se puede finalizar sin pagar.");
    }

    @Override
    public void ingresarMedioPago(MediosPago m) {
        throw new IllegalStateException("Ya hay un Medio de Pago ingresado");
    }
    
    @Override
    public String returnName() {
        return "MedioPagoIngresado";
    }
}

