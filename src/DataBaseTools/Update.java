/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import FrameCreate.TableDB;
import Tools.MyTableModel;

/**
 *
 * @author cherepanov
 */
public class Update {

    public void ReNameAllData(TableDB table, String tmp[], int rusName, int tagName) {
        MyTableModel tableModel = table.getTableModel();
        int jpgMax = tableModel.getRowCount();
        for (int i = 0; i < tableModel.getRowCount(); i++) {

            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                tmp[j] = tableModel.getValueAt(i, j);
            }
            if (!tmp[2].equals("") || !tmp[3].equals("")) {//если хоть одна ячейка из двух не пустая,обновляем строку
                if (tmp[2].equals("")) {
                    tmp[2] = tmp[0];
                }
                if (tmp[3].equals("")) {
                    tmp[3] = tmp[1];
                }
                tableModel.setValue(tmp[2], i, rusName);
                tableModel.setValue(tmp[3], i, tagName);
            }
        }
    }
}
