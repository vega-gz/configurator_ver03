/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import ReadWriteExcel.RWExcel;
import globalData.globVar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nazarov
 */
public class ConfigSigExcell implements ConfigSigStorageInterface{
    private String nameFileByData = null;
    private String[] legacyByTable = {"AI_ToHMI", "CalcPar", "_mb_"};
    private LinkedList<String> ListTableAbonent = null;
    private String[] nameColumnsInFile = {
        "Наименование уставки",
        "Имя тега",
        "Значение уставки",
        "Поведение при пропадании сигнала",
        "Верхняя уставка",
        "Задержка срабатывания",
        "Альтернативное имя",
        "Категория уставки",
        "Родительский сигнал",
        "Род сигнала"
    };
    
    public ConfigSigExcell(String nameFileByData){
        this.nameFileByData = nameFileByData;
        ListTableAbonent = globVar.DB.getListTableAbonent(globVar.abonent);
    }
    
    @Override
    public ArrayList<ConfigSig> getSignals() {
        
        ArrayList<ConfigSig> savedConfigsSignal = new ArrayList<>();
        
        
        RWExcel readWriterWxcell = new RWExcel();
        List<String> listSheet = readWriterWxcell.getListSheetName(nameFileByData);  // не нужно читать все же файлы по сто раз
        findingDataSeting:
        for (String s: listSheet) {
            HashMap<String, Integer> numberColumnEquels = new HashMap<>();
            LinkedList<LinkedList<String>> dataSheet = readWriterWxcell.getDataSheet(nameFileByData,s);
            int i = 0;
            for (i = 0; i < 3; i++) { // три строки проверим только для определение полей установ
                LinkedList<String> rowFromFile = dataSheet.get(i);
                for (int j = 0; j < nameColumnsInFile.length; j++) {
                    int index = rowFromFile.indexOf(nameColumnsInFile[j]); // нужный 
                    if (index >= 0) {
                        numberColumnEquels.put(nameColumnsInFile[j], index);
                    }
                }
                if(nameColumnsInFile.length == numberColumnEquels.size()){
                    // проход по самими данным
                    int localIDseting = 1;
                    for (i += 1; i < dataSheet.size(); i++) {
                        rowFromFile = dataSheet.get(i);
                        int numberColumnByNameSeting =  numberColumnEquels.get(nameColumnsInFile[6]);
                        int numberColumnBySignal =  numberColumnEquels.get(nameColumnsInFile[8]);
                        int numberColumnByLegasySignal =  numberColumnEquels.get(nameColumnsInFile[9]);
                        int numberColumnByType =  numberColumnEquels.get(nameColumnsInFile[7]);
                        int numberColumnDirection =  numberColumnEquels.get(nameColumnsInFile[4]);
                        int delayColumnDirection =  numberColumnEquels.get(nameColumnsInFile[5]);
                        int lostSignalColumnDirection =  numberColumnEquels.get(nameColumnsInFile[3]);
                        int valueColumnDirection =  numberColumnEquels.get(nameColumnsInFile[2]);
                        String nameSeting = rowFromFile.get(numberColumnByNameSeting);
                        String nameSigByFile = rowFromFile.get(numberColumnBySignal);
                        String nameLegacyByFile = rowFromFile.get(numberColumnByLegasySignal);  
                        String typeByFile = rowFromFile.get(numberColumnByType);  
                        String directionByFile = rowFromFile.get(numberColumnDirection);  
                        String delayByFile = rowFromFile.get(delayColumnDirection);  
                        String lostSignalByFile = rowFromFile.get(lostSignalColumnDirection);
                        String valueByFile = rowFromFile.get(valueColumnDirection);
                       
                        String partNameTable = getCommonPartNameTable(nameLegacyByFile);
                        LinkedList<String>  listTablewithsig = checkDataFileExeptionSetingsSig(nameSigByFile, partNameTable);
                        for (String nameTable: listTablewithsig) {                            
                            
                            String[] arr = {null, globVar.abonent, nameTable, nameSeting, typeByFile, nameSigByFile, directionByFile, delayByFile, lostSignalByFile, valueByFile};
                            ConfigSig config = new AnalogSetup();
                            config.setData(arr);
                            config.setLocalId(localIDseting); // с единицы
                            config.setStatus(ConfigSig.StatusSeting.NEWSIGNAL); // установить статус
                            savedConfigsSignal.add(config);
                            ++localIDseting;
                        }
                       
                    }
                }else{
                    numberColumnEquels.clear();
                }
            }
            
            
        }
        return savedConfigsSignal;
    }

    @Override
    public void addSignal(ConfigSig c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeByIDSignal(ConfigSig c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editSignal(ConfigSig c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getNameColumnSetings() {
        return nameColumnsInFile;
    }
    
    
    private LinkedList<String> checkDataFileExeptionSetingsSig(String nameSig, String elementEqualNameTable) {
        /*
        получение таблиц абонента
        в которых есть сигнал
        и содержиться часть названия
        */
        LinkedList<String>  tablesWithSignal = new LinkedList<>();     
        for (String nameTable: ListTableAbonent) {
            if (nameTable.indexOf(elementEqualNameTable) > -1){
                String[][] condition =  {{globVar.namecolumnT[1], nameSig}};
                ArrayList<String[]>  dataSignal = globVar.DB.getDataCondition(nameTable, condition);
                if(dataSignal.size() > 0){
                    tablesWithSignal.add(nameTable);
                }
            }
        }
        return tablesWithSignal;
    }
  
    
    private  String getCommonPartNameTable(String identificator){
        /*
        Род сигнала:
        И	AI_ToHMI
        Р	CalcPar
        С	_mb_
        */
        String partTable = null;
        switch(identificator){
            case "И":
                partTable = "AI_ToHMI";
                break;
            case "Р":
                partTable = "CalcPar";
                break;
            case "С":
                partTable = "_mb_";
                break;
        }
        return partTable;
    }
}
