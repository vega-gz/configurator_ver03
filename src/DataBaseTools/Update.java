/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import FrameCreate.TableDB;
import Tools.MyTableModel;
import javax.swing.JProgressBar;

/**
 *
 * @author cherepanov
 */
public class Update {
    public void ReNameAllData(MyTableModel tableModel,TableDB table,String tmp[],int rusName,int tagName){
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
                table.tableModel.setValue(tmp[2], i, rusName);
                table.tableModel.setValue(tmp[3], i, tagName);
            }
        }
    }
}
