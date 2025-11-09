/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import builder.ConcreteBuilderCliente;
import enums.Genero;
import java.util.List;

/**
 *
 * @author edgar
 */
public class JefeAdministracion extends Usuario {

    public JefeAdministracion(String nombre, String apellido, String numDocumento, String tipoDocumento, String cuit, String condicionAfip, Genero genero, String fechaNacimiento, Domicilio domicilio, String email, List<String> telefonos) {
        super(nombre, apellido, numDocumento, tipoDocumento, cuit, condicionAfip, genero, fechaNacimiento, domicilio, email, telefonos);
    }

    public Cliente agregarCliente(Domicilio d, Persona p) {
        ConcreteBuilderCliente blCliente = new ConcreteBuilderCliente();
        Cliente cl = blCliente
                // Atributos de Domicilio
                .conNomCalle(d.getNomCalle())
                .conNumVivienda(d.getNumVivienda())
                .conNumDep(d.getNumDepartamento())
                .conCodPos(d.getCodPostal())
                .conLoc(d.getLocalidad())
                .conDep(d.getDepartamento())
                .conPais(d.getPais())
                // Atributos de Persona
                .conNombre(p.getNombre())
                .conApellido(p.getApellido())
                .conNumDoc(p.getNumDocumento())
                .conTipoDoc(p.getTipoDocumento())
                .conCuit(p.getCuit())
                .conCondAfip(p.getCondicionAfip())
                .conGenero(p.getGenero())
                .conFechaNac(p.getFechaNacimiento())
                .conEmail(p.getEmail())
                .conTelefonos(p.getTelefonos())
                //Creación
                .build();
        return cl;
    }

    public void generarInformeVentasDiarias() {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void generarInformeVentasCajero() {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void generarInformeStockProducto() {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void obtenerInforme() {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void procesarProducto(Producto p) {
        // No entra en Contexto de esta Implementación
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
