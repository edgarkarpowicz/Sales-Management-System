/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import estado.Creada;
import estado.EstadosVenta;
import estado.MedioPagoIngresado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edgar
 */
public class Venta {

    private Cliente cliente;
    private Cajero cajero;
    private MediosPago medioPago;
    private List<DetallesVenta> detalles;
    private Descuento descuento;
    private EstadosVenta estadoVenta;
    private Factura factura;

    public Venta(Cliente cliente, Cajero cajero) {
        this.cliente = cliente;
        this.cajero = cajero;
        this.detalles = new ArrayList<>();
        this.estadoVenta = new Creada(this);  // Estado Inicial
    }

    public Venta(Cliente cliente, Cajero cajero, MediosPago medioPago, List<DetallesVenta> detalles, Descuento descuento, EstadosVenta estadoVenta) {
        this.cliente = cliente;
        this.cajero = cajero;
        this.medioPago = medioPago;
        this.detalles = detalles;
        this.descuento = descuento;
        this.estadoVenta = estadoVenta;
    }

    public Venta(Cliente cliente, Cajero cajero, MediosPago medioPago, List<DetallesVenta> detalles, Descuento descuento, EstadosVenta estadoVenta, Factura factura) {
        this(cliente, cajero, medioPago, detalles, descuento, estadoVenta);
        this.factura = factura;
    }

    // Funciones con Estado
    public void agregarProducto(Producto prod, int cant) {
        estadoVenta.agregarProducto(prod, cant);
    }

    public boolean comprobarSiCorrespondeRecargo() {
        return estadoVenta.comprobarSiCorrespondeRecargo();
    }

    public boolean comprobarSiCorrespondeDescuento() {
        return estadoVenta.comprobarSiCorrespondeDescuento();
    }

    public void calcularTotalCorrespondiente() {
        estadoVenta.calcularTotalCorrespondiente();
    }

    public void calcularVuelto() {
        estadoVenta.calcularVuelto();
    }

    public void generarFactura() {
        estadoVenta.generarFactura();
    }

    public boolean pagarVenta() {
        return estadoVenta.pagarVenta();
    }

    public void cancelarVenta() {
        estadoVenta.cancelarVenta();
    }

    public void finalizarVenta() {
        estadoVenta.finalizarVenta();
    }

    public void ingresarMedioPago(MediosPago m) {
        estadoVenta.ingresarMedioPago(m);
    }

    // Auxiliares
    public float calcularTotal() {
        float total = 0f;
        for (DetallesVenta d : detalles) {
            total += d.calcularPrecio();
            d.displayDetalleVenta();
        }
        return total;
    }

    public float calcularTotalConRecargo() {
        if (medioPago != null) {
            return medioPago.calcularRecargo(calcularTotal());
        }
        return calcularTotal();
    }

    public float calcularTotalConDescuentoYRecargo() {
        float total = calcularTotal();
        if (descuento != null) {
            total -= total * descuento.getValor() / 100;
        }
        return medioPago != null ? medioPago.calcularRecargo(total) : total;
    }

    public void emitirFactura(String fecha, float total) {
        this.factura = new Factura(fecha, total);
    }

    public void borrarDetalleVenta(int posicion) {
        if (posicion >= 0 && posicion < detalles.size()) {
            detalles.remove(posicion);
        }
    }

    public void borrarDetalleProducto(Producto prod) {
        detalles.removeIf(d -> d.getProducto().equals(prod));
    }

    public void agregarDetalleVenta(DetallesVenta detalle) {
        this.detalles.add(detalle);
    }

    // Transición de Estado
    public void cambiarEstado(EstadosVenta est) {
        this.estadoVenta = est;
    }

    // GETTERS/SETTERS
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public MediosPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MediosPago medioPago) {
        this.medioPago = medioPago;
    }

    public List<DetallesVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesVenta> detalles) {
        this.detalles = detalles;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public EstadosVenta getEstadoVenta() {
        return estadoVenta;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
