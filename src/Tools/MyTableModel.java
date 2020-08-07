package Tools;

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
    
}
