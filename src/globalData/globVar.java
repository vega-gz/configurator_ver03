/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalData;

import DataBaseTools.DataBase;
import Tools.StrTools;
import XMLTools.XMLSAX;
import Tools.FileManager;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import org.w3c.dom.Node;

/**
 *
 * @author lev
 */
public class globVar {
        public static String mainConfSig = "ConfigSignals.xml";
        public static String ConfigHMI = "ConfigHMI.xml";
        public static String logFile = "configurer.log";
        public static String PathToProject = "";
	public static String linSep;
	public static String myDir;//путь до конфигуратора
        public static String desDir;//путь до папки с проектом
        public static String DefaultProjectDir = "DefaultProject"; //путь проекта по умолчанию
	public static FileManager fm;
	public static String backupDir = "bkp"; // папка для бекапов
	public static boolean EOF;
        public static String сonfigAMs = "ConfigAMs.xml"; // название файла конфигурации Исполнительных механихмов
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
        
        public static String abonent = ""; // текущий абонент работы с данными
        public static XMLSAX sax = null; // в генераторе смотрит на ConfigSignals.xml, может переинициализируется еще где то
        public static Node cfgRoot = null;
        
        public static String currentBase = "";
	public static DataBase DB = null;
        public static String dbURL = "";
        public static String USERDB = "";
        public static String PASSDB = "";
        public static String LocalUSER = null;
        public static String LocalPASS = null;
        public static ArrayList<String> processReg = new ArrayList<>();
        
        // Переменные для отклика всех открытых окон в программе 
        public static ArrayList<JFrame> listF = new ArrayList<>(); // Лист с фреймами
        public static boolean captureFocus = true;      //  захват фокуса окна 
        public static boolean windowIconified = false; // триггер определение свернутости окна
        public static int sumFrame = 0;                 // сумма окон для правильного реагирования кликов по ним
        public static String[] namecolumnT = {"Наименование", "TAG_NAME_PLC"};  // Название столбцов для поиска по таблице
        public static String TAGPLC = namecolumnT[1];
        public static String currentSetupsSignal;
        public static String nameTableSignal;
                
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