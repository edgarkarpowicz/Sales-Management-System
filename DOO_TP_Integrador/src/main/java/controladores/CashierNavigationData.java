package controladores;

import fachada.FachadaSistemaVentas;
import modelos.Venta;
import modelos.Cajero;
import modelos.Factura;

/**
 * Utility class to handle navigation data between cashier screens
 * when using App.setRoot() to preserve session state
 */
public class CashierNavigationData {
    
    private static Venta ventaForRestoration;
    private static Cajero cajeroForRestoration;
    private static FachadaSistemaVentas fachadaForRestoration;
    private static Factura facturaForConfirmation;
    private static Venta ventaForConfirmation;
    
    // Getters
    public static Venta getVentaForRestoration() {
        return ventaForRestoration;
    }
    
    public static Cajero getCajeroForRestoration() {
        return cajeroForRestoration;
    }
    
    public static FachadaSistemaVentas getFachadaForRestoration() {
        return fachadaForRestoration;
    }
    
    public static Factura getFacturaForConfirmation() {
        return facturaForConfirmation;
    }
    
    public static Venta getVentaForConfirmation() {
        return ventaForConfirmation;
    }
    
    // Setters
    public static void setVentaForRestoration(Venta venta) {
        ventaForRestoration = venta;
    }
    
    public static void setCajeroForRestoration(Cajero cajero) {
        cajeroForRestoration = cajero;
    }
    
    public static void setFachadaForRestoration(FachadaSistemaVentas fachada) {
        fachadaForRestoration = fachada;
    }
    
    public static void setConfirmationData(FachadaSistemaVentas fachada, Factura factura, Venta venta) {
        fachadaForRestoration = fachada;
        facturaForConfirmation = factura;
        ventaForConfirmation = venta;
    }
    
    // Utility methods
    public static void clearAll() {
        ventaForRestoration = null;
        cajeroForRestoration = null;
        fachadaForRestoration = null;
        facturaForConfirmation = null;
        ventaForConfirmation = null;
    }
    
    public static boolean hasDataForRestoration() {
        return ventaForRestoration != null && cajeroForRestoration != null && fachadaForRestoration != null;
    }
      public static boolean hasConfirmDataForRestoration() {
        return facturaForConfirmation != null && ventaForConfirmation != null && fachadaForRestoration != null;
    }
    
    public static FachadaSistemaVentas getFachadaForConfirmation() {
        return fachadaForRestoration;
    }
    
    public static void clearConfirmData() {
        facturaForConfirmation = null;
        ventaForConfirmation = null;
        fachadaForRestoration = null;
    }
}
