/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import static com.sun.org.apache.xerces.internal.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static com.sun.org.apache.xerces.internal.jaxp.JAXPConstants.W3C_XML_SCHEMA;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
 
public class XMLDomRW {
    private String nameStruct = "";
    private String newUUIDelem = "";
    private String typeStruct = "";
    private String patchF = "";
    private Struct structData;
    private Document document;
    private String uuidProject;
    private String GpatchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\";
    // Создается построитель документа
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

    // Написано Дом но мы используем SAX (Глобальные и хитрые переменные)
    public XMLDomRW()throws ParserConfigurationException, SAXException, IOException{
        // Создается дерево DOM документа из файла
        // document = documentBuilder.parse("src\\WorkXML\\test.xml");}
        document = documentBuilder.parse("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\ControlProgram.iec_st");    
    }
            
    // --- Такой коструктор когда передаем структуру для добавления для файла Programm
    public XMLDomRW(Struct struct) throws ParserConfigurationException, SAXException, IOException{
        this.structData = struct;
        this.newUUIDelem = structData.getUUD();
        this.nameStruct = structData.getName();
        this.typeStruct = structData.getType();
        // Создается дерево DOM документа из файла
        patchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\ControlProgram.iec_st";
        document = documentBuilder.parse(patchF);
    }
    
    Document getDocument(){
    return document;}

    // запуск обрабтки файлов что бы не из статического main . Переписать что бы не было document в входном параметре
    void runMethods() throws DOMException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, SAXException, IOException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException{
            xpatchfind(document); // Variables данные добавления
            xpatchDataTypes(document);        
            writeDocument(document, patchF); // это запись в сам файл
            addSignalGlobal();
            addSignalHMI();
            //viewAllXML(document);  // просмотр всех записей
    }

    public static void main(String[] args) throws DOMException, XPathExpressionException {
       /* try {
        // Тут весь вызов без параметров
        MLDomRW realise = new XMLDomRW();
        realise.xpatchfind(document); // Variables данные добавления
        realise.xpatchDataTypes(document);        
        realise.writeDocument(document); // это запись в сам файл
        //viewAllXML(document);  // просмотр всех записей    
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
         catch (TransformerFactoryConfigurationError ex) {
           Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
           Logger.getLogger(XMLDomRW.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
  
    //переборка всего что есть в ноде
  private  void stepThroughAll (Node start)
  {
    System.out.println(start.getNodeName()+" = "+start.getNodeValue());
    if (start.getNodeType() == start.ELEMENT_NODE)
    {
      NamedNodeMap startAttr = start.getAttributes();
      for (int i = 0;
          i < startAttr.getLength();
          i++) {
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
    
     void viewAllXML (Document document){
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
    
    // Метод добавления Variable
    void xpatchfind(Document document) throws DOMException, XPathExpressionException {
        System.out.println("Печать Variables");
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        // а вот тут надо посчитать сколько переменных
        XPathExpression expr = xpath.compile("Program/Variables/Variable");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        int sumVar = nodes.getLength(); // сколько Variable         
        System.out.println(sumVar);
        
        expr = xpath.compile("Program/Variables");
        nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) { // Переборка не нужна по сути она у нас одна по похер
            Node n = nodes.item(i);
            createBook(document, n, sumVar); //Создаем элементы в ноде которую передали
            //stepThroughAll(n);// вызываем метод по перебору всего что в Variables   
        }    
    }
    
    // еще один метод но добавление Struct
     void xpatchDataTypes(Document document) throws DOMException, XPathExpressionException {
        System.out.println("Печать DataTypes");
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("Program/DataTypes");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            createStruct(document, n); //Создаем элементы в ноде которую передали
            //stepThroughAll(n);// вызываем метод по перебору всего что в DataTypes
        }
        System.out.println();
    }
      void createBook(Document document, Node p_node, int sumVar)throws TransformerFactoryConfigurationError, DOMException, XPathExpressionException {
        // Получаем корневой элемент
        //Node root = document.getDocumentElement();
         Node root = p_node; // это что бы не переписывать
         String addsumVar = Integer.toString(sumVar+1); // строка номер следующей Variable
    /*
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    factory.setExpandEntityReferences(false);
    Element element = document.getElementById("Variable");
    */
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("Variables");   
        Element Variable = document.createElement("Variable");
        Variable.setAttribute("UUID", UUID.getUIID()); // рандомный уид так по логике сонаты
        Variable.setAttribute("Name", "variable"+addsumVar);
        Variable.setAttribute("Type", nameStruct );
        Variable.setAttribute("TypeUUID", newUUIDelem);
        Variable.setAttribute("Usage", "internal");
        // Добавляем книгу в корневой элемент который передали в фукцию
        root.appendChild(Variable); 
        //expr.
    }
        void createStruct(Document document, Node p_node)throws TransformerFactoryConfigurationError, DOMException, XPathExpressionException {
          Node root = p_node; // это что бы не переписывать
          Element Struct = document.createElement("Struct");
          Struct.setAttribute("UUID", newUUIDelem);
          Struct.setAttribute("Name", nameStruct);
          // перебираем все элементы в добавление поля
          Iterator<Map> iter_arg = structData.getlistData().iterator();
          while (iter_arg.hasNext()) {  //перебираем наш лист с Мапом
            Map<String, String> hashMap = iter_arg.next(); // Новый мап с нашими данными
            Element Field = document.createElement("Field");
             for(Map.Entry<String, String> item : hashMap.entrySet()){        
               //System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
              switch (item.getKey()){ 
              case "Name" : Field.setAttribute("Name", item.getValue());break;
              case "Type": Field.setAttribute("Type", typeStruct); break;// вот тут вопрос на каждый элемент он ли будет всегда
              case "UUID": Field.setAttribute("UUID", item.getValue());break;
              case "Comment":Field.setAttribute("Comment", item.getValue());break;
              case "TypeUUID":Field.setAttribute("TypeUUID", item.getValue());break; // вот это как то надо тоже достать УИД начального файла
              default: break;
              }
              //добавляем вложения в структуру
              Struct.appendChild(Field);
             }
           }
        // Добавляем книгу в корневой элемент который передали в фукцию
        root.appendChild(Struct); 
    }
     
    void addSignalAlgorithm(Document document, Node p_node)throws TransformerFactoryConfigurationError, DOMException, XPathExpressionException {
        Node root = p_node; // это что бы не переписывать
        Element Signal = document.createElement("Signal");
        Signal.setAttribute("Name", "AI_PLC"); // нужно генерить будет имя по названию структуры
        Signal.setAttribute("UUID", "81EA86514D465A09124C7DA6B2EB7144");// это должна быть генерация
        Signal.setAttribute("Type", "81EA86514D465A09124C7DA6B2EB7146");//это UUID структуры               
        // Добавляем книгу в корневой элемент который передали в фукцию
        root.appendChild(Signal); 
    }
    
     // Запись в файл глобальной переменой этой структуры с использованием XML
     //  С игнорированием DOCTYPE
     void addSignalGlobal() throws SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException{
        patchF = GpatchF + "Project.prj";
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
        //Document document_final = doc.parse(patchF); // А вот тут у нас сложность с нашим документом <!DOCTYPE Project v. 1.0 >  нужно использовать TestRemoveDTD
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();        
        // а вот тут надо посчитать сколько переменных
        XPathExpression expr = xpath.compile("Project/Globals");
        NodeList nodes = (NodeList) expr.evaluate(document_final, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            Element signal = document_final.createElement("Signal");
            signal.setAttribute("Name", nameStruct);
            this.uuidProject = UUID.getUIID();
            signal.setAttribute("UUID", uuidProject);
            signal.setAttribute("Type", newUUIDelem);
            signal.setAttribute("Global", "TRUE");
            n.appendChild(signal);
        }
        // Тут запустим запись в файл
        writeDocument(document_final, patchF);
        //и возвращаем ему удаленный DOCTYPE
        testStart.returnToFileDtd(patchF);
     }
     
     // Запись в файл, что мнемосхемы увидела этот сигнал
     void addSignalHMI() throws SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException{
        patchF = GpatchF + "HMI.int";
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
        //Document document_final = doc.parse(patchF); // А вот тут у нас сложность с нашим документом <!DOCTYPE Project v. 1.0 >  нужно использовать TestRemoveDTD
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        // а вот тут надо посчитать сколько переменных
        XPathExpression expr = xpath.compile("SubAppType/InterfaceList");    
        NodeList nodes = (NodeList) expr.evaluate(document_final, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            Element signal = document_final.createElement("Signal");
            signal.setAttribute("Name", nameStruct);
            signal.setAttribute("UUID", uuidProject);
            signal.setAttribute("Type", newUUIDelem);
            signal.setAttribute("Usage", "");
            signal.setAttribute("Global", "TRUE");
            n.appendChild(signal);
        }
        // Тут запустим запись в файл
        writeDocument(document_final, patchF);
        //и возвращаем ему удаленный DOCTYPE
        testStart.returnToFileDtd(patchF);
     }
     
    // -- Тут созданике файла  Type_AI_.type (Будет без DOCTYPE) ---
    void createTypeAI_() throws ParserConfigurationException{
        patchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\Type_AI_.type";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "AI_");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", UUID.getUIID());
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
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // это разделители смотреть что бы не в одну строку
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            //transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            
            transformer.transform(new DOMSource(document), new StreamResult(file));
            
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
}
