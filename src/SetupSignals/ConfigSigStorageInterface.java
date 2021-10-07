/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import java.util.ArrayList;

/**
 *
 * @author nazarov
 */
public interface ConfigSigStorageInterface {
    ArrayList<ConfigSig> getSignals();
    void addSignal(ConfigSig c); 
    void removeByIDSignal(ConfigSig c); 
    void editSignal(ConfigSig c);
    String[] getNameColumnSetings();
}
