/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import FrameCreate.MergeBasesFrame;
import globalData.globVar;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ArrayList<DiffDataTable> listTableDiff = new ArrayList<>(); // Храним объекты различия текущей таблицы 
    ArrayList<String> tableNotEnter = new ArrayList<>(); // Список таблиц которых нет в текущей базе

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

    // --- занесенение новых данных в вновь подключенной базе---
    public void insertDatAnotherDB() {
        for(DiffDataTable diffOBJ: listTableDiff){
           // System.out.println("diff Table " + diffOBJ.getName());
            ArrayList<String> listColumnCurr = diffOBJ.columns; // колонки  объекта
            ArrayList<String[]> dataFromTableC = diffOBJ.getData(); // данные объекта
            
            // удаляем номера из данных
            for(int i=0; i<dataFromTableC.size(); ++i){
                String[] dataMass = dataFromTableC.get(i);
                dataFromTableC.set(i, Arrays.copyOfRange(dataMass, 1, dataMass.length));
            }
            
            aDB.dropTableWithBackUp(diffOBJ.name); // удаляем таблицу с именем обхекта
            aDB.createTableEasy(diffOBJ.name, listColumnCurr.toArray(new String[listColumnCurr.size()]), "");
            for(String[] dataT: dataFromTableC){
                aDB.insertRow(diffOBJ.name, dataT, listColumnCurr.toArray(new String[listColumnCurr.size()]), 0);
            }
        }
        for(String nameT: tableNotEnter){
            //System.out.println("name not exist Table " + nameT);
            
            ArrayList<String> listColumnCurr = currentDB.getListColumns(nameT); // колонки  текущей таблицы
            ArrayList<String[]> dataFromTableC = currentDB.getData(nameT); // данные таблицы текущей
             // удаляем номера из данных
            for(int i=0; i<dataFromTableC.size(); ++i){
                String[] dataMass = dataFromTableC.get(i);
                dataFromTableC.set(i, Arrays.copyOfRange(dataMass, 1, dataMass.length));
            }
            aDB.createTableEasy(nameT, listColumnCurr.toArray(new String[listColumnCurr.size()]), "");
            for(String[] dataT: dataFromTableC){
                aDB.insertRow(nameT, dataT, listColumnCurr.toArray(new String[listColumnCurr.size()]),0);
            }
        }
        JOptionPane.showMessageDialog(null, "Данные скорей всего загружены,\nно загляните в консоль"); // Сообщение
        
    }

    public void editBases() {
        // Запросы к базам
        if (connectAnotherDB() == 0) { // запуск если статус подключения есть
            
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
                    // не нашли такую таблицу заносим в список
                    if (!findT) {
                        tableNotEnter.add(currentT);
                    }
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
                        ArrayList<String[]> dataFromTableC = currentDB.getData(aT); // данные таблицы текущей
                        ArrayList<String[]> dataFromTableA = aDB.getData(aT); // 

                        if (listColumnCurr.equals(listColumnADB)) { // если колонки совпали сравниваем данные в таблице
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
                                //System.out.println("Table " + aT + "size Diff list " + columnNotFit.size());
                            // Занисом наши новые объекты в список если есть различия
                            if (columnNotFit.size() > 0) {
                                DiffDataTable diffT = new DiffDataTable(aT, columnNotFit, listColumnCurr);
                                diffT.setComment("Данные не совпадают");
                                diffT.setData(dataFromTableC);
                                listTableDiff.add(diffT);
                            }
                        } else { // столбцы в одинаковых таблицах разные
                            DiffDataTable diffT = new DiffDataTable(aT, columnNotFit, listColumnCurr);
                            diffT.setComment("Различаются столбцы");
                            diffT.setData(dataFromTableC);
                            listTableDiff.add(diffT);
                            //System.out.println("Table " + aT + "size Diff list " + columnNotFit.size());
                        }
                    }

                }
            }
            // вывод сообщения
            if (tableNotEnter.size() > 0) {
                String diffT = "";
                int sizeNT = tableNotEnter.size();
                diffT = "Количество таблиц "+ sizeNT+" которых нет в базе " + currentBase + "\n";
                for (int i=0; i<sizeNT; ++i) {
                    String s = tableNotEnter.get(i);
                    diffT += s + "\n";
                    if (i>10){
                        diffT += "....";
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, diffT); // Сообщение
            }
            String diffT = "";
            diffT = "Имена таблиц которые совпали но есть различия " + currentBase + "\n";
            for (int i=0;i<listTableDiff.size();++i) {
                DiffDataTable difData = listTableDiff.get(i);
                String nameDT = difData.getName();
                diffT += nameDT + "\n";
                if (i>10){
                    diffT += "....";
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, diffT); // Сообщение

            MergeBasesFrame frameMergeDB = new MergeBasesFrame(this);
//                frameMergeDB.setDataToFrame(listTableDiff);
            frameMergeDB.show(true);

        }
    }

    // --- Объект хранения различающихся данных таблиц --- (может быть еще какие то хранения данных)
    public class DiffDataTable {

        private String name;
        private ArrayList<String> columns; // колонки таблицы
        private ArrayList<String[]> diffData; // лист того что не совпало
        private ArrayList<String[]> data; // полный лист копии из базы
        public String comment; // коммментарий (если отличаются на пример количество столбцов)

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
        public void setDiffData(ArrayList<String[]> diffData) {
            this.diffData = diffData;
        }

        public void setData(ArrayList<String[]> data) {
            this.data = data;
        }

        public void setСolumns(ArrayList<String> columns) {
            this.columns = columns;
        }

        public String getName() {
            return name;
        }

        public ArrayList<String[]> getData() {
            return data;
        }
        
        public ArrayList<String[]> diffData() {
            return diffData;
        }

        private void setComment(String s) {
            comment = s;
        }

    }
}
