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
       File testF = new File("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\Project_from_Lev\\FirstGen\\Design");
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
        //new File(SubDirectory2).mkdirs();  // так зоздавать полный путь
        
    }
    
        // Рекурсивное удаление фалов и каталогов очень медленно
    public static void recursiveDelete(File file) {
        // до конца рекурсивного цикла
        if (!file.exists())
            return;
         
        //если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление всего, что там есть
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                // рекурсивный вызов
                recursiveDelete(f);
            }
        }
        // вызываем метод delete() для удаления файлов и пустых(!) папок
        file.delete();
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }
    
    // -- Еще одно удаление---
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
        // pF = f.getPath();
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                pathAllFile(f);
                //System.out.println("файл : " + f);
            }
            System.out.println("Директория " +  dir.getAbsolutePath() );
        } else {
            System.out.println("файл " + dir.getAbsolutePath()+ " size " + dir.length());
            sax.enumerationData(dir.getAbsolutePath());
          }
    } 
}
