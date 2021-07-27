/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import DataBaseTools.DataBase;
import globalData.globVar;
import java.util.ArrayList;


/**
 *
 * @author nazarov
 */
public class ConfigSigDB implements ConfigSigStorageInterface {
    private DataBase db = globVar.DB;
    private String nameTableSetups = globVar.nameTableSetups;
    private String nameSig = null;
    private String nameColumn1 = "Abonent";
    private String nameColumn2 = "NameSig";
    private String[] columnSetingArr = null;
    
    public ConfigSigDB(String nameSig){
        this.nameSig = nameSig;
        ArrayList<String> columnSetingList = db.getListColumns(nameTableSetups);
        columnSetingArr = columnSetingList.toArray(new String[columnSetingList.size()]);
    }

    @Override
    public ArrayList<ConfigSig> get() {
        //db.getListTable().equals(globVar.abonent + "_" + nameTableSetups);
        ArrayList<ConfigSig> savedConfigsSignal = new ArrayList<>();
        ArrayList<String[]> dataSettingDB = db.getDataCondition(nameTableSetups, new String[][]{{nameColumn1, globVar.abonent}, {nameColumn2, nameSig}}); // поиск данных с выборкой
        for (int i = 0; i < dataSettingDB.size(); i++)
        {   
            String[] arr = dataSettingDB.get(i);
            ConfigSig config = new AnalogSetup();
            config.setData(arr);
            config.setLocalId(i + 1); // с еденицы
            config.setStatus(ConfigSig.StatusSeting.FROMBASE); // установить статус
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
    public void addSignal(ConfigSig s) {
        /*
         Создаем новый ID 
        вносим его в настройку
        записываем строку
        */
        
        String newIdSetting = Integer.toString(db.getLastId(nameTableSetups) + 1);
        s.setId(newIdSetting);
        db.insertRow(nameColumn1, s.getData(), columnSetingArr, null);
    }

    @Override
    public String[] getNameColumnSetings() {
        return columnSetingArr;
    }
    
}
