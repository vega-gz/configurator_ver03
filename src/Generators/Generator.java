package Generators;

import FrameCreate.TableDB;
import Generators.ModBus.StatusModBus;
import Tools.FileManager;
import Tools.StrTools;
import Tools.Tools;
import XMLTools.*;
import globalData.globVar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Generator {

    @SuppressWarnings("empty-statement")
    // --- прописываем сигналы для драйверов ---
    public static int genHW(TableDB ft, JProgressBar jProgressBar) throws IOException {
        boolean generetGlobal = false;
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать привязки сигналов к аппаратным каналам\n на основе глабальных сигналов?"); // сообщение с выбором
        switch (casedial) {//0 - yes, 1 - no, 2 - cancel
            case 0:
                generetGlobal = true;
                break;
            case 1:
                break;
            case 2:
                return -2;
            //break;
        }

        // найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0, x);
        nodeTable = nodeTable.substring(x + 1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";

        boolean isModbus = false; // определяет Модбас
        if (y > 0) {
            subAb = "_" + nodeTable.substring(0, y);
            nodeTable = nodeTable.substring(y + 1);
            isMb = "_mb";
            isModbus = true;
        }
        
        globVar.cfgRoot = globVar.sax.readDocument(globVar.mainConfSig); //еще раз прочитать файл
        Node findNode = globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if (findNode == null) {
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
            return -1;
        }
        Node nodeGenHW = globVar.sax.returnFirstFinedNode(findNode, "GenHW");//Ищем в этой ноде ноду GenData
        if (nodeGenHW == null) {
            FileManager.loggerConstructor("В файле ConfigSignals.xml не найдена нода \"" + nodeTable + "/GenHW" + "\"");
            return -1;
        }
        String mainDriverName = globVar.sax.getDataAttr(nodeGenHW, "hwDriver");
        String designDir = globVar.desDir + File.separator + "Design";
        XMLSAX prj = new XMLSAX();
        Node prjNode = null; // вынес сюда что бы  не было ошибок по компиляции
        if (generetGlobal) {
            prjNode = prj.readDocument(designDir + File.separator + "Project.prj");
            if (prjNode == null) {
                FileManager.loggerConstructor("Не удалось прочитать файл \"" + designDir + File.separator + "Project.prj" + "\"");
                return -1;
            }
        }

        String abType = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "Abonent_type");
        //---------------------- Определение списка экземпляров ------------------------------
        String exemplars = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "Экземпляры");
        ArrayList<String> exArr = new ArrayList<>();
        //if (!exemplars.isEmpty()) {
        //    exArr = StrTools.getListFromString(exemplars, ",");
        //} else {
        exArr.add(abonent);
        //}
        
        //------------------ Определение параметров драйвера модбаса -----------------------
        String commentTable = globVar.DB.getCommentTable(ft.tableName());
        if (commentTable == null) {
            commentTable = "";
        }
        String modbusFile = "";
        String modbusAddr = "";
        String group = "";
        String identModbus = "Modbus:";
        x = commentTable.indexOf(identModbus);
        if (x >= 0) {
            int l = commentTable.length();
            boolean validDataComment = true;
            x += identModbus.length();
            String tmpStr = commentTable.substring(x);
            String[] tmpStrArr = tmpStr.split(";");
            
            if (l < x) {
                validDataComment = false;
            }
            
            y = commentTable.indexOf(";", x);
            if (y < 0 & validDataComment) {
                validDataComment = false;
            }
            
            modbusFile = commentTable.substring(x, y).trim();
            x = y + 1;
            if (l < x & validDataComment) {
                validDataComment = false;
            }
            
            y = commentTable.indexOf(";", x);
            if (y > x & validDataComment) {
                modbusAddr = commentTable.substring(x, y).trim();
            }
            
            x = y + 1;
            if (l < x & validDataComment) {
                validDataComment = false;
            }
            
            y = commentTable.indexOf(";", x);
            if (y > x & validDataComment) {
                group = commentTable.substring(x, y).trim();
            } else {
                validDataComment = false;
            }
            
            if( !validDataComment){
                FileManager.loggerConstructor("Неправильный формат описания для драйвера в комментарии \"" + commentTable + "\" к таблице \"" + ft.tableName() + "\"");
                return -1;
            }
            
        }
        
        //--- Определяем файл с описанием
        String drvFileName = "T_" + abonent + subAb + isMb + globVar.sax.getDataAttr(nodeGenHW, "drvFile") + ".type";

        //------ Проход по экземплярам абонентов(нужны ли эти экземпляры?) ----------------
        for (String exemplar : exArr) {
            String prjFildName = exemplar + subAb + isMb + globVar.sax.getDataAttr(nodeGenHW, "globData"); // Собрать имя для запроса к глобальному файлу(ключевое имя переменной сигнала)
            String globUUID = null;
            Node globSigInPrj = null;
            String[] globSigAttr = {"Signal", "Name", prjFildName}; // поиск сигналов в глобале

            if (generetGlobal) { // если выбрали только локальные 
                globSigInPrj = prj.findNodeAtribute(prjNode, globSigAttr);
                if (globSigInPrj == null) {
                    FileManager.loggerConstructor("Нет \"" + prjFildName + " \" в глобальных сигналах проекта");
                    return -1;
                }
                globUUID = prj.getDataAttr(globSigInPrj, "UUID"); // Если локально то генерим это просто? 
            }

            XMLSAX drv = new XMLSAX();
            Node drvSignalsNode = drv.readDocument(designDir + File.separator + drvFileName);
            if (drvSignalsNode == null) {
                FileManager.loggerConstructor("Не удалось прочитать файл \"" + designDir + File.separator + drvFileName + "\"");
                return -1;
            }
            XMLSAX hw = new XMLSAX();
            String currDev = "";
            //String wfFilePref = designDir + File.separator + abonent + "_R";
            Node hwConn = null;

            //------ Проход по таблице  с сигналами ----------------
            int jpgMax = ft.tableSize();
            for (int j = 0; j < jpgMax; j++) {
                if (jProgressBar != null) {
                    jProgressBar.setValue((int) ((j + 1) * 100.0 / jpgMax));
                }
                String tagName = ft.getCell("TAG_NAME_PLC", j);
                String comment = ft.getCell("Наименование", j);
                String device = ft.getCell("Устройство", j);
                //--------------------- Определяем к какому устройству подключеены каналы ---------------------------------------
                //String hwDew = exemplar + "_" + mainDriverName;                              //Поумолчанию - КМ04
                String hwDew = abonent + "_" + mainDriverName;                              //Поумолчанию - КМ04
                String hwFileSuffix = ".km04_cfg";
                String slot;
                String chanell;
                if (isModbus) {
                    //hwDew = exemplar + "_" + modbusFile;  //Смотрим, а не по сонетовскому модбасу подключены модули
                    hwDew = abonent + "_" + modbusFile;  //Смотрим, а не по сонетовскому модбасу подключены модули
                    hwFileSuffix = ".mb_cfg";
                    slot = ft.getCell("dataType", j);
                    if (slot != null) {
                        XMLSAX sax = new XMLSAX();
                        Node root = sax.readDocument("ModbusFormat.xml");
                        Node formNode = sax.returnFirstFinedNode(root, slot);
                        slot = sax.getDataAttr(formNode, "name");
                    } else {
                        slot = "BIT";
                    }
                    chanell = (ft.getCell("mbAddr", j)) + ":" + slot;
                } else {
                    if ("Sonet".equals(abType)) {                         //Если текущий абонент использует Сонет
                        hwDew = ft.getCell("sonetModbus", j);  //Смотрим, а не по сонетовскому модбасу подключены модули
                        if ("".equals(hwDew)) { //Если не по нему,
                            //hwDew = exemplar + "_R" + device + "_LOCALBUS";  //значит каждое устройство - иной файл HW
                            hwDew = abonent + "_R" + device + "_LOCALBUS";  //значит каждое устройство - иной файл HW
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
                if (!currDev.equals(hwDew)) {
                    currDev = hwDew;
                    //hwConn = setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prj, prjFildName, globSigInPrj, globUUID); //(было у Льва)
                    String globSigInPrjType = null;
                    if (generetGlobal) {
                        globSigInPrjType = prj.getDataAttr(globSigInPrj, "Type"); // было до этого внутри метода
                        // globSigInPrjType нужно выдернуть из оригинального файла
                        hwConn = setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prjFildName, globUUID, globSigInPrjType, true); // сокращенное с готовым типом
                    } else {
                        Node typeNode = drv.returnFirstFinedNode("Type"); // берем тип из ноды головного типа драйвера
                        globSigInPrjType = prj.getDataAttr(typeNode, "UUID");

                        // достаем привязку УИДА сигнала в файле драйвера для привязки
                        String intFileName = designDir + File.separator + hwDew + ".int";
                        XMLSAX intFile = new XMLSAX();
                        Node intf = intFile.readDocument(intFileName);
                        Node lokSig = intFile.findNodeAtribute(intf, globSigAttr);          // ищем сигнал в файле драйверов
                        if (lokSig == null) {
                            globUUID = UUID.getUIID();
                        } else {
                            globUUID = intFile.getDataAttr(lokSig, "UUID");
                        }

                        hwConn = setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prjFildName, globUUID, globSigInPrjType, false); // вместо глобального УУИД генерим локально

                    }

                    if (hwConn == null) {
                        return -1;
                    }
                    if (isModbus) {//удаление коннектов с таким же адресом абонента и той же группы
                        ArrayList<Node> conns = hw.getHeirNode(hwConn);
                        for (Node n : conns) {
                            if (modbusAddr.equals(hw.getDataAttr(n, "Device")) && group.equals(hw.getDataAttr(n, "Group"))) {
                                hw.removeNode(n);
                            }
                        }
                    }
                }
                //вставляем новые коннекты
                String[] signalData = {"Field", "Name", tagName};
                Node sigNode = drv.findNodeAtribute(drvSignalsNode, signalData);
                if (sigNode == null) {
                    FileManager.loggerConstructor("В структуре \"" + prjFildName + "\" не найден сигнал \"" + tagName + "\"");
                } else {
                    if (isModbus) {
                        String[] connData = {"Connection", "ItemName", prjFildName + "." + tagName,
                            "Device", modbusAddr,
                            "Channel", chanell,
                            "Group", group,
                            "UUID", globUUID + "." + drv.getDataAttr(sigNode, "UUID"),};
                        hw.insertChildNode(hwConn, connData);
                    } else {
                        String[] findData = {"Connection", "Device", device,
                            "Slot", slot,
                            "Channel", chanell};
                        Node old = hw.findNodeAtribute(hwConn, findData);
                        if (old != null) {
                            hw.removeNode(old);
                        }
                        String[] connData = {"Connection", "ItemName", prjFildName + "." + tagName,
                            "Device", device,
                            "Slot", slot,
                            "Channel", chanell,
                            "UUID", globUUID + "." + drv.getDataAttr(sigNode, "UUID"),
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

    // --- Генерация ST файлов  ---
    public static int genSTcode(TableDB ft, boolean disableReserve, JProgressBar jProgressBar1) throws IOException { //0 -ok, 1 - not source file, 2 -impossible create file
        int casedial = JOptionPane.showConfirmDialog(null, "Генерировать Функции обработки и инициализации?"); // сообщение с выбором
        if (casedial != 0) {
            return -2; //0 - yes, 1 - no, 2 - cancel
        }
        FileManager fm = new FileManager();                                 //создали менеджер файлов
        //---------------------------------------------- найти ноду с именем таблицы в файле конфигурации, и в этой ноде ноду GenData
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0, x);
        nodeTable = nodeTable.substring(x + 1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        //String group = "";
        //boolean isModbus = false;
        if (y > 0) {
            subAb = "_" + nodeTable.substring(0, y);
            nodeTable = nodeTable.substring(y + 1);
            isMb = "_mb";
            //isModbus = true;
            //group = nodeTable.substring(3);
        }

        globVar.cfgRoot = globVar.sax.readDocument(globVar.mainConfSig); //еще раз прочитать файл
        Node nodeGenCode = globVar.sax.returnFirstFinedNode(globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable), "GenCode");
        if (nodeGenCode == null) {
            FileManager.loggerConstructor("Ошибка ноды GenCode " + nodeTable);
            return -1;
        }
        ArrayList<Node> fileList = globVar.sax.getHeirNode(nodeGenCode);
        for (Node f : fileList) { // пробегаемся по всем нодам File
            String commonFileST = (String) globVar.sax.getDataNode(f).get("src");
//            String ext = (String) globVar.sax.getDataNode(f).get("ext");
//            if(ext.equals("")) ext = ".txt";

            //создавать файлы по экземплярам или единично
            ArrayList<String> exArrList = new ArrayList<>();
            if ((String) globVar.sax.getDataNode(f).get("instanceAbonent") != null) {
                //---------------------- Определение списка экземпляров ------------------------------
                String exemplars = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "Экземпляры");
                if (!exemplars.isEmpty()) {
                    exArrList = StrTools.getListFromString(exemplars, ",");
                } else {
                    exArrList.add(abonent);
                }
            } else {
                exArrList.add(abonent);
            }

            int ret = 0;
            for (String exA : exArrList) {
                String stFileName = exA + subAb + "_" + commonFileST; //Для каждого файла
                ret = genInFile(fm, exA + subAb + isMb, commonFileST, f, ft, disableReserve, stFileName, abonent, jProgressBar1);
                System.out.println(ret);
            }

            if (ret != 0) { // тут может быть косяк
                return -1;
            }

        }
        return 0;
    }

    // --- Генерация файлов HMI ---
    public static String genHMI(TableDB ft, boolean ignoreEvent, JProgressBar jProgressBar) throws IOException {
        /*
        ignoreEvent -- игнорировать события по резервам
        */
        int casedial = JOptionPane.showConfirmDialog(null, "Листы мнемосхем для " + ft.tableName() + " генерировать?"); // сообщение с выбором
        if (casedial != 0) {
            return ""; //0 - yes, 1 - no, 2 - cancel
        }
        String modelFile;                  // 
        CompositeFBTypeValInputVars myPageFBInputVars = null;    // Объект с уидами и структурами страницы которую генерируем но если она уже есть в проекте( Переменные  InputVars)      
        String nameTable = ft.tableName();  //нашли ai ao do и тд
        int x = nameTable.indexOf("_");
        String abonent = nameTable.substring(0, x);
        String nodeTable = nameTable.substring(x + 1);
       
        //для определения модбасовских подабонентов 
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        if (y > 0) {
            subAb = nodeTable.substring(0, y + 1);
            nodeTable = nodeTable.substring(y + 1);
            isMb = "mb_";
        }

        // Читаем пользовательскую конфигурацию для ЧМИ 
        XMLSAX HMIcfg = new XMLSAX(); //
        String confHMI = globVar.ConfigHMI;
        Node hmiCfgRoot = HMIcfg.readDocument(confHMI); //AT_HMI.iec_hmi
        if (hmiCfgRoot == null) {
            FileManager.loggerConstructor("не удалось прочитать " + confHMI);
            return null;
        }
        Node findNode = HMIcfg.returnFirstFinedNode(hmiCfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if (findNode == null) {
            FileManager.loggerConstructor("Тип данных " + nodeTable + " не описан в файле" + confHMI);
            return null;
        }//Если не вылетели - значит будет генерация
        
        // Читаем ЧМИ файл Сонаты 
        XMLSAX bigSax = new XMLSAX();
        String hmiProjectFile = globVar.desDir + File.separator + "Design" + File.separator
                + globVar.DB.getDataCell("Abonents", "Abonent", abonent, "HMI") + ".iec_hmi"; // Нужен для поиска УУИДов листов
        Node bigRoot = bigSax.readDocument(hmiProjectFile);                                   // Читаем приложение по умолчанию
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта " + hmiProjectFile + " или графа HMI в окне Абоненты  пустая");
            return null;
        }//Если не вылетели - значит будет генерация
        
        String filePath = globVar.desDir + File.separator + "GenHMI"; //Установили путь для целевых генерируемых файлов
        File d = new File(filePath);
        if (!d.isDirectory()) {
            d.mkdir();
            if (!d.isDirectory()) {
                FileManager.loggerConstructor("Не удалось создать директорию GenHMI в рабочем каталоге проекта");
                return null;
            }
        }
        ArrayList<Node> hmiNodeList = HMIcfg.getHeirNode(findNode);//Берем всех наследников ноды Таблицы (по идеи GenHMI)
        String ret = "";
        for (Node hmiNode : hmiNodeList) {
            String typeGCT = HMIcfg.getDataAttr(hmiNode, "type"); // имя HMI ноды(какой тип блочка может быть в выборе IF-ELSE)
            
            String mainFileReadHMI = HMIcfg.getDataAttr(hmiNode, "target"); // файл не по умолчанию головной ноды(Для серверного по)
            if(mainFileReadHMI == null) mainFileReadHMI = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "HMI") + ".iec_hmi"; // берем по умолчанию
            if(mainFileReadHMI != null ){
                if(mainFileReadHMI.length() > 0){
                    hmiProjectFile = globVar.desDir + File.separator + "Design" + File.separator + mainFileReadHMI;
                    bigRoot = bigSax.readDocument(hmiProjectFile);                                   // Читаем приложение еще раз но конкретный файл
                    if (bigRoot == null) {
                        FileManager.loggerConstructor("Не найден файл проекта " + hmiProjectFile + " или графа HMI в окне Абоненты  пустая" +
                        "\n так же может быть не верно выбран целевой файл в атрибуте target=");
                    return null;
                    }
                }
            }

            String whoTypeFBType = ""; // Сам тип ноды HMI как будет выглядить блочок
            String typeGenFaceHHMI = HMIcfg.getDataAttr(hmiNode, "typeFace"); // какой тип основы HMI из ноды берется
            if (typeGenFaceHHMI != null) {
                whoTypeFBType = typeGenFaceHHMI;
            } else {
                whoTypeFBType = "GraphicsCompositeFBType"; // по умолчанию так как не все конфиги с аттрибутом
            }

            boolean isIF = false; // если есть аттрибут type то IF не будет работать(, так ли оно нужно?)
            for (Node findinfIF : HMIcfg.getHeirNode(hmiNode)) { // проходи опять но нодам проверя есть ли 
                if (findinfIF.getNodeName().equalsIgnoreCase("IF")) {
                    isIF = true;
                }
            }

            // *** определяем тип наших блоков которыми заполняем лист ***
            List<HashMap<String, String>> listVarHMI = new ArrayList<>();       // InputVars из блока HMI  
            List<HashMap<String, String>> listEventHMI = new ArrayList<>();       // InputVars из блока HMI  
            String uuidGCT = null;

            if (typeGCT != null) { // сбор данных по головному блоку
                CompositeFBTypeValInputVars fbBigSAX = new CompositeFBTypeValInputVars(bigSax, typeGCT); // реализовываем новый класс хранящий данные Типа Графического блока
                listVarHMI = fbBigSAX.getFBInputs();    // собрать структурированно InputVars из блока HMI (на основе которых строим блочки)
                listEventHMI = fbBigSAX.getFBEvents(); // собрать структурированно Events из блока HMI (на основе которых строим блочки)
                uuidGCT = fbBigSAX.getFBUUID();
            }

            Integer maxX = null, maxY = null, incPosFBX = null, incPosFBY = null;
            Double startPosX = null, startPosY = null, incX = null, incY = null;
            String tmp = HMIcfg.getDataAttr(hmiNode, "maxX");
            if (tmp != null) {
                maxX = Integer.parseInt(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "maxY");
            if (tmp != null) {
                maxY = Integer.parseInt(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "startPosX");
            if (tmp != null) {
                startPosX = Double.parseDouble(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "startPosY");
            if (tmp != null) {
                startPosY = Double.parseDouble(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "incX");
            if (tmp != null) {
                incX = Double.parseDouble(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "incY");
            if (tmp != null) {
                incY = Double.parseDouble(tmp);
            }
            tmp = HMIcfg.getDataAttr(hmiNode, "incPosFBX");
            if (tmp != null) {
                incPosFBX = Integer.parseInt(tmp);
            } else incPosFBX = 350;
            tmp = HMIcfg.getDataAttr(hmiNode, "incPosFBY");
            if (tmp != null) {
                incPosFBY = Integer.parseInt(tmp);
            } else incPosFBY = 420;
            if (maxX == null || maxY == null || startPosX == null || startPosY == null || incX == null || incY == null) {
                FileManager.loggerConstructor("Для типа данных " + nodeTable + " не полностью описаны координаты элементов ЧМИ");
                return null;
            }

            ArrayList<String> hintAL = new ArrayList<>();
            getHintParts(HMIcfg, hmiNode, hintAL);// Собрать данные по ноде Hint(формируем подсказку)
            modelFile = HMIcfg.getDataAttr(hmiNode, "file");//получили Ноду файла в который запись верхнего уровень,который в последствии читаем _HMI_inc(нигде в файлах нигде не используется)
            String folderNodeName = null;
            String nameGCTcommon = HMIcfg.getDataAttr(hmiNode, "name");
            String nameGCT = "T_" + abonent + "_" + subAb + nameGCTcommon;
            String pageName = nameGCT + "1";

            Node gctNode;                                                       // (это нода на каком компоненте будет строится наш документ)
            String sourceFile;
            XMLSAX HMIsax = new XMLSAX();

            if (modelFile == null || modelFile.length() <= 0) {               // Если значение переменной "file" в ноде пуста
                sourceFile = "root" + File.separator + "HMI_Sheet.txt";
                modelFile = filePath + File.separator + pageName + ".txt";     //если имени в ноде нет - конструируем имя
                Node hmiRoot = HMIsax.readDocument("HMI_Sheet.txt");
                gctNode = HMIsax.returnFirstFinedNode(hmiRoot, whoTypeFBType); // находим нужную Конструкцию с FBType файла манекена

                // Тупое решение по пока что бы не потерятся( берем тлоько нужную ноду)
                HMIsax.cleanNode(hmiRoot);
                hmiRoot.appendChild(gctNode);

                HMIsax.setDataAttr(gctNode, "Name", pageName);

                // ищем Готовый блок в головной ноде который генерируем
                myPageFBInputVars = new CompositeFBTypeValInputVars(bigSax, pageName);
                String sheetUUID = myPageFBInputVars.getFBUUID();

                if (sheetUUID == null) {
                    sheetUUID = UUID.getUIID();
                }
                HMIsax.setDataAttr(gctNode, "UUID", sheetUUID);

            } else { // тут безмыслицы если сюда попадем следующее условие вылетим из подпрограммы
                sourceFile = modelFile;
                gctNode = null;
            }
            if (gctNode == null) {
                FileManager.loggerConstructor("Не найдена нода " + whoTypeFBType + " в " + sourceFile);
                return null;
            }

            // --- Добавить или удалить значения в InputVars ноде графического типа --- 
            ArrayList<ConnectionData> connectionDataSigs = new ArrayList<>();       // Список сигналов которые надо соеденить в данном блоке
            ArrayList<ConnectionData> connectionEvent = new ArrayList<>();          // Список Событий которые надо соеденить в данном блоке
            

            Node nodeInputVars = HMIsax.returnFirstFinedNode(gctNode, "InputVars");
            Node nodeEventInputs = HMIsax.returnFirstFinedNode(gctNode, "EventInputs"); // нода входящих событий из файла манекена
            if(nodeEventInputs == null) {
                Node interfaceList = HMIsax.returnFirstFinedNode("InterfaceList");
                nodeEventInputs = HMIsax.createNode("EventInputs");
                interfaceList.appendChild(nodeEventInputs);
            }
            ControlSignals controlSignals = new ControlSignals(); // следим какие сигналы редактировали
            if (!(typeGCT == null)) {
                if (nodeInputVars != null) { //  не будет работать если вообще нет InputVars
                    Connection getElemenConnect = new ConnectionElementDeclaration(bigSax, HMIcfg, hmiNode, HMIsax, nodeInputVars, listVarHMI, myPageFBInputVars,controlSignals);
                    connectionDataSigs = getElemenConnect.getConnectionsSigsVarDeclaration(); // получить сформированные сигналы для коннекта
                }
                // прогон по файлу если только есть обозначение явно указан type в ноде
                for (ConnectionData c : connectionDataSigs) {                   // Проход по добавленным сигналам
                    if(listVarHMI != null){
                        for (HashMap<String, String> h : listVarHMI) {              // прогон по Варам оригинального головного блока HMI из мнемосхемы
                            if (c.getName().equals(h.get("Name"))) {                // Сравниваем что добавляем и что в оригинале.
                                c.setUUIDOrigSignal(h.get("UUID"));                 // вносим оригинальный УУИд в структуру
                            }
                        }
                    }
                }
                 
                
                if (nodeEventInputs != null) { //  
                    Connection elemenConnectEvent = new ConnectionElementDeclaration(bigSax, HMIcfg, hmiNode, HMIsax, nodeEventInputs, listEventHMI, myPageFBInputVars, controlSignals);
                    connectionEvent = elemenConnectEvent.getConnectionsSigsEvent(); // получить сформированные сигналы для коннекта
                }
                   for (ConnectionData c : connectionEvent) {                   // Проход по добавленным сигналам
                    if(listEventHMI != null){
                        for (HashMap<String, String> h : listEventHMI) {              // прогон по Варам оригинального головного блока HMI из мнемосхемы
                            if (c.getName().equals(h.get("Name"))) {                // Сравниваем что добавляем и что в оригинале.
                                c.setUUIDOrigSignal(h.get("UUID"));                 // вносим оригинальный УУИд в структуру
                            }
                        }
                    }
                }
                
                
            } // if (!(typeGCT == null))

            Node FBNetwork = HMIsax.returnFirstFinedNode(gctNode, "FBNetwork");         //нашел FBNetwork в манекене
            if (FBNetwork != null) {
                HMIsax.removeNode(FBNetwork);                                           //удалили содержимое ноды FBNetwork
            }

            FBNetwork = HMIsax.insertChildNode(gctNode, "FBNetwork");
            Node DataConnections = HMIsax.createNode("DataConnections");
            Node EventConnections = HMIsax.createNode("EventConnections");           // нода для Соединения событий
            int fbX = 0, fbY = 0, col = 1, row = 1;
            Double posElemX = startPosX, posElemY = startPosY;
            int pageCnt = 1;
            String nameCol = HMIcfg.getDataAttr(hmiNode, "ruName");
            boolean isAlarm = HMIcfg.getDataAttr(hmiNode, "isAlarm") != null; //!!!!!!!!!!!!!!Добавить проверку, что атрибут isAlarm == true

            // ### цикл по строкам таблицы ###
            int jpgMax = ft.tableSize();
            for (int i = 0; i < jpgMax; i++) {
                if (jProgressBar != null) {
                    jProgressBar.setValue((int) ((i + 1) * 100.0 / jpgMax));
                }
                String ruName = ft.getCell(nameCol, i);
                if ("".equals(ruName)) {
                    continue;
                }
                
                if(ignoreEvent & ft.getCell(globVar.TAGPLC, i).indexOf("Res_") >= 0){
                 continue;
                }

                ArrayList<String> removedVar = new ArrayList<>();
                ArrayList<String[]> editVar = new ArrayList<>();
                
                for (Node nodeChild : HMIcfg.getHeirNode(hmiNode)) { // прогнать по всем нодам и собрать нужные данные
                    editVar = getListEditVarValue(HMIcfg, nodeChild, null, 0); // получить список переменных которые  нужно поменять в VarValue
                    if (editVar.size() > 0) {
                        break;
                    }
                }
                
                for (Node nodeChild : HMIcfg.getHeirNode(hmiNode)) { // прогнать по всем нодам и собрать нужные данные
                    removedVar = getListRemoveVarValue(HMIcfg, nodeChild); // получить лист нод( списка  VarValue которые не вносим)
                    if (removedVar.size() > 0) {
                        break;
                    }
                }

                ArrayList<String[]> addVarsData = new ArrayList<>();                // данные для полей  генерированного блока(изменение добавление)
                getAddVars(HMIcfg, hmiNode, addVarsData, editVar, ft, i);                  // Формируем InputVar при каждом проходе
                //System.out.println(editVar.size());

                ArrayList<ConnectionData> connectionDataSigsIF = null; // Сигналы которые нужно прикрутить тем которые есть по умолчанию.
                ArrayList<ConnectionData> connectionEventIF = null;
                outerisIF:
                
                if (isIF) {//ищем условния выбора типа блока ( в ноде нет "type" выполняеться "IF") 
                    typeGCT = HMIcfg.getDataAttr(hmiNode, "type"); // Еще раз читаем из конфига так как с предыдущего условия может остаться
                    String[] gctData = new String[3]; //  3 - идентификатор что произошло в фукции
                    ArrayList<String> hintALTMP = new ArrayList<>(); // временная подсказка для этого блока
                    Node nodeIFELSE = setTypeHintAdd(HMIcfg, bigSax, ft, hmiNode, addVarsData, gctData, i, hintALTMP); // type блока,формируется подсказка, узнаем Alarm)
                    
                    boolean findBreak = false;
                    if(gctData[2] != null) findBreak = gctData[2].equals("3"); 
                    if ((nodeIFELSE == null & typeGCT == null) | (nodeIFELSE == null & findBreak)) { // пропуск когда нет не IF не type в конфиге заголовка, или BREAK
                        continue;
                    }
                    
                    if (nodeIFELSE == null) { // не нашли новый тип type блока просто продолжаем работу без IF
                        break outerisIF;
                    }
                    
                    
                    hintAL = hintALTMP; // если прошли блок то подсказка такая
                    if (gctData[0] != null) { // Нашли type ноду и подставляем нужный тип иначе по умолчанию что был или пустой
                        typeGCT = gctData[0];
                    }

                    // Поиск нового блока в головной по условию IF(Так как блок другой приходится переопределять все значения)
                    CompositeFBTypeValInputVars fbBigSAX = new CompositeFBTypeValInputVars(bigSax, typeGCT); // реализовываем новый класс хранящий данные Типа Графического блока
                    listVarHMI = fbBigSAX.getFBInputs();                            // собрать структурированно InputVars из блока HMI (на основе которых строим блочки)
                    listEventHMI = fbBigSAX.getFBEvents();                          // собрать структурированно Events из блока HMI (на основе которых строим блочки)
                    uuidGCT = fbBigSAX.getFBUUID();

                    Connection gegElemenConnect = new ConnectionElementDeclaration(bigSax, HMIcfg, nodeIFELSE, HMIsax, nodeInputVars, listVarHMI, myPageFBInputVars, controlSignals);
                    connectionDataSigsIF = gegElemenConnect.getConnectionsSigsVarDeclaration(); // получить сформированные сигналы для коннекта
                    
                    // тут каждый раз будет проверять и пересоздавать InputVars -> VarDeclaration
                    // Заново переопределение сигналов
                    for (ConnectionData c : connectionDataSigsIF) {                   // Проход по добавленным сигналам
                        for (HashMap<String, String> h : listVarHMI) {              // прогон по Варам оригинального головного блока HMI из мнемосхемы
                            if (c.getName().equals(h.get("Name"))) {                // Сравниваем что добавляем и что в оригинале.
                                c.setUUIDOrigSignal(h.get("UUID"));                 // вносим оригинальный УУИд в структуру
                            }
                        }
                    }

                    // так же Евенты собираем
                    Connection elemenConnectEvent = new ConnectionElementDeclaration(bigSax, HMIcfg, nodeIFELSE, HMIsax, nodeEventInputs, listEventHMI, myPageFBInputVars, controlSignals);
                    connectionEventIF = elemenConnectEvent.getConnectionsSigsEvent(); // получить сформированные сигналы для коннекта
                
                    // Заново переопределение Event
                    for (ConnectionData c : connectionEventIF) {                   // Проход по добавленным сигналам
                        for (HashMap<String, String> h : listEventHMI) {              // прогон по Варам оригинального головного блока HMI из мнемосхемы
                            if (c.getName().equals(h.get("Name"))) {                // Сравниваем что добавляем и что в оригинале.
                                c.setUUIDOrigSignal(h.get("UUID"));                 // вносим оригинальный УУИд в структуру
                            }
                        }
                    }
                    isAlarm = gctData[2] != null;
                }
                
                // Если в ИФ блоке что то получили для конекшена инпутваров
                if(connectionDataSigsIF != null)
                {
                    for (ConnectionData c : connectionDataSigsIF) {
                        boolean notFindSig = true;
                        for (int k = 0; k < connectionDataSigs.size(); k++) {
                            if(c.getName().equals(connectionDataSigs.get(k).getName()))
                            {// нашли совпадающее, удалили и внесли из условия
                                //connectionDataSigs.remove(k);
                                //connectionDataSigs.add(c);
                                notFindSig = false;
                                break; 
                            }
                        }
                        if(notFindSig) connectionDataSigs.add(c);
                    }
                }
                
                // Если в ИФ блоке что то получили для конекшена Event
                if(connectionEventIF != null)
                {
                    for (ConnectionData c : connectionEventIF) {
                        boolean notFindSig = true;
                        for (int k = 0; k < connectionEvent.size(); k++) {
                            if(c.getName().equals(connectionEvent.get(k).getName()))
                            {// нашли совпадающее, удалили и внесли из условия
                                //connectionDataSigs.remove(k);
                                //connectionDataSigs.add(c);
                                notFindSig = false;
                                break; 
                            }
                        }
                        if(notFindSig) connectionEvent.add(c);
                    }
                }

                // конструируем ФБ
                String nameFB = abonent + subAb + typeGCT + "_" + i; // формируем имя сигнала
                FBV objectFB = new FBV();// Объект FB
                String fbUUID = UUID.getUIID().toUpperCase();
                String[] fbData = {"FB", "Name", nameFB,
                    "Type", typeGCT,
                    "TypeUUID", uuidGCT,
                    "UUID", fbUUID,
                    "X", "" + fbX,
                    "Y", "" + fbY};
                Node nodeFB = HMIsax.insertChildNode(FBNetwork, fbData); // Вносим сигнал FB в файл
                fbX += incPosFBX;
                if (fbX > 1200) {
                    fbY += incPosFBY;
                    fbX = 0;
                }//распределение ФБ по листу редактора Сонаты

                //-- Заполнение ФБ содержимым
                String fbChildNode[] = {"VarValue",
                    "Variable", "Name",
                    "Value", "'" + ruName + "'",
                    "Type", "STRING",
                    "TypeUUID", "38FDDE3B442D86554C56C884065F87B7"};//создали массив элемента вариабле ПОС
                //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили его в ноду
                objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                String tagName = ft.getCell("TAG_NAME_PLC", i);
                fbChildNode[2] = "NameAlg";
                fbChildNode[4] = "'" + tagName + "'";
                //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили NameAlg в ноду
                objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                fbChildNode[2] = "PrefStr";
                fbChildNode[4] = "'" + subAb + isMb + "'";
                //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили PrefStr в ноду
                objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                if (isAlarm) {
                    fbChildNode[2] = "TagID";
                    fbChildNode[4] = "'" + nodeTable + tagName + "'";
                    //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили TagID в ноду
                    objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB
                }

                fbChildNode[2] = "Num";
                fbChildNode[4] = "'" + (i + 1) + "'";
                //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили Num в ноду
                objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                //Create Hint
                String hint = "";
                for (String hintPart : hintAL) {
                    String s = ft.getCell(hintPart, i);
                    if (s == null) {
                        s = hintPart;
                    }
                    if ("true".equalsIgnoreCase(s)) {
                        s = "есть";
                    } else if ("false".equalsIgnoreCase(s)) {
                        s = "нет";
                    }
                    hint += s;
                }
                if (!hint.isEmpty()) {
                    fbChildNode[2] = "hint";
                    fbChildNode[4] = "'" + hint + "'";
                    //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили Hint в ноду
                    objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB
                }

                if (isAlarm) {
                    String disableAlarm = "FALSE";
                    String visiblePar = "TRUE";
                    if (tagName.contains("Res_")) {
                        disableAlarm = "TRUE";
                        visiblePar = "FALSE";
                    }
                    fbChildNode[2] = "visiblePar";
                    fbChildNode[4] = visiblePar;
                    fbChildNode[6] = "BOOL";
                    fbChildNode[8] = "EC797BDD4541F500AD80A78F1F991834";
                    //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили visiblePar в ноду
                    objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                    fbChildNode[2] = "disableAlarm";
                    fbChildNode[4] = disableAlarm;
                    //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили disableAlarm в ноду
                    objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB
                }

                fbChildNode[2] = "pos";
                fbChildNode[4] = "(x:=" + posElemX + ",y:=" + posElemY + ")";
                fbChildNode[6] = "TPos";
                fbChildNode[8] = "17C82815436383728D79DA8F2EF7CAF2";
                //HMIsax.insertChildNode(nodeFB, fbChildNode);//добавили pos в ноду
                objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                if (!addVarsData.isEmpty()) { // если есть доп поля в нодах additionalVar
                    for (String[] s : addVarsData) { // новый метод поиска additionalVar с выборкой по свитчам
                        fbChildNode[2] = s[0];
                        String apos = "";
                        if ("STRING".equals(s[2])) {        // Определить строка это или нет
                            apos = "'";
                        }
                        String getCellT = ft.getCell(s[1], i); // запрос данных из таблицы "tableCol"  при case s[1] явно будет null
                        System.out.println();
                        if (getCellT != null) {                 // если явно не указано из какой таблицы брать возьмет из из "def"
                            fbChildNode[4] = apos + getCellT + apos;
                        } else {
                            fbChildNode[4] = apos + s[4] + apos; // нечего не нашли просто обременяем (')
                        }
                        fbChildNode[6] = s[2];
                        fbChildNode[8] = s[3];
                        objectFB.addVarValue(new FBVarValue(fbChildNode)); // вносим новое значение в объект FB

                    }

                }

                objectFB.delVar(removedVar); // Вносим что нужно удалить если таковое есть
                objectFB.editVar(editVar);   // редактируем  если есть ноды для редактирования сигналов

                for (FBVarValue arr : objectFB.getListValue()) {    // получив все данные объекта
                    HMIsax.insertChildNode(nodeFB, arr.getToXML()); //добавим каждую строку в в ноду FB
                }

                // прикручиваем связи сигналов Сигналов Var в листе 
                String[] connects = {"Connection", "Source", "",
                    "Destination", "", "SourceUUID",
                    "", "DestinationUUID", ""};
                for (ConnectionData c : connectionDataSigs) {
                    connects[2] = c.getName();
                    connects[4] = nameFB + "." + c.getName();
                    connects[6] = c.getUUIDVarDeclaration();                        
                    connects[8] = fbUUID + "." + c.getUUIDOrigSignal();
                    HMIsax.insertChildNode(DataConnections, connects);  //добавили его в ноду
                }
                // тут проход по событиям
                for (ConnectionData c : connectionEvent) {
                    connects[2] = c.getName();
                    connects[4] = nameFB + "." + c.getName();
                    connects[6] = c.getUUIDVarDeclaration();                        
                    connects[8] = fbUUID + "." + c.getUUIDOrigSignal();
                    HMIsax.insertChildNode(EventConnections, connects);  //добавили его в ноду
                }

                posElemY += incY;
                row++;
                if (row > maxY) {
                    posElemX += incX;
                    posElemY = startPosY;
                    col++;
                    row = 1;
                } // делим на страницы     
                if (col > maxX && i < ft.tableSize() - 1) {
                    if (folderNodeName == null) {//Если нет имени фолдера листов сигналов - значит мы делаем файл для импорта
                        FBNetwork.appendChild(DataConnections);
                        FBNetwork.appendChild(EventConnections);
                        HMIsax.writeDocument(modelFile);

                        modelFile = modelFile.replace("_" + pageCnt + ".txt", "_" + (pageCnt + 1) + ".txt");
                        pageCnt++;
                        pageName = nameGCT + pageCnt;

                        gctNode = HMIsax.returnFirstFinedNode(HMIsax.getRootNode(), whoTypeFBType);

                        for (Node n : HMIsax.getHeirNode(gctNode)) {
                            if (n.getNodeName().equals("FBNetwork")) {
                                HMIsax.removeNode(n);
                                FBNetwork = HMIsax.insertChildNode(gctNode, "FBNetwork");
                                break;
                            }
                        }
                        HMIsax.setDataAttr(gctNode, "Name", pageName);
                        String sheetUUID = null;

                        // поиск нужного блока в нодах(по разделенным страницам названия будут разные зависящие от номера)
                        myPageFBInputVars = new CompositeFBTypeValInputVars(bigSax, pageName);
                        sheetUUID = myPageFBInputVars.getFBUUID();

                        if (sheetUUID == null) {
                            sheetUUID = UUID.getUIID();
                        }

                        HMIsax.setDataAttr(gctNode, "UUID", sheetUUID);
                        //FBNetwork = HMIsax.insertChildNode(gctNode, "FBNetwork");
                        DataConnections = HMIsax.createNode("DataConnections");
                        EventConnections = HMIsax.createNode("EventConnections");
                        fbX = 0;
                        fbY = 0;
                        posElemX = startPosX;
                        posElemY = startPosY;
                        col = 1;
                        row = 1;
                    } else {
                        //Здесь должны быть методы добавления листов непосредственно в сонатовский файл
                    }
                }
//                }
            }
            FBNetwork.appendChild(DataConnections);
            FBNetwork.appendChild(EventConnections);
            HMIsax.writeDocument(modelFile);
            ret += nameGCT + " - " + pageCnt + " страниц. ";
        }
        return ret;
    }

    // --- Генерация Типов ---
    public static int genTypeFile(TableDB ft, JProgressBar jProgressBar) throws IOException {//0-norm, -1 - not find node
        boolean interGlobCase = true; // вносить ли в глобальные сигналы
        boolean interLocalCase = true; // вносить ли в локальные сигналы приложения
        
        String backUpPath = globVar.backupDir + File.separator;   //установили путь для бэкапа
        String filePath = globVar.desDir + File.separator + "Design"; //установили путь для проекта
        String nodeTable = ft.tableName();
        int x = nodeTable.indexOf("_");
        String abonent = nodeTable.substring(0, x);
        nodeTable = nodeTable.substring(x + 1);
        int y = nodeTable.indexOf("_mb_");
        String subAb = "";
        String isMb = "";
        if (y > 0) {
            subAb = nodeTable.substring(0, y + 1);
            nodeTable = nodeTable.substring(y + 1);
            isMb = "mb_";
        }

        globVar.cfgRoot = globVar.sax.readDocument(globVar.mainConfSig); //еще раз прочитать файл
        Node findNode = globVar.sax.returnFirstFinedNode(globVar.cfgRoot, nodeTable);//Найти там ноду, совпадающую по названию с именем таблицы
        if (findNode == null) {
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
            return -1;
        }
        Node nodeGenData = globVar.sax.returnFirstFinedNode(findNode, "GenData");//Ищем в этой ноде ноду GenData
        if (nodeGenData == null) {
            FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "/GenData\"");
            return -1;
        }
        XMLSAX prjSax = new XMLSAX();
        Node prjRoot = prjSax.readDocument(filePath + File.separator + "Project.prj");
        Node interfaceList = prjSax.returnFirstFinedNode(prjRoot, "Globals");
        NodeList nodesGenData = nodeGenData.getChildNodes(); //получаем список нод внутри ГенДаты
        //---------------------- Определение списка экземпляров ------------------------------
        String exemplars = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "Экземпляры");
        ArrayList<String> exArr = new ArrayList<>();
        if (!exemplars.isEmpty()) {
            exArr = StrTools.getListFromString(exemplars, ",");
        } else {
            exArr.add(abonent);
        }

        //------------------ Определение параметров драйвера модбаса -----------------------
        int jpgMax = nodesGenData.getLength();
        for (int i = 0; i < jpgMax; i++) {//получил размерность ноды и начал цикл
            if (jProgressBar != null) {
                jProgressBar.setValue((int) ((i + 1) * 100.0 / jpgMax));//для прогрессбара
            }
            if (nodesGenData.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node currNodeCfgXML = nodesGenData.item(i);
                ArrayList<Node> globSigList = new ArrayList<>();
                for(Node n: globVar.sax.getHeirNode(currNodeCfgXML))//Находим все ноды globData внутри ноды типа файла
                {
                    if(n.getNodeName().equals("globData")) globSigList.add(n);
                }
                String nodeName = currNodeCfgXML.getNodeName();//определяем имя ноды
                //Создаём список типов данных, для которых не надо создавать полей в структуре
                ArrayList<String> notGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(currNodeCfgXML, "notGenTyps"), ",");
                String fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "Type"); //Ищем, нет ли там определённого типа для переменных. Если нет - будем искать другими способами
                String typeFile = globVar.sax.getDataAttr(currNodeCfgXML, "typeFile"); //Ищем имя файла для случаев, когда имя ноды одно, а имя файла совсем другое
                if (typeFile == null) {
                    typeFile = "T_" + isMb + nodeName + ".type";   //если имени файла нет - синтезируем это имя из имени ноды
                }
                XMLSAX fildUuidSax = null;
                Node fildUuidRoot = null;
                String dtCol = null;
                String defType = null;
                if (fildUUID == null) {//Если тип данных для переменных не был укан явно, его надо определить
                    fildUUID = globVar.sax.getDataAttr(currNodeCfgXML, "dictionary"); //Проверяем, не указан ли в качестве типа данных словарь
                    if (fildUUID != null) {//если указан - 
                        fildUuidSax = new XMLSAX();
                        fildUuidRoot = fildUuidSax.readDocument(fildUUID); // пытаемся прочитать файл словаря
                        if (fildUuidRoot != null) {
                            fildUUID = "dictionary"; // если это удалось - выставяем флаг необходимости узнавать тип каждой переменной из словаря
                        }
                        dtCol = globVar.sax.getDataAttr(currNodeCfgXML, "col");
                        defType = globVar.sax.getDataAttr(currNodeCfgXML, "ifEmpty");
                    } else {
                        fildUUID = FileManager.findStringInFile(filePath + File.separator + typeFile, "UUID=");
                        fildUUID = StrTools.getAttributValue(fildUUID, "UUID=\"");
                    }
                    if (fildUUID == null) {
                        FileManager.loggerConstructor("Не найден файл типа данных " + "T_" + isMb + nodeName + ". Генерация прервана.");
                        return -1;
                    }
                }
                //------ Находим УУИД типа в первом приложении, в которое надо распространить этот тип данных -------------
                XMLSAX uuidSax = new XMLSAX();
                Node intFile1 = globVar.sax.returnFirstFinedNode(currNodeCfgXML, "localData");
                String intFile2 = globVar.sax.getDataAttr(intFile1, "int");
                if ("_".equals(intFile2.substring(0, 1))) {
                    intFile2 = abonent + intFile2;
                }
                Node uuidRoot = uuidSax.readDocument(filePath + File.separator + intFile2);
                String typeUUID = getUUIDfromPrj(uuidSax, uuidRoot, abonent + "_" + subAb + isMb + nodeName, "Type");
                String globUUID = getUUIDfromPrj(uuidSax, uuidRoot, abonent + "_" + subAb + isMb + nodeName, "UUID");
                uuidSax.clear();
                //---- Если сигнал есть в первом приложении - его уид и тип рапространятся на Project.prj и другие приложения
                //----------------------------------------------------------------------------------------------------------
                XMLSAX localSax = new XMLSAX();
                Node type;
                Node newFields;// = null;
                Node oldFields = null;
                String typeName = "T_" + abonent + "_" + subAb + isMb + nodeName;//достаю элементы из ноды(в данный момент T GPA AI DRV)
                String trueName = FileManager.FindFile(filePath, typeName);//вызвал метод поиска файлов по имени(надо доработать)
                if (trueName == null) {//помещаем сюда создание файла
                    trueName = typeName + ".type";
                    type = localSax.createDocument("Type");
                    if (typeUUID == null) {
                        typeUUID = UUID.getUIID();
                    }
                    localSax.setDataAttr(type, "UUID", typeUUID);
                    localSax.setDataAttr(type, "Name", typeName);
                    localSax.setDataAttr(type, "Kind", "Struct");
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                } else {//сюда помещаем добавление
                    FileManager.copyFileWoReplace(filePath + File.separator + trueName, backUpPath + trueName, true);
                    type = localSax.readDocument(filePath + File.separator + trueName);//прочитал файл в котором нашли совпадения по имени
                    if (typeUUID == null) {
                        typeUUID = type.getAttributes().getNamedItem("UUID").getNodeValue();
                    } else {
                        localSax.setDataAttr(type, "UUID", typeUUID);
                    }
                    oldFields = localSax.returnFirstFinedNode(type, "Fields");//нашел ноду Fields 
                    String[] newArray = {"Fields"};
                    newFields = localSax.insertChildNode(type, newArray);
                }
                for (int j = 0; j < ft.tableSize(); j++) {
                    System.out.println(ft.tableSize());
                    String dt = ft.getCell("dataType", j); //Определяем тип данных
                    if (dt != null && notGenTyps != null && notGenTyps.contains(dt)) {
                        continue;//если тип данных есть и есть список ненужных данных и данный тип в этом списке
                    }
                    String tagName = ft.getCell("TAG_NAME_PLC", j);//ПОЛУЧИЛИ ИЗ ТАБЛИЦЫ
                    String comment = ft.getCell("Наименование", j);//получаем НАИМЕНОВАНИЕ из таблицы
                    if (fildUuidRoot != null) {
                        String dataType = ft.getCell(dtCol, j);
                        fildUUID = getFromDict(fildUuidSax, fildUuidRoot, dataType, "dataType");
                        if (fildUUID == null) {
                            fildUUID = defType;
                        }
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
                if (oldFields != null) {
                    localSax.removeNode(oldFields);
                }
                localSax.writeDocument(filePath + File.separator + trueName);//записали файл
                for (Node globSig : globSigList) {//--------------------------------------- Занесение сигналов в .prj и .int файлы -------------------------------------
                    String name = globVar.sax.getDataAttr(globSig, "name");
                    
                    String setEnableGlobaSignal = globVar.sax.getDataAttr(globSig, "disable"); // будет ли включен в Глобальный приложения
                    if (setEnableGlobaSignal != null)
                    {
                        interGlobCase = false;
                    }else interGlobCase = true;
 
                    if (name != null) {
                        String hmiApp = globVar.DB.getDataCell("Abonents", "Abonent", abonent, "HMI");
                        Node localRoot = null;
                        Node localInterfaceList = null;
                        String file = null;
                        XMLSAX localSigSax = new XMLSAX();
                        if (!hmiApp.isEmpty()) {
                            file = hmiApp + ".int";
                            localRoot = localSigSax.readDocument(filePath + File.separator + file);
                            if (localRoot == null) {
                                FileManager.loggerConstructor("Не найден файл " + file + " в каталоге " + filePath);
                                return -1;
                            }
                            localInterfaceList = localSigSax.returnFirstFinedNode(localRoot, "InterfaceList");
                            if (localInterfaceList == null) {
                                FileManager.loggerConstructor("Не найден раздел <InterfaceList> в файле " + filePath + File.separator + file);
                                return -1;
                            }
                        }
                        for (String s : exArr) {
                            if (interGlobCase) { // вносим или пропускаем в Глобальное
                                globUUID = insertVarInPrj(prjSax, interfaceList, s + "_" + subAb + isMb + name, typeUUID, "", true, false, null,
                                        filePath + File.separator + "Project.prj", backUpPath + "Project.prj");
                                if (!hmiApp.isEmpty()) {
                                    globUUID = insertVarInPrj(localSigSax, localInterfaceList, s + "_" + subAb + isMb + name, typeUUID,
                                            "", true, true, globUUID, filePath + File.separator + file, backUpPath + file);
                                }
                            } else {
                                if (!hmiApp.isEmpty()) { //не нужно в HMI вообще помещать

                                }
                            }

                        }
                    }
                    ArrayList<Node> localSigList = globVar.sax.getHeirNode(globSig);// берем наследников globData(они должны быть localData)
                    boolean glob = name != null && exArr.size() == 1;
                    for (Node localSig : localSigList) {
                        String tmp = globVar.sax.getDataAttr(localSig, "name");
                        
                        String setEnableLocalSignal = globVar.sax.getDataAttr(localSig, "disable"); // будет ли включен в локальный приложения
                        if (setEnableLocalSignal != null)
                        {
                            interLocalCase = false;
                        }
                        
                        if (tmp != null) {
                            name = tmp;
                        } else if (name == null) {
                            FileManager.loggerConstructor("Не найдено имя переменной в разделе "
                                    + "/GenData/" + nodeName + globSig.getNodeName() + "/" + localSig.getNodeName());
                            return -1;
                        }
                        XMLSAX localSigSax = new XMLSAX();
                        String localDocName = globVar.sax.getDataAttr(localSig, "int");
                        if (localDocName == null) {
                            FileManager.loggerConstructor("Не найдено имя файла .int в разделе "
                                    + "/GenData/" + nodeName + globSig.getNodeName() + "/" + localSig.getNodeName());
                            return -1;
                        }
                        if ("_".equals(localDocName.substring(0, 1))) {
                            localDocName = abonent + localDocName;
                        }

                        Node localRoot = localSigSax.readDocument(filePath + File.separator + localDocName);
                        if (localRoot == null) {
                            FileManager.loggerConstructor("Не найден файл " + localDocName + " в каталоге " + filePath);
                            return -1;
                        }
                        Node localInterfaceList = localSigSax.returnFirstFinedNode(localRoot, "InterfaceList");
                        if (localInterfaceList == null) {
                            FileManager.loggerConstructor("Не найден раздел <InterfaceList> в файле " + filePath + File.separator + localDocName);
                            return -1;
                        }
                        
                        if(interLocalCase) // помещаем в локальное приложение 
                        {
                            insertVarInPrj(localSigSax, localInterfaceList, abonent + "_" + subAb + isMb + name, typeUUID, "", glob, true,
                                    globUUID, filePath + File.separator + localDocName, backUpPath + localDocName);
                        }
                    }
                }
            }
        }
        return 0;
    }

    // --- Функция для поиска файла с описаниями подключённых к приложению сигналов ---
    public static Node setHWdoc(XMLSAX hw, String hwDew, String hwFileSuffix, String[] globSigAttr, XMLSAX prj,
            String prjFildName, Node globSigInPrj, String globUUID) throws IOException {
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        String designDir = globVar.desDir + File.separator + "Design" + File.separator;
        hw.writeDocument();
        hw.clear();                                             //удаляем старые коннекты
        String hwFileName = designDir + hwDew + hwFileSuffix;
        String intFileName = designDir + hwDew + ".int";
        int ret = FileManager.copyFileWoReplace(hwFileName, backUpPath + File.separator + hwFileName, true);                    //создаём резервную копию
        if (ret == 2) { //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
            return null;
        }
        XMLSAX intFile = new XMLSAX();
        Node intf = intFile.readDocument(intFileName);
        Node lokSig = intFile.findNodeAtribute(intf, globSigAttr);
        if (lokSig == null) {
            ret = FileManager.copyFileWoReplace(hwFileName, backUpPath + File.separator + intFileName, true);                    //создаём резервную копию
            if (ret == 2) { //Функция копирования не нашла исходного файла
                FileManager.loggerConstructor("Не удалось прочитать файл \"" + intFileName + "\"");
                return null;
            }
            Node interfaceList = intFile.returnFirstFinedNode(intf, "InterfaceList");
            String[] newSigArr = new String[]{"Signal", "Name", prjFildName,
                "Type", prj.getDataAttr(globSigInPrj, "Type"),
                "UUID", globUUID, "Usage", "", "Global", "TRUE"};
            intFile.insertChildNode(interfaceList, newSigArr);
            intFile.writeDocument();
        }
        Node hwRoot = hw.readDocument(hwFileName);
        return hw.returnFirstFinedNode(hwRoot, "Crossconnect");
    }

    // --- Функция для поиска файла с описаниями подключённых к приложению сигналов (Разные переменные на вход NZ) ---
    public static Node setHWdoc(XMLSAX hw, String hwDew, String hwFileSuffix, String[] globSigAttr,
            String prjFildName, String globUUID, String typeSignal, boolean globalSig) {
        String backUpPath = globVar.backupDir;   //установили путь для бэкапа
        String designDir = globVar.desDir + File.separator + "Design" + File.separator;
        hw.writeDocument();
        hw.clear();                                             //удаляем старые коннекты
        String hwFileName = designDir + hwDew + hwFileSuffix;
        String intFileName = designDir + hwDew + ".int";
        try {
            int ret = FileManager.copyFileWoReplace(hwFileName, backUpPath + File.separator + hwFileName, true);                    //создаём резервную копию
            if (ret == 2) { //Функция копирования не нашла исходного файла
                FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
                return null;
            }
            XMLSAX intFile = new XMLSAX();
            Node intf = intFile.readDocument(intFileName);
            Node lokSig = intFile.findNodeAtribute(intf, globSigAttr);          // ищем сигнал в файле драйверов
            if (lokSig == null) {
                ret = FileManager.copyFileWoReplace(hwFileName, backUpPath + File.separator + intFileName, true);                    //создаём резервную копию
                if (ret == 2) { //Функция копирования не нашла исходного файла
                    FileManager.loggerConstructor("Не удалось прочитать файл \"" + intFileName + "\"");
                    return null;
                }
                Node interfaceList = intFile.returnFirstFinedNode(intf, "InterfaceList");
                String[] newSigArr = null;
                if (globalSig) {
                    newSigArr = new String[]{"Signal", "Name", prjFildName,
                        "Type", typeSignal, // 
                        "UUID", globUUID, "Usage", "", "Global", "TRUE"};
                } else { // без глобальных сигналов
                    newSigArr = new String[]{"Signal", "Name", prjFildName,
                        "Type", typeSignal, // 
                        "UUID", globUUID, "Usage", ""};
                }
                intFile.insertChildNode(interfaceList, newSigArr);
                intFile.writeDocument();
            }

        } catch (IOException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Node hwRoot = hw.readDocument(hwFileName);
        return hw.returnFirstFinedNode(hwRoot, "Crossconnect");
    }

    // ---  ---
    static int closeByErr(FileManager fm, String tmpFile, String err) throws IOException {
        FileManager.loggerConstructor(err);
        fm.closeRdStream();                                       //закрываем поток чтения
        fm.closeWrStream();                                       //закрываем поток записи
        new File(tmpFile).delete();                          //Удаляем временный файл
        return -1;
    }

    // --- Герерация ST и LUA файлов(продолжение что ли тут само внесение данных в файлы) ---
    // abonent
    static int genInFile(FileManager fm, String abSubAb, String commonFileST, Node nodeGenCode, TableDB ft, boolean disableReserve,
            String stFileName, String abonent, JProgressBar jProgressBar) throws IOException {
        String filePath = globVar.desDir + File.separator + "GenST";

        String nameToblock = commonFileST;                                 // оригинальное название ноды сохраняем и его же и ищем потом
        // проверить modbus ли таблица
        ModBus modbusChecker = new ModBus(ft.tableName());
        if(!modbusChecker.equals(StatusModBus.OK)){
            nameToblock = "Call_" + modbusChecker.getModbusFile() + "_" + modbusChecker.getNodeTable(); // так формируем 
        }
        // разбор имени если есть точка в имени(нахождение расширения)
        String etxLUA = "lua";
        String etxHTML = "html";
        boolean findLUAext = false;
        boolean findHTMLext = false;
        boolean extF = false;                                               //  Есть ли вообще расширение
        boolean findNonFunction = false;
        String[] separNameF = stFileName.split("\\.");
        String ext = ""; // Для расширения на файле
        if (separNameF.length > 1) {                                         // если есть расширение то такой файл и будет, нет так txt
            ext = separNameF[separNameF.length - 1];                        // последнее разбитое это и будет окончание
            
            if (ext.equalsIgnoreCase(etxLUA)) { // определения файла ЛУА    
                findLUAext = true; 
                disableReserve = false;
            }
            
            if (ext.equalsIgnoreCase(etxHTML)) { // определения файла репорта HTML    
                findHTMLext = true;             
                disableReserve = false;
            }
        } else {
            stFileName = stFileName + ".txt";
        }


        File d = new File(filePath);
        if (!d.isDirectory()) {
            d.mkdir();
            if (!d.isDirectory()) {
                FileManager.loggerConstructor("Не удалось создать директорию GenST в рабочем каталоге проекта");
                return -1;
            }
        }

        String srcFile = filePath + File.separator + stFileName;
        String tmpFile = filePath + File.separator + stFileName + "_tmp";
        int ret = fm.createFile2write(tmpFile);                                         //открываем файл на запись
        if (ret != 0) {
            FileManager.loggerConstructor("Не удалось создать файл \"" + tmpFile + "\"");
            return -2;
        }

        ret = fm.openFile4read(globVar.myDir, commonFileST);                            //  читаем шаблон как есть имя 
        if (ret != 0) {
            // Пробуем найти шаблон с раширением txt
            ret = fm.openFile4read(globVar.myDir, commonFileST + ".txt");
            if (ret != 0) {
                return closeByErr(fm, tmpFile, "Не удалось прочитать служебный файл (шаблон) \""
                        + globVar.myDir + File.separator + commonFileST + " or " + commonFileST + ".txt" + 
                        "\n или ошибки в шаблоне.");
            } else {
                commonFileST = commonFileST + ".txt";                                  // на всякий так как не знаю для чего вообще эта переменная
            }
        }

        String nameFunction = (String) globVar.sax.getDataNode(nodeGenCode).get("nonFunc"); //  название для головной ноды
        String algFile = (String) globVar.sax.getDataNode(nodeGenCode).get("target"); // часть названия целевого файла
        if (nameFunction != null) {
            findNonFunction = true;
        } else {
            nameFunction = "Function";
        }

        String funcName = nameToblock;//abonent + "_"+ commonFileST; // Название ноды для поиска в другом файле
        String funcUUID = null;
        String funcResultTypeUUID = null;                                             // возвращаемое значение фуйкции
        if (algFile != null) {
            if ("_".equals(algFile.substring(0, 1))) {
                algFile = abonent + algFile;
            }

            String[] separAbonent = algFile.split("\\.");
            if (separAbonent.length > 1) {
                extF = true;
            }

            XMLSAX algSax = new XMLSAX();
            String nameNodeinfile = globVar.desDir + File.separator + "Design" + File.separator + algFile;

            Node algRoot;
            if (extF) {
                algRoot = algSax.readDocument(nameNodeinfile); //или полное имя без расширения
            } else {
                algRoot = algSax.readDocument(nameNodeinfile + ".iec_st");
            }

            String[] myFunc = {nameFunction, "Name", funcName};
            //String[] myFunc = {nameFunction, "Name", "Call_ANB"};
            Node func = algSax.findNodeAtribute(algRoot, myFunc);
            if (func != null) {
                funcUUID = (String) algSax.getDataNode(func).get("UUID");
                funcResultTypeUUID = (String) algSax.getDataNode(func).get("ResultTypeUUID");
            }
            if (funcUUID == null) {
                funcUUID = UUID.getUIID();
            }
        } else {
            funcUUID = UUID.getUIID();
        }

        // это костыль для LUA файлов не вносим эту строку
        if (findLUAext || findHTMLext){
            //  знаю идиотия но работает
        }
        else{
            if (findNonFunction) {
                fm.wr("<Data>\n<" + nameFunction + " UUID=\"" + funcUUID + "\" Name=\"" + funcName + "\" ShowVarTypes=\"true\">\n");
            } else {
                //не найдена фукции и значения по умолчанию вроде для AI
                if (funcUUID != null & funcResultTypeUUID != null) {
                    fm.wr("<Data>\n<Function UUID=\"" + funcUUID + "\" Name=\"" + funcName + "\" ResultTypeUUID=\"" + funcResultTypeUUID + "\">\n");
                } else {
                    fm.wr("<Data>\n<Function UUID=\"" + funcUUID + "\" Name=\"" + funcName + "\" ResultTypeUUID=\"EC797BDD4541F500AD80A78F1F991834\">\n");
                }
            }
        }
           
        ArrayList<Node> markerEdit = new ArrayList<>();             // ноды маркеров которые нужно изменить в шаблоне   
        ArrayList<Node> blockList = globVar.sax.getHeirNode(nodeGenCode);
        for (Node block : blockList) {
            if(block.getNodeName().equalsIgnoreCase("MarkTemplate")) // поиск НОды с данными которые нужно изменить
            {
                markerEdit = globVar.sax.getHeirNode(block);
                continue;
            }
            String start = (String) globVar.sax.getDataNode(block).get("start");
            String end = (String) globVar.sax.getDataNode(block).get("end");
          
            //Создаём список типов данных, для которых не надо создавать полей в структуре
            ArrayList<String> notGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(block, "notGenTyps"), ",");
            ArrayList<String> isGenTyps = StrTools.getListFromString(globVar.sax.getDataAttr(block, "isGenTyps"), ",");
            if (start == null || end == null) {
                return closeByErr(fm, tmpFile, "В файле \"" + globVar.myDir + File.separator + "ConfigSignals.xml в разделе " + nodeGenCode.getParentNode()
                        + " неправильно сконфигурированы признаки начала и конца вставки кода");
            }

            String s = fm.rd();                                                     //Для копирования всего, что было до этой функции, (оконнчание файла)

            while (!fm.EOF && !s.contains(start)) {
                // изменение строки из шаблона с заданными параметрами
                for(Node n: markerEdit){
                    String whoEdit = (String) globVar.sax.getDataNode(n).get("edit");
                    String toEdit = "";
                    for (Node nodemarkerEdit: globVar.sax.getHeirNode(n)) {
                        toEdit += getPartText(nodemarkerEdit, abonent, null, 0); // проход по значениям аргументов
                    }
            
                    String[] cutStringIndex = s.split(whoEdit);
                    if(cutStringIndex.length > 1)
                    {
                        s = cutStringIndex[0] + toEdit + cutStringIndex[1];
                    }
                
                }
                
                fm.wr(s + "\n");                                                    // переписываем данные шаблона
                s = fm.rd();
            }
            if (fm.EOF) {
                return closeByErr(fm, tmpFile, "В файле \"" + globVar.myDir + File.separator + commonFileST + " не найдена строка \"" + start + "\"");
            }
            // опять идиотизм с LUA это править надо
            if (!findLUAext) {
                if (findHTMLext) {
                    fm.wr("<!--" + start + "-->\n"); // особый формат для html
                }else fm.wr("//" + start + "\n"); // обычная запись для ST
            } else {
                fm.wr("--" + start + "\n");
            } 

            ArrayList<Node> blockCont = globVar.sax.getHeirNode(block);
            int tsz = ft.tableSize();
            String beforeValueGenString = null;
            System.out.println(tsz);
            for (int j = 0; j < tsz; j++) {                                         //по всем строкам таблицы
//                if(j == 119){
//                     System.out.println(tsz);
//                }
                if (jProgressBar != null && tsz != 0) {
                    jProgressBar.setValue((int) ((j + 1) * 100.0 / tsz));           //Прогресс генерации
                }
                String dt = ft.getCell("dataType", j);                              //Определяем тип данных
                if (dt != null && notGenTyps != null && notGenTyps.contains(dt)) {
                    continue;                                                       //если тип данных есть и есть список ненужных данных и данный тип в этом списке
                }
                if (dt != null && isGenTyps != null && !isGenTyps.contains(dt)) {
                    continue;                                                       //если тип данных есть и есть список ненужных данных и данный тип в этом списке
                }
                for ( int k=0; k < blockCont.size(); k++) { // проход по нодам тем же формированием строк
                    Node cont =  blockCont.get(k);

                    String nodeName = cont.getNodeName();
                    if ("Function".equals(nodeName)) {
                        createFunction(cont, fm, ft, abSubAb, disableReserve, j);   // Обработка "фукции" ноды 
                    } else {
                        String addStr = createString(cont, ft, abSubAb, disableReserve, j);     // Обработка "строковой" ноды (любой другой)
                        if (addStr != null) {
                            if(beforeValueGenString != null){ // записываем предыдущий
                                if(findHTMLext){
                                    int indeChar = beforeValueGenString.lastIndexOf(';');
                                    beforeValueGenString = beforeValueGenString.substring(0, indeChar) + beforeValueGenString.substring(indeChar + 1); // обрубаем строку 
                                    fm.wr(beforeValueGenString);
                                    if(k >= blockCont.size() -1 & j >= tsz - 1) { // до присвоения не дойдет так как тут может быть не одно значение для 1 значения из таблица сигнала
                                        indeChar = addStr.lastIndexOf(';');
                                        addStr = addStr.substring(0, indeChar) + addStr.substring(indeChar + 1); // обрубаем строку 
                                        fm.wr(addStr);
                                    }
                                }else{
                                    fm.wr(beforeValueGenString);
                                }
                            } 
                            beforeValueGenString = addStr;

                        } 
                        //по просьбе  Алексея  условие может не только для LUA
                        if (findLUAext & j >= tsz - 1) { // если последняя строка то в файле удалить 
                            int indeChar = beforeValueGenString.lastIndexOf(',');
                            beforeValueGenString = beforeValueGenString.substring(0, indeChar) + beforeValueGenString.substring(indeChar + 1); // обрубаем строку 
                            fm.wr(beforeValueGenString);
                        } else{
                            if ( !findHTMLext & j >= tsz-1 & k >= blockCont.size()-1){
                                fm.wr(beforeValueGenString); // просто запись последней строки таблицы
                            } 
                        }

                    }
                }
            }

            //пролистываем в исходном файле строки со старыми вызовами и пустые строки 
            while (!fm.EOF && !s.contains(end)) {
                s = fm.rd();                                                        // читаем строки из tmp файла
            }
            while (!fm.EOF) {                                                       //дописываем хвост файла
                fm.wr(s + "\n");
                s = fm.rd();
            }
            fm.closeRdStream();                                                     //закрываем поток чтения
            fm.closeWrStream();                                                     //закрываем поток записи

            File file = new File(srcFile);                                          //создаём ссылку на исходный файл(Зачем это делать еще и с разными ссылками ?) 
            file.delete();                                                          //удаляем его
            new File(tmpFile).renameTo(file);                                       //создаём ссылку на сгенерированный файл и делаем его исходным

//            fm.openFile4read(filePath, stFileName + ".txt");                      //открываем его на чтенье
//            fm.createFile2write(filePath, stFileName + ".txt_tmp");               //открываем временный файл для генерации
            fm.openFile4read(filePath, stFileName);                                 //открываем его на чтенье
            fm.createFile2write(filePath, stFileName + "_tmp");                     //открываем временный файл для генерации
        }
        fm.closeRdStream();                                                         //закрываем поток чтения
        fm.closeWrStream();                                                         //закрываем поток записи
        new File(tmpFile).delete();                                                 //Удаляем временный файл
        return 0;
    }

    // --- Создание строки из ноды ---
    static String createString(Node args, TableDB ft, String abonent, boolean disableReserve, int j) throws IOException {
        String tmp = "";
        ArrayList<Node> arglist = null;
        // Проверка на IF 
        List<Node> nodesIFElse = new ArrayList<>();
        processingIF(globVar.sax, args, ft, j, nodesIFElse);
        if (nodesIFElse.size() > 0) {
            //System.out.println("Find IF ELSE");
            arglist = (ArrayList<Node>) nodesIFElse;
        } else {
            arglist = globVar.sax.getHeirNode(args);                  //создаём список аргументов
        }

        for (Node arg : arglist) {                                        //Цикл по всем аргументам функции
            tmp += getPartText(arg, abonent, ft, j);
        }   //Цикл по всем частям аргументов - текстовым и табличным
        String disable = "";
        if (disableReserve && ((String) ft.getCell("TAG_NAME_PLC", j)).contains("Res_")) {
            disable = "//";
        }
        if (disable.equals("") & tmp.equals("")) {
            return null;               // нечего не нашли (нет записи в файл)
        }

        return disable + tmp + ";\n";
    }

    // --- обработчик ноды "Function"  ---
    static int createFunction(Node funcNode, FileManager fm, TableDB ft, String abonent, boolean disableReserve, int j) throws IOException {
        String stFunc = (String) globVar.sax.getDataNode(funcNode).get("name"); //вычитываем её имя
        ArrayList<Node> arglist = globVar.sax.getHeirNode(funcNode);            //создаём список аргументов

        String tmp = "";
        for (Node arg : arglist) {                                              //Цикл по всем аргументам функции

            //ArrayList<Node> argParts = globVar.sax.getHeirNode(arg);
            ArrayList<Node> argParts = null;
            tmp += ",";                                                         //аргумент записан и отделён от следующего запятой

            //Проверка на IF 
            List<Node> nodesIFElse = new ArrayList<>();
            processingIF(globVar.sax, arg, ft, j, nodesIFElse);
            if (nodesIFElse.size() > 0) {
                //System.out.println("Find IF ELSE Function");
                argParts = (ArrayList<Node>) nodesIFElse;
            } else {
                argParts = globVar.sax.getHeirNode(arg);                  //создаём список аргументов
            }
            for (Node argPart : argParts) {                                     // проход по значениям аргументов
                tmp += getPartText(argPart, abonent, ft, j);

            }
        }   //Цикл по всем частям аргументов - текстовым и табличным
        String disable = "";
        if (disableReserve && ((String) ft.getCell("TAG_NAME_PLC", j)).contains("Res_")) {
            disable = "//";
        }
        fm.wr("//" + (String) ft.getCell("Наименование", j) + "\n" + disable + stFunc + "(" + tmp.substring(1) + ");\n");
        return 0;
    }

    // ---  ---
    static String getFromDict(XMLSAX sax, Node root, String s, String attr) {
        Node n = sax.returnFirstFinedNode(root, s);
        if (n == null) {
            return null;
        }
        return sax.getDataAttr(n, attr);
    }

    // --- ---
    static String getFromDict(String s, String dict) {
        XMLSAX sax = new XMLSAX();
        Node root = sax.readDocument(dict);
        Node n = sax.returnFirstFinedNode(root, s);
        if (n == null) {
            return null;
        }
        return sax.getDataAttr(n, "chng");
    }

    // ---  Разбор данных для создания строки (при работе ST)---
    static String getPartText(Node argPart, String abonent, TableDB ft, int j) {
        switch (argPart.getNodeName()) {
            case "text":
                return (String) globVar.sax.getDataAttr(argPart, "t"); // Получить просто текст с ноды "text"
            case "dbd":
                String s = ft.getCell(globVar.sax.getDataAttr(argPart, "t"), j); // Получить текст с ноды "dbd" и сравнить с тем что в таблице на данной строке
                if (s == null || s.isEmpty()) {
                    return globVar.sax.getDataAttr(argPart, "ifEmpty");
                } else {
                    return s;
                }
            case "npp":
                String numberSet = globVar.sax.getDataAttr(argPart, "start");
                if(numberSet != null)
                {
                    int foo = Integer.parseInt(numberSet);
                    return "" + (j + foo) ;
                }
                return "" + j;
            case "abonent":
                return abonent;
            case "dict":
                s = getFromDict(ft.getCell(globVar.sax.getDataAttr(argPart, "t"), j),
                        globVar.sax.getDataAttr(argPart, "dictionary"));
                if (s == null || s.isEmpty()) {
                    return globVar.sax.getDataAttr(argPart, "ifEmpty");
                } else {
                    return s;
                }
            case "switch":
//                s = ft.getCell(globVar.sax.getDataAttr(argPart, "swt"), j); // получить значение клетки в таблице из параметра swt ноды switch
//                String[] arr = {"case", "val", s};
//                Node n = globVar.sax.findNodeAtribute(argPart, arr); // если есть в списке нод наследников совпадения то присваеваем найденное
//                if (n != null) {
//                    return globVar.sax.getDataAttr(n, "def");
//                } else {
//                    return globVar.sax.getDataAttr(argPart, "default");
//                }
                return getSwitchValConfig(argPart, ft, j); // Передаем на обработку Свитч (Ноду, таблицу, строку)

        }
        return "";
    }

    // --- Парсер текстовой ноды без абонента ---
    static String getPartTextNonAbon(Node argPart, TableDB ft, int j) {
        String tmp = "";
        tmp += ",";
        for (Node n : globVar.sax.getHeirNode(argPart)) {
            tmp += getPartText(n, null, ft, j);
        }
        return tmp;
    }

    //  --- Обработка ноды switch (Нода, таблица и строка таблицы)---
    static String getSwitchValConfig(Node nodeSwitch, TableDB ft, int j) {
        String s = ft.getCell(globVar.sax.getDataAttr(nodeSwitch, "swt"), j); // получить значение клетки в таблице из параметра swt ноды switch
        String[] arr = {"case", "val", s};
        Node n = globVar.sax.findNodeAtribute(nodeSwitch, arr); // если есть в списке нод наследников совпадения то присваеваем найденное
        if (n != null) {
            return globVar.sax.getDataAttr(n, "def");
        } else {
            return globVar.sax.getDataAttr(nodeSwitch, "default");
        }
    }
// <Signal Name="GPA_DI_Settings" UUID="6DC2E85F4B6835B2209D6D8076F22EFF" Type="9D3CCA014F76318C4B750981ED2CEA67" Usage="" Global="TRUE" Comment="настройки дискретного канала" />

    // ---  ---
    static String getUUIDfromPrj(XMLSAX intFile, Node interfaceList, String Name, String who) {
        String[] findArr = {"Signal", "Name", Name};
        Node sig = intFile.findNodeAtribute(interfaceList, findArr);
        return intFile.getDataAttr(sig, who);
    }

    // --- занесения переменных в интерфейсные листы приложений Сонаты ---
    static String insertVarInPrj(XMLSAX intFile, Node interfaceList, String Name, String Type, String Comment, boolean global, boolean usage,
            String uuid, String hwFileName1, String backUpFile1) {
        String[] findArr = {"Signal", "Name", Name};
        Node sig = intFile.findNodeAtribute(interfaceList, findArr);
        //System.out.println(sig.getNodeName());
        if (sig != null) {
            if (uuid != null && global == true) { // меняем значение если есть сигнал но он есть и в глобале
                intFile.setDataAttr(sig, "Type", Type);
                intFile.setDataAttr(sig, "UUID", uuid);
                intFile.writeDocument();
                return uuid;
            } else {
                //intFile.writeDocument();
                return intFile.getDataAttr(sig, "UUID");
            }
        }
//        int  ret = FileManager.copyFileWoReplace(hwFileName, backUpFile, true);                    //создаём резервную копию
//        if(ret==2){ //Функция копирования не нашла исходного файла
//          FileManager.loggerConstructor("Не удалось прочитать файл \"" + hwFileName + "\"");
//          return null;
//        }
        if (uuid == null) {
            uuid = UUID.getUIID();
        }
        String[] insArr = {"Signal", "Name", Name,
            "UUID", uuid,
            "Type", Type,
            "Comment", Comment,};
        sig = intFile.insertChildNode(interfaceList, insArr);
        if (global) {
            intFile.setDataAttr(sig, "Global", "TRUE");
        }
        if (usage) {
            intFile.setDataAttr(sig, "Usage", "");
        }
        intFile.writeDocument();
        return uuid;
    }

    // --- Генерация OPC ---
    public static int genOPC(String serverName, String id, String idType, ArrayList<String[]> opcList, JProgressBar jProgressBar) {
        if (opcList == null || opcList.isEmpty()) {
            return 0;
        }
        String appPathName = globVar.desDir + File.separator + "Design" + File.separator + serverName;
        XMLSAX opcSax = new XMLSAX();
        if (opcSax.readDocument(appPathName + ".opcuaServer") == null) { //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName + ".opcuaServer\"");
            return -1;
        }
        Node opcConnection = opcSax.returnFirstFinedNode("Crossconnect");
        opcSax.cleanNode(opcConnection);

        XMLSAX intSax = new XMLSAX();
        Node intRoot = intSax.readDocument(appPathName + ".int");
        if (intRoot == null) { //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName + ".int\"");
            return -1;
        }
        Node interfaceList = intSax.returnFirstFinedNode("InterfaceList");

        XMLSAX bigSax = new XMLSAX();
        Node bigRoot = bigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта " + globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
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
        for (String[] sig : opcList) {
            if (jProgressBar != null) {
                jProgressBar.setValue((int) ((jpbCnt++) * 100.0 / jpgMax));
            }
            //int speceName = Integer.parseInt(sig[1]);
            int x = sig[0].indexOf(".");
            if (x < 0) {
                Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal", "Name", sig[0]});
                if (n == null) {
                    ret = -1;
                    FileManager.loggerConstructor("В Project.prj не найден сигнал \"" + sig[0] + "\"");
                }
                String type = bigSax.getDataAttr(n, "Type");
                String uuid = bigSax.getDataAttr(n, "UUID");
                String comment = bigSax.getDataAttr(n, "Comment");
                if (comment == null) {
                    comment = "";
                } else {
                    comment = comment.trim();
                }
                if (isStdType(type)) {
                    insertInOPC(sig[0], sig[1], id, idType, uuid, comment, opcSax, fm, type, cnt++);
                } else {
                    String sigFileName = FileManager.FindFile(globVar.desDir + File.separator + "Design", type, "UUID=");
                    if (sigFileName == null) {
                        FileManager.loggerConstructor("Не найден файл типа " + type + " в каталоге " + globVar.desDir + File.separator + "Design");
                        ret = -1;
                    } else {
                        XMLSAX sigSax = new XMLSAX();
                        sigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                        Node sigs = sigSax.returnFirstFinedNode("Fields");
                        ArrayList<Node> sigNodeList = sigSax.getHeirNode(sigs);//Находим все ноды
                        for (Node sigNode : sigNodeList) {
                            String nameSig = bigSax.getDataAttr(sigNode, "Name");
                            String typeSig = bigSax.getDataAttr(sigNode, "Type");
                            String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                            String commSig = bigSax.getDataAttr(n, "Comment");
                            if (commSig == null) {
                                commSig = "";
                            } else {
                                commSig = commSig.trim();
                            }
                            if (!nameSig.contains("Res_")) {
                                nameSig = sig[0] + "." + nameSig;
                                uuidSig = uuid + "." + uuidSig;
                                if (!comment.isEmpty()) {
                                    commSig = comment + "." + commSig;
                                }
                                if (!isStdType(typeSig)) {
                                    nameSig += ".PV";
                                    uuidSig += ".19F27C8242D7A36082010591B7CF4F94";
                                    typeSig = "REAL";
                                }
                                insertInOPC(nameSig, sig[1], id, idType, uuidSig, commSig, opcSax, fm, typeSig, cnt++);
                            }
                        }
                        if (ret == 0) {
                            insertVarInPrj(intSax, interfaceList, sig[0], type, "", true, true, uuid, appPathName + ".int", "");
                        }
                    }
                }
            } else {
                String groupName = sig[0].substring(2, x);
                String localName = sig[0].substring(x + 1);
                Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal", "Name", groupName});
                String type = bigSax.getDataAttr(n, "Type");
                String uuid = bigSax.getDataAttr(n, "UUID");
                String comment = bigSax.getDataAttr(n, "Comment");
                if (comment == null) {
                    comment = "";
                } else {
                    comment = comment.trim();
                }
                String sigFileName = FileManager.FindFile(globVar.desDir + File.separator + "Design", type, "UUID=");
                if (sigFileName == null) {
                    FileManager.loggerConstructor("Не найден файл типа " + type + " в каталоге " + globVar.desDir + File.separator + "Design");
                    ret = -1;
                } else {
                    XMLSAX sigSax = new XMLSAX();
                    sigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                    Node sigNode = sigSax.findNodeAtribute(new String[]{"Field", "Name", localName});
                    if (sigNode == null) {
                        FileManager.loggerConstructor("Не найден сигнал " + localName + " в файле " + globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                        ret = -1;
                    } else {
                        String typeSig = bigSax.getDataAttr(sigNode, "Type");
                        String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                        String commSig = bigSax.getDataAttr(n, "Comment");
                        if (commSig == null) {
                            commSig = "";
                        } else {
                            commSig = commSig.trim();
                        }
                        if (!comment.isEmpty()) {
                            commSig = comment + "." + commSig;
                        }
                        String tmpName = groupName + "." + localName;
                        uuidSig = uuid + "." + uuidSig;
                        if (!isStdType(typeSig)) {
                            tmpName += ".PV";
                            uuidSig += ".19F27C8242D7A36082010591B7CF4F94";
                            typeSig = "REAL";
                        }
                        insertInOPC(tmpName, sig[1], id, idType, uuidSig, commSig, opcSax, fm, typeSig, cnt++);
                        insertVarInPrj(intSax, interfaceList, groupName, type, "", true, true, uuid, appPathName + ".int", "");
                    }
                }
            }
        }
        intSax.writeDocument();
        opcSax.writeDocument();
        fm.closeWrStream();
        return ret;
    }

    //  --- Генерация архивов ---
    public static int genArchive(int[][] archTyps, ArrayList<String[]> archList, String abonent,
            JProgressBar jProgressBar, String hmiApp) throws IOException {
        //------- Создаём каталог для файлов ЧМИ
        String hmiPath = globVar.desDir + File.separator + "GenHMI"; //Установили путь для файлов
        File d = new File(hmiPath);
        if (!d.isDirectory()) {
            d.mkdir();
            if (!d.isDirectory()) {
                FileManager.loggerConstructor("Не удалось создать директорию GenHMI в рабочем каталоге проекта");
                return -1;
            }
        }
        String appPathName = globVar.desDir + File.separator + "Design" + File.separator + abonent + "_" + "Archive";
        XMLSAX archSax = new XMLSAX();
        Node archRoot = archSax.readDocument(appPathName + ".arc_cfg");
        if (archRoot == null) { //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName + ".arc_cfg" + "\"");
            return -1;
        }

        XMLSAX intSax = new XMLSAX();
        Node intRoot = intSax.readDocument(appPathName + ".int");
        if (intRoot == null) { //Функция копирования не нашла исходного файла
            FileManager.loggerConstructor("Не удалось прочитать файл \"" + appPathName + ".int" + "\"");
            return -1;
        }
        Node interfaceList = intSax.returnFirstFinedNode("InterfaceList");

        XMLSAX bigSax = new XMLSAX();
        Node bigRoot = bigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
        if (bigRoot == null) {
            FileManager.loggerConstructor("Не найден файл проекта " + globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
            return -1;
        }
        XMLSAX cfgSax = new XMLSAX();
        Node cfgRoot = cfgSax.readDocument("ArchiveConfig.xml");
        if (cfgRoot == null) {
            FileManager.loggerConstructor("Не найден файл " + globVar.myDir + File.separator + "ArchiveConfig.xml");
            return -1;
        }
        HashMap trendAttr = cfgSax.getDataNode(cfgSax.returnFirstFinedNode("Trend"));
        ArrayList<Node> colorList = cfgSax.getHeirNode(cfgSax.returnFirstFinedNode("TableColor"));

        XMLSAX hmiSax = new XMLSAX();
        Node hmiRoot = hmiSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + hmiApp + ".iec_hmi");
        if (hmiRoot == null) {
            FileManager.loggerConstructor("Не найден файл " + globVar.desDir + File.separator + "Design" + File.separator + hmiApp + ".iec_hmi");
            return -1;
        }//Если не вылетели - значит будет генерация
        String trendSheetName = abonent + "_Trend";
        //trendNode = hmiSax.findNodeAtribute(new String[]{"WindowFBType","Name",trendSheetName});
        String trendSheetUUID = hmiSax.getDataAttr(hmiSax.findNodeAtribute(new String[]{"WindowFBType", "Name", trendSheetName}), "UUID");

        hmiSax.clear();

        hmiSax.readDocument("Mnemo_Trend.txt");
        Node trendNode = hmiSax.returnFirstFinedNode("GraphicsCompositeFBType");
        if (trendNode == null) {
            FileManager.loggerConstructor("Не найдена нода <GraphicsCompositeFBType> в файле "
                    + globVar.myDir + File.separator + "Mnemo_Trend.txt");
            return -1;
        }//Если не вылетели - значит будет генерация
        hmiSax.setDataAttr(trendNode, "Name", trendSheetName);
        if (trendSheetUUID != null) {
            hmiSax.setDataAttr(trendNode, "UUID", trendSheetUUID);
        } else {
            hmiSax.setDataAttr(trendNode, "UUID", UUID.getUIID());
        }

        //Аццкие сонатоиды! Корневая нода и нода с трендами называются одинаково
        trendNode = hmiSax.returnFirstFinedNode(hmiSax.findNodeAtribute(new String[]{"FB", "Name", "TREND_WINDOW"}), "Data");

        String[] attr = new String[3];
        ArrayList<String> tableList = globVar.DB.getListTable();

        int ret = 0;
        int jpgMax = archList.size();
        int jpbCnt = 1;
        int colorInd = 0;
        int colorMax = colorList.size() - 1;
        for (int iStruct = 0; iStruct < archList.size(); iStruct++) {

            String[] sig = archList.get(iStruct);
            System.out.println(iStruct); // Для дебага подсчет
            System.out.println(sig[0]); // Для дебага подсчет

            if (jProgressBar != null) {
                jProgressBar.setValue((int) ((jpbCnt++) * 100.0 / jpgMax));
            }
            int archType = Integer.parseInt(sig[1]);
            String tableName = sig[0];
            int x = tableName.indexOf(".");
            //---------------------- Определение списка структур ------------------------------
            if (x > 0) {
                tableName = tableName.substring(2, x);
            }
            ArrayList<String> structArr = new ArrayList<>();
            String sigStructs = globVar.DB.getCommentTable(tableName);
            int z = -1;
            if (sigStructs != null) {
                if (!sigStructs.isEmpty() && sigStructs.length() > 6) {
                    z = sigStructs.indexOf("Архив:");
                }
            } else {
                System.out.println("Not find comment Archive Table " + tableName);
                FileManager.loggerConstructor("Not find comment Archive Table " + tableName);
            } // просто отработка поиска ошибок
            
            //  ууиды PV T_CalcPar T_AI_ToHMI
            String[] arrayS1 = {"T_CalcPar.type", "T_AI_ToHMI.type"};
            String[] asdad = GetUuid_PV_T_CalcPar_T_AI_ToHMI(arrayS1);

            if (z < 0) {
                structArr.add("");
            } else {
                z += 6;
                int n = sigStructs.indexOf(";", z);
                if (n < 0) {
                    FileManager.loggerConstructor("genArchive[1] - В заголовке таблицы " + tableName + " неправильно описаны структуры для архивирования");
                    ret = -1;
                    continue;
                }
                sigStructs = sigStructs.substring(z, n);
                if (!sigStructs.isEmpty()) {
                    structArr = StrTools.getListFromString(sigStructs, ",");
                } else {
                    structArr.add("");
                }
            }
            //---------------------------------------------------------------------------
            String sig0 = sig[0];
            int y = sig0.indexOf("_");
            sig0 = abonent + sig0.substring(y);
            for (String sa : structArr) {
                if (x < 0) {
                    sig0 += sa;
                    Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal", "Name", sig0});
                    if (n == null) {
                        FileManager.loggerConstructor("В проекте не найден глобальный сигнал " + sig0);
                        ret = -1;
                        continue;
                    }
                    String type = bigSax.getDataAttr(n, "Type");
                    String uuid = bigSax.getDataAttr(n, "UUID");
                    if (isStdType(type)) {
                        insertInArcive(sig0, archTyps[archType], uuid, archSax);
                    } else {
                        String sigFileName = FileManager.FindFile(globVar.desDir + File.separator + "Design", type, "UUID=");
                        if (sigFileName == null) {
                            FileManager.loggerConstructor("Не найден файл типа " + type + " в каталоге " + globVar.desDir + File.separator + "Design");
                            ret = -1;
                        } else {
                            XMLSAX sigSax = new XMLSAX();
                            sigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                            Node sigs = sigSax.returnFirstFinedNode("Fields");
                            ArrayList<Node> sigNodeList = sigSax.getHeirNode(sigs);//Находим все ноды
                            for (Node sigNode : sigNodeList) {
                                String nameSig = bigSax.getDataAttr(sigNode, "Name");
                                String typeSig = bigSax.getDataAttr(sigNode, "Type");
                                String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                                if (!nameSig.contains("Res_")) {
                                    String tmpName = sig0 + "." + nameSig;
                                    String tmpUuid = uuid + "." + uuidSig;
                                    if (!isStdType(typeSig)) {
                                        tmpName += ".PV";
                                        String fileReadPV = "";
                                        XMLSAX  CalcParOrAI_ToHMI = new XMLSAX();
                                        if (sig0.contains("CalcPar")) {
                                            tmpUuid += "." + asdad[0];
                                        }else{
                                            tmpUuid += "." + asdad[1];
                                        }
                                    }
                                    // <Trend ItemName="AO_D.Set_APK" UUID="D81CC7224B1F7C96DAA237A634367986.4C16C6034A798CBFCD04F398721A6E10" Min="0" Max="10" Log="FALSE" Color="#000000" InvColor="#00000000" Title="Управление АПК (выход на драйвер 0-10 В)" AxisTitle="Управление АПК (выход на драйвер 0-10 В)" LineWidth="2" HideScale="TRUE" HideYAxis="TRUE" Hide="TRUE" CanChange="TRUE" />

                                    insertInArcive(tmpName, archTyps[archType], tmpUuid, archSax);
                                    if (!getTrendAttr(tableList, sig0, nameSig, attr)) {
                                        ret = -1;
                                    }
                                    Node newTrend = hmiSax.insertChildNode(trendNode, new String[]{"Trend", "Color", cfgSax.getDataAttr(colorList.get(colorInd), "Color"),
                                        "ItemName", tmpName, "UUID", tmpUuid, "Min", attr[0], "Max", attr[1], "Title", attr[2], "AxisTitle", attr[2]});
                                    colorInd++;
                                    if (colorInd > colorMax) {
                                        colorInd = 0;
                                    }
                                    for (Object key : trendAttr.keySet()) {
                                        hmiSax.setDataAttr(newTrend, (String) key, (String) trendAttr.get(key));
                                    }
                                }
                            }
                            if (ret == 0) {
                                insertVarInPrj(intSax, interfaceList, sig0, type, "", true, true, uuid, appPathName + ".int", "");
                            }
                        }
                    }
                } else {
                    String groupName = tableName + sa;
                    // эту часть принес выше  из IF NZ
                    String sig0Else = tableName;
                    int yElse = sig0Else.indexOf("_");
                    sig0Else = abonent + sig0Else.substring(yElse);
                    groupName = sig0Else + sa;

                    String localName = sig0.substring(x); // Был -1
                    Node n = bigSax.findNodeAtribute(bigRoot, new String[]{"Signal", "Name", groupName});
                    if (n == null) {
                        FileManager.loggerConstructor("В проекте не найден глобальный сигнал " + groupName);
                        System.out.println("В проекте не найден глобальный сигнал " + groupName);
                        ret = -1;
                        continue;
                    }
                    String type = bigSax.getDataAttr(n, "Type");
                    String uuid = bigSax.getDataAttr(n, "UUID");
                    String sigFileName = FileManager.FindFile(globVar.desDir + File.separator + "Design", type, "UUID=");
                    if (sigFileName == null) {
                        FileManager.loggerConstructor("Не найден файл типа " + type + " в каталоге " + globVar.desDir + File.separator + "Design");
                        ret = -1;
                    } else {
                        XMLSAX sigSax = new XMLSAX();
                        sigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                        Node sigNode = sigSax.findNodeAtribute(new String[]{"Field", "Name", localName});
                        if (sigNode == null) {
                            FileManager.loggerConstructor("Не найден сигнал " + localName + " в файле " + globVar.desDir + File.separator + "Design" + File.separator + sigFileName);
                            ret = -1;
                        } else {
                            String typeSig = bigSax.getDataAttr(sigNode, "Type");
                            String uuidSig = bigSax.getDataAttr(sigNode, "UUID");
                            String tmpName = groupName + "." + localName;
                            String tmpUuid = uuid + "." + uuidSig;
                            if (!isStdType(typeSig)) {
                                tmpName += ".PV";
                                tmpUuid += ".19F27C8242D7A36082010591B7CF4F94";
                            }
                            insertInArcive(tmpName, archTyps[archType], tmpUuid, archSax);
                            if (!getTrendAttr(tableList, groupName, localName, attr)) {
                                ret = -1;
                            }
                            //Node tmpN = colorList.get(colorInd++);
                            Node newTrend = hmiSax.insertChildNode(trendNode, new String[]{"Trend", "Color", cfgSax.getDataAttr(colorList.get(colorInd), "Color"),
                                "ItemName", tmpName, "UUID", tmpUuid, "Min", attr[0], "Max", attr[1], "Title", attr[2], "AxisTitle", attr[2]});
                            colorInd++;
                            if (colorInd > colorMax) {
                                colorInd = 0;
                            }
                            for (Object key : trendAttr.keySet()) {
                                hmiSax.setDataAttr(newTrend, (String) key, (String) trendAttr.get(key));
                            }
                            insertVarInPrj(intSax, interfaceList, groupName, type, "", true, true, uuid, appPathName + ".int", "");
                        }
                    }
                }
            }
        }
        hmiSax.writeDocument(hmiPath + File.separator + trendSheetName + ".txt");
        archSax.writeDocument();
        return ret;
    }

    // --- получить UUID PV из файлов ---
    private static String[] GetUuid_PV_T_CalcPar_T_AI_ToHMI(String[] fileReadPV ){
        String[] returnUUID = new String[fileReadPV.length];
        XMLSAX CalcParOrAI_ToHMI = new XMLSAX();
        for (int i = 0; i < fileReadPV.length; i++) {
            String file = fileReadPV[i];
            CalcParOrAI_ToHMI.readDocument(globVar.desDir + File.separator + "Design" + File.separator + file);
            Node nFields = CalcParOrAI_ToHMI.returnFirstFinedNode("Fields");
            ArrayList<Node> arrayField = CalcParOrAI_ToHMI.getHeirNode(nFields);// берем Field
            for (Node nF : arrayField) {
                String nameField = CalcParOrAI_ToHMI.getDataAttr(nF, "Name");
                if (nameField.equals("PV")) {
                    returnUUID[i] = CalcParOrAI_ToHMI.getDataAttr(nF, "UUID");
                    break;
                }
            }
        }
        return returnUUID;
    }
    
    // --- Получение атрибутов тренда ---
    private static boolean getTrendAttr(ArrayList<String> tableList, String group, String sig, String[] attr) {
        String tableName = null;
        int x = group.indexOf("_");
        if (x < 0) {
            attr[0] = "0";
            attr[1] = "1";
            attr[2] = sig;
            return false;
        }
        String groupAb = Tools.getAbOfSubAb(group.substring(0, x)) + group.substring(x);
        for (String s : tableList) {
            if (groupAb.toUpperCase().contains(s.toUpperCase())) {
                tableName = s;
                break;
            }
        }
        if (tableName != null) {
            ArrayList<String> listCol = globVar.DB.getListColumns(tableName);
            if (listCol.contains("Диапазон_мин")) {
                attr[0] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Диапазон_мин");
                attr[1] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Диапазон_макс");
            } else {
                attr[0] = "0";
                attr[1] = "1";
            }
            attr[2] = globVar.DB.getDataCell(tableName, "TAG_NAME_PLC", sig, "Наименование");
            if (attr[0] == null || attr[1] == null || attr[2] == null) {
                FileManager.loggerConstructor("При создании архива для сигнала \"" + group + "." + sig + "\" возникли ошибки");
                return false;
            }
            return true;
        }
        FileManager.loggerConstructor("При создании архива для сигнала \"" + group + "." + sig + "\" возникли ошибки");
        attr[0] = "0";
        attr[1] = "1";
        attr[2] = sig;
        return false;
    }

    // ---  ---
    private static void insertInArcive(String sigName, int[] archTyp, String uuid, XMLSAX archSax) {
        Node items = archSax.returnFirstFinedNode("Items");
        Node sig = archSax.findNodeAtribute(items, new String[]{"Item", "ItemName", sigName});
        if (sig != null) {
            archSax.removeNode(sig);
        }
        Double tmp = 1000.0 / archTyp[1];
        String Samples = ((int) (tmp * 86400 * archTyp[3])) + "";
        String Cached = ((int) (tmp * archTyp[2])) + "";
        archSax.insertChildNode(items, new String[]{"Item", "ItemName", sigName, "Period", archTyp[1] + "", "Samples", Samples, "Cached", Cached, "UUID", uuid});
    }

    // ---  ---
    static boolean isStdType(String t) {
        if (t == null) {
            return false;
        }
        String[] std = {"INT", "REAL", "BOOL", "WORD", "DWORD", "BYTE", "USINT", "SINT", "UINT", "UDINT", "DINT", "ULINT", "LINT", "LREAL"};
        for (String s : std) {
            if (t.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    // оригинальный метод Льва 19.02.21 закомитил
    // --- Поле формирования Подсказки (Это больше разбор IF) LEV ---
    /*private static boolean setTypeHintAdd(XMLSAX HMIcfg, XMLSAX bigSax, TableDB ft, Node hmiNode, ArrayList<String[]> addVarsData,
     String[] gctData, int i, ArrayList<String> hintAL) {
     Node ifNode = HMIcfg.returnFirstFinedNode(hmiNode, "IF");               // выдергиваем ноду с условием выбора типа "IF"
     String cond = HMIcfg.getDataAttr(ifNode, "cond");                       // получим условия в каком столбце таблицы смотреть
     String val = HMIcfg.getDataAttr(ifNode, "val");                         // получим условия какое значение клетки таблицы сравнивать
     if (val.equalsIgnoreCase((String) ft.getCell(cond, i))) {               // Сравниваем полученные данные из ИФ с табличной клеткой названия столбца cond
     gctData[0] = HMIcfg.getDataAttr(ifNode, "type");
     if (gctData[0] == null) {                                           // Нужно ли это условие с возвратом?
     return false;
     }
     setOtherData(HMIcfg, bigSax, ifNode, gctData, addVarsData, hintAL); // Нашли значение type в ноде(так же собираются ноды наследники) , и завершаем условие
     } else {
     Node ELS = HMIcfg.returnFirstFinedNode(ifNode, "ELSE");
     gctData[0] = HMIcfg.getDataAttr(ELS, "type");                       // берем в ноде тип рекурсия пока его не найдем
     if (gctData[0] != null) {
     setOtherData(HMIcfg, bigSax, ELS, gctData, addVarsData, hintAL);
     } else {
     return setTypeHintAdd(HMIcfg, bigSax, ft, ELS, addVarsData, gctData, i, hintAL);
     }
     }
     return true;
     }
     */
    
    /* формирования Подсказки (С выбором по условиям) NZ
    *  выбор type блока
    *  или приостановка условия
    */
    private static Node setTypeHintAdd(XMLSAX HMIcfg, XMLSAX bigSax, TableDB ft, Node hmiNode, ArrayList<String[]> addVarsData,
            String[] gctData, int i, ArrayList<String> hintAL) {
        Node finedNewType = null;
        List<Node> IfElseNode = new ArrayList<>();
        IfElseNode = processingIF(HMIcfg, hmiNode, ft, i, IfElseNode);          // Обработка условий IF ELSE

        for (Node n : IfElseNode) {
            if (n.getNodeName().equals("BREAK")) {
                gctData[2] = "3"; // просто как идентификатор что что то есть
            }
            if (n.getNodeName().equals("type")) {                                 // Написал проверку ноды а не аттрибут
                finedNewType = n;
                gctData[0] = HMIcfg.getDataAttr(n, "name");           // получить Тип нового блока
                setOtherData(HMIcfg, bigSax, n, gctData, addVarsData, hintAL, ft, i);  // как и в прошлом выше методе собираем данные с ноды по условию
            }

        }

        return finedNewType;
    }

    // --- разбор нод IF ELSE в xml  ---
    private static List<Node> processingIF(XMLSAX HMIcfg, Node hmiNode, TableDB ft, int i, List<Node> nodeProcessing) {
        List<Node> checkedNode = new LinkedList<>();
        
        if (hmiNode.getNodeName().equalsIgnoreCase("IF")) { // сама ли переданная нода условие
            checkedNode.add(hmiNode);
        }else{
            checkedNode = HMIcfg.getHeirNode(hmiNode);
        }
        
        
        
        for (Node ifNode : checkedNode) {
            if (ifNode.getNodeName().equalsIgnoreCase("IF")) { 
                String cond = HMIcfg.getDataAttr(ifNode, "cond");                                               // получим условия в каком столбце таблицы смотреть
                String val = HMIcfg.getDataAttr(ifNode, "val");                                                 // получим условия какое значение клетки таблицы сравнивать

                if (val.equalsIgnoreCase((String) ft.getCell(cond, i)) || // Сравниваем полученные данные из ИФ с табличной клеткой названия столбца cond
                        compareStrTable(val, (String) ft.getCell(cond, i))) {                                   // или есть окончание то с ним
                    for (Node nD : HMIcfg.getHeirNode(ifNode)) {                                                // добавляем ноды которые прошли по условию IF
                        if (nD.getNodeName().equalsIgnoreCase("ELSE") || nD.getNodeName().equalsIgnoreCase("IF")) {
                            if (nD.getNodeName().equalsIgnoreCase("IF")) {
                                processingIF(HMIcfg, nD, ft, i, nodeProcessing);
                            }
                        } else {
                            nodeProcessing.add(nD);
                        }
                    }
                    return nodeProcessing;
                } else {
                    for (Node nodeElse : HMIcfg.getHeirNode(ifNode)) {

                        if (nodeElse.getNodeName().equalsIgnoreCase("ELSE")) {
                            for (Node nD : HMIcfg.getHeirNode(nodeElse)) {                                                    
                                if (nD.getNodeName().equalsIgnoreCase("ELSE") || nD.getNodeName().equalsIgnoreCase("IF")) {
                                    if (nD.getNodeName().equalsIgnoreCase("IF")) {
                                        processingIF(HMIcfg, nD, ft, i, nodeProcessing);
                                    }
                                }else{
                                    nodeProcessing.add(nD);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return nodeProcessing;
    }

    // --- регулярка разбора строки VAL условия IF-ELSE (простое регулярное)---
    private static boolean compareStrTable(String str, String dataCell) {
        String splitterString = "*";
        if (str != null & !str.equals("")) {
            int currentIndexChar = str.indexOf(splitterString);
            String[] arrSplitString = null;
                    
            if (currentIndexChar  > -1){
                arrSplitString = new String[]{str.substring(0, currentIndexChar)};
                while(currentIndexChar > -1){
                    int beforeIndexChar = currentIndexChar;
                    currentIndexChar = str.indexOf(splitterString, currentIndexChar + 1);
                    String[] currentCut = null;
                    if(currentIndexChar > -1){
                        currentCut = new String[]{str.substring(beforeIndexChar + 1, currentIndexChar)};
                    }else{
                        if(beforeIndexChar >= str.length()){
                            currentCut = new String[]{str.substring(beforeIndexChar, str.length() )};
                        }else{
                            currentCut = new String[]{str.substring(beforeIndexChar + 1, str.length())};
                        }
                    }
                    
                    arrSplitString = Stream.concat(Arrays.stream(arrSplitString), Arrays.stream(currentCut)).toArray(String[]::new);
                }
            }
            if(arrSplitString != null){
                String stringToPattern = "^(";
                for (int i = 0; i < arrSplitString.length; i++) {
                    String s = arrSplitString[i];
                    if (i < arrSplitString.length - 1) {
                        stringToPattern += s + ".*";
                    } else {
                        stringToPattern += s;
                    }
                }
                stringToPattern += ")$";

                Pattern pattern1 = Pattern.compile(stringToPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher matcher1 = pattern1.matcher(dataCell);
                if (matcher1.matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    // --- Собирает доп поля и подсказку тольк в IF else(это метод применяется только в setTypeHintAdd )---
    private static void setOtherData(XMLSAX HMIcfg, XMLSAX bigSax, Node ifNode, String[] gctData,
            ArrayList<String[]> addVarsData, ArrayList<String> hintAL, TableDB ft, int i) {
        gctData[2] = HMIcfg.getDataAttr(ifNode, "isAlarm");                                                         // определеяет есть ли такой аттрибут в ноде
        getAddVars(HMIcfg, ifNode, addVarsData, null, ft, i);                                                           // Доп переменные в условии
        getHintParts(HMIcfg, ifNode, hintAL);                                                                       // подсказку в условии
    }

    // --- Проверка есть ли дополнительные поля в блоке формирования сигналов из конфига---
    /*
     TableDB ft - таблица  
     int i  - строка
     для определения по иф
     */
    private static void getAddVars(XMLSAX HMIcfg, Node hmiNode, ArrayList<String[]> addVarsData, ArrayList<String[]> editVar, TableDB ft, int i) {
        Node addVar = HMIcfg.returnFirstFinedNode(hmiNode, "additionalVar");
        if (addVar != null) {
            boolean checkIF = true; // один раз только прогон IF по ноде(так как проверяет все в головной ноде)
            ArrayList<Node> addVars = HMIcfg.getHeirNode(addVar);
            for (Node av : addVars) {
                List<Node> IfElseNode = new ArrayList<>();

                // заносим если это не ноды логиги
                if (av.getNodeName().equalsIgnoreCase("IF") & checkIF) {
                    IfElseNode = processingIF(HMIcfg, addVar, ft, i, IfElseNode);          // Обработка условий IF ELSE(передавать надо именно саму ноду additionalVar так как if в ней )
                    if (IfElseNode != null) {
                        for (Node n : IfElseNode) {
                            String[] tmp = new String[5];
                            tmp[0] = n.getNodeName();
                            tmp[1] = HMIcfg.getDataAttr(n, "tableCol");
                            tmp[2] = HMIcfg.getDataAttr(n, "Type");
                            tmp[3] = HMIcfg.getDataAttr(n, "TypeUUID");
                            tmp[4] = null;      // тут хранится значение из свитча или еще от куда на будущее
                            ArrayList<String[]> editVars = getListEditVarValue(HMIcfg, n, ft, i);
                            if (editVars.size() <= 0) {
                                addVarsData.add(tmp);
                            } else {
                                for (String[] eVar : editVars) {
                                    editVar.add(eVar);
                                }

                            }

                        }
                    }
                    checkIF = false;
                }
                if (av.getNodeName().equalsIgnoreCase("IF")) {
                    continue;
                }

                String[] tmp = new String[5];
                tmp[0] = av.getNodeName();
                tmp[1] = HMIcfg.getDataAttr(av, "tableCol");
                tmp[2] = HMIcfg.getDataAttr(av, "Type");
                tmp[3] = HMIcfg.getDataAttr(av, "TypeUUID");
                tmp[4] = null;      // тут хранится значение из свитча или еще от куда на будущее
                // Проверка Ноды есть ли у нее Свитч
                ArrayList<String[]> switchesSig = checkNodeSwitch(HMIcfg, av, ft, i); // выбираем все свитчи от этого сигнала
                if (switchesSig == null) {
                    addVarsData.add(tmp); // добавляем что простой сборкой ноды свитча нет
                } else {
                    for (String[] arrSwitch : switchesSig) {
                        addVarsData.add(arrSwitch);
                    }
                }

            }
        }
    }

    // --- Проверка есть ли дополнительные поля в блоке формирования сигналов  с Выборкой Switch из ноды( NZ from LEV )---
    // (Каждый раз дергает ноду нужно просто ноду additionalVar выдернуть)
    private static ArrayList<String[]> checkNodeSwitch(XMLSAX HMIcfg, Node hmiNode, TableDB ft, int i) { // сравнение с данными из таблицы
        ArrayList<String[]> dataSwitch = null;
        // Проверка на присутствие Свитч   в самой ноде переменной
        ArrayList<Node> nodeFindSwitc = HMIcfg.getHeirNode(hmiNode);
        for (Node nSwitch : nodeFindSwitc) { // берем потенциальные все Свитчи этой ноды
            if (nSwitch.getNodeName().equals("switch")) {
                dataSwitch = new ArrayList<>();

                String[] tmp = new String[5];
                tmp[0] = hmiNode.getNodeName();
                tmp[1] = HMIcfg.getDataAttr(hmiNode, "tableCol");
                tmp[2] = HMIcfg.getDataAttr(hmiNode, "Type");
                tmp[3] = HMIcfg.getDataAttr(hmiNode, "TypeUUID");
                tmp[4] = getSwitchValConfig(nSwitch, ft, i); // запуск выборки Свитча
                dataSwitch.add(tmp);
            }
        }
        return dataSwitch;
    }

// --- Занос данных из ноды Hint ---
    private static void getHintParts(XMLSAX HMIcfg, Node hmiNode, ArrayList<String> hintAL) {
        //ArrayList<String> hintAL = new ArrayList<>();
        Node hintNode = HMIcfg.returnFirstFinedNode(hmiNode, "Hint");
        if (hintNode != null) {
            String addCol = HMIcfg.getDataAttr(hintNode, "add0");
            for (int i = 1; i < 100 && addCol != null; i++) {
                hintAL.add(addCol);
                addCol = HMIcfg.getDataAttr(hintNode, "add" + i);
            }
        }
    }

    // --- читаем Ноду Disable ( возращает список полей которые удаляются из объекта  FB )---
    private static ArrayList<String> getListRemoveVarValue(XMLSAX HMIcfg, Node hmiNode) {
        ArrayList<String> disableVar = new ArrayList<>();
        Node disNode = null;
        if (hmiNode.getNodeName().equalsIgnoreCase("Disable")) {
            disNode = hmiNode; // если подсовываем ему ноду на прямую едит
        } else {
            return disableVar;
        }

        for (Node nD : HMIcfg.getHeirNode(disNode)) {
            disableVar.add(nD.getNodeName()); // вносим имена нод входящий в состав ноды Disable
        }
        return disableVar;
    }

    // --- читаем Ноду Edit ( возращает список полей которые редактируются из конфига FB )---
    private static ArrayList<String[]> getListEditVarValue(XMLSAX HMIcfg, Node hmiNode, TableDB ft, int i) {
        /*
         возращаем сприсок
         [0] - название атрибута который меняем 
         [1] - его тип 
         [2]- значение 
         */
        ArrayList<String[]> editVar = new ArrayList<>();
        Node disNode = null;
        if (hmiNode.getNodeName().equalsIgnoreCase("Edit")) {
            disNode = hmiNode; // если подсовываем ему ноду на прямую едит
        } else {
            return editVar;
        }
        if (disNode != null) {
            for (Node nD : HMIcfg.getHeirNode(disNode)) {
                String[] nodeAtt = new String[4];
                nodeAtt[0] = nD.getNodeName(); // вносим имена нод входящий в состав ноды Disable
                HashMap<String, String> dataNode = HMIcfg.getDataNode(nD); // получим данные с ноды
                for (Map.Entry<String, String> entry : dataNode.entrySet()) { // должен по идеи получить 2 значения только
                    String named = entry.getKey();
                    String index = entry.getValue();
                    if (named.equalsIgnoreCase("Type")) {
                        nodeAtt[1] = index; // вторым значение 
                    }
                    if (named.equalsIgnoreCase("value")) {
                        nodeAtt[2] = index;
                    }
                    if (named.equalsIgnoreCase("tableCol")) { // если есть такая нода то смотрим в таблице
                        //условие по базе
                        String valFromTable = ft.getCell(index, i);
                        nodeAtt[2] = valFromTable;
                    }
                    if (named.equalsIgnoreCase("text")) { // если есть такая нода то смотрим в таблице
                        //условие по базе
                        nodeAtt[3] = index;
                    }
                }
                editVar.add(nodeAtt);
            }
        }
        return editVar;
    }

//    // --- получить объект списки полей из HMI (его свежие UUID) (почему нигде не применяется ?)---
//    private static List<HashMap<String, String>> getFBInputsVarIEC_HMI(XMLSAX bigSax, Node myBlock) {
//        if (myBlock == null) {
//            return null;
//        }
//        List<HashMap<String, String>> dataFromFBparent = new ArrayList<>();
//        Node nodeInputVars = bigSax.returnFirstFinedNode(myBlock, "InputVars");
//        if (nodeInputVars == null) {
//            return null;
//        }
//        for (Node n : bigSax.getHeirNode(nodeInputVars)) {
//            //System.out.println(n.getNodeName());
//            dataFromFBparent.add(bigSax.getDataNode(n));
//        }
//        return dataFromFBparent;
//    }

    private static void insertInOPC(String sig, String nameSpace, String id, String idType, String uuid, String comment,
            XMLSAX opcSax, FileManager fm, String type, int npp) {
        Node items = opcSax.returnFirstFinedNode("Crossconnect");
        String sigId = sig;
        if (id.equalsIgnoreCase("Number")) {
            sigId = "" + npp;
        }
        opcSax.insertChildNode(items, new String[]{"Connection", "ItemName", sig,
            "Device", "",
            "Channel", idType + "@" + nameSpace + ":" + sigId,
            "UUID", uuid});
        fm.wr(npp + "\t" + sig + "\t" + comment + "\t" + type + "\n");
    }

}
