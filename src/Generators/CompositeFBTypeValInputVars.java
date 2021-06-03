/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators;

import Tools.FileManager;
import XMLTools.XMLSAX;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.w3c.dom.Node;

/**
 *
 * @author nazarov
 */
// --- объект GraphicsCompositeFBType или CompositeFBType  ---
public class CompositeFBTypeValInputVars implements FindCompositeFBType {

    String nameNodeGetSignalConnection = null;
    XMLSAX bigSax = null;
    String uuidGCT; // УУИД головного объекта
    Node nodeFBType = null; // Сама нода с графикой
    String typeGCT = null;
    //List<HashMap<String, String>> listVar = null;       // InputVars из блока HMI
    //List<HashMap<String, String>> listEvent = null;     // EventInputs из блока HMI

    public CompositeFBTypeValInputVars(XMLSAX bigSax, String typeGCT) {
        this.bigSax = bigSax;
        this.typeGCT = typeGCT;
        nodeFBType = getNodeFBTypeToHMI();
    }

    @Override
    public Node getNodeFBTypeToHMI() {
        Node bigRoot = bigSax.getRootNode(); // головная нода файла
        String[] findInBig = new String[]{"GraphicsCompositeFBType", "Name", typeGCT};
        Node parentHMINode = bigSax.findNodeAtribute(bigRoot, findInBig);   // Ищем  Нужный блок
        if (parentHMINode != null) {
            return parentHMINode;
        } else {
            findInBig = new String[]{"CompositeFBType", "Name", typeGCT};   // Пробуем найти и композит если не нашли обычный
            parentHMINode = bigSax.findNodeAtribute(bigRoot, findInBig);    // Ищем  Нужный блок
            if (parentHMINode != null) {
                return parentHMINode;
            }
        }
        return null;
    }

    // --- получить объект списки полей из HMI (его свежие UUID)---
    public List<HashMap<String, String>> getFBInputs() {
        return getSignalsFromOBJ("InputVars");
    }
    
    // --- получить объект списки полей из HMI из EventInputs(его свежие UUID)---
    public List<HashMap<String, String>> getFBEvents() {
        return getSignalsFromOBJ("EventInputs");
    }
    
    // --- получить какие то сигналы с ноды ---
    private List<HashMap<String, String>> getSignalsFromOBJ(String nameNodeGetSignalConnection) {
        List<HashMap<String, String>> listTmp = new ArrayList<>(); 
            if (nodeFBType != null) {
               Node intetfaceListNode = bigSax.returnFirstFinedNode(nodeFBType, "InterfaceList"); // надо ограничить  иначе вернуть дичь
               if(intetfaceListNode != null){
                    Node nodeInputVars = bigSax.returnFirstFinedNode(intetfaceListNode, nameNodeGetSignalConnection);
                    if (nodeInputVars == null) {
                        return null;
                    }
                    for (Node n : bigSax.getHeirNode(nodeInputVars)) {
                        listTmp.add(bigSax.getDataNode(n));
                    }
                }
        }
        return listTmp;
    }

    // --- получить UUID объекта ---
    public String getFBUUID() {
        String UUIDFBType = null;
        if (nodeFBType != null) {
            UUIDFBType = bigSax.getDataAttr(nodeFBType, "UUID");
        }
        return UUIDFBType;
    }

    // --- возращает UUID структуры VarDeclaration ---
    public String getUUIDSigVarDeclaration(String Name) {
        for (HashMap<String, String> h : getFBInputs()) {              
            if (Name.equals(h.get("Name"))) {                
                return h.get("UUID");                 
            }
        }
        for (HashMap<String, String> h : getFBEvents()) {              
            if (Name.equals(h.get("Name"))) {                
                return h.get("UUID");                 
            }
        }
        return null;
    }

    // --- возращает Type структуры VarDeclaration ---
    public String getTypeSigVarDeclaration(String Name) {
        for (HashMap<String, String> h : getFBInputs()) {              
            if (Name.equals(h.get("Name"))) {                
                return h.get("Type");                 
            }
        }
        for (HashMap<String, String> h : getFBEvents()) {              
            if (Name.equals(h.get("Name"))) {                
                return h.get("Type");                 
            }
        }
        return null;
    }
    public Node getNodeFB() {
        if (nodeFBType != null) {
            return nodeFBType;
        } else {
            FileManager.loggerConstructor("Не найден сигнал GraphicsCompositeFBType или CompositeFBType " + typeGCT + " в файле " + bigSax.getNameFile());
            return null;
        }
    }
}
