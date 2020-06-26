package Tools;

import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class MyTableModel  extends DefaultTableModel{
    @Override
    public boolean isCellEditable(int row, int column) {
        if(column==0)return false;
        return super.isCellEditable(row, column);
    }
    
}
