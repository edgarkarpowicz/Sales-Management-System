/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import builder.ConcreteBuilderCliente;

/**
 *
 * @author edgar
 */
public class Cliente extends Persona {

    public Cliente(ConcreteBuilderCliente builder) {
        super(
                builder.getPersona().getNombre(),
                builder.getPersona().getApellido(),
                builder.getPersona().getNumDocumento(),
                builder.getPersona().getTipoDocumento(),
                builder.getPersona().getCuit(),
                builder.getPersona().getCondicionAfip(),
                builder.getPersona().getGenero(),
                builder.getPersona().getFechaNacimiento(),
                builder.getDomicilio(),
                builder.getPersona().getEmail(),
                builder.getPersona().getTelefonos()
        );
    }
      public void ingresarMedioPago(Venta v, MediosPago m) {
        // Verificar que la venta esté en estado válido para ingresar medio de pago
        if (v == null || m == null) {
            throw new IllegalArgumentException("La venta y el medio de pago no pueden ser nulos");
        }
          // Establecer el medio de pago en la venta
        v.setMedioPago(m);
        
        // Llamar al método del estado para cambiar a MedioPagoIngresado
        v.getEstadoVenta().ingresarMedioPago(m);
    }
    
}
