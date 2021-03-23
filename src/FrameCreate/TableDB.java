package FrameCreate;

import Generators.Generator;
import Main.Main_JPanel;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.FileManager;
import Tools.MyTableModel;
import Tools.RegistrationJFrame;
import Tools.SaveFrameData;
import Tools.TableTools;
import Tools.Tools;
import Tools.closeJFrame;
import Tools.isCange;
import globalData.globVar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class TableDB extends javax.swing.JFrame {
    TableTools tt=new TableTools();
    ArrayList<String[]> newName=new ArrayList<>();
    public MyTableModel tableModel = new MyTableModel();
    JPopupMenu popupMenu = new JPopupMenu();
    public boolean isChang = false;
    String tableName;
    public int tableSize;
    String[] cols;
    String comment;
    ArrayList<String[]> fromDB;
    ArrayList<String[]> listItemList = new ArrayList<>();
    public int[] colsWidth;
    int[] align;
    int qCol;
    ArrayList<JFrame> listJF = new ArrayList();
    javax.swing.JTree jTree1;
    ArrayList<Integer> findDataRows = new ArrayList<>(); // номера строк в таблице по совпадениям
    boolean firstClickMouseFind = true;                        //  ткнули первый раз на поиск
    int idArrayFindigData = 0;

    public TableDB(javax.swing.JTree jTree, String table) {
        
        tableName = table;
        jTree1 = jTree;
        if(!globVar.DB.isConnectOK())return;
        List<String> listColumn = globVar.DB.getListColumns(table);
        if(listColumn==null || listColumn.isEmpty())return;
        cols = listColumn.toArray( new String[listColumn.size()]);
        tableModel.setColumnIdentifiers(cols);
        
        
        fromDB = globVar.DB.getData(table);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        comment = globVar.DB.getCommentTable(table);
        tableSize = fromDB.size();
        qCol = listColumn.size();
        align = new int[qCol];
        colsWidth = new int[qCol];
        
        TableTools.setWidthCols(cols, tableModel, colsWidth, 7.8);
        if(tableSize>0) TableTools.setAlignCols(fromDB.get(0), align);
 
        initComponents();
        
        
        
        RegistrationJFrame rgf = (JFrame jf) ->{ listJF.add(jf); };
        closeJFrame cjf = ()->{ for(JFrame jf: listJF) jf.setVisible(false);};
        
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel, table, rgf, listJF);
        TableTools.setTableSetting(jTable1, colsWidth, align, 20); // вот тут броблема фокуса
        
        TableTools.setColsEditor(table, cols, fromDB, jTable1, listItemList);
        
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            TableTools.saveTableInDB(jTable1, globVar.DB, tableName, cols, comment, fromDB); //сохранение в БД таблицы
        };
        isCange ich = ()->{
            return compareTable(fromDB,tableModel);
        };
        TableTools.setFrameListener(this, sfd, ich, cjf);
        
        this.setTitle(table + ": "+comment);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jProgressBar1 = new javax.swing.JProgressBar();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCheckBox1.setText("без разервов");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTextField1.setText("Поиск");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //System.out.println("jTextField1_add " + e);
                compareFielTextToDataCell(jTextField1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //System.out.println("jTextField1_del " + e);
                compareFielTextToDataCell(jTextField1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //System.out.println("jTextField1_change " + e);
                compareFielTextToDataCell(jTextField1.getText());
            }
        });

        jTable1.setModel(tableModel);
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1396, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                        .addGap(715, 715, 715))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBox1))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 781, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jMenu2.setText("Опции");

        jMenuItem6.setText("Сохранить изменения в таблице");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem8.setText("Переименовать таблицу");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Изменить комментарий к таблице");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Замена имен в файлах через базу данных");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem7.setText("Удалить таблицу из базы данных");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Генерация");

        jMenuItem3.setText("Type ");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("ST");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("HW");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem2.setText("HMI");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        //JOptionPane.showMessageDialog(null, "Key: " + evt.getKeyChar() + ", row: " + tableModel.getRowCount());
    }//GEN-LAST:event_jTable1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        jTable1.setColumnSelectionAllowed(true);
        if (evt.getClickCount() == 2) {
            int row = jTable1.getSelectedRow();
            SinglStrEdit sse = new SinglStrEdit(tableModel, tableName, listJF);
            sse.setVisible(true);
            listJF.add(sse);
            sse.setFields(row);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        //        evt.getComponent().addKeyListener(new java.awt.event.KeyAdapter() {
            //            public void keyReleased(java.awt.event.KeyEvent e) {
                //                System.out.println("Key: " + e.getKeyChar());
                //            }
            //        });
    ////        int i = jTable1.getSelectedRow();
    ////        int j = jTable1.getSelectedColumn();
    ////        jTable1.getComponentAt(j, i).addKeyListener(new java.awt.event.KeyAdapter() {
        ////            public void keyReleased(java.awt.event.KeyEvent e) {
            ////                System.out.println("Key: " + e.getKeyChar());
            ////            }
        ////        });
        //        System.out.println("Component: " + evt.getComponent());
    }//GEN-LAST:event_jTable1FocusLost

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
        //        int i = jTable1.getSelectedRow();
        //        int j = jTable1.getSelectedColumn();
        //        jTable1.getComponentAt(j, i).addKeyListener(new java.awt.event.KeyAdapter() {
            //            public void keyReleased(java.awt.event.KeyEvent e) {
                //                JOptionPane.showMessageDialog(null, "Key: " + e.getKeyChar());
                //            }
            //        });
    //JOptionPane.showMessageDialog(null, "Component: " + jTable1.getComponentAt(j, i) + ", row: " + i + ", col: " + j);
    }//GEN-LAST:event_jTable1FocusGained

    // --- переключение по найденным совпадениям ---
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        if(idArrayFindigData >= findDataRows.size() - 1){
            idArrayFindigData = 0;
        }
        else{
            ++idArrayFindigData;
        }
        if(findDataRows.size() > 0)selectRowInTable(findDataRows.get(idArrayFindigData));
        
        //System.out.println(jTable1.getSelectedRows());
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        if(firstClickMouseFind){
            firstClickMouseFind = false;
            jTextField1.setText(""); // затереть текст при первом наведении мыши
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
          if(!Tools.isDesDir()) return;
        String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            if(!Tools.isDesDir()) return;
            int ret = 1;
            try {
                ret = Generator.genTypeFile(this, jProgressBar1);
            } catch (IOException ex) {
                FileManager.loggerConstructor(ex.toString());
            }
            if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
            else if(ret != -2) JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            globVar.processReg.remove(processName);
            jProgressBar1.setValue(0);
         };
        BackgroundThread bt = new BackgroundThread("Генерация .type", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
         String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            int ret = 0;
            try {
                ret= Generator.genSTcode(this, jCheckBox1.isSelected(), jProgressBar1);
            } catch (IOException ex) {
                //Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
            else if(ret != -2) JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            jProgressBar1.setValue(0);
            globVar.processReg.remove(processName);
        };
        BackgroundThread bt = new BackgroundThread("Генерация ST", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
       if(!Tools.isDesDir()) return;
        String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            int retHW = 0;
            try {
                retHW = Generator.genHW(this, jProgressBar1);
            } catch (IOException ex) {
                //Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(retHW == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
            else if(retHW != -2) JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            globVar.processReg.remove(processName);
            jProgressBar1.setValue(0);
         };
        BackgroundThread bt = new BackgroundThread("Генерация HW", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            if(!Tools.isDesDir()) return;
            String retHMI = null;
            try {
                retHMI = Generator.genHMI(this, jProgressBar1);
            } catch (IOException ex) {
                //Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(retHMI==null) JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            else if(!"".equals(retHMI)) JOptionPane.showMessageDialog(null, "Создано "+retHMI); // Это сообщение 
            jProgressBar1.setValue(0);
            globVar.processReg.remove(processName);
         };
        BackgroundThread bt = new BackgroundThread("Генерация HMI", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
      TableTools.saveTableInDB(jTable1, globVar.DB, tableName, cols, comment, fromDB); //сохранение в БД таблицы
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
       JFrame changeComm = new TextEdit("Редактор названия таблицы", this, true);
        changeComm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changeComm.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        JFrame changeComm = new TextEdit("Редактор комментария", this, false);
        changeComm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changeComm.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
       Object[] options = {"Да", "Нет"};
        if(1 == JOptionPane.showOptionDialog(null, "Удалить таблицу \""+tableName+"\" из БД?",
            "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])
        ) return;
        globVar.DB.dropTableWithBackUp(tableName);
        jTree1.setModel(Main_JPanel.getModelTreeNZ());// обновить дерево

        globVar.processReg.remove(this.getTitle());
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        JFrame changerTagNamed = new ChangerTagNamed(this);
        changerTagNamed.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changerTagNamed.setVisible(true);

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    boolean  compareTable(ArrayList<String[]> fromDB, DefaultTableModel tableModel) {
        int x = tableModel.getColumnCount();
        int y = tableModel.getRowCount();
        
        if(fromDB.size() <= 0 || fromDB.size() != y || fromDB.get(0).length != x) return true; // еще доп проверка от пустых строк от базы
        for(int i=0;i<y;i++)
            for(int j=0;j<x;j++)
                if(!fromDB.get(i)[j].equals(tableModel.getValueAt(i, j))) return true;
        return false;
    }

    public String tableName() {
        return tableName;
    }

    public int tableSize() {
        return tableSize;
    }
    
    public String getCell(String colName, int i){
        int j = getNumberCol(colName);
        if(j<0) return null;
        return (String) tableModel.getValueAt(i, j);
    }
    
    int getNumberCol(String colName){
        for(int i=0; i < cols.length; i++) if(cols[i].equals(colName)) return i;
        return -1;
    }
    
    // --- Поиск с символов в поле --
    void compareFielTextToDataCell(String str) {
        
        if (str != null & !str.equals("")) {
            Pattern pattern1 = Pattern.compile("^(.*" + str + ").*$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            findDataRows.clear();
    
            for (int i = 0; i < tableModel.getColumnCount(); i++) { // пробежим столбцам
                for (int j = 0; j < globVar.namecolumnT.length; j++) { // какие название столбцов сравнивать
                    if(tableModel.getColumnName(i).equalsIgnoreCase(globVar.namecolumnT[j])){ 
                        for (int k = 0; k < tableModel.getRowCount(); k++) {    // нашли столбец бежим по строкам уже
                            String dataT = tableModel.getValueAt(k, i);                        // порядок строка - колонка
                            Matcher matcher1 = pattern1.matcher(dataT);
                            if (matcher1.matches()) {
                                findDataRows.add(k);
                            }
                        }
                    }
                } 
            }
            if(findDataRows.size() > 0) selectRowInTable(findDataRows.get(idArrayFindigData));
        }
    }
    
    // --- выделяет строку в таблице с заданым номером ---
    private void selectRowInTable(int row){
        jTable1.setColumnSelectionAllowed(false); // отключение по столбцам выделение
        ListSelectionModel selModel = jTable1.getSelectionModel();
        selModel.clearSelection();
        selModel.addSelectionInterval(row, row);
        jTable1.scrollRectToVisible(jTable1.getCellRect(row,0, true));  // скролю на нужное
    
    }
}
