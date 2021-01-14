package Tools;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class MyTableModel extends DefaultTableModel {

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

        Vector rowData = (Vector) getDataVector().get(row); // Получаем список значений аналог Листа
        rowData.set(column, aValue);
        String[] tmRow = new String[this.getColumnCount()];

        for (int i = 0; i < this.getColumnCount(); i++) {
            tmRow[i] = this.getValueAt(row, i);
            System.out.println(tmRow[i]);
        }
         if(!tmRow[3].equals("")){
                newName.add(tmRow);
            }
       // newName.add(tmRow);
        System.out.println(aValue.toString());
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

}
