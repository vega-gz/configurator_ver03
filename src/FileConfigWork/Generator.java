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
import globalData.globVar;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        public static void genSTcode(FrameTabel ft) throws IOException{
        String currentDat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        String backUpPath = globVar.desDir + File.separator + "backUpST" + currentDat;   //установили путь для бэкапа
        new File(backUpPath).mkdir();                                       //создали папку для бэкапа
        //------------------------------------------------------------------------------------------------------------
        FileManager fm = new FileManager();                                 //создали менеджер файлов
        XMLSAX configSig = new XMLSAX();                                    //создали менеджер для ХМЛ
        Node cfs = configSig.readDocument("ConfigSignals.xml");// Открыть configCignals из рабочего каталога программы
        Node nodeGenCode = configSig.returnFirstFinedNode(configSig.returnFirstFinedNode(cfs, ft.tableName()), "GenCode");//Ищем в этой ноде ноду GenData
        ArrayList<Node> stFiles = configSig.getHeirNode(nodeGenCode);      //Создали список файлов для генерации.
        for(Node stFile : stFiles){                                         //Перебираем файлы
            String stFileName = (String) configSig.getDataNode(stFile).get("name"); //Для каждого файла
            fm.copyFile(globVar.desDir + File.separator + stFileName, backUpPath + File.separator + stFileName);                    //создаём резервную копию
            int ret = fm.openFile4read(globVar.desDir, stFileName);         //открываем её на чтенье
            if(ret!=0) break;
            ret = fm.createFile2write(globVar.desDir, stFileName + "_tmp"); //открываем файл на запись
            if(ret!=0) break;
            ArrayList<Node> funclist = configSig.getHeirNode(stFile);      //Создали список функций для генерации. Каждую функцию надо сгенерить по числу строк в таблице
            for(Node genSTnode : funclist){                                 //перебираем функции
                String stFunc = (String) configSig.getDataNode(genSTnode).get("name");  //вычитываем её имя
                Node args = configSig.returnFirstFinedNode(genSTnode, "arguments");     //Находим ноду с аргументами
                ArrayList<Node> arglist = configSig.getHeirNode(args);                  //создаём список аргументов
                String s = fm.rd();                                                     //Для копирования всего, что было до этой функции, 
                while(!fm.EOF && !s.contains(stFunc)){
                    fm.wr(s + "\n");                          //ищем в исходнои файле её первое вхождение
                    s = fm.rd();
                }
                String funcCall = stFunc + "(";                                 //Начинаем генерацию вызова функции
                for (int j = 0; j < ft.tableSize(); j++) {                      //Цикл по всем строкам таблицы
                    for(Node arg : arglist){                                        //Цикл по всем аргументам функции
                        ArrayList<Node> argParts = configSig.getHeirNode(arg);
                        for(Node argPart : argParts){                                   //Цикл по всем частям аргументов - текстовым и табличным
                            if("text".equals(argPart.getNodeName())) funcCall += (String) configSig.getDataNode(argPart).get("t");
                            else if("dbd".equals(argPart.getNodeName())) funcCall += (String) ft.getCell((String) configSig.getDataNode(argPart).get("t"),j);
                            else if("npp".equals(argPart.getNodeName())) funcCall += j;
                        }
                        funcCall += ",";                                                //аргумент записан и отделён от следующего запятой
                    }                                                   //Убираем лишнюю запятую в конце
                    funcCall = funcCall.substring(0, funcCall.length()-1) + ");//" + (String) ft.getCell("Наименование", j);
                    fm.wr(funcCall + "\n");                             //записываем вызов функции в файл
                    funcCall = stFunc + "(";                            // подготавливаем следующую строку
                }
                //пролистываем в исходном файле строки со старыми вызовами и пустые строки 
                while(!fm.EOF  && (s.contains(stFunc) || s.trim().isEmpty())&& !s.contains("]]></ST>")) s = fm.rd(); 
                if(!fm.EOF && s.contains("]]></ST>") && s.contains(stFunc)){    //если оказалось, что мы пропустили конец всей функции
                    fm.wr("]]></ST>\n");                                        //восстанавливаем его
                    s = fm.rd();
                }
                while(!fm.EOF){                                                 //дописываем хвост файла
                    fm.wr(s + "\n");                          
                    s = fm.rd();
                }
                fm.rdStream.close();                                                            //закрываем поток чтения
                fm.wrStream.close();                                                            //закрываем поток записи
                File file = new File(globVar.desDir + File.separator + stFileName);             //создаём ссылку на исходный файл
                file.delete();                                                                  //удаляем его
                File tmpFile = new File(globVar.desDir + File.separator + stFileName + "_tmp"); //создаём ссылку на сгенерированный файл
                tmpFile.renameTo(file);                                                         //делаем его исходным
                fm.openFile4read(globVar.desDir, stFileName);                                   //открываем его на чтенье
                fm.createFile2write(globVar.desDir, stFileName + "_tmp");                       //открываем временный файл для генерации

            }
            fm.rdStream.close();
            fm.wrStream.close();
        }
    }

    public static void GenTypeFile(FrameTabel ft) throws IOException {
        String currentDat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        String backUpPath = globVar.desDir + File.separator + "backUpST" + currentDat;   //установили путь для бэкапа
        new File(backUpPath).mkdir();                                       //создали папку для бэкапа
        //------------------------------------------------------------------------------------------------------------
        String filePath = globVar.desDir;
        FileManager manager = new FileManager();
        UUID uuid = new UUID();
        XMLSAX configSig = new XMLSAX();
        Node cfs = configSig.readDocument("ConfigSignals.xml");// Открыть configCignals из рабочего каталога программы
        String nodeTable = ft.tableName();
        Node findNode = configSig.returnFirstFinedNode(cfs, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        Node nodeGenData = configSig.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        NodeList nodesGenData = nodeGenData.getChildNodes();
        Node fieldsNode = null;
        String[] oldArray = {"ver", "old"};//массив для добваления атрибута ОЛД
        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                XMLSAX sax = new XMLSAX();
                Node currNodeCfgXML = nodesGenData.item(i);
                String typeName = currNodeCfgXML.getNodeName();//достаю элементы из ноды(в данный момент T GPA AI DRV)
                filePath = "/home/ad/Документы по работе/сиг/"; // тестовое
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
                    type = sax.readDocument(filePath + File.separator + trueName);//прочитал файл в котором нашли совпадения по имени
                    oldFields = sax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
                    sax.setDataAttr(oldFields, "ver", "old");//добавил атрибут ver old
                    String[] newArray = {"Fields"};//, "ver", "new"};
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
                            String nAndA[] = {"Field", "Name", tagName, "Comment", comment, "Type", typeUUID, "UUID", uuid.getUIID()};
                            sax.insertChildNode(newFields, nAndA);
                        } else {
                            sax.setDataAttr(oldTag, "Comment", comment);
                            newFields.appendChild(oldTag);
                        }
                    }
                }
                if(oldFields != null) sax.removeNode(oldFields);
                sax.writeDocument(backUpPath + File.separator + trueName);//записали файл
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
