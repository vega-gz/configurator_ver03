/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileConfigWork;

import FrameCreate.*;
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

    
    public static void GenSigtype(FrameTabel ft){
        String filePath="C:\\Users\\Григорий\\Desktop\\сиг";
        FileManager manager = new FileManager();
        XMLSAX configSig = new XMLSAX();
        String FILENAME = "ConfigSignals.xml";
        Node cfs = configSig.readDocument(System.getProperty("user.dir") + File.separator + FILENAME);// Открыть configCignals из рабочего каталога программы
        String nodeTable = ft.tableName();
        Node findNode = configSig.returnFirstFinedNode(cfs, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        Node nodeGenData = configSig.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        NodeList nodesGenData = nodeGenData.getChildNodes();
        String[] nodeAndAttr = {"Field", "name", "tagName"};
        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            XMLSAX sax = new XMLSAX();
            Node firstNode = nodesGenData.item(i);
            String typeName = firstNode.getNodeName();//достаю элементы из ноды(в данный момент T GPA AI DRV)
            String trueName = FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
            Node type = sax.readDocument("Указываю путь до этого файла  с новым именем" + "\\" + trueName);//прочитал файл в котором нашли совпадения по имени
            NodeList Fields = (NodeList) sax.returnFirstFinedNode(type, "Fields").getChildNodes();//нашел ноду Fields 

            Node field = Fields.item(0);//вот с этим надо подумать(Дима что то пишет еще)
            String typeUUID = field.getAttributes().getNamedItem("Type").getNodeValue();

            for (int j = 0; j < ft.tableSize(); j++) {
                String tagName = (String) ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                Node fields = sax.findNodeAtribute(field, nodeAndAttr);
                String key = field.getAttributes().getNamedItem("tagName").getNodeName();//назвал key как в примере из инета
                if (key != null) {

                } else {

                }

            }

        }

    }

}
