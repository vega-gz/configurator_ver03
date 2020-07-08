package FrameCreate;

import XMLTools.XMLSAX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import DataBaseTools.DataBase;
import Generators.Generator;
import Main.Main_JPanel;
import Tools.Tools;
import globalData.globVar;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FrameTabel extends javax.swing.JPanel {

    String nameTable;
    private ArrayList<String[]> dataFromDb; // данные из таблицы бызы на основе которых строим нашу
    ArrayList<ArrayList> listToTable = new ArrayList<>(); // Лист для передачи в таблицу
    String[] columnstoMass = null; // Массив столбцов для передачи в таблицу
    TableNzVer3 tableFrameModel = null;
    //XMLSAX sax = new XMLSAX();
    int filepath;
    String filepatch;
    DataBase workbase;// = DataBase.getInstance();

    public int tableSize() {//возвращает размер таблицы
        return jTable1.getRowCount();
    }

    public String tableName() {//возвращает имя таблицы
        return nameTable;
    }

    public Object getCell(String colName, int row) {//возвращает содержимое ячейки по имени столбца и номеру строки
        return tableFrameModel.getDataNameColumn(colName, row);
    }

    public FrameTabel(String nameTable) {
        this.nameTable = nameTable;
        //this.tableFrameModel = getTableData();
        initComponents();
    }

    // --- Конструктор с вызовом таблицы TableNzVer2 и преобразованными данными для нее ---
    public FrameTabel(String selectT, ArrayList<String> columns) {
        this.nameTable = selectT;
        this.workbase = globVar.DB;
        this.dataFromDb = globVar.DB.getData(selectT, columns); // получили данные с базы 
        for (String[] mass : dataFromDb) {//преобразовать данные для переваривания таблицей
            ArrayList<String> tmpList = new ArrayList<>();
            for (String s : mass) {
                tmpList.add(s);
            }
            listToTable.add(tmpList);
        }
        this.tableFrameModel = new TableNzVer3(nameTable, columns, listToTable, true);
        initComponents();
    }

     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = tableFrameModel.getJTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(999, 530));

        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Для HMI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(".type");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("просканировать файл type с автозаменой");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("удалить таблицу");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setText("Для HW");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("без разервов");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton6.setText("Для ST");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1011, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(!Tools.isDesDir()) return;
        String retHMI = null;
        try {
            retHMI = Generator.GenHMI(null,null);
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(retHMI==null) JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
        else if(!"".equals(retHMI)) JOptionPane.showMessageDialog(null, "Создано "+retHMI); // Это сообщение 

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(!Tools.isDesDir()) return;
        int ret = 1;
        try {
            ret = Generator.GenTypeFile(null,null);
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
        else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
    }//GEN-LAST:event_jButton2ActionPerformed

        // --- Временная кнопка для преобразования файлов type ---
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
//        int ren = fileopen.showDialog(null, ".type");
//        if (ren == JFileChooser.APPROVE_OPTION) {
//
//            File file = fileopen.getSelectedFile();// выбираем файл из каталога
//            String pathFileType = file.toString();
//            //System.out.println(file.getName());
//            if (pathFileType.endsWith(".type")) {
//                new SignalTypeConvertTagName(pathFileType);
//            } else {
//                JOptionPane.showMessageDialog(null, "Расширение файла не .type"); // Это сообщение
//            }
//        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // --- Кнопка удаления таблицы ---
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         workbase.dropTable(nameTable);
        // преобразование с закрытием
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(!Tools.isDesDir()) return;
        int retHW = 0;
        try {
            retHW = Generator.genHW(null,null);
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(retHW == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
        else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if(!Tools.isDesDir()) return;
        int ret = 0;
        try {
            ret= Generator.genSTcode(null, jCheckBox1.isSelected(),null);
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
        else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}



   

