package Tools;

import globalData.globVar;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/*@author Lev*/
public class Tools {
    public static boolean isDesDir(){
        File f = new File(globVar.desDir + File.separator +"Design");
        
        boolean x = false;
        if(f!=null) x = f.isDirectory();
        if(!x) JOptionPane.showMessageDialog(null, "В каталоге \"" + globVar.desDir + "\" нет каталога \"Design\"");
        return x;
    }
    
    public static boolean isDB(){
        if (globVar.DB == null) {
            JOptionPane.showMessageDialog(null, "База " + globVar.currentBase + "по пути " + globVar.dbURL + " не найдена");
            return false;
        }
        return true;
    }
    
    public static int indexOfArray(Object[] a, Object o){
        for(int i=0; i< a.length; i++)
            if(o.equals(a[i])) return i;
        return -1;
    }
    
    public static void setPlusList(ArrayList<String[]> list1, ArrayList<String> list2){
        for(int i=0; i < list1.size(); i++){
            String s = list1.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && !list2.contains(s.substring(2,x))) 
                list2.add(s.substring(2,x));
        }
    }
    
    public static boolean isPlusInList2(ArrayList<String[]> list1, String sig){
        for(int i=0; i < list1.size(); i++){
            String s = list1.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) return true;
        }
        return false;
    }

    public static boolean isInteger(String s) {
        try { Integer.parseInt(s); } 
        catch(NumberFormatException e) { return false; } 
        catch(NullPointerException e) { return false; }
        return true;
    }    
    public static boolean isNumeric(String s) {
        try { Double.parseDouble(s); } 
        catch(NumberFormatException e) { return false; } 
        catch(NullPointerException e) { return false; }
        return true;
    }    
    public static void delPlusFromArch(ArrayList<String[]> list1, DefaultListModel list2, String sig){
        for(int i=0; i < list1.size(); i++){
            String s = list1.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) list1.remove(i--);
        }
        for(int i=0; i < list2.size(); i++){
            String s = list2.get(i).toString();
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) list2.remove(i--);
        }
    }

    public static int getIndexOfComboBox(JComboBox<String> jComboBox, String s) {
        int cnt = jComboBox.getItemCount();
        for(int i=0; i < cnt; i++) if(s.equals(jComboBox.getItemAt(i))) return i;
        return -1;
    }
    public static void fillCellCol(JTable jTable1, ArrayList<String> cellNames, int rows[], int col) {
        int cnt = 0;
        for (int k = 0; k < rows.length; k++) {
            jTable1.setValueAt(cellNames.get(cnt), rows[k], col);
            cnt++;
            if (cnt >= cellNames.size()) {
                cnt = 0;
            }
        }
    }
    
    public static void fillCellRect(JTable jTable1, ArrayList<String[]> cellNames, int rows[], int cols[]) {
        for (int i = 0; i < cols.length; i++) {
            int cnt = 0;
            for (int k = 0; k < rows.length; k++) {
                jTable1.setValueAt(cellNames.get(cnt)[i], rows[k], cols[i]);
                cnt++;
                if (cnt >= cellNames.size()) {
                    cnt = 0;
                }
            }
        }
    }
    
}
