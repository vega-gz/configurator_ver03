package Tools;

import FrameCreate.TableNzVer3;
import globalData.globVar;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public final class SimpleTable {
    public MyTableModel tableModel = new MyTableModel();
    public JPopupMenu popupMenu = new JPopupMenu();
    public boolean isChang = false;
    public String tableName;
    public int tableSize;
    public String[] cols;
    public String comment;
    public ArrayList<String[]> fromDB;
    public ArrayList<String[]> listItemList = new ArrayList<>();
    public ArrayList<ArrayList> listToTable = new ArrayList<>(); // Лист для передачи в таблицу
    public int[] colsWidth;
    int[] align;
    int qCol;
    int trgColNum = -1;
    ArrayList<JFrame> listJF = new ArrayList();
    String trgCol;
    
    
    public SimpleTable(String table, String col,String val, String trgCol) {
        if(!globVar.DB.isConnectOK())return;
        ArrayList<String> listColumn = globVar.DB.getListColumns(table);
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
        resetTableContent(val);
        tableSize = fromDB.size();
        qCol = cols.length;
        align = new int[qCol];
        colsWidth = new int[qCol];

        TableTools.setWidthCols(cols, tableModel, colsWidth, 7.7);
        if (tableSize > 0) {
            TableTools.setAlignCols(fromDB.get(0), align);
        }
    }

    public void setSimpleTableSettings(JTable jTable1) {
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel, null, null, null);
        TableTools.setTableSetting(jTable1, colsWidth, align, 20);
        TableTools.setColsEditor(tableName, cols, fromDB, jTable1, listItemList);
    }
    
    public void resetTableContent(String val){//String col,
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
        //isNew(fromDB, tableModel);
    }

    private boolean isNew(ArrayList<String[]> fromDB, DefaultTableModel tableModel) {
        if (fromDB == null || tableModel == null) {
            return false;
        }
        int fY = fromDB.size();
        if (fY <= 0) {
            return false;
        }
        int x = tableModel.getColumnCount();
        int y = tableModel.getRowCount();
        for (int i = 0; i < y; i++) {
            boolean is = false;
            for (int k = 0; k < fY; k++) {
                boolean lokal;
                for (int j = 0; j < x; j++) {
                    if (!fromDB.get(k)[j].equals(tableModel.getValueAt(i, j))) {
                        is = true;
                    }
                }
            }
        }
        return false;
    }

}
