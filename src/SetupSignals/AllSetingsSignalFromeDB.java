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
public class AllSetingsSignalFromeDB extends SettingsSignal{

    public AllSetingsSignalFromeDB()
    {
        ConfigSigStorageInterface dbSetting = new ConfigSigDB();
        setConfigSigStorage(dbSetting);
        setListConfigs(dbSetting.getConfigsSignal());
    }
    
}
