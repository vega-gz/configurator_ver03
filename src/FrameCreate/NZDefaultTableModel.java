/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import DataBaseConnect.DataBase;

/**
 *
 * @author ad
 */
    // --- Собственный класс таблиц из за своих методов ---
public class NZDefaultTableModel extends DefaultTableModel { // название столбцов resultColumn   
      Object[][] dataInTable = null;
      String[] resultColumn = null;
      String nameTable = null;
      DataBase workbase = DataBase.getInstance();// создаем запрос к базе
      
      // просто построить данные
      public NZDefaultTableModel (Object[][] dataInTable, String[] resultColumn){
        super(dataInTable, resultColumn);
        this.dataInTable = dataInTable;
        this.resultColumn = resultColumn;
       }
      // Строим конструктором таким для редактирования таблицы
      public NZDefaultTableModel (Object[][] dataInTable, String[] resultColumn, String nameTable){
        super(dataInTable, resultColumn);
        this.dataInTable = dataInTable;
        this.resultColumn = resultColumn;
        this.nameTable = nameTable;
       }
      
//            @Override
//            public Class<?> getColumnClass(int columnIndex) { // структура для отображения таблицы с галками
//                Class clazz = String.class;
//                switch (columnIndex) {
//                    case 0:
//                        clazz = Boolean.class;
//                        break;
//                }
//                return clazz;
//            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == column;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                // Условие проверки галочки скрывать легенду
                if (aValue instanceof Boolean && column == 0) {
                    //System.out.println("Posution - > " + row + " " + aValue);        
                    Vector rowData = (Vector) getDataVector().get(row); // не помню для чего но без этого только скрывает =(
                    rowData.set(0, (boolean) aValue);
                    fireTableCellUpdated(row, column);

                    try {
                        // Само действие не реализованно
                        if ((boolean) aValue == true) {
                            System.out.println("true");
                        }
                        if ((boolean) aValue == false) {
                            System.out.println("false");
                        }
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(null, "Трудности с добавлением");
                    }
                } else {  // если нет Галки просто обновляем данные
                    if (workbase != null & nameTable != null) { // проверка на редактирования и таблица и экземпляр баз
                        Vector rowData = (Vector) getDataVector().get(row); // не помню для чего но без этого только скрывает =(
                        //String curentCell = (String) rowData.get(column); // получить предыдущие данные
                        //String ColumnName = jTable1.getColumnName(column); // Имя выделенного столбца
                        String ColumnName = resultColumn[column];
                        //int rowTM = jTable1.getRowCount(); // количество строк
                        //int ColumnTM =  jTable1.getColumnCount();  //Количество столбцов
                        int ColumnTM = resultColumn.length;
                        HashMap< String, String> mapDataRow = new HashMap<>(); // элементы для отображения в этих полях
                        for (int i = 1; i < ColumnTM; ++i) { // Пробежать по строке где изменяются данные и сформировать список для обновления данных в базе c 1 так как там галки
                            //mapDataRow.put(jTable1.getColumnName(i), (String) rowData.get(i)); // Формируем список данных принудительно в String
                            mapDataRow.put(resultColumn[i], (String) rowData.get(i)); // Формируем список данных принудительно в String
                        }
                        workbase.Update(nameTable, ColumnName, (String) aValue, mapDataRow); // обновить данные ячейки в таблицы базы
                        rowData.set(column, aValue); // Вставляем новые данные в нужную ячейку( только после этого вставляем ячейку иначе в базу неправильный запрос пойдет)
                        //System.out.println("columnTM " + columnTM + " yT " + yT  );
                    }
                }

            }
             // --- Собственный метод получить данные из таблицы по имени столбца  ---
            public Object getDataNameColumn(String nameColumn, int row){
                Object objTable = null;
                for(int i=0; i<resultColumn.length; ++i){ // Пробегаем по всем нашим именам столбцов как они стоят 
                    if(nameColumn.equals(resultColumn[i])){ //  нашли совпадение
                        objTable = getValueAt(row, i); // -1 Массив мы видиот от 0 а ячейки он видит от 1
                    }
                }
                return objTable;
            }
        }
   
