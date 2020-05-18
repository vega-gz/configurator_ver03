package Generators;

import FrameCreate.FrameTabel;
import XMLTools.*;
import fileTools.*;
import globalData.globVar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Generator {
    @SuppressWarnings("empty-statement")
    public static int genHW(FrameTabel ft) throws IOException {
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать привязки сигналов к аппаратным каналам?"); // сообщение с выбором
        if(casedial != 0) return -2; //0 - yes, 1 - no, 2 - cancel
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        Node findNode = globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if(findNode == null){
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
            return -1;
        }
        Node nodeGenHW = globVar.sax.returnFirstFinedNode(findNode, "GenHW");//Ищем в этой ноде ноду GenData
        if(nodeGenHW == null){
            FileManager.loggerConstructor("В файле ConfigSignals.xml не найдена нода \"" + nodeTable+"/GenHW"+ "\"");
            return -1;
        }
        String drvFileName = abonent + globVar.sax.getDataAttr(nodeGenHW, "drvFile") + ".type";
        String hwFileSuffix;// = globVar.sax.getDataAttr(nodeGenHW, "hwFile");
        //String intFileSuffix = globVar.sax.getDataAttr(nodeGenHW, "intFile");
        String prjFildName = abonent + globVar.sax.getDataAttr(nodeGenHW, "globData");

        String designDir = globVar.desDir  + "\\" + "Design";
        XMLSAX prj = new XMLSAX();
        Node prjNode = prj.readDocument(designDir+"\\Project.prj");
        if(prjNode == null){
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + designDir+"\\Project.prj" + "\"");
            return -1;
        }
        String[] globSigAttr = {"Signal","Name", prjFildName};
        Node globSigInPrj = prj.findNodeAtribute(prjNode, globSigAttr);
        if(globSigInPrj == null){
            FileManager.loggerConstructor("Нет \"" + prjFildName + " \" в глобальных сигналах проекта");
            return -1;
        }
        String globUUID = prj.getDataAttr(globSigInPrj,"UUID");
        
        XMLSAX drv = new XMLSAX();
        Node drvSignalsNode = drv.readDocument(designDir+"\\T_"+drvFileName);
        if(drvSignalsNode == null){
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + designDir+"\\T_"+drvFileName + "\"");
            return -1;
        }
        XMLSAX hw = new XMLSAX();
        String currDev = "";
        //String wfFilePref = designDir + "\\" + abonent + "_R";
        Node hwConn=null;
        String abType = globVar.DB.getDataCell("Abonents","Abonent",abonent,"Abonent_type"); 
        //------ Проход по таблице ----------------
        for (int j = 0; j < ft.tableSize(); j++) {
            String tagName = (String) ft.getCell("TAG_NAME_PLC", j);
            String comment = (String) ft.getCell("Наименование", j);
            String device = (String) ft.getCell("Устройство", j);
            //--------------------- Определяем к какому устройству подключеены каналы ---------------------------------------
            String hwDew = "M_DRV";                              //Поумолчанию - КМ04
            hwFileSuffix = ".km04_cfg";
            if("Sonet".equals(abType)){                         //Если текущий абонент использует Сонет
                hwDew = (String) ft.getCell("sonetModbus", j);  //Смотрим, а не по сонетовскому модбасу подключены модули
                if("".equals(hwDew)){ //Если не по нему,
                    hwDew = abonent + "_R" + device + "_LOCALBUS";  //значит каждое устройство - иной файл HW
                    device = "";
                    hwFileSuffix = ".snt_lb";
                } else {
                    hwFileSuffix = ".snt_mb";
                }
            }
            
            //--------------------------------------------------------------------------------------------------------------
            String slot = (String) ft.getCell("Слот", j);
            String chanell = (String) ft.getCell("Канал", j);
            if(!currDev.equals(hwDew)){
                currDev = hwDew;
                hwConn = setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prj, prjFildName, globSigInPrj, globUUID);
                if(hwConn == null) return -1;
            }
            //вставляем новые коннекты
            String[] signalData = {"Field","Name",tagName};
            Node sigNode = drv.findNodeAtribute(drvSignalsNode, signalData);
            if(sigNode == null) FileManager.loggerConstructor("В структуре \""+prjFildName+"\" не найден сигнал \""+tagName+"\"");
            else{
                String[] findData = {"Connection",  "Device", device,
                                                    "Slot", slot,
                                                    "Channel", chanell}; 
                Node old = hw.findNodeAtribute(hwConn, findData);
                if(old!=null) hw.removeNode(old);
                String[] connData = {"Connection", "ItemName", prjFildName + "." + tagName,
                                                    "Device", device,
                                                    "Slot", slot,
                                                    "Channel", chanell,
                                                    "UUID", globUUID + "." + drv.getDataAttr(sigNode,"UUID"),
                                                    "Comment", comment
                                    }; 
                hw.insertChildNode(hwConn, connData);
            }
        }
        hw.writeDocument();
        return 0;
    }
    static Node setHWdoc(XMLSAX hw, String hwDew, String hwFileSuffix,String[] globSigAttr, XMLSAX prj,
                         String prjFildName, Node globSigInPrj, String globUUID) throws IOException{
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        String designDir = globVar.desDir  + "\\" + "Design\\";        
        hw.writeDocument();
        hw.clear();                                             //удаляем старые коннекты
        String hwFileName = designDir + hwDew +hwFileSuffix;
        String intFileName = designDir + hwDew + ".int";
        int ret = FileManager.copyFileWoReplace(hwFileName, backUpPath+"\\"+hwFileName, true);                    //создаём резервную копию
        if(ret==2){ //Функция копирования не нашла исходного файла
          FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
          return null;
        }
        XMLSAX intFile = new XMLSAX();
        Node intf = intFile.readDocument(intFileName);
        Node lokSig = intFile.findNodeAtribute(intf, globSigAttr);
        if(lokSig==null){
            ret = FileManager.copyFileWoReplace(hwFileName, backUpPath+"\\"+intFileName, true);                    //создаём резервную копию
            if(ret==2){ //Функция копирования не нашла исходного файла
              FileManager.loggerConstructor("Не удалось прочитать файл \"" + intFileName + "\"");
              return null;
            }
            Node interfaceList = intFile.returnFirstFinedNode(intf, "InterfaceList");
            String[] newSigArr = {"Signal","Name",prjFildName,
                                            "Type",prj.getDataAttr(globSigInPrj,"Type"),
                                            "UUID", globUUID,"Usage","","Global","TRUE"};
            intFile.insertChildNode(interfaceList, newSigArr);
            intFile.writeDocument();
        }
        Node hwRoot = hw.readDocument(hwFileName);
        return hw.returnFirstFinedNode(hwRoot, "Crossconnect");
    }

    public static int genSTcode(FrameTabel ft, boolean disableReserve) throws IOException{ //0 -ok, 1 - not source file, 2 -impossible create file
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать Функции обработки и инициализации?"); // сообщение с выбором
        if(casedial != 0) return -2; //0 - yes, 1 - no, 2 - cancel
        //String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        FileManager fm = new FileManager();                                 //создали менеджер файлов
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        
        Node nodeGenCode = globVar.sax.returnFirstFinedNode(globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable), "GenCode");
        if(nodeGenCode == null )return 0;
        String commonFileST = (String) globVar.sax.getDataNode(nodeGenCode).get("file");
        String stFileName = abonent + "_" + commonFileST + ".txt"; //Для каждого файла
        String srcFile = globVar.desDir + "\\" + "GenST" + "\\" + stFileName;
        String tmpFile = globVar.desDir + "\\" + "GenST" + "\\" + stFileName + "_tmp";
        
        int ret = fm.createFile2write(tmpFile); //открываем файл на запись
        if(ret!=0){
            FileManager.loggerConstructor("Не удалось создать файл \"" + tmpFile + "\"");
            return -2;
        }

        ret = fm.openFile4read(globVar.myDir, commonFileST + ".txt");
        if(ret!=0){
            FileManager.loggerConstructor("Не удалось прочитать служебный файл \""+srcFile+globVar.myDir+"\\"+commonFileST+".txt");
            return -1;
        }
        fm.wr("<Data>\n<Function UUID=\"" + UUID.getUIID() + "\" Name=\"" + abonent + 
                "_"+ commonFileST +"\" ResultTypeUUID=\"EC797BDD4541F500AD80A78F1F991834\">\n");

        ArrayList<Node> funclist = globVar.sax.getHeirNode(nodeGenCode);      //Создали список функций для генерации. Каждую функцию надо сгенерить по числу строк в таблице
        for(Node genSTnode : funclist){                                 //перебираем функции
            String stFunc = (String) globVar.sax.getDataNode(genSTnode).get("name");  //вычитываем её имя
            Node args = globVar.sax.returnFirstFinedNode(genSTnode, "arguments");     //Находим ноду с аргументами
            ArrayList<Node> arglist = globVar.sax.getHeirNode(args);                  //создаём список аргументов
            String s = fm.rd();                                                     //Для копирования всего, что было до этой функции, 
            while(!fm.EOF && !s.contains(stFunc)){
                fm.wr(s + "\n");                          //ищем в исходнои файле её первое вхождение
                s = fm.rd();
            }
            fm.wr("//Начало сгенерированного кода "+stFunc+"\n");
            for (int j = 0; j < ft.tableSize(); j++) {                      //Цикл по всем строкам таблицы
               String tmp = "";
               for(Node arg : arglist){                                        //Цикл по всем аргументам функции
                    ArrayList<Node> argParts = globVar.sax.getHeirNode(arg);
                    tmp += ",";                                                //аргумент записан и отделён от следующего запятой
                    for(Node argPart : argParts){                                   //Цикл по всем частям аргументов - текстовым и табличным
                        if("text".equals(argPart.getNodeName())) tmp += (String) globVar.sax.getDataNode(argPart).get("t");
                        else if("dbd".equals(argPart.getNodeName())){
                            //String colName = (String) globVar.sax.getDataNode(argPart).get("t");
                            //System.out.println(colName);
                            tmp += (String) ft.getCell((String) globVar.sax.getDataNode(argPart).get("t"),j);
                        }
                        else if("npp".equals(argPart.getNodeName())) tmp += j;
                        else if("abonent".equals(argPart.getNodeName())) tmp += abonent;
                    }
                }            
                String disable = "";
                if(disableReserve && ((String)ft.getCell("TAG_NAME_PLC", j)).contains("Res_")) disable = "//";
                                                                                     //Убираем лишнюю запятую в конце
                fm.wr("//"+(String)ft.getCell("Наименование", j)+"\n"+disable+stFunc+"("+tmp.substring(1)+");\n");  //записываем вызов функции в файл
            }
            //пролистываем в исходном файле строки со старыми вызовами и пустые строки 
            while(!fm.EOF  && !s.contains("Конец сгенерированного кода")) s = fm.rd(); 
            while(!fm.EOF){                                                 //дописываем хвост файла
                fm.wr(s + "\n");                          
                s = fm.rd();
            }
            fm.closeRdStream();                                       //закрываем поток чтения
            fm.closeWrStream();                                       //закрываем поток записи
            File file = new File(srcFile);                             //создаём ссылку на исходный файл
            file.delete();                                             //удаляем его
            new File(tmpFile).renameTo(file);                          //создаём ссылку на сгенерированный файл и делаем его исходным
            fm.openFile4read(globVar.desDir + "\\" + "GenST", stFileName);              //открываем его на чтенье
            fm.createFile2write(globVar.desDir + "\\" + "GenST", stFileName + "_tmp");  //открываем временный файл для генерации
        }
        fm.closeRdStream();                                       //закрываем поток чтения
        fm.closeWrStream();                                       //закрываем поток записи

        new File(tmpFile).delete();                          //Удаляем временный файл
        return 0;
    }

    public static int GenTypeFile(FrameTabel ft) throws IOException {//0-norm, -1 - not find node
        int casedial = JOptionPane.showConfirmDialog(null, "Файлы .TYPE для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        if(casedial != 0) return 0; //0 - yes, 1 - no, 2 - cancel
        String backUpPath = globVar.backupDir + "\\";   //установили путь для бэкапа
        String filePath = globVar.desDir + "\\Design";
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        if(y>0){
            subAb = nodeTable.substring(0,y+1);
            nodeTable = nodeTable.substring(y+1);
            isMb = "mb_";
        }
        Node findNode = globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if(findNode == null){
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
            return -1;
        }
        Node nodeGenData = globVar.sax.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        if(nodeGenData == null){
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable+"/GenData\"");
            return -1;
        }
        XMLSAX prjSax = new XMLSAX();
        Node prjRoot = prjSax.readDocument(filePath + "\\Project.prj");;
        Node interfaceList = prjSax.returnFirstFinedNode(prjRoot, "Globals");
        NodeList nodesGenData = nodeGenData.getChildNodes();
        for (int i = 0; i < nodesGenData.getLength(); i++) {//получил размерность ноды и начал цикл
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                XMLSAX localSax = new XMLSAX();
                Node currNodeCfgXML = nodesGenData.item(i);
                ArrayList<Node> globSigList = globVar.sax.getHeirNode(currNodeCfgXML);//Находим все ноды
                String nodeName = currNodeCfgXML.getNodeName();
                String typeName = "T_"+abonent+"_"+subAb+isMb+nodeName;//достаю элементы из ноды(в данный момент T GPA AI DRV)
                String trueName = FileManager.FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
                String fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "Type");
                String typeUUID;
                if(fildUUID == null){
                    fildUUID = FileManager.getUUIDFromFile(filePath,"T_"+isMb+nodeName);
                    if(fildUUID==null){
                        JOptionPane.showMessageDialog(null, "Не найден файл типа данных "+"T_"+isMb+nodeName +". Генерация прервана.");
                         return -1;
                    }
                }
                Node newFields;// = null;
                Node type;
                Node oldFields = null;
                if (trueName == null) {//помещаем сюда создание файла
                    trueName = typeName + ".type";
                    type = localSax.createDocument("Type");
                    typeUUID = UUID.getUIID();
                    localSax.setDataAttr(type, "UUID", typeUUID);
                    localSax.setDataAttr(type, "Name", typeName);
                    localSax.setDataAttr(type, "Kind", "Struct");
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                } else {//сюда помещаем добавление
                    FileManager.copyFileWoReplace(filePath + "\\" + trueName, backUpPath + trueName, true);
                    type = localSax.readDocument(filePath + "\\" + trueName);//прочитал файл в котором нашли совпадения по имени
                    typeUUID = type.getAttributes().getNamedItem("UUID").getNodeValue();
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
                            String nAndA[] = {"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", UUID.getUIID()};
                            localSax.insertChildNode(newFields, nAndA);
                        } else {
                            localSax.setDataAttr(oldTag, "Comment", comment);
                            newFields.appendChild(oldTag);
                        }
                    }
                }
                if(oldFields != null) localSax.removeNode(oldFields);
                localSax.writeDocument(filePath + "\\" + trueName);//записали файл
                //fmProject.wr("<Signal Name=\"" + abonent+"_"+nodeName+"\" UUID=\""+ttt+"\" Type=\"" + typeUUID +"\" Global=\"TRUE\" />\n");
                for(Node globSig: globSigList){
                    String name = globVar.sax.getDataAttr(globSig, "name");
                    String globUUID = null;
                    if(name!=null)
                        globUUID = insertVarInPrj(prjSax, interfaceList, abonent+"_"+subAb+isMb+name, typeUUID, "", true, false, null,
                                       filePath + "\\Project.prj", backUpPath + "Project.prj");
                    ArrayList<Node> localSigList = globVar.sax.getHeirNode(globSig);//Находим все ноды
                    boolean glob = true;
                    for(Node localSig: localSigList){
                        if(name==null || !glob){
                            name = globVar.sax.getDataAttr(localSig, "name");
                            glob = false;
                            if(name==null){
                                FileManager.loggerConstructor("Не найдено имя переменной в разделе "+
                                        nodeName + "/GenData/"+globSig.getNodeName()+"/"+localSig.getNodeName());
                                return -1;
                            }
                        }
                        //int nf = Integer.parseInt(globVar.sax.getDataAttr(localSig, "int"));
                        XMLSAX localSigSax = new XMLSAX();
                        String localDocName = globVar.sax.getDataAttr(localSig, "int");
                        if(localDocName==null){
                            FileManager.loggerConstructor("Не найдено имя файла .int в разделе "+
                                    nodeName + "/GenData/"+globSig.getNodeName()+"/"+localSig.getNodeName());
                            return -1;
                        }
                        if("_".equals(localDocName.substring(0,1))) localDocName = abonent+localDocName;
                        Node localRoot = localSigSax.readDocument(filePath+"\\"+localDocName);
                        Node localInterfaceList = localSigSax.returnFirstFinedNode(localRoot, "InterfaceList");
                        insertVarInPrj(localSigSax, localInterfaceList, abonent+"_"+subAb+isMb+name, typeUUID, "", glob, true,
                                       globUUID, filePath+"\\"+localDocName, backUpPath + localDocName);
                    }
                }
            }

        }
        //fmProject.closeWrStream();
        return 0;
    }
    
    public static String GenHMI(FrameTabel ft) throws IOException {
        int casedial = JOptionPane.showConfirmDialog(null, "Листы мнемосхем для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        if(casedial != 0) return ""; //0 - yes, 1 - no, 2 - cancel
        String targetFile;// = null;
        String nameTable = ft.tableName();//нашли ai ao do и тд
        int x = nameTable.indexOf("_");
        String abonent = nameTable.substring(0,x);
        String nodeTable = nameTable.substring(x+1);
        String subGroup = ""; //Для обозначения подгруппы сигнала, например "FR"
        XMLSAX HMIcfg = new XMLSAX();
        Node hmiCfgRoot = HMIcfg.readDocument("ConfigHMI.xml"); //AT_HMI.iec_hmi
        Node findNode = HMIcfg.returnFirstFinedNode(hmiCfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if (findNode == null) {
            FileManager.loggerConstructor("Тип данных "+nodeTable+" не описан в файле ConfigSignals.xml");
            return null;
        }//Если не вылетели - значит будет генерация
        XMLSAX bigSax = new XMLSAX();
        String hmiProjectFile = globVar.desDir + "\\" + "Design" + "\\" + HMIcfg.getDataAttr(hmiCfgRoot, "project"); // Нужен для поиска УУИДов листов
        Node bigRoot = bigSax.readDocument(hmiProjectFile);
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта "+hmiProjectFile);
            return null;
        }//Если не вылетели - значит будет генерация
        
        String filePath = globVar.desDir + "\\" + "GenHMI"; //Установили путь для файлов
        
        ArrayList<Node> hmiNodeList = HMIcfg.getHeirNode(findNode);//Находим все ноды
        String ret = "";
        for(Node hmiNode: hmiNodeList){
            //hmiNode = HMIcfg.returnFirstFinedNode(findNode, "GenHMI");//В конфигурации нашли описание для ЧМИ
            if (!"GenHMI".equals(hmiNode.getNodeName())) {
                FileManager.loggerConstructor("Для типа данных "+nodeTable+" не описана генерация листов ЧМИ");
                return null;
            }//Если не вылетели - значит будет генерация

            String typeGCT = HMIcfg.getDataAttr(hmiNode, "type");

            String uuidGCT = null;//HMIcfg.getDataAttr(hmiNode, "typeUUID");
            String[] findInBig = {"GraphicsCompositeFBType","Name",typeGCT};
            Node myBlock = bigSax.findNodeAtribute(bigRoot, findInBig);
            if(myBlock!=null) uuidGCT = bigSax.getDataAttr(myBlock, "UUID");
            else {
                FileManager.loggerConstructor("В проекте нет блока "+typeGCT);
                return null;
            }

            String uuidNameRuGCT = "D11E59B74DBD1F394957C4876E4DCD20";//HMIcfg.getDataAttr(hmiNode, "nameRuUUID");
            String uuidPrefAbGCT = "7B21228345FE145C1039349D18AF2C71";//HMIcfg.getDataAttr(hmiNode, "prefAbUUID");

            Integer maxX=null, maxY=null;
            Double startPosX=null, startPosY=null, incX=null, incY=null;
            String tmp = HMIcfg.getDataAttr(hmiNode, "maxX");
            if(tmp!=null) maxX = Integer.parseInt(tmp);
            tmp = HMIcfg.getDataAttr(hmiNode, "maxY");
            if(tmp!=null) maxY = Integer.parseInt(tmp);
            tmp = HMIcfg.getDataAttr(hmiNode, "startPosX");
            if(tmp!=null) startPosX = Double.parseDouble(tmp);
            tmp = HMIcfg.getDataAttr(hmiNode, "startPosY");
            if(tmp!=null) startPosY = Double.parseDouble(tmp);
            tmp = HMIcfg.getDataAttr(hmiNode, "incX");
            if(tmp!=null) incX = Double.parseDouble(tmp);
            tmp = HMIcfg.getDataAttr(hmiNode, "incY");
            if(tmp!=null) incY = Double.parseDouble(tmp);
            if(maxX==null || maxY==null || startPosX==null || startPosY==null || incX==null || incY==null){
                FileManager.loggerConstructor("Для типа данных "+nodeTable+" не полностью описаны координаты элементов ЧМИ");
                return null;
            }

            Node addVar = HMIcfg.returnFirstFinedNode(hmiNode, "additionalVar");
            String[][] addVarsData = null;
            if(addVar!=null){
                ArrayList<Node> addVars = HMIcfg.getHeirNode(addVar);
                addVarsData = new String[addVars.size()][4];
                int i=0;
                for(Node av: addVars){
                    addVarsData[i][0] = av.getNodeName();
                    addVarsData[i][1] = HMIcfg.getDataAttr(av, "tableCol");
                    addVarsData[i][2] = HMIcfg.getDataAttr(av, "Type");
                    addVarsData[i][3] = HMIcfg.getDataAttr(av, "TypeUUID");
                    i++;
                }
             }

            ArrayList<String> hintAL = new ArrayList<>();
            Node hintNode = HMIcfg.returnFirstFinedNode(hmiNode, "Hint");
            if(hintNode!=null){
                String addCol = HMIcfg.getDataAttr(hintNode, "add0");
                for(int i=1; i<100 && addCol != null; i++){
                    hintAL.add(addCol);
                    addCol = HMIcfg.getDataAttr(hintNode, "add"+i);
                }
            }
            targetFile = HMIcfg.getDataAttr(hmiNode, "file");//получили имя файла в который записываю верхний уровень,который в последствии читаем AI_HMI_inc
            String folderNodeName=null;
            String nameGCTcommon = HMIcfg.getDataAttr(hmiNode, "name");
            String nameGCT = abonent +"_"+ nameGCTcommon;
            String pageName = nameGCT+"1";
            Node gctNode;
            String sourceFile;
            XMLSAX HMIsax = new XMLSAX();
            findInBig[2] = pageName;
            FileManager fm = new FileManager();
            if(targetFile==null){
                sourceFile = "root\\HMI_Sheet.txt";
                targetFile = filePath + "\\" + pageName +".txt";//если имени в ноде нет - конструируем имя
                Node hmiRoot = HMIsax.readDocument("HMI_Sheet.txt");
                gctNode = HMIsax.returnFirstFinedNode(hmiRoot, "GraphicsCompositeFBType");
                HMIsax.setDataAttr(gctNode, "Name", pageName);
                String sheetUUID = null;
                Node myPage = bigSax.findNodeAtribute(bigRoot, findInBig);
                //System.out.println("Print 2:"+sheetUUID);//------------------------------------------ check print ----------------------------
                if(myPage!=null) sheetUUID = bigSax.getDataAttr(myPage, "UUID");
                if(sheetUUID==null) sheetUUID = UUID.getUIID();
                HMIsax.setDataAttr(gctNode, "UUID", sheetUUID);
            }else{
                sourceFile = targetFile;
                gctNode = null;
            }
            if(gctNode==null){
                FileManager.loggerConstructor("Не найдена нода GraphicsCompositeFBType в " + sourceFile);
                return null;
            }
            //Определяем УУИДы этого блока ---
            String arrForFindNode[] = {"VarDeclaration", "Name", "NameRu"};
            //Node il = HMIsax.findNodeValue(gctNode, arrForFindNode);
            String NameRuUUID = "31E704A94C1D0BCA16C48C8F563CAB4B";//il.getAttributes().getNamedItem("UUID").getNodeValue();
            arrForFindNode[2] = "PrefAb";
            //il = HMIsax.findNodeValue(gctNode, arrForFindNode);
            String PrefAbUUID = "7DF53A3B47B1075B9D3AE78253FC271B";//il.getAttributes().getNamedItem("UUID").getNodeValue();

            Node FBNetwork = HMIsax.returnFirstFinedNode(gctNode, "FBNetwork");         //нашел FBNetwork
            if(FBNetwork!=null) HMIsax.removeNode(FBNetwork);                           //удалили содержимое ноды FBNetwork
            FBNetwork = HMIsax.insertChildNode(gctNode, "FBNetwork");
            Node DataConnections = HMIsax.createNode("DataConnections");
            int fbX = 0, fbY = 0, col = 1, row = 1;
            Double posElemX = startPosX, posElemY = startPosY;
            int pageCnt = 1;
            String nameCol = HMIcfg.getDataAttr(hmiNode, "ruName");
            boolean isAlarm = HMIcfg.getDataAttr(hmiNode, "isAlarm")!=null;
            //-------------------- начинаем цикл по строкам таблицы ------------------------------------
            for (int i = 0; i < ft.tableSize(); i++) {
                String ruName = (String) ft.getCell(nameCol, i);
                if(!"".equals(ruName)){
                    //-- конструируем ФБ
                    String fbUUID = UUID.getUIID().toUpperCase();
                    String[] fbData = {"FB","Name",     abonent + typeGCT + "_"+ i, 
                                            "Type",     typeGCT, 
                                            "TypeUUID", uuidGCT,
                                            "UUID",     fbUUID, 
                                            "X",        ""+fbX, 
                                            "Y",        ""+fbY};
                    Node nodeFB = HMIsax.insertChildNode(FBNetwork, fbData);
                    fbX += 350; if(fbX > 1200){fbY+=420; fbX = 0;}//распределение ФБ по листу редактора Сонаты
                    //-- Начинаем заполнять ФБ содержимым
                    String fbChildNode[] = {"VarValue", 
                                            "Variable", "Name", 
                                            "Value",    "'" + ruName + "'",
                                            "Type",     "STRING", 
                                            "TypeUUID", "38FDDE3B442D86554C56C884065F87B7"};//создали массив элемента вариабле ПОС
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду

                    String tagName = (String) ft.getCell("TAG_NAME_PLC", i);
                    fbChildNode[2] = "NameAlg";
                    fbChildNode[4] = "'" + tagName + "'";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    fbChildNode[2] = "PrefStr";
                    fbChildNode[4] = "'" + subGroup + "'";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    if(isAlarm){
                        fbChildNode[2] = "TagID";
                        fbChildNode[4] = "'" + nodeTable + tagName + "'";
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    }
                    fbChildNode[2] = "Num";
                    fbChildNode[4] = "'" + (i+1) + "'";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    //Create Hint
                    String hint = "";
                    for(String hintPart : hintAL){
                        String s = (String) ft.getCell(hintPart, i);
                        if(s==null) s = hintPart;
                        hint += s;
                    }
                    fbChildNode[2] = "hint";
                    fbChildNode[4] = "'" + hint + "'";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду

                    if(isAlarm){
                        String disableAlarm = "FALSE";
                        String visiblePar = "TRUE";
                        if(tagName.contains("Res_")){disableAlarm = "TRUE"; visiblePar = "FALSE";}
                        fbChildNode[2] = "visiblePar";
                        fbChildNode[4] = visiblePar;
                        fbChildNode[6] = "BOOL";
                        fbChildNode[8] = "EC797BDD4541F500AD80A78F1F991834";
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                        fbChildNode[2] = "disableAlarm";
                        fbChildNode[4] = disableAlarm;
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    }
                    fbChildNode[2] = "pos";
                    fbChildNode[4] = "(x:=" + posElemX + ",y:=" + posElemY + ")";
                    fbChildNode[6] = "TPos";
                    fbChildNode[8] = "17C82815436383728D79DA8F2EF7CAF2";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду

                    if(addVarsData!=null) for(int j=0; j<addVarsData.length; j++){
                        fbChildNode[2] = addVarsData[j][0];
                        String apos = "";
                        if("STRING".equals(addVarsData[j][2])) apos = "'";
                        fbChildNode[4] = apos + (String) ft.getCell( addVarsData[j][1], i) + apos;
                        fbChildNode[6] =  addVarsData[j][2];
                        fbChildNode[8] =  addVarsData[j][3];
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    }

                    String[] connects = {"Connection",  "Source" , "PrefAb" , 
                                                        "Destination" , nameGCT + i + ".PrefAb" , 
                                                        "SourceUUID" , uuidPrefAbGCT,
                                                        "DestinationUUID" , fbUUID + "." + PrefAbUUID};
                    HMIsax.insertChildNode(DataConnections, connects);//добавили его в ноду
                    
                    if(isAlarm){
                        connects[2] = "NameRU";
                        connects[4] = nameGCT + i + ".NameRU";
                        connects[6] = uuidNameRuGCT;
                        connects[8] = fbUUID + "." + NameRuUUID;
                        HMIsax.insertChildNode(DataConnections, connects);//добавили его в ноду
                    }
                    posElemY += incY; 
                    row++;
                    if(row > maxY){
                        posElemX += incX; 
                        posElemY = startPosY; 
                        col++;
                        row = 1;
                    } // делим на страницы     
                    if(col > maxX && i < ft.tableSize()-1){
                        if(folderNodeName==null){//Если нет имени фолдера листов сигналов - значит мы делаем файл для импорта
                            FBNetwork.appendChild(DataConnections);
                            HMIsax.writeDocument(targetFile);
                            HMIsax.clear();
                            targetFile = targetFile.replace("_"+pageCnt+".txt", "_"+(pageCnt+1)+".txt");
                            pageCnt++;
                            pageName = nameGCT+pageCnt;
                            Node hmiRoot = HMIsax.readDocument("HMI_Sheet.txt");
                            gctNode = HMIsax.returnFirstFinedNode(hmiRoot, "GraphicsCompositeFBType");
                            HMIsax.setDataAttr(gctNode, "Name", pageName);
                            String sheetUUID = null;
                            findInBig[2] = pageName;
                            Node myPage = bigSax.findNodeAtribute(bigRoot, findInBig);
                            if(myPage!=null) sheetUUID = bigSax.getDataAttr(myPage, "UUID");
                            if(sheetUUID==null)sheetUUID = UUID.getUIID();
                            HMIsax.setDataAttr(gctNode, "UUID", sheetUUID);
                            FBNetwork = HMIsax.insertChildNode(gctNode, "FBNetwork");
                            DataConnections = HMIsax.createNode("DataConnections");
                            fbX = 0;
                            fbY = 0;
                            posElemX = startPosX;
                            posElemY = startPosY;
                            col = 1;
                            row = 1;
                        }else{
                            //Здесь должны быть методы добавления листов непосредственно в сонатовский файл
                        }
                    }
                }
            }
            FBNetwork.appendChild(DataConnections);
            HMIsax.writeDocument(targetFile);
            ret += nameGCT + " - " +pageCnt + " страниц. ";
        }
        return ret;
    }
    
//Функция занесения переменных в интерфейсные листы приложений Сонаты
// <Signal Name="GPA_DI_Settings" UUID="6DC2E85F4B6835B2209D6D8076F22EFF" Type="9D3CCA014F76318C4B750981ED2CEA67" Usage="" Global="TRUE" Comment="настройки дискретного канала" />
    static String insertVarInPrj(XMLSAX intFile, Node interfaceList, String Name, String Type, String Comment, boolean global, boolean usage, 
            String uuid, String hwFileName, String backUpFile) throws IOException{
        String[] findArr = {"Signal","Name",Name};
        Node sig = intFile.findNodeAtribute(interfaceList, findArr);
        if(sig!=null){
            if(uuid!=null){
                intFile.setDataAttr(sig, "UUID", uuid);
                return uuid;
            }else return intFile.getDataAttr(sig,"UUID");
        }
        int  ret = FileManager.copyFileWoReplace(hwFileName, backUpFile, true);                    //создаём резервную копию
        if(ret==2){ //Функция копирования не нашла исходного файла
          FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
          return null;
        }
        if(uuid==null) uuid = UUID.getUIID();
        String[] insArr = {"Signal","Name",Name,
                                    "UUID",uuid,
                                    "Type",Type,
                                    "Comment",Comment,
        };
        sig = intFile.insertChildNode(interfaceList, insArr);
        if(global) intFile.setDataAttr(sig, "Global", "TRUE");
        if(usage) intFile.setDataAttr(sig, "Usage", "");
        intFile.writeDocument();
        return uuid;
    }
}
