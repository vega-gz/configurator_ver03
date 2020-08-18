package Tools;

import globalData.globVar;
import java.util.ArrayList;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/*@author Lev*/
public final class SimpleTable {
    public MyTableModel tableModel = new MyTableModel();
    public JPopupMenu popupMenu = new JPopupMenu();
    public boolean isChang = false;
    public String tableName;
    public int tableSize = -1;
    public String[] cols;
    public String comment;
    public ArrayList<String[]> fromDB;
    public ArrayList<String[]> listItemList = new ArrayList<>();
    public ArrayList<ArrayList> listToTable = new ArrayList<>(); // Лист для передачи в таблицу
    public int[] colsWidth;
    int[] align;
    int qCol;
    int trgColNum = -1;
    //ArrayList<JFrame> listJF = new ArrayList();
    String trgCol;
    String oldVal=null;
    ArrayList<String> listColumn;
    
    public SimpleTable(String table, String trgCol, String val) {
        if(!globVar.DB.isConnectOK())return;
        listColumn = globVar.DB.getListColumns(table);
        if(listColumn==null || listColumn.isEmpty())return;
        tableName = table;
        this.trgCol = trgCol;
        //Если усть целевой столбец - исключаем его из списка заголовков
        if(trgCol!=null){
            trgColNum =  listColumn.indexOf(trgCol); //запоминаем номер целевого столбца
            cols = new String[listColumn.size()-1];
            int i = 0;
            for(String s: listColumn) if(!s.equals(trgCol)) cols[i++] = s;
        } else cols = listColumn.toArray(new String[listColumn.size()]);
        //---------------------------------------------------------------
        tableModel.setColumnIdentifiers(cols);
        fromDB = globVar.DB.getData(table);//, listColumn, "id");//, col, val);
        comment = globVar.DB.getCommentTable(table);
        //fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        tableSize = fromDB.size();
        reSetTableContent(val);
        qCol = cols.length;
        align = new int[qCol];
        colsWidth = new int[qCol];

        TableTools.setWidthCols(cols, tableModel, colsWidth, 7.7);
        if (tableSize > 0) {
            TableTools.setAlignCols(fromDB.get(0), align);
        }
    }

    public void setSimpleTableSettings(JTable jTable1) {
        if(tableSize<0) return;
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel, null, null, null);
        TableTools.setTableSetting(jTable1, colsWidth, align, 20);
        TableTools.setColsEditor(tableName, cols, fromDB, jTable1, listItemList);
    }
    
    public void reSetTableContent(String val){
        if(tableSize<0) return;
        if(oldVal!=null && trgColNum>=0){
            for(int i = fromDB.size()-1; i >=0; i--)
                 if(fromDB.get(i)[trgColNum].equals(oldVal)) fromDB.remove(i);
            
            for(int i = tableModel.getRowCount()-1; i >=0; i--){
                String[] tmp = new String[tableModel.getColumnCount()+1];
                int cnt=0;
                for(int j = 0; j < tmp.length; j++){
                    if(j!=trgColNum) tmp[j] = tableModel.getValueAt(i, cnt++);
                }
                tmp[trgColNum] = oldVal;
                
                fromDB.add(tmp);
            }
            TableTools.sotrList(fromDB,trgColNum);
        }
        
        oldVal = val;
        
        tableModel.clear();
        if(trgColNum<0){
            fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        }else{
            String[]data = new String[fromDB.get(0).length - 1];
            for(String[]rowData : fromDB){
                if(rowData[trgColNum].equals(val)){
                    int cnt = 0;
                    for(int i=0; i < rowData.length; i++) if(i!=trgColNum) data[cnt++] = rowData[i];
                    tableModel.addRow(data);
                }
            }
        }
    }

    public boolean isNew(){
        if(tableSize<0) return false;
        return TableTools.isTableDiffDB(fromDB, tableName);
    }
    
    public boolean isCreate(){
        return tableSize>0;
    }
    
    public void saveTableInDB(){
        if(tableSize<0) return;
        globVar.DB.createTable(tableName, listColumn, fromDB, comment);
    }
    public void saveJTable(){
        if(tableSize<0) return;
        globVar.DB.createTable(tableName, listColumn, fromDB, comment);
    }

}
