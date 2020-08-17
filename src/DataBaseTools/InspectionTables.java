/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseTools;

import java.util.ArrayList;

/**
 *
 * @author nazarov
 */
// --- Анализ удаленных таблиц ---
public class InspectionTables {

    public static void main(String[] arg) {
        InspectionTables start = new InspectionTables();
        for (String s : start.findCommonPart("Satain 333 say no, Alisa in the Hell", "God, Alisa in the Wonderland 333  sdfds")) {
//        //for (String s : start.findCommonPart("14sdf345Aliba1a aasd6", "g1as aas4sdf3d6 wsAli2334Aliba1a 888888888Aliba888888148888888814888888888")) {
//            //for (String s : start.findCommonPart("1 14sd s", "a 14sd n")) {
//            System.out.println(s);
        }

        String s5 = "  a       ";
        String s6 = "a";
        s5 = s5.trim();
        System.out.println(s5);
        if (s5.trim().equals(s6.trim())) {
            System.out.println("Find");
        }

    }

    // найти максимальное совпадение строк
    public ArrayList<String> findCommonPart(String str1, String str2) {
        char[] array1 = str1.toCharArray();
        char[] array2 = str2.toCharArray();
        String findStr = "";
        String tmpStr = "";
        ArrayList<String> fitList = new ArrayList<>();

        int posOne = 0; // позиция от куда ищем первого
        int posTwo = 0; // позиция от куда ищем второй
        for (int n = array2.length; n > 0; --n) {
            posTwo = n;
            for (int i = posOne; i < array1.length; ++i) {
                char simbol1 = array1[i];

                for (int j = posTwo; j < array2.length; ++j) {
                    char simbol2 = array2[j];
                    if (simbol1 == simbol2) {
                        findStr += array2[j];
                        posTwo = j + 1; // Со следующего элемента ищем

                        // если следующий проход конец списка 
                        if (posTwo >= array2.length) {
                            if (findStr.length() > 1) {
                                fitList.add(findStr);
                            }
                            findStr = "";
                        }
                        break;
                    } else { // до первого несовпадающего
                        posTwo = n;
                        if (findStr.length() > 1) {
                            fitList.add(findStr);
                        }
                        findStr = "";
                    }

                }
            }
        }
        // удалить совпадений и пробелы с обоих концов
        for (int i = 0; i < fitList.size(); ++i) {
            String s1 = fitList.get(i);
            s1 = s1.trim();
            for (int j = i + 1; j <= fitList.size(); ++j) {
                //System.out.println(j);
                if (j >=fitList.size()) break;
                    String s2 = fitList.get(j);
                    s2 = s2.trim();
                    if (s1.equals(s2)) {
                        //System.out.println(s1 + " | " + s2);
                        fitList.remove(j);
                    }
                
            }
        }

        return fitList;
    }

}
