/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileConfigWork;

import FrameCreate.FrameTabel;
import FrameCreate.TableNzVer2;
import XMLTools.XMLSAX;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.apache.poi.ss.usermodel.Table;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Григорий
 */
public class Generator {

    public static void GenSigType(FrameTabel ft)  {
        HashMap<String, String> map = new HashMap<>();
        XMLSAX test = new XMLSAX();
        Node newEl = test.createDocument("mazafaker");
        Node n = test.createNode("mazafaker_child");
        //newEl.setTextContent("setTextContent"); // так пишем текстовое поле если надо
        map.put("Name", "имя которое необходимо вытянуть");
        map.put("Kind", "Struct");
        map.put("UUID", "уид который надо вытянуть");//вот это UUDSTRUC должен совпадать с дочерними уидами,то естьв нем должны быть сигналы с типом его уида
        test.insertDataNode(newEl, map);
        newEl.appendChild(n);
        test.insertDataNode(n, map);
        test.writeDocument("test666.xml");

    }
    
    public static void GenMnemoSig(){
       
    }
}
