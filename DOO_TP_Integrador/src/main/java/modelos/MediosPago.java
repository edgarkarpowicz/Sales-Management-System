/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import estrategia.EstrategiaRecargo;
import java.util.List;

/**
 *
 * @author edgar
 */
public class MediosPago {
    
    private EstrategiaRecargo estrategiaRecargo;
    private String numTarjeta;

    public MediosPago(EstrategiaRecargo estrategiaRecargo, String numTarjeta) {
        // Para Debito o Credito
        this.estrategiaRecargo = estrategiaRecargo;
        this.numTarjeta = numTarjeta;
    }

    public MediosPago(EstrategiaRecargo estrategiaRecargo) {
        // Para Contado
        this.estrategiaRecargo = estrategiaRecargo;
        this.numTarjeta = null;
    }
    
    public float getRecargo() {
        return estrategiaRecargo.getRecargo();
    }
    
    public void actualizarRecargo(float rec) {
        estrategiaRecargo.actualizarRecargo(rec);
    }
    
    public float calcularRecargo(float total) {
        return total + ((estrategiaRecargo.getRecargo() * total) / 100);
    }

    public EstrategiaRecargo getEstrategiaRecargo() {
        return estrategiaRecargo;
    }

    public void setEstrategiaRecargo(EstrategiaRecargo estrategiaRecargo) {
        this.estrategiaRecargo = estrategiaRecargo;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }
    
}
