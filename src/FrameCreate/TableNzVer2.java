/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import configurator.StructSelectData;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Stream;
//import javafx.beans.value.ChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import DataBaseConnect.DataBase;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

/**
 *
 * @author nazarov
 */

// --- строиться таблица внутри  JPanel -- 
public class TableNzVer2 extends javax.swing.JPanel {

    String nameTable = null;
    DataBase workbase = null; // создаем пустой запрос к базе
    ArrayList<ArrayList> listData = null; // Массив для  перебора в запросе
    String[] columns = null; // Колонки таблицы

    public TableNzVer2(ArrayList<ArrayList> listData) { // В реализации Механизмов вызывается это
        this.listData = listData;
        //getModelTable(workbase, nameTable, listData); // вызываем функцию с пустым запросом к базе
        initComponents();
    }

    public TableNzVer2() { // обязательно что то должно быть, так получаем Tableodel
        //getModelTable(workbase, nameTable, listData); // вызываем функцию с пустым запросом к базе
        //initComponents();
         this.workbase = DataBase.getInstance();
    }

    public TableNzVer2(String nameTable) {
        this.nameTable = nameTable;
        this.workbase = DataBase.getInstance();
        //getModelTable(workbase, nameTable, listData); // вызываем функцию с пустым запросом к базе
        initComponents();
    }

    TableModel getModelTable() {
        return getModelTable(nameTable, columns, listData); // вызываем функцию с пустым запросом к базе
    }

    // --- таблица с Подключение к базе и какой таблице --- 
    TableModel getModelTable(String nameTable, String[] columns, ArrayList<ArrayList> listData) { // Надо передавать сюда и названи столбцов
        //String[] columnDop = {"Выбор"};// до поля для галок или еще чего
        final String[] resultColumn;// = null;
        // вынужденная мера пока что
        String[] columnNames;
        if (StructSelectData.getColumns() != null) {
            columnNames = StructSelectData.getColumns();
        } else {
            columnNames = null;
        }
        Object[][] data;
        if (StructSelectData.getColumns() != null) {
            data = StructSelectData.getcurrentSelectTable(); // От куда беру данные( а вот тут вопрос)
        } else {
            data = null;
        }

        Object[] streamArray;
        //Object[] streamNull = new Object[1];
        //streamNull[0] = null; // нулевая первая ячейки
        Object[][] dataInTable = null; // Это данные в самой таблице
        
        if (columns != null) { // если колонки для столбцов базы не пусты то просто инициализируем
            dataInTable = new Object[listData.size()][]; // Сколько строк
            //resultColumn = Stream.concat(Arrays.stream(columnDop), Arrays.stream(columns)).toArray(String[]::new); // сформированный массив присоединяем к массиву Выбор
            resultColumn = columns;
            for (int i = 0; i < listData.size(); ++i) {
                    ArrayList<String> list = listData.get(i);
                    String[] sList = new String[list.size()];
                    for (int j = 0; j < list.size(); ++j) {
                        sList[j] = list.get(j);
                    }
                    //sList = (String[]) list.toArray(); // принудительно преобразуем
                    // прикручиваем к данным
                    streamArray = new Object[columns.length]; // длинна объекта +1 галочка
                    //Object[] testStream = Stream.concat(Arrays.stream(streamNull), Arrays.stream(sList)).toArray(Object[]::new);// преобразовываем массив в 1 первая булевая
                    Object[] testStream = sList;
                    dataInTable[i] = testStream;
                }
        } else {
            if (listData != null) { // так преабазуем Массив листов в двойной массив что бы код ниже не ковырять(при условие что сюда подставили ArrayList)
                String[] nameFromList = null;
                dataInTable = new Object[listData.size()][]; // Сколько строк
                int colColumn = 0; // количество столбцов которое будет в таблице
                for (ArrayList list : listData) {// пробежать узнать максивальное что бы построить кол столбцов
                    if (list.size() > colColumn) {
                        colColumn = list.size();
                    }
                }
                String[] massColumnList = new String[colColumn]; // Масив длинной колонок из листа
                for (int i = 0; i < massColumnList.length; ++i) {
                    massColumnList[i] = "data" + Integer.toString(i); // Название столбцов
                }
                // --- тут повторяю надо поправить ---
                for (int i = 0; i < listData.size(); ++i) {
                    ArrayList<String> list = listData.get(i);
                    String[] sList = new String[list.size()];
                    for (int j = 0; j < list.size(); ++j) {
                        sList[j] = list.get(j);
                    }
                    //sList = (String[]) list.toArray(); // принудительно преобразуем
                    // прикручиваем к данным
                    streamArray = new Object[colColumn + 1];
                    //Object[] testStream = Stream.concat(Arrays.stream(streamNull), Arrays.stream(sList)).toArray(Object[]::new);// преобразовываем массив в 1 первая булевая
                    Object[] testStream = sList;
                    dataInTable[i] = testStream;
                }
                //resultColumn = Stream.concat(Arrays.stream(columnDop), Arrays.stream(massColumnList)).toArray(String[]::new); // сформированный массив присоединяем к массиву Выбор
                resultColumn = massColumnList;
            } else {
                //resultColumn = Stream.concat(Arrays.stream(columnDop), Arrays.stream(columnNames)).toArray(String[]::new); // соединяем два массива
                resultColumn = columnNames;
                dataInTable = new Object[data.length][];
                for (int i = 0; i < data.length; i++) {
                    streamArray = new Object[data[i].length + 1];
                    //Object[] testStream = Stream.concat(Arrays.stream(streamNull), Arrays.stream(data[i])).toArray(Object[]::new);// преобразовываем массив в 1 первая булевая
                    Object[] testStream = data[i];
                    dataInTable[i] = testStream;
                }
            }
        }
        return new NZDefaultTableModel(dataInTable, resultColumn, nameTable); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(getModelTable());
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // --- Обработка нажатия клавиш ---
    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) { // right mouse
            int row = jTable1.rowAtPoint(evt.getPoint()); // хитрое вычисление где нажато
            int column = jTable1.columnAtPoint(evt.getPoint());
            // if (column == 0){        
            ++column;
            String nameGra = (String) jTable1.getValueAt(row, column); // так получим имя графика
            PopUpDemo menu = new PopUpDemo();
            menu.show(evt.getComponent(), evt.getX(), evt.getY());
            //}
            // перерисовываем таблицу из данных новых графиков

            //jTable1.getColumnModel().getColumn(0).setCellRenderer(new myTableCellRenderer_ver2(tmpC));       
            JTableHeader th = jTable1.getTableHeader();
            th.repaint(); // без этого не работает отрисовка , тут с ней тоже
        }
    }//GEN-LAST:event_jTable1MousePressed

   

// -- мини меню по мыши первого столбца таблицы---
    class PopUpDemo extends JPopupMenu {
        JMenuItem anItem;
        int i1 = 0;
        public PopUpDemo() {
            //anItem = new JMenuItem("Click Me!");
            JSlider slider = new JSlider();
            //add(anItem);
            add(slider);
            int value = slider.getValue();
            JMenuItem anItem2;
            anItem2 = new JMenuItem(Integer.toString(value));
            add(anItem2);
            slider.addChangeListener(new javax.swing.event.ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int value = ((JSlider) e.getSource()).getValue();
                    //System.out.println(value);
                }
            });
        }
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}



