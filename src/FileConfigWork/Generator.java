/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileConfigWork;

import FrameCreate.*;

import FrameCreate.FrameTabel;
import XMLTools.*;
import FrameCreate.TableNzVer2;
import XMLTools.XMLSAX;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import fileTools.*;
import fileTools.*;
import static fileTools.FileManager.FindFile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.apache.poi.ss.usermodel.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Григорий
 */
public class Generator {

    public static void GenSigtype(FrameTabel ft) {
        String filePath = "C:\\Users\\Григорий\\Desktop\\сиг";
        FileManager manager = new FileManager();
        UUID uuid=new UUID();
        
        
        XMLSAX configSig = new XMLSAX();
        String FILENAME = "ConfigSignals.xml";
        Node cfs = configSig.readDocument(System.getProperty("user.dir") + File.separator + FILENAME);// Открыть configCignals из рабочего каталога программы
        String nodeTable = ft.tableName();
        Node findNode = configSig.returnFirstFinedNode(cfs, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        Node nodeGenData = configSig.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        NodeList nodesGenData = nodeGenData.getChildNodes();
       

        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            XMLSAX sax = new XMLSAX();
            Node firstNode = nodesGenData.item(i);
            String typeName = firstNode.getNodeName();//достаю элементы из ноды(в данный момент T GPA AI DRV)
            String trueName = FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
            Node type = sax.readDocument(filePath + "\\" + trueName);//прочитал файл в котором нашли совпадения по имени
            Node oldFields = sax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
            Node firstFields = oldFields.getFirstChild();
              String typeUUID=firstFields.getAttributes().getNamedItem("Type").getNodeValue();//получаю значение ноды type
//              HashMap<String, String> dataN = new HashMap<>();
//              sax.insertDataNode(type, dataN);

          /// Node rootNode = sax.createDocument("Type");//создаем ноды с именем ,которое вытянули из конфиг сигнался
             Node rootNode=sax.createNode("Type");
            HashMap<String, String> dataNode = new HashMap<>();
            dataNode.put("Kind", "Struct");
            dataNode.put("Name", typeName);
            dataNode.put("UUID", "UUID");
            sax.insertDataNode(rootNode, dataNode);

          

            for (int j = 0; j < ft.tableSize(); j++) {
                
                String tagName = (String) ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                 String[] nodeAndAttr = {"Field", "name", tagName};
                String comment=(String)ft.getCell("Наименование", j);
                Node fields = sax.findNodeAtribute(firstFields, nodeAndAttr);
                String key = firstFields.getAttributes().getNamedItem("tagName").getNodeName();//назвал key как в примере из инета
                if (key != null) {
                    
                } else {
                    Node fieldsNode=sax.createNode("Fields");
                sax.insertDataNode(fieldsNode, dataNode);
                rootNode.appendChild(fieldsNode);
                Node fieldNode=sax.createNode("Field");
                HashMap<String, String> childNode = new HashMap<>();
                childNode.put("Comment", comment);
                childNode.put("Name",tagName);
                childNode.put("Type", typeUUID);
                childNode.put("UUID", uuid.getUIID());
                fieldsNode.appendChild(fieldNode);
                sax.writeDocument(typeName);
               
                
                }
                
                

            }

        }
    }

}
