/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools;

import Tools.FileManager;
import User.Users;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author nazarov
 * Реализация записи действий данных в файл
 * 
 */
public class LoggerFile implements LoggerInterface {

    private String logFile = "configurer.log";
    
    @Override
    public void writeLog(String str) {
        
        String nameUser = Users.getInstance().getCurentUser();
        if(nameUser != null){
            FileManager.writeLogToFile(nameUser + "\t" + str);
        }else{
            System.out.println("Error acces, user null!!!");
            FileManager.writeLogToFile("User null(contact programmist)" + "\t" + str);
        }
    }

    @Override
    public List<String> readLog() {
        List<String> allStringLogFile = new LinkedList<>();
        InputStreamReader inputsream = null;
        try {
            String fPath = logFile;
            File logF = new File(logFile);
            if (!logF.exists()) {
                writeLog("Лог файла не было. Cоздан.");
            }
            inputsream = new InputStreamReader(new FileInputStream(fPath), "UTF8");
            BufferedReader bufReader = new BufferedReader(inputsream);
            char[] buffer = new char[8096];
            int numberOfCharsRead = bufReader.read(buffer); 
            while (numberOfCharsRead != -1) {
                String strTmp = String.valueOf(buffer, 0, numberOfCharsRead);
                String[] dataFromLogFileTmp = strTmp.split("\n");

                for(String s: dataFromLogFileTmp){
                    allStringLogFile.add(s);
                }
                numberOfCharsRead = inputsream.read(buffer);
            }
            bufReader.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoggerFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoggerFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoggerFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputsream.close();
            } catch (IOException ex) {
                Logger.getLogger(LoggerFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return allStringLogFile;
    }
    
    public static void main(String[] args){
    LoggerInterface testLoggerFile = new LoggerFile();
    for(String s: testLoggerFile.readLog()){
        System.out.println(s);
    }
    }
}
