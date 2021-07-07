/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import DataBaseTools.DataBase;
import globalData.globVar;


/**
 *
 * @author nazarov
 */
public class ConfigSigDB implements ConfigSigDBInterface {
    DataBase db = globVar.DB;
    String nameTableSetups = "SigSetups";
    String nameColumn1 = "Abonent";
    String nameColumn2 = "NameSig";
    


    @Override
    public ConfigSig get(String nameSig) {
        //db.getListTable().equals(globVar.abonent + "_" + nameTableSetups);
        db.getDataCondition(nameTableSetups, new String[][]{{nameColumn1, nameColumn2}, {globVar.abonent, nameSig}}); // поиск данны=х с выборкой
        return null;
    }

    @Override
    public boolean remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean edit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
