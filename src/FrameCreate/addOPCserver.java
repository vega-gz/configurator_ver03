package FrameCreate;

import DataBaseTools.DataBase;
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
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
//import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/*@author Lev*/
public class addOPCserver extends javax.swing.JFrame {
    DefaultListModel list1 = new DefaultListModel();
    DefaultListModel list2 = new DefaultListModel();
    //String abonent = globVar.abonent;
    int prevArch = 0;
    final String[] nodeIDitems = {"Number","Name"};
    final String[] nodeIDtypeItems = {"Numeric","String"};
    final String[] opcTabCols = {"tagName","nameSpace"};
    ArrayList<String[]> opcList;
    ArrayList<String> plusList = new ArrayList<>();
    boolean isChang = false;
    int opcServName;
    int nameSpace;
    int nodeID;
    int nodeIDtype;
    String comment;

    private void setServSettings(String s){
        if(s==null) return;
        int x = s.indexOf("nameSpace:");
        x += 10;
        int y = s.indexOf(";",x);
        nameSpace = Integer.parseInt(s.substring(x,y));
        x = s.indexOf("nodeID:");
        x += 7;
        y = s.indexOf(";",x);
        nodeID = Tools.indexOfArray(nodeIDitems, s.substring(x,y));
        x = s.indexOf("nodeIDtype:");
        x += 11;
        y = s.indexOf(";",x);
        nodeIDtype = Tools.indexOfArray(nodeIDtypeItems, s.substring(x,y));
    }
    
    public addOPCserver() {
        if(globVar.DB==null)return;
        
        initComponents();
        
        jComboBox1.setModel(new DefaultComboBoxModel(nodeIDitems));//ID
        jComboBox2.setModel(new DefaultComboBoxModel(nodeIDtypeItems));//ID type
        
        ArrayList<String> tableList = globVar.DB.getListTable("opc");
        if(tableList==null){
            comment = "nameSpace:1; nodeID:number; nodeIDtype:Numeric";
            globVar.DB.createTableEasy("opcServer1", opcTabCols, comment);
            jComboBox4.setModel(new DefaultComboBoxModel(new String[]{"OPC_Server1"}));
            jComboBox3.setModel(new DefaultComboBoxModel(new String[]{"1","2"}));//namespace
            opcServName = 0;
            nameSpace = 0;
            nodeID = 0;
            nodeIDtype = 0;
        }else{
            jComboBox4.setModel(new DefaultComboBoxModel(tableList.toArray(new String[tableList.size()])));
            opcServName = 0;
            comment = globVar.DB.getCommentTable(tableList.get(0));
            setServSettings(comment);
        }
        
        
        opcList = globVar.DB.getData(jComboBox4.getItemAt(opcServName));
        
        TableTools.setArchiveSignalList(list2, opcList, 0);
        Tools.setPlusList(opcList, plusList);
        try {
            TableTools.setSignalList(list1, null, null, false, opcList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addOPCserver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jList1.setModel(list1);
        jList2.setModel(list2);
        //Лямбда для определения изменений
        isCange ich = ()->{return isChang;};
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            resetArchList();        //сохраняем изменения списака предназначенных к архивированию сигналов
            TableTools.saveListInDB(opcList, globVar.DB, jComboBox4.getItemAt(opcServName), opcTabCols, comment);//сохранение в БД списка сигналов 
        };
        TableTools.setTableListener(this, sfd, ich, null);
   }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton6 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jButton1.setText("Приложение");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jProgressBar1.setToolTipText("");

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
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 221, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel1.setText("ОПС сервер");

        jLabel2.setText("Namespace");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel3.setText("NodeID");

        jLabel4.setText("NodeID type");

        jComboBox4.setEditable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(jComboBox4, 0, 1, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(2, 2, 2)
                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton6))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //Кнопка ">"
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int[] x = jList1.getSelectedIndices();
        int offset = 0;
        for(int i: x){
            String s = list1.get(i-offset).toString();
            if(Tools.isPlusInList2(opcList, s)){
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane.showOptionDialog(null, "<HTML><BODY>Структура \""+s+"\" уже частично сконфигурирована для<br/>"+
                                                           "архивирования. Удалить её элементы из других архивов и включить целиком в этот?</BODY></HTML>",
                                "Вопрос", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if(n==0){
                    Tools.delPlusFromArch(opcList, list2, s);
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

    private void jTable1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable1ComponentResized
    }//GEN-LAST:event_jTable1ComponentResized

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
    }//GEN-LAST:event_jTable1MousePressed
    //Кнопка "Сохранить"
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
            resetArchList();        //сохраняем изменения списака предназначенных к архивированию сигналов
            TableTools.saveListInDB(opcList, globVar.DB, jComboBox4.getItemAt(opcServName), opcTabCols, comment);//сохранение в БД списка сигналов 
        isChang = false;
    }//GEN-LAST:event_jButton6ActionPerformed
    //Кнопка "+"
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        plusList.add(jList1.getSelectedValue());
        try {
            TableTools.setSignalList(list1, null, null, false, opcList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addOPCserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed
    //Кнопка "-"
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String s = jList1.getSelectedValue();
        int x = s.indexOf(".");
        if(x>0) plusList.remove(s = s.substring(2, x));
        try {
            TableTools.setSignalList(list1, null, null, false, opcList, plusList);
        } catch (IOException ex) {
            Logger.getLogger(addOPCserver.class.getName()).log(Level.SEVERE, null, ex);
        }
        x = list1.indexOf(s);
        if(x>=0) jList1.setSelectedIndex(x);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        int x = jComboBox1.getSelectedIndex();//сохранем выбранный пункт типа архива
        resetArchList();
        prevArch = x;
        list2.removeAllElements();
        TableTools.setArchiveSignalList(list2, opcList, x);
    }//GEN-LAST:event_jComboBox1ActionPerformed
    //Кнопка "Приложение"
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int i = -1;
        //try{
            String processName = "Генерация из таблицы";
            if(globVar.processReg.indexOf(processName)>=0){
                JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
                return;
            }
            DoIt di = () -> {
                int ret = -1;
                try {
                    ret = Generator.GenOPC(null, null, null, jProgressBar1); // вызываем функцию генерации
                } catch (IOException ex) {
                    Logger.getLogger(addOPCserver.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
                else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
                
                globVar.processReg.remove(processName);
                jProgressBar1.setValue(0);
             };
            BackgroundThread bt = new BackgroundThread("Генерация OPC", di);
            bt.start();
            globVar.processReg.add(processName);
        //}catch(NumberFormatException e){
        //    JOptionPane.showMessageDialog(null, "Неправильно сконфигурирован архив № " + (i+1));
        //}        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void resetArchList(){
        for(int i=0;i<opcList.size(); i++){    //пробегаем поо списту предназнгаченных к архивированию сигналов
            if(opcList.get(i)[1].equals(""+prevArch)){ // если сигнал имеет номер нужный архива
                opcList.remove(i);                     // удаляем его
                i--;
            }
        }
        for(int i=0; i<list2.size(); i++) opcList.add(new String[]{(String)list2.get(i),""+prevArch}); //заносим в списов все сигналы из текужего списка второго листа        
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
            java.util.logging.Logger.getLogger(addOPCserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addOPCserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addOPCserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addOPCserver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addOPCserver().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
