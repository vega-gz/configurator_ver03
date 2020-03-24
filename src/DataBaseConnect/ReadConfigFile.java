/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseConnect;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author cherepanov
 */
public class ReadConfigFile {

    String url, nameProject, user, pass;
    String FILENAME = "Config.xml";

    public void ReadConfig() {
        try {
            final File xmlFile = new File(System.getProperty("user.dir") //user.dir-это путь до домашнего каталога(каталог где хранится прога)
                    + File.separator + FILENAME);//separator это разделитель =="\"

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(xmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Наш файл:" + doc.getDocumentElement().getNodeName());
            System.out.println("=================");

            NodeList nodeList = doc.getElementsByTagName("config");

            for (int i = 0; i < nodeList.getLength(); i++) {
                //выводим инфу по каждому их элементов
                Node node = nodeList.item(i);
                System.out.println();
                System.out.println("Текущий элемент: " + node.getNodeName());
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
//                    
                    pass = element.getElementsByTagName("PASS").item(0).getTextContent();
                    user = element.getElementsByTagName("USER").item(0).getTextContent();
                    url = element.getElementsByTagName("URL").item(0).getTextContent();
                }

            }
            System.out.println(pass + " " + url + " " + user);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        DataBase db = new DataBase();
        db.connectionToBase(url,user,pass);
        
    }
}
