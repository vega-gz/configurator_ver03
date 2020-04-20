package StringTools;

import Algorithm.token;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrTools {
    //static final String operatorChar = "-+*/=<>:";
    /*
    static final String spaceChar = " \n\r\t";
    static final String allowVarChar = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890_.";
    static final String allowFirstChar = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm_";

    public boolean IsVarName(String s) {
            if(s.isEmpty()) return false;
            char[] chArr = s.toCharArray();
            if(allowFirstChar.indexOf(chArr[0]) < 0) return false;
            for(int i = 1; i < chArr.length; i++) if(allowVarChar.indexOf(chArr[i]) < 0) return false;
            return true;
    }*/
    public static ArrayList<token> strToTokArr(String tail){
        tail = tail.trim();
        tail = tail.replaceAll("\\[\\s+", "[");
        tail = tail.replaceAll("\\s+\\]", "]");

        ArrayList<token> toks = new ArrayList<>();

        strSplit(tail, "[\\w\\.\\[\\],]+", toks);
        strSplit(tail, "[\\+\\*\\^:/<>=%,]+", toks);
        strSplit(tail, "[\\(\\)]", toks);

        Collections.sort(toks);

        return toks;
    }
    public static void strSplit(String inStr, String pattern, ArrayList<token> toks){
        
        Pattern p1 = Pattern.compile(pattern);
        //System.out.println(inStr);//------------------------ контрольный вывод
        Matcher match = p1.matcher(inStr);
        while (match.find()){
            int s = match.start();
            int e = match.end();
            toks.add(new token(s, inStr.substring(s, e).trim()));
            //System.out.println(toks.get(toks.size()-1).start + ") " + toks.get(toks.size()-1).tok);//----------------- контрольный вывод
        }
    }
    public static int searchSequence(ArrayList<token> toks, ArrayList<token> srch){
        return searchSequence(toks, srch, 0, -1);
    }
    public static int searchSequence(ArrayList<token> toks, ArrayList<token> srch, int start){
        return searchSequence(toks, srch, start, -1);
    }
    public static int searchSequence(ArrayList<token> toks, ArrayList<token> srch, int start, int end){
        int x = 0;
        int qt = toks.size();
        if(end > 0 && end < qt) qt = end;
        int qs = srch.size();
        if(qs > qt) return -1;
        int i;
        for(i = start; i < qt-qs; i++){
            int j;
            for(j = 0; j < qs; j++) if(!toks.get(i+j).tok.equalsIgnoreCase(srch.get(j).tok)) break;
            if(j==qs) break;
        }
        if(i < qt-qs) return i;
        return -1;
    }
}