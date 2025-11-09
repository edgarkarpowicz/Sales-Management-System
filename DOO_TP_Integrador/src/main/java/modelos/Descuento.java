/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import enums.TiposDescuentos;

/**
 *
 * @author edgar
 */
public class Descuento {

    private TiposDescuentos tipo;
    private float valor;

    public Descuento(TiposDescuentos tipo, float valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TiposDescuentos getTipo() {
        return tipo;
    }

    public void setTipo(TiposDescuentos tipo) {
        this.tipo = tipo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

}
