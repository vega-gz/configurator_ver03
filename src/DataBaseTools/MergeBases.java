/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import globalData.globVar;
import java.util.ArrayList;
import java.util.Collections;

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

    public MergeBases(String dbURL, String currentBase, String USER, String PASS) {
        this.dbURL = dbURL;
        this.currentBase = currentBase;
        this.USER = USER;
        this.PASS = PASS;
    }

    public int connectAnotherDB() {
        aDB = new DataBase(dbURL, currentBase, USER, PASS);
        int statusConnect = aDB.statusConnectDB;

        return statusConnect;

    }

    private void editBases() {
        // Запросы к базам
        ArrayList<String> tableCurrentDB = currentDB.getListTable();
        ArrayList<String> tableADB = aDB.getListTable();
        Collections.sort(tableCurrentDB); // сортируем листы
        Collections.sort(tableADB);
        if (!tableCurrentDB.equals(tableADB)) { // если не совпадают списки таблиц

            for (String currentT : tableCurrentDB) {
                for (String aT : tableADB) {

                }

            }
        }
    }
}
