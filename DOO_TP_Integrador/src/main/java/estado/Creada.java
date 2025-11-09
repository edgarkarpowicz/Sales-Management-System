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
public class Creada implements EstadosVenta {
    private Venta venta;

    public Creada(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        venta.agregarDetalleVenta(new DetallesVenta(prod, cant));
        venta.cambiarEstado(new Agregando(venta));
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        throw new IllegalStateException("No se puede comprobar en estado Creada.");
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        throw new IllegalStateException("No se puede comprobar en estado Creada.");
    }

    @Override
    public float calcularTotalCorrespondiente() {
        throw new IllegalStateException("No se puede calcular total en estado Creada.");
    }

    @Override
    public void calcularVuelto() {
        throw new IllegalStateException("No se puede calcular vuelto en estado Creada.");
    }

    @Override
    public void generarFactura() {
        throw new IllegalStateException("No se puede generar factura en estado Creada.");
    }

    @Override
    public boolean pagarVenta() {
        throw new IllegalStateException("No se puede pagar en estado Creada.");
    }

    @Override
    public void cancelarVenta() {
        venta.cambiarEstado(new RechazadaOCancelada(venta));
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("No se puede finalizar en estado Creada.");
    }

    @Override
    public void ingresarMedioPago(MediosPago m) {
        throw new IllegalStateException("No se puede ingresar Medio de Pago en estado Creada.");
    }
    
    @Override
    public String returnName() {
        return "Creada";
    }
}