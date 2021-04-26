/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import Tools.MyTableModel;
import globalData.globVar;

/**
 *
 * @author nazarov
 * 
 *  будет все что касается Уставок
 * 
 */
public class Setups {
    String nameSignal = null;
    String abonent = null;
    
    void getDataSetupToSignal(String nameSignal){
        String nameTableSetups = "SigSetups";
        this.nameSignal = nameSignal;
        int status = globVar.DB.statusConnectDB;
        
        globVar.DB.getListTable().equals(globVar.abonent + "_" + nameTableSetups);
        
    }
    
    public MyTableModel  getTableModel(){
        
        return null;
    }
}
