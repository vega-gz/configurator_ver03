/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author admin
 */
public class CreateFileIO_AILUA {
       public static void main(String[] args) throws IOException { 
    {
        String data = "AI_Struct = { \n" +
         "{Name = , Address = , Comment = , LL = , HL = , UM = , TypeCh = , TypeADC =, AdrCh =}" +
         "\n}";
        FileOutputStream out = new FileOutputStream("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\IO_XLS\\GPA\\IO_AI.lua");
        out.write(data.getBytes());
        out.close();
    }
       }
     void writeData(ArrayList<String[]> arg) throws IOException {
       Iterator<String[]> iter_arg = arg.iterator();
       String data  = "AI_Struct = { \n";
       int firstS = 0;
       String zapatai = "";
        while (iter_arg.hasNext()) {
            if (firstS == 0){
            zapatai += "";
            firstS =1;}
            else zapatai = ",";
            String[] tmpS = iter_arg.next();
        data += zapatai + "{Name = '" +tmpS[0] +"', Address = '" +tmpS[1] +"', Comment = '" +tmpS[2] +
        "', LL = '" +tmpS[3] +"', HL = '" +tmpS[4] +"', UM = '" +tmpS[5] +"', TypeCh = '" +tmpS[6] +
                "', TypeADC = '" +tmpS[7] +"', AdrCh = '" +tmpS[8] + ":" + tmpS[9]+ "', uuid_plc = '" + tmpS[10] +
                "', uuid_hmi = '" + tmpS[11] + "', uuid_drv = '" + tmpS[12] + "', uuid_trend = '" + tmpS[13] + "'" + "}";
         data += "\n";
        }
        data += "}";
        FileOutputStream out = new FileOutputStream("C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\IO_XLS\\GPA\\IO_AI.lua");
        out.write(data.getBytes("Cp1251"));
        out.close();
       
     }
     void writeData(ArrayList<String[]> arg, String patchfile) throws IOException {
       Iterator<String[]> iter_arg = arg.iterator();
       String data  = "AI_Struct = { \n";
       int firstS = 0;
       String zapatai = "";
        while (iter_arg.hasNext()) {
            if (firstS == 0){
            zapatai += "";
            firstS =1;}
            else zapatai = ",";
            String[] tmpS = iter_arg.next();
        data += zapatai + "{Name = '" +tmpS[0] +"', Address = '" +tmpS[1] +"', Comment = '" +tmpS[2] +
        "', LL = '" +tmpS[3] +"', HL = '" +tmpS[4] +"', UM = '" +tmpS[5] +"', TypeCh = '" +tmpS[6] +
                "', TypeADC = '" +tmpS[7] +"', AdrCh = '" +tmpS[8] + ":" + tmpS[9]+ "', uuid_plc = '" + tmpS[10] +
                "', uuid_hmi = '" + tmpS[11] + "', uuid_drv = '" + tmpS[12] + "', uuid_trend = '" + tmpS[13] + "'" + "}";
         data += "\n";
        }
        data += "}";
        System.out.println(patchfile);
        FileOutputStream out = new FileOutputStream(patchfile);
        out.write(data.getBytes("Cp1251"));
        out.close();
       
     }
}

