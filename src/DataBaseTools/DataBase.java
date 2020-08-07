package DataBaseTools;


import Tools.FileManager;
import Tools.Observed;
import Tools.Observer;
import Tools.StrTools;
import XMLTools.UUID;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class DataBase implements Observed {
    Statement stmt;
    Connection connection = null;
    
     // данные для таскбара
    int min;
    int max;
    int value;
    
    public int statusConnectDB = -2; // 0 - connect; -1 - failed; -2 - not attempt to connect
    
    List<Observer> observers = new ArrayList<>(); // Список наблюдателей
    
//    // Делаем синглтон(убрал)
//    private static DataBase instance;

    public  DataBase() { // для синглтона в однопоточном варианте
        connectionToBase(globVar.dbURL, globVar.currentBase, globVar.USER, globVar.PASS); // И сразу подключаемся к базе
        
    }
    // подключение к иной базе(для сравнения и слияния)
    public  DataBase(String dbURL, String currentBase,String USER,String PASS) { // для синглтона в однопоточном варианте
        dbURL = "jdbc:postgresql://" + dbURL + "/";
        connectionToBase(dbURL, currentBase, USER, PASS); // И сразу подключаемся к базе
    }

    public boolean isConnectOK(){
        return statusConnectDB == 0;
    }
    // --- Метод подключения к базе ---
    private void connectionToBase(String URL, String DB, String USER, String PASS) {
        if (connection != null){
            try {
                connection.close(); // обязательно выходим перед вызовом так как к многим базам конектимся
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            //e.printStackTrace();
            statusConnectDB = -1;
        }
        
        try {
            connection = DriverManager.getConnection(URL + DB, USER, PASS);
            System.out.println("PostgreSQL JDBC Driver successfully connected" + "Base:" + URL + DB);
            statusConnectDB = 0;
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            FileManager.loggerConstructor("Connection Failed base " + URL + DB);
            //e.printStackTrace();
            statusConnectDB = -1;
        }
        
    }
    
    // --- Получить статус базы ---
    public int getStatusConnect(){
        return statusConnectDB;
    }
    
    // --- получить имя текущей базы ---
    public String getCurrentNameBase(){
        return globVar.currentBase;
    }
    // --- получить имя текущего пользователя ---
     public String getCurrentUser(){
        return globVar.USER;
    }
    
    // не правильно (так данные передавать это боль)
    private String[] getInfoCurrentConnect(){
        String[] infoConnect = {globVar.dbURL,globVar.USER,globVar.PASS};
        return infoConnect;
    }
    
    // Сменить базу
    public int changeBase(String base){
       String[] infoConnect = getInfoCurrentConnect();
       connectionToBase(infoConnect[0],base,infoConnect[1],infoConnect[2]);
       return statusConnectDB;
    }
    
    // -------------- CREATE DATABASE -----------
    void createBase(String name) {
        //connectionToBase(); // вызов Фукция подключения к базе
        String sql = null;
        try {
            //name = name.replace("-", "_").replace(".", "_").replace(" ", "_"); // может понадобиться как в сосздании таблицы
            connection.getAutoCommit();
            connection.setAutoCommit(true);
            stmt = connection.createStatement();
            sql = "CREATE DATABASE  " + name + ";";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            //connection.commit();
            System.out.println("-- Base" + name + " created successfully");

        } catch (SQLException e) {
            System.out.println("Failed CREATE BASE");
            FileManager.loggerConstructor("Failed CREATE BASE sqlRequest- " + sql);
            e.printStackTrace();
            return;

        }
    }
    //-------------- Проверка наличия в БД таблицы с заданным именем -Lev----
    public boolean isTable(String name){
        ArrayList<String> list = getListTable();
        if(list==null || list.isEmpty()) return false;
        for(String s: getListTable()){ // Получаем список таблиц и пробежимся сравнивая их
            if(name.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
    //-------------- CREATE TABLE ---------------
    public void createTable(String name_table,  ArrayList<String> listNameColum, ArrayList<String[]> data, String  comment) {
        if(isTable(name_table)) dropTable(name_table);
        String[] arrNameCol = listNameColum.toArray(new String[listNameColum.size()]);
        createTableEasy(name_table,  arrNameCol, comment);
        for(int i=0; i < data.size(); i++){
            insertRow(name_table, data.get(i), arrNameCol, i+1);
        }
    }
    public void createTable(String name_table,  String[] arrNameCol, String[][] data, String  comment) {
        if(isTable(name_table)) dropTable(name_table);
        createTableEasy(name_table,  arrNameCol, comment);
        for(int i=0; i < data.length; i++){
            insertRow(name_table, data[i], arrNameCol, i+1);
        }
    }
    
    public void createTableEasy(String name_table,  String[] listNameColum,  String  comment) {
        if (name_table.isEmpty() || listNameColum.length == 0 || isTable(name_table)) return; 
        String sql = "";
        try {
            //connection.setAutoCommit(true);
            stmt = connection.createStatement();
            sql = "CREATE TABLE \"" + name_table + "\" (id INTEGER";
            int start = 0;
            if("id".equalsIgnoreCase(listNameColum[0]) || "№".equals(listNameColum[0])) start = 1;
            for (int i = start; i<listNameColum.length;i++ ) sql += ", \"" + listNameColum[i] + "\" TEXT";
            sql += ");";
            
            System.out.println("Easy table " + sql); // смотрим какой запрос на соз
            stmt.executeUpdate(sql);
            stmt.close();
            if(comment!=null && !comment.isEmpty()) createCommentTable(name_table, comment); // вызом метода добавления комментария
        } catch (SQLException e) {
            System.out.println("Failed CREATE TABLE");
            System.out.println(sql); // смотрим какой запрос на соз
            e.printStackTrace();
        }
    }
    
// --- Вставка данных (название таблицы, данные, список столбцов) если нет данных для UUID сам сделает---
    public void insertRowNZ(String name_table, String[] rows, ArrayList<String> listNameColum) {
        int colId = getLastId(name_table) + 1; // Получаем последний id и всегда +1 как инкремент
        String addUUID = "";
        String dataUUID = "";
        // проверка есть ли вообще столбей UUID в таблице
        for(String s:getListColumns(name_table)){
            if(s.equalsIgnoreCase("UUID")){
                addUUID = "\"UUID\", "; // блок определения нужно ли генерировать UUID
                dataUUID = "\'" +UUID.getUIID()+"\'" + ", ";//  генерим ууид прмо тут если его нет в данных для для добавки
            }
        }
        String addId = "\"id\", ";
        String dataId = "\'" +colId+"\'" + ", ";
        // проверка на UUID и id в входящих данных
        for(String s:listNameColum){
            if(s.equalsIgnoreCase("UUID")){ // если нашли что создаем с нужными UUID обнуляем
            addUUID ="";
            dataUUID = "";
            }
            if(s.equalsIgnoreCase("id")){
                addId = "";
                dataId = "";
            }
        }
        
        try {
            connection.setAutoCommit(true);
            String nameTbanalise = new String(name_table);
            String sql = "";
            try {
                //name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", ""); // тут и при создании нужно сделать единый модуль
                name_table = replacedNt(name_table);
                //--------------- INSERT ROWS ---------------
                        if (!listNameColum.isEmpty()) {
                            sql = "INSERT INTO " + "\"" +name_table + "\"" + " ("+addId + addUUID; // при первом проходе иначе будет отличаться данные и столбцы
                            for (int i = 0; i < listNameColum.size(); i++) { // формирую данные для этого запроса - 1 так как добавили ID
                                if (i + 1 >= listNameColum.size()) {
                                    String bufer_named = listNameColum.get(i).replace("/", "_");
                                    sql += "\"" + bufer_named + "\"";
                                } else {
                                    String bufer_named = listNameColum.get(i).replace("/", "_");
                                    sql += "\"" + bufer_named + "\"" + " ,";
                                }
                            }
                            sql += ") VALUES ("+ dataId +dataUUID;
                            // row и listNameColum должны быть одинаковы но косяк
                            for (int i = 0; i < rows.length; i++) { // формирую данные для этого запроса
                                if (i + 1 >= rows.length) {
                                    sql += "'" + rows[i] + "'";
                                } // не нравится точка похоже в данных как то надо обходить(похоже)
                                else {
                                    sql += "'" + rows[i] + "'" + ", ";
                                }
                                //System.out.println(sql);
                            }
                            sql += ");";
                            
                        } else {
                            for (int i = 0; i < rows.length; i++) {
                                sql += ",colum_" + Integer.toString(i + 1);
                            }// +1 что бы соответствовать нумерации из файла Exel
                            sql += ") VALUES (";
                            for (int i = 0; i < rows.length; i++) {
                                if (i + 1 == rows.length) {
                                    sql += "'" + rows[i] + "'";
                                } // не нравится точка похоже в данных как то надо обходить
                                else {
                                    sql += "'" + rows[i] + "'" + ", ";
                                }
                            }
                            sql += ");";
                     
                }
                System.out.println(sql); // Если надо смотрим что за sql запрос
                stmt = connection.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
                //connection.commit();
                //System.out.println("-- Records created successfully");
            } catch (SQLException e) {
                System.out.println("Failed ADD data");
                e.printStackTrace();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
// --- Вставка данных (название таблицы, список столбцов, данные, index) -Lev--
    public void insertRow(String name_table, String[] row, String[] listNameColum, int index) {
        if(name_table.isEmpty() || row.length == 0 || listNameColum.length == 0 || row.length != listNameColum.length) return;
        //connection.setAutoCommit(true);
        String sql;
        try {
            name_table = replacedNt(name_table);
            sql = "INSERT INTO \"" + name_table + "\" (id"; // при первом проходе иначе будет отличаться данные и столбцы
            
            int start = 0;
            if("id".equalsIgnoreCase(listNameColum[0]) || "№".equals(listNameColum[0])) start = 1;
            for (int i = start; i<listNameColum.length;i++ ) sql += ", \"" + listNameColum[i] + "\"";
            if(start == 0){
                sql += ") VALUES ("+index;
                for (String row1 : row) sql += ", '" + row1 + "'";
            }else{
                sql += ") VALUES ("+row[0];
                for(int i = 1; i<row.length; i++)
                    sql += ", '" + row[i] + "'";
            }
            sql += ");";
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed ADD data");
            e.printStackTrace();
        }
    }
     
    // получение таблицы целиком отсортированной по столбцу id
    public ArrayList<String[]> getData(String table) {
        return getData(table, "id");
    }
    // получение таблицы целиком отсортированной по заданному столбцу ---Lev---
    public ArrayList<String[]> getData(String table, String orderCol) {
        ArrayList<String> columns = getListColumns(table);
        return getData(table, columns, " ORDER BY \"" +orderCol +"\"");
    }
    public ArrayList<String[]> getData(String table, String orderCol, String desiredCol, String disiredVal) {
        ArrayList<String> columns = getListColumns(table);
        String where = "";
        if(desiredCol!=null && disiredVal!=null) where = " WHERE \""+desiredCol+"\"='"+disiredVal+"'";
        String order = "";
        if(orderCol!=null) order = " ORDER BY \"" +orderCol +"\"";
        return getData(table, columns, order, where);
    }
    // --- Получить данные из БД по имени таблицы и массиву столбцов ---Lev---
    public ArrayList<String[]> getData(String table, String[] columns) {
        ArrayList<String> al = new ArrayList<>();
        for(String c:columns) al.add(c);
        return getData(table, al);
    }
    // --- Получить данные из БД по имени таблицы и списку столбцов ---Lev---
    public ArrayList<String[]> getData(String table, ArrayList<String> columns) {
        return getData(table, columns, "");
    }
    public ArrayList<String[]> getData(String table, ArrayList<String> columns, String orderCol) {
        return getData(table, columns, orderCol, "");
    }
    public ArrayList<String[]> getData(String table, ArrayList<String> columns, String orderCol, String where) {
        
        int size = columns.size();
        String sql = "SELECT \"" + columns.get(0) + "\"";
        for (int i = 1; i < size; ++i) sql +=  ", \"" + columns.get(i) + "\"";
        sql += " FROM \"" + table +"\""+where+orderCol+";"; 
        
        ArrayList<String[]> selectData = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String[] strFromTb = new String[size]; // массив под данные
                for (int i = 0; i < size; ++i) {
                    strFromTb[i] = rs.getString(columns.get(i));
                    if(strFromTb[i] == null) strFromTb[i] = "";
                }
                selectData.add(strFromTb);
                //System.out.println(strfromtb[0]); // это просто для тестов
            }
            rs.close();
            stmt.close();
            //connection.commit();
            //System.out.println("-- Operation SELECT done successfully");
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        return selectData;
    }
        // ---найти данные по имени столбца и значению ячейки и имени искомого столбца---Lev---
    public String getDataCell(String table, String col1, String val1, String col2) {
        String sql = "SELECT \"" + col2 + "\" FROM \"" + table +"\" WHERE \""+col1+"\"='"+val1+"';";
        String val2=null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            val2 = rs.getString(col2);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске элемента "+val1+" в таблице "+table);
            //e.printStackTrace();
            return null;
        }
        return val2;
    }
    // --- Select columns Table  ---
    public ArrayList<String> getListColumns(String table) {
        ArrayList<String> listColumn = new ArrayList();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = \'" + table + "\';");
            while (rs.next()) {
                listColumn.add(rs.getString("column_name"));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        return listColumn;
    }
    //-------------- Получить список таблиц базы ---------------
    public ArrayList<String> getListTable() {
        //connectionToBase(); // вызов Фукция подключения к базе
        ArrayList<String> list_table_base = new ArrayList();
        try {
            stmt = connection.createStatement();
            // Показывает все таблицы =( и из основной и из тестовой(с сортировкой)
            ResultSet rs = stmt.executeQuery(
                    "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema' "
                    + "ORDER BY tablename;");
            while (rs.next()) {
                String table_schema = rs.getString("tablename");
                list_table_base.add(table_schema);
                //System.out.println(table_schema);
            }
            rs.close();
            stmt.close();
            // connection.commit();
            //System.out.println("-- DROPE TABLE BASE");
        } catch (SQLException e) {
            System.out.println("Failed WIEW TABLE BASE");
            e.printStackTrace();
            return null;
        }
        if(list_table_base.isEmpty()) return null;
        return list_table_base;
    }
    //-------------- Получить список таблиц базы ---------------
    public ArrayList<String> getListTable(String namePart) {
        //connectionToBase(); // вызов Фукция подключения к базе
        ArrayList<String> list_table_base = new ArrayList();
        try {
            stmt = connection.createStatement();
            // Показывает все таблицы =( и из основной и из тестовой(с сортировкой)
            ResultSet rs = stmt.executeQuery(
                    "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog'"+
                    " AND schemaname != 'information_schema' "+ 
                    " AND tablename LIKE '"+ namePart + "%'"+ 
                    " ORDER BY tablename;");
            while (rs.next()) {
                list_table_base.add(rs.getString("tablename"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed WIEW TABLE BASE");
            e.printStackTrace();
            return null;
        }
        if(list_table_base.isEmpty()) return null;
        return list_table_base;
    }
    // ---   List base on server   ---
    public ArrayList<String> getListBase() {
        ArrayList<String> listBase = new ArrayList();
        //connectionToBase(); // вызов Фукция подключения к базе
        try {
            String where = " WHERE NOT datname in ('template0','template1')"; // Исключаем из списка эти базы
            stmt = connection.createStatement();
            String sql = "SELECT datname FROM pg_database" + where + ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                listBase.add(rs.getString("datname")); // Заносим в лист поочередно названия баз которые есть
                //System.out.println( rs.getString("datname"));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        return listBase;
    }
    // --- получить список из базы SEQUENCE ---
    private ArrayList<String> getListSequnce(){
        String sql = null;    
        ArrayList<String> lisrSEQ = new ArrayList<>();
        try {
            connection.setAutoCommit(true);
            stmt = connection.createStatement();
            sql = "SELECT sequence_schema, sequence_name FROM information_schema.sequences;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println( rs.getString("sequence_schema"));
                lisrSEQ.add(rs.getString("sequence_name"));
            }           
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            //e.printStackTrace();
        }
        return lisrSEQ;
    }
    


        // ---  Обновить данные простой запрос(Таблица, столбец, текущие данные, новые данные, массив всех данных/условие)  ---
    public int Update(String table, String column, String newData, HashMap< String, String> mapDataRow) {
        int requestr = 0;
        try {
            connection.setAutoCommit(true);
            String sql = "UPDATE " + "\"" + table + "\""+ " SET " + "\"" + column + "\""  // очень строго вот так почему то(UPDATE  sharp__var SET "Num_0" = 'NULL' WHERE 'Num_0' = '3';)
                    + " = \'" + newData + "\' WHERE " ;
            // Формируем условие запроса из столбцов и данных
            int lastValue = 1; // с первого так как размер не с нуля
             for(Map.Entry<String, String> entry : mapDataRow.entrySet()) {               
                String key = entry.getKey();
                String value = entry.getValue();
                if(lastValue >= mapDataRow.size()){
                    sql += "\"" + key + "\" = " + "\'" + value + "\'";
                }else sql += "\"" + key + "\" = " + "\'" + value + "\'" + " and ";
                ++lastValue;
              }
            sql += ";";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestr;
    }
    //-------------- Удаление списка таблиц ---------------
    public void dropTable(ArrayList<String> listT) {
        //connectionToBase(); // вызов Фукция подключения к базе
        Iterator<String> iter_list_table = listT.iterator();
        try {
            connection.setAutoCommit(false);
            String sql;
            stmt = connection.createStatement();
            while (iter_list_table.hasNext()) {
                sql = "DROP TABLE \"" + iter_list_table.next() + "\";";
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
    //-------------- Удаление таблицы c сохранением резервной копии ---------------
    public int dropTableWithBackUp(String nameT) {
        if(!globVar.DB.isTable(nameT)) return -1;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH_mm_ss_dd_MM_yy");
        String dt = formatForDateNow.format(new Date());
        //connectionToBase(); // вызов Фукция подключения к базе ALTER TABLE products RENAME TO items;
        try {
            stmt = connection.createStatement();
            String sql = "ALTER TABLE \""+ nameT +"\" RENAME TO \"Del_"+dt+"_"+nameT+"\";";
            stmt.executeUpdate(sql);
            System.out.println("Del_"+dt+"_"+nameT + " RENAME successfully");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed RENAME TABLE");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    //-------------- Удаление конкретной таблицы  ---------------
    public void dropTable(String nameT) {
        if(!globVar.DB.isTable(nameT)) return;
        //connectionToBase(); // вызов Фукция подключения к базе
        try {
            connection.setAutoCommit(false);
            String sql;
            stmt = connection.createStatement();
            sql = "DROP TABLE \"" + nameT + "\";";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            System.out.println("-- Table DROPE successfully");

        } catch (SQLException e) {
            System.out.println("Failed DROPE TABLE");
            e.printStackTrace();
        }
    }
    //-------------- Переименование таблицы ---------------
    public int renameTable(String nameT, String name2) {
        if(!globVar.DB.isTable(nameT)) return -1;
        try {
            stmt = connection.createStatement();
            String sql = "ALTER TABLE \""+ nameT +"\" RENAME TO \""+name2+"\";";
            stmt.executeUpdate(sql);
            System.out.println(nameT+" to " + name2 +" RENAME successfully");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed RENAME TABLE" + nameT+" to " + name2);
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    

    // Функция корректировки названия таблиц для базы данных
    String replacedNt(String s) {
        s = s.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", "Sharp_").replace("$", "Dollar_"); // тут и при создании нужно сделать единый модуль
        return s;
    }
    
//    // --- Создание  SEQUENCE ---
//    private void createSEQ1(String name){
//        String sql = null;
//                
//        try {
//            for(String s: getListSequnce()){ // пробегаем по названиям SEQUENCE 
//                System.out.println(s);
//                if(s.equalsIgnoreCase(name)){ // проверка на соответствие и удаление(создает их в нижнем регистре)
//                    sql = "DROP SEQUENCE " + s +";";
//                    stmt = connection.createStatement();
//                    stmt.executeUpdate(sql);
//                }
//            }
//            sql = "CREATE SEQUENCE " + name+";";
//            stmt = connection.createStatement();
//            stmt.executeUpdate(sql);
//            stmt.close();
//        } catch (SQLException e) {
//            FileManager.loggerConstructor("error PSQL request " + sql);
//            e.printStackTrace();
//        }
//    }
    
    // --- добавить комментарий к таблице ---
    public void createCommentTable(String table, String comment){
        String sql = null;   
        try {
            stmt = connection.createStatement();
            sql = "COMMENT ON TABLE \"" + table + "\" IS '" +comment +"'" +";";
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            e.printStackTrace();
        }
    }
   // --- добавить комментарий к столюцу ---
    public void createCommentColumn(String table, String col, String comment){
        String sql = null;   
        try {
            stmt = connection.createStatement();
            sql = "COMMENT ON COLUMN \""+table+"\".\""+col+"\" IS 'colName:"+col+", comment:"+comment+"'" +";";
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            e.printStackTrace();
        }
    }
    
    // --- получить комментарии таблицы ---
    public ArrayList<String>  getCommentList(String table_name){
        String sql = null; 
        //String comment = null;
        ArrayList<String> listComm = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            sql = "SELECT description FROM pg_description join pg_class on pg_description.objoid = pg_class.oid WHERE relname = '" +table_name+ "';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int q = rsmd.getColumnCount();
                for(int i = 1; i<=q; i++) 
                    listComm.add(rs.getString(i));
            }           
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            return null;
        }
        return listComm;
    }
    // --- получить комментарии таблицы ---
    public String getCommentTable(String table_name){
        String sql = null; 
        String comment = null;
        try {
            stmt = connection.createStatement();
            sql = "SELECT description FROM pg_description join pg_class on pg_description.objoid = pg_class.oid WHERE relname = '" +table_name+ "';"; 
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            comment = rs.getString("description");
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
        }
        return comment;
    }
    
    // --- Удалить строку по условию точное удаление ---
    public void deleteRow(String table, HashMap< String, String> mapDataRow) { // может будем удалять по id 
        int requestr = 0;
        try {
            connection.setAutoCommit(true);
            String sql = "DELETE FROM " + "\"" + table + "\"" + " WHERE " ;
            // Формируем условие запроса из столбцов и данных
            int lastValue = 1; // с первого так как размер не с нуля
             for(Map.Entry<String, String> entry : mapDataRow.entrySet()) {               
                String key = entry.getKey();
                String value = entry.getValue();
                if(lastValue >= mapDataRow.size()){
                    sql += "\"" + key + "\" = " + "\'" + value + "\'";
                }else sql += "\"" + key + "\" = " + "\'" + value + "\'" + " and ";
                ++lastValue;
              }
            sql += ";";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // --- Удалить строку по id но удалит все строки c таким же id---
    public void deleteRowId(String table, String id) { // может будем удалять по id 
        try {
            connection.setAutoCommit(true);
            String sql = "DELETE FROM " + "\"" +table + "\"" + " WHERE id='" + id + "'";
            sql += ";";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //---------------- возратить последний id ---------
    public int getLastId(String table){
        String  id = "id";
        int lastId=0;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT "+id+" FROM "+"\"" +table+"\""+" ORDER BY "+id+" desc limit 1;" );
            while ( rs.next() ) {
              lastId = rs.getInt(id);
            }
        } catch (SQLException e) {
		System.out.println("Failed CREATE TABLE");
		e.printStackTrace();
        }
        return lastId;
     }
    
    // --- инкрементировать или декрементировать id всей таблицы ---
    public void updateID(String table, int numberId, boolean i_dCriment) {
        String PorM = "";
        if (i_dCriment == true) PorM = "+"; else PorM = "-";
        int requestr = 0;
        String columnId = "id";
        try {
            connection.setAutoCommit(true);
            String sql = "UPDATE " + "\""+  table + "\"" + " SET " + "\"" + columnId + "\""  
                    + " = \"" + columnId + "\"" + PorM + 1 + " WHERE " +  // Прибавляем на 1
                    columnId + ">" +numberId + ";";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //--------- Методы обслюживающие систему абонентов -Lev-----------------
    public static void createAbonentTable(){ //Создание таблицы абонентов, если её не было
        if(!globVar.DB.isConnectOK()) return;
        ArrayList<String> list_table_base = globVar.DB.getListTable();
        if(StrTools.searchInList("Abonents", list_table_base)< 0){
            String[] columns = {"Abonent","Наименование","Path_to_Excel", "Abonent_type", "Экземпляры"};
            globVar.DB.createTableEasy("Abonents", columns,"Типовые абоненты");
        }
    }
    public static ArrayList<String[]> getAbonentArray() // функция для создания списка из таблицы абонентов
    {
        if(!globVar.DB.isConnectOK()) return null;
        return globVar.DB.getData("Abonents","Abonent"); //Получаем список абонентов отсортированный по алгоритмическому имени 
    }
    
    
    // --- смотрим за временем создания коммита поля таблицы ---
    public Timestamp getTimeFirstCommit(String table){
        Timestamp resultT = null;
        try {
            stmt = connection.createStatement();
            boolean enableTimestamp = false;
            ResultSet rs = stmt.executeQuery("show track_commit_timestamp;");
            while (rs.next()) {
                // проверка на включение
                if(rs.getString("track_commit_timestamp").equals("on")){
                    enableTimestamp = true;
                    break;
                }
            }
            // основной запрос
            if(enableTimestamp){
                boolean notComm = true;
                rs = stmt.executeQuery("SELECT pg_xact_commit_timestamp(xmin), * FROM  \"" + table +"\";");
                while (rs.next()) {
                    resultT = new Timestamp(System.currentTimeMillis()); // определяем текущее время системы
                    String tmpStr = null;
                    Timestamp timeStamp = rs.getTimestamp("pg_xact_commit_timestamp");
                    if (timeStamp != null){
                        // System.out.println(timeStamp); систе
                        if(timeStamp.getTime() < resultT.getTime() ){ // ищем наименьшее время
                            resultT = timeStamp;
                            notComm = false;
                        }
                    }
                }
                // если не было не одного коммита
                if(notComm){
                    resultT = null;
                }
                //System.out.println(resultT);
            }
            else{
                FileManager.loggerConstructor("Don't get commit :  Enable #track_commit_timestamp = on	--> postgresql.conf");
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("Faled get commit data " + table);
            //e.printStackTrace();
        }
        return resultT;
        
    }
    
    // --- Функции наблюдателя ---
    @Override
    public void addObserver(Observer o) { // добавить слушателя
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) { // удалить слушателя
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
       for(Observer o: observers){ // Рассылаем слушетелям 
       o.handleEvent(min, max, value);
       }
    }
    
     // метод изменения состояни 
    public void setValueObserver(int min, int max, int value){
        this.min = min;
        this.max = max; 
        this.value = value;
        notifyObserver(); // вызов оповещения всем слушателям
    }

    
//    public static void main(String[] arg){
//        XMLSAX.getConnectBaseConfig("Config.xml");
//        DataBase db = new DataBase();
//        System.out.println(db.getTimeFirstCommit("Abonents")); // получить первый коммит времени строки таблицы
//       //String nameBD = db.getCurrentNameBase();
//       System.out.println(db.getListTable().toString());
//       db.createCommentTable("NMC_DGI", "comment");
//       System.out.println(db.getCommentTable("NMC_DGI"));
       //System.out.println(db.getListColumnTable("t_gpa_di_settings").toString());
       //String[] rows = {"325", "Commen-665", "NZ", "0987654321", "name-struct"};
       //String[] rows = {"Commen-665", "NZ", "name-struct"};
       //ArrayList<String> listNameColum = new ArrayList<>();
       //listNameColum.add("id");
       // listNameColum.add("UUID");
       //listNameColum.add("Comment");
       //listNameColum.add("Type");    
       //listNameColum.add("Name");
       //db.insertRows("t_gpa_di_settings", rows, listNameColum);
    
 //   }
}
