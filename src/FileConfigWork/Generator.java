package FileConfigWork;

//import FrameCreate.*;

import FrameCreate.FrameTabel;
import XMLTools.*;
//import XMLTools.XMLSAX;
import fileTools.*;
import static fileTools.FileManager.FindFile;
import globalData.globVar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*@author Григорий*/
public class Generator {
    @SuppressWarnings("empty-statement")
    
    public static int genSTcode(FrameTabel ft) throws IOException{
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        FileManager fm = new FileManager();                                 //создали менеджер файлов
        //XMLSAX configSig = new XMLSAX();                                    //создали менеджер для ХМЛ
        String tmpFile = "";
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        Node nodeGenCode = globVar.sax.returnFirstFinedNode(globVar.sax.returnFirstFinedNode(globVar.cfgRoot, ft.tableName()), "GenCode");
        if(nodeGenCode == null )return 0;
        ArrayList<Node> stFiles = globVar.sax.getHeirNode(nodeGenCode);      //Создали список файлов для генерации.
        for(Node stFile : stFiles){                                         //Перебираем файлы
            String stFileName = (String) globVar.sax.getDataNode(stFile).get("name"); //Для каждого файла
            //Сохраняем бэкапную копию. Если в текущем бэкапном каталоге уже есть такой файл - оставляем его
            String srcFile = globVar.desDir + File.separator + "Design" + File.separator + stFileName;
            tmpFile = globVar.desDir + File.separator + "Design" + File.separator + stFileName + "_tmp";
            
            fm.copyFileWoReplace(srcFile, backUpPath + File.separator + stFileName, true);                    //создаём резервную копию
            int ret = fm.openFile4read(globVar.desDir + File.separator + "Design", stFileName);         //открываем её на чтенье
            if(ret!=0){
                FileManager.loggerConstructor("Не удалось прочитать файл \"" + srcFile + "\"");
                break;
            }
            ret = fm.createFile2write(globVar.desDir + File.separator + "Design", stFileName + "_tmp"); //открываем файл на запись
            if(ret!=0){
                FileManager.loggerConstructor("Не удалось создать файл \"" + tmpFile + "\"");
                break;
            }
            ArrayList<Node> funclist = globVar.sax.getHeirNode(stFile);      //Создали список функций для генерации. Каждую функцию надо сгенерить по числу строк в таблице
            for(Node genSTnode : funclist){                                 //перебираем функции
                String stFunc = (String) globVar.sax.getDataNode(genSTnode).get("name");  //вычитываем её имя
                int casedial = JOptionPane.showConfirmDialog(null, "Функции " + stFunc + " генерировать?"); // сообщение с выбором
                if(casedial == 0){ //0 - yes, 1 - no, 2 - cancel
                    Node args = globVar.sax.returnFirstFinedNode(genSTnode, "arguments");     //Находим ноду с аргументами
                    ArrayList<Node> arglist = globVar.sax.getHeirNode(args);                  //создаём список аргументов
                    String s = fm.rd();                                                     //Для копирования всего, что было до этой функции, 
                    while(!fm.EOF && !s.contains(stFunc)){
                        fm.wr(s + "\n");                          //ищем в исходнои файле её первое вхождение
                        s = fm.rd();
                    }
                    if(s.contains("<![CDATA[")){
                        int end = s.indexOf("<![CDATA[") + 9;
                        fm.wr(s.substring(0,end) + "\n");
                    }
                    String funcCall = stFunc + "(";                                 //Начинаем генерацию вызова функции
                    for (int j = 0; j < ft.tableSize(); j++) {                      //Цикл по всем строкам таблицы
                        for(Node arg : arglist){                                        //Цикл по всем аргументам функции
                            ArrayList<Node> argParts = globVar.sax.getHeirNode(arg);
                            for(Node argPart : argParts){                                   //Цикл по всем частям аргументов - текстовым и табличным
                                if("text".equals(argPart.getNodeName())) funcCall += (String) globVar.sax.getDataNode(argPart).get("t");
                                else if("dbd".equals(argPart.getNodeName())) funcCall += (String) ft.getCell((String) globVar.sax.getDataNode(argPart).get("t"),j);
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
                    fm.rdStream.close();                                       //закрываем поток чтения
                    fm.wrStream.close();                                       //закрываем поток записи
                    File file = new File(srcFile);                             //создаём ссылку на исходный файл
                    file.delete();                                             //удаляем его
                    new File(tmpFile).renameTo(file);                          //создаём ссылку на сгенерированный файл и делаем его исходным
                    fm.openFile4read(globVar.desDir + File.separator + "Design", stFileName);              //открываем его на чтенье
                    fm.createFile2write(globVar.desDir + File.separator + "Design", stFileName + "_tmp");  //открываем временный файл для генерации
                }else if(casedial != 1) return -2; //0 - yes, 1 - no, 2 - cancel
            }
            fm.rdStream.close();
            fm.wrStream.close();
        }
        new File(tmpFile).delete();                          //создаём ссылку на сгенерированный файл и делаем его исходным
        return 0;
    }

    public static int GenTypeFile(FrameTabel ft) throws IOException {//0-norm, -1 - not find node
        //int casedial = JOptionPane.showConfirmDialog(null, "Файлы .TYPE для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        //if(casedial != 0) return 0; //0 - yes, 1 - no, 2 - cancel
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        String filePath = globVar.desDir + File.separator + "Design";
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        Node findNode = globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if(findNode == null){
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
            return -1;
        }
        Node nodeGenData = globVar.sax.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        if(nodeGenData == null){
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable+"/GenData"+ "\"");
            return -1;
        }
        NodeList nodesGenData = nodeGenData.getChildNodes();
        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                XMLSAX localSax = new XMLSAX();
                Node currNodeCfgXML = nodesGenData.item(i);
                String nodeName = currNodeCfgXML.getNodeName();
                String typeName = "T_"+abonent+"_"+nodeName;//достаю элементы из ноды(в данный момент T GPA AI DRV)
                String trueName = FileManager.FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
                String fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "Type");
                if(fildUUID == null){
                    fildUUID = FileManager.getUUIDFromFile(filePath,"T_"+nodeName);
                    if(fildUUID==null){
                        JOptionPane.showMessageDialog(null, "Не найден файл типа данных "+ ". Генерация прервана.");
                         return -1;
                    }
                }
                Node newFields;// = null;
                Node type;
                Node oldFields = null;
                if (trueName == null) {//помещаем сюда создание файла
                    trueName = typeName + ".type";
                    type = localSax.createDocument("Type");
                    localSax.setDataAttr(type, "UUID", UUID.getUIID());
                    localSax.setDataAttr(type, "Name", typeName);
                    localSax.setDataAttr(type, "Kind", "Struct");
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                } else {//сюда помещаем добавление
                    FileManager.copyFileWoReplace(filePath + File.separator + trueName, backUpPath + File.separator + trueName, true);
                    type = localSax.readDocument(filePath + File.separator + trueName);//прочитал файл в котором нашли совпадения по имени
                    oldFields = localSax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                    Node firstFields = localSax.returnFirstFinedNode(oldFields, "Field");
                    fildUUID = firstFields.getAttributes().getNamedItem("Type").getNodeValue();//получаю значение ноды type
                }
                for (int j = 0; j < ft.tableSize(); j++) {
                    String tagName = (String) ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                    String comment = (String) ft.getCell("Наименование", j);//получаем НАИМЕНОВАНИЕ из таблицы
                    if (oldFields == null) {//если нода пустая,то создаю элементы
                        String nAndA[] = {"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", UUID.getUIID()};
                        localSax.insertChildNode(newFields, nAndA);
                    } else {
                        String[] nodeAndAttr = {"Field", "Name", tagName};
                        Node oldTag = localSax.findNodeAtribute(oldFields, nodeAndAttr);
                         if (oldTag == null) {
                            //String sha128hex = org.apache.commons.codec.digest.DigestUtils.sha128Hex(typeName + tagName);
                            String nAndA[] = {"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", UUID.getUIID()};
                            localSax.insertChildNode(newFields, nAndA);
                        } else {
                            localSax.setDataAttr(oldTag, "Comment", comment);
                            Node aC = newFields.appendChild(oldTag);
                        }
                    }
                }
                if(oldFields != null) localSax.removeNode(oldFields);
                localSax.writeDocument(filePath + File.separator + trueName);//записали файл
            }

        }
        return 0;
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
