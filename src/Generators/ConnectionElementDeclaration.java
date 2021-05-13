/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

import Tools.FileManager;
import XMLTools.UUID;
import XMLTools.XMLSAX;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author nazarov
 */
public class ConnectionElementDeclaration {
    ArrayList<ConnectionData> connectionsSigs = null;
    XMLSAX bigSax = null;
    XMLSAX HMIcfg = null;
    Node hmiNode = null;
    XMLSAX HMIsax= null;
    Node nodeInputVars= null;
    List<HashMap<String, String>> listVarHMI= null;
    CompositeFBType myPageFB= null;
    
     // --- Метод поиск и добавления сигналов  ---
    public ConnectionElementDeclaration(XMLSAX bigSax, XMLSAX HMIcfg, Node hmiNode,
            XMLSAX HMIsax, Node nodeInputVars, ArrayList<ConnectionData> connectionsSigs,
            List<HashMap<String, String>> listVarHMI, CompositeFBType myPageFB) 
    {
        /*
        надо бы видимо делат объект
        XMLSAX bigSax  -- основноя нода HMI Сонаты
        XMLSAX HMIcfg  -- основная нода конфигурационного файла
        Node hmiNode -- одна из нод конфигурационного файла GenHMI
        XMLSAX HMIsax -- нода файла манекена( основа из чего создаем лист с сигналами)
        List<HashMap<String, String>> listVarHMI -- данные из сигналов блока на основе которого делаем внутренние блочки
        CompositeFBType myPageFB -- объект из основного HMI(если нашли такой же генерируемый объект)
        */
        this.connectionsSigs = connectionsSigs;
        this.bigSax = bigSax;
        this.HMIcfg = HMIcfg;
        this.hmiNode = hmiNode;
        this.HMIsax = HMIsax;
        this.nodeInputVars = nodeInputVars;
        this.listVarHMI = listVarHMI;
        this.myPageFB = myPageFB;
        
        generationConnectionData(); // 
    }
    
    private void generationConnectionData(){
        ArrayList<Node> varDecListNode = new ArrayList<>();
        for (Node n : bigSax.getHeirNode(nodeInputVars))
        {
            //System.out.println(n.getNodeName());
            varDecListNode.add(n);
        }
        for (Node n : getListAddVarDeclaration(HMIcfg, hmiNode)) { // если есть в ноде конфиг "addVarDeclaration"(добавляет доп поля в заголовок основной страницы генерированного блока)
            boolean findNodeVar = false;
            String uuidVarDeclaration = null;
            String nameVarDeclaration = HMIcfg.getDataAttr(n, "Name");
            String typeOrigSig = null;

            // достаем сигналы из ноды которая есть, если нет то ищем сигнала вообще на основе чего делаем и генерим УУИДЫ 
            String uuidVadD = myPageFB.getUUIDSigVarDeclaration(nameVarDeclaration); // достаем ууид сигнала если он такой есть
            if (uuidVadD != null) {
                uuidVarDeclaration = uuidVadD;
                typeOrigSig = myPageFB.getTypeSigVarDeclaration(nameVarDeclaration);
            } else {
                uuidVarDeclaration = UUID.getUIID(); // новый УИД сигналу
                // А данные берем о сигнале уже из головного блока
                // определяем тип головного ууида что бы правильно сростить типы
                for (HashMap<String, String> h : listVarHMI) {              // прогон по Варам оригинального головного блока HMI из мнемосхемы
                    if (h.get("Name").equals(nameVarDeclaration)) {         // Сравниваем что добавляем и что в оригинале.
                        typeOrigSig = h.get("Type");                        // берем нужный тип и нужны ууид типа
                    }
                }

            }

            ConnectionData sigToConnect = new ConnectionData(nameVarDeclaration); // структура для коннекта сигналов
            sigToConnect.setUUIDVarDeclaration(uuidVarDeclaration);     // вносим 
            connectionsSigs.add(sigToConnect);

            for (Node nAdd : varDecListNode) { // это проверка есть ли такой сигнал уже в структуре манекена или нет
                try {
                    if (bigSax.getDataAttr(n, "Name").equals(HMIsax.getDataAttr(nAdd, "Name"))) { // если нашли такую же ноду
                        //HMIsax.removeNode(nAdd);
                        HMIsax.setDataAttr(nAdd, "UUID", uuidVarDeclaration);
                        HMIsax.setDataAttr(nAdd, "Type", typeOrigSig);
                        //nodeInputVars.appendChild(HMIsax.importNode(n)); // таким извращенным способом добавить ноду из одного документа в другой
                        findNodeVar = true;
                        break;
                    }
                } catch (NullPointerException e) {
                    FileManager.loggerConstructor("не верно описан сигнал для добавления в ноде addVarDeclaration " + nAdd.getNodeName()
                            + "формат должен быть пример: <VarDeclaration InitialValue=\"''\" Name=\"PrefAb\"/>");
                }
            }
            if (!findNodeVar) {
                HMIcfg.setDataAttr(n, "UUID", uuidVarDeclaration);
                HMIcfg.setDataAttr(n, "Type", typeOrigSig);
                nodeInputVars.appendChild(HMIsax.importNode(n)); // тут импорт
            }
        }

        varDecListNode.clear(); // нужно обновить список иначе Null
        for (Node n : bigSax.getHeirNode(nodeInputVars)) {
            varDecListNode.add(n);
        }

        for (Node n : getListRemoveVarDeclaration(HMIcfg, hmiNode)) {             // если есть в ноде конфиг "disableVarDeclaration"
            for (Node nAdd : varDecListNode) {
                if (n.getNodeName().equals(HMIsax.getDataAttr(nAdd, "Name"))) {   // а тут по имени ноды и аттрибуту
                    HMIsax.removeNode(nAdd);
                    break;
                }
            }
        }

    }
    
    // --- Вернуть сформированные коннекшены коннекшены ---
    public ArrayList<ConnectionData> getConnectionsSigs(){
        return connectionsSigs;
    
    }
    
    // --- читаем Ноду addVarDeclaration ( возращает список Node которые добавляются в объекта InputVars FB )---
    private static ArrayList<Node> getListAddVarDeclaration(XMLSAX HMIcfg, Node hmiNode) {
        ArrayList<Node> disableVar = new ArrayList<>();
        Node disNode = HMIcfg.returnFirstFinedNode(hmiNode, "addVarDeclaration");
        if (disNode != null) {
            disableVar = HMIcfg.getHeirNode(disNode);
        }
        return disableVar;
    }
    
    // --- читаем Ноду addEvent ( возращает список Node которые добавляются в объекта EventInputs ноды так же связи )---
    private static ArrayList<Node> getListAddEventDeclaration(XMLSAX HMIcfg, Node hmiNode) {
        ArrayList<Node> disableVar = new ArrayList<>();
        Node disNode = HMIcfg.returnFirstFinedNode(hmiNode, "addEvent");
        if (disNode != null) {
            disableVar = HMIcfg.getHeirNode(disNode);
        }
        return disableVar;
    }
    
    // --- читаем Ноду disableVarDeclaration ( возращает список Node которые удаляются из объекта InputVars FB )---
    private static ArrayList<Node> getListRemoveVarDeclaration(XMLSAX HMIcfg, Node hmiNode) {
        ArrayList<Node> disableVar = new ArrayList<>();
        Node disNode = HMIcfg.returnFirstFinedNode(hmiNode, "disableVarDeclaration");
        if (disNode != null) {
            disableVar = HMIcfg.getHeirNode(disNode);
        }
        return disableVar;
    }
}
