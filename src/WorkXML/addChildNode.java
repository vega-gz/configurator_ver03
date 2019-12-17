/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WorkXML;

import basepostgresluaxls.UUID;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author nazarov
 */
public class addChildNode {
    String patchF ="C:\\Users\\Nazarov\\Documents\\NetBeansProjects\\BasePostgresLuaXLS\\src\\WorkXML\\newXMLDocument.xml"; 
    public static void main(String args[]){
        try {
            addChildNode test = new addChildNode();
            test.start();
        } catch (SAXException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerFactoryConfigurationError ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathFactoryConfigurationException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(addChildNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void start()throws SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, XPathFactoryConfigurationException, InterruptedException{
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
        XPathExpression expr = xpath.compile("office");
//        XPathExpression expr = xpath.compile("company/offices/office");
        NodeList nodes = (NodeList) expr.evaluate(document_final, XPathConstants.NODESET);
        //  так как нода у нас одна то пишем только в 1 по этому for так работает
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            Element GCFBtype = document_final.createElement("GraphicTest2");
            GCFBtype.setAttribute("Name", "TGraphicTest2"); // тоже цикл с изменения доолжен быть так как по 64 элемента
            GCFBtype.setAttribute("UUID", "000000000");
            n.appendChild(GCFBtype);
            System.out.println(n.getNodeName());
            for (int j = 0; j < 3; j++) {
                Element testC = document_final.createElement("Test" + Integer.toString(j));
                GCFBtype.appendChild(testC);
            }
        }
        writeDocument(document_final, patchF);
        //и возвращаем ему удаленный DOCTYPE
        //testStart.returnToFileDtd(patchF);
    }
    
    void writeDocument(Document document, String patchWF) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        try {
            File file = new File(patchWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file));
            
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
    }
}
