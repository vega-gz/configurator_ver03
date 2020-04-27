/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalData;

import DataBaseConnect.DataBase;
import StringTools.StrTools;
import XMLTools.XMLSAX;
import fileTools.FileManager;
import java.util.Date;
import org.w3c.dom.Node;

/**
 *
 * @author lev
 */
public class globVar {
        public static String logFile = "configurer.log";
        public static String PathToProject = "";
        public static String currentBase = "";
	public static String linSep;
	public static String myDir;
	public static String desDir;
	public static FileManager fm;
	public static String backupDir;
	public static boolean EOF;
	//public static LineList ll = null;
	public static StrTools st;
        public static int simbolWidth = 210;
        public static int simbolHeight = 600;
        public static int vertGap = 200;
        public static int skale = 400; //масштаб в %
        public static int skale10 = 4000; //масштаб в %
        
        public static int logicBlockWidth = 10; //ширина логических блоков
	
	static String stUuid;
	static long dinUuid = 0;
        
        public static XMLSAX sax = null;
        public static Node cfgRoot = null;
	public static DataBase DB = null;
                
	public static void SetStatUUID() {
		Date date = new Date();
		stUuid = String.format("%020X", date.getTime());
	}
	
	public static String GetUUID() {
		return stUuid + String.format("%012X", dinUuid);
	}
	
	public static String GetNewUUID() {
		dinUuid ++;
		return stUuid + String.format("%012X", dinUuid);
	}

}