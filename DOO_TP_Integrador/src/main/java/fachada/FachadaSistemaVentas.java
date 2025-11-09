/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fachada;

import modelos.Cajero;
import modelos.Cliente;
import modelos.Descuento;
import modelos.Factura;
import modelos.MediosPago;
import modelos.Producto;
import modelos.Venta;

/**
 *
 * @author edgar
 */
public class FachadaSistemaVentas {

    public void escanearProducto(Producto p, int cant, Venta v) {
        v.agregarProducto(p, cant);
    }

    public Venta generarVenta(Cliente cl, Cajero ca) {
        return new Venta(cl, ca);
    }

    public void ingresarMedioPago(Venta v, MediosPago m) {
        v.ingresarMedioPago(m);
    }

    public void modificarPrecioProducto(Producto p, float newPrecio) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void modificarStockProducto(Producto p, int newValue) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void modificarDescuento(Descuento d, float newValue) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void procesarFactura(Factura f) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void procesarProducto(Producto p) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
