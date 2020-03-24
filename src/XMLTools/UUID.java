/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author cherepanov
 */
public class UUID {

    // public static String getUUID;
    String upperUUID;

    public String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается
        String uiid_str = uniqueKey.toString().replace("-", "");
        upperUUID = uiid_str.toUpperCase(Locale.ENGLISH);
        return uiid_str;
    }

    public static String getUUID() {
        java.util.UUID unUuid = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssss");
        String uuid_str = unUuid.toString().replace("-", "");
        return uuid_str;
    }

    public static String getUID() {
        java.util.UUID uidKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssss");
        String uuid_strr = uidKey.toString().replace("-", "");
        return uuid_strr;

    }
}
