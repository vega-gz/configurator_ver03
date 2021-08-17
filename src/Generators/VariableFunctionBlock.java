/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

/**
 *
 * @author nazarov
 */

// === класс Variable данных  Variables  из FunctionBlock ===
class VariableFunctionBlock {

    private String name;
    private String Type;
    private String TypeUUID;
    private String UUID;
    private String Variable = "Variable";
    private String Usage = "internal";


     
    public VariableFunctionBlock(String UUID, String name, String Type, String TypeUUID) {
        this.UUID = UUID;
        this.name = name;
        this.Type = Type;
        this.TypeUUID = TypeUUID;
    }

    public String getType() {
        return Type;
    }

    public String getTypeUUID() {
        return TypeUUID;
    }

    public String getVariable() {
        return Variable;
    }
    
    public String getUsage() {
       if(Usage == null){
           Usage = "internal";
           return Usage;
       }else{
           return Usage;
       }
    }
    
    public void editType(String Type) {
        this.Type = Type;
    }

    public void editTypeUUID(String TypeUUID) {
        this.TypeUUID = TypeUUID;
    }

    public void editVariable(String Variable) {
        this.Variable = Variable;
    }
    
    public void editUsage(String Text) {
        this.Usage = Text;
    }
    
    
    // --- Подготовленные данные для занесения в ноду XML ---
    public String getStringToFile() {
        return "<" + Variable + " UUID=\"" + UUID + "\" Name=\"" + name + "\" Type=\"" + Type + "\" TypeUUID=\"" + TypeUUID + "\" Usage=\"" + Usage + "\" />";
    }

}

