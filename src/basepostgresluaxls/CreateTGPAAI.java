/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nazarov
 */
// --- Реализация файлами а не структорой 
public class CreateTGPAAI {
  private  String name_str = "";
  private  String Type_UUIDstr = "";
  private  String patchPrg = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\";
  private  String uuidAI_ =""; //0-11 коды отказов канала, 12-15 команды с мнемосхемы
  private  String uuidAI_PLC ="";
  private  String uuidAI_HMI ="";
  private  final String AI_HMI = "AI_HMI";
  private  ReadWriteExel crivoiUID = new ReadWriteExel();// временнно для формирования UUID
  
  // методы возврата  UUID сформированных листов
  String getType_UUIDstr(){return Type_UUIDstr;}
  String getuuidAI_(){return uuidAI_;}
  String getuuidAI_PLC(){return uuidAI_PLC;}
  String getuuidAI_HMI(){return uuidAI_HMI;}

    
    
        void T_GPA_AI_PLC(ArrayList<String[]> arg, String name_str) throws IOException {
             this.name_str = name_str;
           // ReadWriteExel crivoiUID = new ReadWriteExel();// временнно для формирования UUID
            Type_UUIDstr = crivoiUID.getUIID();
            Iterator<String[]> iter_arg = arg.iterator();
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\"" + name_str + "\" Kind=\"Struct\" UUID=\"" + Type_UUIDstr + "\"> \n"
                   + "<Fields>\n";

            while (iter_arg.hasNext()) {  
                String[] tmpS = iter_arg.next();
                data += "<Field Name=\"" +tmpS[0] +"\" Type=\"" +uuidAI_PLC+ "\" UUID=\"" +tmpS[1] +"\" Comment=\"" +tmpS[2] +"\" />\n";
            }
            data += "</Fields>\n"
                   + "</Type>";
            FileOutputStream out = new FileOutputStream(patchPrg + "Type_GPA_AI_PLC_from_java.type");
            //out.write(data.getBytes("Cp1251"));
            out.write(data.getBytes("UTF8"));
            out.close();  
        }
         
        void T_GPA_AI_HMI(ArrayList<String[]> arg, String name_str) throws IOException, ParserConfigurationException, SAXException, DOMException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, XPathFactoryConfigurationException, InterruptedException {
             this.name_str = name_str;
           // ReadWriteExel crivoiUID = new ReadWriteExel();// временнно для формирования UUID
            Type_UUIDstr = crivoiUID.getUIID();
            
            Struct structT_GPA_AI_HMI = new Struct(name_str, Type_UUIDstr , AI_HMI); // это новое класс для структуры
            
            Iterator<String[]> iter_arg = arg.iterator();
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\"" + name_str + "\" Kind=\"Struct\" UUID=\"" + Type_UUIDstr + "\"> \n"
                   + "<Fields>\n";

        while (iter_arg.hasNext()) {  
          String[] tmpS = iter_arg.next();
          data += "<Field Name=\"" +tmpS[0] +"\" Type=\"" +uuidAI_HMI+ "\" UUID=\"" +tmpS[1] +"\" Comment=\"" +tmpS[2] +"\" />\n";
          // тоже новое добавление данный в структуру
          structT_GPA_AI_HMI.addData(tmpS[0], uuidAI_HMI, tmpS[1], tmpS[2]);
        }        
        // Тут вызовем все что записали в структуру - для теста, смотрим что записалось а так же наш новый класс
        //structT_GPA_AI_HMI.getAllData();
        // записываем XML методом
        
         XMLDomRW realise = new XMLDomRW(structT_GPA_AI_HMI); // пересылаем структуру для добавления  ее в глобальные переменные
         realise.runMethods(); // это надо вытащить в Главную панель
         
         //ниже что не особо правильно но тоже работает
        // Document document = realise.getDocument();
        // realise.xpatchfind(document); // Variables данные добавления
        // realise.xpatchDataTypes(document);        
        // realise.writeDocument(document); // это запись в сам файл
        data += "</Fields>\n"
                + "</Type>";
        FileOutputStream out = new FileOutputStream(patchPrg + "Type_GPA_AI_HMI_from_java.type");
        //out.write(data.getBytes("Cp1251"));
        out.write(data.getBytes("UTF8"));
        out.close(); 
     }
          void T_GPA_AI_DRV(ArrayList<String[]> arg, String name_str) throws IOException {
             this.name_str = name_str;
           // ReadWriteExel crivoiUID = new ReadWriteExel();// временнно для формирования UUID
            Type_UUIDstr = crivoiUID.getUIID();
       Iterator<String[]> iter_arg = arg.iterator();
       String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\"" + name_str + "\" Kind=\"Struct\" UUID=\"" + Type_UUIDstr + "\"> \n"
                   + "<Fields>\n";

        while (iter_arg.hasNext()) {  
          String[] tmpS = iter_arg.next();
          data += "<Field Name=\"" +tmpS[0] +"\" Type=\"" + "BOOL" + "\" UUID=\"" +tmpS[1] +"\" Comment=\"" +tmpS[2] +"\" />\n"; // Тут как сказали бул а не тип что выше в двух
        }
        data += "</Fields>\n"
                + "</Type>";
        FileOutputStream out = new FileOutputStream(patchPrg + "Type_GPA_AI_DRV_from_java.type");
        //out.write(data.getBytes("Cp1251"));
        out.write(data.getBytes("UTF8"));
        out.close();
       
     }
         
          void writeListData(int sumEl) throws IOException {
           // ReadWriteExel crivoiUID = new ReadWriteExel();// временнно для формирования UUID
            String UUIDstr = crivoiUID.getUIID();
            String namefield = "var_";
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >";
              data += "<Type Name=\"" + "List_"+ name_str + "\" Kind=\"Struct\" UUID=\"" + UUIDstr + "\"> \n"
                   + "<Fields>\n";
            int i=0;
            while (i < sumEl) {  
              data += "<Field Name=\"" +namefield + Integer.toString(i)
              +"\" Type=\"" + Type_UUIDstr + "\" UUID=\"" + crivoiUID.getUIID() +"\" Comment=\"" + "тут должен быть коментарий" +"\" />\n";
              ++i;
            }
            data += "</Fields>\n"
            + "</Type>";
            FileOutputStream out = new FileOutputStream(patchPrg + "Type_List_GPA_AI_from_java.type");
            //out.write(data.getBytes("Cp1251"));
            out.write(data.getBytes("UTF8"));
            out.close();
            }
        
          void writeAI_() throws IOException {
            uuidAI_ = crivoiUID.getUIID();
            String namefield = "var_";
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\"AI_\" Kind=\"Struct\" UUID=\"" + uuidAI_ + "\"> \n"
                   + "\t<Fields>\n";
              data += "\t\t<Field Name=\"fault_common\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"общая неисправность канала\" />" + "\n";
              data += "\t\t<Field Name=\"simulation\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"режим симуляции включен\" />" + "\n";
              data += "\t\t<Field Name=\"repair\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"в ремонте\" />" + "\n";
              data += "\t\t<Field Name=\"repair_time_less_10_percent\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"время ремонта истекает\" />" + "\n";
              data += "\t\t<Field Name=\"up_scale\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"больше чем max_fault_sensor_Eu\" />" + "\n";
              data += "\t\t<Field Name=\"down_scale\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"меньше чем min_fault_sensor_Eu\" />" + "\n";
              data += "\t\t<Field Name=\"ROC\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"ROC превышен\" />" + "\n";
              data += "\t\t<Field Name=\"init_err\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"ошибка инициализации\" />" + "\n";
              data += "\t\t<Field Name=\"res\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t\t<Field Name=\"simulation_on\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"включить режим симуляции из HMI\" />" + "\n";
              data += "\t\t<Field Name=\"repair_on\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"включить режим ремонта из HMI\" />" + "\n";
              data += "\t\t<Field Name=\"repair_extension\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"продлить время ремонта из HMI\" />" + "\n";
              data += "\t\t<Field Name=\"ps\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"для посветки желтым\" />" + "\n";
              data += "\t\t<Field Name=\"orr\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"ограничение режима\" />" + "\n";
              data += "\t\t<Field Name=\"as\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"для посветки красным\" />" + "\n";
              data += "\t\t<Field Name=\"ROC_enabled\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"включение функции ROC\" />" + "\n";
              data += "\t\t<Field Name=\"res2\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t\t<Field Name=\"res3\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t\t<Field Name=\"res4\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t\t<Field Name=\"res5\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t\t<Field Name=\"res6\" Type=\"BOOL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"резерв\" />" + "\n";
              data += "\t</Fields>\n"
              + "</Type>";
            FileOutputStream out = new FileOutputStream(patchPrg + "Type_AI__from_java.type");
            //out.write(data.getBytes("Cp1251"));
            out.write(data.getBytes("UTF8"));
            out.close();
            }
        
       void writeAI_PLC() throws IOException {
            uuidAI_PLC = crivoiUID.getUIID();
            String namefield = "var_";
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\"AI_PLC\" Kind=\"Struct\" UUID=\"" + uuidAI_PLC + "\"> \n"
                   + "\t<Fields>\n";
              data += "\t\t<Field Name=\"Span\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"диапазон датчика в EU\" />" + "\n";
              data += "\t\t<Field Name=\"Offset\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"смещение датчика в EU\" />" + "\n";
              data += "\t\t<Field Name=\"Tf\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"постоянная фильтра &gt; 0.001\" />" + "\n";
              data += "\t\t<Field Name=\"min_ADC\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"минимальное значение в единицах АЦП для обрабатываемого канала\" />" + "\n";
              data += "\t\t<Field Name=\"max_ADC\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"максимальное значение в единицах АЦП для обрабатываемого канала\" />" + "\n";
              data += "\t\t<Field Name=\"min_fault_sensor_Eu\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"если сигнал с датчика Status.Input_sensor_eu меньше этого значения, то отказ канала\" />" + "\n";
              data += "\t\t<Field Name=\"max_fault_sensor_Eu\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"если сигнал с датчика Status.Input_sensor_eu больше этого значения, то отказ канала\" />" + "\n";
              data += "\t\t<Field Name=\"ROC\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"скорость изменения сигнала\" />" + "\n";
              data += "\t\t<Field Name=\"recovery_time\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"время восстановления канала после исчезновения неисправности\" />" + "\n";
              data += "\t\t<Field Name=\"repair_time\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"время через которое канал будет автоматически выведен из ручного режима\" />" + "\n";
              data += "\t\t<Field Name=\"ROC_max\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"максимальное значение ROC, если ROC_enable = 1\" />" + "\n";
              data += "\t\t<Field Name=\"ROC_min\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"минимальное значение ROC, если ROC_enable = 1\" />" + "\n";
              data += "\t\t<Field Name=\"ROC_time\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"Время определения скорости изменения сигнала  (сек)\" />" + "\n";
              data += "\t\t<Field Name=\"nAi\" Type=\"INT\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"Номер канала\" />" + "\n";
              data += "\t</Fields>\n"
              + "</Type>";
            FileOutputStream out = new FileOutputStream(patchPrg + "Type_AI_PLC_from_java.type");
            //out.write(data.getBytes("Cp1251"));
            out.write(data.getBytes("UTF8"));
            out.close();
            }
       
            //Начальный файл
            void writeAI_HMI() throws IOException {
            uuidAI_HMI = crivoiUID.getUIID();
            String namefield = "var_";
            String data  = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
                      + "<!DOCTYPE Type v. 1.0 >\n";
              data += "<Type Name=\""+AI_HMI+"\" Kind=\"Struct\" UUID=\"" + uuidAI_HMI + "\"> \n"
                   + "\t<Fields>\n";
              data += "\t\t<Field Name=\"PV\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"сигнал с датчика, преобразованный к физическим единицам\" />" + "\n";
              data += "\t\t<Field Name=\"Condition\" Type=\"" +uuidAI_+ "\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"0-11 коды отказов канала, 12-15 команды с мнемосхемы\" />" + "\n"; 
              data += "\t\t<Field Name=\"CurrentTimeOfRepair\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"текущее время ремонта канала\" />" + "\n";  // вот странный момент
              data += "\t\t<Field Name=\"Manual_Target\" Type=\"REAL\" UUID=\""+crivoiUID.getUIID()+"\" Comment=\"задание для выхода блока в ручном режиме\" />" + "\n";
              data += "\t</Fields>\n"
              + "</Type>";
            FileOutputStream out = new FileOutputStream(patchPrg + "Type_AI_HMI_from_java.type");
            //out.write(data.getBytes("Cp1251"));
            out.write(data.getBytes("UTF8"));
            out.close();
            }
    
}

