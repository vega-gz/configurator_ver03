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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteConfigFile {

    private static final String FILENAME = "Config.txt";

    void writeTXT(String user, String url, String nameProject) throws IOException {
        String USER, URL, namePro;
        USER = user;
        URL = url;
        namePro = nameProject;
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);//путь в папку к файлу
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

    void writeXML(String user, String url, String nameProject,String pass) throws ParserConfigurationException {//сделать файл красиво,с переносом строки чтобы записывал
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("config");
            doc.appendChild(rootElement);
            
            Element settings = doc.createElement("Settings");
            rootElement.appendChild(settings);
            
            Element USER=doc.createElement("USER");
            USER.appendChild(doc.createTextNode(user));
            settings.appendChild(USER);
            
            Element urlPath=doc.createElement("URL");
            urlPath.appendChild(doc.createTextNode(url));
            settings.appendChild(urlPath);
            
            Element PASS=doc.createElement("PASS");
            PASS.appendChild(doc.createTextNode(pass));
            settings.appendChild(PASS);
            
            Element nameproject=doc.createElement("nameProject");
            nameproject.appendChild(doc.createTextNode(nameProject));
            settings.appendChild(nameproject);
            
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();
            DOMSource sourse=new DOMSource(doc);
             StreamResult result=new StreamResult(new File(System.getProperty("user.dir") + File.separator + "Config.xml"));
            
          //  StreamResult result1 =new StreamResult(System.out);
            
            transformer.transform(sourse, result);
            
            System.out.println("File saved!");
        } catch (TransformerException ex) {
            Logger.getLogger(WriteConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        

      

    }
}
