/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nazarov
 */
// --- Доп файл для базовых типов
// может станет базовым вообще
public class XMLSAX {
    final String AI_UUID = UUID.getUIID();
    final String AI_HMI_UUID = UUID.getUIID();
    final String AI_DRV_UUID = UUID.getUIID();
    final String AI_PLC_UUID = UUID.getUIID();
    final String T_GPA_AI_HMI_UUID = UUID.getUIID();


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
    void createTypeT_GPA_AI_HMI(ArrayList<String[]> arg, String name, String UUDparent, String UUDstruc) throws ParserConfigurationException{
        // не понимаю зачем я такую делаю структуру и потом ее сложно передаю в XML для внесения 
        Struct structT_GPA_AI_HMI = new Struct(name, T_GPA_AI_HMI_UUID , AI_HMI_UUID); // это новое класс для структуры
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
          structT_GPA_AI_HMI.addData(field[0], UUDparent, field[1], field[2]);
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
        createTypeT_GPA_AI_HMI(dataFromDbGPA, "T_GPA_AI_HMI", AI_HMI_UUID, T_GPA_AI_HMI_UUID);
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
