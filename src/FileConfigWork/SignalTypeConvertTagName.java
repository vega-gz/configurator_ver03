/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileConfigWork;

import XMLTools.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;
import DataBaseConnect.*;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author ad
 */
// --- Класс создания таблицы из файла type ---

public class SignalTypeConvertTagName {
    private String nameFile = null;
    private String nameTable = null;
    private String commentTable = null;

    public SignalTypeConvertTagName(){
        //new SignalTypeToBase("/home/ad/NetBeansProjects/Type_Mode.type");
    }
    public SignalTypeConvertTagName(String nameFile){
        this.nameFile = nameFile;
        HashMap<String, String> dataFromField = null;  // данные от ноды Field
        XMLSAX xmlTool = new XMLSAX();
        Node rootType = xmlTool.readDocument(nameFile);
        if(rootType.getNodeName().equals("Type")){ // точно ли нода файл Тип
            Node fieldsN = xmlTool.returnFirstFinedNode(rootType, "Fields");// возвратим нужную ноду с полями для данных базы
            //System.out.print(fieldsN.getNodeName());
            //xmlTool.stepThroughAll(fieldsN);
            ArrayList<Node> nodesFieldsN = xmlTool.getHeirNode(fieldsN); // Получаем наследников в виде Field(тут нечего не получаем)
            for (int i = 0; i < nodesFieldsN.size(); i++) { // пробегаме по всем Field
                System.out.println("List Field " + nodesFieldsN.size());
                Node nodeField = nodesFieldsN.get(i);
                
                NamedNodeMap startAttr = nodeField.getAttributes(); // Получение имена и атрибутов каждого элемента
                for (int j = 0; j < startAttr.getLength(); j++) { // Переборка значений ноды
                   Node attr = startAttr.item(j);
                   String attribute = attr.getNodeName(); // Название атрибута
                   String value = attr.getNodeValue();
                   System.out.println(attribute + "  "+ value );
                   if(attribute.equalsIgnoreCase("Name")){
                        if(value.equalsIgnoreCase("tagName")){
                            System.out.println("i find " + value);
                            // еще раз пробегамаем по атрибутам и меняем Comment
                            for (int jTwo = 0; jTwo < startAttr.getLength(); jTwo++) {
                                Node attrFindComment = startAttr.item(jTwo);
                                String comment = attrFindComment.getNodeName(); // Название атрибута
                                if(comment.equalsIgnoreCase("Comment")){
                                    attrFindComment.setNodeValue("Mazafaker");
                                }
                            }
                        }
                    
                    }
                }
                
           }
            
        }
        xmlTool.writeDocument(); // сохраним изменения
    }
    
//     public static void main(String[] args) {
//         new SignalTypeToBase();
//     }
    
}
