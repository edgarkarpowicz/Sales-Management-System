/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author edgar
 */
public class DescuentoDto {
    private int idDescuento;
    private String tipo;
    private float valor;

    public DescuentoDto() {}

    public DescuentoDto(int idDescuento, String tipo, float valor) {
        this.idDescuento = idDescuento;
        this.tipo = tipo;
        this.valor = valor;
    }

    public DescuentoDto(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    public int getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
