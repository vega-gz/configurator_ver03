/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author lev
 */
public abstract class operand {
        public String opa ="";
        public boolean isNot = false;
        public boolean isNotOp(){ return opa.equals("NOT");};
        public abstract void optimization();
        public abstract String getSelf();
        public abstract formula getFormula();
        public abstract int CalcW();
        public abstract int CalcH();
        public abstract void drawSelf(int x, int y, int h, String id);
        //public operand getSelf
}
