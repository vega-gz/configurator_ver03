/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;

/**
 *
 * @author admin
 */
public class Main{ 
        static    BasePostgresLuaXLS workbase = new BasePostgresLuaXLS();
        static    ReadWriteExel rwexel = new ReadWriteExel();
        static    CreateFileIO_AILUA createF = new CreateFileIO_AILUA();
        static    LuaRun Lrun = new LuaRun();
        static    CreateTGPAAI gpaai = new CreateTGPAAI();
    
    
        Main(){ // Для работы с окнами
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
            int sizeWidth = 800;
            int sizeHeight = 600;
            int locationX = (screenSize.width - sizeWidth) / 2;
            int locationY = (screenSize.height - sizeHeight) / 2;
            Main_JPanel Windo_main = new Main_JPanel();
            JFrame frame = new JFrame();
            frame.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
            frame.setContentPane(Windo_main); // Передаем нашу форму
            frame.setVisible(true);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); // Закрытие приложения но можно более хитро с диалоговыми
            
        }

        public static void main(String[] args) throws IOException {
       
            new Main();
            /*
            BasePostgresLuaXLS workbase = new BasePostgresLuaXLS();
            ReadWriteExel rwexel = new ReadWriteExel();
            CreateFileIO_AILUA createF = new CreateFileIO_AILUA();
            LuaRun Lrun = new LuaRun();
            CreateTGPAAI gpaai = new CreateTGPAAI();
    */
        

            //start.find_null();
    //workbase.connectionToBase();  // вот это весно забываю для конекта с DB 
   // workbase.dropTable();
    //workbase.createTable();
    //workbase.dropTable();
   // workbase.insertRows();
   // workbase.selectData();
   // workbase.getviewTable("test08_DB");

// Create Table
//вот так сработало
/*
int maxlencol = rwexel.getMaxcColumn("Лист13"); //количество столбцов методом вычисления в фукции
for (int i = 0; i < rwexel.getDataCell("Лист13", maxlencol).size(); i++) {
   String[] catName = rwexel.getDataCell("Лист13", maxlencol).get(i);
   System.out.print( catName[0] + " " + catName[1]  + " " + catName[2] );
   System.out.println();
}
  */   
    
 //
    
    //Участок кода формирования баз их заполнения их файла основа работает
    /*
    {
Iterator<String> it_list_sheet = rwexel.get_list_sheet().iterator();
while (it_list_sheet.hasNext()) {
    String name_table = it_list_sheet.next();
    int maxlencol = rwexel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
    ArrayList<String> name_collums = new ArrayList<>(rwexel.getDataNameTable(name_table)); // получаем массив столбцов
  
    workbase.createTable(name_table, maxlencol, name_collums); // передаем название таблицы и максимальное  - создание таблиц
    
    // берем данные с файла
   ArrayList<String[]> sheet_fromsheet_from ;
                sheet_fromsheet_from = new ArrayList<>(rwexel.getDataCell(name_table, maxlencol));
Iterator<String[]> iter_sheet = sheet_fromsheet_from.iterator();
while (iter_sheet.hasNext()) {
        String[] dataFromFile = iter_sheet.next();
          
        if (name_table.equals("AI1")){
   for (int i_1=0; i_1 < dataFromFile.length; i_1++ ){       
                    
                   // System.out.print(dataFromFile[i_1] + " " );
                    
            }
           }

  workbase.insertRows(name_table, dataFromFile, name_collums); //Вносим данные в базу

     } 
}
        }
   
    */
    
    
// test get arralist nabe table AI1
 /* Iterator<String> iter_list_table = rwexel.getDataNameTable("AI1").iterator();
  while (iter_list_table.hasNext()) {
  String name_table = iter_list_table.next();
  System.out.print(name_table + " ");
  }
*/


/*
// select Database test and write to file from Lua
ArrayList<String[]> dataFromDb = new ArrayList<>();
dataFromDb = workbase.selectData("ai1");
createF.writeData(dataFromDb); // write file
*/



// Формируем файлы , это работало
/*
ArrayList<String[]> dataFromDbGPAI = new ArrayList<>();
dataFromDbGPAI = workbase.selectDataGPAAI("ai1");
gpaai.writeData(dataFromDbGPAI, "8sd76fgjhg"); // write file
gpaai.writeListData(15); // write file
*/
    
//test run Lua
//String fileFullName = "C:\\Users\\Nazarov\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\LUA_FILE\\Template\\AI\\test_02.lua";
//Lrun.runL();

        }
        
        static void fillDB(String patch_file)throws IOException{
        //Участок кода формирования баз их заполнения их файла основа работает
        rwexel.setPatchF(patch_file); // изменяем путь файла
  workbase.connectionToBase();
Iterator<String> it_list_sheet = rwexel.get_list_sheet().iterator();
while (it_list_sheet.hasNext()) {
    String name_table = it_list_sheet.next();
    int maxlencol = rwexel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
    ArrayList<String> name_collums = new ArrayList<>(rwexel.getDataNameTable(name_table)); // получаем массив столбцов
  
    workbase.createTable(name_table, maxlencol, name_collums); // передаем название таблицы и максимальное  - создание таблиц
    
    // берем данные с файла
   ArrayList<String[]> sheet_fromsheet_from ;
                sheet_fromsheet_from = new ArrayList<>(rwexel.getDataCell(name_table, maxlencol));
Iterator<String[]> iter_sheet = sheet_fromsheet_from.iterator();
while (iter_sheet.hasNext()) {
        String[] dataFromFile = iter_sheet.next();
          
        if (name_table.equals("AI1")){
   for (int i_1=0; i_1 < dataFromFile.length; i_1++ ){       
                    
                   // System.out.print(dataFromFile[i_1] + " " );
                    
            }
           }

  workbase.insertRows(name_table, dataFromFile, name_collums); //Вносим данные в базу

     } 
}
           }
        
}
       

    

