package FrameCreate;

import DataBaseTools.DataBase;
import Generators.Generator;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.MyTableModel;
import Tools.SaveFrameData;
import Tools.TableTools;
import Tools.isCange;
import globalData.globVar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/*@author Lev*/
public class addArchive extends javax.swing.JFrame {
    DefaultListModel list1 = new DefaultListModel();
    DefaultListModel list2 = new DefaultListModel();
    MyTableModel tableModel = new MyTableModel();
    JPopupMenu popupMenu = new JPopupMenu();
    String abonent = globVar.abonent;
    int prevArch = 0;
    ArrayList<String[]> abList;
    ArrayList<String[]> archList;
    ArrayList<String> plusList = new ArrayList<>();
    String[] archTabCols = {"tagName","archType"};
    boolean isChang = false;
    // Данные для таблиц
    private final String[] continueArchiv = new String[] {"0","x100","100","30","31"};
    private final String[] jTableCols = new String[] {  "№","Наименование архива", 
                                                            "<HTML><BODY align=\"center\">Периодичность<br/>[мсек]</BODY></HTML>", 
                                                            "<HTML><BODY align=\"center\">Кэш<br/>[сек]</BODY></HTML>", 
                                                            "<HTML><BODY align=\"center\">Длительность<br/>[дни]</BODY></HTML>"
    };

    public addArchive() {
        if(globVar.DB==null)return;
        ArrayList<String[]> archives;
        if(!globVar.DB.isTable("Archive")){
            globVar.DB.createTableEasy("Archive",  jTableCols, "Конфигурации архивов");
            globVar.DB.insertRow("Archive", continueArchiv, jTableCols,0);
            archives = new ArrayList<>();
            archives.add(continueArchiv);
        }else{
            archives = globVar.DB.getData("Archive");
        }
        
        tableModel.setColumnIdentifiers(jTableCols);
        
        for (int i = 0; i < archives.size(); i++) 
            tableModel.addRow(archives.get(i));
        initComponents();
        
        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel);
        //int qCol = jTableCols.length;
        int[] colWidth = {25,160,100,50,100};//new int[qCol];
        int[] align = {1,0,0,0,0};//new int[qCol];
        
        TableTools.setTableSetting(jTable1, colWidth, align, 40);
        
        String[] archiveTyps = new String[jTable1.getRowCount()];
        for(int i = 0; i < jTable1.getRowCount(); i++){
            archiveTyps[i]= jTable1.getValueAt(i, 1).toString();
        }
        
        archList = globVar.DB.getData("Archive_"+abonent, archTabCols);
        
        jComboBox1.setModel(new DefaultComboBoxModel(archiveTyps));
        jList1.setModel(list1);
        jList2.setModel(list2);
        abList = DataBase.getAbonentArray();
        TableTools.setArchiveSignalList(list2, archList, 0);
        setPlusList();
        try {
            TableTools.setSignalList(list1, abList, abonent, false, archList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            TableTools.saveTableInDB(jTable1, globVar.DB, "Archive", jTableCols, "Конфигурации архивов"); //сохранение в БД настроек архивов
            resetArchList();        //сохраняем изменения списака предназначенных к архивированию сигналов
            TableTools.saveListInDB(archList, globVar.DB, "Archive_"+abonent, archTabCols, "");//сохранение в БД списка сигналов 
        };
        isCange ich = ()->{return isChang;};
        TableTools.setTableListener(this, sfd, ich);
        jSplitPane1.setDividerLocation(100);
   }
    private void setPlusList(){
        for(int i=0; i < archList.size(); i++){
            String s = archList.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && !plusList.contains(s.substring(2,x))) 
                plusList.add(s.substring(2,x));
        }
    }
    private boolean isPlusInList2(String sig){
        for(int i=0; i < archList.size(); i++){
            String s = archList.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) return true;
        }
        return false;
    }
    private void delPlusFromArch(String sig){
        for(int i=0; i < archList.size(); i++){
            String s = archList.get(i)[0];
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) archList.remove(i--);
        }
        for(int i=0; i < list2.size(); i++){
            String s = list2.get(i).toString();
            int x = s.indexOf(".");
            if(x > 0 && sig.equals(s.substring(2,x))) list2.remove(i--);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTable1.setModel(tableModel);
        jTable1.setMinimumSize(new java.awt.Dimension(100, 100));
        jTable1.setOpaque(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane2.setViewportView(jList1);

        jButton2.setText(">");
        jButton2.setAlignmentX(0.5F);
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.setMaximumSize(new java.awt.Dimension(17, 17));
        jButton2.setMinimumSize(new java.awt.Dimension(17, 17));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("<");
        jButton3.setAlignmentX(0.5F);
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jList2);

        jButton7.setText("+");
        jButton7.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("-");
        jButton8.setToolTipText("");
        jButton8.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel1);

        jButton5.setText(">");
        jButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Сохранить");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Общие сигналы");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Приложение");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jProgressBar1.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton1)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //Кнопка ">"
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int[] x = jList1.getSelectedIndices();
        int offset = 0;
        for(int i: x){
            String s = list1.get(i-offset).toString();
            if(isPlusInList2(s)){
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane.showOptionDialog(null, "<HTML><BODY>Структура \""+s+"\" уже частично сконфигурирована для<br/>"+
                                                           "архивирования. Удалить её элементы из других архивов и включить целиком в этот?</BODY></HTML>",
                                "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if(n==0){
                    delPlusFromArch(s);
                    list2.addElement(list1.get(i-offset));
                    list1.remove(i-offset);
                    offset++;
                    isChang = true;
                }
            }else{
                list2.addElement(list1.get(i-offset));
                list1.remove(i-offset);
                offset++;
                isChang = true;
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    //Кнопка "<"
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int[] x = jList2.getSelectedIndices();
        int offset = 0;
        for(int i: x){
            list1.addElement(list2.get(i-offset));
            list2.remove(i-offset);
            offset++;
        }
        isChang = true;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        this.popupMenu.isVisible();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable1ComponentResized
    }//GEN-LAST:event_jTable1ComponentResized

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
    }//GEN-LAST:event_jTable1MousePressed
    //Кнопка "Сохранить"
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        TableTools.saveTableInDB(jTable1, globVar.DB, "Archive", jTableCols, "Конфигурации архивов"); //сохранение в БД настроек архивов
        resetArchList();
        TableTools.saveListInDB(archList, globVar.DB, "Archive_"+abonent, archTabCols, "");//сохранение в БД списка сигналов 
        isChang = false;
    }//GEN-LAST:event_jButton6ActionPerformed
    //Кнопка "+"
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        plusList.add(jList1.getSelectedValue());
        try {
            TableTools.setSignalList(list1, abList, abonent, false, archList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed
    //Кнопка "-"
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String s = jList1.getSelectedValue();
        int x = s.indexOf(".");
        if(x>0) plusList.remove(s = s.substring(2, x));
        try {
            TableTools.setSignalList(list1, abList, abonent, false, archList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
        x = list1.indexOf(s);
        if(x>=0) jList1.setSelectedIndex(x);
    }//GEN-LAST:event_jButton8ActionPerformed
    //Кнопка ">" для выбора типа архива
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int x = prevArch+1;
        if(x>=jComboBox1.getItemCount()) x = 0;
        jComboBox1.setSelectedIndex(x);//сохранем выбранный пункт типа архива
        //list2.removeAllElements();
        //TableTools.setArchiveSignalList(list2, archList, prevArch);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        int x = jComboBox1.getSelectedIndex();//сохранем выбранный пункт типа архива
        resetArchList();
        prevArch = x;
        list2.removeAllElements();
        TableTools.setArchiveSignalList(list2, archList, x);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        try {
            list1.removeAllElements();
            TableTools.setSignalList(list1, abList, abonent, jCheckBox1.isSelected(), archList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    //Кнопка "Приложение"
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int x = tableModel.getRowCount(); // Определяем, сколько у нас видов архивов
        int[][] archTyps = new int[x][4]; // Создаём прямоугольный массив, что бы не таскать с собой всю структуру таблицы
        int i = -1;
        try{
            for(i=0;i<x;i++) {
                archTyps[i][0] = Integer.parseInt(tableModel.getValueAt(i, 0).toString()); //Номер типа архива
                archTyps[i][1] = Integer.parseInt(tableModel.getValueAt(i, 2).toString()); //Периодичность архива
                archTyps[i][2] = Integer.parseInt(tableModel.getValueAt(i, 3).toString()); //Кэш в секундах
                archTyps[i][3] = Integer.parseInt(tableModel.getValueAt(i, 4).toString()); //Глубина в днях
            } // переписываем данные из таблицы в массив
            String processName = "Генерация из таблицы";
            if(globVar.processReg.indexOf(processName)>=0){
                JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
                return;
            }
            DoIt di = () -> {
                int ret = -1;
                try {
                    ret = Generator.GenArchive(archTyps, archList, abonent, jProgressBar1); // вызываем функцию генерации
                } catch (IOException ex) {
                    Logger.getLogger(addArchive.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
                else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
                
                globVar.processReg.remove(processName);
                jProgressBar1.setValue(0);
             };
            BackgroundThread bt = new BackgroundThread("Генерация .type", di);
            bt.start();
            globVar.processReg.add(processName);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Неправильно сконфигурирован архив № " + (i+1));
        }        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void resetArchList(){
        for(int i=0;i<archList.size(); i++){    //пробегаем поо списту предназнгаченных к архивированию сигналов
            if(archList.get(i)[1].equals(""+prevArch)){ // если сигнал имеет номер нужный архива
                archList.remove(i);                     // удаляем его
                i--;
            }
        }
        for(int i=0; i<list2.size(); i++) archList.add(new String[]{(String)list2.get(i),""+prevArch}); //заносим в списов все сигналы из текужего списка второго листа        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addArchive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addArchive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addArchive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addArchive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addArchive().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
