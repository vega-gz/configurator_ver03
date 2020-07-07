package TableTools;

import globalData.globVar;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

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
    public int[] colsWidth;
    int[] align;
    int qCol;
    ArrayList<JFrame> listJF = new ArrayList();
    
    public SimpleTable(String table,String col,String val) {
        tableName = table;
        if(globVar.DB==null)return;
        List<String> listColumn = globVar.DB.getListColumns(table);
        if(listColumn==null || listColumn.isEmpty())return;
        cols = listColumn.toArray( new String[listColumn.size()]);
        tableModel.setColumnIdentifiers(cols);
        //(String table, String orderCol, String desiredCol, String disiredVal)
        fromDB = globVar.DB.getData(table,"id",col,val);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        comment = globVar.DB.getCommentTable(table);
        //this.setTitle(table + ": "+comment);
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
        TableTools.setColsEditor(tableName, cols, fromDB, jTable1);
    }
    
    public void resetTableContent(String col,String val){
        for(int i=tableModel.getRowCount()-1; i >= 0; i--)
            tableModel.removeRow(i);
        fromDB = globVar.DB.getData(tableName,"id",col,val);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
    }
}
