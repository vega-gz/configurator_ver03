/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FileConfigWork;

/**
 *
 * @author Григорий
 */
public class ConfigData {
    private String Type,UUID;
    
    public ConfigData(String Type,String UUID){
        this.Type=Type;
        this.UUID=UUID;
    }
    
    public String getType(){
        return Type;
    }
    public String getUUID(){
        return UUID;
    }
}
