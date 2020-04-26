 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseConnect;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import XMLTools.UUID;
import XMLTools.XMLSAX;
import java.util.HashMap;
import java.util.Map;
import fileTools.FileManager;
import globalData.globVar;

public class DataBase {


  
    Statement stmt;
    Connection connection = null;
    private ArrayList<String[]> currentSelectTable;
    private String[] columns;
    String URL = ""; //jdbc:postgresql://192.168.10.41:5432/";
    String BASE = null; //test";
    String DB_URL = URL + BASE;
    //String USER = "mutonar";
    String USER = null; //postgres";
    //String PASS = "postgres";
    String PASS = ""; //Solovin2";
    String FILECONFIG = "Config.xml";
    
    // Делаем синглтон
    private static DataBase instance;

    private  DataBase() { // для синглтона в однопоточном варианте
        //connectionToBase();
    }

    //  синглтон не нужен а вот нужен
    public static DataBase getInstance() { // #3
        int ret=0;
        if (instance == null) {		//если объект еще не создан
            instance = new DataBase();	//создать новый объект
            ret = instance.connectionToBaseconfig(); // И сразу подключаемся к базе
        }
        if(ret==0)return instance;		// вернуть ранее созданный объект
        else return null;
    }
    
    // --- Подключение к базе используя параметры ---
    public int connectionToBase(String URL, String DB, String USER, String PASS) {
        this.URL = URL;
        this.BASE = DB;
        this.USER = USER;
        this.PASS = PASS;
        return connectionToBase();
    }
    
    // --- получить имя текущей базы ---
    public String getCurrentNameBase(){
        return this.BASE;
    }
    // --- получить имя текущего пользователя ---
     public String getCurrentUser(){
        return this.USER;
    }
    
    // --- Читает конфигурацию для подключения к базе ---
    private int connectionToBaseconfig(){
        int x = new XMLSAX().setConnectBaseConfig(FILECONFIG); // так читаем файл и подключаемся к базе
        globVar.currentBase = BASE; // присваием глобальным паметрам значение после инициализации
        return x;
    }
    // не правильно (так данные передавать это боль)
    private String[] getInfoCurrentConnect(){
        String[] infoConnect = {URL,USER,PASS};
        return infoConnect;
    }
    
    // Сменить базу
    public int changeBase(String base){
       String[] infoConnect = getInfoCurrentConnect();
       return connectionToBase(infoConnect[0],base,infoConnect[1],infoConnect[2]);
    }
    
    // --- Метод подключения к базе ---
    private int connectionToBase() {
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
            e.printStackTrace();
            return -1;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected" + "Base:" + URL + BASE);
        try {
            connection = DriverManager
                    .getConnection(URL + BASE, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return -1;
        }
        return 0;
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
            //" WITH OWNER " + name +
            //" ENCODING 'UTF8' " +
            //" TEMPLATE template0 ;";
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
    
    //-------------- CREATE TABLE ---------------
    public void createTable(String name_table,  ArrayList<String> listNameColum) {
        String nameSEQ = name_table +"_id_seq"; // имя итератора
        int number_colum = listNameColum.size();
        //проверка если есть такая таблица удаляем ее
        {
        for(String s: getListTable()){ // Получаем список таблиц и пробежимся сравнивая их
            if(name_table.equalsIgnoreCase(s)){
                System.out.println("Finding exist table and droping: " + name_table);
                ArrayList<String> dropingTable = new ArrayList();
                //new ArrayList<String>().add(name_table); // так передать нельзя, разобраться 
                dropingTable.add(name_table);
                dropTable(dropingTable); // так удаляем по тому что изначально был список очищения(можно сделать отдельно)
                break;
            }
        }
        createSEQ(nameSEQ);// После удаления создаем и удаляем итератор 
        }
        
        // Блок проверки нужен ли нам столбец с UUID 
        String addUUID = ", UUID TEXT NOT NULL";
        for(String s:listNameColum){
            if(s.equalsIgnoreCase("UUID")) addUUID =""; // если нашли что создаем с нужными UUID обнуляем
        }
          
        String sql = null;
        //переменная для анализа
        //String nameTbanalise = new String(name_table);
        String nc_stringing = "";
        name_table = replacedNt(name_table); //Заменяем символы так как ограничения в Postgrese
        if (!listNameColum.isEmpty()) { // 
                    int tmp_cell = 0;
                    nc_stringing = " (id INTEGER DEFAULT NEXTVAL(\'" +nameSEQ+"\')" + addUUID;
                    Iterator<String> iter_list_table = listNameColum.iterator();
                    while (iter_list_table.hasNext()) {
                        tmp_cell++;
                        String bufer_named = iter_list_table.next().replace("/", "_");
                        //nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT";
                    }
                    for (int i = tmp_cell + 1; i < number_colum; i++) {
                        //nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT";
                    }
                    nc_stringing += ")";
                } else {
                    // -- create table max lengt row in sheet
                    nc_stringing = " (ID TEXT NOT NULL";
                    for (int i = 1; i < number_colum; i++) {
                        nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT";
                    }
                    nc_stringing += ")";
                }

        try {
            connection.setAutoCommit(true);
            stmt = connection.createStatement();
            sql = "CREATE TABLE " + name_table + nc_stringing;
            System.out.println("Create t_sql " + sql); // смотрим какой запрос на соз
            stmt.executeUpdate(sql);
            stmt.close();
            //connection.commit();
            //System.out.println("-- Table created successfully");
            createCommentTable(name_table, "комментарий к этой таблице"); // вызом метода добавления комментария

        } catch (SQLException e) {
            System.out.println("Failed CREATE TABLE");
            System.out.println(sql); // смотрим какой запрос на соз
            e.printStackTrace();
            return;
        }
    }
    
// --- Вставка данных (название таблицы, список столбцов, данные) если нет данных для UUID сам сделает---
    public void insertRows(String name_table, String[] rows, ArrayList<String> listNameColum) {
        
        String addUUID = "\"UUID\", "; // блок определения нужно ли генерировать UUID
        String dataUUID = "\'" +UUID.getUIID()+"\'" + ", ";//  генерим ууид прмо тут если его нет в данных для для добавки
        for(String s:listNameColum){
            if(s.equalsIgnoreCase("UUID")){ // если нашли что создаем с нужными UUID обнуляем
            addUUID ="";
            dataUUID = "";
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
                            sql = "INSERT INTO " + name_table + " ("+addUUID; // при первом проходе иначе будет отличаться данные и столбцы
                            for (int i = 0; i < listNameColum.size(); i++) { // формирую данные для этого запроса - 1 так как добавили ID
                                if (i + 1 >= listNameColum.size()) {
                                    String bufer_named = listNameColum.get(i).replace("/", "_");
                                    sql += "\"" + bufer_named + "\"";
                                } else {
                                    String bufer_named = listNameColum.get(i).replace("/", "_");
                                    sql += "\"" + bufer_named + "\"" + " ,";
                                }
                            }
                            sql += ") VALUES ("+dataUUID;
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
     
    // какие именно столбцы дергать
    public ArrayList<String[]> getData(String table, ArrayList<String> columns) {
        StructSelectData.setColumns(columns); // вот это жопа надо что то с этим делать мешает в Main_Jpanel
        ArrayList<String[]> selectData = new ArrayList<>();
        String s_columns = "";
        String[] strfromtb = new String[columns.size()]; // массив под данные
        for (int i = 0; i < columns.size(); ++i) { //формирование строки запроса
            if (i < columns.size() - 1) {
                s_columns += "\"" + columns.get(i) + "\"" + ", ";
            } // Кавычки для руских имен и пробелов
            else {
                s_columns += "\"" + columns.get(i) + "\"";
            }
        }
        // проверка на столбец по которому упорядочим данные
        String orderCol = "id";
        String sql = null;
        for(String s: getListColumnTable(table)){
            if(s.equals(orderCol)){ // нашли тогда упорядовать
              sql = "SELECT " + s_columns + " FROM " + table + " ORDER BY \"" +orderCol +"\";";
              break;
            }else sql = "SELECT " + s_columns + " FROM " + table +";"; 
        }
        try {
            stmt = connection.createStatement();
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                for (int i = 0; i < columns.size(); ++i) {
                    strfromtb[i] = rs.getString(columns.get(i));
                }
                String[] tmp1 = Arrays.copyOf(strfromtb, strfromtb.length); // необходимость из за ссылки
                selectData.add(tmp1);
                //System.out.println(strfromtb[0]); // это просто для тестов
            }
            rs.close();
            stmt.close();
            StructSelectData.setcurrentSelectTable(selectData); // Вносим данные в структуру( зачем)
            //connection.commit();
            //System.out.println("-- Operation SELECT done successfully");
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        return selectData;
    }

    // --- Select columns Table  ---
    public List<String> selectColumns(String table) {
        //connectionToBase(); // вызов Фукция подключения к базе
        List<String> listColumn = new ArrayList();
        String ColumnN = "column_name"; // Если захоим выборку пеще чего то
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + ColumnN
                    + " FROM information_schema.columns WHERE table_name = \'" + table + "\';");
            while (rs.next()) {
                listColumn.add(rs.getString(ColumnN));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        return listColumn;
    }

        // ---  Обновить данные простой запрос(Таблица, столбец, текущие данные, новые данные, массив всех данных/условие)  ---
    public int Update(String table, String column, String newData, HashMap< String, String> mapDataRow) {
        int requestr = 0;
        try {
            connection.setAutoCommit(true);
            String sql = "UPDATE " + table + " SET " + "\"" + column + "\""  // очень строго вот так почему то(UPDATE  sharp__var SET "Num_0" = 'NULL' WHERE 'Num_0' = '3';)
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
   
            
    //--------------- SELECT DATA to CreateTGPAAI тоже тестовое?--------------
    ArrayList<String[]> selectDataGPAAI(String table) {
        //connectionToBase(); // вызов Фукция подключения к базе
        ArrayList<String[]> selectData = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + ";");
            while (rs.next()) {
                String TypeADC = rs.getString("Tag name"); // должы выбирать
                String id = rs.getString("uuid_plc");
                String nsign = rs.getString("Наименование сигнала");
                String[] strfromtb = {TypeADC, id, nsign};
                selectData.add(strfromtb);
                //System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s UM=%s TYPEADC=%s A1=%s A2=%s" ,id,name,age,address,salary,UM,TypeCh,TypeADC,addres1,addres2));
            }
            rs.close();
            stmt.close();
            //connection.commit();
            System.out.println("-- Operation SELECT done successfully");
        } catch (SQLException e) {
            System.out.println("Failed ADD data");
            e.printStackTrace();
            //return;
        }
        return selectData;
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

    //-------------- Удаление конкретной таблицы  ---------------
    public void dropTable(String nameT) {
        //connectionToBase(); // вызов Фукция подключения к базе
        try {
            connection.setAutoCommit(false);
            String sql;
            stmt = connection.createStatement();
            sql = "DROP TABLE " + nameT + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            System.out.println("-- Table DROPE successfully");

        } catch (SQLException e) {
            System.out.println("Failed DROPE TABLE");
            e.printStackTrace();
            return;

        }
    }
    
    // ---   List base on server   ---
    ArrayList<String> listBase() {
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

    //-------------- Получить список таблиц базы ---------------
    public ArrayList<String> getListTable() {
        //connectionToBase(); // вызов Фукция подключения к базе
        ArrayList<String> list_table_base = new ArrayList();
        try {
            stmt = connection.createStatement();
            // Показывает все таблицы =( и из основной и из тестовой
            ResultSet rs = stmt.executeQuery("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';");
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
        }
        return list_table_base;
    }
    
    // Функция корректировки названия таблиц для базы данных
    String replacedNt(String s) {
        s = s.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", "Sharp_").replace("$", "Dollar_"); // тут и при создании нужно сделать единый модуль
        return s;
    }

    // Гришин метот а может и не один все заменить.
      //----Строим все сигналы которые сюда ссылаются
    public ArrayList<String[]> getSelectData(String table) {//в перспективе задавать в параметрах листи через if else указывать,ибо разный набор столбцов мы вытягиваем для ai ao di do
        ArrayList<String[]> selectData = new ArrayList<>();
        String TableNumber;

        switch (table) {
            case "ai":
                try {
                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
                    while (rs.next()) {
                        String TypeADC = rs.getString("TAG_NAME_PLC");
                      //  String id = UUID.getUIID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование");
                        String RangeMin = rs.getString("Диапазон_мин");
                        String RangeMax = rs.getString("Диапазон_макс");//field[4]
                        String Unit = rs.getString("Единица_измерения");
                        String sigType = rs.getString("Тип_датчика");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Устройство");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");
                        String An = rs.getString("УСТНИЖНАВАР");
                        String Pn = rs.getString("УСТНИЖНПРЕД");
                        String Pv = rs.getString("УСТВЕРХПРЕД");
                        String Av = rs.getString("УСТВЕРХАВАР");

                        String[] str = {TypeADC,  namesig, RangeMax, RangeMin, Unit, sigType, Adres_1, Adres_2, Device, Slot, Channel, An, Pn, Pv, Av};
                        selectData.add(str);
                    }
                    rs.close();
                    stmt.close();
                    System.out.println("--Operation SELECT done sucessfully");
                } catch (SQLException ex) {
                    Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                break;
            case "ao":
                try {
                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
                    while (rs.next()) {
                        String TypeADC = rs.getString("TAG_NAME_PLC");
                       // String id = UUID.getUIID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование");
                        String RangeMin = rs.getString("Диапазон_мин");
                        String RangeMax = rs.getString("Диапазон_макс");//field[4]
                        String Unit = rs.getString("Единица_измерения");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Устройство");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC,  namesig, RangeMax, RangeMin, Unit, Adres_1, Adres_2, Device, Slot, Channel};
                        selectData.add(str);
                    }
                    rs.close();
                    stmt.close();
                    System.out.println("--Operation SELECT done sucessfully");
                } catch (SQLException ex) {
                    Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                break;
            case "dgo":
                try {
                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
                    while (rs.next()) {
                        String TypeADC = rs.getString("TAG_NAME_PLC");
                      //  String id = UUID.getUIID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование");
                        
                        String sigType = rs.getString("Тип_сигнала");
                        String Device = rs.getString("Устройство");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC,  namesig, sigType,  Device, Slot, Channel};
                        selectData.add(str);
                    }
                    rs.close();
                    stmt.close();
                    System.out.println("--Operation SELECT done sucessfully");
                } catch (SQLException ex) {
                    Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                break;
            case "di":
                try {
                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
                    while (rs.next()) {
                        String TypeADC = rs.getString("TAG_NAME_PLC");
                      //  String id = UUID.getUIID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование");
                        String sigType = rs.getString("Тип_сигнала");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Устройство");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC,  namesig, sigType, Adres_1, Adres_2, Device, Slot, Channel};
                        selectData.add(str);
                    }
                    rs.close();
                    stmt.close();
                    System.out.println("--Operation SELECT done sucessfully");
                } catch (SQLException ex) {
                    Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                break;

        }
        return selectData;
    }
    
    // --- Создание  SEQUENCE ---
    private void createSEQ(String name){
        String sql = null;
                
        try {
            for(String s: getListSequnce()){ // пробегаем по названиям SEQUENCE 
                System.out.println(s);
                if(s.equalsIgnoreCase(name)){ // проверка на соответствие и удаление(создает их в нижнем регистре)
                    sql = "DROP SEQUENCE " + s +";";
                    stmt = connection.createStatement();
                    stmt.executeUpdate(sql);
                }
            }
            sql = "CREATE SEQUENCE " + name+";";
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            e.printStackTrace();
        }
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
    
    // --- получить столбцы из таблицы ---
    private ArrayList<String> getListColumnTable(String table_name){
        String sql = null;    
        ArrayList<String> listColumn = new ArrayList<>();
        try {
            //connection.setAutoCommit(true);
            stmt = connection.createStatement();
            sql = "SELECT column_name,data_type FROM information_schema.columns where table_name = '" +table_name+ "';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println( rs.getString("sequence_schema"));
                listColumn.add(rs.getString("column_name"));
            }           
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            //e.printStackTrace();
        }
        return listColumn;
    }

    // --- добавить комментарий к таблице ---
    public void createCommentTable(String table, String comment){
        String sql = null;   
        try {
            sql = "COMMENT ON TABLE  " + table+ " IS " +"'" +comment +"'" +";";
            System.out.println(sql);
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            e.printStackTrace();
        }
    }
    
    // --- получить комментарии таблицы ---
    public String getCommentTable(String table_name){
        String sql = null; 
        String comment = null;
        ArrayList<String> listColumn = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            sql = "SELECT description FROM pg_description join pg_class on pg_description.objoid = pg_class.oid"
                    + " where relname = '" +table_name+ "';"; // такой запрос нам нечего не возвращает
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                comment = rs.getString("description");
            }           
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            FileManager.loggerConstructor("error PSQL request " + sql);
            //e.printStackTrace();
        }
        return comment;
    }
    
    public static void main(String[] arg){
       DataBase db = DataBase.getInstance();
       String nameBD = db.getCurrentNameBase();
       //System.out.println(db.getListTable().toString());
       System.out.println(db.getListColumnTable("t_gpa_di_settings").toString());
       //String[] rows = {"325", "Commen-665", "NZ", "0987654321", "name-struct"};
       String[] rows = {"Commen-665", "NZ", "name-struct"};
       ArrayList<String> listNameColum = new ArrayList<>();
       //listNameColum.add("id");
       // listNameColum.add("UUID");
       listNameColum.add("Comment");
       listNameColum.add("Type");
      
       listNameColum.add("Name");
       db.insertRows("t_gpa_di_settings", rows, listNameColum);
    
    }
}
