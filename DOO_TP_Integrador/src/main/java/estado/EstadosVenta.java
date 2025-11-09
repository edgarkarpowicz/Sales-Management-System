/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package estado;

import modelos.MediosPago;
import modelos.Producto;

/**
 *
 * @author edgar
 */
public interface EstadosVenta {

    void agregarProducto(Producto prod, int cant);

    boolean comprobarSiCorrespondeRecargo();

    boolean comprobarSiCorrespondeDescuento();

    float calcularTotalCorrespondiente();

    void calcularVuelto();

    void generarFactura();

    boolean pagarVenta();

    void cancelarVenta();

    void finalizarVenta();
    
    String returnName();
    
    void ingresarMedioPago(MediosPago m);
}
