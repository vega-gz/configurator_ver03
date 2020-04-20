package Algorithm.DrumTools;

import Algorithm.formula;
import Algorithm.token;
import StringTools.StrTools;
//import static StringTools.StrTools.strToTokArr;
import globalData.globVar;
import java.io.IOException;
import java.util.ArrayList;

public final class drum {
    String name;
    String rusName;

    int qStep;
    int qTimer = 1;
    
    ArrayList<drumStep> drumSteps = new ArrayList<>();
    
    int readDrumStep(int step, ArrayList<token> dt){    
        return readDrumStep(step, dt, 0);
    }
    int readDrumStep(int step, ArrayList<token> dt, int start){    
        int x = -1;
        int y = -1;
        String stepFormula = "";
        //--- Ищем начало шага барабана
        ArrayList<token> srch = new ArrayList<>();
        srch.add(new token(0, "Drum."+name+".state"));
        srch.add(new token(0, "="));
        srch.add(new token(0, ""+step));
        srch.add(new token(0, "then"));
        x = StrTools.searchSequence(dt, srch);
        
        if(x<0) return -1; // если не нашли - возвращаем ошибку
        x+=4;
        //--- Ищем первый блок if - then --------------------------------
        srch.clear();
        srch.add(new token(0, "if"));
        y = StrTools.searchSequence(dt, srch, x);
        //---------------------------
        srch.clear();
        srch.add(new token(0, "then"));
        x = StrTools.searchSequence(dt, srch, y+1);
        //----------- Проверяем, а не таймер ли там есть -----------------
        srch.clear();
        srch.add(new token(0, "Drum."+name+".timer[" + qTimer + "]"));
        srch.add(new token(0, ">="));
        int tPos = StrTools.searchSequence(dt, srch, y+1, x);
        
        //String tmp = "";//--------------------------------------------------- контрольный вывод ----------
        //for(int i=y+1; i < x; i++) tmp += i + ")" + dt.get(i).tok + " ";//--- контрольный вывод ----------
        //System.out.println("x="+x+", y="+y+ ", "+ tmp); //------------------------------------------ контрольный вывод ----------
        
        if(tPos >= 0){//----если таймер ---------
            qTimer ++;//---проверяем, один ли он там. Если да, то добавляем в список шагов шаг с чистым таймером
            if(x-y == 4){ 
                drumSteps.add(new drumStep(qTimer));
            }else if(tPos == y+1){
                for(int i=y+4; i < x; i++) stepFormula += dt.get(i).tok + " ";
                drumSteps.add(new drumStep(new formula(stepFormula),qTimer));
            }else{
                for(int i=y+1; i < x-4; i++) stepFormula += dt.get(i).tok + " ";
                drumSteps.add(new drumStep(new formula(stepFormula),qTimer));
            }
        }else{
            for(int i=y+1; i < x; i++) stepFormula += dt.get(i).tok + " ";
            drumSteps.add(new drumStep(new formula(stepFormula)));
        }

        //System.out.println("["+ stepFormula +"]"); //------------ контрольный вывод
        return x;
    }
    
    String readDrumText() throws IOException{
        if(globVar.EOF) return "";
        String buf = "";
        String drumStr = "";
        int x = -1;
        while(!globVar.EOF && x < 0){
            buf = globVar.fm.rd().trim();
            x = buf.indexOf("end_drum");
            int y = buf.indexOf("//");
            drumStr += (y < 0)? buf + " " : buf.substring(0, y) + " ";
        }
        if(x<0) return "";
       //System.out.println(drumStr +"\n"); //------------ контрольный вывод
        return drumStr;
    }
    
    public drum() throws IOException{
        String buf = "";
        int x = -1;
        while(!globVar.EOF && x < 0){
            buf = globVar.fm.rd();
            x = buf.indexOf("Drum.");
        }
        if(x<0) return;
        int y = buf.indexOf(".enable");
        if(y<0) return;
        
        name = buf.substring(x+5, y);
        
        x = buf.indexOf("//");
        rusName = buf.substring(x+2).trim();
        //System.out.println("name: " + name +", rusName: " + rusName); //------------ контрольный вывод
        String ds = readDrumText();
        if("".equals(ds))return;
        
        ArrayList<token> dt = StrTools.strToTokArr(ds);
        if(dt.isEmpty())return;
        x = readDrumStep(1, dt);
        y = readDrumStep(2, dt, x);
    }
}

        //srch.add(new token(0, "Drum."+name+".timer[" + qTimer + "]"));
        //srch.add(new token(0, "+"));
        //srch.add(new token(0, "cycle"));
