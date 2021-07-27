/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nazarov
 * 
 * Реализация меню по правой кнопки мыши для таблицы уставок
 * 
 */
public class PopUpMenuJtableSetupsSignal implements InterfacePopmenu{

    JPopupMenu popupMenu;
    ArrayList<String[]> list_cell = new ArrayList<>();
    JTable jTable;

    public PopUpMenuJtableSetupsSignal() {        
        popupMenu = new JPopupMenu();
        JMenuItem menuItemCopyStr = new JMenuItem("Скопировать строки");
        JMenuItem menuItemInsertStr = new JMenuItem("Вставить строки");
        JMenuItem menuItemRemove = new JMenuItem("Удалить строку");
        JMenuItem menuItemCreateStr = new JMenuItem("Создать уставку");

//      menuItemCopyCells.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
//      menuItemIncertCells.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        popupMenu.add(menuItemCopyStr);
        popupMenu.add(menuItemInsertStr);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemCreateStr);

        // Слушатели меню
        menuItemCopyStr.addActionListener((ActionEvent event) -> { // копии строк
            int[] rows  = jTable.getSelectedRows();
            if (rows.length == 0) {
                return;
            }
            list_cell.clear();
            for (int i = 0; i < rows.length; i++) {
                int n = jTable.getColumnCount();
                String[] s = new String[n];
                for (int j = 0; j < n; j++) {
                    Object dataCell = jTable.getValueAt(i, j);
                    if(dataCell != null)
                    {
                        s[j] = jTable.getValueAt(i, j).toString();
                    }else 
                    {
                        s[j] = "";
                    }
                }
                list_cell.add(s);
            }
        });
        
        menuItemInsertStr.addActionListener((ActionEvent event) -> { // вставка строк
            if (list_cell.isEmpty()) {
                return;
            }
            DefaultTableModel tm = (DefaultTableModel) jTable.getModel();
            for (int i = 0; i < list_cell.size(); i++) {
                int row = jTable.getRowCount();
                System.out.println(row);
                tm.insertRow(row,  list_cell.get(i));
            }
        });
        
        menuItemRemove.addActionListener((ActionEvent event) -> { // удалить строки
            int[] rows  = jTable.getSelectedRows();
            if (rows.length == 0) {
                return;
            }
            DefaultTableModel tm = (DefaultTableModel) jTable.getModel();
            for (int i = 0; i < rows.length; i++) {
                 tm.removeRow(i);
            }
        });
        
        
        menuItemCreateStr.addActionListener((ActionEvent event) -> { // создание пустой строки
            DefaultTableModel tm = (DefaultTableModel) jTable.getModel();

            int row = jTable.getRowCount();
            int column = jTable.getColumnCount();
            Integer numberIdColumn = null;
            for (int i = 0; i < jTable.getColumnCount(); i++) {
                if(jTable.getColumnName(i).equalsIgnoreCase("ID")){
                    numberIdColumn = i;
                }
            }
            String[] tmpArr;
            if(numberIdColumn != null){
                tmpArr = new String[column - 1];
            }
            else {
                tmpArr = new String[column];
            }
            
            for (int i = 0; i < tmpArr.length; i++) {
                tmpArr[i] = "";
            }
            tm.insertRow(row, tmpArr);
        });
    }
    

    @Override
    public void setPopMenu(JComponent jTable) {
        //if(JComponent.class.getName().equals("JTable")){
            System.out.println(jTable.getClass().getSimpleName());
            this.jTable = (JTable) jTable;
            jTable.setComponentPopupMenu(popupMenu);
        //}
    }
    
    

}
