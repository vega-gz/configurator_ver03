/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import globalData.globVar;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 *
 * @author nazarov
 */
// класс для подклчения и слияния базз
public class MergeBases {

    String dbURL = null;
    String currentBase = null;
    String USER = null;
    String PASS = null;
    DataBase aDB; // база к которой подключаемся
    DataBase currentDB = globVar.DB; // запрос к текущей базе
    private int statusConnect = 2;
    ArrayList<DiffDataTable> listTableDiff = new ArrayList<>(); // Храним объекты различия текущей таблицы 

    public MergeBases(String dbURL, String currentBase, String USER, String PASS) {
        this.dbURL = dbURL;
        this.currentBase = currentBase;
        this.USER = USER;
        this.PASS = PASS;
    }

    public int connectAnotherDB() {
        aDB = new DataBase(dbURL, currentBase, USER, PASS);
        statusConnect = aDB.statusConnectDB;

        return statusConnect;

    }

    public void editBases() {
        // Запросы к базам
        if (connectAnotherDB() == 0) { // запуск если статус подключения есть
            ArrayList<String> tableNotEnter = new ArrayList<>(); // Список таблиц нет вновь подключенной базе
            ArrayList<String> tableCurrentDB = currentDB.getListTable();
            ArrayList<String> tableADB = aDB.getListTable();
            Collections.sort(tableCurrentDB); // сортируем листы
            Collections.sort(tableADB);
            if (!tableCurrentDB.equals(tableADB)) { // если не совпадают списки таблиц
                System.out.println("tableCurrentDB size " + tableCurrentDB.size());
                System.out.println("tableADB size " + tableADB.size()); 
                
                for (String currentT : tableCurrentDB) {
                    boolean findT = false;
                    for (String aT : tableADB) {
                        if (currentT.equals(aT)) { // Названия таблиц совпали
                            findT = true;
                            break;
                        }
                    }
                    if(!findT){
                        tableNotEnter.add(currentT);
                    } // не нашли такую таблицу заносим в список
                   }
            } // это должен быть код когда список таблиц одинаков у 1 и второй базы
            else {
                System.out.println("Table fit");
            }
            // переборка таблиц(распознаем различия)
            for (String currentT : tableCurrentDB) {
                    for (String aT : tableADB) {
                        if (currentT.equals(aT)) { // Названия таблиц совпали
                            //System.out.println("Tables fit " + aT);
                            ArrayList<String[]> columnNotFit = new ArrayList<>(); // лист не совподающих данных строке в таблице
                            ArrayList<String> listColumnCurr = currentDB.getListColumns(aT); // колонкам  текущей таблицы
                            ArrayList<String> listColumnADB = aDB.getListColumns(aT); // колонкам  таблицы вновь подключенному
//                            Collections.sort(listColumnCurr);
//                            Collections.sort(listColumnADB);
                            if (listColumnCurr.equals(listColumnADB)) { // если колонки совпали сравниваем данные в таблице
                                ArrayList<String[]> dataFromTableC = currentDB.getData(aT); // данные таблицы
                                ArrayList<String[]> dataFromTableA = aDB.getData(aT); // 

                                for (String[] columnCurr : dataFromTableC) {
                                    boolean dataFit = false;
                                    for (String[] columnADB : dataFromTableA) {// проходим по колонкам таблицы вновь подключенному
                                        if (columnCurr.length == columnADB.length) {
                                            for (int i = 1; i < columnCurr.length; ++i) { // с певой так как первый это id
                                                if (columnCurr[i].equals(columnADB[i])) { // сравниваем данные
                                                    //System.out.print( " fit " + columnCurr[i] + " " +columnADB[i]);
                                                    dataFit = true;  // 
                                                } else {
                                                    //System.out.println( " Diff " + columnCurr[i] + " " +columnADB[i]);
                                                    dataFit = false;
                                                    break;
                                                }
                                            }
                                            // если строки совпадают дальше смысла читать нет и триггер собъется
                                            if (dataFit == true) {
                                                break;
                                            }
                                        }
                                    }
                                    if (dataFit == false) {
                                        columnNotFit.add(columnCurr); // заносим данные из талицы не совпадающие с новой базой
                                    }

                                }
                                System.out.println("Table " + aT + "size Diff list " + columnNotFit.size());
                                if (columnNotFit.size() > 0) listTableDiff.add(new DiffDataTable(aT, columnNotFit, listColumnCurr)); // Занисом наши новые объекты в список если есть различия
                            }
                        }

                    }
                }
            // вывод сообщения
            String diffT = "";
            if (tableNotEnter.size() > 0){
                diffT = "Таблицы в которых есть различия" + currentBase + "\n";
                for(DiffDataTable difData: listTableDiff){
                    String nameDT = difData.getName();
                    diffT += nameDT +"\n";
                }
                JOptionPane.showMessageDialog(null, diffT);
            }
            
            
            
        }
    }

    // --- Объект хранения различающихся данных таблиц --- (может быть еще какие то хранения данных)
    class DiffDataTable {

        private String name;
        private ArrayList<String> columns; // колонки таблицы
        private ArrayList<String[]> diffData; // лист того что не совпало

        public DiffDataTable(String name) {
            this.name = name;
        }

        public DiffDataTable(String name, ArrayList<String[]> diffData) {
            this(name); // вызов конструктора с именем
            this.diffData = diffData;
        }
        
        public DiffDataTable(String name, ArrayList<String[]> diffData, ArrayList<String> columns) {
            this(name, diffData); // вызов конструктора с именем и данными
            this.columns = columns;
        }
        
        
        // Методы для удобства редактирования и получения данных
        public void setData(ArrayList<String[]> diffData) {
            this.diffData = diffData;
        }
        
        public void setСolumns(ArrayList<String> columns) {
            this.columns = columns;
        }
        
        public String getName( ) {
            return name;
        }
        
        public ArrayList<String[]> getData( ) {
            return diffData;
        }

    }
}
