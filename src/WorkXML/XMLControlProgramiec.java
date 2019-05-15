/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WorkXML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author nazarov
 * тут все работает что реализованно
 */
public class XMLControlProgramiec {
    private static ArrayList<Program> employees = new ArrayList<>();
    XMLHandler handler = new XMLHandler();
    //static File pathF = new File("src\\WorkXML\\ControlProgram.iec_st");
    //static File pathF = new File("src\\WorkXML\\newXMLDocument.xml");
    static File pathF = new File("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\ControlProgram.iec_st");

    public static void main(String args[])throws ParserConfigurationException, SAXException, IOException, FileNotFoundException, XMLStreamException, UnsupportedEncodingException, TransformerException {
        XMLControlProgramiec s_xml = new XMLControlProgramiec();
        s_xml.startTest();
        s_xml.writeXML();
        
        /* тут все не работает из за static 
        // Создание фабрики и образца парсера
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        
        
        parser.parse(pathF, handler);
        
       

        for (Employee employee : employees)
            System.out.println(String.format("Имя сотрудника: %s, его должность: %s", employee.getName(), employee.getJob()));
        */
    
    }
    
    public void startTest()throws ParserConfigurationException, SAXException, IOException { 
        // Создание фабрики и образца парсера
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
                
        parser.parse(pathF, handler);

        
        //for (Program employee : employees)
          //  System.out.println(String.format("Имя Name: %s, Комментарий: %s, его UUD: %s", employee.getName(), employee.getcomment(), employee.getUUID()));
    
        }

 private  class XMLHandler extends DefaultHandler { // вся эта сисстема не годится, нужен DOM парцер
        @Override
        public void startDocument() throws SAXException {
            // Тут будет логика реакции на начало документа

        }

        @Override
        public void endDocument() throws SAXException {
            // Тут будет логика реакции на конец документа
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // Тут будет логика реакции на начало элемента
            if (qName.equals("Field")) {
                String name = attributes.getValue("Name");
                String UUID = attributes.getValue("UUID");
                String comment = attributes.getValue("Comment");
                employees.add(new Program(name, UUID, comment));
            }
            if (qName.equals("Variable")) { // немного не то =( нам нужно знать родителей и ноды 
                System.out.println(attributes.getLength());
                for (int i =0; i<attributes.getLength(); i++){
                System.out.println(attributes.getQName(i));
                }
                
              /*  String name = attributes.getValue("Name");
                String UUID = attributes.getValue("UUID");
                String comment = attributes.getValue("Comment");
                employees.add(new Program(name, UUID, comment));
              */
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Тут будет логика реакции на конец элемента
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // Тут будет логика реакции на текст между элементами
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            // Тут будет логика реакции на пустое пространство внутри элементов (пробелы, переносы строчек и так далее).
        }
    }

 class Program {
    private String name, UUID, comment;

    public Program(String name, String UUID, String comment) {
        this.name = name;
        this.UUID = UUID;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getUUID() {
        return UUID;
    }
    
    public String getcomment() {
        return comment;
    }
}
 
 void writeXML() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException, ParserConfigurationException, TransformerConfigurationException, TransformerException{

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
factory.setNamespaceAware(true);
Document doc = factory.newDocumentBuilder().newDocument();


Element root = doc.createElement("root");
root.setAttribute("xmlns", "http://www.javacore.ru/schemas/");
doc.appendChild(root);
 
Element item1 = doc.createElement("item");
item1.setAttribute("val", "1");
root.appendChild(item1);
            
Element item2 = doc.createElement("item");
item2.setAttribute("val", "2");
root.appendChild(item2);
            
Element item3 = doc.createElement("item3");
item3.setAttribute("val", "3");
root.appendChild(item3);  

Element item4 = doc.createElement("item");
item4.setAttribute("val", "4");
item3.appendChild(item4);  



File file = new File("src\\WorkXML\\test.xml");
 
Transformer transformer = TransformerFactory.newInstance().newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
transformer.transform(new DOMSource(doc), new StreamResult(file));

 }
}