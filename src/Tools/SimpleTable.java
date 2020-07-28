package Tools;

import globalData.globVar;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class SimpleTable {
    public MyTableModel tableModel = new MyTableModel();
    JPopupMenu popupMenu = new JPopupMenu();
    public boolean isChang = false;
    String tableName;
    int tableSize;
    String[] cols;
    String comment;
    ArrayList<String[]> fromDB;
    ArrayList<String[]> listItemList = new ArrayList<>();
    public int[] colsWidth;
    int[] align;
    int qCol;
    ArrayList<JFrame> listJF = new ArrayList();
    String trgCol;
    
    public SimpleTable(String table,String col,String val, String trgCol) {
        if(!globVar.DB.isConnectOK())return;
        List<String> listColumn = globVar.DB.getListColumns(table);
        if(listColumn==null || listColumn.isEmpty())return;
        tableName = table;
        this.trgCol = trgCol;
        int tSize = listColumn.size();
        if(trgCol!=null && listColumn.contains(trgCol)){
            cols = new String[tSize-1];
            int cnt=0;
            for(int i=0; i<tSize; i++)
                if(!trgCol.equals(listColumn.get(i))) cols[cnt++]=listColumn.get(i);
        }else cols = listColumn.toArray( new String[tSize]);
        
        tableModel.setColumnIdentifiers(cols);
        fromDB = globVar.DB.getData(table,"id",col,val);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        comment = globVar.DB.getCommentTable(table);
        tableSize = fromDB.size();
        qCol = listColumn.size();
        align = new int[qCol];
        colsWidth = new int[qCol];
        
        TableTools.setWidthCols(cols, tableModel, colsWidth, 7.7);
        if(tableSize>0) TableTools.setAlignCols(fromDB.get(0), align);
    }
    
    public void setSimpleTableSettings(JTable jTable1){
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel, null, null, null);
        TableTools.setTableSetting(jTable1, colsWidth, align, 20);
        TableTools.setColsEditor(tableName, cols, fromDB, jTable1, listItemList);
    }
    
    public void resetTableContent(String col,String val){
        for(int i=tableModel.getRowCount()-1; i >= 0; i--)
            tableModel.removeRow(i);
        fromDB = globVar.DB.getData(tableName,"id",col,val);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        
        isNew(fromDB, tableModel);
    }
    
    private boolean  isNew(ArrayList<String[]> fromDB, DefaultTableModel tableModel) {
        if(fromDB==null || tableModel==null) return false;
        int fY = fromDB.size();
        if(fY<=0) return false;
        int x = tableModel.getColumnCount();
        int y = tableModel.getRowCount();
        for(int i=0;i<y;i++){
            boolean is = false;
            for(int k=0;k<fY;k++){
                boolean lokal;
                for(int j=0;j<x;j++)
                    if(!fromDB.get(k)[j].equals(tableModel.getValueAt(i, j))) is = true;
            }
        }
        return false;
    }

}
