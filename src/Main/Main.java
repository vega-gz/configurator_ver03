package Main;

import FrameCreate.Main_JPanel;
import Table.TableTools;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.io.File;
import javax.swing.JOptionPane;


public class Main {
    //static DataBase workbase =  DataBase.getInstance();

    public static void main(String[] args) {
        
        XMLSAX.getConnectBaseConfig("Config.xml");
        globVar.myDir = System.getProperty("user.dir");
        globVar.sax = new XMLSAX(); // Класс работы с XML  static что бы не парится
        globVar.cfgRoot = globVar.sax.readDocument(globVar.mainConfSig);
        if(globVar.cfgRoot == null){
            JOptionPane.showMessageDialog(null, "Не удалось прочитать "+globVar.myDir+File.separator+"ConfigSignals.xml");
            return;
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               Main_JPanel mainF = new Main_JPanel();
               TableTools.setFrameListener(mainF, null, null, null); // слушателей фреймов
               mainF.setVisible(true);
            }
        });
    }
}