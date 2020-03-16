/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseConnect;

import XMLTools.UUID;
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
import java.util.HashMap;
import java.util.Map;

public class DataBase {

    Statement stmt;
    Connection connection;
    private ArrayList<String[]> currentSelectTable;
    private String[] columns;
    UUID uuid = new UUID();
    String url, pass, user;

    // Делаем синглтон
    private static DataBase instance;

    public DataBase() {
        connectionToBase();
    }

    public static DataBase getInstance() { // #3

        if (instance == null) {		//если объект еще не создан
            instance = new DataBase();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }

    public void setConnection(String str) {
        //inFile += str + "\n";
    }

    public void connectionToBase() {
        this.user = user;
        this.pass = pass;
        this.url = url;
        //  String DB_URL = "jdbc:postgresql://172.16.35.25:5432/test08_DB";
        //  String PASS = "test08_DB";
        //  String USER = "test08_DB";
//        String DB_URL=url;
//        String PASS=pass;
//        String USER=user;

        final String DB_URL = "jdbc:postgresql://172.16.35.25:5432/test665";//
        final String USER = "postgres";
        final String PASS = "postgres";//

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

    void createBase(String name) {
        //  connectionToBase(url, pass, user);

// вызов Фукция подключения к базе
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

    public void createTable(String name_table, int number_colum, ArrayList<String> listNameColum) {
        //connectionToBase(); // вызов Фукция подключения к базе
        //переменная для анализа
        String nameTbanalise = new String(name_table);
        String nc_stringing = "";
        //Заменяем символы так как ограничения в Postgrese
        //  name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_");
        name_table = replacedNt(name_table);

        switch (name_table) {
            case "AI1":
            case "AO1":
            case "DI1":
            case "DO1": {
                int tmp_cell = 0;
                number_colum = number_colum - 4; // так как добавили Еще UUID
                // -- create table max lengt + name table from file cells
                nc_stringing = " (UUID_PLC TEXT NOT NULL, UUID_HMI TEXT NOT NULL, UUID_DRV TEXT NOT NULL, UUID_Trend TEXT NOT NULL";
                Iterator<String> iter_list_table = listNameColum.iterator();
                while (iter_list_table.hasNext()) {
                    tmp_cell++;
                    String bufer_named = iter_list_table.next().replace("/", "_");
                    // nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT    NOT NULL";
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
                    nc_stringing = " (ID TEXT NOT NULL";
                    Iterator<String> iter_list_table = listNameColum.iterator();
                    while (iter_list_table.hasNext()) {
                        tmp_cell++;
                        String bufer_named = iter_list_table.next().replace("/", "_");
                        // nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "\"" + bufer_named + "\"" + "      TEXT";
                    }
                    for (int i = tmp_cell + 1; i < number_colum; i++) {
                        // nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT";
                    }
                    nc_stringing += ")";
                    //System.out.print(nc_stringing);

                } else {
                    // -- create table max lengt row in sheet
                    nc_stringing = " (ID TEXT NOT NULL";
                    for (int i = 1; i < number_colum; i++) {
                        //  nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT    NOT NULL";
                        nc_stringing += " ," + "colum_" + Integer.toString(i) + "      TEXT";
                    }
                    nc_stringing += ")";
                }
                break;
        }

        try {

            connection.setAutoCommit(false);//вот тут я еще не понимаю setAouto это дял чекго7
            String sql;//фотегечитисмо
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

    public void insertRows(String name_table, String[] rows, ArrayList<String> listNameColum) throws SQLException {
        //connectionToBase(); // вызов Фукция подключения к базе
        connection.setAutoCommit(true);
        String nameTbanalise = new String(name_table);
        String sql = "";
        try {
           // name_table = name_table.replace("-", "_").replace(".", "_").replace(" ", "_");
            name_table=replacedNt(name_table);
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
                        sql = "INSERT INTO " + name_table + " (ID, "; // при первом проходе иначе будет отличаться данные и столбцы
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

    public ArrayList<String[]> selectData(String table) {
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

    String replacedNt(String s) {
        s = s.replace("-", "_").replace(".", "_").replace(" ", "_").replace("#", "Dies_").replace("$", "Dollar_"); // тут и при создании нужно сделать единый модуль
        return s;
    }

    public void selectData(String table, String[] columns) {
        StructSelectData.setColumns(columns);
        ArrayList<String[]> selectData = new ArrayList<>();
        String s_columns = "";
        String[] strfromtb = new String[columns.length]; // массив под данные
        for (int i = 0; i < columns.length; ++i) { //формирование строки запроса
            if (i < columns.length - 1) {
                s_columns += "\"" + columns[i] + "\"" + ", ";
            } // Кавычки для руских имен и пробелов
            else {
                s_columns += "\"" + columns[i] + "\"";
            }
        }
        try {
            stmt = connection.createStatement();
            System.out.println("SELECT " + s_columns + " FROM " + table + ";");
            ResultSet rs = stmt.executeQuery("SELECT " + s_columns + " FROM " + table + ";");
            while (rs.next()) {
                for (int i = 0; i < columns.length; ++i) {
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
        } catch (SQLException e) {
            System.out.println("Failed select data");
            e.printStackTrace();
        }
        //return currentSelectTable;
    }

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

//    public ArrayList<String[]> selectDataGPAAI(String table) {
//        //connectionToBase(); // вызов Фукция подключения к базе
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("Tag name");
//                String id = uuid.getUIID();
//                String nsign = rs.getString("Наименование сигнала");
//                String[] strfromtb = {TypeADC, id, nsign};
//                selectData.add(strfromtb);
//                //System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s UM=%s TYPEADC=%s A1=%s A2=%s" ,id,name,age,address,salary,UM,TypeCh,TypeADC,addres1,addres2));
//            }
//            rs.close();
//            stmt.close();
//            //connection.commit();
//            System.out.println("-- Operation SELECT done successfully");
//        } catch (SQLException e) {
//            System.out.println("Failed ADD data");
//            e.printStackTrace();
//            //return;
//        }
//        return selectData;
//    }
//
//    public ArrayList<String[]> selectDataGPAAO(String table) {//посик данных для выходныъ аналоговых
//        // column всегда три
//        ArrayList<String[]> selectData = new ArrayList<>();//не знаю добавлять три или нет
//        try {
//            stmt = connection.createStatement();//подключаемся
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {//цикл от одной строки к следующей
//                String TypeADC = rs.getString("Tag name");//ищу колонну по названию
//                String id = uuid.getUIID();//и это в базе
//                String namesign = rs.getString("Наименование сигнала");//и это тоже
//
//                String[] str = {TypeADC, id, namesign};//по циклу вообще все правильно,не знаю почему не работает
//                selectData.add(str);
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("-- Operation SELECT done successfully");
//        } catch (SQLException e) {
//            System.out.println("Failed ADD data");
//            e.printStackTrace();
//        }
//        return selectData;
//    }
//
//    public ArrayList<String[]> selectDataAO(String table) {
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("Tag name");
//                String id = uuid.getUIID();//здесь необходимо решить как менять рандом UUID ибо получается на каждый сигнал одна генерация 
//                String namesign = rs.getString("colum_6");//надо подумать ибо я пока не знаю как это сделать
//
//                String[] str = {TypeADC, id, namesign};
//                selectData.add(str);
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("--Operation SELECT done sucessfully");
//        } catch (SQLException ex) {
//            System.out.println("Failed ADD data");
//            ex.printStackTrace();
//        }
//        return selectData;
//    }
//
//    public ArrayList<String[]> selectDataAO_HMI(String table) {//здесь мы вызываем из базы для AO PLC ,пиздц муть
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("colum_20");
//                String id = uuid.getUIID();
//                String namesign = rs.getString("colum_6");
//
//                String[] str = {TypeADC, id, namesign};
//                selectData.add(str);
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("--Operation SELECT done sucessfully");
//        } catch (SQLException ex) {
//            System.out.println("Failed ADD data");
//            ex.printStackTrace();
//        }
//        return selectData;
//    }
//
//    public ArrayList<String[]> selectDataAO_DRV(String table) {//AO DRV вот т ут не знаюстоило ли этоделать 
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("colum_18");
//                String id = uuid.getUIID();
//                String namesign = rs.getString("colum_6");
//
//                String[] str = {TypeADC, id, namesign};
//                selectData.add(str);
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("--Operation SELECT done sucessfully");
//
//        } catch (SQLException ex) {
//            System.out.println("Failed ADD data");
//            ex.printStackTrace();
//        }
//        return selectData;
//    }
//
//    public ArrayList<String[]> selectDataGPA_DO(String table) {
//
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("Tag name");
//                String id = uuid.getUIID();
//                String namesign = rs.getString("Наименование сигнала");
//
//                String[] str = {TypeADC, id, namesign};
//                selectData.add(str);
//
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("--Operation SELECT done sucessfully");
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();
//        }
//        return selectData;
//
//    }
//
//    public ArrayList<String[]> selectDataGPA_DI(String table) {
//
//        ArrayList<String[]> selectData = new ArrayList<>();
//        try {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
//            while (rs.next()) {
//                String TypeADC = rs.getString("Tag name");
//                String id = uuid.getUIID();
//                String namesign = rs.getString("Наименование сигнала");
//
//                String[] str = {TypeADC, id, namesign};
//                selectData.add(str);
//            }
//            rs.close();
//            stmt.close();
//            System.out.println("--Operation SELECT done sucessfully");
//
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();
//        }
//        return selectData;
//    }
//----Строим все сигналы которые сюда ссылаются
    public ArrayList<String[]> getSelectData(String table) {

        ArrayList<String[]> selectData = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM " + table + ";");
            while (rs.next()) {
                String TypeADC = rs.getString("Num_40");
                String id = uuid.getUIID();
                String namesig = rs.getString("Num_5");

                String[] str = {TypeADC, id, namesig};
                selectData.add(str);
            }
            rs.close();
            stmt.close();
            System.out.println("--Operation SELECT done sucessfully");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return selectData;
    }

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

    List<String> listBase() {
        ArrayList<String> listBase = new ArrayList();
        //connectionToBase(); // вызов Фукция подключения к базе
        try {
            stmt = connection.createStatement();
            String sql = "SELECT datname FROM pg_database;";
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
        return null;
    }

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
            System.out.println("-- SELECT TABLE BASE");
        } catch (SQLException e) {
            System.out.println("Failed SELECT TABLE BASE");
            e.printStackTrace();
        }
        return list_table_base;
    }

    public static void main(String[] args) {
        //new BasePostgresLuaXLS().createBase("test6671"); // Так создаем 
        List<String> tmp = new DataBase().listBase(); // Так смотрим
        for (String s : tmp) {
            System.out.println(s);
        }
    }

      // ---  Обновиьт данные простой запрос(Таблица, столбец, текущие данные, новые данные, массив всех данных/условие)  ---
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
    String[] getColumns() {
        return columns;
    }

    public ArrayList<String[]> getcurrentSelectTable() {
        return currentSelectTable;
    }
}
