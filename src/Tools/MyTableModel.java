package Tools;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class MyTableModel  extends DefaultTableModel{
    @Override
    public boolean isCellEditable(int row, int column) {
        if(column==0)return false;
        return super.isCellEditable(row, column);
    }
    
    @Override
    public String getValueAt(int row, int column) {
        return super.getValueAt(row, column) + "";//(String)
    }
    
    public void clear(){
        for(int i=this.getRowCount()-1; i >= 0; i--) 
            this.removeRow(i);
    }
    
    public ArrayList<String[]> toArrayList(){
        int x = this.getColumnCount();
        int y = this.getRowCount();
        ArrayList<String[]> al = new ArrayList<>();
        for(int i = 0; i < y; i++){
            String[] row = new String[x];
            for(int j=0; j<x; j++) row[j] = this.getValueAt(i, j);
            al.add(row);
        }
        return al;
    }
    
}
