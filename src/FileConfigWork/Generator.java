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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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

    @SuppressWarnings("empty-statement")
    public static void GenTypeFile(FrameTabel ft) {
        String filePath = "/home/ad/Документы по работе/сиг/";
        FileManager manager = new FileManager();
        UUID uuid = new UUID();
        XMLSAX configSig = new XMLSAX();
        String FILENAME = "ConfigSignals.xml";
        Node cfs = configSig.readDocument(FILENAME);// Открыть configCignals из рабочего каталога программы
        String nodeTable = ft.tableName();
        Node findNode = configSig.returnFirstFinedNode(cfs, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        Node nodeGenData = configSig.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        NodeList nodesGenData = nodeGenData.getChildNodes();
        HashMap<String, String> dataNode = new HashMap<>();
        Node fieldsNode = null;
        String[] oldArray = {"ver", "old"};//массив для добваления атрибута ОЛД
        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                XMLSAX sax = new XMLSAX();
                Node currNodeCfgXML = nodesGenData.item(i);
                String typeName = currNodeCfgXML.getNodeName();//достаю элементы из ноды(в данный момент T GPA AI DRV)
                String trueName = FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
                String typeUUID = uuid.getUIID();
                Node newFields = null;
                Node type;
                Node oldFields = null;
                if (trueName == null) {//помещаем сюда создание файла
//                    Node rootNode = sax.createNode("Type");//создали шапку 
//                    dataNode.put("Kind", "Struct");//записали атрибуты
//                    dataNode.put("Name", typeName);
//                    dataNode.put("UUID", "UUID");
//                    sax.insertDataNode(rootNode, dataNode);//поместили атрибуты в Type
//                    fieldsNode = sax.createNode("Fields");//создали ноду
                } else {//сюда помещаем добавление
                    type = sax.readDocument(filePath + trueName);//прочитал файл в котором нашли совпадения по имени
                    oldFields = sax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
                    sax.setDataAttr(oldFields, "ver", "old");//добавил атрибут ver old
                    String[] newArray = {"Fields", "ver", "new"};
                    newFields = sax.insertChildNode(type, newArray);
                    Node firstFields = sax.returnFirstFinedNode(oldFields, "Field");
                    typeUUID = firstFields.getAttributes().getNamedItem("Type").getNodeValue();//получаю значение ноды type
                }
                for (int j = 0; j < ft.tableSize(); j++) {
                    String tagName = (String) ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                    String comment = (String) ft.getCell("Наименование", j);//получаем НАИМЕНОВАНИЕ из таблицы

                    if (oldFields == null) {//если нода пустая,то создаю элементы

//                        oldFields.appendChild(fieldsNode);//поместили ноду без атрибутов 
//                        Node fieldNode = sax.createNode("Field");//создали ноду filed
//                        HashMap<String, String> childNode = new HashMap<>();
//                        childNode.put("Comment", comment);
//                        childNode.put("Name", tagName);
//                        childNode.put("Type", typeUUID);
//                        childNode.put("UUID", uuid.getUIID());
//                        sax.insertDataNode(fieldNode, childNode);
//                        fieldsNode.appendChild(fieldNode);//добавили Field в Fileds

                    } else {
                        String[] nodeAndAttr = {"Field", "name", tagName};
                        Node oldTag = sax.findNodeAtribute(oldFields, nodeAndAttr);
                        if (oldTag == null) {
                            String nAndA[] = {"Field", "name", tagName, "Comment", comment, "Type", typeUUID, "UUID", uuid.getUIID()};
                            sax.insertChildNode(newFields, nAndA);
                        } else {
                            sax.setDataAttr(oldTag, "Comment", comment);
                            newFields.appendChild(oldTag);
                        }
                    }
                }
                sax.writeDocument(typeName);//записали файл
            }

        }
    }

    String fileChosserLocal() {
        String pathFileType = null;
        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
        int ren = fileopen.showDialog(null, ".type");
        if (ren == JFileChooser.APPROVE_OPTION) {

            File file = fileopen.getSelectedFile();// выбираем файл из каталога
            pathFileType = file.toString();
            //System.out.println(file.getNaтяme());
            if (pathFileType.endsWith(".type")) {
                new SignalTypeToBase(pathFileType);
            } else {
                JOptionPane.showMessageDialog(null, "Расширение файла не .type"); // Это сообщение
            }
        }
        return pathFileType;

    }

//    public static void main(String[] arg){
//        Generator genTest = new Generator();
//    }
}
