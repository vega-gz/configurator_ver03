package XMLTools;

import DataBaseConnect.DataBase;
import configurator.RemoveDTDFromSonataFile;
import configurator.Struct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import DataBaseConnect.DataBase;
import XMLTools.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class XMLSAX {

    private final String AI_UUID = UUID.getUUID();
    private final String AI_HMI_UUID = UUID.getUUID();
    private final String AI_DRV_UUID = UUID.getUUID();
    private final String AI_PLC_UUID = UUID.getUUID();
    private final String T_GPA_AI_HMI_UUID = UUID.getUUID();

    private final String AO_HMI_UUID = UUID.getUUID();
    private final String AO_PLC_UUID = UUID.getUUID();
    private final String AO_DRV_UUID = UUID.getUUID();
    private final String T_AO_UUID = UUID.getUUID();
    String upperUUID;

    private final String All_Random_UUID = UUID.getUUID();

    String globalpatchF;//сюда записываем путь,куда писать наши сигналы

    // не понимаю зачем я такую делаю структуру и потом ее сложно передаю в XML для внесения 
    private Struct struct; // это новое класс для структуры

    String massParametrsAI_[][] = {};

//    
    void createTypeAllSignal(ArrayList<String[]> arg, String name, String UUIDType, String UUDstruc, String file) throws ParserConfigurationException {
        // struct = new Struct(name, All_Random_UUID, All_Random_UUID);//разобраться со вторым рандомом в параметрах(он не должен быть рандомным)
        globalpatchF = file;
        String patchF = globalpatchF + "\\" + name + ".type";
        //Iterator<String[]> iter_arg = arg.iterator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", name);
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", UUDstruc);//вот это UUDSTRUC должен совпадать с дочерними уидами,то естьв нем должны быть сигналы с типом его уида
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        root.appendChild(Fields);
        for (int i = 0; i < arg.size(); i++) {
            for (int j = 0; j < i; j++) {
                if ((arg.get(i)[0].equals(arg.get(j)[0])) || arg.get(i)[1].equals(arg.get(j)[1])) {
                    System.out.println("Одинаковые имена " + arg.get(j)[0] + " в строках" + i + " и " + j);
                }
            }
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", arg.get(i)[0]);
            Field.setAttribute("Type", UUIDType);//задали тип данных рукописно.Кстати не знаю верно это или нет Но вроде пишет что то
            Field.setAttribute("UUID", arg.get(i)[1]);
            Field.setAttribute("Comment", arg.get(i)[2]);
            Fields.appendChild(Field);

        }

        try {
            writeDocument(doc, patchF);
        } catch (TransformerFactoryConfigurationError ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);//
        } catch (TransformerException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void createTypeDRV(ArrayList<String[]> arg, String name, String file, String Type) throws ParserConfigurationException {
        // struct = new Struct(name, All_Random_UUID, All_Random_UUID);//разобраться со вторым рандомом в параметрах(он не должен быть рандомным)
        globalpatchF = file;
        String patchF = globalpatchF + "\\" + name + ".type";
        Iterator<String[]> iter_arg = arg.iterator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", name);
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", AI_UUID);//
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        root.appendChild(Fields);

        while (iter_arg.hasNext()) {
            String[] field = iter_arg.next();
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", field[0]);
            Field.setAttribute("Type", Type);//задали тип данных рукописно.Кстати не знаю верно это или нет Но вроде пишет что то
            Field.setAttribute("UUID", field[1]);
            Field.setAttribute("Comment", field[2]);
            Fields.appendChild(Field);
            //  struct.addData(field[0], "BOOL", field[1], field[2]);
        }

    }

    public void runBasecreateTypeAll(String file, String nameTable, String nameSignal, String UUID_Type, String UUID_Parent) throws ParserConfigurationException {
        DataBase workbase = new DataBase();

        ArrayList<String[]> dataFromGPA_Sig = workbase.getSelectData(nameTable);//
        createTypeAllSignal(dataFromGPA_Sig, nameSignal, UUID_Type, UUID_Parent//AI_UUID нужно изучить,мне кажется это дерьмо не должно быть рандомным
                , file);

    }

    public void runBasecreateTypeAll(String nameTable, String nameSignal, String file, String Type) throws ParserConfigurationException {
        DataBase workbase = new DataBase();

        ArrayList<String[]> dataFromGPA_Sig = workbase.getSelectData(nameTable);
        createTypeDRV(dataFromGPA_Sig, nameSignal, file, Type);
    }

    // --- Передача собственной структуры в глобальные переменные Сонаты --- 
    public void sendStructToGlobV(Struct structInt) throws ParserConfigurationException, SAXException, IOException, DOMException, XPathExpressionException,
            TransformerFactoryConfigurationError, TransformerException, XPathFactoryConfigurationException, InterruptedException {
        DomRW realise = new DomRW(structInt); // пересылаем структуру для добавления  ее в глобальные переменные
        realise.runMethods(); // это надо вытащить в Главную панель
    }

    // Добавляем сигналы в Мнемосхемы  в ручную сделано, надо автоматом
    public void addSignalesMnemo(ArrayList<String[]> lisSig, String nameListSign, String filepatch, String UUIDHigth, String GName,
            String UUIDSourse, String UUIDBlock, String TypeName, String UUIDSourceName, String UUIDBlockName, String ElemName) throws SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError,
            TransformerException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException {
        String myVarU_0 = UUID.getUUID();
        String myVarU_0_0 = UUID.getUUID(); // УИД переменной для связки с переменно входа объекта
        String myVarU_1 = UUID.getUUID();
																																					

        //  String uuIdTBaSence = UUIDHigth; // перебрать файлы в его поиска  // Это уид TBaseAnIn
        Iterator<String[]> iter_arg = lisSig.iterator();
        //  globalpatchF="C:\\Users\\cherepanov\\Desktop\\ArchNew\\Design";
        String patchF = filepatch + "\\" + "AT_HMI.iec_hmi"; // сюда будем вносиь структуру
        DocumentBuilderFactory document = DocumentBuilderFactory.newInstance();
        DocumentBuilder doc = document.newDocumentBuilder();
        // это из тестового метода преобразовываем файл для чтения XML
        RemoveDTDFromSonataFile testStart = new RemoveDTDFromSonataFile(patchF);
        String documenWithoutDoctype = testStart.methodRead(patchF);// Так читаем и получаем преобразованные данные, 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        // так преобразовываем строку в поток и скармливаем билдеру XML
        InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
        Document document_final = factory.newDocumentBuilder().parse(stream);
        XPathFactory pathFactory = XPathFactory.newInstance();//создали объект для чтения XML
        XPath xpath = pathFactory.newXPath();

        // а вот тут надо посчитать сколько переменных
        XPathExpression expr = xpath.compile("SubAppType/FBLibrary");
        NodeList nodes = (NodeList) expr.evaluate(document_final, XPathConstants.NODESET);
        int Number = 0, Num = 0, Counter = 1,NumElem=0;

        //  так как нода у нас одна то пишем только в 1 по этому for так работает либо пишем в первую.
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            Document GraphicsCompositeType = createTGraphicsCompositeType(GName); // так забрали документ из метода
            Element FBNetwork = (Element) GraphicsCompositeType.getElementsByTagName("FBNetwork").item(0); // вытягиваем ноду элемента FBNetwork из Документа который получили выше

            Node rootGP = GraphicsCompositeType.getFirstChild(); // Начальная нода файла (что будем импортировать)

		  
            // настройка сигналов помещаемых в Графический компонент
            int Ycord = 0; // Переменная смещения по Y в виде компонентов
            int NumberSign = -1;

            int xPos = 2;
            int yPos = 0;
            int sumColumn = 1; // Количество столбцов
            int offset = 747;
            String visibleAI = "", disableVisible = "";// Переменная на смещение по иксам в нарисованном элементе

            Element DataConnections = GraphicsCompositeType.createElement("DataConnections"); // это для связи переменных с входными сигналами(чуть позже реализовать)
            
            //-----ЭТО СОЗДАНИЕ T_BASE_------И CONNECT//
            
            while (iter_arg.hasNext()) {//это цикл создания одного блока 
                String XYposition = "(x:=" + Integer.toString(xPos) + ",y:=" + Integer.toString(yPos) + ")"; //"(x:=0,y:=0)" это позиция графики первой вкладки
                String uuidFB = getUIID();
                String[] field = iter_arg.next();
                Element FB = GraphicsCompositeType.createElement("FB"); // Создаем FB в вытянутом элементе из фукции создания графического компонента
                String nameBAnpartClone = ElemName + "_" + (Num++); // так связь делается переменных ее основа
                FB.setAttribute("Name", nameBAnpartClone);
                FB.setAttribute("Type", TypeName);//исправить почему ANOut
                FB.setAttribute("UUID", uuidFB);
                FB.setAttribute("TypeUUID", UUIDHigth); // уид TBaSence
                FB.setAttribute("X", "-704.75");
                FB.setAttribute("Y", Integer.toString(Ycord)); // Меняем только Y
                Ycord = Ycord + 450;

                Element VarValue0 = GraphicsCompositeType.createElement("VarValue");
                VarValue0.setAttribute("Variable", "PrefStr");
                String VPrefStr = "'" + nameListSign + "'"; // Только таким видом добился
                System.out.println(VPrefStr);
                VarValue0.setAttribute("Value", VPrefStr);
                VarValue0.setAttribute("Type", "STRING");
                VarValue0.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                FB.appendChild(VarValue0);

                Element VarValue1 = GraphicsCompositeType.createElement("VarValue");
                VarValue1.setAttribute("Variable", "pos");
                VarValue1.setAttribute("Value", XYposition);
                VarValue1.setAttribute("Type", "TPos");
                VarValue1.setAttribute("TypeUUID", "17C82815436383728D79DA8F2EF7CAF2");
                FB.appendChild(VarValue1);

                Element VarValue2 = GraphicsCompositeType.createElement("VarValue");
                VarValue2.setAttribute("Variable", "NameAlg");
                String VNameAlg = "\u0027" + field[0] + "\u0027";// Только так работает как добиться методом кода не понятно
                VarValue2.setAttribute("Value", VNameAlg); // Название сигнала

                VarValue2.setAttribute("Type", "STRING");
                VarValue2.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                FB.appendChild(VarValue2);
                FBNetwork.appendChild(FB);

                Element VarValue3 = GraphicsCompositeType.createElement("VarValue");
                VarValue3.setAttribute("Variable", "Name");
                String NameComm = "\u0027" + field[2] + "\u0027";
                VarValue3.setAttribute("Value", NameComm);
                VarValue3.setAttribute("Type", "String");
                VarValue3.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");//этот уид в типе TBaseSen
                FB.appendChild(VarValue3);

                int result = field[0].indexOf("Res");
                if (result >= 0) {
                    visibleAI = "FALSE";
                    disableVisible = "TRUE";
                } else {
                    visibleAI = "TRUE";
                    disableVisible = "FALSE";
                }
                Element VarValue4 = GraphicsCompositeType.createElement("VarValue");
                VarValue4.setAttribute("Variable", "visiblePar");
                VarValue4.setAttribute("Value", visibleAI);
                VarValue4.setAttribute("Type", "BOOL");
                VarValue4.setAttribute("TypeUUID", "EC797BDD4541F500AD80A78F1F991834");
                FB.appendChild(VarValue4);

                Element VarValue5 = GraphicsCompositeType.createElement("VarValue");
                VarValue5.setAttribute("Variable", "disableAlarm");
                VarValue5.setAttribute("Value", disableVisible);
                VarValue5.setAttribute("Type", "BOOL");
                VarValue5.setAttribute("TypeUUID", "EC797BDD4541F500AD80A78F1F991834");
                FB.appendChild(VarValue5);

                Element VarValue6 = GraphicsCompositeType.createElement("VarValue");
                VarValue6.setAttribute("Variable", "TagID");
                VarValue6.setAttribute("Value", "\u0027" + field[0] + field[2] + "\u0027");
                VarValue6.setAttribute("Type", "STRING");
                VarValue6.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                FB.appendChild(VarValue6);
                
                Element VarValue8=GraphicsCompositeType.createElement("VarValue");
                VarValue8.setAttribute("Variable","Num");
                VarValue8.setAttribute("Value","\u0027"+(NumElem++)+"\u0027");
                VarValue8.setAttribute("Type", "STRING");
                VarValue8.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                FB.appendChild(VarValue8);

                if (TypeName == "T_BaseAnIn") {
                    Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                    VarValue7.setAttribute("Variable", "hint");
                    VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + "," + field[6] + "," + field[7] + "," + field[8] + ","
                            + field[9] + "," + field[10] + "," + field[11] + "," + field[12] + "," + field[13] + "," + field[14] + "," + field[15] + "," + "\u0027");
                    VarValue7.setAttribute("Type", "STRING");
                    VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                    FB.appendChild(VarValue7);

                } else if (TypeName == "T_BaseAnOut") {
                    Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                    VarValue7.setAttribute("Variable", "hint");
                    VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + "," + field[6] + "," + field[7] + "," + field[8] + ","
                            + field[9] + "," + field[10] + "\u0027");
                    VarValue7.setAttribute("Type", "STRING");
                    VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                    FB.appendChild(VarValue7);

                } else if (TypeName == "T_BaseDO") {
                    Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                    VarValue7.setAttribute("Variable", "hint");
                    VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + ","
                            + field[6] + "," + field[7] + "," + field[8] + "," + "\u0027");
                    VarValue7.setAttribute("Type", "STRING");
                    VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                    FB.appendChild(VarValue7);

                } else if (TypeName == "T_BaseDI") {
                    Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                    VarValue7.setAttribute("Variable", "hint");
                    VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + ","
                            + field[6] + "," + field[7] + "," + field[8] + "," + "\u0027");
                    VarValue7.setAttribute("Type", "STRING");
                    VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                    FB.appendChild(VarValue7);

                }
                

                {
                    Element Connection = GraphicsCompositeType.createElement("Connection");
                    Connection.setAttribute("Source", "PrefAb");
                    Connection.setAttribute("Destination", "BaseAnPar_Test_" + (Number++) + ".PrefAb");
                    Connection.setAttribute("SourseUUID", UUIDSourse);
                    Connection.setAttribute("DestinationUUID", uuidFB + "." + UUIDBlock);
                    DataConnections.appendChild(Connection);
                    FBNetwork.appendChild(DataConnections);
                }
                {
                    Element Connection = GraphicsCompositeType.createElement("Connection");
                    Connection.setAttribute("Source", "NameRU");
                    Connection.setAttribute("Destination", "BaseAnPar_Test_" + (Number++) + ".NameRU");
                    Connection.setAttribute("SourseUUID", UUIDSourceName);
                    Connection.setAttribute("DestinationUUID", uuidFB + "." + UUIDBlockName);
                    DataConnections.appendChild(Connection);
                    FBNetwork.appendChild(DataConnections);
                }
                if (sumColumn > 0) {
                    xPos = xPos + offset; // так меняем смещение графики по иксам
                } else {
                    xPos = 2;
                    sumColumn = 2;
                    yPos = yPos + 24;
                }
                if (NumberSign >63) {
                    NumElem=0;
                    Num = 0;
                    NumberSign = -1;
                    FBNetwork.appendChild(DataConnections);
                    Node importNode = document_final.importNode(rootGP, true); // Вытягиваем элемент и импортируем Импорт обязателен
                    n.appendChild(importNode); // Добавляем коренную ноду в Мнемосхему уже после всех преобразований

                    Ycord = 0; // Переменная смещения по Y в виде компонентов
                    xPos = 2;
                    yPos = 0;
                    offset = 747; // Переменная на смещение по иксам в нарисованном элементе

                    GraphicsCompositeType = createTGraphicsCompositeType(GName + (Counter++)); // так забрали документ из метода
                    FBNetwork = (Element) GraphicsCompositeType.getElementsByTagName("FBNetwork").item(0); // вытягиваем ноду элемента FBNetwork из Документа который получили выше
                    rootGP = GraphicsCompositeType.getFirstChild(); // Начальная нода файла (что будем импортировать)
                    DataConnections = GraphicsCompositeType.createElement("DataConnections"); // это для связи переменных с входными сигналами(чуть позже реализовать)

                }

                ++NumberSign;
                --sumColumn;

            }

            FBNetwork.appendChild(DataConnections);
            Node importNode = document_final.importNode(rootGP, true); // Вытягиваем элемент и импортируем Импорт обязателен
            n.appendChild(importNode); // Добавляем коренную ноду в Мнемосхему уже после всех преобразований

        }
        // Тут запустим запись в файл
        writeDocument(document_final, patchF);
        //и возвращаем ему удаленный DOCTYPE
        testStart.returnToFileDtd(patchF);
    }

    public String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается
        String uiid_str = uniqueKey.toString().replace("-", "");
        upperUUID = uiid_str.toUpperCase(Locale.ENGLISH);

        return upperUUID;
    }

    // --- Метод создания элементов TGraphicsCompositeType ---
    Document createTGraphicsCompositeType(String GraphName) throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();

        String TypeuuIdString = "38FDDE3B442D86554C56C884065F87B7";
        String TypeuuIdTpos = "17C82815436383728D79DA8F2EF7CAF2";
        String TypeuuIdLREAL = "65F1DDD44EDA9C0776BB16BBDFE36B1F";
        String TypeuuIdBOOL = "EC797BDD4541F500AD80A78F1F991834";
        String TypeuuIdTSize = "B33EE7B84825BBBA7F975BB735D4EB22";
        String TypeuuidPref = "8F544E934F31327C4EBC88930CB7AD32";
        String TypeNameRU = "E543C4CD4FF3878196D61C86A31A4CE0";

        String uuIdpos = "599604C246641AA6BA0E508C9ABF7EA4";
        String uuIdangle = "00FC1D804A2DE5A83DE85390D640AC3D";
        String uuIdenabled = "15B097034B9BBE7CCD78E0A466A64239";
        String uuIdmoveable = "6D62810D46DF8C4B27E62DBEBA63194B";
        String uuIdvisible = "EAC5288F431A370F7493EF98A2C613D5";
        String uuIdzValue = "29E9E6AD475BD9B49E6F40B0328374A7";
        String uuIdhint = "9001F21244C66932FB81B7B021B085BA";
        String uuIdsize = "1555B4384D69683C33FCB4A79B1A0932";

        String uuIdMouseLBPress = "299295AF47A6CCAC26009C964C5B47C5";
        String uuIdmouseLBRelease = "8DE6001343803CF639F332B16CC30079";
        String uuIdmouseRBPress = "5DE993F543E00267EF077D89D3D01B5B";
        String uuIdmouseRBRelease = "0AB0718D41E90B02F75425B41E39C1F0";
        String uuIdmouseEnter = "AA1D53154C9D3E9C25B0ADA056F82B5D";
        String uuIdmouseLeave = "C21BC0A24A8E157AF50BC59A1635CD7B";
        String uuIdmouseLBDblClick = "1BD263D2412FA33DA367C5B3480C9F0A";
        // Это постоянные переменные  для Графического компонентного типа (вроде как неизменяемые) лучше из фала брать
        String EventOutputsEvent[][] = {
            // тут вопрос с UUID
            {"mouseLBPress", "событие нажатия левой кнопки мыши на объекте", uuIdMouseLBPress},
            {"mouseLBRelease", "событие отпускания левой кнопки мыши на объекте", uuIdmouseLBRelease},
            {"mouseRBPress", "событие нажатия правой кнопки мыши на объекте", uuIdmouseRBPress},
            {"mouseRBRelease", "событие отпускания правой кнопки мыши на объекте", uuIdmouseRBRelease},
            {"mouseEnter", "событие входа указателя мыши в пределы объекта", uuIdmouseEnter},
            {"mouseLeave", "событие выхода указателя мыши за пределы объекта", uuIdmouseLeave},
            {"mouseLBDblClick", "событие двойного щелчка левой кнопки мыши на объекте", uuIdmouseLBDblClick}
        };
        String InputVarsDeclare[][] = {
            {"pos", "TPos", TypeuuIdTpos, "позиция объекта", "(x:=0,y:=0)", uuIdpos},
            {"angle", "LREAL", TypeuuIdLREAL, "угол поворота объекта", "0", uuIdangle},
            {"enabled", "BOOL", TypeuuIdBOOL, "доступность объекта", "TRUE", uuIdenabled},
            {"moveable", "BOOL", TypeuuIdBOOL, "подвижность объекта", "FALSE", uuIdmoveable},
            {"visible", "BOOL", TypeuuIdBOOL, "видимость объекта", "TRUE", uuIdvisible},
            {"zValue", "LREAL", TypeuuIdLREAL, "z-индекс объекта", "0", uuIdzValue},
            {"hint", "STRING", TypeuuIdString, "всплывающая подсказка", "&apos;&apos;", uuIdhint},
            {"size", "TSize", TypeuuIdTSize, "размер прямоугольника", "(width:=50,height:=50)", uuIdsize},
            {"PrefAb", "STRING", TypeuuIdString, "префикс абонента", "&apos;&apos;", TypeuuidPref},
            {"NameRU", "STRING", TypeuuIdString, "имя абонента", "&apos;&apos;", TypeNameRU}
        };
 
																																			   
																																																		   
														  
																	   
												
																	 
													
															   
												 
															   
											

        Element GCFBtype = doc.createElement("GraphicsCompositeFBType"); // Наша основа графического элемента
        GCFBtype.setAttribute("Name", GraphName); // тоже цикл с изменения доолжен быть так как по 64 элемента для аналогов
        GCFBtype.setAttribute("UUID", UUID.getUUID());
        Element InterfaceList = doc.createElement("InterfaceList");
        GCFBtype.appendChild(InterfaceList);
        Element EventOutputs = doc.createElement("EventOutputs");
        InterfaceList.appendChild(EventOutputs);
        Element InputVars = doc.createElement("InputVars");
        InterfaceList.appendChild(InputVars);
        Element FBNetwork = doc.createElement("FBNetwork");
        GCFBtype.appendChild(FBNetwork);

        //Element DataConnections = doc.createElement("DataConnections"); // это для связи переменных с входными сигналами(чуть позже реализовать)
        // элемент событий
        for (String field[] : EventOutputsEvent) {
            Element Event = doc.createElement("Event");
            Event.setAttribute("Name", field[0]);
            Event.setAttribute("Comment", field[1]);
            Event.setAttribute("UUID", field[2]);
            EventOutputs.appendChild(Event); // заносим в родителя
        }
        for (String field[] : InputVarsDeclare) {
            Element VarDeclaration = doc.createElement("VarDeclaration");
            VarDeclaration.setAttribute("Name", field[0]);
            VarDeclaration.setAttribute("Type", field[1]);
            VarDeclaration.setAttribute("TypeUUID", field[2]);
            VarDeclaration.setAttribute("Comment", field[3]);
            VarDeclaration.setAttribute("InitialValue", field[4]);
            VarDeclaration.setAttribute("UUID", field[5]);
            InputVars.appendChild(VarDeclaration); // заносим в родителя
        }
        InterfaceList.appendChild(EventOutputs); // так же но заносим уже в родителя их
        InterfaceList.appendChild(InputVars);
        doc.appendChild(GCFBtype);
        Node test = doc.getElementsByTagName("FBNetwork").item(0);
        //FBNetwork.appendChild(DataConnections); // переменные обязательно должно быть после
        return doc;
    }


    public void enumerationData(String patchF) {//значит вот это я вытолкнул
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            RemoveDTDFromSonataFile testStart = new RemoveDTDFromSonataFile(patchF);
            String documenWithoutDoctype;
            documenWithoutDoctype = testStart.methodRead(patchF); // Так читаем и получаем преобразованные данные,
            InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
            Document document_final = factory.newDocumentBuilder().parse(stream);
            // Получаем корневой элемент
            Node root = document_final.getDocumentElement();
            //передаем ноду на переборку
            stepThroughAll(root);
            // Тут запустим запись в файл
            writeDocument(document_final, patchF);
            //и возвращаем ему удаленный DOCTYPE
            //testStart.returnToFileDtd(patchF);
        } catch (SAXException ex) {
            System.out.println("Not file XML !!!");
        } catch (InterruptedException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerFactoryConfigurationError ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // --- вот тут мы все и получаем данные c ноды рекурсией ---
    private static void stepThroughAll(Node start) {
        System.out.println(start.getNodeName() + " = " + start.getNodeValue());
        if (start.getNodeType() == start.ELEMENT_NODE) {
            NamedNodeMap startAttr = start.getAttributes();
            for (int i = 0; i < startAttr.getLength(); i++) {
                Node attr = startAttr.item(i);//node str=strAttr.item(i);
                System.out.println(" Attribute: " + attr.getNodeName()
                        + " = " + attr.getNodeValue());
            }
        }
        for (Node child = start.getFirstChild();
                child != null;
                child = child.getNextSibling()) {
            stepThroughAll(child);
        }
    }

    boolean insertNode(Node addNode, String parentNode, String targetFile) {
        boolean request = false;
        return request;

    }

    // --- копирование или внедрени ноды в новый файл  --- 
    boolean insertNode(Node addNode, String targetFile) {
        boolean request = false;
        return request;

    }

    // --- Запипись в файл структурой XML ---
    void writeDocument(Document document, String patchWF) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        try {
            File file = new File(patchWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file));
            //вот это то что я написал это чушь,можно удалить
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
}
