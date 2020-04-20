package Algorithm.DrumTools;

import Algorithm.formula;
//import java.util.ArrayList;

/*@author lev*/
public class drumStep {
    formula f = null;
    //int targetStep;
    int timer = -1;
    //ArrayList<String> outs = new ArrayList<>();
    
    public drumStep(formula F, int t){//, ArrayList<String> o){
        f = F;
        timer = t;
        //outs = o;
    }
    public drumStep(int t){//, ArrayList<String> o){
        timer = t;
    }
    public drumStep(formula F){//, ArrayList<String> o){
        f = F;
    }
}
