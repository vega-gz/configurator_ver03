/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;

public class WriteConfigFile {

    private static final String FILENAME = "Config.txt";

    void write(String pass, String url, String nameProject) throws IOException {
        String PASS, URL, namePro;
        PASS = pass;
        URL = url;
        namePro = nameProject;
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + FILENAME);
            if(!file.exists())
                file.createNewFile();
            
            FileWriter fw;
            fw=new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("Адрес базы данных: "+URL+" Пароль: "+PASS+" имя программы:"+namePro);//разобраться с переносом строки
//            bw.write(PASS);
//            bw.write(namePro);
            bw.close();
            
            System.out.println("Запись завершена");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
