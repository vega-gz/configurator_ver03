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
public class CompositeFBType implements FindCompositeFBType {

    XMLSAX bigSax = null;
    String uuidGCT; // УУИД головного объекта
    Node nodeFBType = null; // Сама нода с графикой
    String typeGCT = null;

    public CompositeFBType(XMLSAX bigSax, String typeGCT) {
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
        List<HashMap<String, String>> listVarHMI = new ArrayList<>();       // InputVars из блока HMI  
        if (nodeFBType != null) {
            String[] UUIDFBType = new String[]{bigSax.getDataAttr(nodeFBType, "UUID")};            // берем уид блок превращаем в массив
            // собрать структурированно InputVars из блока HMI основного
            Node nodeInputVars = bigSax.returnFirstFinedNode(nodeFBType, "InputVars");
            if (nodeInputVars == null) {
                return null;
            }
            for (Node n : bigSax.getHeirNode(nodeInputVars)) {
                listVarHMI.add(bigSax.getDataNode(n));
            }
        }
        return listVarHMI;
    }
    
    // --- получить объект списки полей из HMI (его свежие UUID)---
    public String getFBUUID() {
        String UUIDFBType = null;
        if (nodeFBType != null) {
            UUIDFBType = bigSax.getDataAttr(nodeFBType, "UUID");  
        }
        return UUIDFBType;
    }
    
    public Node getNodeFB(){
        if(nodeFBType !=null ) return nodeFBType;
        else{
            FileManager.loggerConstructor("Не найден сигнал GraphicsCompositeFBType или CompositeFBType " + typeGCT + " в файле " + bigSax.getNameFile());
            return null;
        }
    }
}