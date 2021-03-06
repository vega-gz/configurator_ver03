/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import SetupSignals.ConfigSig.StatusSeting;
import java.util.ArrayList;

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
    
    public SettingsSignal(String nameSig)
    {
        this.nameSig = nameSig;
        containSetting = new ConfigSigDB(nameSig);
        listSettings = containSetting.get(); 
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
        берем данные с того что есть но подставляем локальный id
        */
        String[][] dataToTable = new String[listSettings.size()][];
        for (int i = 0; i < listSettings.size(); i++) {
            String[] data = listSettings.get(i).getData();
            data[0] = Integer.toString(listSettings.get(i).getLocalId());
            dataToTable[i] =  data;
        }
        return dataToTable;
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
            case NEWSIGNAL: listSettings.remove(row);
                break;
            case FROMBASE: 
            {
                currentRow.setStatus(StatusSeting.DELBASE);
                break;
            }
        }        
    }

    @Override
    public void SaveData() {
        for (ConfigSig c : listSettings) {
            switch (c.getStatus()) {
                case NEWSIGNAL:
                    containSetting.addSignal(c);
                    break;
                case FROMBASE: {
                    containSetting.editSignal(c);
                    break;
                }
            }            
        }
        
    }

    
    
}
