/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*@author cherepanov
 */public class StructSelectData {

    static private ArrayList<String[]> currentSelectTable;
    static private String[] columns;
    static private String nTable;

    public static String[] getColumns() {
        return columns;
    }

    public static String getnTable() {
        return nTable;
    }

    public static String[][] getcurrentSelectTable() {
        Iterator<String[]> iter_arg = currentSelectTable.iterator();
        // длинна итератора для двойного массива таблицы
        int i = 0;
        int j = 0;
        while (iter_arg.hasNext()) {
            i++;
            j = iter_arg.next().length; // длинна массива в итераторе что то ближе к правде но все же нужен if на всякий
        }
        iter_arg = currentSelectTable.iterator();// заново так как next прошелся уже

        String[][] data = new String[i][j];
        int i1 = 0;
        while (iter_arg.hasNext()) {
            String[] mas_int_i = iter_arg.next();

            for (int i2 = 0; i2 < mas_int_i.length; ++i2) {
                data[i1][i2] = mas_int_i[i2];
                // System.out.print(mas_int_i[i2]);
            }
            ++i1;
        }
        //String[][] data = currentSelectTableю
        return data;
    }

    public static void setColumns(String[] columns) {
        StructSelectData.columns = columns;
    }

    static void setColumns(ArrayList<String> col) {
        // Костыли =(
        columns = new String[col.size()]; 
        for (int i = 0; i < col.size(); ++i) {
            StructSelectData.columns[i] = col.get(i);
        }

    }

    static void setnTable(String nTable) {
        StructSelectData.nTable = nTable;
    }

    static void setcurrentSelectTable(ArrayList<String[]> currentSelectTable) {
        StructSelectData.currentSelectTable = currentSelectTable;
    }
}
