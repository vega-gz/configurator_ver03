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

public class DataBase {

  
    Statement stmt;
    Connection connection = null;
    private ArrayList<String[]> currentSelectTable;
    private String[] columns;
    String URL = ""; //jdbc:postgresql://192.168.10.41:5432/";
    String BASE = ""; //test";
    String DB_URL = URL + BASE;
    //String USER = "mutonar";
    String USER = ""; //postgres";
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

        if (instance == null) {		//если объект еще не создан
            instance = new DataBase();	//создать новый объект
            instance.connectionToBaseconfig(); // И сразу подключаемся к базе
        }
        return instance;		// вернуть ранее созданный объект
    }
    
    // --- Подключение к базе используя параметры ---
    public void connectionToBase(String URLfile, String DB, String USER, String PASS) {
        this.URL = URLfile;
        this.BASE = DB;
        this.USER = USER;
        this.PASS = PASS;
        connectionToBase();
    }
    
    // --- Читает конфигурацию для подключения к базе ---
    private void connectionToBaseconfig(){
        new XMLSAX().setConnectBaseConfig(FILECONFIG); // так читаем файл и подключаемся к базе
    }
    // не правильно (так данные передавать это боль)
    private String[] getInfoCurrentConnect(){
        String[] infoConnect = {URL,USER,PASS};
        return infoConnect;
    }
    
    // Сменить базу
    public void changeBase(String base){
       String[] infoConnect = getInfoCurrentConnect();
       connectionToBase(base,infoConnect[0],infoConnect[1],infoConnect[2]);
    }
    
    // --- Метод подключения к базе ---
    private void connectionToBase() {
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
            return;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected" + "Base:" + URL + BASE);
        try {
            connection = DriverManager
                    .getConnection(URL + BASE, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }
    }

    // -------------- CREATE DATABASE -----------
    void createBase(String name) {
        //connectionToBase(); // вызов Фукция подключения к базе
        try {
            //name = name.replace("-", "_").replace(".", "_").replace(" ", "_"); // может понадобиться как в сосздании таблицы
            connection.getAutoCommit();
            connection.setAutoCommit(true);
            String sql;
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
            e.printStackTrace();
            return;

        }
    }
    
    //-------------- CREATE TABLE ---------------
    public void createTable(String name_table,  ArrayList<String> listNameColum) {
        int number_colum = listNameColum.size();
        String sql = null;
        connectionToBase(); // вызов Фукция подключения к базе
        //переменная для анализа
        String nameTbanalise = new String(name_table);
        String nc_stringing = "";
        //Заменяем символы так как ограничения в Postgrese
        //name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", "");
        name_table = replacedNt(name_table);

        switch (name_table) {
            case "AI1":
            case "AO1":
            case "DI1":
            case "DO1": 
            case "DO": {
                int tmp_cell = 0;
                number_colum = number_colum - 4; // так как добавили Еще UUID
                // -- create table max lengt + name table from file cells
                nc_stringing = " (UUID_PLC TEXT NOT NULL, UUID_HMI TEXT NOT NULL, UUID_DRV TEXT NOT NULL, UUID_Trend TEXT NOT NULL";
                Iterator<String> iter_list_table = listNameColum.iterator();
                while (iter_list_table.hasNext()) {
                    tmp_cell++;
                    String bufer_named = iter_list_table.next().replace("/", "_");
                    //nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT    NOT NULL";
                    nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT";
                }
                /* for (int i=tmp_cell+1; i < number_colum; i++){ // это было для добавления когда нечего нет и авто заполнение
                 nc_stringing += " ," + "colum_" + Integer.toString(i) +  "      TEXT    NOT NULL";
                 }*/
                nc_stringing += ")";
            }
            break; //до этого до скобки
            default:
                if (!listNameColum.isEmpty()) { // если не передели ни каких значений в таком варианте оно не зайдет сюда
                    int tmp_cell = 0;
                    // -- create table max lengt + name table from file cells
                    nc_stringing = " (UUID TEXT NOT NULL";
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
                    //System.out.print(nc_stringing);

                } else {
                    // -- create table max lengt row in sheet
                    nc_stringing = " (ID TEXT NOT NULL";
                    for (int i = 1; i < number_colum; i++) {
                        //nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT";
                    }
                    nc_stringing += ")";
                }
                break;
        }

        try {

            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            sql = "CREATE TABLE " + name_table + nc_stringing;
            System.out.println("Create t_sql " + sql); // смотрим какой запрос на соз
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            System.out.println("-- Table created successfully");

        } catch (SQLException e) {
            System.out.println("Failed CREATE TABLE");
            System.out.println(sql); // смотрим какой запрос на соз
            e.printStackTrace();
            return;

        }
    }
    // --- Вставка данных (название таблицы, список столбцов, данные)---
    public void insertRows(String name_table, String[] rows, ArrayList<String> listNameColum) throws SQLException {
        connectionToBase(); // вызов Фукция подключения к базе
        connection.setAutoCommit(true);
        String nameTbanalise = new String(name_table);
        String sql = "";
        try {
            //name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", ""); // тут и при создании нужно сделать единый модуль
            name_table = replacedNt(name_table);
            //--------------- INSERT ROWS ---------------
            switch (name_table) {
                case "AI1":
                case "AO1":
                case "DI1":
                case "DO1": {

                    sql = "INSERT INTO " + name_table
                            + " (UUID_PLC , UUID_HMI , UUID_DRV ,UUID_Trend";

                    int tmp_cell = 0;

                    Iterator<String> iter_list_table = listNameColum.iterator();
                    while (iter_list_table.hasNext()) {
                        tmp_cell++;
                        String bufer_named = iter_list_table.next().replace("/", "_");
                        sql += " ," + "\"" + bufer_named + "\"";
                    }
                    for (int i = tmp_cell; i < rows.length - 4; i++) { // -3 так как опять UUID в AI1
                        sql += ",colum_" + Integer.toString(i + 1);
                    }// +1 что бы соответствовать нумерации из файла Exel
                    sql += ") VALUES (";
                    for (int i = 0; i < rows.length; i++) {
                        if (i + 1 == rows.length) {
                            sql += "'" + rows[i] + "'";
                        } // не нравится точка похоже в данных как то надо обходить(похоже)
                        else {
                            sql += "'" + rows[i] + "'" + ", ";
                        }
                    }
                    sql += ");";

                }
                break;
                default: { // а вот тут трудности у нас

                    if (!listNameColum.isEmpty()) {
                        /*
                         int tmp_cell = 0;
                         Iterator<String> iter_list_table = listNameColum.iterator();
                         while (iter_list_table.hasNext()) {
                         if (tmp_cell <= 0) {
                         sql = "INSERT INTO " + name_table + " (ID"; // при первом проходе иначе будет отличаться данные и столбцы
                         iter_list_table.next(); // Обязательное иначе тришер не сработает
                         } else {
                         String bufer_named = iter_list_table.next().replace("/", "_");
                         sql += " ," + "\"" + bufer_named + "\""; 
                         }
                         tmp_cell++;
                         }
                         */
                        sql = "INSERT INTO " + name_table + " (UUID, "; // при первом проходе иначе будет отличаться данные и столбцы
                        for (int i = 0; i < listNameColum.size(); i++) { // формирую данные для этого запроса - 1 так как добавили ID
                            if (i + 1 >= listNameColum.size()) {
                                String bufer_named = listNameColum.get(i).replace("/", "_");
                                sql += "\"" + bufer_named + "\"";
                            } else {
                                String bufer_named = listNameColum.get(i).replace("/", "_");
                                sql += "\"" + bufer_named + "\"" + " ,";
                            }
                        }

                        /* for (int i = tmp_cell; i < rows.length+1; i++) { // Вот это не понятно
                         sql += ",colum_" + Integer.toString(i + 1);
                         }// +1 что бы соответствовать нумерации из файла Exel
                         */
                        sql += ") VALUES (";
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
                    break;
                }
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
    }

//--------------- SELECT DATA --------------
    ArrayList<String[]> selectData(String table) {
        //connectionToBase(); // вызов Фукция подключения к базе
        ArrayList<String[]> selectData = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + ";");
            while (rs.next()) {
                String id = rs.getString("Colum_18");
                String name = rs.getString("Канал");
                String age = rs.getString("Наименование сигнала");
                String address = rs.getString("мин");
                String salary = rs.getString("макс");
                String UM = rs.getString("ед.изм");
                String TypeCh = rs.getString("Тип");
                String TypeADC = rs.getString("Colum_19");
                String addres1 = rs.getString("2.0");
                String addres2 = rs.getString("3.0");
                String uuid_hmi = rs.getString("uuid_hmi");
                String uuid_plc = rs.getString("uuid_plc");
                String uuid_drv = rs.getString("uuid_drv");
                String uuid_trend = rs.getString("uuid_trend");
                String[] strfromtb = {id, name, age, address, salary, UM, TypeCh, TypeADC, addres1, addres2, uuid_hmi, uuid_plc, uuid_drv, uuid_trend};
                selectData.add(strfromtb);
                System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s UM=%s TYPEADC=%s A1=%s A2=%s", id, name, age, address, salary, UM, TypeCh, TypeADC, addres1, addres2));
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

        //--------------- SELECT DATA sum columns --------------
    // какие именно столбцы дергать
    public ArrayList<String[]> getData(String table, ArrayList<String> columns) {
        //  connectionToBase(); // вызов Фукция подключения к базе
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
        try {
            stmt = connection.createStatement();
            System.out.println("SELECT " + s_columns + " FROM " + table + ";");
            ResultSet rs = stmt.executeQuery("SELECT " + s_columns + " FROM " + table + ";");
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

    //-------------- DROPE TABLE ---------------
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

    //-------------- TABLE похоже тестовая ?---------------
    public ArrayList<String> getviewTable() {
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

    /*
    public static void main(String[] args) {
        //new BasePostgresLuaXLS().createBase("test6671"); // Так создаем 
        List<String> tmp = new BasePostgresLuaXLS().listBase(); // Так смотрим
        for (String s : tmp) {
            System.out.println(s);
        }
    }
*/
   /* String[] getColumns() { // не нужный метод
        return columns;
    }*/

    public ArrayList<String[]> getcurrentSelectTable() {
        return currentSelectTable;
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
                        String id = UUID.getUUID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование сигнала");
                        String RangeMin = rs.getString("Диапазон мин.");
                        String RangeMax = rs.getString("Диапазон макс.");//field[4]
                        String Unit = rs.getString("Единица измерения");
                        String sigType = rs.getString("Тип сигнала");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Уст");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");
                        String An = rs.getString("УСТ_НИЖН_АВАР");
                        String Pn = rs.getString("УСТ_НИЖН_ПРЕД");
                        String Pv = rs.getString("УСТ_ВЕРХ_ПРЕД");
                        String Av = rs.getString("УСТ_ВЕРХ_АВАР");

                        String[] str = {TypeADC, id, namesig, RangeMax, RangeMin, Unit, sigType, Adres_1, Adres_2, Device, Slot, Channel, An, Pn, Pv, Av};
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
                        String id = UUID.getUUID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование сигнала");
                        String RangeMin = rs.getString("Диапазон мин.");
                        String RangeMax = rs.getString("Диапазон макс.");//field[4]
                        String Unit = rs.getString("Единица измерения");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Уст");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC, id, namesig, RangeMax, RangeMin, Unit, Adres_1, Adres_2, Device, Slot, Channel};
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
                        String id = UUID.getUUID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование сигнала");
                        String RangeMin = rs.getString("Диапазон мин.");
                        String RangeMax = rs.getString("Диапазон макс.");//field[4]
                        String sigType = rs.getString("Тип сигнала");
                        String Device = rs.getString("Уст");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC, id, namesig, RangeMax, sigType, RangeMin, Device, Slot, Channel};
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
                        String id = UUID.getUUID();//генерит рандомный уид который никому не интересен
                        String namesig = rs.getString("Наименование сигнала");
                        String sigType = rs.getString("Тип сигнала");
                        String Adres_1 = rs.getString("Адрес_1");
                        String Adres_2 = rs.getString("Адрес_2");
                        String Device = rs.getString("Уст");//field[9]
                        String Slot = rs.getString("Слот");
                        String Channel = rs.getString("Канал");

                        String[] str = {TypeADC, id, namesig, sigType, Adres_1, Adres_2, Device, Slot, Channel};
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
}
