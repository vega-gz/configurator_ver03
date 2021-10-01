/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Table;

import DataBaseTools.DataBase;
import globalData.globVar;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nazarov
 * 
 * Обновление базы в реальном режиме изменения таблицы
 * 
 */
public class ConnectBaseTable extends DefaultTableModel {

    NZDefaultTableModel nz;
    DataBase workbase = globVar.DB;

    public ConnectBaseTable(NZDefaultTableModel nz) {
        super(nz.getDataVector(), nz.getColumns());
        nz.getDataVector(); 
        this.nz = nz;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return nz.isCellEditable(row, column);
    }
    @Override
    public void setValueAt(Object aValue, int row, int column) {   
         if (column == 0) {
            return;
        } 
        
        Vector rowData = (Vector) getDataVector().get(row); // Получаем список значений аналог Листа
        String ColumnName = this.getColumnName(column);
        int ColumnTM = this.getColumnCount();
        HashMap< String, String> mapDataRow = new HashMap<>(); // элементы для отображения в этих полях
        for (int i = 0; i < ColumnTM; ++i) { // Пробежать по строке где изменяются данные и сформировать список для обновления данных в базе c 1 так как там галки
            mapDataRow.put(this.getColumnName(i), (String) rowData.get(i)); // Формируем список данных принудительно в String
        }
        workbase.Update(nz.nameTable, ColumnName, (String) aValue, mapDataRow); // обновить данные ячейки в таблицы базы
        
        rowData.set(column, aValue); // Вставляем новые данные в нужную ячейку( только после этого вставляем ячейку иначе в базу неправильный запрос пойдет)            
    }
    
    @Override
    public void insertRow(int row,Object aValue[]){
        super.insertRow(row, aValue);
        //workbase.getSetingsSignal((String[])aValue); // Вызов недоделанного метода? 
        System.out.println("Debug 01102021");
    }
}
