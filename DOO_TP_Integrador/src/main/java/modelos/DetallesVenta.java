/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author edgar
 */
public class DetallesVenta {
    
    private Producto producto;
    private int cantidad;

    public DetallesVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    public float calcularPrecio() {
        return (cantidad * producto.getPrecio());
    }
    
    public void displayDetalleVenta() {
        System.out.println("Cantidad: " + cantidad + " | Producto: " + producto.getNombre());
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
