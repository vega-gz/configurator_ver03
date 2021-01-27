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
import DataBaseTools.DataBase;
import globalData.globVar;

/**
 *
 * @author ad
 */
    // --- Собственный класс таблиц из за своих методов ---
public class NZDefaultTableModel extends DefaultTableModel { // название столбцов resultColumn   
    //Object[][] dataInTable1 = null;

    String[] resultColumn = null;
    String nameTable = null;
    DataBase workbase = null;//DataBase.getInstance();// создаем запрос к базе

    // просто построить данные
    public NZDefaultTableModel(Object[][] dataInTable, String[] resultColumn) {
        super(dataInTable, resultColumn);
        //this.dataInTable1 = dataInTable;
        this.resultColumn = resultColumn;
        this.workbase = globVar.DB;
    }

    // Строим конструктором таким для редактирования таблицы

    public NZDefaultTableModel(Object[][] dataInTable, String[] resultColumn, String nameTable) {
        super(dataInTable, resultColumn);
        //this.dataInTable1 = dataInTable;
        this.resultColumn = resultColumn;
        this.nameTable = nameTable;
        this.workbase = globVar.DB;
    }

    // отключить редактирование базы когда это не нужно(нет еще таблица или самой базы)
    public void disableEditDB() {
        this.workbase = null;// отключаем редактирование базы

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == column;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        // Условие проверки не трогать 1 столбец
        if (column == 0) {
            JOptionPane.showMessageDialog(null, "поле не редактируется");

        } else {  // если нет Галки просто обновляем данные
            Vector rowData = (Vector) getDataVector().get(row); // Получаем список значений аналог Листа
            if (workbase != null & nameTable != null) { // проверка на редактирования и таблица и экземпляр баз
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
            } else { // без базы просто обновляем
                rowData.set(column, aValue);
            }
        }

    }

    // --- Собственный метод получить данные из таблицы по имени столбца  ---

    public Object getDataNameColumn(String nameColumn, int row) {
        Object objTable = null;
        for (int i = 0; i < resultColumn.length; ++i) { // Пробегаем по всем нашим именам столбцов как они стоят 
            if (nameColumn.equals(resultColumn[i])) { //  нашли совпадение
                objTable = getValueAt(row, i); // -1 Массив мы видиот от 0 а ячейки он видит от 1
            }
        }
        return objTable;
    }
}
