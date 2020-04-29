package Algorithm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@author lev*/
public class complexOperand  extends operand{
    formula f;

    public complexOperand(formula o, String op) {
        f = o; 
        opa = op;
        /*if(opa.equals("NOT")) {
            isNot = true;
        }*/
}
    @Override
    public String getSelf() {
        String inv = "";
        if(isNot) inv = "!";
        return inv + f.getSelf();// + "{" + opa + "}";
    }
    @Override
    public int CalcW() {
        return f.CalcW();
    }

    @Override
    public int CalcH() {
        return f.CalcH();
    }

    @Override
    public void drawSelf(int x, int y, int h, String id) {
        try {
            f.drawSelf(x, y, h, id);
        } catch (IOException ex) {
            Logger.getLogger(complexOperand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void optimization() {
        f.optization();
    }


    @Override
    public formula getFormula() {
        return f;
    }
}
