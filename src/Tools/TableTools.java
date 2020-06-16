package Tools;

import DataBaseConnect.DataBase;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.w3c.dom.Node;

/*@author Lev*/
public class TableTools {//ссылка на таблицу, массив ширин столбцов, массив алигнов - -1 лево, 0 - центр, 1 - право
    // ----- Функция для настройки свойств таблицы --------------Lev---
    static public int setTableSetting(JTable jTable1, int[] colWidth, int[] align, int headerWidth){
        if(jTable1==null) return -1;
        jTable1.setRowSelectionAllowed(true);           // Разрешаю выделять по строкам
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);    // Разрешение выделения столбца
        columnModel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION   );// Режим выделения интервала
        int qCol = jTable1.getColumnCount();                //Определяю количество столбцов
        if(qCol > colWidth.length) qCol = colWidth.length;  //чтобы не вылететь за границы таблицы, если переданный массив ширин неправильный
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //запрещаю изменение ширин солбхов при растягивании окна
        for(int i=0; i<qCol; i++ )                          //устанавливаю ширину всех столбцов
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(colWidth[i]);
        //Работа с алигнами
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(JLabel.LEFT);
        //чтобы не вылететь за границы таблицы, если переданный массив алигнов неправильный
        if(qCol > align.length) qCol = colWidth.length; //Определяю количество столбцов
        for(int i=0; i<qCol; i++ ){// Задаю алигны всем столбцам
            if(align[i]<0)      jTable1.getColumnModel().getColumn(i).setCellRenderer(left);
            else if(align[i]>0) jTable1.getColumnModel().getColumn(i).setCellRenderer(right);
            else                jTable1.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        //Работа с высотой заголовков столбцов, чтобы туда влезали многострочные заголовки
        JTableHeader th = jTable1.getTableHeader();
        int width = th.getSize().width;
        th.setPreferredSize(new Dimension(width, headerWidth));
        th.setSize(width, headerWidth);
        jTable1.repaint();
        return 0;
    }
    
    // ----- Функция для настройки контекстного меню таблиц--------------Lev---
    static public int setPopUpMenu(JTable jTable1, JPopupMenu popupMenu, DefaultTableModel tableModel){
        JMenuItem menuItemAdd = new JMenuItem("Добавить пустую строку");
        JMenuItem menuItemCopy = new JMenuItem("Скопировать строку");
        JMenuItem menuItemRemove = new JMenuItem("Удалить строку");
        JMenuItem menuItemClearCells = new JMenuItem("Очистить ячейки");

        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemCopy);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemClearCells);
        jTable1.setComponentPopupMenu(popupMenu);
        
        menuItemAdd.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0) row = jTable1.getRowCount()-1;
            tableModel.insertRow(row+1, new String[1]);
            setId(jTable1);
        });
        menuItemCopy.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0){
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            String[] r = getRow(jTable1, row);
            tableModel.insertRow(row+1, r);
            setId(jTable1);
        });
        menuItemRemove.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if(row<0){
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить строку "+(row+1)+"?"); // сообщение с выбором
            if(casedial != 0) return; //0 - yes, 1 - no, 2 - cancel            
            tableModel.removeRow(row);
            setId(jTable1);
        });
        menuItemClearCells.addActionListener((ActionEvent event) -> {
            int rows[] = jTable1.getSelectedRows();
            int cols[] = jTable1.getSelectedColumns();
            if(rows.length==0 || cols.length==0){
                JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить содержимое ячеек?"); // сообщение с выбором
            if(casedial != 0) return; //0 - yes, 1 - no, 2 - cancel    
            for(int i=0; i< rows.length; i++) 
                for(int j=0; j< cols.length; j++)
                    jTable1.setValueAt("",rows[i], cols[j]);
        });
        return 0;
    }
    // ----- Функция для расстановки номеров строк в первом столбце --------------Lev---
    static public void setId(JTable jTable1){
        int n = jTable1.getRowCount();
        for(int i=0; i< n; i++) jTable1.setValueAt(i+1,i,0);
    }
    // ----- Функция для считывания строки таблицы --------------Lev---
    static public String[] getRow(JTable jTable1, int row){
        int n = jTable1.getColumnCount();
        String[] s = new String[n];
        for(int i=0; i< n; i++) s[i] = jTable1.getValueAt(row, i).toString();
        return s;
    }
    // ----- функция для загрузки таблицы в базу со стриранием старой таблицы --------------
    static public int saveTableInDB(JTable jTable1, DataBase DB, String tableName, String[] listNameColum, String tableComment){
        if(DB.isTable(tableName)){
            DB.dropTable(tableName);
        }
        DB.createTableEasy(tableName, listNameColum, tableComment);
        int y = jTable1.getRowCount();
        for(int i=0; i<y; i++) DB.insertRow(tableName, getRow(jTable1,i), listNameColum, i);
        return 0;
    }
    
    static public void setTableListener(JFrame frame, SaveFrameData sfd){
        frame.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent event) {}
            public void windowClosed(WindowEvent event) {}
            public void windowClosing(WindowEvent event) {//выполнение операций сохранения данных из фрейма
//                    Object[] options = { "Сохранить", "Не сохранять", "Не закрывать" };
//                    int n = JOptionPane.showOptionDialog(event.getWindow(), "Сохранить изменения перед закрытиемокна?",
//                                    "Вопрос", JOptionPane.YES_NO_OPTION,
//                                    JOptionPane.QUESTION_MESSAGE, null, options,
//                                    options[0]);
//                    if (n == 0) {
                if(sfd!=null) sfd.doIt();
                String title = frame.getTitle();
                globVar.windReg.remove(title);
                event.getWindow().setVisible(false);
                frame.setVisible(false);
            }
            public void windowDeactivated(WindowEvent event) {}
            public void windowDeiconified(WindowEvent event) {}
            public void windowIconified(WindowEvent event) {}
            public void windowOpened(WindowEvent event) {// Регистрация фрейма в регистре
                String title = frame.getTitle();
                if(globVar.windReg.indexOf(title)<0) globVar.windReg.add(title);
                else frame.setVisible(false);
            }
        });
    }
        
    public static void saveListInDB(ArrayList<String[]> list, DataBase DB, String tableName, String[] listNameColum, String comment) {
        if(DB.isTable(tableName)){
            DB.dropTable(tableName);
        }
        DB.createTableEasy(tableName, listNameColum, comment);
        int y = list.size();
        for(int i=0; i<y; i++) DB.insertRow(tableName, list.get(i), listNameColum, i);
    }

    public static void setArchiveSignalList(DefaultListModel list, ArrayList<String[]> archList, int i) {
        for(String[] al: archList) if(al[1].equals(""+i)) list.addElement(al[0]);
    }
    //Заполнение списка источника данных с учётом уже выбранных структур и раскрытых пользователем списков
    public static void setSignalList(DefaultListModel list, ArrayList<String[]> abList, String abonent, boolean commonSig, ArrayList<String[]> archList,
            ArrayList<String> plusList) throws IOException {
        list.removeAllElements();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + "\\Design\\Project.prj");
        Node signals = prj.returnFirstFinedNode(root, "Globals");
        ArrayList<Node> sigList = prj.getHeirNode(signals);
        for(Node n : sigList){
            String sigName = prj.getDataAttr(n, "Name");
            int x = sigName.indexOf("_");
            String sigAb = null;
            if(x>0) sigAb = sigName.substring(0,x);
            boolean ins;
            if(sigAb!=null && sigAb.equals(abonent)) ins = true;
            else if(commonSig)
                if(sigAb==null) ins = true;
                else {
                    ins = true;
                    for(String[] s: abList){
                        if(sigAb.equals(s[1])){
                            ins = false;
                            break;
                        }
                    }
                }
            else ins = false;
            
            if(ins){ //если глобальнаяя структура доложна быть включена в список
                if(plusList.contains(sigName)){ //проверяем, нет ли её в списке раскрытых структур
                    ArrayList<String> plusSigList = openSig(sigName); //если есть - раскрываем структуру
                    for(String psl: plusSigList) if(!isInList(psl,archList,0)) list.addElement(psl);
                }else if(!isInList(sigName,archList,0)) list.addElement(sigName);
            }
        }
    }
    
    public static boolean isInList(String s, ArrayList<String[]> archList, int i){
        if(s==null || archList==null) return false;
        for(String[] a: archList) if(a[i].equals(s)) return true;
        return false;
    } 

    public static ArrayList<String> openSig(String glibSigName) throws IOException{
        ArrayList<String> list = new ArrayList<>();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + "\\Design\\Project.prj");
        Node mySig = prj.findNodeAtribute(root, new String[]{"Signal", "Name", glibSigName});
        String type = prj.getDataAttr(mySig, "Type");
        if("REAL".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type) || "BOOL".equalsIgnoreCase(type) || "WORD".equalsIgnoreCase(type)) return list;
        
        String fileName = FileManager.FindFile(globVar.desDir + "\\Design", type, "UUID=");
        
        XMLSAX sigSax = new XMLSAX();
        Node rootSig = sigSax.readDocument(globVar.desDir + "\\Design\\"+fileName);
        Node signals = sigSax.returnFirstFinedNode(rootSig, "Fields");
        ArrayList<Node> sigList = sigSax.getHeirNode(signals);
        if(sigList == null || sigList.isEmpty()) return list;
        for(Node n : sigList) list.add("– " + glibSigName + "." + sigSax.getDataAttr(n, "Name"));
        return list;
    }
}
