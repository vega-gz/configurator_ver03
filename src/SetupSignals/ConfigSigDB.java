/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import DataBaseTools.DataBase;
import globalData.globVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 *
 * @author nazarov
 */
public class ConfigSigDB implements ConfigSigStorageInterface {
    private DataBase db = globVar.DB;
    private String nameTableSetups = "SignalSetups";
    private String TAG_NAME_PLC = "TAG_NAME_PLC";
    private String commentT = "setups signals";
    private String nameSig = null;
    private String nameColumn1 = "Abonent";
    private String nameColumn2 = "NameSig";
    private String[] columnTableDefault = {
        "id", "Abonent", "NameTableFromSignal",
        "Наименование", TAG_NAME_PLC, "Type", "NameSig",
        "Direction", "Delay", "LostSignal", "Value", "ExternalInit"
    }; // Набор столбцов для базы таблицы
    private String[] _ParentColumnsSignal = new String[]{"Точность", "Наименование", "Единица_измерения"}; // данные из родителя.
    
    public ConfigSigDB(){
       checktableSeting();
    }
    
    public ConfigSigDB(String nameSig){
        setNameSig(nameSig);
        checktableSeting();
    }
    
    private void setNameSig(String nameSig){
        this.nameSig = nameSig;
    }
    
    private void checktableSeting(){
        int testfindT = db.getListTable().indexOf(nameTableSetups);
        if (db.getListTable().indexOf(nameTableSetups) < 0) {
            db.createTableEasy(nameTableSetups, columnTableDefault, commentT);
        }      
    }
    
    public void clearDBSetups() {
        db.dropTable(nameTableSetups);
        checktableSeting();
    }

    private String getAccuracyFromParrent(String tableName, String nameSig, String _ColumnFind){
        /*
        достаем данные точности из родителя
        достаем российское название сигнала из родителя
        достаем меру измерения сигнала из родителя
        */
        return db.getDataCell(tableName, TAG_NAME_PLC, nameSig, _ColumnFind); // запрос к методы пиздецки конченный
    }
    
    @Override
    public ArrayList<ConfigSig> getConfigsSignal() {        
        
        ArrayList<ConfigSig> savedConfigsSignal = new ArrayList<>();
        ArrayList<String[]> dataSettingDB = null;
        if(nameSig != null){ // выбор вссех уставок или конкретного
            dataSettingDB = db.getDataCondition(nameTableSetups, new String[][]{{nameColumn1, globVar.abonent}, {nameColumn2, nameSig}}); 
        }else{
            dataSettingDB = db.getDataCondition(nameTableSetups, new String[][]{{nameColumn1, globVar.abonent}});
        }
        
        for (int i = 0; i < dataSettingDB.size(); i++)
        {   
            String[] arr = dataSettingDB.get(i);
            ConfigSig config = new AnalogSetup();
            config.setData(arr);
            config.setLocalId(i + 1); // с единицы
            config.setStatus(ConfigSig.StatusSeting.FROMBASE); // установить статус
            String[] addDataParent = new String[_ParentColumnsSignal.length];
            
            for (int j = 0; j < _ParentColumnsSignal.length; j++) {
                addDataParent[j] = getAccuracyFromParrent(arr[2], arr[6], _ParentColumnsSignal[j]); // таблица и название сигнала
            }
            
            config.setAddetionDataParent(addDataParent);
            savedConfigsSignal.add(config);
        }
        return savedConfigsSignal;
    }

    @Override
    public void removeByIDSignal(ConfigSig s) {
        db.deleteRowId(nameTableSetups, s.getId());
    }

    @Override
    public void editSignal(ConfigSig s) {       
        if(s.getStatus() == ConfigSig.StatusSeting.FROMBASE)
        {
            removeByIDSignal(s);
            addSignal(s);
        }else
        {
            System.out.println("Edit setting " + s.getName() + "not posible, not ID");
        }
    }

    @Override
    public void addSignal(ConfigSig s) { // тут момент нужно ли нам id вычислять для внесения
        /*
         Создаем новый ID 
        вносим его в настройку
        записываем строку
        */ 
        if(s.getStatus() == ConfigSig.StatusSeting.FROMFILE)
        {
            setNameSig(s.getNameSignalSeting());       
            for (ConfigSig sigBase : getConfigsSignal()) {
                if(sigBase.equals(s)){
                    removeByIDSignal(sigBase);
                    
                }
            }
        }
        String newIdSetting = Integer.toString(db.getLastId(nameTableSetups) + 1);
        s.setId(newIdSetting);
        db.insertRow(nameTableSetups, s.getData(), columnTableDefault, null);
        s.setStatus(ConfigSig.StatusSeting.FROMBASE);
    }

    @Override
    public String[] getNameColumnSetings() {
        // возращает столбцы для постройки данных таблицы
        // оригинальные что есть в базе и что выдергиваем из родительского сигнала
        String[] tmpnameParent = new String[_ParentColumnsSignal.length];
        for (int i = 0; i < _ParentColumnsSignal.length; i++) {
            tmpnameParent[i] = _ParentColumnsSignal[i] + "_SIGNAL";
        }
        return Stream.concat(Arrays.stream(columnTableDefault), Arrays.stream(tmpnameParent)).toArray(String[]::new);
        //return columnTableDefault;
    }
    
}
