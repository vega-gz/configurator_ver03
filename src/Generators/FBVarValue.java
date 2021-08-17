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

// === класс переменной VarValue ===
class FBVarValue {

    private String NameNode = "VarValue";
    private String Type = "";
    private String TypeUUID = "";
    private String Value = "";
    private String Variable = "";
    private String Text = ""; // доп параметр для привязки  к значению    

    public FBVarValue(String Type, String TypeUUID, String Value, String Variable) {
        this.Type = Type;
        this.TypeUUID = TypeUUID;
        this.Value = Value;
        this.Variable = Variable;
    }

    public FBVarValue(String[] dataVar) { // Конструктор с массивом из 9 (Временное что бы не переписывать Льва алгоритмы)
        if (dataVar.length >= 9) {
            this.NameNode = dataVar[0];
            this.Type = dataVar[6];
            this.TypeUUID = dataVar[8];
            this.Value = dataVar[4];
            Variable = dataVar[2];
        }
    }

    public String getType() {
        return Type;
    }

    public String getTypeUUID() {
        return TypeUUID;
    }

    public String getValue() {
        if(Text != null)return Text + Value;
        else return Value;
        
    }

    public String getVariable() {
        return Variable;
    }
    
    public void editType(String Type) {
        this.Type = Type;
    }

    public void editTypeUUID(String TypeUUID) {
        this.TypeUUID = TypeUUID;
    }

    public void editValue(String Value) {
        this.Value = Value;
    }

    public void editVariable(String Variable) {
        this.Variable = Variable;
    }

    public void editText(String Text) {
        this.Text = Text;
    }
    
    
    // --- Подготовленные данные для занесения в ноду XML ---
    public String[] getToXML() {
        String[] strXml = {NameNode,
            "Variable", Variable,
            "Value", Value,
            "Type", Type,
            "TypeUUID", TypeUUID};
        return strXml;
    }
}

