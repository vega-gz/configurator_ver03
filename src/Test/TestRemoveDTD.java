/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import basepostgresluaxls.UUID;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author nazarov
 */

// удаляем не верный <!DOCTYPE SubAppType v. 1.3 >
public  class TestRemoveDTD {
    static String doctype = "";
    static int positionDTD;
    public static Document removeDTDFromXML(String payload) throws Exception {

    System.out.println("### Payload received in XMlDTDRemover: " + payload);

    Document doc = null;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {

         dbf.setValidating(false);
         dbf.setNamespaceAware(true);
         dbf.setIgnoringElementContentWhitespace(false);
         dbf.setIgnoringComments(false);
         dbf.setNamespaceAware(true);
         dbf.setFeature("http://xml.org/sax/features/namespaces", false);
         dbf.setFeature("http://xml.org/sax/features/validation", false);
         dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
         dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); // это работает с примером
         
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        db.setEntityResolver(new EntityResolver() // вот это должны были переопределить но не работает так как не должно быть ошибки это просто обработчик
        {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException
            {    
                if (systemId.contains("Project")) {
                return new InputSource(new StringReader(""));
            } else {
                return null;
            }
        }
    });
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(payload));
        doc = db.parse(is); 

    } catch (ParserConfigurationException e) {
        System.out.println("Parse Error: " + e.getMessage());
        return null;
    } catch (SAXException e) {
        System.out.println("SAX Error: " + e.getMessage());
        return null;
    } catch (IOException e) {
        System.out.println("IO Error: " + e.getMessage());
        return null;
    }
    return doc;

}
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Exception{
    //String patchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\Project.prj";
    String patchD = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\";
    String patchF = patchD + "ControlProgram.int";
   
    String DTD = " <!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">" +"\n" +
    "<MYACCSERVICE>\n" +
    "   <REQ_PAYLOAD>\n" +
    "      <ACCOUNT>1234567890</ACCOUNT>\n" +
    "      <BRANCH>001</BRANCH>\n" +
    "      <CURRENCY>USD</CURRENCY>\n" +
    "      <TRANS_REFERENCE>201611100000777</TRANS_REFERENCE>\n" +
    "   </REQ_PAYLOAD>\n" +
    "</MYACCSERVICE>";    
    
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = removeDTDFromXML(DTD);
        
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        
        XPathExpression expr = xpath.compile("MYACCSERVICE/REQ_PAYLOAD");
         NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            Element signal = document.createElement("Signal");
            signal.setAttribute("Name", "Test1");
            signal.setAttribute("UUID", "Test1");
            signal.setAttribute("Type", "Test1");
            signal.setAttribute("Global", "TRUE");
            n.appendChild(signal);
        }
        // в файл потом запишем
        /*
            patchF = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design\\Project1.prj";
            File file = new File(patchF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult());
         */
        
        methodRead(patchF);
    }
    
    static void viewAllXML (Document document){
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
    
    // Метод Парсера строк поиск Доктипа
    static String paternDOCTYPE(String st1, int pos){
     Pattern pattern2 = Pattern.compile("^(<!DOCTYPE.*)$"); // Патерн нашего ДокТипа
     Matcher matcher2 = pattern2.matcher(st1);
     if (matcher2.matches()){ 
         positionDTD = pos; // Так же вносим позицию от куда это взяли
         doctype = matcher2.group(1); // в глобальные переменную что собираемся затереть
         //badDatatype findDatatype = new badDatatype(doctype, pos); // покане понятно нужно ли использовать отдельный класс для храннения этого
         System.out.println(doctype);
         return "";  // Возвращаем пустую строку если нашли DOCTYPE 
     }else return st1;

    }
    
     //метод чтения файла
   static  public void methodRead(String path) throws InterruptedException{    
        String result_data = "";
        try {
        BufferedReader in = new BufferedReader(new FileReader(path));
        String str;
        int pos_str = 0;
        while ((str = in.readLine()) != null){
            result_data += paternDOCTYPE(str, pos_str) + "\n"; // Передаем строки в парсер обработчик
            // тут уже doctype пустая
            ++pos_str;
        }
        in.close();
    } catch (IOException e) {
    }
    methodWrite(path, result_data);
    }
   
   //метод записи файла и дублирующие предыдущии
    static public void  methodWrite(String path, String data)throws InterruptedException{   
        try {
               
        File resultName = new File(path + "_newfile"); //Файл с новой записью
        File tmpName = new File(path + "_original"); // это просто Имя
        File realName = new File(path); // Оригинальное имя
        //File resultName = new File(path + "_newfile"); //Файл с новой записью
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultName, true));
        int tmpPos =0;
        String resultTofile = "";
        for (String tmpStr : data.split("\n")){ // бъем строку что бы записать в нужное место что вырезали выше
          //writer.append(tmpStr);
          
          if (tmpPos == positionDTD){ // если позиция верная то внсим обратно доктипДТД
              //writer.append(doctype);
              resultTofile += doctype + "\n";
            } else resultTofile += tmpStr + "\n"; // таким способоб убираем пустую и вставляем нужную
          //writer.append("\n");
          ++tmpPos;
        }        
        //writer.write(data); // это походу переписать
        writer.write(resultTofile); // Добавляем вновь сформированную строку в файл
        //for (int st=0; st<10; ++st){
        //   if(st==3) writer.append("\n A am your Fathet LOOK!" );
        //}
       // writer.append('\n' );        // это добавить
       // writer.append(str.toString());
        writer.close();
        
        // Тут создадим временную папку для файлов бекапов
        // максимальное вхождение вложений папок вышло 85 на W7
        /*
        String SubDirectory2 = "C:\\Directory" ;
        // но сначала все удалим
        //recursiveDelete(new File(SubDirectory2)); // jxtym медленно удаляло
        //deleteDirectory(new File(SubDirectory2)); // Такое же
        new File(SubDirectory2).mkdir(); 
        int pa = 1000000;
        for (int it=0; it<pa; ++it){ 
            SubDirectory2  += "\\" + Integer.toString(it);
            new File(SubDirectory2).mkdir(); 
            File testF = new File(SubDirectory2 + "\\newfile_test.txt"); //Файл с новой записью
            BufferedWriter writertest = new BufferedWriter(new FileWriter(testF, true));
            for (int j=0; j<pa; ++j){ //тут просто мучаем диск 
               writertest.write(SubDirectory2); // вносим строк с путем до него
            writertest.close();
            }
        }
        System.out.println(SubDirectory2);
        //new File(SubDirectory2).mkdirs();  // так зоздавать полный путь
        */

         // Удаляем  и переименовываем в удаленный файл
         if (tmpName.exists()){ // проверяем на существование бекапного файла
             if(tmpName.delete()){ // если он есть удаляем его
                System.out.println(tmpName.getName() + " is deleted!");
                realName.renameTo(tmpName); 
            }else{
                System.out.println("Delete operation is failed.");
            }
         } else realName.renameTo(tmpName); 
         resultName.renameTo(realName);
        //tmpName.renameTo(resultName);
       
             

    } catch (IOException e) {
    }
     }
    
   
    // Рекурсивное удаление фалов и каталогов очень медленно
    public static void recursiveDelete(File file) {
        // до конца рекурсивного цикла
        if (!file.exists())
            return;
         
        //если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление всего, что там есть
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                // рекурсивный вызов
                recursiveDelete(f);
            }
        }
        // вызываем метод delete() для удаления файлов и пустых(!) папок
        file.delete();
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }
    
    // -- Еще одно удаление---
    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
                System.out.println("Удалено : " + f);
            }
            dir.delete();
        } else dir.delete();
    }
 
}

// Класс для хранения расположение и что должно быть Дататипе 
class badDatatype{
    Map<Integer, String> mapDataType = new HashMap<>();
    String doctype = "";
    int position;
public badDatatype(String doctype, int position){
     this.doctype = doctype;
     this.position = position;
     // Вносим значения в наш Мап  номера строк в файле уникальные по этому этот метод
     mapDataType.put(position, doctype);
}
public void getVale(){
    Set<Map.Entry<Integer, String>> set = mapDataType.entrySet(); // Какой то метод Set мне ене известно почитать
    for (Map.Entry<Integer, String> me : set) {
    System.out.print(me.getKey() + ": " + me.getValue());
}
}

}

