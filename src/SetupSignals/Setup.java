/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

/**
 *
 * @author nazarov
 */


public abstract class Setup {    
    String  name;
    String[] columnT = {"Abonent", "NameSeting", "Type", "NameSig", "Direction", "Delay", "LostSignal", "Value"}; // Набор столбцов для базы таблицы
    
    ConfigSig editConfig(ConfigSig c){
        return null; 
    }
    
    void removeConfig(ConfigSig c){
    
    }
    
    ConfigSig addConfig(String s){
        return null;
    
    }
}
