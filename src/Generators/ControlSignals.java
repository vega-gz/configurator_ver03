/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

import java.util.ArrayList;

/**
 *
 * @author nazarov
 * 
 * Контроллирующий класс для фиксации изменения в файле HMI_sheet
 * Определяет изменен был ли сигнал, если да то не нужно генерировать UUID и присваивать новое
 * 
 */
public class ControlSignals {
    
    private ArrayList<String> listVarHMI = new ArrayList<>();

    public void markDummySignal(String s){
        listVarHMI.add(s);
    }
    
    // --- проверка редактировался ли сигнал ---
    public boolean isEdit(String s){
        for (String sArray : listVarHMI) {
            if(sArray.equals(s)){
             return true;
            }
        }
        return false;
    }
    
}
