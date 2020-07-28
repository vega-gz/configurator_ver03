package Tools;

import Algorithm.token;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrTools {//isFilter
    
    public static boolean isFilter(String s, String f){
        if(s==null) return false;
        if(f==null || f.trim().isEmpty()) return true;
        return Pattern.matches(f.toUpperCase()
                .replaceAll(" ", "")
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\?", ".")
                .replaceAll("\\*", "\\.\\*"),s.toUpperCase());
    }
    
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
    //Нарезает строку на токены по терминаторам из регулярки
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
    //Делает список из строки "s" с разделителями "t"
    public static ArrayList<String> getListFromString(String s, String t){
        if(s==null || t==null) return null;
        ArrayList<String> l = new ArrayList<>();
        int x = 0;
        int y = s.indexOf(t);
        while(y>=0){
            l.add(s.substring(x, y).trim());
            x = y+1;
            y = s.indexOf(t,x);
        }
        l.add(s.substring(x).trim());
        return l;
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
    public static int searchInList(String s, ArrayList<String> l){
        int i;
        for(i=0; i< l.size(); i++) if(s.equals(l.get(i))) return i;
        return -1;
    }
    
    public static String getAttributValue(String s, String a){
        if(s == null || a == null) return null;
        int start = s.indexOf(a);
        if(start < 0) return null;
        start= s.indexOf("\"", start)+1;//a.length()+1;
        int end = s.indexOf("\"", start); 
        if (end > start) return s.substring(start, end);        
        return null;
    }
}
