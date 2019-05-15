/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author nazarov
 */
public class UUID {
 static  String getUIID(){
            java.util.UUID uniqueKey = java.util.UUID.randomUUID();
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается
            String uiid_str = uniqueKey.toString().replace("-", "");
            return uiid_str;}
    
}
