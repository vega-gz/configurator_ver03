/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Table;

import SetupSignals.ConfigSig;
import SetupSignals.SettingsSignal;
import SetupSignals.SetupsDataToTables;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nazarov
 * 
 * Обновление базы в реальном режиме изменения таблицы
 * 
 */
public class ConfigSignalsTableModel extends DefaultTableModel {
    SetupsDataToTables dataSetings = null;
    
    
    public ConfigSignalsTableModel(String nameSig) // на каком имени сигнала строим таблицу
    {
        dataSetings = new SettingsSignal(nameSig);
        String[] columnSetings = dataSetings.getNameColumnsToTable();
        String[][] dataToTable = dataSetings.getDataToTable();
        setDataVector(dataToTable, columnSetings);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == column;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {   
        if (column == 0) {
            return;
        } 
        
        Vector rowData = (Vector) getDataVector().get(row); // Получаем список значений аналог Листа
        String ColumnName = this.getColumnName(column);
        int ColumnTM = this.getColumnCount();
        String[] DataRow = new String[ColumnTM]; 
        for (int i = 0; i < ColumnTM; ++i) { 
            DataRow[i] = (String) rowData.get(i); // Формируем список данных принудительно в String
        }
        //workbase.Update(nz.nameTable, ColumnName, (String) aValue, mapDataRow); // обновить данные ячейки в таблицы базы
        
        rowData.set(column, aValue); // Вставляем новые данные в нужную ячейку( только после этого вставляем ячейку иначе в базу неправильный запрос пойдет)            
    }
    
    @Override
    public void insertRow(int row, Object aValue[]){
        int idLocal = dataSetings.addSetingSignal(aValue);
        ConfigSig newCreateSeting = dataSetings.getSetingByIdLocal(idLocal);
        String[] dataInsertRow = newCreateSeting.getData();
        dataInsertRow[0] = Integer.toString(newCreateSeting.getLocalId());
        super.insertRow(this.getRowCount(), dataInsertRow);
        

    }
    
    @Override
    public void removeRow(int row) {
        dataVector.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }
}
