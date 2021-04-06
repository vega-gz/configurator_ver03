/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.ArrayList;

/**
 *
 * @author cherepanov
 */
public class Utilities {
    
    public static  void DeleteEmptyString(ArrayList<String[]>tableData){
        for(int i=0;i<tableData.size();){
            String[] tmRow= tableData.get(i);
            if(tmRow[2].equals("")&&tmRow[3].equals("")){
                tableData.remove(i);
              //  System.out.println("Удалили строку с "+ tmRow[1]);
            }else{
                if (tableData.get(i)[2].equals("")) {
                    tableData.get(i)[2] = tableData.get(i)[0];
                }
                if (tableData.get(i)[3].equals("")) {
                    tableData.get(i)[3] = tableData.get(i)[1];
                }
                i++;
            }
        }
        
    }
    //метод который создает массив из строк в которых была замена
    
}
