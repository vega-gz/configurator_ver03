package Tools;

import DataBaseTools.DataBase;
import FrameCreate.SinglStrEdit;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.w3c.dom.Node;

/*@author Lev*/
public class TableTools {//ссылка на таблицу, массив ширин столбцов, массив алигнов - -1 лево, 0 - центр, 1 - право
    // ----- Функция для настройки свойств таблицы --------------Lev---

    static public int setTableSetting(JTable jTable1, int[] colWidth, int[] align, int headerWidth) {
        if (jTable1 == null) {
            return -1;
        }
        jTable1.setRowSelectionAllowed(true);           // Разрешаю выделять по строкам
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);    // Разрешение выделения столбца
        columnModel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);// Режим выделения интервала
        int qCol = jTable1.getColumnCount();                //Определяю количество столбцов
        if (qCol > colWidth.length) {
            qCol = colWidth.length;  //чтобы не вылететь за границы таблицы, если переданный массив ширин неправильный
        }
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //запрещаю изменение ширин солбхов при растягивании окна
        for (int i = 0; i < qCol; i++) //устанавливаю ширину всех столбцов
        {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(colWidth[i]);
        }
        //Работа с алигнами
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(JLabel.LEFT);
        //чтобы не вылететь за границы таблицы, если переданный массив алигнов неправильный
        if (qCol > align.length) {
            qCol = align.length; //Определяю количество столбцов
        }
        for (int i = 0; i < qCol; i++) {// Задаю алигны всем столбцам
            if (align[i] < 0) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(left);
            } else if (align[i] > 0) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(right);
            } else {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(center);
            }
        }
        //Первый столбик всегда не редактируемый, потому серый
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        System.setProperty("myColor", "0XEEEEEE");
        defaultTableCellRenderer.setBackground(Color.getColor("myColor")); //задаем цвет столбца
        columnModel.getColumn(0).setCellRenderer(defaultTableCellRenderer);
        //Работа с высотой заголовков столбцов, чтобы туда влезали многострочные заголовки
        JTableHeader th = jTable1.getTableHeader();
        int width = th.getSize().width;
        th.setPreferredSize(new Dimension(width, headerWidth));
        th.setSize(width, headerWidth);
        jTable1.repaint();
        return 0;
    }

    // ----- Функция для настройки контекстного меню таблиц--------------Lev---
    static public int setPopUpMenu(JTable jTable1, JPopupMenu popupMenu, MyTableModel tableModel, String title, regitrationJFrame rgf, ArrayList<JFrame> listJF) {
        JMenuItem menuItemAdd = new JMenuItem("Добавить пустую строку");
        JMenuItem menuItemCopy = new JMenuItem("Скопировать строку");
        JMenuItem menuItemRemove = new JMenuItem("Удалить строку");
        JMenuItem menuItemClearCells = new JMenuItem("Очистить ячейки");
        JMenuItem menuItemOpenWindow = null;
        if (rgf != null) {
            menuItemOpenWindow = new JMenuItem("Открыть в отдельном окне");
        }

        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemCopy);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemClearCells);
        if (rgf != null) {
            popupMenu.add(menuItemOpenWindow);
        }
        jTable1.setComponentPopupMenu(popupMenu);
        jTable1.getTableHeader().setComponentPopupMenu(popupMenu);

        if (rgf != null) {
            menuItemOpenWindow.addActionListener((ActionEvent event) -> {
                int row = jTable1.getSelectedRow();
                if (row < 0) {
                    row = 0;
                }
                SinglStrEdit sse = new SinglStrEdit(tableModel, title, listJF);
                sse.setVisible(true);
                if (rgf != null) {
                    rgf.reg(sse);
                }
                sse.setFields(row);
            });
        }
        menuItemAdd.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if (row < 0) {
                row = jTable1.getRowCount() - 1;
            }
            row++;
            tableModel.insertRow(row, new String[1]);
            for (int i = 1; i < tableModel.getColumnCount(); i++) {
                tableModel.setValueAt("", row, i);
            }
            setId(jTable1);
        });
        menuItemCopy.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            String[] r = getRow(jTable1, row);
            tableModel.insertRow(row + 1, r);
            setId(jTable1);
        });
        menuItemRemove.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить строку " + (row + 1) + "?"); // сообщение с выбором
            if (casedial != 0) {
                return; //0 - yes, 1 - no, 2 - cancel            
            }
            tableModel.removeRow(row);
            setId(jTable1);
        });
        menuItemClearCells.addActionListener((ActionEvent event) -> {
            int rows[] = jTable1.getSelectedRows();
            int cols[] = jTable1.getSelectedColumns();
            if (rows.length == 0 || cols.length == 0) {
                JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
                return;
            }
            int casedial = JOptionPane.showConfirmDialog(null, "Удалить содержимое ячеек?"); // сообщение с выбором
            if (casedial != 0) {
                return; //0 - yes, 1 - no, 2 - cancel    
            }
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < cols.length; j++) {
                    if (cols[j] > 0) {
                        jTable1.setValueAt("", rows[i], cols[j]);
                    }
                }
            }
        });

        return 0;
    }

    // ----- Функция для расстановки номеров строк в первом столбце --------------Lev---

    static public void setId(JTable jTable1) {
        int n = jTable1.getRowCount();
        for (int i = 0; i < n; i++) {
            jTable1.setValueAt(i + 1, i, 0);
        }
    }

    // ----- Функция для считывания строки таблицы --------------Lev---

    static public String[] getRow(JTable jTable1, int row) {
        int n = jTable1.getColumnCount();
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = jTable1.getValueAt(row, i).toString();
        }
        return s;
    }

    // ----- функция для загрузки таблицы в базу со стриранием старой таблицы --------------

    static public int saveTableInDB(JTable jTable1, DataBase DB, String tableName, String[] listNameColum, String tableComment, ArrayList<String[]> fromDB) {
        if (DB.isTable(tableName)) {
            if (tableComment == null) {
                tableComment = DB.getCommentTable(tableName);
            }
            DB.dropTable(tableName);
        }
        DB.createTableEasy(tableName, listNameColum, tableComment);
        fromDB.clear();
        int y = jTable1.getRowCount();
        for (int i = 0; i < y; i++) {
            DB.insertRow(tableName, getRow(jTable1, i), listNameColum, i);
            fromDB.add(getRow(jTable1, i));
        }
        return 0;
    }

    static public int saveTableInDB(JTable jTable1, DataBase DB, String tableName, String[] listNameColum, String tableComment) {
        if (DB.isTable(tableName)) {
            if (tableComment == null) {
                tableComment = "";
            }
            DB.dropTable(tableName);
        }
        DB.createTableEasy(tableName, listNameColum, tableComment);
        int y = jTable1.getRowCount();
        for (int i = 0; i < y; i++) {
            DB.insertRow(tableName, getRow(jTable1, i), listNameColum, i);
        }
        return 0;
    }

    static public void setTableListener(JFrame frame, SaveFrameData sfd, isCange ich, closeJFrame cjf) {
        frame.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowClosing(WindowEvent event) {//выполнение операций сохранения данных из фрейма
                int n = 1;
                if (ich.is()) {
                    Object[] options = {"Сохранить", "Не сохранять", "Не закрывать"};

                    n = JOptionPane.showOptionDialog(event.getWindow(), "Сохранить изменения?",
                            "Вопрос", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options,
                            options[0]);
                }
                if (n == 2) {
                    return;
                }
                if (sfd != null && n == 0) {
                    sfd.doIt();
                }
                String title = frame.getTitle();
                globVar.processReg.remove(title);
                event.getWindow().setVisible(false);
                frame.setVisible(false);
                if (cjf != null) {
                    cjf.close();
                }
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
            }

            public void windowOpened(WindowEvent event) {// Регистрация фрейма в регистре
                String title = frame.getTitle();
                if (globVar.processReg.indexOf(title) < 0) {
                    globVar.processReg.add(title);
                } else {
                    frame.setVisible(false);
                }
            }
        });
    }

    public static void saveListInDB(ArrayList<String[]> list, DataBase DB, String tableName, String[] listNameColum, String comment) {
        if (DB.isTable(tableName)) {
            DB.dropTable(tableName);
        }
        DB.createTableEasy(tableName, listNameColum, comment);
        int y = list.size();
        for (int i = 0; i < y; i++) {
            DB.insertRow(tableName, list.get(i), listNameColum, i);
        }
    }

    public static void setArchiveSignalList(DefaultListModel list, ArrayList<String[]> archList, String i) {
        for (String[] al : archList) {
            if (al[1].equals(i)) {
                list.addElement(al[0]);
            }
        }
    }

    //Заполнение списка источника данных с учётом уже выбранных структур и раскрытых пользователем списков

    public static void setSignalList(DefaultListModel list, ArrayList<String[]> abList, String abonent, boolean commonSig, ArrayList<String[]> archList,
            ArrayList<String> plusList) throws IOException {
        list.removeAllElements();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
        Node signals = prj.returnFirstFinedNode(root, "Globals");
        ArrayList<Node> sigList = prj.getHeirNode(signals);
        for (Node n : sigList) {
            boolean ins;
            String sigName = prj.getDataAttr(n, "Name");
            if(abList!=null){
                int x = sigName.indexOf("_");
                String sigAb = null;
                if (x > 0) {
                    sigAb = sigName.substring(0, x);
                }
                if (sigAb != null && sigAb.equals(abonent)) {
                    ins = true;
                } else if (commonSig) {
                    if (sigAb == null) {
                        ins = true;
                    } else {
                        ins = true;
                        for (String[] s : abList) {
                            if (sigAb.equals(s[1])) {
                                ins = false;
                                break;
                            }
                        }
                    }
                } else ins = false;
            } else ins = true;
            
            if (ins) { //если глобальнаяя структура доложна быть включена в список
                if (plusList.contains(sigName)) { //проверяем, нет ли её в списке раскрытых структур
                    ArrayList<String> plusSigList = openSig(sigName); //если есть - раскрываем структуру
                    for (String psl : plusSigList) {
                        if (!isInList(psl, archList, 0)) {
                            list.addElement(psl);
                        }
                    }
                } else if (!isInList(sigName, archList, 0)) {
                    list.addElement(sigName);
                }
            }
        }
    }

    public static boolean isInList(String s, ArrayList<String[]> archList, int i) {
        if (s == null || archList == null) {
            return false;
        }
        for (String[] a : archList) {
            if (a[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> openSig(String glibSigName) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + File.separator+"Design"+File.separator+"Project.prj");
        Node mySig = prj.findNodeAtribute(root, new String[]{"Signal", "Name", glibSigName});
        String type = prj.getDataAttr(mySig, "Type");
        if ("REAL".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type) || "BOOL".equalsIgnoreCase(type) || "WORD".equalsIgnoreCase(type)) {
            return list;
        }

        String fileName = FileManager.FindFile(globVar.desDir + File.separator+"Design", type, "UUID=");

        XMLSAX sigSax = new XMLSAX();
        Node rootSig = sigSax.readDocument(globVar.desDir + File.separator+"Design"+File.separator + fileName);
        Node signals = sigSax.returnFirstFinedNode(rootSig, "Fields");
        ArrayList<Node> sigList = sigSax.getHeirNode(signals);
        if (sigList == null || sigList.isEmpty()) {
            return list;
        }
        for (Node n : sigList) {
            list.add("– " + glibSigName + "." + sigSax.getDataAttr(n, "Name"));
        }
        return list;
    }

    //---------Функция для исключения показываемых столбцов -----------------

    public static boolean exeptCol(String s, String[] exCol) {
        for (String e : exCol) {
            if (e.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static void setWidthCols(String[] cols, MyTableModel tableModel, int[] colsWidth, double x) {
        if (colsWidth == null) {
            return;
        }
        int max = colsWidth.length;
        if (cols != null && max > cols.length) {
            max = cols.length;
        }
        if (cols != null && max > tableModel.getColumnCount()) {
            max = tableModel.getColumnCount();
        }
        for (int i = 0; i < max; i++) {
            if (cols != null && cols[i] != null) {
                colsWidth[i] = cols[i].length();
            } else {
                colsWidth[i] = 0;
            }
            for (int j = 0; j < tableModel.getRowCount(); j++) {
                if (tableModel.getValueAt(j, i) != null) {
                    int l = tableModel.getValueAt(j, i).length();
                    if (colsWidth[i] < l) {
                        colsWidth[i] = l;
                    }
                }
            }
            colsWidth[i] = (int) ((colsWidth[i] + 1) * x);
        }
    }

    public static void setAlignCols(String[] fromDB, int[] colsAlign) {
        for (int i = 0; i < colsAlign.length; i++) {
            if (fromDB[i] == null || fromDB[i].isEmpty()) {
                colsAlign[i] = 0;
            } else if ("-0123456789".contains(fromDB[i].substring(0, 1))) {
                colsAlign[i] = 1;
            } else {
                colsAlign[i] = -1;
            }
        }
    }

    public static void setColsEditor(String table, String[] cols, ArrayList<String[]> fromDB, JTable jTable1, ArrayList<String[]> listItemList) {
        XMLSAX xml = new XMLSAX();
        Node root = xml.readDocument("TableColumnSettings.xml");
        ArrayList<Node> nl = xml.getHeirNode(root);
        for (Node n : nl) {
            if (table.contains(n.getNodeName())) {
                ArrayList<Node> nc = xml.getHeirNode(n);
                for (Node c : nc) {
                    for (int i = 0; i < cols.length; i++) {
                        String[] listItems = {};
                        if (cols[i].equals(c.getNodeName())) {
                            String src = xml.getDataAttr(c, "src");
                            String editable = xml.getDataAttr(c, "edit");
                            if (src.equals("file")) {
                                String file = xml.getDataAttr(c, "file");
                                String col = xml.getDataAttr(c, "col");
                                XMLSAX list = new XMLSAX();
                                Node listRoot = list.readDocument(file);
                                ArrayList<Node> listNameNode = list.getHeirNode(listRoot);
                                int ls = listNameNode.size();
                                listItems = new String[ls];
                                if (col.isEmpty()) {
                                    for (int j = 0; j < ls; j++) {
                                        listItems[j] = listNameNode.get(j).getNodeName();
                                    }
                                } else {
                                    for (int j = 0; j < ls; j++) {
                                        listItems[j] = list.getDataAttr(listNameNode.get(j), col);
                                    }
                                }
                            } else if (src.equals("this")) {
                                ArrayList<Node> list = xml.getHeirNode(c);
                                int ls = list.size();
                                listItems = new String[ls];
                                for (int j = 0; j < ls; j++) {
                                    listItems[j] = xml.getDataAttr(list.get(j), "t");
                                }
                            } else if (src.equals("DB")) {
                                ArrayList<String> listTable = globVar.DB.getListTable();
                                listItems = listTable.toArray(new String[listTable.size()]);
                            } else if (src.equals("table")) {
                                String colInTable = xml.getDataAttr(c, "colInTable");
                                String tableName = xml.getDataAttr(c, "table");
                                if (tableName.equals("this")) {
//                                String col = xml.getDataAttr(c, "col");
//                                String def = xml.getDataAttr(c, "default");
//                                tableName = getCell(col, int i);
                                    int x = table.indexOf("_");
                                    tableName = table.substring(0, x + 1) + "AI";
                                }
                                ArrayList<String[]> list = globVar.DB.getData(tableName, new String[]{colInTable});
                                listItems = new String[list.size()];
                                for (int j = 0; j < list.size(); j++) {
                                    listItems[j] = list.get(j)[0];
                                }
                            }
                            if (listItems.length != 0) {
                                JComboBox<String> combo = new JComboBox<>(listItems);// Раскрывающийся список
                                if (editable != null) {
                                    combo.setEditable(true);
                                }
                                combo.getEditor().getEditorComponent().addKeyListener(new java.awt.event.KeyAdapter() {
                                    public void keyReleased(java.awt.event.KeyEvent e) {
                                        String filtr = (String) combo.getEditor().getItem();
                                        int i = jTable1.getSelectedColumn();
                                        ArrayList<String> al = new ArrayList<>();
                                        for (String s : listItemList.get(i)) {
                                            if (s.toUpperCase().indexOf(filtr.toUpperCase()) == 0) {
                                                al.add(s);
                                            }
                                        }
                                        String[] listItems = al.toArray(new String[al.size()]);
                                        combo.setModel(new DefaultComboBoxModel(listItems));
                                        combo.getEditor().setItem(filtr);
                                        combo.showPopup();
                                    }
                                });
                                DefaultCellEditor editor = new DefaultCellEditor(combo);// Редактор ячейки с раскрывающимся списком
                                jTable1.getColumnModel().getColumn(i).setCellEditor(editor);    // Определение редактора ячеек для колонки    
                            }
                        }
                        listItemList.add(listItems);
                    }
                }
            }
        }
    }

    public static void jTable1MouseClicked(java.awt.event.MouseEvent evt, JTable jTable1, MyTableModel tableModel, String tableName, ArrayList<JFrame> listJF) {
        if (evt.getClickCount() == 2) {
            int row = jTable1.getSelectedRow();
            FrameCreate.SinglStrEdit sse = new SinglStrEdit(tableModel, tableName, listJF);
            sse.setVisible(true);
            listJF.add(sse);
            sse.setFields(row);
        }
    }

}