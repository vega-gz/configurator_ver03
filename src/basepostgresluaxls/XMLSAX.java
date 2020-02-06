/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

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

/**
 *
 * @author nazarov
 */
// --- Доп файл для базовых типов
// может станет базовым вообще
public class XMLSAX {
    private final String AI_UUID = UUID.getUIID();
    private final String AI_HMI_UUID = UUID.getUIID();
    private final String AI_DRV_UUID = UUID.getUIID();
    private final String AI_PLC_UUID = UUID.getUIID();
    private final String T_GPA_AI_HMI_UUID = UUID.getUIID();
    // не понимаю зачем я такую делаю структуру и потом ее сложно передаю в XML для внесения 
    private Struct struct; // это новое класс для структуры


    String massParametrsAI_ [][] = {		 
         {"fault_common", "BOOL", "320393664796D414EEE541BB9E499327", "общая неисправность канала" },
         {"simulation", "BOOL", "1D2891814EB623B6A2177C8E52D96124", "режим симуляции включен" },
         {"repair", "BOOL", "19B2F17A4BB568846D6850B9C163F350", "в ремонте" },
         {"repair_time_less_10_percent", "BOOL", "35BBB4274C8D26DC25AEB69E278DF862", "время ремонта истекает" },
         {"up_scale", "BOOL", "7C74AD4E40598C67B4BDB2BA071C1569", "больше чем max_fault_sensor_Eu" },
         {"down_scale", "BOOL", "241E7A0B486837D643F6348AE7F39734", "меньше чем min_fault_sensor_Eu" },
         {"ROC", "BOOL", "3A8878DD4B8809AEFA6E22B04B11D0B8", "ROC превышен" },
         {"init_err", "BOOL", "6733064D4826D6D9EC2D9FAE4A9A9E9A", "ошибка инициализации" },
         {"res", "BOOL", "3722EB2F4972A3DD7B25EAAF4CE921B0", "резерв" },
         {"simulation_on", "BOOL", "77AE2CE043A5A720D4EADA9920F6C01C", "включить режим симуляции из HMI" },
         {"repair_on", "BOOL", "D038B8D34993B8F9AF40BD824FFAA3D7", "включить режим ремонта из HMI" },
         {"repair_extension", "BOOL", "9308A2364CAE685CBEF3E5B7A034EE69", "продлить время ремонта из HMI" },
         {"ps", "BOOL", "8D1BF9A744DED3DCE98FBCA44E039DAA", "для посветки желтым" },
         {"orr", "BOOL", "ABB24F6443C1E1D85D970488B4E77D27", "ограничение режима" },
         {"as", "BOOL", "929D2ED54388FF5D070139B202B37AB7", "для посветки красным" },
         {"ROC_enabled", "BOOL", "F8749F754994935836C41A87CE57DE35", "включение функции ROC" },
         {"res2", "BOOL", "205BA42B43E2213FC6B4EA87700659C7", "резерв" },
         {"res3", "BOOL", "3FC4AB9446CCEFEFCE7144A3AC5C977D", "резерв" },
         {"res4", "BOOL", "7DC565C6438B44421E8F65B96ADC1441", "резерв" },
         {"res5", "BOOL", "853846974520C1BE4165B6A0B38344FF", "резерв" },
         {"res6", "BOOL", "7D2AE31D4C0C92D9095DD7B58E1B61A9", "резерв" }
    };
    String massParametrsAI_HMI [][] = {		 
         {"PV", "REAL", UUID.getUIID(), "сигнал с датчика, преобразованный к физическим единицам" },
         {"Condition", AI_UUID, UUID.getUIID(), "0-11 коды отказов канала, 12-15 команды с мнемосхемы" },
         {"CurrentTimeOfRepair", "REAL", UUID.getUIID(), "текущее время ремонта канала" },
         {"Manual_Target", "REAL", UUID.getUIID(), "задание для выхода блока в ручном режиме" }
    };
    String massParametrsAI_PLC [][] = {		 
         {"Span", "REAL", UUID.getUIID(), "диапазон датчика в EU" },
         {"Offset", "REAL", UUID.getUIID(), "смещение датчика в EU" },
         {"Tf", "REAL", UUID.getUIID(), "постоянная фильтра &gt; 0.001" },
         {"min_ADC", "REAL", UUID.getUIID(), "минимальное значение в единицах АЦП для обрабатываемого канала" },
         {"max_ADC", "REAL", UUID.getUIID(), "максимальное значение в единицах АЦП для обрабатываемого канала" },
         {"min_fault_sensor_Eu", "REAL", UUID.getUIID(), "если сигнал с датчика Status.Input_sensor_eu меньше этого значения, то отказ канала" },
         {"max_fault_sensor_Eu", "REAL", UUID.getUIID(), "если сигнал с датчика Status.Input_sensor_eu больше этого значения, то отказ канала" },
         {"ROC", "REAL", UUID.getUIID(), "скорость изменения сигнала" },
         {"recovery_time", "REAL", UUID.getUIID(), "время восстановления канала после исчезновения неисправности" },
         {"repair_time", "REAL", UUID.getUIID(), "время через которое канал будет автоматически выведен из ручного режима" },
         {"ROC_max", "REAL", UUID.getUIID(), "максимальное значение ROC, если ROC_enable = 1" },
         {"ROC_min", "REAL", UUID.getUIID(), "минимальное значение ROC, если ROC_enable = 1" },
         {"ROC_time", "REAL", UUID.getUIID(), "Время определения скорости изменения сигнала  (сек)" },
         {"nAi", "INT", UUID.getUIID(), "Номер канала" }         
    };
    String globalpatchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\";
     // -- Тут созданике файла  Type_AI_.type (Будет без DOCTYPE) ---
    void createTypeAI_() throws ParserConfigurationException{
        String patchF = globalpatchF + "Type_AI_.type";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "AI_");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", AI_UUID);
        doc.appendChild(root);
            
        Element Fields = doc.createElement("Fields");
        //Fields.setAttribute("val", "3");
        root.appendChild(Fields);  
        
        for (String field[] : massParametrsAI_){
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", field[0]);
            Field.setAttribute("Type", field[1]);
            Field.setAttribute("UUID", field[2]);
            Field.setAttribute("Comment", field[3]);
            Fields.appendChild(Field);
        }
        
    try {
        writeDocument(doc, patchF);
    } catch (TransformerException ex) {
        Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
    }  
    }

    // --- Создание файла AI_HMI ---
    void createTypeAI_HMI() throws ParserConfigurationException{
        String patchF = globalpatchF + "Type_AI_HMI.type";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "AI_HMI");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", AI_HMI_UUID);
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        //Fields.setAttribute("val", "3");
        root.appendChild(Fields);  
        for (String field[] : massParametrsAI_HMI){
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", field[0]);
            Field.setAttribute("Type", field[1]);
            Field.setAttribute("UUID", field[2]);
            Field.setAttribute("Comment", field[3]);
            Fields.appendChild(Field);
        }
    try {
        writeDocument(doc, patchF);
    } catch (TransformerException ex) {
        Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    // --- Создание файла AI_PLC ---
    void createTypeAI_PLC() throws ParserConfigurationException{
        String patchF = globalpatchF + "Type_AI_PLC.type";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "AI_PLC");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", AI_PLC_UUID);
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        //Fields.setAttribute("val", "3");
        root.appendChild(Fields);  
        for (String field[] : massParametrsAI_PLC){
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", field[0]);
            Field.setAttribute("Type", field[1]);
            Field.setAttribute("UUID", field[2]);
            Field.setAttribute("Comment", field[3]);
            Fields.appendChild(Field);
        }
        try {
            writeDocument(doc, patchF);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // --- Создание файла Списка структур  T_GPA_---
    // Список из базы, имя структуры, уиды или типы, и новый uud этой структуры
    void createTypeT_GPA(ArrayList<String[]> arg, String name, String UUDparent, String UUDstruc) throws ParserConfigurationException{
        // не понимаю зачем я такую делаю структуру и потом ее сложно передаю в XML для внесения 
        struct = new Struct(name, T_GPA_AI_HMI_UUID , AI_HMI_UUID); // это новое класс для структуры
        //String patchF = globalpatchF + "T_GPA_AI_HMI.type";
        String patchF = globalpatchF + name + ".type";
        Iterator<String[]> iter_arg = arg.iterator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", name);
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", UUDstruc);
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        root.appendChild(Fields);          
        while (iter_arg.hasNext()) {  
          String[] field = iter_arg.next();
          Element Field = doc.createElement("Field");
          Field.setAttribute("Name", field[0]);
          Field.setAttribute("Type", UUDparent);
          Field.setAttribute("UUID", field[1]); // уид из базы
          Field.setAttribute("Comment", field[2]);
          Fields.appendChild(Field);          
          // тоже новое добавление данный в структуру
          struct.addData(field[0], UUDparent, field[1], field[2]);
        }  
        try {
            writeDocument(doc, patchF);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // получаем данные из базы и засовыем их в метод создания списка 
    void runBaseRuncreateTypeT() throws ParserConfigurationException{
        BasePostgresLuaXLS workbase = new BasePostgresLuaXLS();
        workbase.connectionToBase();
        ArrayList<String[]> dataFromDbGPA = workbase.selectDataGPAAI("ai1");
        // Тут передаем данные тестовый вызов
        createTypeT_GPA(dataFromDbGPA, "T_GPA_AI_HMI", AI_HMI_UUID, T_GPA_AI_HMI_UUID);
        try {
            // Заносим нашу структуру выше созданную в глобалку переменную
            sendStructToGlobV(struct);
        } catch (SAXException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DOMException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerFactoryConfigurationError ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathFactoryConfigurationException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // --- Передача собственной структуры в глобальные переменные Сонаты --- 
    void sendStructToGlobV (Struct structInt) throws ParserConfigurationException, SAXException, IOException, DOMException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, XPathFactoryConfigurationException, InterruptedException{
        XMLDomRW realise = new XMLDomRW(structInt); // пересылаем структуру для добавления  ее в глобальные переменные
        realise.runMethods(); // это надо вытащить в Главную панель
    }
    
    // --- Внесение структуры AI_ в Мнемосхемы iec_hmi ---
    void createTSensor_Aux_AI_Repair() throws ParserConfigurationException{
        String patchF = globalpatchF + "HMI.iec_hmi";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "AI_");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", UUID.getUIID());
        doc.appendChild(root);
            
        Element Fields = doc.createElement("Fields");
        //Fields.setAttribute("val", "3");
        root.appendChild(Fields);  
        
        for (String field[] : massParametrsAI_HMI){
            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", field[0]);
            Field.setAttribute("Type", field[1]);
            Field.setAttribute("UUID", field[2]);
            Field.setAttribute("Comment", field[3]);
            Fields.appendChild(Field);
        }
        
    try {
        writeDocument(doc, patchF);
    } catch (TransformerFactoryConfigurationError ex) {
        Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
    } catch (TransformerException ex) {
        Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
    }
      
    }
    
        // Добавляем сигналы в Мнемосхемы  не реализованно =(
    void addSignalesMnemo(ArrayList<String[]> lisSig, String nameListSign) throws SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException{
        String myVarU_0 = UUID.getUIID();
        String myVarU_0_0 = UUID.getUIID(); // УИД переменной для связки .но не уверен
        String myVarU_1 = UUID.getUIID();
        String uuIdTBaSence = "6BF99E384F16CE39204C00877BBA46AE"; // перебрать файлы в его поиска  // Это уид TBaSence
        
        String TypeuuIdString = "38FDDE3B442D86554C56C884065F87B7";
        String TypeuuIdTpos = "17C82815436383728D79DA8F2EF7CAF2";
        String TypeuuIdLREAL = "65F1DDD44EDA9C0776BB16BBDFE36B1F";
        String TypeuuIdBOOL = "EC797BDD4541F500AD80A78F1F991834";
        String TypeuuIdTSize = "B33EE7B84825BBBA7F975BB735D4EB22"; 
        
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
        
        String EventOutputsEvent [][] = {		 
          // тут вопрос с UUID
         {"mouseLBPress", "событие нажатия левой кнопки мыши на объекте", uuIdMouseLBPress},
         {"mouseLBRelease","событие отпускания левой кнопки мыши на объекте", uuIdmouseLBRelease},
         {"mouseRBPress","событие нажатия правой кнопки мыши на объекте", uuIdmouseRBPress},
         {"mouseRBRelease","событие отпускания правой кнопки мыши на объекте", uuIdmouseRBRelease},
         {"mouseEnter","событие входа указателя мыши в пределы объекта", uuIdmouseEnter},
         {"mouseLeave","событие выхода указателя мыши за пределы объекта", uuIdmouseLeave},
         {"mouseLBDblClick","событие двойного щелчка левой кнопки мыши на объекте", uuIdmouseLBDblClick}       
        };
        String InputVarsDeclare [][] = {		 
         {"pos", "TPos", TypeuuIdTpos, "позиция объекта", "(x:=0,y:=0)", uuIdpos},
         {"angle", "LREAL", TypeuuIdLREAL, "угол поворота объекта", "0", uuIdangle},
         {"enabled", "BOOL", TypeuuIdBOOL, "доступность объекта", "TRUE", uuIdenabled},
         {"moveable", "BOOL", TypeuuIdBOOL, "подвижность объекта", "FALSE", uuIdmoveable},
         {"visible", "BOOL", TypeuuIdBOOL, "видимость объекта", "TRUE", uuIdvisible},
         {"zValue", "LREAL", TypeuuIdLREAL, "z-индекс объекта", "0", uuIdzValue},
         {"hint", "STRING", TypeuuIdString, "всплывающая подсказка", "&apos;&apos;", uuIdhint},
         {"size", "TSize", TypeuuIdTSize, "размер прямоугольника", "(width:=50,height:=50)", uuIdsize}
        };
        Iterator<String[]> iter_arg = lisSig.iterator();
        String patchF = globalpatchF + "HMI.iec_hmi";
        DocumentBuilderFactory document = DocumentBuilderFactory.newInstance();
        DocumentBuilder doc = document.newDocumentBuilder();
        // это из тестового метода преобразовываем файл для чтения XML
        Test.RemoveDTDFromSonataFile testStart = new Test.RemoveDTDFromSonataFile(patchF);   
        String documenWithoutDoctype = testStart.methodRead(patchF);// Так читаем и получаем преобразованные данные, 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        // так преобразовываем строку в поток и скармливаем билдеру XML
        InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8)); 
        Document document_final = factory.newDocumentBuilder().parse(stream);
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();        
        // а вот тут надо посчитать сколько переменных
        XPathExpression expr = xpath.compile("SubAppType/FBLibrary");
        NodeList nodes = (NodeList) expr.evaluate(document_final, XPathConstants.NODESET);
        //  так как нода у нас одна то пишем только в 1 по этому for так работает
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            System.out.println(n.getNodeName());
            // Создаем элемент Графический компонент GraphicsCompositeFBType
            // и его структуру не в папке а просто в корне
            Element GCFBtype = document_final.createElement("GraphicsCompositeFBType");
            GCFBtype.setAttribute("Name", "TGraphicsCompositeTypeTest1"); // тоже цикл с изменения доолжен быть так как по 64 элемента
            GCFBtype.setAttribute("UUID", UUID.getUIID());
            n.appendChild(GCFBtype); // Так прикручаваем сам компонент
            Element InterfaceList = document_final.createElement("InterfaceList");
            GCFBtype.appendChild(InterfaceList);
            Element EventOutputs = document_final.createElement("EventOutputs");
            InterfaceList.appendChild(EventOutputs);
            Element InputVars = document_final.createElement("InputVars");
            InterfaceList.appendChild(InputVars);
            Element FBNetwork = document_final.createElement("FBNetwork");
            GCFBtype.appendChild(FBNetwork);
            Element DataConnections = document_final.createElement("DataConnections");
          
            // элемент событий
            for (String field[] : EventOutputsEvent){
                Element Event = document_final.createElement("Event");
                Event.setAttribute("Name", field[0]);
                Event.setAttribute("Comment", field[1]);
                Event.setAttribute("UUID", field[2]);
                EventOutputs.appendChild(Event); // заносим в родителя
            }
            for (String field[] : InputVarsDeclare){
                Element VarDeclaration = document_final.createElement("VarDeclaration");
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
            int Ycord = -704;
            int NumberSign = 0;
            int xPos = 2;
            int yPos = 0;
             while (iter_arg.hasNext()) { 
                String XYposition = "(x:=" + Integer.toString(xPos) + ",y:=" + Integer.toString(yPos) + ")" ; //"(x:=0,y:=0)"
                String uuidFB = UUID.getUIID();
                String[] field = iter_arg.next();
                Element FB = document_final.createElement("FB");
                //nameListSign
                String nameBAnpartClone = "BaseAnPar_Test_" + Integer.toString(NumberSign);
                FB.setAttribute("Name", nameBAnpartClone);
                //FB.setAttribute("Type", "TBaseAnPar");
                FB.setAttribute("Type", "TBaseSen");
                FB.setAttribute("UUID", uuidFB);
                FB.setAttribute("TypeUUID", uuIdTBaSence); // уид TBaSence
                FB.setAttribute("X", "-704.75"); 
                FB.setAttribute("Y", Integer.toString(Ycord)); // Меняем только Y
                Ycord = Ycord + 400;
                // Таких элементов 3
                Element VarValue0 = document_final.createElement("VarValue");
                VarValue0.setAttribute("Variable", "PrefStr");
                String VPrefStr = "'" + nameListSign +"." + "'"; // Только таким видом добился
                System.out.println(VPrefStr);
                VarValue0.setAttribute("Value", VPrefStr);
                VarValue0.setAttribute("Type", "STRING"); 
                VarValue0.setAttribute("TypeUUID", "38FDDE3B442D86554C56C884065F87B7"); 
                FB.appendChild(VarValue0);            
                Element VarValue1 = document_final.createElement("VarValue");
                VarValue1.setAttribute("Variable", "pos");
                VarValue1.setAttribute("Value", XYposition);
                VarValue1.setAttribute("Type", "TPos"); 
                VarValue1.setAttribute("TypeUUID", "17C82815436383728D79DA8F2EF7CAF2"); 
                FB.appendChild(VarValue1);
                Element VarValue2 = document_final.createElement("VarValue");
                VarValue2.setAttribute("Variable", "NameAlg");
                String VNameAlg = "\u0027" + field[0] + "\u0027";// Только так работает как добиться методом кода не понятно
                VarValue2.setAttribute("Value", VNameAlg); // Название сигнала
            
                VarValue2.setAttribute("Type", "TPos"); 
                VarValue2.setAttribute("TypeUUID", "17C82815436383728D79DA8F2EF7CAF2"); 
                FB.appendChild(VarValue2);
                FBNetwork.appendChild(FB);  
                // Так же переменные наши в Дату конектионс
                Element Connection = document_final.createElement("Connection");
                Connection.setAttribute("Source", "Agregat1_test");
                Connection.setAttribute("Destination", nameBAnpartClone +"."+"PrefAb");
                Connection.setAttribute("SourceUUID", myVarU_0);
                Connection.setAttribute("DestinationUUID", uuidFB +"." + "7DF53A3B47B1075B9D3AE78253FC271B");// АЙДИ  TBaseAnPar может поменяться
                DataConnections.appendChild(Connection);
                ++NumberSign;
                yPos = yPos + 20;
            }
            FBNetwork.appendChild(DataConnections); // переменные обязательно должно быть после
        }
    // Тут запустим запись в файл
    writeDocument(document_final, patchF);
    //и возвращаем ему удаленный DOCTYPE
    testStart.returnToFileDtd(patchF);
    }
    
    public void enumerationData(String patchF) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            Test.RemoveDTDFromSonataFile testStart = new Test.RemoveDTDFromSonataFile(patchF);   
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
        }    
        catch (InterruptedException ex) {
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
    private static void stepThroughAll (Node start){
        System.out.println(start.getNodeName()+" = "+start.getNodeValue());
        if (start.getNodeType() == start.ELEMENT_NODE){
            NamedNodeMap startAttr = start.getAttributes();
            for (int i = 0; i < startAttr.getLength(); i++) {
                Node attr = startAttr.item(i);
                System.out.println(" Attribute: "+ attr.getNodeName()
                +" = "+attr.getNodeValue());
            }
        }
        for (Node child = start.getFirstChild();
            child != null;
            child = child.getNextSibling())
        {
        stepThroughAll(child);
        }
  }
    // --- Запипись в файл структурой XML ---
     void writeDocument(Document document, String patchWF) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        try {
            //тут в одну строку работает тоже
            /*
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("src\\WorkXML\\test.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
            */
            File file = new File(patchWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file));
            
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
    
}
