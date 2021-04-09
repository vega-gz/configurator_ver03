/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import Main.Main_JPanel;
import Tools.TableTools;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;

/**
 *
 * @author cherepanov
 */
public class CreateFTable extends javax.swing.JFrame {

    XMLSAX xmlsax = new XMLSAX();
    TableTools tt = new TableTools();
    String typeTable;//тип таблицы AI AO DI итд
    String commentTable;
    String prefixTable;//префикс таблицы (вводится руками)
    String nameTable;//послное имя таблицы
    public String[] tableCols;
    javax.swing.JTree jTree1;
    ArrayList<String> nColList = new ArrayList<>();//ArrayList имен столбцов Exel из ConfigSugnals
    String[] continueArchiv;
    String[] aList;//список абонентов окна

    /**
     * Creates new form CreateTable
     */
    public CreateFTable(javax.swing.JTree jTree) {
        jTree1 = jTree;
        initComponents();
        jComboBox1.setModel(tt.getComboBoxModelTable());
        jComboBox2.setModel(tt.getComboBoxModelAbonent());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Создать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Префикс");

        jLabel2.setText("Шаблон типа таблицы");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Тип таблицы");

        jLabel4.setText("Абоненты");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Комментарий к таблице");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, 99, Short.MAX_VALUE)
                            .addComponent(jTextField2)
                            .addComponent(jTextField1)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        typeTable = jTextField2.getText();//тип таблицы AI AO (если хочешь создать новую)
        prefixTable = jTextField1.getText();
        commentTable = jTextField3.getText();
        String abonentTable = (String) jComboBox2.getSelectedItem();
        String[] tableColumns;
        if (jTextField2.getText().equals("")) {//если поле "тип таблицы" пустое
            typeTable = (String) jComboBox1.getSelectedItem();
            if (jTextField1.getText().equals("")) {
                nameTable = abonentTable + "_" + typeTable;
            } else {
                nameTable = abonentTable + "_" + prefixTable + "_" + typeTable;
            }
        }
        else{//если пользователь решил создать новую таблицу,запускаем метод записи новой ноды и создания таблицы
            if (jTextField1.getText().equals("")) {//продублировал два раза,надо бы исправить,но пока ничего не придумывается,работает корректно
                nameTable = abonentTable + "_" + typeTable;
            } else {
                nameTable = abonentTable + "_" + prefixTable + "_" + typeTable;
            }
            createTableNode(nameTable,commentTable);
        }

        System.out.println(nameTable);
        tableColumns = getExcelChild();
        continueArchiv = new String[tableColumns.length];
        Arrays.fill(continueArchiv, "");//заполнение массива пустыми строками
        //---В процесс до этойчерты мы получаем значения из фрейма,все что далее будет относиться к построению новой таблицы
        globVar.DB.createTableEasy(nameTable, tableColumns, commentTable);//---------создаем таблицу
        globVar.DB.insertRow(nameTable, continueArchiv, tableColumns, 0);
        //(добавить условия)
        JOptionPane.showMessageDialog(null, "Таблица создана");
        jTree1.setModel(Main_JPanel.getModelTreeNZ());//обновление таблицы
    }//GEN-LAST:event_jButton1ActionPerformed

    public String[] getExcelChild() {//метод получения столбцов из ConfigSignals
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);//получили детей ноды ConfigSignals
        ArrayList<Node> eList;
        for (int i = 0; i < nList.size(); i++) {
            if (nList.get(i).getNodeName().equals(typeTable)) {
                Node excel = xmlsax.returnFirstFinedNode(nList.get(i), "EXEL");//нашли ноду
                eList = xmlsax.getHeirNode(excel);
                for (int j = 0; j < eList.size(); j++) {//получили детей ноды excel ,в которых хранятся наименования столбцов
                    String nCol = xmlsax.getDataAttr(eList.get(j), "nameColumnPos");
                    nColList.add(nCol);
                }
            }
        }
        tableCols = nColList.toArray(new String[nColList.size()]);

        return tableCols;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateFTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateFTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateFTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateFTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }
//получение комбо бокс таблиц

    public void createTableNode(String nameNode,String comment) {
//        XMLSAX sax=new XMLSAX();
//        String[] str={nameNode,"excelSheetName",nameNode,"Comment",comment};
//       // Node node= sax.readDocument(globVar.sax.toString());
//        Node node= sax.readDocument("C:\\Users\\cherepanov\\Documents\\NetBeansProjects\\new_config\\configurator_ver03\\ConfigSignals.xml");
//        Node configSignals = sax.returnFirstFinedNode(node,"ConfigSignals");
//        sax.insertChildNode(configSignals, str);
//        sax.writeDocument();
//        System.out.println("");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
