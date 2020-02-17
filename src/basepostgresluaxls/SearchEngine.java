/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.xml.sax.SAXException;

/**
 *
 * @author nazarov
 */
public class SearchEngine {
    private String Fname = "";
    
    public SearchEngine(String s) {
        Fname = s;
    }
    
    public static void main(String[] args) throws IOException {
       SearchEngine s = new SearchEngine("C:\\Directory");
       File testF = new File("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\KC_NOVGOROD_GPA_Serv\\KC_NOVGOROD_GPA_Serv\\Design\\");
       s.pathAllFile(testF);
    }

    public void search() throws IOException{
       // Тут создадим временную папку для файлов бекапов
        // максимальное вхождение вложений папок вышло 85 на W7
        String SubDirectory2 = "C:\\Directory" ;
        // но сначала все удалим
        //recursiveDelete(new File(SubDirectory2)); // jxtym медленно удаляло
        //deleteDirectory(new File(SubDirectory2)); // Такое же
        new File(SubDirectory2).mkdir(); 
        int pa = 1000000;
        for (int it=0; it<pa; ++it){ 
            SubDirectory2  += "\\" + Integer.toString(it);
            new File(SubDirectory2).mkdir(); 
            File testF = new File(SubDirectory2 + "\\newfile_test.txt"); //Файл с новой записью
            BufferedWriter writertest = new BufferedWriter(new FileWriter(testF, true));
            for (int j=0; j<pa; ++j){ //тут просто мучаем диск 
               writertest.write(SubDirectory2); // вносим строк с путем до него
            writertest.close();
            }
        }
        System.out.println(SubDirectory2);
        //new File(SubDirectory2).mkdirs();  // так создавать полный путь
        
    }
    
    // -- Рекурсивное удаление---
    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
                System.out.println("Удалено : " + f);
            }
            dir.delete();
        } else dir.delete();
    }
    
        // -- Пути файлов полный путь ---
    public  void pathAllFile(File dir) {
       XMLSAX sax = new XMLSAX(); 
        String pF = "";
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                pathAllFile(f);
            }
            //System.out.println("Директория " +  dir.getAbsolutePath() ); // сообщение о директории прячем
        } else {
            System.out.println("файл " + dir.getAbsolutePath()+ " symbols " + dir.length());
            //if(sax.enumerationData(dir.getAbsolutePath())) break; // так посылаем файл на обработку XML парсеру тут с условием доделаем
            sax.enumerationData(dir.getAbsolutePath()); // так посылаем файл на обработку XML парсеру
          }
    } 
}
