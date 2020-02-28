/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cherepanov
 */
public class UUID {
   // public static String getUUID;

     String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается
        String uiid_str = uniqueKey.toString().replace("-", "");
        return uiid_str;
    }
      static String getUUID() {
        java.util.UUID unUuid = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssss");
        String uuid_str = unUuid.toString().replace("-", "");
        return uuid_str;
    }

    static String getUID() {
        java.util.UUID uidKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssss");
        String uuid_strr = uidKey.toString().replace("-", "");
        return uuid_strr;

    }
}
