/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import globalData.globVar;

/**
 *
 * @author lev
 */
public class svgTools {
    public static String getSvgTitleA4(){
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"210mm\" height=\"297mm\">\n"+
                "<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">" + 
                    "<rect x=\"20mm\" y=\"5mm\" width=\"185mm\" height=\"287mm\" />"+
                    "<rect x=\"8mm\" y=\"147mm\" width=\"12mm\" height=\"145mm\"/>"+
                    "<line x1=\"13mm\" x2=\"13mm\" y1=\"147mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"8mm\" x2=\"20mm\" y1=\"182mm\" y2=\"182mm\"/>" + 
                    "<line x1=\"8mm\" x2=\"20mm\" y1=\"207mm\" y2=\"207mm\"/>" + 
                    "<line x1=\"8mm\" x2=\"20mm\" y1=\"232mm\" y2=\"232mm\"/>" + 
                    "<line x1=\"8mm\" x2=\"20mm\" y1=\"267mm\" y2=\"267mm\"/>" + 
                    "<line x1=\"20mm\" x2=\"205mm\" y1=\"277mm\" y2=\"277mm\"/>" + 
                    "<line x1=\"20mm\" x2=\"85mm\" y1=\"282mm\" y2=\"282mm\"/>" + 
                    "<line x1=\"20mm\" x2=\"85mm\" y1=\"287mm\" y2=\"287mm\"/>" + 
                    "<line x1=\"195mm\" x2=\"205mm\" y1=\"282mm\" y2=\"282mm\"/>" + 
                    "<line x1=\"195mm\" x2=\"205mm\" y1=\"287mm\" y2=\"287mm\"/>" + 
                    "<line x1=\"27mm\" x2=\"27mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"37mm\" x2=\"37mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"60mm\" x2=\"60mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"75mm\" x2=\"75mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"85mm\" x2=\"85mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                    "<line x1=\"195mm\" x2=\"195mm\" y1=\"277mm\" y2=\"292mm\"/>" + 
                "</g>\n";
    }
    public static String getSvgEnd(){
        return "</svg>";
    }
    public static String getSvgRect(int x, int y, int w, int h, String id){
        return "<rect x=\"" + x*globVar.skale/100 + 
                  "\" y=\"" + y*globVar.skale/100 + 
              "\" width=\"" + w*globVar.skale/100 +
             "\" height=\"" + h*globVar.skale/100 +
             "\" fill=\"none\" stroke=\"black\" id=\"rect_" + id + "\"/>\n";
    }
    public static String getSvgText(int x, int y, String id, String t){
        return "<text x=\"" + x*globVar.skale/100 + 
                  "\" y=\"" + y*globVar.skale/100 + 
            "\" id=\"rect_" + id + "\"\n" +
                "style=\"font-style:normal;font-weight:normal;font-size:12px;line-height:1.25;font-family:'DejaVu Sans Mono';" + 
                "letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none\">\n" + t + "</text>\n";
    }
    
    public static String drawSvgTextRect(int x, int y, int w, int h, String id, String t){
        return  "<g id=\"" + id + "\" >\n" + 
                "   <rect x=\"" + x*globVar.skale/100 + 
                  "\" y=\"" + y*globVar.skale/100 + 
              "\" width=\"" + w*globVar.skale/100 +
             "\" height=\"" + h*globVar.skale/100 +
             "\" fill=\"none\" stroke=\"black\"/>\n" + 
        
               "    <text x=\"" + (x + globVar.simbolWidth/100)*globVar.skale/100 + 
                  "\" y=\"" + (y + globVar.simbolHeight/100*4/5)*globVar.skale/100 + 
                "\"\n       style=\"font-style:normal;font-weight:normal;font-size:14px;line-height:1.25;font-family:'DejaVu Sans Mono';" + 
                "letter-spacing:0px;word-spacing:0px;fill:#000000;fill-opacity:1;stroke:none\">\n   " + t + "</text>\n</g>\n";
    }
    public static String drawSvgPath(int x0, int y0, int x2, int y2, int offset, String id1, String id2, boolean inv){
        if(offset > 90 || offset < 10) offset = 50;
        int x1 = x0 + (x2 - x0)*offset/100;
        int x3 = x2;
        String isInv = "";
        if(inv){
            x3 = x2-1;
            int r = 1*globVar.skale/100;
            isInv = " a " + r + " "+ r + " 0 1 1 " + 2*r + " 0 a " + r + " "+ r + " 0 1 1 -" + 2*r + " 0";
        }
        return "<path fill=\"none\" stroke=\"black\" id=\"" + id1+ "_" + id2 +
               "\" d=\"M "+ x0*globVar.skale/100 +","    + y0*globVar.skale/100 + " H " + x1*globVar.skale/100 + " V " + 
                         y2*globVar.skale/100 + " H " + x3*globVar.skale/100  + isInv + "\"\n" +
               "    inkscape:connection-start=\"#" + id1 +"\"\n" +
               "    inkscape:connection-end=\"#" + id2 +"\" />\n";// +
                /*
                "<circle  fill=\"#FFFF00\" stroke=\"black "+
                         "\" cx=\"" + x2*globVar.skale/100  + 
                         "\" cy=\"" + y2*globVar.skale/100 + 
                         "\"  r=\"" + 1*globVar.skale/100 + "\"/>\n";*/
    }    
}
