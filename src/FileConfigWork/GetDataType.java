/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FileConfigWork;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Григорий
 */
public class GetDataType {
    private static ArrayList<ConfigData>employees=new ArrayList<>();
    
    public static void main(String[] args) throws ParserConfigurationException,SAXException,IOException{
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=factory.newDocumentBuilder();
        Document document =builder.parse(new File("C:\\Users\\Григорий\\Documents\\NetBeansProjects\\JavaApplication9\\ConfigSignals.xml"));
        
    }
}
