/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

import Tools.FileManager;
import Tools.LoggerFile;
import Tools.LoggerInterface;
import globalData.globVar;

/**
 *
 * @author nazarov
 * 
 * данные по модбассу из информации по комментарию таблицы
 * 
 */
public class ModBus {
    private LoggerInterface loggerFile = new LoggerFile();
    private String subAb = null;
    private String isMb = null;
    private String abonent = null;
    private String nodeTable = null;
    private String modbusFile = null;
    private String modbusAddr = null;
    private String group = null;
    private String commentTable = null;
    enum StatusModBus{ OK, NOTMODBUS, ERRORCOMMENTTABLE};
    StatusModBus statusElement = null;
    
    public ModBus(String nameTable){
        requestModbus(nameTable);
    }
    
    public StatusModBus isModbus(){
        return statusElement;
    }
    
    private void requestModbus(String nameTable){
       
        int x = nameTable.indexOf("_");
        abonent = nameTable.substring(0, x);
        String identModBus = "_mb_";
        int y = nameTable.indexOf(identModBus);

        if (y > 0) {
            subAb = "_" + nameTable.substring(0, y);
            //nodeTable = nameTable.substring(y + 1);
            nodeTable  = nameTable.substring(y + identModBus.length());
            isMb = "_mb";

            boolean validDataTable = validateDataInCommentTable(nameTable);
            if (validDataTable) {
                statusElement = StatusModBus.OK;
            } else {
                loggerFile.writeLog("Неправильный формат описания для драйвера в комментарии \"" + commentTable + "\" к таблице \"" + nameTable + "\"");
                statusElement = StatusModBus.ERRORCOMMENTTABLE;
            }
        } else {
            statusElement = StatusModBus.NOTMODBUS;
        }

    }
    
    // --- разборка записи в комментарии ---
    private boolean validateDataInCommentTable(String nameTable){
        boolean validDataComment = true;
        
        commentTable = globVar.DB.getCommentTable(nameTable);
        if (commentTable == null) {
            commentTable = "";
        }
        
        String identModbus = "Modbus:";
        int sizeStartData = commentTable.indexOf(identModbus);
        if (sizeStartData > 0) {
            sizeStartData += identModbus.length();
            String tmpStr = commentTable.substring(sizeStartData);
            String[] tmpStrArr = tmpStr.split(";");
            if (tmpStrArr.length < 4) {
                validDataComment = false;
            }
            for (int i = 0; i < tmpStrArr.length; i++) {
                tmpStrArr[i] = tmpStrArr[i].trim();
            }
            modbusFile = tmpStrArr[0];
            modbusAddr = tmpStrArr[1];
            group = tmpStrArr[2];
        } else {
            validDataComment = false;
        }

        return validDataComment;
    }
    
    public String getSubAb(){
        return subAb;
    }
    
    public String getIsMb(){
        return isMb;
    }
    
    public String getAbonent(){
        return abonent;
    }
    
    public String getNodeTable(){
        return nodeTable;
    }
    
    public String getModbusFile(){
        return modbusFile;
    }
    
    public String getModbusAddr(){
        return modbusAddr;
    }
    public String getGroup(){
        return group;
    }
 
}
