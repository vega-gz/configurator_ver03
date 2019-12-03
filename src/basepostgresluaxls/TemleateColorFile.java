/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nazarov
 */
public class TemleateColorFile {
     File realName = new File("TemplateColor.txt"); // Оригинальное имя

    private static List<String> listColor = new ArrayList();
        
     // --- Читаем файл и превращаем в объект строк ---
    static List<String> readF(File file) throws FileNotFoundException, UnsupportedEncodingException{
     // read the content from file
    InputStreamReader inputsream = new InputStreamReader(new FileInputStream(file), "UTF8"); // Правильное отображение русскаого языка чтение буыера такое себе
    try(BufferedReader bufferedReader = new BufferedReader(inputsream)) {
    String line = bufferedReader.readLine();
    while(line != null) {
        listColor.add(line);
        line = bufferedReader.readLine();
    }
    bufferedReader.close();
    } catch (FileNotFoundException e) {
        // exception handling
    } catch (IOException e) {
        // exception handling
    }
        return listColor;
    }
    
    
        
    // --- Пишем в файл ---
    static void writeF(File file, String str) throws IOException{
        //File realName = new File(absolutePath); // Оригинальное имя
        OutputStreamWriter inputsream = new OutputStreamWriter(new FileOutputStream(file), "WINDOWS-1251"); // Правильное отображение русскаого языка чтение буыера такое себе
        BufferedWriter writer = new BufferedWriter(inputsream);        
        //BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));        
        //writer.write(new String(str.getBytes("WINDOWS-1251"))); // это походу переписать
        writer.write(str); // это походу переписать
        //System.out.println(new String(str.getBytes("WINDOWS-1251")));
        writer.write("\n"); // это походу переписать
        writer.close();
    }
}

