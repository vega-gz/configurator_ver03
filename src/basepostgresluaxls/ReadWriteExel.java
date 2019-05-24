/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import org.apache.poi.hssf.extractor.ExcelExtractor;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;

public class ReadWriteExel {
    private String patch_file = "C:/Users/Nazarov/Desktop/Info_script_file_work/"
       + "_actual_config/Config/Design/IO_XLS/GPA/IO_Арская_КМЧ ГПА_v02.xls";
     public static void main(String[] args) throws IOException{
        ReadWriteExel test = new ReadWriteExel();
        //test.readAllfile();
    }
     //C:\Users\Nazarov\Desktop\Info_script_file_work\_actual_config\Config\Design\IO_XLS\GPA
        
    // String patch_file = "C:/Users/admin/Desktop/Info_script_file_work/"
     //    + "_actual_config/Config/Design/IO_XLS/GPA/test_01.xls";
     void setPatchF(String patch_file){
     this.patch_file = patch_file;}
        
    public void readAllfile() throws IOException {
        
        

        // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(patch_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        
         ArrayList<String> list_sheet = new ArrayList<String>();
         
        // проганаяем список Листов в файле 
         Iterator<Sheet> it_sheet = wb.iterator();
         int tmp = 0;
         while (it_sheet.hasNext()) {
         Sheet sheet = it_sheet.next();
         System.out.println(sheet.getSheetName());
         tmp++;
         list_sheet.add(sheet.getSheetName());
                 }
         
        Iterator<String> iter_list_sheet = list_sheet.iterator();
        while(iter_list_sheet.hasNext()){
            String name_sheet = iter_list_sheet.next();
             System.out.println(name_sheet);
        Sheet sheet = wb.getSheet(name_sheet);
              
        //Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator(); // итератор Строк
        int len_row = 0;
        int max_len_row = 0;
        while (it.hasNext()) {
            Row row = it.next();
            len_row = row.getLastCellNum();
            if (len_row > max_len_row){
            max_len_row = len_row; }
           
             
            Iterator<Cell> cells = row.iterator(); // итератор Ячеек
            while (cells.hasNext()) {
                Cell cell = cells.next();
                  
                CellType cellType = cell.getCellType();
                
                switch (cellType){
                    case STRING : System.out.print(cell.getStringCellValue());
                        break;
                    case BLANK : System.out.print(cell.getColumnIndex());
                        break;
                    case NUMERIC : System.out.print(cell.getNumericCellValue());
                        break;
                    case FORMULA : System.out.print(cell.getCellFormula());
                        break;
                    default: System.out.print("|");
                }
                
                
                       
               //System.out.print("[" + cell.getAddress()+ " " + cell.getCellType()+ "]");
            } 
            
           System.out.println();
                     
    }
        String unicId = getUIID();
        System.out.println("Max_len_in_string_row " + Integer.toString(max_len_row) + " Unic ID " + getUIID());
        }

    }
    
        String getUIID(){
            UUID uniqueKey = UUID.randomUUID();
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается

            //String uiid_str = uniqueKey.toString().replace("-", "") + formatForDateNow.format(dateNow).toString();
            String uiid_str = uniqueKey.toString().replace("-", "");
            return uiid_str;}
      
        // --------- Max lengh string -------------
      int getMaxcColumn (String name_sheet) throws FileNotFoundException, IOException{

              // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(patch_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);   

        System.out.println(name_sheet);
        Sheet sheet = wb.getSheet(name_sheet);
        Iterator<Row> it = sheet.iterator(); // итератор Строк
        int max_len_row = 0;
        while (it.hasNext()) {
            Row row = it.next();
            int len_row = row.getLastCellNum();
            if (len_row > max_len_row){
            max_len_row = len_row; }
        }
        
  
        return max_len_row;
      }
      
    ArrayList<String>  get_list_sheet() throws FileNotFoundException, IOException{
                // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(patch_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        
         ArrayList<String> list_sheet = new ArrayList<String>();
         
        // проганаяем список Листов в файле 
         Iterator<Sheet> it_sheet = wb.iterator();
         while (it_sheet.hasNext()) {
         Sheet sheet = it_sheet.next();
         list_sheet.add(sheet.getSheetName());
                 }
    return list_sheet;} 
    
    // --- Geting data from file Exel ----
   ArrayList<String[]> getDataCell(String name_sheet, int lenmass)throws FileNotFoundException, IOException{
     String[] array_cell_len = new String[lenmass + 1] ;
     ArrayList<String[]> array_cell = new ArrayList<>();
     
     FileInputStream inputStream = new FileInputStream(new File(patch_file));
     HSSFWorkbook wb = new HSSFWorkbook(inputStream);
     Sheet sheet = wb.getSheet(name_sheet);
     
      
        int first_len =0;
        int tmpFirstLenght =0;
        int col_UUID =0;
        int startm = 1;
        switch (name_sheet){ // Высчитываем с какой строки заполнять таблицу и 4 UUID - 3 доп
            case "AI1":
            case "AO1":
            case "DI1":
            //case "DO1": // Что то у нас с этой Книгой в файле
            {
                        first_len = 3; // с 4 строки файла
                        col_UUID = 3; // 4 UUID
                        startm = 4; // это моя идиотия тут из за того что я решил тут вносить UUID (переделать)

                    }
                        break;
                        default: {
                            first_len = 0;
                            startm =1;}
                }
        Iterator<Row> it = sheet.iterator(); // итератор Строк
        int sum_sheet = 0;
        int len_row = 0;
        int max_len_row = 0;

            
        while (it.hasNext()) { 
            while (   tmpFirstLenght < first_len){
                it.next();
                ++tmpFirstLenght;
            }
            //++sum_sheet;
            Row row = it.next();
           // System.out.println(row.getFirstCellNum() + " " + row.getLastCellNum()); //в строку что бы посмотреть что за нах
            int tmp =0;
            //заносим Кол UUID
            int tmp_UUID =0;
            do {
                array_cell_len[tmp]= getUIID();
               tmp_UUID++;
               tmp++;
                }
            while(tmp_UUID <= col_UUID);
            
            //array_cell_len[tmp]= getUIID(); // так было до For
            
            // System.out.println(array_cell_len[tmp]);
           // tmp++;
            //System.out.println(row.getLastCellNum());
            System.out.println(row.getCell(16));
            
            Iterator<Cell> cells = row.cellIterator(); // итератор Ячеек вот не работает должным образом пропускает ячейки
            //boolean void_cell = cells.hasNext(); 
            while (cells.hasNext()) {
                Cell cell = cells.next();
                
                //System.out.println(cell.getAddress());
               /* CellAddress cellReference = new CellAddress("Q110");
                if (cell.getAddress().equals(cellReference)){
                    System.out.println(cell.getAddress());
                    System.out.println(cell.getCellType());
                }*/
                //System.out.println(cell.getAddress()); // Для проверки сдвига
                
                CellType cellType = cell.getCellType();                
            switch (cellType){
                    case STRING :{
                        if (cell.getStringCellValue().contains("'")){
                            //System.out.print("Find ' ->  " + cell.getStringCellValue());
                            array_cell_len[tmp] = cell.getStringCellValue().replaceAll("'", "");
                        }
                        array_cell_len[tmp]= cell.getStringCellValue();  // // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                        }
                        break;
                    case BLANK : array_cell_len[tmp]="NULL";
                        break;
                    case NUMERIC :array_cell_len[tmp]= Double.toString(cell.getNumericCellValue()); // Double
                        break;
                    //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                        case FORMULA : 
                                       // System.out.println("Formula");
                         switch(cell.getCachedFormulaResultType()) {
                          case NUMERIC:
                          array_cell_len[tmp]=(Double.toString(cell.getNumericCellValue()));
                          break;
                         case STRING:
                            array_cell_len[tmp]=cell.getRichStringCellValue().toString();
                            array_cell_len[tmp]=array_cell_len[tmp].replaceAll("'", ""); // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                         //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                         break;
                     }
                        break;
                    default:  array_cell_len[tmp]= "|"; break;
                }
                
                tmp++;
            }
            
             for (int i=0; i < array_cell_len.length; i++ )
            {
           // System.out.print(array_cell_len[i] + " " );
            }
           //System.out.println();
         
           // ПРоверяемс считались какие то данные из ячеек строки (1 так как первый элемент занят ID) 
           //Желательно переписать
           int not_null_dat = 0;
            for (int i=1; i < array_cell_len.length; i++ )
            {
            if (array_cell_len[i].isEmpty()){continue;}
            else {
            not_null_dat = 1;
            break;
            }
            }  
            if (not_null_dat != 0){
                String[] tmp_array_cell_len = Arrays.copyOf(array_cell_len, array_cell_len.length);
                
                // Проверяем пустой ли массив который мы заносим, так как Exel думает что есть данные
                
                boolean empty = true;
              if(tmp_array_cell_len.length != 0){    //массив может быть пустой
                for (int i=startm; i<tmp_array_cell_len.length; i++) {
                 //if (!tmp_array_cell_len[i].equals("NULL") |  tmp_array_cell_len[i] != null) {
                 if (tmp_array_cell_len[i] == null || tmp_array_cell_len[i].equals("NULL") || tmp_array_cell_len[i].equals("")) { 
                 empty = true;  
                 //System.out.println("This find => " + tmp_array_cell_len[i]);
                 } 
                 else{
                    // System.out.println("This Else => " + tmp_array_cell_len[i]);
                       empty = false;
                       break;
                 }
                     }   
              }
                 if (!empty){array_cell.add(tmp_array_cell_len);} // не пусто тогда заносим.
                 //array_cell.add(tmp_array_cell_len);
                not_null_dat = 0;
            }
           //обнуляем массив для проверки выше если строки программа видит но они пустые.
           // array_cell_len = null;
            for (int i=0; i < array_cell_len.length; i++ )
            {array_cell_len[i] = "";}
     }

       //System.out.println( sum_sheet);
        System.out.println(array_cell.size() + " -number string in mass");
   return array_cell;
}
   
   
    // --- Geting data from file Exel formating name table----
   ArrayList<String> getDataNameTable(String name_sheet)throws FileNotFoundException, IOException{
     ArrayList<String> array_cell = new ArrayList<>();
     final String[] MASSCELLID = {"A2","B2","C2","D2","E2","F2","G3","H3","I3","J2","L3","M3","N3","O2","P2","Q2","R3"}; // а вот это пропуск в файле ,"D2" но нужно название
     FileInputStream inputStream = new FileInputStream(new File(patch_file));
     HSSFWorkbook wb = new HSSFWorkbook(inputStream);
     Sheet sheet = wb.getSheet(name_sheet);
              
     switch (name_sheet){
            case "AI1": 
           // case "AO1":
           // case "DO1" :
            //case "DI1" : 
            {
                    //Надо делать вот такие таблицы
                        for ( int i=0; i < MASSCELLID.length; i++) {
                        
                CellReference cellReference = new CellReference(MASSCELLID[i]);
                Row row = sheet.getRow(cellReference.getRow());
                Cell cell = row.getCell(cellReference.getCol());
                CellType cellType = cell.getCellType();                
            switch (cellType){
                    case STRING :{
                        array_cell.add(cell.getStringCellValue());
                    }
                        break;
                    case BLANK : array_cell.add("Null" + Integer.toString(i));                   
                        break;
                    case NUMERIC :array_cell.add(Double.toString(cell.getNumericCellValue())); // Double
                        break;
                    //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                        case FORMULA : 
                        switch(cell.getCachedFormulaResultType()) {
                         case NUMERIC:
                          array_cell.add(Double.toString(cell.getNumericCellValue()));
                         break;
                         case STRING:
                            array_cell.add(cell.getRichStringCellValue().toString());
                         //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                         break;
                     }
                        break;
                    default:  array_cell.add( "|");
                }
                        
                    }
                         
                       
            }
            break;
            default:  ; //System.out.println("No name Sheet");
                                
     }          
   return array_cell;
}
}