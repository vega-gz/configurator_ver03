/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import SetupSignals.ConfigSig.StatusSeting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author nazarov
 * 
 * Класс описывающий список Уставок
 * 
 */
public class SettingsSignal implements SetupsDataToTables{
    private ArrayList<ConfigSig> listSettings = null;
    private ConfigSigStorageInterface containSetting = null;
    private String nameSig = null;
    
    public SettingsSignal()
    {
        
    }
    
    public SettingsSignal(String nameSig)
    {
        this.nameSig = nameSig;
        ConfigSigStorageInterface dbSetting = new ConfigSigDB(nameSig);
        setConfigSigStorage(dbSetting);
        setListConfigs(dbSetting.getConfigsSignal());
    }
    
    public void setConfigSigStorage(ConfigSigStorageInterface containSetting){
        this.containSetting = containSetting;
    }
    public void setListConfigs(ArrayList<ConfigSig> listSettings){
        this.listSettings = listSettings; 
    }

    private int getNextFreeID(){
        int idElement = 0;
        for (int i = 0; i < listSettings.size(); i++) {
            int idCurrentElement = listSettings.get(i).getLocalId();
            if(idCurrentElement > idElement)
            {
                idElement = idCurrentElement;
            }
        }
        return idElement + 1;
    }
    
    @Override
    public String[][] getDataToTable() {
        /*
        берем данные с того что есть подставляем локальный id
        */
        String[][] dataToTable = new String[listSettings.size()][];
        for (int i = 0; i < listSettings.size(); i++) {
            //String[] data = listSettings.get(i).getData();
            String[] accuracySeting =  listSettings.get(i).getDataParent();
            String[] data = Stream.concat(Arrays.stream(listSettings.get(i).getData()), Arrays.stream(accuracySeting)).toArray(String[]::new);
            data[0] = Integer.toString(listSettings.get(i).getLocalId());
            dataToTable[i] =  data;
        }
        return dataToTable;
    }
    
    @Override
    public String getDataToHint() {
        // выборка данных для подсказки в формировании HMI
         
        /*
        5 - Type
        7 - Direction
        10 - Value 
        */
        String _dataToHint = "";
        for (int i = 0; i < listSettings.size(); i++) {
            String[] tmpDataSet = listSettings.get(i).getData();
            String directions = tmpDataSet[7];
            StringBuilder tmpStr = new StringBuilder();
            
            if(directions.equalsIgnoreCase("Верхняя")){
                tmpStr.append((char) (0x3E));
            }else{
                if(directions.equalsIgnoreCase("Нижняя")){
                    tmpStr.append((char) (0x3C));
                }
            }
            directions = tmpStr.toString();
            
            String[] data = new String[]{tmpDataSet[5], directions, tmpDataSet[10]};
            
            for (String s : data) {
                _dataToHint += s;
            }
            
            if (i < listSettings.size() - 1) {
            _dataToHint += ", ";
            }
        }
        return _dataToHint;
    }


    @Override
    public String[] getNameColumnsToTable() {
        return containSetting.getNameColumnSetings();
    }

    @Override
    public int addSetingSignal(Object[] row) {
        ConfigSig conf = new AnalogSetup();
        conf.setData((String[]) row);
        conf.setLocalId(getNextFreeID());
        conf.setStatus(StatusSeting.NEWSIGNAL);
        listSettings.add(conf);
        return conf.getLocalId();
    }
    
    @Override
    public ConfigSig getSetingByIdLocal(int idLocal) {
        ConfigSig finedConfig = null;
        for (ConfigSig c: listSettings) {
            if(idLocal == c.getLocalId())
            {
                finedConfig = c;
            }
        }
        return finedConfig;
    }

    @Override
    public void removeRowToData(Object row) {
        ConfigSig currentRow = (ConfigSig) row;
        switch(currentRow.getStatus()){
            case FROMBASE: 
            {
                currentRow.setStatus(StatusSeting.DELBASE);
                break;
            }
            default:
            {
                listSettings.remove(row);
                break;
            }
        }        
    }

    @Override
    public void SaveData() {
        for (ConfigSig c : listSettings) {
            if (c.getStatus() != null) {
                switch (c.getStatus()) {
                    case NEWSIGNAL:
                        containSetting.addSignal(c);
                        break;
                    case FROMBASE: {
                        containSetting.editSignal(c);
                        break;
                    }
                    case DELBASE: {
                        containSetting.removeByIDSignal(c);
                        break;
                    }
                }
            } else {
                System.out.println("Error sig " + c.getName() + " not status!!!!");
            }
        }
        
    }


    
    
}
