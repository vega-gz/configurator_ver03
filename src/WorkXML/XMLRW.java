/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package WorkXML;

/**
 *
 * @author nazarov
 */
 
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class XMLRW {
 
    private static void stepThrough (Node start)
  {
    System.out.println(start.getNodeName()+" = "+start.getNodeValue() + " " + start.getAttributes());
    for (Node child = start.getFirstChild();
          child != null;
          child = child.getNextSibling())
    {
      stepThrough(child); 
    }
  }
    
    // вот тут мы все и получаем данные рекурсией
    private static void stepThroughAll (Node start)
    {
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
    //изменение данных не работает
    private static void changeOrder (Node start,
            String elemName,
            String elemValue)
    {
        if (start.getNodeName().equals(elemName)) {
        start.getFirstChild().setNodeValue(elemValue);
        }
        for (Node child = start.getFirstChild();
            child != null;
            child = child.getNextSibling())
        {
            changeOrder(child, elemName, elemValue);
        }
  }
    public static void main(String[] args) {
        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse("E:\\Sonata\\r9709\\project_switcher\\KVANT2R_Ekaterinovka_20191204_1\\Design\\AT_HMI.iec_hmi");
            //Document document = documentBuilder.parse("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\ControlProgram.iec_st");
            
            // Получаем корневой элемент
            Node root = document.getDocumentElement();
         /*   System.out.println(root.getNodeName()); //Получение корневого элемента
            System.out.println(root.getChildNodes());
            System.out.println(root.getFirstChild()); // не видит
            System.out.println(root.getNodeValue());
            System.out.println(root.getNodeType());
           */ 
            System.out.println(root.getParentNode());
            // Просматриваем все подэлементы корневого - т.е. книги
            
            //тут нработает ноды перебирает
           XMLRW testXMLRW =  new XMLRW();
           testXMLRW.stepThroughAll(root); 

            
      /*
            NodeList books = root.getChildNodes();
                for (Node child = root.getFirstChild();
          child != null;
          child = child.getNextSibling())
         {
         System.out.println(child.getNodeName()+" = "
           +child.getNodeValue());
         }
       */
                   // stepThrough(root);
                   // stepThroughAll(root);
                  //  changeOrder(root, "status", "processing");  // не работает

              /*
            for (int i = 0; i < books.getLength(); i++) {
                Node book = books.item(i);
                System.out.println("This getNodeName " + book.getNodeName() + " getNodeValue " + book.getNodeValue());// Название второстепенных нод
                // Если нода не текст, то это книга - заходим внутрь
                if (book.getNodeType() != Node.TEXT_NODE) {
                    
                  
                    NodeList bookProps = book.getChildNodes();
                    for(int j = 0; j < bookProps.getLength(); j++) {
                        Node bookProp = bookProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            int len_chilnode =bookProp.getChildNodes().getLength();
                            System.out.println(bookProp.getNodeName() + ":" );
                            System.out.println(bookProp.getChildNodes().getLength());
                            // System.out.println(bookProp.getChildNodes().item(0).getTextContent());
                           // for (int i1=0; i1 < len_chilnode; i1++){
                           // bookProp.getChildNodes().item(i1).getTextContent();
                           // }
                            
                        } 
                    }
                    System.out.println("===========>>>>");
                }
            }*/
 
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
