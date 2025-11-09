/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import estrategia.EstrategiaRecargo;

/**
 *
 * @author edgar
 */
public class Recargo {

    private String fecha;
    private float valorAnterior;
    private float valorNuevo;
    private EstrategiaRecargo recargo;

    public Recargo(String fecha, float valorAnterior, float valorNuevo, EstrategiaRecargo recargo) {
        this.fecha = fecha;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.recargo = recargo;
    }

    public String getValorOnDate(String date) {
        if (fecha == date) {
            return (String.valueOf(valorAnterior) + " | " + String.valueOf(valorNuevo));
        } else {
            return null;
        }
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(float valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public float getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(float valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public EstrategiaRecargo getRecargo() {
        return recargo;
    }

    public void setRecargo(EstrategiaRecargo recargo) {
        this.recargo = recargo;
    }

}
