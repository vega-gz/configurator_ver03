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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButton2.setText("Сохранить");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton10.setText(".type");
        jButton10.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton1.setText("HW");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("HMI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText(" ST ");
        jButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("без разервов");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton7.setText("изм.комм");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Переименовать");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton6.setText("Удалить таблицу из БД");
        jButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton9.setText("Замена имен");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton2)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1)
                .addComponent(jButton3)
                .addComponent(jButton5)
                .addComponent(jCheckBox1)
                .addComponent(jButton7)
                .addComponent(jButton8))
            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton6)
                .addComponent(jButton9))
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1396, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 782, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

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

    // --- Кнопка смены TAGname ---
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JFrame changerTagNamed = new ChangerTagNamed(this);
        changerTagNamed.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changerTagNamed.setVisible(true);

        //new ChangerTagNamed(this).setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JFrame changeComm = new TextEdit("Редактор названия таблицы", this, true);
        changeComm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changeComm.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        JFrame changeComm = new TextEdit("Редактор комментария", this, false);
        changeComm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changeComm.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Object[] options = {"Да", "Нет"};
        if(1 == JOptionPane.showOptionDialog(null, "Удалить таблицу \""+tableName+"\" из БД?",
            "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])
        ) return;
        globVar.DB.dropTableWithBackUp(tableName);
        jTree1.setModel(Main_JPanel.getModelTreeNZ());// обновить дерево

        globVar.processReg.remove(this.getTitle());
        this.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

    }//GEN-LAST:event_jCheckBox1ActionPerformed

   //Создание файлов для импорта в Алгоритм
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
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
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        TableTools.saveTableInDB(jTable1, globVar.DB, tableName, cols, comment, fromDB); //сохранение в БД таблицы
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
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
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
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
    }//GEN-LAST:event_jButton3ActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
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
