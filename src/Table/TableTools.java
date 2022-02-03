package Table;

import DataBaseTools.DataBase;
import FrameCreate.SinglStrEdit;
import ReadWriteExcel.ExcelAdapter;
import Tools.FileManager;
import Tools.LoggerFile;
import Tools.LoggerInterface;
import Tools.MyTableModel;
import Tools.RegistrationJFrame;
import Tools.SaveFrameData;
import Tools.StrTools;
import Tools.Tools;
import Tools.closeJFrame;
import Tools.isCange;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.w3c.dom.Node;

/*@author Lev*/
public class TableTools {//ссылка на таблицу, массив ширин столбцов, массив алигнов - -1 лево, 0 - центр, 1 - право

    static ArrayList<String[]> list_str = new ArrayList<>();
    static ArrayList<String> list_cells = new ArrayList<>();
    HashMap<int[], Object> current_change = new HashMap<>();
    int index_changes = 1;//индекс отсчета изменений
    static int rows[] = {};
    static int cols[] = {};
    // ----- Функция для настройки свойств таблицы --------------Lev---

    static public int setTableSetting(JTable jTable1, int[] colWidth, int[] align, int headerWidth) {
        if (jTable1 == null) {
            return -1;
        }
        jTable1.setRowSelectionAllowed(true);           // Разрешаю выделять по строкам
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);    // Разрешение выделения только в предах одного столбца
        columnModel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);// Режим выделения интервала
        int qCol = jTable1.getColumnCount();                //Определяю количество столбцов
        if (qCol > colWidth.length) {
            qCol = colWidth.length;  //чтобы не вылететь за границы таблицы, если переданный массив ширин неправильный
        }
        
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //запрещаю изменение ширин столбцов при растягивании окна
        
        for (int i = 0; i < qCol; i++) //устанавливаю ширину всех столбцов
        {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(colWidth[i]);
            
//            //Component component = jTable1.prepareRenderer(colWidth[i]);
//            int rendererWidth = component.getPreferredSize().width;
//            TableColumn tableColumn = jTable1.getColumnModel().getColumn(i);
//            tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
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
        
        //Работа с высотой заголовков столбцов, чтобы туда влезали многострочные заголовки(не работает идет смещение)
//        JTableHeader th = jTable1.getTableHeader();
//        int width = th.getSize().width;
//        th.setPreferredSize(new Dimension(width, headerWidth));
//        th.setSize(width, headerWidth);
//        jTable1.repaint();

        // --- Вставка и копирование объектов таблицы (есть работа с excel) ---
        ExcelAdapter editT = new ExcelAdapter(jTable1);
        return 0;
    }

    //---метод,который помещает изменения в хэшмэп
    public void currentMap(int rows, int cols, Object value) {
        int[] index_value = {rows, cols};
        current_change.put(index_value, value);
        index_changes++;
        if (index_changes == 20) {
            current_change.remove(current_change.keySet().toArray()[current_change.keySet().size() - 1]);//удаляем последний элемент
        }
    }

//    // --- открыть сигнал в отдельном окне ---
//      public static void OpenFrameEditableSignal(JTable jTable1) {
//        int row = jTable1.getSelectedRow();
//        if (row < 0) {
//            row = 0;
//        }
//        int countcolumn = jTable1.getColumnCount();
//        String nameSinglSeting = null;
//        for (int i = 0; i < countcolumn; i++) {
//            String nameColumn = jTable1.getColumnName(i);
//            if (nameColumn.equals(globVar.TAGPLC)) {
//                nameSinglSeting = (String) jTable1.getValueAt(row, i);
//                break;
//            }
//        }
//        SinglStrEdit sse = new SinglStrEdit(jTable1.getModel(), nameSinglSeting, jTable1.getName());
//        sse.setVisible(true);
//        rgf.reg(sse);
//        sse.setFields(row);
//    }
    
    //----- Функция для настройки контекстного меню таблиц--------------Lev----//
    static public int setPopUpMenu(JTable jTable1, JPopupMenu popupMenu, MyTableModel tableModel, String title, RegistrationJFrame rgf, ArrayList<JFrame> listJF) {
        JMenuItem menuItemCopyStr = new JMenuItem("Скопировать строки");
        JMenuItem menuItemIncertStr = new JMenuItem("Вставить строки");
        JMenuItem menuItemClearCells = new JMenuItem("Очистить ячейки");
        JMenuItem menuItemRemove = new JMenuItem("Удалить строку");
        JMenuItem menuItemFillCells = new JMenuItem("Заполнить ячейки");
        JMenuItem menuItemCopyCells = new JMenuItem("Скопировать ячейки");
        JMenuItem menuItemIncertCells = new JMenuItem("Вставить ячейки");
        JMenuItem menuItemOpenWindow = null;

        ImageIcon icon_copy = new ImageIcon("icon_copy.png");
        menuItemCopyStr.setRequestFocusEnabled(false);
        menuItemCopyStr.setIcon(icon_copy); //   устанавливает необходимую иконку вставить
        
        ImageIcon icon_paste = new ImageIcon("icon_paste.png");
        menuItemCopyCells.setRequestFocusEnabled(false);
        menuItemCopyCells.setIcon(icon_paste); //   устанавливает необходимую иконку вставить
        menuItemCopyCells.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        menuItemIncertCells.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));

        if (rgf != null) {
            menuItemOpenWindow = new JMenuItem("Открыть в отдельном окне");
        }

        popupMenu.add(menuItemCopyStr);
        popupMenu.add(menuItemIncertStr);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemClearCells);
        popupMenu.add(menuItemFillCells);
        popupMenu.add(menuItemCopyCells);
        popupMenu.add(menuItemIncertCells);
        if (rgf != null) {
            popupMenu.add(menuItemOpenWindow);
        }
        jTable1.setComponentPopupMenu(popupMenu);
        jTable1.getTableHeader().setComponentPopupMenu(popupMenu); // к заголовку меню так как при пустой таблице не создать строки

        if (rgf != null) {//---
            menuItemOpenWindow.addActionListener((ActionEvent event) -> {
                
                int row = jTable1.getSelectedRow();
                if (row < 0) {
                    row = 0;
                }
                int countcolumn = jTable1.getColumnCount();
                String nameSinglSeting = null;
                for (int i = 0; i < countcolumn; i++) {
                    String nameColumn = jTable1.getColumnName(i);
                    if(nameColumn.equals(globVar.TAGPLC))
                    {
                        nameSinglSeting = (String)jTable1.getValueAt(row, i);
                        break;
                    }
                }
                //SinglStrEdit sse = new SinglStrEdit(tableModel, nameSinglSeting, jTable1.getName(), listJF);
                SinglStrEdit sse = new SinglStrEdit(tableModel, nameSinglSeting, jTable1.getName());
                sse.setVisible(true);
                rgf.reg(sse);
                sse.setFields(row);
            });
        }
        menuItemCopyCells.addActionListener((ActionEvent event) -> {
            rows = jTable1.getSelectedRows();
            cols = jTable1.getSelectedColumns();
            String value_cells;
            if (rows.length == 0 || cols.length == 0) {
                JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
                return;
            }
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < cols.length; j++) {
                    value_cells = tableModel.getValueAt(rows[i], cols[j]);
                    list_cells.add(value_cells);
                }

            }
        });
        menuItemIncertCells.addActionListener((ActionEvent event) -> {
            int startRow = (jTable1.getSelectedRows())[0];
            int startCol = (jTable1.getSelectedColumns())[0];
            int value_number = 0;
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < cols.length; j++) {
                    jTable1.setValueAt(list_cells.get(value_number), startRow + i, startCol + j);
                    value_number++;
                }

            }
            list_cells.clear();
        });
        menuItemCopyStr.addActionListener((ActionEvent event) -> {
            rows = jTable1.getSelectedRows();
            cols = jTable1.getSelectedColumns();
            if (rows.length == 0 || cols.length == 0) {
                JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
                return;
            }
            for (int i = 0; i < rows.length; i++) {
                String[] r = getRow(jTable1, rows[i]);
                list_str.add(r);

            }
        });
        menuItemIncertStr.addActionListener((ActionEvent event) -> {
            int row = jTable1.getSelectedRow();
            if (list_str.size() == 0) {
                row++;
                tableModel.insertRow(row, new String[1]);
                for (int i = 1; i < tableModel.getColumnCount(); i++) {
                    tableModel.setValueAt("", row, i);
                }
                setId(jTable1);
            }
            for (int i = 0; i < list_str.size(); i++) {
                tableModel.insertRow(row, list_str.get(i));

                setId(jTable1);
                row++;
            }
            list_str.clear();

        });
        menuItemRemove.addActionListener((ActionEvent event) -> {
           
            int row[] = jTable1.getSelectedRows();
            if (row.length < 0) {
                JOptionPane.showMessageDialog(null, "Ни одна строка не помечена");
                return;
            }
//            Object[] options = {"Да", "Нет"};
//            int casedial = JOptionPane.showOptionDialog(null, "Удалить строку " + (row + 1) + "?", "Выберите действие",
//                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); // сообщение с выбором
//            if (casedial != 0) {
//                return; //0 - yes, 1 - no, 2 - cancel            
//            }
//            tableModel.removeRow(row);
//            setId(jTable1);
            for(int i=0;i<row.length;i++){
                tableModel.removeRow(row[0]);
                setId(jTable1);
                System.out.println("Удалена строка "+row[i]);
            }
            

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
        menuItemFillCells.addActionListener((ActionEvent event) -> {
            fillCells(jTable1, tableModel);
        });
        return 0;
    }

    public static void fillCells(JTable jTable1, MyTableModel tableModel) {
        rows = jTable1.getSelectedRows();
        cols = jTable1.getSelectedColumns();
        if (rows.length == 0 || cols.length == 0) {
            JOptionPane.showMessageDialog(null, "Ни одна ячейка не помечена");
            return;
        }
        String chosseName;
        ArrayList<String> cellNames = new ArrayList<>();
        for (int i = 0; i < cols.length; i++) {//пробегаемся по столбцам
            for (int j = 0; j < rows.length; j++) {//пробегаемся по строкам
                chosseName = tableModel.getValueAt(rows[j], cols[i]);
                if (!chosseName.isEmpty()) {//если не пустая,записываем в лист
                    cellNames.add(chosseName);//записываем в лист имена ячеек для последующего сравнения
                } else {//бежим цикл до первой пустой ячейки
                    if (cellNames.size() != 2) {
                        Tools.fillCellCol(jTable1, cellNames, rows, cols[i]);
                        break;
                    } else {
                        //---получение значениe 1-ой ячейки---//
                        String st = cellNames.get(0);
                        Matcher matcher = Pattern.compile("\\D+$").matcher(st);
                        matcher.find();
                        int numberEnd = st.length();
                        try {
                            numberEnd = matcher.start();
                        } catch (IllegalStateException e) {
                        }
                        if (numberEnd == 0) {
                            Tools.fillCellCol(jTable1, cellNames, rows, cols[i]);
                            break;
                        }
                        String end1 = st.substring(numberEnd);
                        st = st.substring(0, numberEnd);
                        matcher = Pattern.compile("[\\d\\.]+$").matcher(st);
                        matcher.find();
                        int numberStart = st.length();
                        try {
                            numberStart = matcher.start();
                        } catch (IllegalStateException e) {
                        }
                        String dobS = st.substring(numberStart);
                        String start1 = st.substring(0, numberStart);
                        boolean isInt = Tools.isInteger(dobS);
                        Double dob1;
                        if (Tools.isNumeric(dobS)) {
                            dob1 = Double.parseDouble(dobS);
                        } else {
                            Tools.fillCellCol(jTable1, cellNames, rows, cols[i]);
                            break;
                        }

                        //---получение значениe 2-ой ячейки---//
                        st = cellNames.get(1);
                        matcher = Pattern.compile("\\D+$").matcher(st);
                        matcher.find();
                        numberEnd = st.length();
                        try {
                            numberEnd = matcher.start();
                        } catch (IllegalStateException e) {
                        }
                        String end2 = st.substring(numberEnd);
                        st = st.substring(0, numberEnd);

                        matcher = Pattern.compile("[\\d\\.]+$").matcher(st);
                        matcher.find();
                        numberStart = st.length();
                        try {
                            numberStart = matcher.start();
                        } catch (IllegalStateException e) {
                        }
                        dobS = st.substring(numberStart);
                        String start2 = st.substring(0, numberStart);
                        if (!end2.equals(end1) || !start2.equals(start1)) {
                            Tools.fillCellCol(jTable1, cellNames, rows, cols[i]);
                            break;
                        }
                        isInt = isInt && Tools.isInteger(dobS);
                        Double dob2;
                        if (Tools.isNumeric(dobS)) {
                            dob2 = Double.parseDouble(dobS);
                        } else {
                            Tools.fillCellCol(jTable1, cellNames, rows, cols[i]);
                            break;
                        }
                        Double inc = dob2 - dob1;//вычисление инкремента
                        for (int k = 1; k < rows.length; k++) {
                            Double dobInc = dob1 + inc * k;
                            String dbi;
                            if (isInt) {
                                dbi = "" + dobInc.intValue();
                            } else {
                                Double z = Math.pow(10.0, (dobS.length() - dobS.indexOf(".") - 1));
                                dobInc = Math.ceil(dobInc * z) / z;
                                dbi = "" + dobInc;
                            }
                            jTable1.setValueAt(start1 + dbi + end1, rows[k], cols[i]);
                        }
                        break;
                    } //                    
                }
            }
            cellNames.clear();//обнуляем все для следующего столбца
        }
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
    static public int saveTableInDB(JTable jTable1, DataBase DB, String tableName, String tableComment, ArrayList<String[]> fromDB) {
        if (DB.isTable(tableName)) {
            if (tableComment == null) {
                tableComment = DB.getCommentTable(tableName);
            }
            DB.dropTable(tableName);
        }
        int countcolumn = jTable1.getColumnCount();
        String[] listNameColumTmp = new String[countcolumn];
        String nameSinglSeting = null;
        for (int i = 0; i < countcolumn; i++) {
            listNameColumTmp[i] = jTable1.getColumnName(i); 
        }
        
        DB.createTableEasy(tableName, listNameColumTmp, tableComment);
        fromDB.clear();
        int y = jTable1.getRowCount();
        for (int i = 0; i < y; i++) {
            DB.insertRow(tableName, getRow(jTable1, i), listNameColumTmp, i);
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

    // надо бы этот функционал в отдельный интерфейс и он не только для таблиц :/
    static public void setFrameListener(JFrame frame, SaveFrameData sfd, isCange ich, closeJFrame cjf) {
        frame.addWindowListener(new WindowListener() {

            boolean onCreate = false; // триггер для создания окна

            // когда ткнули на окно или сняли с него фокус(при любых действиях)
            public void windowActivated(WindowEvent event) {
//                if (onCreate) { // проверка только после создания окна
//                    ++globVar.sumFrame;
//                    if (!globVar.windowIconified) { // проверка на сворачивание
//                        if (globVar.captureFocus) { // только одно окно может захватить управление
//                            globVar.captureFocus = false; // сразу блочим для остальныхF
//                            for (int i = 0; i < globVar.listF.size(); i++) {
//                                JFrame f = globVar.listF.get(i);
//                                // все окна на передний план
//                                if (f == frame) {
//                                    System.out.println("I am find myself " + f.getTitle());
//                                    --globVar.sumFrame; // нужен правильный подсчет
//                                    continue;
//                                }
//                                //f.setExtendedState(JFrame.ICONIFIED);
//                                f.setExtendedState(JFrame.NORMAL);
//                                f.toFront();
//                                f.requestFocus();
//                            }
//
//                        }
//                    } else {
//                        globVar.windowIconified = false;
//                        globVar.sumFrame = 0;
//                    }
//
//                    if (globVar.sumFrame >= globVar.listF.size()) { // проверка раньше времени чем обработка
//                        globVar.sumFrame = 0;
//                        globVar.captureFocus = true; // разблокировка
//
//                    }
//                }
            }

            public void windowClosed(WindowEvent event) {
                System.out.println("windowClosing " + frame.getTitle());
                for (int i = 0; i < globVar.listF.size(); i++) {
                    JFrame f = globVar.listF.get(i);
                    if (f == frame) {
                        globVar.listF.remove(f);
                    }
                }
                String title = frame.getTitle();
                globVar.processReg.remove(title);
                event.getWindow().setVisible(false);
                frame.setVisible(false);
            }

            public void windowClosing(WindowEvent event) {//операции при закрытии окна
                int n = 1;
                if (ich != null) {
                    if (ich.is()) {
                        Object[] options = {"Сохранить", "Не сохранять", "Не закрывать"};

                        n = JOptionPane.showOptionDialog(event.getWindow(), "Сохранить изменения?",
                                "Вопрос", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                    }
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
                onCreate = true;
                globVar.listF.add(frame);
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
        if (archList == null) {
            return;
        }
        for (String[] al : archList) {
            if (al[1].equals(i)) {
                list.addElement(al[0]);
            }
        }
    }

    public static void setSignalListFromDB(DefaultListModel list, ArrayList<String> tabList, String abonent, boolean b,
            ArrayList<String[]> archList, ArrayList<String> plusList) {
        boolean ins = true;
        list.removeAllElements();
        for (String tn : tabList) {
            if (ins) { //если глобальнаяя структура доложна быть включена в список
                if (plusList.contains(tn)) { //проверяем, нет ли её в списке раскрытых структур
                    //ArrayList<String> plusSigList = openSigFromDB(tn); //если есть - раскрываем структуру
                    ArrayList<String> plusSigList = globVar.DB.getDataFromColumn(tn, "TAG_NAME_PLC", "");
                    for (String psl : plusSigList) {
                        String tmp = "– " + tn + "." + psl;
                        if (!isInList(tmp, archList, 0)) {
                            list.addElement(tmp);
                        }
                    }
                } else if (!isInList(tn, archList, 0)) {
                    list.addElement(tn);
                }
            }
        }
    }

    //Заполнение списка источника данных с учётом уже выбранных структур и раскрытых пользователем списков
    public static void setSignalList(DefaultListModel list, ArrayList<String[]> abList, String abonent, boolean commonSig, ArrayList<String[]> archList,
            ArrayList<String> plusList) {// throws IOException {
        setSignalList(list, abList, abonent, commonSig, archList, plusList, null);
    }

    public static void setSignalList(DefaultListModel list, ArrayList<String[]> abList, String abonent, boolean commonSig,
            ArrayList<String[]> archList, ArrayList<String> plusList, String filter) {// throws IOException {
        list.removeAllElements();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
        Node signals = prj.returnFirstFinedNode(root, "Globals");
        ArrayList<Node> sigList = prj.getHeirNode(signals);
        //создание списка исключенных сигналов - других абонентов и других экземпляров
        int posSubAb = 5;
        String abOrSubAb = abonent;
        ArrayList<String> exList = new ArrayList<>();
        if (abList != null) {
            for (String[] s : abList) {//Перебираем абонентов
                if ("".equals(s[posSubAb])) {//если у абонента нет экземпляров
                    if (!s[1].equals(abonent)) {
                        exList.add(s[1]);//и абонент не тот для которого мы формируем архивы - добавляем его в список исключений
                    }
                } else {//если экземпляры есть
                    String firstSubAb; //готовим строку для поиска первого экземпляра
                    int x = s[posSubAb].indexOf(","); //ищем позиуию разделителя экземпляров
                    if (x < 0) {
                        firstSubAb = s[posSubAb].trim(); //если её нет - значит экземпляр 1, он же - первый
                    } else {
                        firstSubAb = s[posSubAb].substring(0, x).trim(); //если есть ещё экземпляры - запоминаем первый
                        x++;
                        int y = s[posSubAb].indexOf(",", x); //ищем ещё разделителя
                        while (y > 0) { //пока они есть
                            exList.add(s[posSubAb].substring(x, y).trim()); //заносим экземпляры в список исключений
                            x = y + 1;
                            y = s[posSubAb].indexOf(",", x);
                        }
                        exList.add(s[posSubAb].substring(x).trim()); //заносим в список исключений последний экземпляр
                    }
                    if (!s[1].equals(abonent)) {
                        exList.add(firstSubAb);//если абонент не тот - добавляем в список исключений и первый экземпляр
                    } else {
                        abOrSubAb = firstSubAb;
                    }
                }
            }
        }
        //----------------------------------------------------------------------------
        for (Node n : sigList) {
            String sigName = prj.getDataAttr(n, "Name");
            if (!StrTools.isFilter(sigName, filter)) {
                continue;
            }
            boolean ins;
            if (abList != null) {
                int x = sigName.indexOf("_");
                String sigAb = null;
                if (x > 0) {
                    sigAb = sigName.substring(0, x);
                }
                if (sigAb != null && sigAb.equals(abOrSubAb)) {
                    ins = true;
                } else if (commonSig) {
                    if (sigAb == null) {
                        ins = true;
                    } else {
                        ins = true;
                        for (String s : exList) {
                            if (sigAb.equals(s)) {
                                ins = false;
                                break;
                            }
                        }
                    }
                } else {
                    ins = false;
                }
            } else {
                ins = true;
            }

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

    public static boolean isInList(String s, ArrayList<String[]> list, int i) {
        if (s == null || list == null) {
            return false;
        }
        for (String[] a : list) {
            if (a[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> openSig(String glibSigName) {// throws IOException {
        ArrayList<String> list = new ArrayList<>();
        XMLSAX prj = new XMLSAX();
        Node root = prj.readDocument(globVar.desDir + File.separator + "Design" + File.separator + "Project.prj");
        Node mySig = prj.findNodeAtribute(root, new String[]{"Signal", "Name", glibSigName});
        String type = prj.getDataAttr(mySig, "Type");
        if ("REAL".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type) || "BOOL".equalsIgnoreCase(type) || "WORD".equalsIgnoreCase(type)) {
            return list;
        }

        String fileName = FileManager.FindFile(globVar.desDir + File.separator + "Design", type, "UUID=");
        XMLSAX sigSax = new XMLSAX();
        Node rootSig = sigSax.readDocument(globVar.desDir + File.separator + "Design" + File.separator + fileName);
        Node signals = sigSax.returnFirstFinedNode(rootSig, "Fields");
        ArrayList<Node> sigList = sigSax.getHeirNode(signals);
        if (sigList == null || sigList.isEmpty()) {
            return list;
        }
        sigList.forEach(n -> {
            list.add("– " + glibSigName + "." + sigSax.getDataAttr(n, "Name"));
        });
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
     //----задаем столбцам размеры
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
                colsWidth[i] = cols[i].length();//длина первого столбца
            } else {
                colsWidth[i] = 0;
            }
            for (int j = 0; j < tableModel.getRowCount(); j++) {//цикл по всем строкам данной таблицы в БД(исключаем строку шапку)
                if (tableModel.getValueAt(j, i) != null) {
                    int l = tableModel.getValueAt(j, i).length();
                    if (colsWidth[i] < l) {//задаем размер столбца по максимальной длине значение одной из строк
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
         //   FrameCreate.SinglStrEdit sse = new SinglStrEdit(tableModel, tableName, jTable1.getName(), listJF);
            FrameCreate.SinglStrEdit sse = new SinglStrEdit(tableModel, tableName, jTable1.getName());
            sse.setVisible(true);
            listJF.add(sse);
            sse.setFields(row);
        }
    }

    public static boolean isTableDiffDB(ArrayList<String[]> t1, String dbTableName) {
        return isArrayListsDiff(t1, globVar.DB.getData(dbTableName));
    }

    public static boolean isArrayListsDiff(ArrayList<String[]> t1, ArrayList<String[]> t2) {
        if (t1 == null && t2 == null) {
            return false;
        }
        if (t1 == null || t2 == null) {
            return true;
        }
        int sizeY = t1.size();
        if (sizeY != t2.size()) {
            return true;
        }
        if (sizeY == 0) {
            return false;
        }
        for (int i = 0; i < sizeY; i++) {
            int sizeX = t1.get(i).length;
            if (sizeX != t2.get(i).length) {
                return true;
            }
            for (int j = 0; j < sizeX; j++) {
                //System.out.println("t1="+t1.get(i)[j]+", t2="+t2.get(i)[j]);
                if (!t1.get(i)[j].equals(t2.get(i)[j])) {
                    return true;
                }
            }
        }
        //System.out.println("return false");
        return false;
    }

//    public static void resetID(ArrayList<String[]> t1){
//        if(t1==null) return;
//        for(int i=0; i < t1.size(); i++) t1.get(i)[0] = "" + (i+1);
//    }
    public static void sotrList(ArrayList<String[]> t1, int k) {//ArrayList<String[]>
        if (t1 == null || t1.size() < 2) {
            return;
        }
        for (int i = 1; i < t1.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (t1.get(i)[k].compareTo(t1.get(j)[k]) < 0) {
                    t1.add(j, t1.get(i));
                    t1.remove(i + 1);
                    break;
                }
            }
        }
        for (int i = 0; i < t1.size(); i++) {
            t1.get(i)[0] = "" + (i + 1);
        }
    }

    // --- Перерисовка панели с таблицой ---
    public static void repaintJpanelTable(JPanel jPanel_Tmp, JComponent componetSwing) {
        jPanel_Tmp.removeAll();
        jPanel_Tmp.invalidate();
        jPanel_Tmp.repaint();
        jPanel_Tmp.setLayout(new java.awt.CardLayout()); // без этого не сработало
        jPanel_Tmp.add(componetSwing); // так и остальное заработало
        jPanel_Tmp.revalidate();
        jPanel_Tmp.repaint();
        jPanel_Tmp.requestFocus();
    }

    public DefaultComboBoxModel getComboBoxModelAbonent() {
        String[] aList;//список абонентов окна
        if (!globVar.DB.isConnectOK()) {
            return null;
        }
        ArrayList<String[]> listAbonent = globVar.DB.getAbonentArray();
        aList = new String[listAbonent.size()];

        for (int i = 0; i < listAbonent.size(); i++) {
            String[] str = listAbonent.get(i);
            aList[i] = str[1];
        }

        return new DefaultComboBoxModel(aList);
    }

    public DefaultComboBoxModel getComboBoxModelTable() {//----метод формирования комбобокса где AI AO DI  и тд
        if (!globVar.DB.isConnectOK()) {
            return null;
        }
        int counter = 0;
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);
        String[] nodeTable = new String[nList.size()];
        for (Node n : nList) {
            String nodeName = globVar.sax.getDataAttr(n, "excelSheetName");

            nodeTable[counter] = nodeName;
            counter++;
        }

        return new DefaultComboBoxModel(nodeTable);

    }

    //-----возвращает список строк таблицы ,в которых были изменения
    public static ArrayList<String[]> getCngRows(ArrayList<String[]> listTable, ArrayList<String[]> chgLTable, Boolean bolean) {
        LoggerInterface loggerFile = new LoggerFile();
        ArrayList<String[]> cngLine = new ArrayList<>();
        Boolean result;
        if (listTable.size() != chgLTable.size()) {
            loggerFile.writeLog("Размеры таблиц не совпадают");
        }
        for (int i = 0; i < listTable.size(); i++) {
            result = Arrays.equals(listTable.get(i), chgLTable.get(i));
            if (!result) {
                if (bolean) {
                    cngLine.add(chgLTable.get(i));

                } else {
                    cngLine.add(listTable.get(i));
                }
            }
        }

        return cngLine;
    }

}
