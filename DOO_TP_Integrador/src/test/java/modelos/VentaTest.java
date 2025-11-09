package modelos;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class VentaTest {

    private Venta venta;
    private Producto prodA;
    private Producto prodB;
    private DetallesVenta detA;
    private DetallesVenta detB;

    @Before
    public void setUp() {
        // Creamos dos productos con diferentes precios
        prodA = new Producto("Manzana", 100, 10.0f, "FrutasSA", "COD123");
        prodB = new Producto("Banana", 50, 5.0f, "FrutasSA", "COD456");

        // Creamos los DetallesVenta correspondientes
        detA = new DetallesVenta(prodA, 2); // 2 * 10 = 20
        detB = new DetallesVenta(prodB, 3); // 3 * 5  = 15

        // Instanciamos una Venta “vacía”. Pasamos null a cliente/cajero 
        venta = new Venta(null, null);

        // Agregamos los detalles manualmente
        venta.agregarDetalleVenta(detA);
        venta.agregarDetalleVenta(detB);
        // Ahora la venta tiene dos detalles: [detA, detB] y su total base = 20 + 15 = 35
    }

    @Test
    public void testCalcularTotal_sumaExacta() {
        float total = venta.calcularTotal();
        assertEquals("El total debe ser 35.0 (20 + 15).", 35.0f, total, 0.0001f);
    }

    @Test
    public void testCalcularTotalConRecargo_sinMedioPago() {
        float totalRec = venta.calcularTotalConRecargo();
        assertEquals("Sin medio de pago, debe ser igual al total base (35).", 35.0f, totalRec, 0.0001f);
    }

    @Test
    public void testCalcularTotalConDescuentoYRecargo_sinDescuentoYMedioPago() {
        float totalFinal = venta.calcularTotalConDescuentoYRecargo();
        assertEquals("Sin descuento y sin medio de pago, debe ser 35.", 35.0f, totalFinal, 0.0001f);
    }

    @Test
    public void testEmitirFactura_y_getFactura() {
        assertNull("Inicialmente no debe haber factura.", venta.getFactura());
        venta.emitirFactura("2025-06-05", 35.0f);
        Factura f = venta.getFactura();
        assertNotNull("Luego de emitirFactura, no debe ser null.", f);
        assertEquals("La fecha debe guardarse correctamente.", "2025-06-05", f.getFechaEmitida());
        assertEquals("El totalFactura debe ser 35.0.", 35.0f, f.getTotalFactura(), 0.0001f);
    }

    @Test
    public void testBorrarDetalleVenta_porPosiciónValida() {
        venta.borrarDetalleVenta(0);
        assertEquals("Después de borrar índice 0, queda solo detB (15).", 15.0f, venta.calcularTotal(), 0.0001f);
        List<DetallesVenta> lista = venta.getDetalles();
        assertEquals("Debe quedar 1 detalle.", 1, lista.size());
        assertEquals("El detalle restante debe corresponder a prodB.", prodB, lista.get(0).getProducto());
    }

    @Test
    public void testBorrarDetalleVenta_posiciónNegativa_oFueraDeRango() {
        venta.borrarDetalleVenta(-1);
        venta.borrarDetalleVenta(999);
        assertEquals("Índices inválidos no deben alterar total.", 35.0f, venta.calcularTotal(), 0.0001f);
        assertEquals("La lista de detalles sigue con 2 elementos.", 2, venta.getDetalles().size());
    }

    @Test
    public void testBorrarDetalleProducto_existente() {
        venta.borrarDetalleProducto(prodA);
        assertEquals("Al borrar prodA, queda solo detB con total 15.", 15.0f, venta.calcularTotal(), 0.0001f);
        assertEquals("Lista debe tener 1 detalle.", 1, venta.getDetalles().size());
        assertEquals("El detalle restante debe ser prodB.", prodB, venta.getDetalles().get(0).getProducto());
    }

    @Test
    public void testBorrarDetalleProducto_noExistente() {
        Producto fantasma = new Producto("Pera", 10, 7.0f, "FrutasSA", "COD999");
        venta.borrarDetalleProducto(fantasma);
        assertEquals("Si el producto no existe, total sigue 35.", 35.0f, venta.calcularTotal(), 0.0001f);
        assertEquals("Lista sigue con 2 detalles.", 2, venta.getDetalles().size());
    }
}
