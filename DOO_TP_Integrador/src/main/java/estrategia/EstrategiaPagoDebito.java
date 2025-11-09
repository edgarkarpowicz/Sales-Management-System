/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estrategia;

import estrategia.EstrategiaRecargo;

/**
 *
 * @author edgar
 */
public class EstrategiaPagoDebito implements EstrategiaRecargo {
    
    float recargo = 0;

    @Override
    public float getRecargo() {
        return recargo;
    }

    @Override
    public void actualizarRecargo(float rec) {
        this.recargo = rec;
    }
    
    @Override
    public String getName() {
        return "Debito";
    }
    
}
