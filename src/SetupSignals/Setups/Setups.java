/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals.Setups;

import Tools.MyTableModel;
import globalData.globVar;

/**
 *
 * @author nazarov
 */
public class Setups {
    String nameSignal = null;
    String abonent = null;
    
    void getDataSetupToSignal(String nameSignal){
        this.nameSignal = nameSignal;
        int status = globVar.DB.statusConnectDB;
        
    }
    
    public MyTableModel  getTableModel(){
        
        return null;
    }
}
