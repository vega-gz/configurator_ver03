/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

/**
 *
 * @author nazarov
 */
public interface SetupsDataToTables {
    
   String[][] getDataToTable();
   String[] getNameColumnsToTable();
   int addSetingSignal(Object[] row);
   ConfigSig getSetingByIdLocal(int i);
   void removeRowToData(Object row);
   void SaveData();
}
