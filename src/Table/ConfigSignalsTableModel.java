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
public class ConfigSignalsTableModel extends DefaultTableModel implements SaveTable{
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
        rowData.set(column, aValue);
        
        String ColumnName = this.getColumnName(column);
        int ColumnTM = this.getColumnCount();
        String[] DataRow = new String[ColumnTM]; 
        for (int i = 0; i < ColumnTM; ++i) { 
            DataRow[i] = (String) rowData.get(i); // Формируем список данных принудительно в String
        }
        
        ConfigSig editSeting = getConfigSigFromTable(row);
        if (editSeting != null) {
            editSeting.setData(DataRow);
            ConfigSig editSetingAfterEdit = dataSetings.getSetingByIdLocal(row + 1);
            System.out.println(editSetingAfterEdit.getData());
        }
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
        ConfigSig editSeting = getConfigSigFromTable(row);
        if (editSeting != null) {
            dataSetings.removeRowToData(editSeting);
        }
    }

    @Override
    public void saveTable() {
        dataSetings.SaveData();
    }
    
    private ConfigSig getConfigSigFromTable(int row){
        ConfigSig editSeting = null;
        String idColumn = this.getColumnName(0);
        if (idColumn != null && idColumn.equalsIgnoreCase("ID")) {
            editSeting = dataSetings.getSetingByIdLocal(row + 1);
        }
        return editSeting;
    }
}
