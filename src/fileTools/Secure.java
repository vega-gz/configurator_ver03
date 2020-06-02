/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileTools;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
/**
 *
 * @author ad
 */
public class Secure {
public static void main(String[] args) {
    //BASE64Decoder decoder = new BASE64Decoder();
    //BASE64Encoder encoder = new BASE64Encoder();
    try {
    String text = "password";
    // Generate new key
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(256);
    Key key = keygen.generateKey();
    
    // Преобразовываем ключ в строку
    //String keyString = encoder.encode(key.getEncoded());
    //byte[] encodedKey = decoder.decodeBuffer(keyString);
    //Key key2 = new SecretKeySpec(encodedKey,0,encodedKey.length, "AES");  
    
// Encrypt with key
    String transformation = "AES/ECB/PKCS5Padding";
    Cipher cipher = Cipher.getInstance(transformation);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encrypted = cipher.doFinal(text.getBytes());  
    String cryptPass = DatatypeConverter.printHexBinary(encrypted);
    System.out.println(cryptPass);
    // Decrypt with key
    //cipher.init(Cipher.DECRYPT_MODE, key2); // расшифруем с помощью другого ключа
    String result = new String(cipher.doFinal(encrypted));
    System.out.println(result);
        
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
//    } catch (IOException ex) {
//        Logger.getLogger(Secure.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}    

