  package XMLTools;

import XMLTools.RemoveDTDFromSonataFile;
import configurator.StructSelectData;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import ReadWriteExcel.RWExcel;

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
    private StructSelectData struct; // это новое класс для структуры
    String massParametrsAI_[][] = {};
    Document document; // Глобальный документ с которым работаем
    String patchWF = "";
    DataBase workbase = DataBase.getInstance();
        RemoveDTDFromSonataFile fixXML; // объект реализации обхода неверно сформированного файла

    
     // --- Взять корневую ноду  из документа ---
    public Node readDocument(String patchF) {
        patchWF = patchF;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        document = null;
        List<String> lines; // Лист с прочитанным файлом
        boolean findErr = false; // если попали в ексепшен и дробим нашим парсером
        try {
            document = factory.newDocumentBuilder().parse(patchF);
        } catch (IOException ex) {
            System.out.println("Error read XML file" + Paths.get(patchF)); // выброс ошибок по файлу
            findErr = true;
        } catch (SAXException ex) {
            System.out.println("Over error, all ride step to two !!!!"); // вот тут уже вызываем метод удаления DOCTYPE и тд если будет
            findErr = true;
        } catch (ParserConfigurationException ex) {
            System.out.println("Standart parser down :(");
            findErr = true; // так же как выше пробуем убирать DOCTYPE
        }
       
        Node n = null; // это для корневой ноды но может и не надо
        if (findErr) { // Запускаем режим преобразования файлов только после Exception
            try {
                fixXML = new RemoveDTDFromSonataFile(patchF);
                String documenWithoutDoctype = fixXML.methodRead(patchF); // Так читаем и получаем преобразованные данные,
                InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
                document = factory.newDocumentBuilder().parse(stream);
                n = document.getDocumentElement(); // Получаем корневой элемент

            } catch (SAXException | InterruptedException | TransformerFactoryConfigurationError | IOException | ParserConfigurationException ex) {
                System.out.println("Not file XML or bed remove data in file" + patchF + " !");
            }

        } else {
            n = document.getDocumentElement(); // Получаем корневой элемент
        }
        return n;
    }

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
            Fields.appendChild(Field);//if(){}public static void main(String []args){}

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
        DataBase workbase = DataBase.getInstance();

        ArrayList<String[]> dataFromGPA_Sig = workbase.getSelectData(nameTable);//
        createTypeAllSignal(dataFromGPA_Sig, nameSignal, UUID_Type, UUID_Parent//AI_UUID нужно изучить,мне кажется это дерьмо не должно быть рандомным
                , file);

    }

    public void runBasecreateTypeAll(String nameTable, String nameSignal, String file, String Type) throws ParserConfigurationException {
        DataBase workbase = DataBase.getInstance();

        ArrayList<String[]> dataFromGPA_Sig = workbase.getSelectData(nameTable);
        createTypeDRV(dataFromGPA_Sig, nameSignal, file, Type);
    }

    // --- Передача собственной структуры в глобальные переменные Сонаты --- 
    public void sendStructToGlobV(StructSelectData structInt) throws ParserConfigurationException, SAXException, IOException, DOMException, XPathExpressionException,
            TransformerFactoryConfigurationError, TransformerException, XPathFactoryConfigurationException, InterruptedException {
//        DomRW realise = new DomRW(structInt); // пересылаем структуру для добавления  ее в глобальные переменные
  //      realise.runMethods(); // это надо вытащить в Главную панель
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
        int Number = 0, Num = 0, Counter = 1, NumElem = 0, Counter1 = 1;
        int xPosModule = 2;
        int size = 0;

        //  так как нода у нас одна то пишем только в 1 по этому for так работает либо пишем в первую.
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            Document GraphicsCompositeType = createTGraphicsCompositeType(GName); // так забрали документ из метода
            Element FBNetwork = (Element) GraphicsCompositeType.getElementsByTagName("FBNetwork").item(0); // вытягиваем ноду элемента FBNetwork из Документа который получили выше

            Node rootGP = GraphicsCompositeType.getFirstChild(); // Начальная нода файла (что будем импортировать)

            // настройка сигналов помещаемых в Графический компонент
            int Ycord = 0; // Переменная смещения по Y в виде компонентов
            int NumberSign = -1;

            int xPos = 50;
            int yPos = 0;
            int sumColumn = 1; // Количество столбцов
            int offset = 850;
            String visibleAI = "", disableVisible = "";// Переменная на смещение по иксам в нарисованном элементе

            Element DataConnections = GraphicsCompositeType.createElement("DataConnections"); // это для связи переменных с входными сигналами(чуть позже реализовать)

            //-----ЭТО СОЗДАНИЕ T_BASE_(ОДНОГО БЛОКА)И CONNECT//
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

                Element VarValue8 = GraphicsCompositeType.createElement("VarValue");
                VarValue8.setAttribute("Variable", "Num");
                VarValue8.setAttribute("Value", "\u0027" + (NumElem++) + "\u0027");
                VarValue8.setAttribute("Type", "STRING");
                VarValue8.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                FB.appendChild(VarValue8);//

                switch (TypeName) {
                    case "T_BaseAnIn": {
                        Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                        VarValue7.setAttribute("Variable", "hint");
                        VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + ","
                                + field[4] + "," + field[5] + "," + field[6] + "," + field[7] + "," + field[8] + ","
                                + field[9] + "," + field[10] + "," + field[11] + "," + field[12] + "," + field[13] + ","
                                + field[14] + "," + field[15] + "," + "\u0027");
                        VarValue7.setAttribute("Type", "STRING");
                        VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                        FB.appendChild(VarValue7);
                        break;
                    }
                    case "T_BaseAnOut": {
                        Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                        VarValue7.setAttribute("Variable", "hint");
                        VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + ","
                                + field[4] + "," + field[5] + "," + field[6] + "," + field[7] + "," + field[8] + ","
                                + field[9] + "," + field[10] + "\u0027");
                        VarValue7.setAttribute("Type", "STRING");
                        VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                        FB.appendChild(VarValue7);
                        break;
                    }
                    case "T_BaseDO": {
                        Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                        VarValue7.setAttribute("Variable", "hint");
                        VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + ","
                                + field[6] + "," + field[7] + "," + field[8] + "," + "\u0027");
                        VarValue7.setAttribute("Type", "STRING");
                        VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                        FB.appendChild(VarValue7);
                        break;
                    }
                    case "T_BaseDI": {
                        Element VarValue7 = GraphicsCompositeType.createElement("VarValue");
                        VarValue7.setAttribute("Variable", "hint");
                        VarValue7.setAttribute("Value", "\u0027" + field[0] + "," + field[2] + "," + field[3] + "," + field[4] + "," + field[5] + ","
                                + field[6] + "," + field[7] + "," + field[8] + "," + "\u0027");
                        VarValue7.setAttribute("Type", "STRING");
                        VarValue7.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
                        FB.appendChild(VarValue7);
                        break;
                    }
                }

                //-----СОЗДАНИЕ КОНКРЕТНОГО ЭЛЕМЕНТА КОННЕКШНА----//
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
                    xPos = 50;
                    sumColumn = 2;
                    yPos = yPos + 24;

                }
                if (NumberSign >= 63) {
                    size = (Counter1 / 2) * 24;
                    createModuleBox(xPosModule, yPos, GraphicsCompositeType, NumElem, Ycord, FBNetwork, size);
                    NumElem = 0;
                    Num = 0;
                    NumberSign = -1;
                    FBNetwork.appendChild(DataConnections);
                    Node importNode = document_final.importNode(rootGP, true); // Вытягиваем элемент и импортируем Импорт обязателен
                    n.appendChild(importNode); // Добавляем коренную ноду в Мнемосхему уже после всех преобразований

                    Ycord = 0; // Переменная смещения по Y в виде компонентов
                    xPos = 50;
                    yPos = 0;
                    offset = 850; // Переменная на смещение по иксам в нарисованном элементе

                    GraphicsCompositeType = createTGraphicsCompositeType(GName + (Counter++)); // так забрали документ из метода
                    FBNetwork = (Element) GraphicsCompositeType.getElementsByTagName("FBNetwork").item(0); // вытягиваем ноду элемента FBNetwork из Документа который получили выше
                    rootGP = GraphicsCompositeType.getFirstChild(); // Начальная нода файла (что будем импортировать)
                    DataConnections = GraphicsCompositeType.createElement("DataConnections"); // это для связи переменных с входными сигналами(чуть позже реализовать)
                    size = 0;
                    Counter1 = 0;

                }

                ++NumberSign;
                --sumColumn;
                Counter1++;

            }
            size = Counter1 * 24;

            createModuleBox(xPosModule, yPos, GraphicsCompositeType, NumElem, Ycord, FBNetwork, size);
            FBNetwork.appendChild(DataConnections);
            Node importNode = document_final.importNode(rootGP, true); // Вытягиваем элемент и импортируем Импорт обязателен
            n.appendChild(importNode); // Добавляем коренную ноду в Мнемосхему уже после всех преобразований
            Counter1 = 0;

        }
        // Тут запустим запись в файл
        writeDocument(document_final, patchF);
        //и возвращаем ему удаленный DOCTYPE
        testStart.returnToFileDtd(patchF);
    }

    public void createModuleBox(int xPos, int yPos, Document GraphicsCompositeType, int NumElem, int Ycord, Element FBNetwork, int size) {//xPos4=yPos4
        int x = 1, y = 0;

        for (int i = 0; i <= 1; i++) {
            String XYposition = "(x:=" + Integer.toString(xPos) + ",y:=" + Integer.toString(yPos) + ")"; //"(x:=0,y:=0)" это позиция графики первой вкладки
            String uuidFB = getUIID();
            Element FB = GraphicsCompositeType.createElement("FB");
            String nameBAnpartClone = "TabSignal_" + (NumElem++);//
            FB.setAttribute("Name", nameBAnpartClone);
            FB.setAttribute("Type", "T_TabSignal");
            FB.setAttribute("UUID", uuidFB);
            FB.setAttribute("TypeUUID", "1C463F3841079B59C05076AB4B981F77");
            FB.setAttribute("X", "598");
            FB.setAttribute("Y", Integer.toString(Ycord)); // Меняем только Y
            // Ycord = Ycord + 450;

            Element VarValue0 = GraphicsCompositeType.createElement("VarValue");
            VarValue0.setAttribute("Variable", "zValue");
            VarValue0.setAttribute("Value", "-1");
            VarValue0.setAttribute("Type", "LREAL");
            VarValue0.setAttribute("TypeUUID", "65F1DDD44EDA9C0776BB16BBDFE36B1F");
            FB.appendChild(VarValue0);

            Element VarValue1 = GraphicsCompositeType.createElement("VarValue");
            VarValue1.setAttribute("Variable", "size");
            VarValue1.setAttribute("Value", "(width:=48,height:=" + size + ")");
            VarValue1.setAttribute("Type", "TSize");
            VarValue1.setAttribute("TypeUUID", "B33EE7B84825BBBA7F975BB735D4EB22");
            FB.appendChild(VarValue1);

            Element VarValue2 = GraphicsCompositeType.createElement("VarValue");
            VarValue2.setAttribute("Variable", "angle");
            VarValue2.setAttribute("Value", "0");
            VarValue2.setAttribute("Type", "LREAL");
            VarValue2.setAttribute("TypeUUID", "65F1DDD44EDA9C0776BB16BBDFE36B1F");
            FB.appendChild(VarValue2);

            Element VarValue3 = GraphicsCompositeType.createElement("VarValue");
            VarValue3.setAttribute("Variable", "pos");
            VarValue3.setAttribute("Value", "(x:=" + x + "," + "y:=" + y + ")");
            VarValue3.setAttribute("Type", "TPos");
            VarValue3.setAttribute("TypeUUID", "17C82815436383728D79DA8F2EF7CAF2");
            FB.appendChild(VarValue3);

            Element VarValue4 = GraphicsCompositeType.createElement("VarValue");
            VarValue4.setAttribute("Variable", "Modulus");
            VarValue4.setAttribute("Value", "\u0027" + "A.1.1" + "\u0027");
            VarValue4.setAttribute("Type", "STRING");
            VarValue4.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7");
            FB.appendChild(VarValue4);
            FBNetwork.appendChild(FB);
            x = 852;

        }

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
            System.out.println("Not file XML!!!");
        } catch (InterruptedException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerFactoryConfigurationError ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            System.out.println("Ошибка парсинга файлов");
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
    
    // --- Метод чтения и подключение к базе посредством конфига ---
    public void setConnectBaseConfig(String patchFile) {
        File f = new File(patchFile);
        String pass = null;
        String user = null;
        String url = null;
        String base = null;
        if (f.exists()) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                org.w3c.dom.Document doc = db.parse(f);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("config");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (Node.ELEMENT_NODE == node.getNodeType()) {
                        Element element = (org.w3c.dom.Element) node;
                        pass = element.getElementsByTagName("PASS").item(0).getTextContent();
                        user = element.getElementsByTagName("USER").item(0).getTextContent();
                        url = element.getElementsByTagName("URL").item(0).getTextContent();
                        base = element.getElementsByTagName("BASE").item(0).getTextContent();
                    }
                    DataBase.getInstance().connectionToBase(url, base, user, pass); // Вызов запроса к базе подключения
                }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                errorExecuter("Проверьте существование или структуру " + patchFile);
            }
        }
    }
    
     // --- Найти первую ноду и возвернуть ее ---
    public Node returnFirstFinedNode(Node n, String s) {
        Node finding = null;
        if (n != null) {
            System.out.println("NodeName" + n.getNodeName() + " NameType" + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                if (n.getNodeName().equals(s)) {
                    System.out.println("Find Node " + n.getNodeName());
                    finding = n;
                    return finding;
                }
            }
            if (finding == null) { // если не нашли
                for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                    finding = returnFirstFinedNode(child, s);
                    if (finding != null) {
                        break;
                    }
                }

            }
        }
        return finding;
    }
    
    // --- сформировать даные из конфигугации XML ---
    public void ReadConfig(Node n, String pathExel) {  // pathExel Временно так как мозгов не хватило ночью.                
        RWExcel readExel = new RWExcel(pathExel);
        ArrayList<String> it_list_sheet = readExel.get_list_sheet(pathExel); //забираем список листов в файле и строим итератор из них

        //ArrayList<ArrayList> returnData = new ArrayList<>(); // что возвернем(если возвернем)
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        ArrayList<ArrayList> dataconfig = new ArrayList<>();
        NodeList nodesExcel = null;

        String nameTB = ""; // имя таблицы для базы
        String nameSheetExel = ""; // название листа Exel
        ArrayList<String> columnExcel = new ArrayList<>(); // Колонки из Excell
        ArrayList<String> columnBase = new ArrayList<>(); //названия таблиц для Базы

        NodeList signalList = n.getChildNodes();
        if (n != null) { // если не пустая нода
            for (int i = 0; i < signalList.getLength(); i++) {
                Node firsNode = signalList.item(i);
                if (firsNode.getNodeType() == firsNode.ELEMENT_NODE) {
                    nameTB = firsNode.getNodeName();
                    System.out.println("NameTableBase " + nameTB);
                    NamedNodeMap atrsig = firsNode.getAttributes(); // вот тут ньюанс смотреть атрибуты а не ноды наследника
                    for (int atr = 0; atr < atrsig.getLength(); atr++) { // пробегаем по атребутам
                        Node sigSheet = atrsig.item(atr);
                        if (sigSheet.getNodeName().equalsIgnoreCase("excelSheetName")) { // проверка что идентификатор атрибута excelSheetName
                            System.out.println("NameExelSheet " + sigSheet.getNodeValue());
                            nameSheetExel = sigSheet.getNodeValue();
                        }
                    }
                    try {
                        XPathExpression xPathExpression = xpath.compile("EXEL"); // путь до 
                        nodesExcel = (NodeList) xPathExpression.evaluate(firsNode, XPathConstants.NODESET);
                    } catch (XPathExpressionException ex) {
                        Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (int j = 0; j < nodesExcel.getLength(); j++) { // перборка Exel ноды хоть она и одна
                        Node element = nodesExcel.item(j);
                        //stepThroughAll(element);
                        NodeList listNodeEl = element.getChildNodes();
                        for (int j1 = 0; j1 < listNodeEl.getLength(); j1++) {
                            Node start = listNodeEl.item(j1);
                            if (start.getNodeType() == start.ELEMENT_NODE) { //  проверка хз чего The node is an Element.
                                System.out.println("ColumnExel " + start.getNodeName());
                                columnExcel.add(start.getNodeName()); // Имя забора колонки из Excel
                                NamedNodeMap startAttr = start.getAttributes(); // Получение имена и атрибутов каждого элемента
                                for (int i1 = 0; i1 < startAttr.getLength(); i1++) { // Переборка значений ноды
                                    Node attr = startAttr.item(i1);
                                    String Attribute = attr.getNodeName(); // Название атрибута
                                    String Value = attr.getNodeValue(); // значение атрибута
                                    if (Attribute.equals("nameColumnPos")) {//проверка что этот атрибут nameColumnPos
                                        System.out.println("NameColumnToBase: " + Value);
                                        columnBase.add(Value);
                                    }
                                }
                            }
                        }
                    }
                    if (nameTB.equals("") | nameSheetExel.equals("") | columnExcel.isEmpty() | columnBase.isEmpty()) { // если что то не совпало или возвернулось пустое то нечего не делаем
                        System.out.println("What is way wrong!"); // дебаг
                    } else {
                        // тут реализация сигнала в базе данных
                        try {
                            for (String s : it_list_sheet) { // пробегаем по Листам файла и сравниваем есть ли такое в конфиг-файле
                                if (nameSheetExel.equals(s)) {
                                    workbase.createTable(nameTB, columnBase); // Создание таблицы
                                    ArrayList<String[]> sheet_fromsheet_from = new ArrayList<>(readExel.getDataCell(nameSheetExel, columnExcel));
                                    for (String[] massS : sheet_fromsheet_from) {
                                        workbase.insertRows(nameTB, massS, columnBase); //Вносим данные в базу
                                    }
                                }
                            }
                            // все обнуляем для следующего сигнала
                            nameTB = "";
                            nameSheetExel = "";
                            columnExcel.clear();
                            columnBase.clear();
                        } catch (SQLException ex) {
                            System.out.println("Error read file or connect to base " + ex);
                        }
                    }
                }
            }
        }
    }
         // --- обработчик ошибок показывает что было не так сделанно(можно выводить логи в фал) --
    void errorExecuter(String s) {
        JOptionPane.showMessageDialog(null, "Сообщения о ошибке " + s);
    }
    
}


// класс удаления неверного <!DOCTYPE SubAppType v. 1.3 >
class RemoveDTDFromSonataFile {

    static String patchD = "";

    public RemoveDTDFromSonataFile(String patchD) {
        this.patchD = patchD;
    }

    static String doctype = "";
    static int positionDTD;
    static boolean positionDTDFind = false; // триггер для поиска DTD что бы не гонять цикл

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Exception {
        // --- Ниже реализация этого функционала ----    
        RemoveDTDFromSonataFile testStart = new RemoveDTDFromSonataFile("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\ControlProgram.int");
        String patchF = patchD;
        String documenWithoutDoctype = testStart.methodRead(patchF);// Так читаем и получаем преобразованные данные, 
        // так парсим что получили
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        // так преобразовываем строку в поток и скармливаем билдеру XML
        InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
        // или так
        //InputStream in = org.apache.commons.io.IOUtils.toInputStream(source, "UTF-8");
        Document doc = factory.newDocumentBuilder().parse(stream);
        // Так передаем на переработку
        viewAllXML(doc);
        writeDocument(doc, patchF);
        testStart.returnToFileDtd(patchF);
        //System.out.println(testStart.methodRead(patchF)); 
        //testStart.writeWithoutDTD(patchF, methodRead(patchF)); // Так записываем без DTD
    }

    // --- Запись книги в файл ----
    static void writeDocument(Document document, String patchF) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        try {
            File file = new File(patchF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file));

        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }

    static void viewAllXML(Document document) {
        // Получаем корневой элемент
        Node root = document.getDocumentElement();
        System.out.println("List of books:");
        System.out.println();
        // Просматриваем все подэлементы корневого - т.е. книги
        NodeList books = root.getChildNodes();
        for (int i = 0; i < books.getLength(); i++) {
            Node book = books.item(i);
            // Если нода не текст, то это книга - заходим внутрь
            if (book.getNodeType() != Node.TEXT_NODE) {
                NodeList bookProps = book.getChildNodes();
                System.out.println(books.getLength());
                for (int j = 0; j < bookProps.getLength(); j++) {
                    Node bookProp = bookProps.item(j);
                    // Если нода не текст, то это один из параметров книги - печатаем
                    if (bookProp.getNodeType() != Node.TEXT_NODE) {
                        // System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
                        for (int i1 = 0; i1 < bookProp.getChildNodes().getLength(); i1++) {
                            System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(i1).getTextContent());
                        }
                    }
                }
                System.out.println("===========>>>>");
            }
        }
    }

    // Метод Парсера строк поиск Доктипа
    static String paternDOCTYPE(String st1, int pos) {
        Pattern pattern2 = Pattern.compile("^(<!DOCTYPE.*)$"); // Патерн нашего ДокТипа
        Matcher matcher2 = pattern2.matcher(st1);
        if (matcher2.matches()) {
            positionDTD = pos; // Так же вносим позицию от куда это взяли
            doctype = matcher2.group(1); // в глобальные переменную что собираемся затереть
            //System.out.println(doctype);
            positionDTDFind = true; // Тригер сработал
            return "";  // Возвращаем пустую строку если нашли DOCTYPE 
        } else {
            return st1;
        }

    }

    //метод чтения файла
    static public String methodRead(String path) throws InterruptedException {
        String result_data = "";
        StringBuffer sb = new StringBuffer();
        long start_time = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            int pos_str = 0;
            start_time = System.nanoTime();
            while ((str = in.readLine()) != null) {
                //System.out.println(str);
                if (positionDTDFind == false) {
                    sb.append(paternDOCTYPE(str, pos_str) + "\n"); // Передаем строки в парсер обработчик
                    //result_data += paternDOCTYPE(str, pos_str) + "\n";
                } else {
                    sb.append(str).append("\n");
                    //result_data += str + "\n";
                }
                // sb.append(tmpStr1);
                ++pos_str;
            }
            in.close();
        } catch (IOException e) {
        }
        result_data = sb.toString();
        // тут сразу и записываем для тестов видимо было
        long end_time = System.nanoTime();
        System.out.println("time " + (end_time - start_time));
        return result_data; // возвращаем преобразованную строку 
    }

    //метод записи файла и изначальных данных и DTD
    static public void methodWrite(String path, String data) throws InterruptedException {
        try {

            File resultName = new File(path + "_newfile"); //Файл с новой записью
            File tmpName = new File(path + "_original"); // это просто Имя
            File realName = new File(path); // Оригинальное имя

            BufferedWriter writer = new BufferedWriter(new FileWriter(resultName, true));
            int tmpPos = 0;
            String resultTofile = "";
            for (String tmpStr : data.split("\n")) { // бъем строку что бы записать в нужное место что вырезали выше
                //writer.append(tmpStr);

                if (tmpPos == positionDTD) { // если позиция верная то внсим обратно доктипДТД
                    resultTofile += doctype + "\n";
                } else {
                    resultTofile += tmpStr + "\n"; // таким способоб убираем пустую и вставляем нужную
                }
                ++tmpPos;
            }
            //writer.write(data); // это походу переписать
            writer.write(resultTofile); // Добавляем вновь сформированную строку в файл
            writer.close();
            // Удаляем  и переименовываем в удаленный файл
            if (tmpName.exists()) { // проверяем на существование бекапного файла
                if (tmpName.delete()) { // если он есть удаляем его
                    System.out.println(tmpName.getName() + " is deleted!");
                    realName.renameTo(tmpName);
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } else {
                realName.renameTo(tmpName);
            }
            resultName.renameTo(realName);
            //tmpName.renameTo(resultName);
        } catch (IOException e) {
        }
    }

    //метод записи файла без DTD
    static public void writeWithoutDTD(String path, String data) throws InterruptedException {
        try {

            File resultName = new File(path + "_newfile"); //Файл с новой записью
            File tmpName = new File(path + "_original"); // это просто Имя
            File realName = new File(path); // Оригинальное имя

            BufferedWriter writer = new BufferedWriter(new FileWriter(resultName, true));
            writer.write(data); // это походу переписать
            writer.close();

            // Удаляем  и переименовываем в удаленный файл
            if (tmpName.exists()) { // проверяем на существование бекапного файла
                if (tmpName.delete()) { // если он есть удаляем его
                    System.out.println(tmpName.getName() + " is deleted!");
                    realName.renameTo(tmpName);
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } else {
                realName.renameTo(tmpName);
            }
            resultName.renameTo(realName);
            //tmpName.renameTo(resultName);
        } catch (IOException e) {
        }
    }

    //метод записи файла и изначальных данных и DTD(не доделал просто чуть подчищен выше метод)
    static public void writeOnlyDtd(String path, String data) throws InterruptedException {
        try {

            File resultName = new File(path + "_newfile"); //Файл с новой записью
            File tmpName = new File(path + "_original"); // это просто Имя
            File realName = new File(path); // Оригинальное имя

            BufferedWriter writer = new BufferedWriter(new FileWriter(resultName, true));
            int tmpPos = 0;
            String resultTofile = "";
            for (String tmpStr : data.split("\n")) { // бъем строку что бы записать в нужное место что вырезали выше
                if (tmpPos == positionDTD) { // если позиция верная то внсим обратно доктипДТД
                    resultTofile += doctype + "\n";
                } else {
                    resultTofile += tmpStr + "\n"; // таким способоб убираем пустую и вставляем нужную
                }
                ++tmpPos;
            }
            writer.write(resultTofile); // Добавляем вновь сформированную строку в файл
            writer.close();
            // Удаляем  и переименовываем в удаленный файл
            if (tmpName.exists()) { // проверяем на существование бекапного файла
                if (tmpName.delete()) { // если он есть удаляем его
                    System.out.println(tmpName.getName() + " is deleted!");
                    realName.renameTo(tmpName);
                } else {
                    System.out.println("Delete operation is failed.");
                }
            } else {
                realName.renameTo(tmpName);
            }
            resultName.renameTo(realName);
            //tmpName.renameTo(resultName);

        } catch (IOException e) {
        }
    }

    //метод записи файла DOCTYPE который мы забрали
    public void returnToFileDtd(String path) throws InterruptedException {
        try {
            BufferedReader read = new BufferedReader(new FileReader(path));
            String str;
            int pos_str = 0;
            String resultTofile = "";
            while ((str = read.readLine()) != null) {
                if (pos_str == positionDTD) { // если позиция верная то внсим обратно доктипДТД
                    resultTofile += doctype + "\n";
                    resultTofile += str + "\n";
                } else {
                    resultTofile += str + "\n"; // таким способоб убираем пустую и вставляем нужную
                }
                ++pos_str;
            }
            read.close();
            // прочитали сформировали и теперь его перепишем строкой
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path), false)); // true если надо добавлять в конец файла
            writer.write(resultTofile); // Добавляем вновь сформированную строку в файл
            writer.close();
        } catch (IOException e) {
        }
    }

}