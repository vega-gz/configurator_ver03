/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLTools;

/**
 *
 * @author nazarov
 */
public class UUID {

    public  String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        String uiid_str = uniqueKey.toString().replace("-", "");
        return uiid_str;
    }
}
