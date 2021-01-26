package Tools;

import java.nio.file.FileVisitResult;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.luaj.vm2.ast.Stat;

/*@author Lev*/
public class MyTableModel extends DefaultTableModel {
    String[] tmRow;
    ArrayList<String[]> newName;

    public MyTableModel() {
    }
    public MyTableModel(ArrayList<String[]> newName) {
        this.newName = newName;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        }
        return super.isCellEditable(row, column);
    }
    @Override
    public String getValueAt(int row, int column) {
        return super.getValueAt(row, column) + "";//(String)
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Vector rowData = (Vector) getDataVector().get(row);
        rowData.setElementAt(aValue, column);
        fireTableCellUpdated(row, column);
         tmRow = new String[this.getColumnCount()];
        for (int i = 0; i < this.getColumnCount(); i++) {
            tmRow[i] = this.getValueAt(row, i);

        }
        if (tmRow[3].equals("")) {
            tmRow[3] = tmRow[1];

        } else if (tmRow[2].equals("")) {
            tmRow[2] = tmRow[0];

        }
        newName.add(tmRow);
    }

    public void setValue(Object aValue, int row, int column) {
        Vector rowVector = (Vector) dataVector.elementAt(row);
        rowVector.setElementAt(aValue, column);
        fireTableCellUpdated(row, column);
    }

    public void clear() {
        for (int i = this.getRowCount() - 1; i >= 0; i--) {
            this.removeRow(i);
        }
    }

    public ArrayList<String[]> toArrayList() {
        int x = this.getColumnCount();
        int y = this.getRowCount();
        ArrayList<String[]> al = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            String[] row = new String[x];
            for (int j = 0; j < x; j++) {
                row[j] = this.getValueAt(i, j);
            }
            al.add(row);
        }
        return al;
    }
//    public ArrayList<String[]> getAddname(){
//        //newName.add(tmRow);
//        return newName;
//    }
//    public void setAddName(String [] tmRow){
//        newName.add(tmRow);
//    }
}
