package Generators;

import Tools.FileManager;
import FrameCreate.TableDB;
import Tools.StrTools;
import Tools.Tools;
import XMLTools.*;
import globalData.globVar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Generator {
    @SuppressWarnings("empty-statement")
    public static int genHW(TableDB ft, JProgressBar jProgressBar) throws IOException {
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать привязки сигналов к аппаратным каналам?"); // сообщение с выбором
        if(casedial != 0) return -2; //0 - yes, 1 - no, 2 - cancel
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        //String group = "";
        boolean isModbus = false;
        if(y>0){
            subAb = "_"+nodeTable.substring(0,y);
            nodeTable = nodeTable.substring(y+1);
            isMb = "_mb";
            isModbus = true;
        }
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
        String mainDriverName = globVar.sax.getDataAttr(nodeGenHW, "hwDriver");

        String designDir = globVar.desDir  + File.separator + "Design";
        XMLSAX prj = new XMLSAX();
        Node prjNode = prj.readDocument(designDir+File.separator+"Project.prj");
        if(prjNode == null){
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + designDir+File.separator+"Project.prj" + "\"");
            return -1;
        }
        
        String abType = globVar.DB.getDataCell("Abonents","Abonent",abonent,"Abonent_type"); 
        //---------------------- Определение списка экземпляров ------------------------------
        String exemplars = globVar.DB.getDataCell("Abonents","Abonent",abonent,"Экземпляры"); 
        ArrayList<String> exArr = new ArrayList<>();
        if(!exemplars.isEmpty()) exArr = StrTools.getListFromString(exemplars, ",");
        else exArr.add(abonent);
        //------------------ Определение параметров драйвера модбаса -----------------------
        String tabComm = globVar.DB.getCommentTable(abonent+subAb+"_"+nodeTable);
        if(tabComm==null) tabComm ="";
        String modbusFile = "";
        String modbusAddr = "";
        String group = "";
        x = tabComm.indexOf("Modbus:");
        if(x >= 0){
            int l = tabComm.length();
            x+=7;
            if(l < x){
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в коментарии \"" + tabComm+ "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
            y = tabComm.indexOf(";",x);
            if(y < 0){
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в коментарии \"" + tabComm+ "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
            modbusFile = tabComm.substring(x, y).trim();
            x = y+1;
            if(l < x){
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в коментарии \"" + tabComm+ "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
            y = tabComm.indexOf(";",x);
            if(y>x) modbusAddr = tabComm.substring(x, y).trim();
            
            x = y+1;
            if(l < x){
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в коментарии \"" + tabComm+ "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
            y = tabComm.indexOf(";",x);
            if(y>x) group = tabComm.substring(x, y).trim();
            else{
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в коментарии \"" + tabComm+ "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
        }
        //--- Определяем файл с описание
        String drvFileName = "T_"+abonent+subAb+isMb+globVar.sax.getDataAttr(nodeGenHW, "drvFile")+".type";
        //------ Проход по таблице ----------------
        for(String exemplar: exArr){
            String prjFildName = exemplar+subAb+isMb+globVar.sax.getDataAttr(nodeGenHW, "globData");
            String[] globSigAttr = {"Signal","Name", prjFildName};
            Node globSigInPrj = prj.findNodeAtribute(prjNode, globSigAttr);
            if(globSigInPrj == null){
                FileManager.loggerConstructor("Нет \"" + prjFildName + " \" в глобальных сигналах проекта");
                return -1;
            }
            String globUUID = prj.getDataAttr(globSigInPrj,"UUID");

            XMLSAX drv = new XMLSAX();
            Node drvSignalsNode = drv.readDocument(designDir+File.separator+drvFileName);
            if(drvSignalsNode == null){
                FileManager.loggerConstructor("Не удалось прочитать файл \""+designDir+File.separator+drvFileName + "\"");
                return -1;
            }
            XMLSAX hw = new XMLSAX();
            String currDev = "";
            //String wfFilePref = designDir + File.separator + abonent + "_R";
            Node hwConn=null;
            int jpgMax = ft.tableSize();
            for (int j = 0; j < jpgMax; j++) {
                if(jProgressBar!=null) jProgressBar.setValue((int)((j+1)*100.0/jpgMax));
                String tagName = ft.getCell("TAG_NAME_PLC", j);
                String comment = ft.getCell("Наименование", j);
                String device = ft.getCell("Устройство", j);
                //--------------------- Определяем к какому устройству подключеены каналы ---------------------------------------
                String hwDew = exemplar+"_"+mainDriverName;                              //Поумолчанию - КМ04
                String hwFileSuffix = ".km04_cfg";
                String slot;
                String chanell;
                if(isModbus){
                    hwDew = exemplar+"_"+modbusFile;  //Смотрим, а не по сонетовскому модбасу подключены модули
                    hwFileSuffix = ".mb_cfg";
                    slot = ft.getCell("dataType", j);
                    if(slot!=null){
                        XMLSAX sax = new XMLSAX();
                        Node root = sax.readDocument("ModbusFormat.xml");
                        Node formNode = sax.returnFirstFinedNode(root, slot);
                        slot = sax.getDataAttr(formNode,"name");
                    }else slot = "BIT";
                    chanell = (ft.getCell("mbAddr", j))+":"+slot;
               }else{
                    if("Sonet".equals(abType)){                         //Если текущий абонент использует Сонет
                        hwDew = ft.getCell("sonetModbus", j);  //Смотрим, а не по сонетовскому модбасу подключены модули
                        if("".equals(hwDew)){ //Если не по нему,
                            hwDew = exemplar + "_R" + device + "_LOCALBUS";  //значит каждое устройство - иной файл HW
                            device = "";
                            hwFileSuffix = ".snt_lb";
                        } else {
                            hwFileSuffix = ".snt_mb";
                        }
                    }
                    slot = ft.getCell("Слот", j);
                    chanell = ft.getCell("Канал", j);
                }

                //--------------------------------------------------------------------------------------------------------------
                if(!currDev.equals(hwDew)){
                    currDev = hwDew;
                    hwConn = setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prj, prjFildName, globSigInPrj, globUUID);
                    if(hwConn == null) return -1;
                    if(isModbus){//удаление коннектов с таким же адресом абонента и той же группы
                        ArrayList<Node> conns = hw.getHeirNode(hwConn);
                        for(Node n: conns){
                            if(modbusAddr.equals(hw.getDataAttr(n, "Device")) && group.equals(hw.getDataAttr(n, "Group"))) 
                                hw.removeNode(n);
                        }
                    }
                }
                //вставляем новые коннекты
                String[] signalData = {"Field","Name",tagName};
                Node sigNode = drv.findNodeAtribute(drvSignalsNode, signalData);
                if(sigNode == null) FileManager.loggerConstructor("В структуре \""+prjFildName+"\" не найден сигнал \""+tagName+"\"");
                else{
                    if(isModbus){
                        String[] connData = {"Connection", "ItemName", prjFildName + "." + tagName,
                                                            "Device", modbusAddr,
                                                            "Channel", chanell,
                                                            "Group", group,
                                                            "UUID", globUUID + "." + drv.getDataAttr(sigNode,"UUID"),
                                            }; 
                        hw.insertChildNode(hwConn, connData);
                    }else{
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
            }
            hw.writeDocument();
        }
        return 0;
    }
    
    public static int genSTcode(TableDB ft, boolean disableReserve, JProgressBar jProgressBar1) throws IOException{ //0 -ok, 1 - not source file, 2 -impossible create file
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать Функции обработки и инициализации?"); // сообщение с выбором
        if(casedial != 0) return -2; //0 - yes, 1 - no, 2 - cancel
        FileManager fm = new FileManager();                                 //создали менеджер файлов
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0,x);
        nodeTable = nodeTable.substring(x+1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        //String group = "";
        //boolean isModbus = false;
        if(y>0){
            subAb = "_"+nodeTable.substring(0,y);
            nodeTable = nodeTable.substring(y+1);
            isMb = "_mb";
            //isModbus = true;
            //group = nodeTable.substring(3);
        }
        
        Node nodeGenCode = globVar.sax.returnFirstFinedNode(globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable), "GenCode");
        if(nodeGenCode == null )return 0;
        ArrayList<Node> fileList = globVar.sax.getHeirNode(nodeGenCode);
        for(Node f: fileList){
            String commonFileST = (String) globVar.sax.getDataNode(f).get("src");
            String stFileName = abonent + subAb + "_" + commonFileST; //Для каждого файла
            int ret = genInFile(fm, abonent + subAb + isMb, commonFileST, f, ft, disableReserve,stFileName, abonent, jProgressBar1);
            if(ret!=0) return -1;
        }
        return 0;
    }
    
    public static String genHMI(TableDB ft, JProgressBar jProgressBar) throws IOException {
        int casedial = JOptionPane.showConfirmDialog(null, "Листы мнемосхем для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        if(casedial != 0) return ""; //0 - yes, 1 - no, 2 - cancel
        String targetFile;// = null;
        String nameTable = ft.tableName();//нашли ai ao do и тд
        int x = nameTable.indexOf("_");
        String abonent = nameTable.substring(0,x);
        String nodeTable = nameTable.substring(x+1);
        //String subGroup = ""; //Для обозначения подгруппы сигнала, например "FR"
        //--- для определения модбасовских подабонентов ----------------------------------
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        if(y>0){
            subAb = nodeTable.substring(0,y+1);
            nodeTable = nodeTable.substring(y+1);
            isMb = "mb_";
        }//------------------- Читаем пользовательскую конфигурацию для ЧМИ ----------------------------------------
        XMLSAX HMIcfg = new XMLSAX(); //
        Node hmiCfgRoot = HMIcfg.readDocument("ConfigHMI.xml"); //AT_HMI.iec_hmi
        Node findNode = HMIcfg.returnFirstFinedNode(hmiCfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if (findNode == null) {
            FileManager.loggerConstructor("Тип данных "+nodeTable+" не описан в файле ConfigHMI.xml");
            return null;
        }//Если не вылетели - значит будет генерация
        //------------------------------------ Читаем ЧМИ файл Сонаты -----------------------------------------------
        XMLSAX bigSax = new XMLSAX();
        String hmiProjectFile = globVar.desDir + File.separator + "Design" + File.separator + 
                globVar.DB.getDataCell("Abonents", "Abonent", abonent, "HMI") + ".iec_hmi"; // Нужен для поиска УУИДов листов
        Node bigRoot = bigSax.readDocument(hmiProjectFile);
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта "+hmiProjectFile);
            return null;
        }//Если не вылетели - значит будет генерация
        //--------------------------------------------------------------------------------------------------------------
        String filePath = globVar.desDir + File.separator + "GenHMI"; //Установили путь для файлов
        File d = new File(filePath);
        if(!d.isDirectory()){
            d.mkdir();
            if(!d.isDirectory()){
                FileManager.loggerConstructor("Не удалось создать директорию GenHMI в рабочем каталоге проекта");
                return null;
            }
        }
        
        ArrayList<Node> hmiNodeList = HMIcfg.getHeirNode(findNode);//Находим все ноды
        String ret = "";
        for(Node hmiNode: hmiNodeList){
            String typeGCT = HMIcfg.getDataAttr(hmiNode, "type");
            boolean isIF = typeGCT==null;
            String uuidGCT = null;//HMIcfg.getDataAttr(hmiNode, "typeUUID");
            String[] findInBig = {"GraphicsCompositeFBType","Name",typeGCT};
            Node myBlock = bigSax.findNodeAtribute(bigRoot, findInBig);
            ArrayList<String[]> addVarsData = new ArrayList<>();
            if(myBlock!=null){
                uuidGCT = bigSax.getDataAttr(myBlock, "UUID");
                getAddVars(HMIcfg, hmiNode, addVarsData);
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
            ArrayList<String> hintAL = new ArrayList<>();

            getHintParts(HMIcfg, hmiNode,hintAL);//new ArrayList<>();

            targetFile = HMIcfg.getDataAttr(hmiNode, "file");//получили имя файла в который записываю верхний уровень,который в последствии читаем AI_HMI_inc
            String folderNodeName=null;
            String nameGCTcommon = HMIcfg.getDataAttr(hmiNode, "name");
            String nameGCT = abonent + "_" + subAb + nameGCTcommon;
            String pageName = nameGCT+"1";
            Node gctNode;
            String sourceFile;
            XMLSAX HMIsax = new XMLSAX();
            findInBig[2] = pageName;
            if(targetFile==null){
                sourceFile = "root"+File.separator+"HMI_Sheet.txt";
                targetFile = filePath + File.separator + pageName +".txt";//если имени в ноде нет - конструируем имя
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
            int jpgMax = ft.tableSize();
            for (int i = 0; i < jpgMax; i++) {
                if(jProgressBar!=null) jProgressBar.setValue((int)((i+1)*100.0/jpgMax));
                String ruName = ft.getCell(nameCol, i);
                if("".equals(ruName)) continue;
//                if(!"".equals(ruName)){
                    if(isIF){//ищем условния выбора типа блока
                        String[] gctData=new String[3];
                        hintAL.clear();
                        addVarsData.clear();
                        if(!setTypeHintAdd(HMIcfg, bigSax, ft, hmiNode, addVarsData, gctData, i, hintAL)) continue;
                        typeGCT = gctData[0];
                        uuidGCT = gctData[1];
                        isAlarm = gctData[2]!=null;
                    }
                    //-- конструируем ФБ
                    String fbUUID = UUID.getUIID().toUpperCase();
                    String[] fbData = {"FB","Name",     abonent + subAb + typeGCT + "_"+ i, 
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

                    String tagName = ft.getCell("TAG_NAME_PLC", i);
                    fbChildNode[2] = "NameAlg";
                    fbChildNode[4] = "'" + tagName + "'";
                    HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    fbChildNode[2] = "PrefStr";
                    fbChildNode[4] = "'" + subAb + isMb + "'";
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
                        String s = ft.getCell(hintPart, i);
                        if(s==null) s = hintPart;
                        if("true".equalsIgnoreCase(s)) s = "есть";
                        else if ("false".equalsIgnoreCase(s)) s = "нет";
                        hint += s;
                    }
                    if(!hint.isEmpty()){
                        fbChildNode[2] = "hint";
                        fbChildNode[4] = "'" + hint + "'";
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    }
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

                    if(!addVarsData.isEmpty()) for(String[] s: addVarsData){
                        fbChildNode[2] = s[0];
                        String apos = "";
                        if("STRING".equals(s[2])) apos = "'";
                        fbChildNode[4] = apos + ft.getCell( s[1], i) + apos;
                        fbChildNode[6] =  s[2];
                        fbChildNode[8] =  s[3];
                        HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                    }

                    String[] connects = {"Connection",  "Source", "PrefAb", 
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
//                }
            }
            FBNetwork.appendChild(DataConnections);
            HMIsax.writeDocument(targetFile);
            ret += nameGCT + " - " +pageCnt + " страниц. ";
        }
        return ret;
    }

    public static int genTypeFile(TableDB ft, JProgressBar jProgressBar) throws IOException {//0-norm, -1 - not find node
        int casedial = JOptionPane.showConfirmDialog(null, "Файлы .TYPE для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        if(casedial != 0) return 0; //0 - yes, 1 - no, 2 - cancel
        //-------------------------------------------------------------------------------------
        String backUpPath = globVar.backupDir + File.separator;   //установили путь для бэкапа
        String filePath = globVar.desDir + File.separator+"Design"; //установили путь для проекта
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
        Node prjRoot = prjSax.readDocument(filePath + File.separator+"Project.prj");;
        Node interfaceList = prjSax.returnFirstFinedNode(prjRoot, "Globals");
        NodeList nodesGenData = nodeGenData.getChildNodes(); //получаем список нод внутри ГенДаты
        //---------------------- Определение списка экземпляров ------------------------------
        String exemplars = globVar.DB.getDataCell("Abonents","Abonent",abonent,"Экземпляры"); 
        ArrayList<String> exArr = new ArrayList<>();
        if(!exemplars.isEmpty()) exArr = StrTools.getListFromString(exemplars, ",");
        else exArr.add(abonent);
        //------------------ Определение параметров драйвера модбаса -----------------------
        int jpgMax = nodesGenData.getLength();
        for (int i = 0; i < jpgMax; i++) {//получил размерность ноды и начал цикл
            if(jProgressBar!=null) jProgressBar.setValue((int)((i+1)*100.0/jpgMax));//для прогрессбара
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node currNodeCfgXML = nodesGenData.item(i);
                ArrayList<Node> globSigList = globVar.sax.getHeirNode(currNodeCfgXML);//Находим все ноды внутри ноды типа файла
                String nodeName = currNodeCfgXML.getNodeName();//определяем имя ноды
                //Создаём список типов данных, для которых не надо создавать полей в структуре
                ArrayList<String> notGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(currNodeCfgXML, "notGenTyps"),",");
                String fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "Type"); //Ищем, нет ли там определённого типа для переменных. Если нет - будем искать другими способами
                String typeFile = globVar.sax.getDataAttr(currNodeCfgXML, "typeFile"); //Ищем имя файла для случаев, когда имя ноды одно, а имя файла совсем другое
                if(typeFile==null) typeFile = "T_"+isMb+nodeName+".type";   //если имени файла нет - синтезируем это имя из имени ноды
                XMLSAX fildUuidSax=null;
                Node fildUuidRoot = null;
                String dtCol=null;
                String defType=null;
                if(fildUUID == null){//Если тип данных для переменных не был укан явно, его надо определить
                    fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "dictionary"); //Проверяем, не указан ли в качестве типа данных словарь
                    if(fildUUID!=null){//если указан - 
                        fildUuidSax = new XMLSAX();
                        fildUuidRoot = fildUuidSax.readDocument(fildUUID); // пытаемся прочитать файл словаря
                        if(fildUuidRoot!=null) fildUUID = "dictionary"; // если это удалось - выставяем флаг необходимости узнавать тип каждой переменной из словаря
                        dtCol = globVar.sax.getDataAttr(currNodeCfgXML, "col");
                        defType = globVar.sax.getDataAttr(currNodeCfgXML, "ifEmpty");
                    }else{
                        fildUUID = FileManager.findStringInFile(filePath+File.separator+typeFile, "UUID=");
                        fildUUID = StrTools.getAttributValue(fildUUID,"UUID=\"");
                    }
                    if(fildUUID==null){
                        FileManager.loggerConstructor("Не найден файл типа данных "+"T_"+isMb+nodeName +". Генерация прервана.");
                         return -1;
                    }
                }
                //------ Находим УУИД типа в первом приложении, в которое надо распространить этот тип данных -------------
                XMLSAX uuidSax = new XMLSAX();
                Node intFile1 = globVar.sax.returnFirstFinedNode(currNodeCfgXML,"localData");
                String intFile2 = globVar.sax.getDataAttr(intFile1, "int");
                if("_".equals(intFile2.substring(0, 1))) intFile2 = abonent+intFile2;
                Node uuidRoot = uuidSax.readDocument(filePath + File.separator + intFile2);
                String typeUUID = getUUIDfromPrj(uuidSax, uuidRoot, abonent+"_"+subAb+isMb+nodeName, "Type");
                String globUUID = getUUIDfromPrj(uuidSax, uuidRoot, abonent+"_"+subAb+isMb+nodeName, "UUID");
                uuidSax.clear();
                //---- Если сигнал есть в первом приложении - его уид и тип рапространятся на Project.prj и другие приложения
                //----------------------------------------------------------------------------------------------------------
                XMLSAX localSax = new XMLSAX();
                Node type;
                Node newFields;// = null;
                Node oldFields = null;
                String typeName = "T_"+abonent+"_"+subAb+isMb+nodeName;//достаю элементы из ноды(в данный момент T GPA AI DRV)
                String trueName = FileManager.FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
                if (trueName == null) {//помещаем сюда создание файла
                    trueName = typeName + ".type";
                    type = localSax.createDocument("Type");
                    if(typeUUID==null) typeUUID = UUID.getUIID();
                    localSax.setDataAttr(type, "UUID", typeUUID);
                    localSax.setDataAttr(type, "Name", typeName);
                    localSax.setDataAttr(type, "Kind", "Struct");
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                } else {//сюда помещаем добавление
                    FileManager.copyFileWoReplace(filePath + File.separator + trueName, backUpPath + trueName, true);
                    type = localSax.readDocument(filePath + File.separator + trueName);//прочитал файл в котором нашли совпадения по имени
                    if(typeUUID==null) typeUUID = type.getAttributes().getNamedItem("UUID").getNodeValue();
                    else localSax.setDataAttr(type, "UUID", typeUUID);
                    oldFields = localSax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                }
                for (int j = 0; j < ft.tableSize(); j++) {
                    String dt = ft.getCell("dataType", j); //Определяем тип данных
                    if(dt!=null && notGenTyps!=null && notGenTyps.contains(dt)) continue;//если тип данных есть и есть список ненужных данных и данный тип в этом списке
                    String tagName = ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                    String comment = ft.getCell("Наименование", j);//получаем НАИМЕНОВАНИЕ из таблицы
                    if(fildUuidRoot!=null){
                        String dataType = ft.getCell(dtCol, j);
                        fildUUID = getFromDict(fildUuidSax,fildUuidRoot,dataType,"dataType");
                        if(fildUUID==null) fildUUID = defType;
                    }
                    if (oldFields == null) {//если нода пустая,то создаю элементы
                        localSax.insertChildNode(newFields, new String[]{"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", UUID.getUIID()});
                    } else {
                        Node oldTag = localSax.findNodeAtribute(oldFields, new String[]{"Field", "Name", tagName});
                        if (oldTag == null) {
                            localSax.insertChildNode(newFields, new String[]{"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", UUID.getUIID()});
                        } else {
                            String localUUID = oldTag.getAttributes().getNamedItem("UUID").getNodeValue();
                            localSax.insertChildNode(newFields, new String[]{"Field", "Name", tagName, "Comment", comment, "Type", fildUUID, "UUID", localUUID});
                        }
                    }
                }
                if(oldFields != null) localSax.removeNode(oldFields);
                localSax.writeDocument(filePath + File.separator + trueName);//записали файл
                for(Node globSig: globSigList){//--------------------------------------- Занесение сигналов в .prj и .int файлы -------------------------------------
                    String name = globVar.sax.getDataAttr(globSig, "name");
                    if(name!=null){
                        String hmiApp = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "HMI");
                        Node localRoot = null;
                        Node localInterfaceList = null;
                        String file = null;
                        XMLSAX localSigSax = new XMLSAX();
                        if(!hmiApp.isEmpty()){
                            file = hmiApp+".int";
                            localRoot = localSigSax.readDocument(filePath+File.separator+file);
                            if(localRoot==null){
                                FileManager.loggerConstructor("Не найден файл "+file+" в каталоге "+filePath);
                                return -1;
                            }
                            localInterfaceList = localSigSax.returnFirstFinedNode(localRoot, "InterfaceList");
                            if(localInterfaceList==null){
                                FileManager.loggerConstructor("Не найден раздел <InterfaceList> в файле "+filePath+File.separator+file);
                                return -1;
                            }
                        }
                        for(String s: exArr){
                            globUUID = insertVarInPrj(prjSax, interfaceList, s+"_"+subAb+isMb+name, typeUUID, "", true, false, null,
                                           filePath + File.separator+"Project.prj", backUpPath + "Project.prj");
                            if(!hmiApp.isEmpty())
                                globUUID = insertVarInPrj(localSigSax, localInterfaceList, s+"_"+subAb+isMb+name, typeUUID, 
                                        "", true, true, globUUID, filePath+File.separator+file, backUpPath+file);
                            
                        }
                    }
                    ArrayList<Node> localSigList = globVar.sax.getHeirNode(globSig);//Находим все ноды
                    boolean glob = name!=null && exArr.size()==1;
                    for(Node localSig: localSigList){
                        String tmp = globVar.sax.getDataAttr(localSig, "name");
                        if(tmp!=null) name = tmp;
                        else if(name==null){
                            FileManager.loggerConstructor("Не найдено имя переменной в разделе "+
                                    "/GenData/"+nodeName+globSig.getNodeName()+"/"+localSig.getNodeName());
                            return -1;
                        }
                        XMLSAX localSigSax = new XMLSAX();
                        String localDocName = globVar.sax.getDataAttr(localSig, "int");
                        if(localDocName==null){
                            FileManager.loggerConstructor("Не найдено имя файла .int в разделе "+
                                    "/GenData/"+nodeName+globSig.getNodeName()+"/"+localSig.getNodeName());
                            return -1;
                        }
                        if("_".equals(localDocName.substring(0,1))) localDocName = abonent+localDocName;
                        
                        Node localRoot = localSigSax.readDocument(filePath+File.separator+localDocName);
                        if(localRoot==null){
                            FileManager.loggerConstructor("Не найден файл "+localDocName+" в каталоге "+filePath);
                            return -1;
                        }
                        Node localInterfaceList = localSigSax.returnFirstFinedNode(localRoot, "InterfaceList");
                        if(localInterfaceList==null){
                            FileManager.loggerConstructor("Не найден раздел <InterfaceList> в файле "+filePath+File.separator+localDocName);
                            return -1;
                        }
                        insertVarInPrj(localSigSax, localInterfaceList, abonent+"_"+subAb+isMb+name, typeUUID, "", glob, true,
                                       globUUID, filePath+File.separator+localDocName, backUpPath + localDocName);
                    }
                }
            }
        }
        return 0;
    }
    //---------- Функция для поиска файла с описаниями подключённых к приложению сигналов
    public static Node setHWdoc(XMLSAX hw, String hwDew, String hwFileSuffix,String[] globSigAttr, XMLSAX prj,
                         String prjFildName, Node globSigInPrj, String globUUID) throws IOException{
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        String designDir = globVar.desDir  + File.separator + "Design"+File.separator;        
        hw.writeDocument();
        hw.clear();                                             //удаляем старые коннекты
        String hwFileName = designDir + hwDew +hwFileSuffix;
        String intFileName = designDir + hwDew + ".int";
        int ret = FileManager.copyFileWoReplace(hwFileName, backUpPath+File.separator+hwFileName, true);                    //создаём резервную копию
        if(ret==2){ //Функция копирования не нашла исходного файла
          FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
          return null;
        }
        XMLSAX intFile = new XMLSAX();
        Node intf = intFile.readDocument(intFileName);
        Node lokSig = intFile.findNodeAtribute(intf, globSigAttr);
        if(lokSig==null){
            ret = FileManager.copyFileWoReplace(hwFileName, backUpPath+File.separator+intFileName, true);                    //создаём резервную копию
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

    static int closeByErr(FileManager fm, String tmpFile, String err) throws IOException{
            FileManager.loggerConstructor(err);
            fm.closeRdStream();                                       //закрываем поток чтения
            fm.closeWrStream();                                       //закрываем поток записи
            new File(tmpFile).delete();                          //Удаляем временный файл
            return -1;
    }
    //================================================================================================================
    static int genInFile(FileManager fm, String abSubAb, String commonFileST, Node nodeGenCode, TableDB ft, boolean disableReserve,
                  String stFileName, String abonent, JProgressBar jProgressBar) throws IOException{
        String filePath = globVar.desDir + File.separator + "GenST";
        File d = new File(filePath);
        if(!d.isDirectory()){
            d.mkdir();
            if(!d.isDirectory()){
                FileManager.loggerConstructor("Не удалось создать директорию GenST в рабочем каталоге проекта");
                return -1;
            }
        }
        String srcFile = filePath + File.separator + stFileName + ".txt";
        String tmpFile = filePath + File.separator + stFileName + ".txt_tmp";
        int ret = fm.createFile2write(tmpFile); //открываем файл на запись
        if(ret!=0){
            FileManager.loggerConstructor("Не удалось создать файл \"" + tmpFile + "\"");
            return -2;
        }
        ret = fm.openFile4read(globVar.myDir, commonFileST + ".txt");
        if(ret!=0) return closeByErr(fm, tmpFile, "Не удалось прочитать служебный файл \""+globVar.myDir+File.separator+commonFileST+".txt");
        
        String algFile = (String) globVar.sax.getDataNode(nodeGenCode).get("target");
        String funcName = stFileName;//abonent + "_"+ commonFileST;
        String funcUUID=null;
        if(algFile!=null){
            if("_".equals(algFile.substring(0, 1))) algFile = abonent + algFile;
            XMLSAX algSax = new XMLSAX();
            Node algRoot = algSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + algFile + ".iec_st");
            String[] myFunc = {"Function", "Name", funcName};
            Node func = algSax.findNodeAtribute(algRoot, myFunc);
            if(func!=null) funcUUID = (String) algSax.getDataNode(func).get("UUID");
            if(funcUUID==null) funcUUID = UUID.getUIID();
        }else funcUUID = UUID.getUIID();
        
        fm.wr("<Data>\n<Function UUID=\""+funcUUID+"\" Name=\""+funcName+"\" ResultTypeUUID=\"EC797BDD4541F500AD80A78F1F991834\">\n");
        ArrayList<Node> blockList = globVar.sax.getHeirNode(nodeGenCode);
        for(Node block : blockList){
            String start = (String) globVar.sax.getDataNode(block).get("start");
            String end = (String) globVar.sax.getDataNode(block).get("end");
            //Создаём список типов данных, для которых не надо создавать полей в структуре
            ArrayList<String> notGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(block, "notGenTyps"),",");
            ArrayList<String> isGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(block, "isGenTyps"),",");
            if(start==null || end==null) 
                return closeByErr(fm, tmpFile, "В файле \""+globVar.myDir+File.separator+"ConfigSignals.xml в разделе "+ nodeGenCode.getParentNode()+
                                  " неправильно сконфигурированы признаки начала и конца вставки кода");
            String s = fm.rd();                                                     //Для копирования всего, что было до этой функции, 
            while(!fm.EOF && !s.contains(start)){
                fm.wr(s + "\n");                          //ищем в исходнои файле её первое вхождение
                s = fm.rd();
            }
            if(fm.EOF) return closeByErr(fm, tmpFile, "В файле \""+globVar.myDir+File.separator+commonFileST+".txt"+" не найдена строка \""+start+"\"");
            fm.wr("//"+start+"\n");
            ArrayList<Node> blockCont = globVar.sax.getHeirNode(block);
            int tsz = ft.tableSize();
            for (int j = 0; j < tsz; j++) {                      //Цикл по всем строкам таблицы
                if(jProgressBar!=null && tsz!=0) jProgressBar.setValue((int)((j+1)*100.0/tsz));//Прогресс генерации
                String dt = ft.getCell("dataType", j); //Определяем тип данных
                if(dt!=null && notGenTyps!=null && notGenTyps.contains(dt)) continue;//если тип данных есть и есть список ненужных данных и данный тип в этом списке
                if(dt!=null && isGenTyps!=null && !isGenTyps.contains(dt)) continue;//если тип данных есть и есть список ненужных данных и данный тип в этом списке
                for(Node cont: blockCont){
                    String nodeName = cont.getNodeName();
                    if("Function".equals(nodeName)) createFunction(cont, fm, ft, abSubAb, disableReserve, j);
                    else                              createString(cont, fm, ft, abSubAb, disableReserve, j);
                }
            }
            //пролистываем в исходном файле строки со старыми вызовами и пустые строки 
            while(!fm.EOF  && !s.contains(end)) 
                s = fm.rd(); 
            while(!fm.EOF){                                                 //дописываем хвост файла
                fm.wr(s + "\n");                          
                s = fm.rd();
            }
            fm.closeRdStream();                                       //закрываем поток чтения
            fm.closeWrStream();                                       //закрываем поток записи
            File file = new File(srcFile);                             //создаём ссылку на исходный файл
            file.delete();                                             //удаляем его
            new File(tmpFile).renameTo(file);                          //создаём ссылку на сгенерированный файл и делаем его исходным
            fm.openFile4read(filePath, stFileName + ".txt");              //открываем его на чтенье
            fm.createFile2write(filePath, stFileName + ".txt_tmp");  //открываем временный файл для генерации
        }
        fm.closeRdStream();                                       //закрываем поток чтения
        fm.closeWrStream();                                       //закрываем поток записи
        new File(tmpFile).delete();                          //Удаляем временный файл
        return 0;
    }
    
    static int createString(Node args, FileManager fm, TableDB ft, String abonent, boolean disableReserve, int j) throws IOException{
        ArrayList<Node> arglist = globVar.sax.getHeirNode(args);                  //создаём список аргументов
        String tmp = "";
        for(Node arg : arglist){                                        //Цикл по всем аргументам функции
            tmp+=getPartText(arg, abonent, ft, j);
        }   //Цикл по всем частям аргументов - текстовым и табличным
        String disable = "";
        if(disableReserve && ((String)ft.getCell("TAG_NAME_PLC", j)).contains("Res_")) disable = "//";
        fm.wr(disable + tmp + ";\n");
    return 0;
    }
    
    static int createFunction(Node funcNode, FileManager fm, TableDB ft, String abonent, boolean disableReserve, int j) throws IOException{
        String stFunc = (String) globVar.sax.getDataNode(funcNode).get("name");  //вычитываем её имя
        ArrayList<Node> arglist = globVar.sax.getHeirNode(funcNode);                  //создаём список аргументов
        String tmp = "";
        for(Node arg : arglist){                                        //Цикл по всем аргументам функции
            ArrayList<Node> argParts = globVar.sax.getHeirNode(arg);
            tmp += ",";                                                //аргумент записан и отделён от следующего запятой
            for(Node argPart : argParts) tmp += getPartText(argPart, abonent, ft, j);
        }   //Цикл по всем частям аргументов - текстовым и табличным
        String disable = "";
        if(disableReserve && ((String)ft.getCell("TAG_NAME_PLC", j)).contains("Res_")) disable = "//";
        fm.wr("//"+(String)ft.getCell("Наименование", j)+"\n"+disable+stFunc+"("+tmp.substring(1)+");\n");
        return 0;
    }    
    
    static String getFromDict(XMLSAX sax, Node root, String s, String attr){
        Node n = sax.returnFirstFinedNode(root, s);
        if(n==null) return null;
        return sax.getDataAttr(n, attr);
    }

    static String getFromDict(String s, String dict){
        XMLSAX sax = new XMLSAX();
        Node root = sax.readDocument(dict);
        Node n = sax.returnFirstFinedNode(root, s);
        if(n==null) return null;
        return sax.getDataAttr(n,"chng");
    }
    
    static String getPartText(Node argPart, String abonent, TableDB ft, int j){
        switch (argPart.getNodeName()) {
            case "text": return (String) globVar.sax.getDataAttr(argPart,"t");
            case "dbd":  
                String s = ft.getCell(globVar.sax.getDataAttr(argPart,"t"),j);
                if(s==null || s.isEmpty()) return globVar.sax.getDataAttr(argPart,"ifEmpty");
                else return s;
            case "npp":  return ""+j;
            case "abonent": return abonent;
            case "dict" : 
                s = getFromDict(ft.getCell(globVar.sax.getDataAttr(argPart,"t"),j),
                                             globVar.sax.getDataAttr(argPart,"dictionary"));
                if(s==null || s.isEmpty()) return globVar.sax.getDataAttr(argPart,"ifEmpty");
                else return s;
            case "switch" : 
                s = ft.getCell(globVar.sax.getDataAttr(argPart,"swt"),j);
                String[] arr = {"case", "val", s};
                Node n = globVar.sax.findNodeAtribute(argPart, arr);
                if(n!=null) return globVar.sax.getDataAttr(n,"def");
                else return globVar.sax.getDataAttr(argPart,"default");
        }
        return "";
    }
// <Signal Name="GPA_DI_Settings" UUID="6DC2E85F4B6835B2209D6D8076F22EFF" Type="9D3CCA014F76318C4B750981ED2CEA67" Usage="" Global="TRUE" Comment="настройки дискретного канала" />
    static String getUUIDfromPrj(XMLSAX intFile, Node interfaceList, String Name, String who){
        String[] findArr = {"Signal","Name",Name};
        Node sig = intFile.findNodeAtribute(interfaceList, findArr);
        return intFile.getDataAttr(sig,who);
    }
    //Функция занесения переменных в интерфейсные листы приложений Сонаты
    static String insertVarInPrj(XMLSAX intFile, Node interfaceList, String Name, String Type, String Comment, boolean global, boolean usage, 
            String uuid, String hwFileName1, String backUpFile1){
        String[] findArr = {"Signal","Name",Name};
        Node sig = intFile.findNodeAtribute(interfaceList, findArr);
        if(sig!=null){
            intFile.setDataAttr(sig, "Type", Type);
            if(uuid!=null){
                intFile.setDataAttr(sig, "UUID", uuid);
                intFile.writeDocument();
                return uuid;
            }else{
                intFile.writeDocument();
                return intFile.getDataAttr(sig,"UUID");
            }
        }
//        int  ret = FileManager.copyFileWoReplace(hwFileName, backUpFile, true);                    //создаём резервную копию
//        if(ret==2){ //Функция копирования не нашла исходного файла
//          FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
//          return null;
//        }
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

    public static int genOPC(String serverName, String id, String idType,  ArrayList<String[]> opcList, JProgressBar jProgressBar){
        if(opcList==null || opcList.isEmpty()) return 0;
        String appPathName = globVar.desDir + File.separator+"Design"+File.separator + serverName;
        XMLSAX opcSax = new XMLSAX();
        if(opcSax.readDocument(appPathName +".opcuaServer")==null){ //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName +".opcuaServer\"");
            return -1;
        }
        Node opcConnection = opcSax.returnFirstFinedNode("Crossconnect");
        opcSax.cleanNode(opcConnection);
        
        XMLSAX intSax = new XMLSAX();
        Node intRoot = intSax.readDocument(appPathName +".int");
        if(intRoot==null){ //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName +".int\"");
            return -1;
        }
        Node interfaceList = intSax.returnFirstFinedNode("InterfaceList");
        
        XMLSAX bigSax = new XMLSAX();
        Node bigRoot = bigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта "+globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
            return -1;
        }
        FileManager fm = new FileManager();
        try {
            fm.createFile2write(globVar.desDir + File.separator + serverName + ".csv");
        } catch (IOException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
        int jpgMax = opcList.size();
        int jpbCnt = 1;
        int ret = 0;
        int cnt = 1;
        for(String[] sig: opcList){
            if(jProgressBar!=null) jProgressBar.setValue((int)((jpbCnt++)*100.0/jpgMax));
            //int speceName = Integer.parseInt(sig[1]);
            int x = sig[0].indexOf(".");
            if(x < 0){
                Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal","Name",sig[0]});
                if(n==null){
                    ret = -1;
                    FileManager.loggerConstructor("В Project.prj не найден сигнал \""+ sig[0] + "\"");
                }
                String type = bigSax.getDataAttr(n, "Type");
                String uuid = bigSax.getDataAttr(n, "UUID");
                String comment = bigSax.getDataAttr(n, "Comment");
                if(comment==null) comment="";
                else comment = comment.trim();
                if( isStdType(type)) insertInOPC(sig[0], sig[1], id, idType, uuid,  comment, opcSax, fm, type, cnt++);
                else{
                    String sigFileName = FileManager.FindFile(globVar.desDir + File.separator+"Design", type, "UUID=");
                    if(sigFileName==null){
                        FileManager.loggerConstructor("Не найден файл типа "+ type + " в каталоге "+ globVar.desDir + File.separator+"Design");
                        ret = -1;
                    }
                    else {
                        XMLSAX sigSax = new XMLSAX();
                        sigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                        Node sigs = sigSax.returnFirstFinedNode("Fields");
                        ArrayList<Node> sigNodeList = sigSax.getHeirNode(sigs);//Находим все ноды
                        for(Node sigNode: sigNodeList){
                            String nameSig = bigSax.getDataAttr(sigNode, "Name");
                            String typeSig = bigSax.getDataAttr(sigNode, "Type");
                            String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                            String commSig = bigSax.getDataAttr(n, "Comment");
                            if(commSig==null) commSig="";
                            else commSig = commSig.trim();
                            if(!nameSig.contains("Res_")){
                                nameSig = sig[0]+"."+nameSig;
                                uuidSig = uuid+"."+uuidSig;
                                if(!comment.isEmpty()) commSig = comment + "." + commSig;
                                if(!isStdType(typeSig)){
                                    nameSig += ".PV";
                                    uuidSig += ".19F27C8242D7A36082010591B7CF4F94";
                                    typeSig = "REAL";
                                }
                                insertInOPC(nameSig, sig[1], id, idType, uuidSig,  commSig, opcSax, fm, typeSig, cnt++);
                            }                            
                        }
                        if(ret==0) insertVarInPrj(intSax, interfaceList, sig[0], type, "", true, true, uuid, appPathName +".int", "");
                    }
                }
            }else{
                String groupName = sig[0].substring(2, x);
                String localName = sig[0].substring(x+1);
                Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal","Name",groupName});
                String type = bigSax.getDataAttr(n, "Type");
                String uuid = bigSax.getDataAttr(n, "UUID");
                String comment = bigSax.getDataAttr(n, "Comment");
                if(comment==null) comment="";
                else comment = comment.trim();
                String sigFileName = FileManager.FindFile(globVar.desDir + File.separator+"Design", type, "UUID=");
                if(sigFileName==null){
                    FileManager.loggerConstructor("Не найден файл типа "+ type + " в каталоге "+ globVar.desDir + File.separator+"Design");
                    ret = -1;
                }
                else {
                    XMLSAX sigSax = new XMLSAX();
                    sigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                    Node sigNode = sigSax.findNodeAtribute(new String[]{"Field","Name",localName});
                    if(sigNode==null){
                        FileManager.loggerConstructor("Не найден сигнал "+localName+" в файле "+ globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                        ret = -1;
                    }else{
                        String typeSig = bigSax.getDataAttr(sigNode, "Type");
                        String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                        String commSig = bigSax.getDataAttr(n, "Comment");
                        if(commSig==null) commSig="";
                        else commSig = commSig.trim();
                        if(!comment.isEmpty()) commSig = comment + "." + commSig;
                        String tmpName = groupName+"."+localName;
                        uuidSig = uuid+"."+uuidSig;
                        if(!isStdType(typeSig)){
                            tmpName += ".PV";
                            uuidSig += ".19F27C8242D7A36082010591B7CF4F94";
                            typeSig = "REAL";
                        }
                        insertInOPC(tmpName, sig[1], id, idType, uuidSig,  commSig, opcSax, fm, typeSig, cnt++);
                        insertVarInPrj(intSax, interfaceList, groupName, type, "", true, true, uuid, appPathName +".int", "");
                    }
                }
            }
        }
        intSax.writeDocument();
        opcSax.writeDocument();
        fm.closeWrStream();
        return ret;
    }
    public static int genArchive(int[][] archTyps, ArrayList<String[]> archList, String abonent,
            JProgressBar jProgressBar, String hmiApp) throws IOException {
        //------- Создаём каталог для файлов для ЧМИ
        String hmiPath = globVar.desDir + File.separator + "GenHMI"; //Установили путь для файлов
        File d = new File(hmiPath);
        if(!d.isDirectory()){
            d.mkdir();
            if(!d.isDirectory()){
                FileManager.loggerConstructor("Не удалось создать директорию GenHMI в рабочем каталоге проекта");
                return -1;
            }
        }
        String appPathName = globVar.desDir + File.separator+"Design"+File.separator + abonent + "_" + "Archive";
        XMLSAX archSax = new XMLSAX();
        Node archRoot = archSax.readDocument(appPathName +".arc_cfg");
        if(archRoot==null){ //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName +".arc_cfg" + "\"");
            return -1;
        }
        
        XMLSAX intSax = new XMLSAX();
        Node intRoot = intSax.readDocument(appPathName +".int");
        if(intRoot==null){ //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName +".int" + "\"");
            return -1;
        }
        Node interfaceList = intSax.returnFirstFinedNode("InterfaceList");
        
        XMLSAX bigSax = new XMLSAX();
        Node bigRoot = bigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта "+globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
            return -1;
        }
        XMLSAX cfgSax = new XMLSAX();
        Node cfgRoot = cfgSax.readDocument("ArchiveConfig.xml");
        if (cfgRoot == null) {
            FileManager.loggerConstructor("Не найден файл "+globVar.myDir + File.separator+"ArchiveConfig.xml");
            return -1;
        }
        HashMap trendAttr = cfgSax.getDataNode(cfgSax.returnFirstFinedNode("Trend"));
        ArrayList<Node> colorList = cfgSax.getHeirNode(cfgSax.returnFirstFinedNode("TableColor"));
        
        XMLSAX hmiSax = new XMLSAX();
        Node hmiRoot = hmiSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator+hmiApp+".iec_hmi");
        if (hmiRoot == null) {
            FileManager.loggerConstructor("Не найден файл "+globVar.desDir + File.separator+"Design"+File.separator+hmiApp+".iec_hmi");
            return -1;
        }//Если не вылетели - значит будет генерация
        String trendSheetName = abonent + "_Trend";
        //trendNode = hmiSax.findNodeAtribute(new String[]{"WindowFBType","Name",trendSheetName});
        String trendSheetUUID = hmiSax.getDataAttr(hmiSax.findNodeAtribute(new String[]{"WindowFBType","Name",trendSheetName}), "UUID");

        hmiSax.clear();
        
        hmiSax.readDocument("Mnemo_Trend.txt");
        Node trendNode = hmiSax.returnFirstFinedNode("GraphicsCompositeFBType");
        if (trendNode == null) {
            FileManager.loggerConstructor("Не найдена нода <GraphicsCompositeFBType> в файле "+
                    globVar.myDir+File.separator+"Mnemo_Trend.txt");
            return -1;
        }//Если не вылетели - значит будет генерация
        hmiSax.setDataAttr(trendNode, "Name", trendSheetName);
        if(trendSheetUUID!=null)hmiSax.setDataAttr(trendNode, "UUID", trendSheetUUID);
        else hmiSax.setDataAttr(trendNode, "UUID", UUID.getUIID());
        //trendNode = hmiSax.findNodeAtribute(new String[]{"FB","Name","TREND_WINDOW"});//FB Name="TREND_WINDOW"
        trendNode = hmiSax.returnFirstFinedNode(hmiSax.findNodeAtribute(new String[]{"FB","Name","TREND_WINDOW"}), "Data");//Аццкие сонатоиды! Корневая нода и нода с трендами называются одинаково

        String[] attr = new String[3];
        ArrayList<String> tableList = globVar.DB.getListTable();
        int ret = 0;
        int jpgMax = archList.size();
        int jpbCnt = 1;
        int colorInd = 0;
        int colorMax = colorList.size()-1;
        for(String[] sig: archList){
            if(jProgressBar!=null) jProgressBar.setValue((int)((jpbCnt++)*100.0/jpgMax));
            int archType = Integer.parseInt(sig[1]);
            String tableName = sig[0];
            int x = tableName.indexOf(".");
            //---------------------- Определение списка структур ------------------------------
            if(x>0) tableName = tableName.substring(2, x);
            ArrayList<String> structArr = new ArrayList<>();
            String sigStructs = globVar.DB.getCommentTable(tableName);
            int z = -1;
            if(!sigStructs.isEmpty() && sigStructs.length()>6) z = sigStructs.indexOf("Архив:");
            if(z < 0) structArr.add("");
            else {
                z+=6;
                int n = sigStructs.indexOf(";",z);
                if(n<0){
                    FileManager.loggerConstructor("genArchive[1] - В заголовке таблицы "+ tableName+" неправильно описаны структуры для архивирования");
                    ret = -1;
                    continue;
                }
                sigStructs = sigStructs.substring(z, n);
                if(!sigStructs.isEmpty()) structArr = StrTools.getListFromString(sigStructs, ",");
                else structArr.add("");
            }
            //---------------------------------------------------------------------------
            String sig0 = sig[0];
            int y = sig0.indexOf("_");
            sig0 = abonent+sig0.substring(y);
            for(String sa: structArr){
                if(x < 0){
                    sig0 += sa;
                    Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal","Name",sig0});
                    if(n==null){
                        FileManager.loggerConstructor("В проекте не найден глобальный сигнал "+ sig0);
                        ret = -1;
                        continue;
                    }
                    String type = bigSax.getDataAttr(n, "Type");
                    String uuid = bigSax.getDataAttr(n, "UUID");
                    if( isStdType(type)) insertInArcive(sig0, archTyps[archType],uuid,archSax);
                    else{
                        String sigFileName = FileManager.FindFile(globVar.desDir + File.separator+"Design", type, "UUID=");
                        if(sigFileName==null){
                            FileManager.loggerConstructor("Не найден файл типа "+ type + " в каталоге "+ globVar.desDir + File.separator+"Design");
                            ret = -1;
                        }
                        else {
                            XMLSAX sigSax = new XMLSAX();
                            sigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                            Node sigs = sigSax.returnFirstFinedNode("Fields");
                            ArrayList<Node> sigNodeList = sigSax.getHeirNode(sigs);//Находим все ноды
                            for(Node sigNode: sigNodeList){
                                String nameSig = bigSax.getDataAttr(sigNode, "Name");
                                String typeSig = bigSax.getDataAttr(sigNode, "Type");
                                String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                                if(!nameSig.contains("Res_")){
                                    String tmpName = sig0+"."+nameSig;
                                    String tmpUuid = uuid+"."+uuidSig;
                                    if(!isStdType(typeSig)){
                                        tmpName += ".PV";
                                        tmpUuid += ".19F27C8242D7A36082010591B7CF4F94";
                                    }// <Trend ItemName="AO_D.Set_APK" UUID="D81CC7224B1F7C96DAA237A634367986.4C16C6034A798CBFCD04F398721A6E10" Min="0" Max="10" Log="FALSE" Color="#000000" InvColor="#00000000" Title="Управление АПК (выход на драйвер 0-10 В)" AxisTitle="Управление АПК (выход на драйвер 0-10 В)" LineWidth="2" HideScale="TRUE" HideYAxis="TRUE" Hide="TRUE" CanChange="TRUE" />

                                    insertInArcive(tmpName, archTyps[archType],tmpUuid,archSax);
                                    if(!getTrendAttr(tableList, sig0, nameSig, attr)) ret = -1;
                                    Node newTrend = hmiSax.insertChildNode(trendNode, new String[]{"Trend", "Color", cfgSax.getDataAttr(colorList.get(colorInd),"Color"),
                                    "ItemName", tmpName,"UUID", tmpUuid,"Min", attr[0],"Max", attr[1], "Title", attr[2], "AxisTitle", attr[2]});
                                    colorInd++;
                                    if(colorInd > colorMax) colorInd = 0;
                                    for(Object key: trendAttr.keySet()) 
                                        hmiSax.setDataAttr(newTrend, (String)key, (String)trendAttr.get(key));
                                }                            
                            }
                            if(ret==0) insertVarInPrj(intSax, interfaceList, sig0, type, "", true, true, uuid, appPathName +".int", "");
                        }
                    }
                }else{
                    String groupName = tableName+sa;
                    String localName = sig0.substring(x-1);
                    Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal","Name",groupName});
                    if(n==null){
                        FileManager.loggerConstructor("В проекте не найден глобальный сигнал "+ groupName);
                        ret = -1;
                        continue;
                    }
                    String type = bigSax.getDataAttr(n, "Type");
                    String uuid = bigSax.getDataAttr(n, "UUID");
                    String sigFileName = FileManager.FindFile(globVar.desDir + File.separator+"Design", type, "UUID=");
                    if(sigFileName==null){
                        FileManager.loggerConstructor("Не найден файл типа "+ type + " в каталоге "+ globVar.desDir + File.separator+"Design");
                        ret = -1;
                    }
                    else {
                        XMLSAX sigSax = new XMLSAX();
                        sigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                        Node sigNode = sigSax.findNodeAtribute(new String[]{"Field","Name",localName});
                        if(sigNode==null){
                            FileManager.loggerConstructor("Не найден сигнал "+localName+" в файле "+ globVar.desDir + File.separator+"Design"+File.separator + sigFileName);
                            ret = -1;
                        }else{
                            String typeSig = bigSax.getDataAttr(sigNode, "Type");
                            String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                            String tmpName = groupName+"."+localName;
                            String tmpUuid = uuid+"."+uuidSig;
                            if(!isStdType(typeSig)){
                                tmpName += ".PV";
                                tmpUuid += ".19F27C8242D7A36082010591B7CF4F94";
                            }
                            insertInArcive(tmpName, archTyps[archType],tmpUuid,archSax);
                            if(!getTrendAttr(tableList, groupName, localName, attr)) ret = -1;
                            //Node tmpN = colorList.get(colorInd++);
                            Node newTrend = hmiSax.insertChildNode(trendNode, new String[]{"Trend", "Color", cfgSax.getDataAttr(colorList.get(colorInd),"Color"),
                            "ItemName", tmpName,"UUID", tmpUuid,"Min", attr[0],"Max", attr[1], "Title", attr[2], "AxisTitle", attr[2]});
                            colorInd++;
                            if(colorInd > colorMax) colorInd = 0;
                            for(Object key: trendAttr.keySet()) 
                                hmiSax.setDataAttr(newTrend, (String)key, (String)trendAttr.get(key));
                            insertVarInPrj(intSax, interfaceList, groupName, type, "", true, true, uuid, appPathName +".int", "");
                        }
                    }
                }
            }
        }
        hmiSax.writeDocument(hmiPath+File.separator+ trendSheetName + ".txt");
        archSax.writeDocument();
        return ret;
    }
    //Получение атрибутов тренда
    private static boolean getTrendAttr(ArrayList<String> tableList, String group, String sig, String[]attr){
        String tableName = null;
        int x = group.indexOf("_");
        if(x<0){
            attr[0]="0";
            attr[1]="1";
            attr[2] = sig;
            return false;
        }
        String groupAb = Tools.getAbOfSubAb(group.substring(0, x)) + group.substring(x);
        for(String s: tableList) if(groupAb.toUpperCase().contains(s.toUpperCase())) {
            tableName = s;
            break;
        }
        if(tableName!=null){
            ArrayList<String> listCol = globVar.DB.getListColumns(tableName);
            if(listCol.contains("Диапазон_мин")){
                attr[0] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Диапазон_мин");
                attr[1] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Диапазон_макс");
            }else{
                attr[0]="0";
                attr[1]="1";
            }
            attr[2] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Наименование");
            if(attr[0]==null || attr[1]==null || attr[2]==null){
                FileManager.loggerConstructor("При создании архива для сигнала \""+group+"."+sig+"\" возникли ошибки");
                return false;
            }
            return true;
        }
        FileManager.loggerConstructor("При создании архива для сигнала \""+group+"."+sig+"\" возникли ошибки");
        attr[0]="0";
        attr[1]="1";
        attr[2] = sig;
        return false;
    }
    private static void insertInArcive(String sigName, int[] archTyp, String uuid, XMLSAX archSax) {
        Node items = archSax.returnFirstFinedNode("Items");
        Node sig = archSax.findNodeAtribute(items,  new String[]{"Item","ItemName",sigName});
        if(sig!=null)archSax.removeNode(sig);
        Double tmp = 1000.0/archTyp[1];
        String Samples =((int)(tmp*86400*archTyp[3]))+"";
        String Cached =((int)(tmp*archTyp[2]))+"";
        archSax.insertChildNode(items, new String[]{"Item","ItemName",sigName,"Period",archTyp[1]+"","Samples",Samples,"Cached",Cached,"UUID",uuid});
    }
    
    static boolean isStdType(String t){
        if(t==null) return false;
        String[] std = {"INT", "REAL", "BOOL", "WORD", "DWORD", "BYTE", "USINT", "SINT", "UINT", "UDINT", "DINT", "ULINT", "LINT", "LREAL"};
        for(String s : std) if(t.equalsIgnoreCase(s)) return true;
        return false;
    }

    private static boolean setTypeHintAdd(XMLSAX HMIcfg, XMLSAX bigSax, TableDB ft, Node hmiNode, ArrayList<String[]> addVarsData, 
            String[] gctData,  int i, ArrayList<String> hintAL) {
        Node ifNode = HMIcfg.returnFirstFinedNode(hmiNode, "IF");
        String cond = HMIcfg.getDataAttr(ifNode, "cond");
        String val = HMIcfg.getDataAttr(ifNode, "val");
        if(val.equalsIgnoreCase((String)ft.getCell(cond, i))){
            gctData[0] = HMIcfg.getDataAttr(ifNode, "type");
            if(gctData[0]==null) return false;
            setAtherData(HMIcfg, bigSax, ifNode, gctData, addVarsData, hintAL);
        }else{
            Node ELS = HMIcfg.returnFirstFinedNode(ifNode, "ELSE");
            gctData[0] = HMIcfg.getDataAttr(ELS, "type");
            if(gctData[0]!=null)setAtherData(HMIcfg, bigSax, ELS, gctData, addVarsData, hintAL);
            else return setTypeHintAdd(HMIcfg, bigSax, ft, ELS, addVarsData, gctData, i, hintAL);
        }
        return true;
    }
    
    private static void setAtherData(XMLSAX HMIcfg, XMLSAX bigSax, Node ifNode, String[] gctData, ArrayList<String[]> addVarsData, ArrayList<String> hintAL){
        gctData[2] = HMIcfg.getDataAttr(ifNode, "isAlarm");
        Node myBlock = bigSax.findNodeAtribute(new String[]{"GraphicsCompositeFBType","Name",gctData[0]});
        gctData[1] = bigSax.getDataAttr(myBlock, "UUID");
        getAddVars(HMIcfg, ifNode, addVarsData);
        getHintParts(HMIcfg, ifNode, hintAL);
    }
    
    private static void getAddVars(XMLSAX HMIcfg, Node hmiNode, ArrayList<String[]> addVarsData){
        Node addVar = HMIcfg.returnFirstFinedNode(hmiNode, "additionalVar");
        if(addVar!=null){
            ArrayList<Node> addVars = HMIcfg.getHeirNode(addVar);
            for(Node av: addVars){
                String[]tmp = new String[4];
                tmp[0] = av.getNodeName();
                tmp[1] = HMIcfg.getDataAttr(av, "tableCol");
                tmp[2] = HMIcfg.getDataAttr(av, "Type");
                tmp[3] = HMIcfg.getDataAttr(av, "TypeUUID");
                addVarsData.add(tmp);
            }
        }
    }
    
    private static void getHintParts(XMLSAX HMIcfg, Node hmiNode, ArrayList<String> hintAL){
        //ArrayList<String> hintAL = new ArrayList<>();
        Node hintNode = HMIcfg.returnFirstFinedNode(hmiNode, "Hint");
        if(hintNode!=null){
            String addCol = HMIcfg.getDataAttr(hintNode, "add0");
            for(int i=1; i<100 && addCol != null; i++){
                hintAL.add(addCol);
                addCol = HMIcfg.getDataAttr(hintNode, "add"+i);
            }
        }
    }

    private static void insertInOPC(String sig, String nameSpace, String id, String idType, String uuid, String comment,
                                            XMLSAX opcSax, FileManager fm, String type, int npp) {
        Node items = opcSax.returnFirstFinedNode("Crossconnect");
        String sigId = sig;
        if(id.equalsIgnoreCase("Number")) sigId = ""+npp;
        opcSax.insertChildNode(items, new String[]{"Connection","ItemName",sig,
                                                                "Device","",
                                                                "Channel",idType+"@"+nameSpace+":"+sigId,
                                                                "UUID",uuid});
        fm.wr(npp+"\t"+sig+"\t"+comment+"\t"+type+"\n");
    }

}
