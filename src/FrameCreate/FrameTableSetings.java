/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FrameCreate;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author nazarov
 * 
 * Отображение сигналов
 * 
 */
public class FrameTableSetings extends TableDB{
    private String[][] dataToTable = null;
    private String[] columnTable;
    
    public FrameTableSetings(String table, String[] columnTable, String[][] dataToTable ) {
        this.dataToTable = dataToTable;
        this.columnTable = columnTable;
        initialUserConfig(table);
        getTable().setDefaultEditor(Object.class, null); // запрет редактирования таблицы
        
    }
    
    @Override
    protected boolean setDate(String nameTable){
        getTableModel().setColumnIdentifiers(columnTable);
        for(String[] arr: dataToTable){
            getTableModel().addRow(arr);
        }
        colsWidth = new int[columnTable.length];
        align  = new int[columnTable.length]; // Этот идиотизм из родителя
        cols = columnTable;
        return true;
    }
    
     private void jTable1MouseClicked(java.awt.event.MouseEvent evt) { 
        // System.out.println();
     }
    
    @Override
    protected void configTable(String tableName) {
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //e.getWindow().setVisible(false);
                 e.getWindow().dispose();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowActivated(WindowEvent e) {
                 //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
}
