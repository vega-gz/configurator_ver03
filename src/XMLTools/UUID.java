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

    public static String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        String uiid_str = uniqueKey.toString().replace("-", "");
        return uiid_str.toUpperCase();
    }
    
    public static String getUIID26() {
        byte[] b = new byte[]{'x','y','z'};
        java.util.UUID uniqueKey = java.util.UUID.nameUUIDFromBytes(b);
        String uiid_str = uniqueKey.toString();
        return uiid_str.toUpperCase();
    }
    
    public static void main(String[] args) {
        UUID _UUID = new UUID();
        while(true){
            System.out.println(_UUID.getUIID26());
        }
    }
}
