package FrameCreate;

import Generators.Generator;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.MyTableModel;
import Tools.SaveFrameData;
import Tools.TableTools;
import Tools.Tools;
import Tools.isCange;
import globalData.globVar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class TableDB extends javax.swing.JFrame {
    MyTableModel tableModel = new MyTableModel();
    JPopupMenu popupMenu = new JPopupMenu();
    public boolean isChang = false;
    String tableName;
    int tableSize;
    String[] cols;
    String comment;
    ArrayList<String[]> fromDB;

    public TableDB(String table) {
        tableName = table;
        if(globVar.DB==null)return;
        List<String> listColumn = globVar.DB.getListColumns(table);
        if(listColumn==null || listColumn.isEmpty())return;
        cols = listColumn.toArray( new String[listColumn.size()]);
        tableModel.setColumnIdentifiers(cols);
        
        fromDB = globVar.DB.getData(table);
        fromDB.forEach((rowData) -> tableModel.addRow(rowData));
        comment = globVar.DB.getCommentTable(table);
        this.setTitle(table + ": "+comment);
        tableSize = fromDB.size();
 
        initComponents();
        
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel);
        int qCol = listColumn.size();
        int[] align = new int[qCol];
        int[] colsWidth = new int[qCol];
        
        TableTools.setWidthCols(cols, fromDB, colsWidth, 7);
        if(tableSize>0) TableTools.setAlignCols(fromDB.get(0), align);
        TableTools.setTableSetting(jTable1, colsWidth, align, 20);
        
        TableTools.setColsEditor(table, cols, fromDB, jTable1);
        
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            TableTools.saveTableInDB(jTable1, globVar.DB, tableName, cols, comment, fromDB); //сохранение в БД таблицы
        };
        isCange ich = ()->{
            return compareTable(fromDB,tableModel);
        };
        TableTools.setTableListener(this, sfd, ich);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText(".type");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Сохранить");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(" HW");
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("HMI");
        jButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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

        jButton6.setText("Удалить таблицу из БД");
        jButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton6)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //Создание файлов типа
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
                ret = Generator.GenTypeFile(this, jProgressBar1);
            } catch (IOException ex) {
                //Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
            else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            globVar.processReg.remove(processName);
            jProgressBar1.setValue(0);
         };
        BackgroundThread bt = new BackgroundThread("Генерация .type", di);
        bt.start();
        globVar.processReg.add(processName);

    }//GEN-LAST:event_jButton1ActionPerformed
    //Конфигурирование хардваре
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
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
            else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            globVar.processReg.remove(processName);
            jProgressBar1.setValue(0);
         };
        BackgroundThread bt = new BackgroundThread("Генерация HW", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton3ActionPerformed
    //Создание файлов для импорта в ЧМИ
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            if(!Tools.isDesDir()) return;
            String retHMI = null;
            try {
                retHMI = Generator.GenHMI(this, jProgressBar1);
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
    }//GEN-LAST:event_jButton4ActionPerformed
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
            else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
            jProgressBar1.setValue(0);
            globVar.processReg.remove(processName);
        };
        BackgroundThread bt = new BackgroundThread("Генерация ST", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        TableTools.saveTableInDB(jTable1, globVar.DB, tableName, cols, comment, fromDB); //сохранение в БД таблицы
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Object[] options = {"Да", "Нет"};
        if(1 == JOptionPane.showOptionDialog(null, "Удалить таблицу \""+tableName+"\" из БД?",
                "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])
        ) return;
        globVar.DB.createDelTable(tableName);
        globVar.processReg.remove(this.getTitle());
        this.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private boolean  compareTable(ArrayList<String[]> fromDB, DefaultTableModel tableModel) {
        int x = tableModel.getColumnCount();
        int y = tableModel.getRowCount();
        if(fromDB.size() != y || fromDB.get(0).length != x) return true;
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
}
