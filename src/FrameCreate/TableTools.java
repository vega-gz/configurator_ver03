package FrameCreate;

import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/*@author Lev*/
// ----- Функция для настройки свойств таблицы --------------Lev---
public class TableTools {//ссылка на таблицу, массив ширин столбцов, массив алигнов - -1 лево, 0 - центр, 1 - право
    static int setTableSetting(JTable jTable1, int[] colWidth, int[] align){
        if(jTable1==null) return -1;
        jTable1.setRowSelectionAllowed(true);           // Разрешаю выделять по строкам
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);    // Разрешение выделения столбца
        columnModel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION   );// Режим одиночного выделения
        int qCol = jTable1.getColumnCount();                //Определяю количество столбцов
        if(qCol > colWidth.length) qCol = colWidth.length;  //чтобы не вылететь за границы таблицы, если переданный массив неправильный
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //запрещаю изменение ширин солбхов при растягивании окна
        for(int i=0; i<qCol; i++ )                          //устанавливаю ширину всех столбцов
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(colWidth[i]);
        //Работа с алигнами
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.LEFT);
        
        if(qCol > align.length) qCol = colWidth.length;
        for(int i=0; i<qCol; i++ ){
            if(align[i]<0) jTable1.getColumnModel().getColumn(i).setCellRenderer(left);
            else if(align[i]>0) jTable1.getColumnModel().getColumn(i).setCellRenderer(right);
            else jTable1.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        //Работа с высотой заголовков столбцов, чтобы туда влезали многострочные заголовки (пока неудачно)
        JTableHeader th = jTable1.getTableHeader();
        int width = th.getSize().width;
        th.setSize(width, 50);
        return 0;
    }
    
    // ----- Функция для настройки контекстного меню таблиц--------------Lev---
    static int setPopUpMenu(JTable jTable1, JPopupMenu popupMenu, DefaultTableModel tableModel){
        JMenuItem menuItemAdd = new JMenuItem("Добавить пустую строку");
        JMenuItem menuItemCopy = new JMenuItem("Скопировать строку");
        JMenuItem menuItemRemove = new JMenuItem("Удалить строку");
        JMenuItem menuItemRemoveAll = new JMenuItem("Очистить ячейки");

        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemCopy);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemRemoveAll);
        jTable1.setComponentPopupMenu(popupMenu);
        
        menuItemAdd.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0) row = jTable1.getRowCount()-1;
            tableModel.insertRow(row+1, new String[1]);
        });
        menuItemCopy.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0){
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            String[] r = getRow(jTable1, row);
            tableModel.insertRow(row+1, r);
            setId(jTable1);
        });
        menuItemRemove.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0){
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить строку "+(row+1)+"?"); // сообщение с выбором
            if(casedial != 0) return; //0 - yes, 1 - no, 2 - cancel            
            tableModel.removeRow(row);
            setId(jTable1);
        });
        menuItemRemoveAll.addActionListener((ActionEvent event) -> {
            int rows[] = jTable1.getSelectedRows();
            int cols[] = jTable1.getSelectedColumns();
            if(rows.length==0 || cols.length==0){
                JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить содержимое ячеек?"); // сообщение с выбором
            if(casedial != 0) return; //0 - yes, 1 - no, 2 - cancel    
            for(int i=0; i< rows.length; i++) 
                for(int j=0; j< cols.length; j++)
                    jTable1.setValueAt("",rows[i], cols[j]);
        });
        return 0;
    }
    // ----- Функция для расстановки номеров строк в первом столбце --------------Lev---
    static void setId(JTable jTable1){
        int n = jTable1.getRowCount();
        for(int i=0; i< n; i++) jTable1.setValueAt(i+1,i,0);
    }
    // ----- Функция для считывания строки таблицы --------------Lev---
    static String[] getRow(JTable jTable1, int row){
        int n = jTable1.getColumnCount();
        String[] s = new String[n];
        for(int i=0; i< n; i++) s[i] = jTable1.getValueAt(row, i).toString();
        return s;
    }
}
