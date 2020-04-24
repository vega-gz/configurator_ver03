/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import DataBaseConnect.DataBase;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author ad
 */
public class PopMenuDialog extends javax.swing.JFrame {
    DataBase workbase = null; // создаем пустой запрос к базе
    String name_table;
    
    public PopMenuDialog() {
        initComponents();
    }

    
    PopMenuDialog(ArrayList<String> listColumn) {
        initComponents();
        getPanelTable(listColumn);
    }
    
    // --- коструктор с указателем на базу ---
    PopMenuDialog(ArrayList<String> listColumn, DataBase workbase, String name_table) {
        this.workbase = workbase;
        this.name_table = name_table;
        initComponents();
        getPanelTable(listColumn); // автоматическое располжение элементов для занесения информации
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTextField1.setText("рпапр");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(269, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1MouseClicked

    
    // --- Метод динамической атрисовки элементов Frame ---
    private void getPanelTable(ArrayList<String> elementTable) {
        //JTable jTableMech = new JTable();
        
        // GridLayout нужно это прикрутить для адекватности расположения
        super.getContentPane().removeAll();
        super.getContentPane().setLayout(new BorderLayout());
        // Создание панели
        JPanel contents = new JPanel();  
        GridLayout experimentLayout = new GridLayout(0,2); // расположение на полэкрана элементов
        contents.setLayout(experimentLayout);
        // Замена панели содержимого
        setContentPane(contents);
        for(String s: elementTable){
            if(s.equals("id")) continue;//Пропуск id
            JTextField jTextField = new JTextField(); 
            jTextField.setName(s);
            jTextField.setText(s); // название столбцов в полях(при нажатие мыши удалить)
            jTextField.addMouseListener(new java.awt.event.MouseAdapter() { // нажатие мыши
                int mouseEvent = 0;
                public void mouseClicked(java.awt.event.MouseEvent evt) {  
                    System.out.println("MouseE " + mouseEvent);
                    if (mouseEvent<=0){
                        jTextField.setText(""); // При первом кли только стираем содержимое
                    }
                    ++mouseEvent;
                }
            });
            contents.add(jTextField);
            //super.getContentPane().add(jTextField, BorderLayout.BEFORE_FIRST_LINE);
        }
        JButton addSig = new JButton("добавить сигнал");
        addSig.addActionListener(new java.awt.event.ActionListener() {
            // данные для заноса в базу 
            String[] rows;
            ArrayList<String> listNameColum = new ArrayList<>();
            public void actionPerformed(java.awt.event.ActionEvent evt) { // обработка нажатия кнопки
               int sumComponent = 0;
               for (int i=0; i<getContentPane().getComponents().length; ++i){ // Сколько компонентов с текстом столько и столбцов
                   Component c = getContentPane().getComponents()[i];
                    if (c instanceof JTextField) { // // проверяем все компоненты что из них кто
                        ++sumComponent;
                    }
               }
               rows = new String[sumComponent]; 
               
               int column = 0; // для массива
               for (Component c : getContentPane().getComponents()){
                    if (c instanceof JTextField) { // // проверяем все компоненты что из них кто
                        JTextField  jTextFieldB = (JTextField) c;
                        rows[column] = jTextFieldB.getName(); // забор имени и она же колонка 
                        listNameColum.add(jTextFieldB.getText()); // забор текста
                        //System.out.println("nameFtext " + keyMapTextfild);
                        //jTextFieldB.setText("Я нашел все текстовые поля");             
                     } 
                ++column;    
                }
                workbase.insertRows(name_table, rows, listNameColum); // Все в базу заносим
                dispose(); // и закрываем
            } 
               
        });
        contents.add(addSig); // внести сюда кнопку сигналов
        super.setContentPane(contents);
        super.setPreferredSize(new Dimension(300, 100));
        super.pack();
        //super.setLocationRelativeTo(null);
        super.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
