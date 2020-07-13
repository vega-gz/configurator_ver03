/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators;

import DataBaseTools.DataBase;
import XMLTools.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;

/**
 *
 * @author ad
 */
// --- Класс создания таблицы из файла type ---

public class SignalTypeToBase {
    private String nameFile = null;
    private String nameTable = null;
    private String commentTable = null;

    public SignalTypeToBase(){
        //new SignalTypeToBase("/home/ad/NetBeansProjects/Type_Mode.type");
    }
    public SignalTypeToBase(String nameFile){
        DataBase db = new DataBase();
        this.nameFile = nameFile;
        HashMap<String, String> dataRootN = null;  // данные основной ноды по которым строим таблицу
        HashMap<String, String> dataFromField = null;  // данные от ноды Field
        XMLSAX xmlTool = new XMLSAX();
        Node rootType = xmlTool.readDocument(nameFile);
        if(rootType.getNodeName().equals("Type")){ // точно ли нода файл Тип
            //xmlTool.stepThroughAll(rootType);
            dataRootN = xmlTool.getDataNode(rootType);// получаем значения ноды Type
            nameTable = dataRootN.get("Name"); // возвращеме значение имени
            // и по фору все в комментарий для таблицы
            for(Map.Entry<String, String> entry : dataRootN.entrySet()) {               
                    String value = entry.getValue();
                    commentTable += value + " "; // вконце будет пробел но мне лень анализировать 
            }
            Node fieldsN = xmlTool.returnFirstFinedNode(rootType, "Fields");// возвратим нужную ноду с полями для данных базы
            //System.out.print(fieldsN.getNodeName());
            //xmlTool.stepThroughAll(fieldsN);
            ArrayList<Node> nodesFieldsN = xmlTool.getHeirNode(fieldsN); // Получаем наследников в виде Field(тут нечего не получаем)
            for (int i = 0; i < nodesFieldsN.size(); i++) { // пробегаме по всем Field
                Node nodeField = nodesFieldsN.get(i);
                dataFromField = xmlTool.getDataNode(nodeField);// получаем значения ноды
                ArrayList<String> listNameColum = new ArrayList<>(); // именя колонок таблицы
                String[] rows = null; // Массив для вставки(как то избавиться)
               
                ArrayList<String> dataToTable = new ArrayList<>(); // данные для таблицы
                for(Map.Entry<String, String> entry : dataFromField.entrySet()) {       
                    String key = entry.getKey();
                    String value = entry.getValue();
                    listNameColum.add(key);
                    dataToTable.add(value);
                }
                 {
                    // преобразовываем Лист в массив (этот тупняк надо править)
                    rows = new String[dataToTable.size()];
                    for(int iC=0; iC<dataToTable.size(); ++iC){
                        rows[iC] = dataToTable.get(iC);
                    }
                }
                if(i == 0){ // если это первый проход то мы создаем таблицу по образу первого Field(ошибки в XML не обрабатываем) 
                    db.createTable(nameTable, listNameColum);
                    db.insertRows(nameTable, rows, listNameColum);
                }else{
                    db.insertRows(nameTable, rows, listNameColum);
                }
           }
            db.createCommentTable(nameTable, commentTable);
        }
        
    }
    
//     public static void main(String[] args) {
//         new SignalTypeToBase();
//     }
    
}
