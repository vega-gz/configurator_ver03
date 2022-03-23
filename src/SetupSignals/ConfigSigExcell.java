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
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author nazarov
 */
public class ConfigSigExcell implements ConfigSigStorageInterface{
    private String nameFileByData = null;
    private LinkedList<String> ListTableAbonent = null;
    private String[] nameColumnsInFile = {
        "Наименование уставки",
        "Имя тега",
        "Значение уставки",
        "Поведение при пропадании сигнала",
        "Верхняя уставка",
        "Задержка срабатывания",
        "Дополнительная информация",
        "Категория уставки",
        "Родительский сигнал",
        "Род сигнала",
        "Внешняя инициализация"   
    };
    
    public ConfigSigExcell(String nameFileByData){
        this.nameFileByData = nameFileByData;
        ListTableAbonent = globVar.DB.getListTableAbonent(globVar.abonent);
    }
    
    @Override
    public ArrayList<ConfigSig> getConfigsSignal() {
        
        ArrayList<ConfigSig> savedConfigsSignal = new ArrayList<>();
        
        
        RWExcel readWriterWxcell = new RWExcel();
        List<String> listSheet = readWriterWxcell.getListSheetName(nameFileByData);  // не нужно читать все же файлы по сто раз
        findingDataSeting:
        for (String s: listSheet) {
            HashMap<String, Integer> numberColumnEquels = new HashMap<>();
            LinkedList<LinkedList<String>> dataSheet = readWriterWxcell.getDataSheet(nameFileByData,s);
            int iStartData = 0;
            LinkedList<String> rowFromFile = null;
            for (int i = 0; i < 3; i++) { // три строки проверим только для определение полей установ
                rowFromFile = dataSheet.get(i);
                for (int j = 0; j < nameColumnsInFile.length; j++) {
                    String currentnameColumn = nameColumnsInFile[j];
                    int index = rowFromFile.indexOf(currentnameColumn); // нужный 
                    if (index >= 0) {
                        iStartData = i;
                        numberColumnEquels.put(nameColumnsInFile[j], index);
                    }
                }
            }
            
            
            if (nameColumnsInFile.length == numberColumnEquels.size()) { // все нужные поля нашли для поиска данных
                
                int numberColumnBynameSetingRus = numberColumnEquels.get(nameColumnsInFile[0]);
                int numberColumnByNameSeting = numberColumnEquels.get(nameColumnsInFile[1]);
                int numberColumnBySignal = numberColumnEquels.get(nameColumnsInFile[8]);
                int numberColumnByLegasySignal = numberColumnEquels.get(nameColumnsInFile[9]);
                int numberColumnByType = numberColumnEquels.get(nameColumnsInFile[7]);
                int numberColumnDirection = numberColumnEquels.get(nameColumnsInFile[4]);
                int delayColumnDirection = numberColumnEquels.get(nameColumnsInFile[5]);
                int addetionInfo = numberColumnEquels.get(nameColumnsInFile[6]);
                int lostSignalColumnDirection = numberColumnEquels.get(nameColumnsInFile[3]);
                int valueColumnDirection = numberColumnEquels.get(nameColumnsInFile[2]);
                int valueExternalInitial = numberColumnEquels.get(nameColumnsInFile[10]);
                // проход по самими данным
                int localIDseting = 1;
                for (int i = iStartData + 1; i < dataSheet.size(); i++) {
                    rowFromFile = dataSheet.get(i); // могут быть пустые значения сделай проверку по циклам

                    String nameSetingRus = rowFromFile.get(numberColumnBynameSetingRus);
                    String nameSeting = rowFromFile.get(numberColumnByNameSeting);
                    String nameSigByFile = rowFromFile.get(numberColumnBySignal);
                    String nameLegacyByFile = rowFromFile.get(numberColumnByLegasySignal);
                    String typeByFile = rowFromFile.get(numberColumnByType);
                    String directionByFile = rowFromFile.get(numberColumnDirection);
                    String delayByFile = rowFromFile.get(delayColumnDirection);
                    String valueaddetionInfo = rowFromFile.get(addetionInfo);
                    String lostSignalByFile = rowFromFile.get(lostSignalColumnDirection);
                    String valueByFile = rowFromFile.get(valueColumnDirection);
                    String partNameTable = getCommonPartNameTable(nameLegacyByFile);
                    String ExternalInitial = "";
                    if(rowFromFile.size() > valueExternalInitial){
                        ExternalInitial = rowFromFile.get(valueExternalInitial);
                    }
                    
                    LinkedList<String> listTablewithsig = checkDataFileExeptionSetingsSig(nameSigByFile, partNameTable);
                    
                    for (String nameTable : listTablewithsig) {
                        String[] arr = {null, globVar.abonent, nameTable, nameSetingRus, nameSeting, typeByFile, nameSigByFile, directionByFile, delayByFile, lostSignalByFile, valueByFile, ExternalInitial, valueaddetionInfo};
                        ConfigSig config = new AnalogSetup();
                        config.setData(arr);
                        config.setLocalId(localIDseting); // с единицы
                        config.setStatus(ConfigSig.StatusSeting.FROMFILE); // установить статус
                        savedConfigsSignal.add(config);
                        ++localIDseting;
                    }

                }
            } else {
                numberColumnEquels.clear();
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
        И	_AI
        Р	CalcPar
        С	_mb_
        */
        String partTable = null;
        switch(identificator){
            case "И":
                //partTable = "AI_ToHMI";
                partTable = globVar.abonent + "_AI";
                break;
            case "Р":
                partTable = "_CalcPar";
                break;
            case "С":
                partTable = "_mb_";
                break;
        }
        return partTable;
    }
}
