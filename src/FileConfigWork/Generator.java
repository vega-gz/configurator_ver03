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

    

    public static void GenSigType(String nameTable,TableNzVer2 tnz) throws ParserConfigurationException {
        
        String patchF = "путь из файла конфигурации" + "\\" + "имя типа которое надо вытянуть" + ".type";
       
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
            Fields.appendChild(Field);

        

//        try  {
//           createXMLSax.writeDocument(doc, patchF);
//        } catch (TransformerFactoryConfigurationError ex) {
//            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);//
//        } catch (TransformerException ex) {
//            Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}


