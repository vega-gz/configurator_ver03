/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import FrameCreate.TableDB;
import Tools.MyTableModel;
import javax.swing.JTable;

/**
 *
 * @author cherepanov
 */
public class Update {

    public void ReNameAllData(TableDB table, JTable tableRanamed, int rusName, int tagName) {
        /* переименование тегов в таблице сигналов
            пустые получается переименовывает на то что было
        */
        MyTableModel tableModel = table.getTableModel();
        //int jpgMax = tableModel.getRowCount();
        for (int i = 0; i < tableModel.getRowCount(); i++) {

            int sizeColumnTableRename = tableRanamed.getColumnCount();
            String tmp[] = new String[sizeColumnTableRename]; // данные по таблице
            for (int j = 0; j < sizeColumnTableRename; j++) {
                tmp[j] = (String) tableRanamed.getModel().getValueAt(i, j);
            }
            
            if (!tmp[2].equals("") || !tmp[3].equals("")) {
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
