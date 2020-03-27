/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class WriteXMLsignals {

    String FILENAME = "ConfigSignals.xml";
    File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);//путь к файлу указали в корневую папку

    public static void main(String[] args) throws ParserConfigurationException {
        WriteXMLsignals w = new WriteXMLsignals();
       
    }

    public void writeSignal( String TypeSignal, String nameGPASignal, String nameSignal) throws ParserConfigurationException {
        ArrayList<String[][]> selectSignal = new ArrayList<>();

        String[][] strAI = {
            {"T_GPA_AI_FromHMI", "T_GPA_AI_OnlyToHMI", "T_GPA_AI_Settings", "T_GPA_AI_ToHMI"},
            {"T_AI_FromHMI", "T_AI_OnlyToHMI", "T_AI_Setting", "T_AI_ToHMI"}
        };
        String[][] strAO = {
            {"T_GPA_AO_FromHMI", "T_GPA_AO_ToHMI"},
            {"T_AO_FromHMI", "T_AO_ToHMI"}
        };
        String[][] strDO = {
            {"T_GPA_DO_FromHMI", "T_GPA_DO_ToHMI"},
            {"T_DO_FromHMI", "T_DO_ToHMI"}
        };
        String[][] strDI = {
            {"T_GPA_DI_FromHMI", "T_GPA_DI_Settings", "T_GPA_DI_ToHMI"},
            {"T_DI_FromHMI", "T_DI_Setting", "T_DI_ToHMI"}
        };
        
        selectSignal.add(strAI);
        System.out.println(selectSignal.get(0));
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("ConfigSignals");
            doc.appendChild(rootElement);

            Element settings = doc.createElement(TypeSignal);
            rootElement.appendChild(settings);
            {
                Element T_GPA_AI = doc.createElement(nameGPASignal);
                T_GPA_AI.setAttribute("Type", nameSignal);
                settings.appendChild(T_GPA_AI);
            }
            {

                Element T_GPA_AI = doc.createElement(nameGPASignal);
                T_GPA_AI.setAttribute("Type", "T_AI_Setting");
                settings.appendChild(T_GPA_AI);
            }
            {

                Element T_GPA_AI = doc.createElement(nameGPASignal);
                T_GPA_AI.setAttribute("Type", nameSignal);
                settings.appendChild(T_GPA_AI);
            }
            {

                Element T_GPA_AI = doc.createElement(nameGPASignal);
                T_GPA_AI.setAttribute("Type", nameSignal);
                settings.appendChild(T_GPA_AI);
            }
            {

                Element T_GPA_AI = doc.createElement(nameGPASignal);
                T_GPA_AI.setAttribute("Type", nameSignal);
                settings.appendChild(T_GPA_AI);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//запись xml в виде структуры
            DOMSource sourse = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + File.separator + FILENAME));

              StreamResult result1 =new StreamResult(System.out);
            transformer.transform(sourse, result);
        } catch (TransformerException ex) {
            Logger.getLogger(WriteConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
