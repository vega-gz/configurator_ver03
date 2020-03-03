/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class readXML {

    public void RXML() {
         try {
            final File xmlFile = new File("C:\\Users\\cherepanov\\Desktop\\types\\T_AI_FromProcessing.type");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(xmlFile);

            doc.getDocumentElement().normalize();
            
            Element root=(Element) doc.getElementsByTagName("Type");
            
            

            System.out.println("Наш файл:" + doc.getDocumentElement().getNodeName());
            System.out.println("=================");

            NodeList nodeList = doc.getElementsByTagName("Type");

            for (int i = 0; i < nodeList.getLength(); i++) {
                //выводим инфу по каждому их элементов
                Node node = nodeList.item(i);
                System.out.println();
                System.out.println("Текущий элемент: " + node.getNodeName());
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
//                    
                   
                }

            }
         //   System.out.println(pass + " " + url + " " + user);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
    
      void viewAllXML (org.w3c.dom.Document document){
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
                for(int j = 0; j < bookProps.getLength(); j++) {
                    Node bookProp = bookProps.item(j);
                    // Если нода не текст, то это один из параметров книги - печатаем
                    if (bookProp.getNodeType() != Node.TEXT_NODE) {
                        // System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
                        for(int i1=0; i1 < bookProp.getChildNodes().getLength(); i1++){
                            System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(i1).getTextContent());
                        }
                    }
                }
                System.out.println("===========>>>>");
            }
        }
    }
    
    public static void main(String[] args) {
        readXML read=new readXML();
        File file;
     //   read.viewAllXML();
        
    }

}
