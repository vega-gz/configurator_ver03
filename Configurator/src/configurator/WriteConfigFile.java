/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class WriteConfigFile {

    String FILENAME = "Config.xml";
    File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);//путь к файлу указали в корневую папку

    void writeTXT(String user, String url, String nameProject) throws IOException {//читаем txt файл 

        String USER, URL, namePro;
        USER = user;
        URL = url;
        namePro = nameProject;
        try {
            // File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);//путь в папку к файлу
            if (!file.exists()) {
                file.createNewFile();//пишем новый файл
            }
            FileWriter fw;
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Адрес базы данных: " + URL + " Имя пользователя: " + USER + " имя программы:" + namePro);//разобраться с переносом строки
//            bw.write(PASS);
//            bw.write(namePro);
            bw.close();

            System.out.println("Запись завершена");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void writeXML(String user, String url, String nameProject, String pass) throws ParserConfigurationException{//сделать файл красиво,с переносом строки чтобы записывал
//--------этот метод для того чтобы добавлять в конфиг файл доп записи
//        if (file.isFile()) {//если файл уже существует ,добавить в него данные
//            try{
//                //это пока не актуально,нужно сделать динамически изменяющиеся 
//           DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
//
//                Document doc = docBuilder.parse(file);
//                Element rootElement = doc.createElement("config");
//                doc.appendChild(rootElement);
//                doc.getDocumentElement().normalize();
//
//                Element settings = doc.createElement("Settings3");
//                rootElement.appendChild(settings);
//
//                Element USER = doc.createElement("USER");
//                USER.appendChild(doc.createTextNode(user));
//                settings.appendChild(USER);
//
//                Element urlPath = doc.createElement("URL3");
//                urlPath.appendChild(doc.createTextNode(url));
//                settings.appendChild(urlPath);
//
//                Element nameproject = doc.createElement("nameProject3");
//                nameproject.appendChild(doc.createTextNode(nameProject));
//                settings.appendChild(nameproject);
//
//                Element PASS = doc.createElement("PASS3");
//                PASS.appendChild(doc.createTextNode(pass));
//                settings.appendChild(PASS);
//                
//                Transformer transformer=TransformerFactory.newInstance().newTransformer();
//                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//                
//                StreamResult result=new StreamResult(new StringWriter());
//                DOMSource sourse=new DOMSource(doc);
//                transformer.transform(sourse, result);
//                String xmlString =result.getWriter().toString();
//                
//                PrintWriter output=new PrintWriter(file);
//                output.println(xmlString);
//                output.close();
//                
//                System.out.println("File create!");
//            }catch(TransformerException ex){
//                
//            }catch(FileNotFoundException ex){
//                
//            }catch(SAXException ex){
//                
//            }catch(IOException ex){
//                
//            }
//            
//        } else {

            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();//создание дом парсера
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                Document doc = docBuilder.newDocument();//создание верхнего элемента
                Element rootElement = doc.createElement("config");
                doc.appendChild(rootElement);

                Element settings = doc.createElement("Settings");//ниже идет создание внутренних элементов
                rootElement.appendChild(settings);

                Element USER = doc.createElement("USER");//
                USER.appendChild(doc.createTextNode(user));
                settings.appendChild(USER);

                Element urlPath = doc.createElement("URL");
                urlPath.appendChild(doc.createTextNode(url));
                settings.appendChild(urlPath);

                Element nameproject = doc.createElement("nameProject");
                nameproject.appendChild(doc.createTextNode(nameProject));
                settings.appendChild(nameproject);

                Element PASS = doc.createElement("PASS");
                PASS.appendChild(doc.createTextNode(pass));
                settings.appendChild(PASS);
                
                //позволяет задать диапазон

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");//запись xml в виде структуры
                DOMSource sourse = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + File.separator + FILENAME));

                //  StreamResult result1 =new StreamResult(System.out);
                transformer.transform(sourse, result);

                System.out.println("File saved!");
            } catch (TransformerException ex) {
                Logger.getLogger(WriteConfigFile.class.getName()).log(Level.SEVERE, null, ex);
            }
     //   }

    }
   
}
