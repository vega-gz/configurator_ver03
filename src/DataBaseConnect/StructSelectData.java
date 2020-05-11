/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseConnect;
import java.util.ArrayList;

public class StructSelectData {

    static private ArrayList<String[]> currentSelectTable;
    static private String[] columns;
    //static private String nTable;

    public static void setColumns(ArrayList<String> col) {
        // Костыли =(
        columns = new String[col.size()]; 
        for (int i = 0; i < col.size(); ++i) {
            StructSelectData.columns[i] = col.get(i);
        }
    }

    public static void setcurrentSelectTable(ArrayList<String[]> currentSelectTable) {
        StructSelectData.currentSelectTable = currentSelectTable;
    }

    //public static void setnTable(String selectElem) {
        //StructSelectData.nTable = selectElem;
    //}
}
