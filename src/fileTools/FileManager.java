/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileTools;

import globalData.globVar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import main.globVar;
public class FileManager {

    // --- копирование файла используя поток ---
    public void copyFile(String source, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source); // Можно и файл подставлять
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    // -- метод бекапа на основе времени ---
    public void backupFile(String source) throws IOException {
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String currentDat = dateFormat.format(cal.getTime());
        copyFile(source, source + "_" + currentDat);
    }

    
     //public File dir;
     public File wrFile;
     public File rdFile;
     public FileOutputStream fos;
     public FileInputStream  fis;
     public Writer wrStream;
     public Reader rdStream;
     //String buf;

     public int MoveFile(String fileName, String srcDir, String dstDir) {
     File originalFile = new File(srcDir, fileName);
     File newFile 	  = new File(dstDir, fileName);
    	
     if(originalFile.exists() && !newFile.exists()) {
     if(originalFile.renameTo(newFile)) {
     } else {
     return 1;
     }
     }
     return 0;
     }
    
     public int createFile2write(String dirName, String fileName) throws IOException {
    	
     File dir = new File(dirName);
     // если объект представляет каталог
     if(dir.isFile()) return 1; //неправильный путь
     if(dir.isDirectory()) {
     wrFile = new File(dir, fileName);
     if(wrFile.isFile()) {
     wrFile.delete();
     }
     boolean created = wrFile.createNewFile();
     if(!created) return 2; // не удалось создать файл
     }
     fos = new FileOutputStream(wrFile, false);
     wrStream = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
     return 0;
     }
    
     public int openFile4read(String dirName, String fileName, String charSer) throws IOException {
     File dir = new File(dirName);
     // если объект представляет каталог
     if(dir.isFile()) return 1; //неправильный путь
     if(dir.isDirectory()) {
     rdFile = new File(dir, fileName);
     if(!rdFile.isFile()) return 3; // не удалось найти файл
     }
     else return 1; //неправильный путь 
     fis = new FileInputStream(rdFile);
     rdStream = new InputStreamReader(fis, charSer); //cs);
     globVar.EOF = false;
     return 0;
     }
    
     public void wr(String s) throws IOException {
     wrStream.write(s);
     }
    
     public String rd() throws IOException {
     String s = "";
     int ch = 0;
     //while (((char)ch) != '\n' && ch >= 0 ) {
     while (true) {
     try {
     ch = rdStream.read();
     if(!(s.isEmpty() && (ch == 10))) {//ch == 13 || 
     if(((char)ch) == '\n' || ch < 0 || ch == 13 || ch == 10) break;
     s += (char)ch;
     }
     }
     catch(IOException ex){
     System.out.println(ex.getMessage());
     globVar.EOF = true;
     break;
     }
    		
     }
     if(ch == -1) globVar.EOF = true;
     return s;
     }
    
     public int CopyFile(String file1Name, String file2Name, String srcDir, String dstDir, String charSet) throws IOException {
     if ( globVar.fm.openFile4read(srcDir, file1Name, charSet) != 0 ) return 1;
     if ( globVar.fm.createFile2write(dstDir, file2Name) != 0 ) return 1;
     while(!globVar.EOF) {
     String buf = globVar.fm.rd();
     globVar.fm.wr(buf);
     //System.out.println("[" + buf +"]" + globVar.EOF);
     }
     globVar.fm.rdStream.close();
     globVar.fm.wrStream.close();
     return 0;
     }
     
}
