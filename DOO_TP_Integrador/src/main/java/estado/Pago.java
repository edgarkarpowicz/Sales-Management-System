/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estado;

import java.time.LocalDate;
import modelos.MediosPago;
import modelos.Producto;
import modelos.Venta;

/**
 *
 * @author edgar
 */
public class Pago implements EstadosVenta {
    private Venta venta;

    public Pago(Venta venta) {
        this.venta = venta;
    }

    @Override
    public void agregarProducto(Producto prod, int cant) {
        throw new IllegalStateException("No se pueden agregar productos después del pago.");
    }

    @Override
    public boolean comprobarSiCorrespondeRecargo() {
        throw new IllegalStateException("Ya no se puede comprobar si corresponde Recargo.");
    }

    @Override
    public boolean comprobarSiCorrespondeDescuento() {
        throw new IllegalStateException("Ya no se puede comprobar si corresponde Descuento.");
    }

    @Override
    public float calcularTotalCorrespondiente() {
        throw new IllegalStateException("Total ya fue calculado.");
    }

    @Override
    public void calcularVuelto() {
        venta.calcularVuelto();
    }

    @Override
    public void generarFactura() {
        float total = venta.calcularTotalConDescuentoYRecargo();
        venta.emitirFactura(LocalDate.now().toString(), total);
        venta.cambiarEstado(new Completada(venta));
    }

    @Override
    public boolean pagarVenta() {
        throw new IllegalStateException("Ya esta pagado.");
    }

    @Override
    public void cancelarVenta() {
        throw new IllegalStateException("No se puede cancelar después del pago.");
    }

    @Override
    public void finalizarVenta() {
        throw new IllegalStateException("Aún se debe generar la Factura.");
    }

    @Override
    public void ingresarMedioPago(MediosPago m) {
        throw new IllegalStateException("Medio de pago ya ingresado.");
    }
    
    @Override
    public String returnName() {
        return "Pago";
    }
}
