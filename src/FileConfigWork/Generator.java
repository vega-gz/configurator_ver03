/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileConfigWork;

import FrameCreate.TableNzVer2;
import XMLTools.XMLSAX;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.apache.poi.ss.usermodel.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Григорий
 */
public class Generator {

    XMLSAX createXMLSax = new XMLSAX();

    public void GenSigType(String nameTable,TableNzVer2 tnz,String filepath) throws ParserConfigurationException {
        
        String patchF = filepath + "\\" + "имя типа которое надо вытянуть" + ".type";
        //Iterator<String[]> iter_arg = arg.iterator();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        Document doc = factory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("Type");
        root.setAttribute("Name", "имя которое необходимо вытянуть");
        root.setAttribute("Kind", "Struct");
        root.setAttribute("UUID", "уид который надо вытянуть");//вот это UUDSTRUC должен совпадать с дочерними уидами,то естьв нем должны быть сигналы с типом его уида
        doc.appendChild(root);
        Element Fields = doc.createElement("Fields");
        root.appendChild(Fields);

            Element Field = doc.createElement("Field");
            Field.setAttribute("Name", "алгоритмическое имя");
            Field.setAttribute("Type", "тип который вытянуть");//задали тип данных рукописно.Кстати не знаю верно это или нет Но вроде пишет что то
            Field.setAttribute("UUID", "УИД вытянуть");
            Field.setAttribute("Comment", "русское имя вытянуть");
            Fields.appendChild(Field);//if(){}public static void main(String []args){}
            
       createXMLSax.docInstance(doc, patchF); // зарегистрировать документ в нашем XML парсере
       createXMLSax.writeDocument(); // без регистрации которая выше не сработает

    }
}


