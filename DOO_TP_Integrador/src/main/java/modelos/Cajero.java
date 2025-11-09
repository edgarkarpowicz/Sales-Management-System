/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import enums.Genero;
import fachada.FachadaSistemaVentas;
import java.util.List;

/**
 *
 * @author edgar
 */
public class Cajero extends Usuario {
    
    public Cajero(String nombre, String apellido, String numDocumento, String tipoDocumento, String cuit, String condicionAfip, Genero genero, String fechaNacimiento, Domicilio domicilio, String email, List<String> telefonos) {
        super(nombre, apellido, numDocumento, tipoDocumento, cuit, condicionAfip, genero, fechaNacimiento, domicilio, email, telefonos);
    }
    
    @Override
    public String getCargo() {
        return "Cajero";
    }
    
    public void generarVenta(Cliente c) {
        FachadaSistemaVentas fac = new FachadaSistemaVentas();
        fac.generarVenta(c, this);
    }
    
    public void escanearProducto(Producto p, int cant, Venta v) {
        FachadaSistemaVentas fac = new FachadaSistemaVentas();
        fac.escanearProducto(p, cant, v);
    }
}
