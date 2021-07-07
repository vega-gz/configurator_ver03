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
public interface ConfigSigDBInterface {
    ConfigSig get(String nameSig);
    boolean remove();
    boolean edit();
    
}
