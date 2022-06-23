/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools;

import XMLTools.XMLSAX;
import globalData.globVar;
import java.util.ArrayList;
import org.w3c.dom.Node;

/**
 *
 * @author nazarov
 */
public class EditorConfigMain {
    private static String mainConf = "Config.xml";
    private static String nodeDBid = "BASE";
    private XMLSAX sax = null;
    private Node root = null;
    
    public EditorConfigMain(){
        sax = new XMLSAX();
        root = sax.readDocument(mainConf);
    }
    
    public String getNameFileConfig(){
        return mainConf;
    }
    
    
    private void changeNodeContent(String nodeName, int i){
        // смена мест нод адресов сервера или название баз
        Node old = sax.returnFirstFinedNode(root, nodeName);
        Node nw = sax.returnFirstFinedNode(root, nodeName+i);
        String tmp = old.getTextContent();
        old.setTextContent(nw.getTextContent());
        nw.setTextContent(tmp);
        
        sax.writeDocument();
    }
    
    public void changeURL(int number){
        changeNodeContent("URL", number);
    }
    
    public void changeBase(int number){
        changeNodeContent("BASE", number);
    }
    
    public void changeUSERDB(String USERDB){
        Node _NodeUSERDB = sax.returnFirstFinedNode(root, "USERDB");
        _NodeUSERDB.setTextContent(USERDB);
        sax.writeDocument();
    }
    
    public void changePASSDB(String PASSDB){
        Node _NodePASSDB = sax.returnFirstFinedNode(root, "PASSDB");
        _NodePASSDB.setTextContent(PASSDB);
        sax.writeDocument();
    }
    
    public int insertNameBaseInConfig(String nameDB)
    {
        // занести базу в список настроек 
        Node tmp = sax.returnFirstFinedNode(root, nodeDBid);
        int sumBase = 0;
        ArrayList<String> aList = new ArrayList<>();
        for (int i = 1; tmp != null; i++) {
            sumBase = i;
            aList.add(tmp.getTextContent());
            tmp = sax.returnFirstFinedNode(root, nodeDBid + i);
        }
        Node Settings = sax.returnFirstFinedNode(root, "Settings");
        Node naodeB = sax.insertChildNode(Settings, nodeDBid + Integer.toString(sumBase));
        naodeB.setTextContent(nameDB); // метод самой ноды
        sax.writeDocument();
        
        return sumBase;
    }
    
    public void changePathDirProject(String newPathDir){
        XMLSAX cfgSax = new XMLSAX();
        Node cfgSaxRoot = cfgSax.readDocument(mainConf);
        Node desDir = cfgSax.returnFirstFinedNode(cfgSaxRoot, "DesignDir");
        desDir.setTextContent(newPathDir);
        cfgSax.writeDocument(mainConf);
    }
}
