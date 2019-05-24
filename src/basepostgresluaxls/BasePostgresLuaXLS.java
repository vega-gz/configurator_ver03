/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

/**
 *
 * @author admin
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BasePostgresLuaXLS {

    
        Statement stmt;
       Connection connection = null;
       private ArrayList<String[]>  currentSelectTable;
       private String[] columns;

    /**
     * @param args the command line arguments
     */
      
        void connectionToBase(){
               //  Database credentials
       final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/test08_DB";
       //final String USER = "postgres";
       //final String PASS = "232345#";
       final String USER = "test08_DB";
       final String PASS = "test08_DB";


              
       System.out.println("Testing connection to PostgreSQL JDBC");
 
	try {
		Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
		e.printStackTrace();
		return;
	}
                        System.out.println("PostgreSQL JDBC Driver successfully connected");
	try {
		connection = DriverManager
		.getConnection(DB_URL, USER, PASS);
  
	} catch (SQLException e) {
		System.out.println("Connection Failed");
		e.printStackTrace();
		return;
	}
        }
        
        
           //-------------- CREATE TABLE ---------------
     void createTable(){
        try {
          
		connection.setAutoCommit(false);
                String sql;
                stmt = connection.createStatement();
        sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        VARCHAR(50), " +
                " SALARY         REAL)";
        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
        System.out.println("-- Table created successfully");
        
	} catch (SQLException e) {
		System.out.println("Failed CREATE TABLE");
		e.printStackTrace();
		return;
	
        }
     }
     void createTable(String name_table, int number_colum, ArrayList<String> listNameColum){
         //переменная для анализа
         String nameTbanalise = new String(name_table);
         String nc_stringing = "";
         //Заменяем символы так как ограничения в Postgrese
         name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_");
         
         switch(name_table){
            case "AI1":
            case "AO1":
            case "DI1":
            case "DO1":
            {
             int tmp_cell = 0;
             number_colum = number_colum -3; // так как добавили Еще UUID
             // -- create table max lengt + name table from file cells
                nc_stringing = " (UUID_PLC TEXT NOT NULL, UUID_HMI TEXT NOT NULL, UUID_DRV TEXT NOT NULL, UUID_Trend TEXT NOT NULL";
                Iterator<String> iter_list_table = listNameColum.iterator();
                  while (iter_list_table.hasNext()) {
                      tmp_cell++;
                      String bufer_named = iter_list_table.next().replace("/", "_"); 
                      nc_stringing += " ," + "\"" + bufer_named +  "\"" +  "      TEXT    NOT NULL";
                  }
                for (int i=tmp_cell+1; i < number_colum + 1; i++){
                    nc_stringing += " ," + "colum_" + Integer.toString(i) +  "      TEXT    NOT NULL";
                }
                nc_stringing += ")";
            }
            break; //до этого до скобки
             default:
         if (!listNameColum.isEmpty()){
             int tmp_cell = 0;
             // -- create table max lengt + name table from file cells
                nc_stringing = " (ID TEXT NOT NULL";
                Iterator<String> iter_list_table = listNameColum.iterator();
                  while (iter_list_table.hasNext()) {
                      tmp_cell++;
                      String bufer_named = iter_list_table.next().replace("/", "_"); 
                      nc_stringing += " ," + "\"" + bufer_named +  "\"" +  "      TEXT    NOT NULL";
                  }
                for (int i=tmp_cell+1; i < number_colum + 1; i++){
                    nc_stringing += " ," + "colum_" + Integer.toString(i) +  "      TEXT    NOT NULL";
                }
                nc_stringing += ")";
                //System.out.print(nc_stringing);
            
         }
         else {
                         // -- create table max lengt row in sheet
                nc_stringing = " (ID TEXT NOT NULL";
                for (int i=1; i < number_colum + 1; i++){
                    nc_stringing += " ," + "colum_" + Integer.toString(i) +  "      TEXT    NOT NULL";
                }
                nc_stringing += ")";
         }
             break;
}


        try {
         
		connection.setAutoCommit(false);
                String sql;
                stmt = connection.createStatement();
                sql = "CREATE TABLE " + name_table + nc_stringing;

        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
        System.out.println("-- Table created successfully");
        
	} catch (SQLException e) {
		System.out.println("Failed CREATE TABLE");
		e.printStackTrace();
		return;
	
        }
     }
         void insertRows(){
         try {
                //--------------- INSERT ROWS ---------------
             String sql;
        stmt = connection.createStatement();
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);
 
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (2, 'Allen', 25, 'Mazafaker' , 15000.00 );";
        stmt.executeUpdate(sql);
 
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);
 
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);
                sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS) VALUES (5, 'Mark', 25, 'Rich-Mond ');";
        stmt.executeUpdate(sql);
 
        stmt.close();
        connection.commit();
        System.out.println("-- Records created successfully");
         }
          catch (SQLException e) {
		System.out.println("Failed ADD data");
		e.printStackTrace();
		return;
                	
        }
         }
         
         
                  void insertRows(String name_table, String[] rows, ArrayList<String> listNameColum){
                       String nameTbanalise = new String(name_table);
                       String sql = "";
         try {
            name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_");
                //--------------- INSERT ROWS ---------------
             switch(name_table){
            case "AI1":
            case "AO1":
            case "DI1":
            case "DO1":
            {
                
                 sql = "INSERT INTO " +name_table+
                 " (UUID_PLC , UUID_HMI , UUID_DRV ,UUID_Trend"; 
            
             int tmp_cell = 0;

                Iterator<String> iter_list_table = listNameColum.iterator();
                  while (iter_list_table.hasNext()) {
                      tmp_cell++;
                      String bufer_named = iter_list_table.next().replace("/", "_"); 
                      sql += " ," + "\"" + bufer_named +  "\"" ;
                  }
                  for ( int i=tmp_cell; i < rows.length-1-3; i++){ // -3 так как опять UUID в AI1
            sql += ",colum_" + Integer.toString(i+1);}// +1 что бы соответствовать нумерации из файла Exel
                  sql += ") VALUES (";
                for ( int i=0; i < rows.length; i++){
                if (i+1 == rows.length ){sql += "'" + rows[i] + "'" ;} // не нравится точка похоже в данных как то надо обходить(похоже)
                else{
            sql += "'" + rows[i] + "'" + ", ";}
            }
            sql += ");";
            
                
            }
            break;
            default: 
            {
           sql = "INSERT INTO " +name_table+ " (ID"; 
            
             if (!listNameColum.isEmpty()){
             int tmp_cell = 0;

                Iterator<String> iter_list_table = listNameColum.iterator();
                  while (iter_list_table.hasNext()) {
                      tmp_cell++;
                      String bufer_named = iter_list_table.next().replace("/", "_"); 
                      sql += " ," + "\"" + bufer_named +  "\"" ;
                  }
                  for ( int i=tmp_cell; i < rows.length-1; i++){
            sql += ",colum_" + Integer.toString(i+1);}// +1 что бы соответствовать нумерации из файла Exel
                  sql += ") VALUES (";
                for ( int i=0; i < rows.length; i++){
                if (i+1 == rows.length ){sql += "'" + rows[i] + "'" ;} // не нравится точка похоже в данных как то надо обходить(похоже)
                else{
            sql += "'" + rows[i] + "'" + ", ";}
            }
            sql += ");";
            
         }
         else {
            for ( int i=0; i < rows.length-1; i++){
            sql += ",colum_" + Integer.toString(i+1);}// +1 что бы соответствовать нумерации из файла Exel
            sql += ") VALUES (";
            for ( int i=0; i < rows.length; i++){
                if (i+1 == rows.length ){sql += "'" + rows[i] + "'" ;} // не нравится точка похоже в данных как то надо обходить
                else{
            sql += "'" + rows[i] + "'" + ", ";}
            }
            sql += ");";
             }
                    break;
            }
             }
            //System.out.println(sql); // Если надо смотрим что за sql запрос
        stmt = connection.createStatement();
        stmt.executeUpdate(sql); 
        stmt.close();
        connection.commit();
        //System.out.println("-- Records created successfully");
         }
          catch (SQLException e) {
		System.out.println("Failed ADD data");
		e.printStackTrace();
		return;	
        }
         }
          //--------------- SELECT DATA --------------
          ArrayList<String[]> selectData(String table){
             ArrayList<String[]> selectData = new ArrayList<>();
             try {        
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM " + table +";" );
        while ( rs.next() ) {
            String id = rs.getString("Colum_18");
            String  name = rs.getString("Канал");
            String age  = rs.getString("Наименование сигнала");
            String  address = rs.getString("мин");
            String salary = rs.getString("макс");
            String UM = rs.getString("ед.изм");
            String TypeCh  = rs.getString("Тип");
            String TypeADC  = rs.getString("Colum_19");
            String addres1  = rs.getString("2.0");
            String addres2  = rs.getString("3.0");
            String uuid_hmi = rs.getString("uuid_hmi");
            String uuid_plc = rs.getString("uuid_plc");
            String uuid_drv = rs.getString("uuid_drv");
            String uuid_trend = rs.getString("uuid_trend");
            String[] strfromtb = {id,name, age,address,salary,UM,TypeCh,TypeADC,addres1,addres2,uuid_hmi,uuid_plc,uuid_drv,uuid_trend};
            selectData.add(strfromtb);
            System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s UM=%s TYPEADC=%s A1=%s A2=%s" ,id,name,age,address,salary,UM,TypeCh,TypeADC,addres1,addres2));
        }
        rs.close();
        stmt.close();
        //connection.commit();
        System.out.println("-- Operation SELECT done successfully");
             }
              catch (SQLException e) {
		System.out.println("Failed ADD data");
		e.printStackTrace();
		//return;
                	
        }
             return selectData;
          }
          
          //--------------- SELECT DATA sum columns --------------
          void selectData(String table, String[] columns){
             StructSelectData.setColumns(columns);
             ArrayList<String[]> selectData = new ArrayList<>();
             String s_columns = "";
             String[] strfromtb = new String[columns.length]; // массив под данные
             for(int i=0; i<columns.length; ++i){ //формирование строки запроса
                 if (i < columns.length-1){
                s_columns +=columns[i]+", "; }
                 else s_columns +=columns[i];
             }
             try {        
        stmt = connection.createStatement();
        
       // System.out.println("SELECT " +s_columns+ " FROM " + table +";" );
        
        ResultSet rs = stmt.executeQuery( "SELECT " +s_columns+ " FROM " + table +";" );
        
        while ( rs.next() ) {
            for (int i=0; i<columns.length; ++i){
            strfromtb[i] = rs.getString(columns[i]);
                    }
            String[] tmp1 = Arrays.copyOf(strfromtb, strfromtb.length); // необходимость из за ссылки

            selectData.add(tmp1);
            //System.out.println(strfromtb[0]); // это просто для тестов
        }
        rs.close();
        stmt.close();
        StructSelectData.setcurrentSelectTable(selectData); // Вносим данные в структуру
        //connection.commit();
        //System.out.println("-- Operation SELECT done successfully");
             }
              catch (SQLException e) {
		System.out.println("Failed ADD data");
		e.printStackTrace();                	
        }
             //return currentSelectTable;
          }
                                 //--------------- SELECT DATA to CreateTGPAAI--------------
          ArrayList<String[]> selectDataGPAAI(String table){
             ArrayList<String[]> selectData = new ArrayList<>();
             try {        
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM " + table +";" );
        while ( rs.next() ) {
            String TypeADC  = rs.getString("Colum_18");
            String id = rs.getString("uuid_plc");
            String nsign  = rs.getString("Наименование сигнала");

            String[] strfromtb = {TypeADC,id, nsign};
            selectData.add(strfromtb);
            //System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s UM=%s TYPEADC=%s A1=%s A2=%s" ,id,name,age,address,salary,UM,TypeCh,TypeADC,addres1,addres2));
        }
        rs.close();
        stmt.close();
        //connection.commit();
        System.out.println("-- Operation SELECT done successfully");
             }
              catch (SQLException e) {
		System.out.println("Failed ADD data");
		e.printStackTrace();
		//return;
                	
        }
             return selectData;
          }
          
           void dropTable(ArrayList<String> listT){
         //-------------- DROPE TABLE ---------------
            Iterator<String> iter_list_table = listT.iterator();
        try {
          
		connection.setAutoCommit(false);
                String sql;
                stmt = connection.createStatement();
                
                while (iter_list_table.hasNext()) {
                sql = "DROP TABLE " + iter_list_table.next() + ";";
                stmt.executeUpdate(sql);
                }
        stmt.close();
        connection.commit();
        System.out.println("-- Table DROPE successfully");
        
	} catch (SQLException e) {
		System.out.println("Failed DROPE TABLE");
		e.printStackTrace();
		return;
	
        }
           }
           
            ArrayList<String>  getviewTable(String ndbase){
                  //-------------- DROPE TABLE ---------------
             ArrayList<String> list_table_base = new ArrayList();
       
     
        try {
        stmt = connection.createStatement();
        // Показывает все таблицы =( и из основной и из тестовой
        ResultSet rs = stmt.executeQuery( "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';" );
        while ( rs.next() ) {
            String  table_schema = rs.getString("tablename");
            list_table_base.add(table_schema);
            //System.out.println(table_schema);
        }
        rs.close();
        stmt.close();
       // connection.commit();
        System.out.println("-- SELECT TABLE BASE");
             }
              catch (SQLException e) {
		System.out.println("Failed SELECT TABLE BASE");
		e.printStackTrace();
		                	
        }
        return list_table_base;
           }
    public static void main(String[] args) {
//        Main startProgramm = new Main();

  }
    
    String[] getColumns(){
    return columns;
    }
    
    ArrayList<String[]> getcurrentSelectTable(){
    return currentSelectTable;
    }
}