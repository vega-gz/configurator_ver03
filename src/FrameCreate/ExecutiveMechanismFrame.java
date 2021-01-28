/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import DataBaseTools.DataBase;
import Algorithm.ExecutiveMechanismObject;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer; //Таймер каждую секунду
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.w3c.dom.Node;

/**
 *
 * @author nazarov
 */
public class ExecutiveMechanismFrame extends javax.swing.JFrame {

    //HashMap< String, Integer> addElementTable0 = new HashMap<>(); // элементы для отображения в левом полях
    //HashMap< String, Integer> addElementTable1 = new HashMap<>(); // элементы для отображения в правом поле
    ArrayList<String> addElementTable0 = new ArrayList<>(); // элементы для отображения в левом полях
    ArrayList<String> addElementTable1 = new ArrayList<>(); // элементы для отображения в правом поле
    private HashMap< String, Integer> listNamedGraphMap = new HashMap<>();
    DefaultListModel listModel = new DefaultListModel(); // модель для динамического добавления данных
    private DefaultListModel<String> listModel_two = new DefaultListModel<String>();
    String[] checkFieldTable = null; // массив выделенных в таблицах
    DataBase workbase = globVar.DB; // подключаемся к базе и берем список
    ArrayList<ArrayList> listDataToTable; // ПРи старте сразу формируем данные
    String nameTable; // название таблицы и заголовка
    String[] columns; // названия колонок для таблицы в формате масива
    ArrayList<String> columnT; // названия колонок для таблицы
    int identNodecase; // по идентификатор выбора какой механизм использовать
    ExecutiveMechanismObject testW; // механизм работы с данными для обрабоки Исполнительных механизмов
    TableNzVer3 boneTable;
    JTable tableMech; // Таблица механизмов
    boolean showAlltable = false; // тригер показать всю таблицу или только
    int caseMecha = 0; // значение по которым пойдет обработка механизмов
    ArrayList<int[]> markCelT = new ArrayList<>(); // список клеток которые метим для сопоставленя данных из базы и вновь сгенерированных

    /**
     * Creates new form ExecutiveMechanism
     */
    public ExecutiveMechanismFrame() { // С передачей базы
        testW = new ExecutiveMechanismObject();
        String[] arrNameExecute = testW.getNodeMechRun();// Что передаем на выбор
        getJDialogChoiser(arrNameExecute).setVisible(true); // вызываем диалог с выбором какой механизм обрабатываем

        // если в окне был какой то выбор по кнопке или списку
        if (caseMecha != 0) {
            switch (caseMecha) {
                case 1: { // если по списку 
                    listDataToTable = testW.getDataCurrentNode(identNodecase, showAlltable);  // реализация конкретного механизма
                    nameTable = testW.getNameTable(); // получим название таблицы строго после getDataCurrentNode
                    columns = testW.getColumns(); // получить колонки для построки таблицы
                    //columnT = testW.getColumnsT();

                    ArrayList<String[]> dataDB = testW.getDataFromBase(nameTable); // Данные из базы
                    markCelT.clear();

                    if (dataDB != null) {
                        // ==== Сравнить данные из того что сгенерировали и данные из базы ====
                        for (int i = 0; i < listDataToTable.size(); ++i) {
                            ArrayList<String> arrAM = listDataToTable.get(i);
                            for (int j = 0; j < arrAM.size(); ++j) {
                                String s = arrAM.get(j);
                                String sB = null;
                                if (i < dataDB.size() && j < dataDB.get(i).length) {
                                    sB = dataDB.get(i)[j]; // должны быть идентичны
                                }
                                int[] tmpINT = {i, j};
                                if (!s.equals(sB)) {
                                    markCelT.add(tmpINT);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Не найдены таблицы или пусты \n" + nameTable); //сообщение
                    }

                    boneTable = new TableNzVer3(nameTable, columns, listDataToTable, false);  // реализация моей таблицы(без внесения в базу)
                    tableMech = new TableNzVer3(nameTable, columns, listDataToTable, false).getJTable();

                    // раскрас таблицы
                    for (int i = 0; i < tableMech.getColumnCount(); i++) {
                        System.out.println("IterI");
                        tableMech.getColumnModel().getColumn(i).setCellRenderer(new RendererCellTable(markCelT));
                    }

                    initComponents();
                    this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE); // Закрываем окно а не приложение
                    this.setVisible(true);
                    break;
                }
                case 2: { // если по кнопке                    
                    listDataToTable = testW.getDataAllMechaNode(showAlltable);  // реализация всех механизмов
                    nameTable = testW.getNameTable(); // получим название таблицы строга после getDataCurrentNode
                    columns = testW.getColumns(); // получить колонки для построки таблицы
                    //columnT = testW.getColumnsT();
                    boneTable = new TableNzVer3(nameTable, columns, listDataToTable, false);  // реализация моей таблицы(без внесения в базу)
                    initComponents();
                    this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE); // Закрываем окно а не приложение
                    this.setVisible(true);
                    break;
                }
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = tableMech;     // Новая таблица(а есть еще более новей);
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("jLabel1");

        jButton1.setText("jButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jButton1)))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(6);

        jButton2.setText("добавить в базу");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        // Раскрывающийся список
        JComboBox<String> comboTrueFalse = new JComboBox<String>(new String[] { "true", "false"});
        // Редактор ячейки с списком
        DefaultCellEditor editor = new DefaultCellEditor(comboTrueFalse);
        // Определение редактора ячеек для колонки
        jTable1.getColumnModel().getColumn(3).setCellEditor(editor);
        jScrollPane1.setViewportView(jTable1);

        jCheckBox1.setText("показать без окончаний");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton3.setText("GetFromDB");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addGap(51, 51, 51))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // --- Кнопка добавления данных в базу ---
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // получим данные с таблицы Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<String[]> dataTable = new ArrayList<>();
                int columnCount = jTable1.getModel().getColumnCount(); // количество столбцов
                for (int y = 0; y < jTable1.getRowCount(); ++y) { // бежим по строкам
                    String[] arraRow = new String[columnCount];
                    for (int x = 0; x < columnCount; ++x) {
                        arraRow[x] = (String) jTable1.getModel().getValueAt(y, x);
                    }
                    dataTable.add(arraRow);
                }
                testW.addDataToBase(dataTable);
            }
        });

    }//GEN-LAST:event_jButton2ActionPerformed

    // -- активен флажок или нет --
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            showAlltable = true;
            listDataToTable = testW.getDataCurrentNode(identNodecase, showAlltable);  // реализация конкретного механизма
            nameTable = testW.getNameTable(); // получим название таблицы строга после getDataCurrentNode
            columnT = testW.getColumnsT();
            boneTable = new TableNzVer3(nameTable, columns, listDataToTable, false);  // реализация моей таблицы(без внесения в базу)
            this.getContentPane().removeAll(); // все удаляет работает
            initComponents();
            this.repaint();
            jCheckBox1.setSelected(showAlltable);

        } else {
            showAlltable = false;
            listDataToTable = testW.getDataCurrentNode(identNodecase, showAlltable);  // реализация конкретного механизма
            nameTable = testW.getNameTable(); // получим название таблицы строга после getDataCurrentNode
            columnT = testW.getColumnsT();
            boneTable = new TableNzVer3(nameTable, columns, listDataToTable, false);  // реализация моей таблицы(без внесения в базу)
            this.getContentPane().removeAll();
            initComponents();
            this.repaint();;
            jCheckBox1.setSelected(showAlltable);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        testW.getDataFromBase(nameTable);
    }//GEN-LAST:event_jButton3ActionPerformed

    // ---  метод диалога выбора по какому методу делаем ИМ ---
    private JDialog getJDialogChoiser(String[] massNameNode) {
        // тут он определяется до инициализации всех компонентов
        String time = "Time : ";
        //int counT = 5;
        JDialog jDialog1 = new JDialog(this, "Выбор метода генерации ИМ", true); // модальное блокирующее окно
        jDialog1.setResizable(false); // отключить изменение размера окна
        jDialog1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jDialog1.setSize(500, 300);
        jDialog1.setLocationRelativeTo(null); // по центру экрана
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JComboBox jComboBox1 = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JButton jButtonAllMech = new javax.swing.JButton(); // кнопка выбора всех механизмов
        //jPanel2 = new javax.swing.JPanel();
        //jButton2 = new javax.swing.JButton();
        //jScrollPane1 = new javax.swing.JScrollPane();

// слушатель окна выбора(не работает)
        jDialog1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPreased(java.awt.event.KeyEvent evt) {
                //if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
                //System.out.println(jComboBox1.getSelectedItem());
                System.out.println("key");
                //identNodecase = jComboBox1.getSelectedIndex();
                //jDialog1.dispose(); // Закрыть
                //}
            }
        });

        jLabel1.setText("Выбор метода генерации ИМ");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(massNameNode));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //jComboBox1ActionPerformed(evt);
                //System.out.println(jComboBox1.getSelectedItem());
                identNodecase = jComboBox1.getSelectedIndex(); // присвоить перемнной название из списка
                caseMecha = 1; // метод выбора какой то одного механизма
                jDialog1.dispose(); // Закрыть
            }
        });

        // обработчик кнопки всех механизмов
        jButtonAllMech.setText("обработать все механизмы");
        jButtonAllMech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                caseMecha = 2; // метод обработки всех механизма
                jDialog1.dispose(); // Закрыть диалоговое окно
            }
        });

//        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
//        jPanel1.setLayout(jPanel1Layout);
//        jPanel1Layout.setHorizontalGroup(
//                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(jPanel1Layout.createSequentialGroup()
//                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addGroup(jPanel1Layout.createSequentialGroup()
//                                                .addGap(82, 82, 82)
//                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
//                                        .addGroup(jPanel1Layout.createSequentialGroup()
//                                                .addGap(146, 146, 146)
//                                                .addComponent(jLabel2)))
//                                .addContainerGap(92, Short.MAX_VALUE))
//        );
//        jPanel1Layout.setVerticalGroup(
//                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(jPanel1Layout.createSequentialGroup()
//                                .addComponent(jLabel1)
//                                .addGap(18, 18, 18)
//                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addGap(53, 53, 53)
//                                .addComponent(jLabel2)
//                                .addGap(0, 151, Short.MAX_VALUE))
//        );
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabel1))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(65, 65, 65)
                                        .addComponent(jButtonAllMech)))
                        .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel1)
                        .addGap(62, 62, 62)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonAllMech))
                        .addContainerGap(160, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
                jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
                jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );

        return jDialog1;
    }

    // --- Таким классом красим нашу 1 таблицу  ---
    class RendererCellTable implements TableCellRenderer {

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        ArrayList<int[]> markCelT;

        public RendererCellTable(ArrayList<int[]> markCelT) {
            this.markCelT = markCelT;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            for (int[] arrInt : markCelT) {
                if (arrInt[1] == column & arrInt[0] == row) {
                    cell.setBackground(Color.YELLOW);
                    break;
                } else {
                    cell.setBackground(Color.WHITE);
                }
            }
//        System.out.println(Integer.toString(row) +  " " + Integer.toString(column));
//        if (column == 0 && row == 0){
//         cell.setBackground(Color.BLACK);
//         }else if (column == 0 && row == 1) {
//         cell.setBackground(Color.BLUE);
//         }else if (column == 0 && row == 2) {
//         cell.setBackground(Color.YELLOW);
//         }
            return cell;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
