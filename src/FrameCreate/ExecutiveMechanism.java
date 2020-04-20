/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import DataBaseConnect.*;

/**
 *
 * @author nazarov
 */
public class ExecutiveMechanism extends javax.swing.JFrame {
    
    //HashMap< String, Integer> addElementTable0 = new HashMap<>(); // элементы для отображения в левом полях
    //HashMap< String, Integer> addElementTable1 = new HashMap<>(); // элементы для отображения в правом поле
     ArrayList<String>  addElementTable0 = new  ArrayList<>(); // элементы для отображения в левом полях
    ArrayList<String> addElementTable1 = new ArrayList<>(); // элементы для отображения в правом поле
    private HashMap< String, Integer> listNamedGraphMap = new HashMap<>();
    DefaultListModel listModel = new DefaultListModel(); // модель для динамического добавления данных
    private DefaultListModel<String> listModel_two = new DefaultListModel<String>();
    String[] checkFieldTable = null; // массив выделенных в таблицах
    DataBase workbase; // подключаемся к базе и берем список

    /**
     * Creates new form ExecutiveMechanism
     */
    public ExecutiveMechanism(DataBase workbase) { // С передачей базы
        this.workbase = workbase;
        initComponents();
        createListOneColimn();
    }

    public ExecutiveMechanism() {
        this.workbase = DataBase.getInstance();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new JList<String>(listModel_two);
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new JList<String>(listModel);
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton5.setText("-->");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("<--");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        //jList2.addListSelectionListener(new listSelectionListener());
        jList2.setToolTipText("выбрать столбцы Ctrl + л.кнопка мыши");
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jList2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jButton1.setText("Получить список таблиц");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        //jList1.addListSelectionListener(new listSelectionListener());
        jList1.setToolTipText("выбрать столбцы Ctrl + л.кнопка мыши");
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton2.setText("генерировать");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 189, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 94, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (checkFieldTable != null) {
            listModel_two.clear();
            listModel.clear();
            for (int i = 0; i < checkFieldTable.length; ++i) {
                //  System.out.println(checkFieldTable[i]);
                addElementTable1.add(checkFieldTable[i]);
                addElementTable0.remove(checkFieldTable[i]);
            }
            for (String value : addElementTable1) {
                listModel_two.add(listModel_two.getSize(), value);
            }
            for (String value : addElementTable0) {
                listModel.add(listModel.getSize(), value);
            }

            validate();
        }
        checkFieldTable = null;
    }//GEN-LAST:event_jButton5ActionPerformed

    // убрать из правого списка
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (checkFieldTable != null) {
            listModel_two.clear();
            listModel.clear();
            for (int i = 0; i < checkFieldTable.length; ++i) {
                //  System.out.println(checkFieldTable[i]);
                addElementTable0.add(checkFieldTable[i]);
                addElementTable1.remove(checkFieldTable[i]);
            }
            for (String value : addElementTable1) {
                listModel_two.add(listModel_two.getSize(), value); // Добавляем в конец таблицы
            }
            for (String value : addElementTable0) {
                listModel.add(listModel.getSize(), value); // Добавляем в конец таблицы
            }

            validate();
        }
        checkFieldTable = null;
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = list.locationToIndex(evt.getPoint());
            String datainList = (String) list.getSelectedValue();
            addElementTable1.remove(datainList); // удаляем по ключу элемент и перерисовываем
            addElementTable0.add(datainList);
            listModel_two.clear();
            listModel.clear();

            /* for (String  value : addElementTable0.keySet()) {
             listModel.add(listModel.getSize(), value);
             }
             for (String  value : addElementTable1.keySet()) {
             listModel_two.add(listModel_two.getSize(), value);
             }
             */
            //  Так до этого было
            //listModel_two.remove(index);
            //List listTable1 = sortMap(addElementTable1); // Новый отсортированный массив
            List listTable1 = addElementTable1; // Новый отсортированный массив
            Iterator iterTable1 = listTable1.iterator();
            int idT = 0;
            while (iterTable1.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable1.next();
                String named = iter_ent.getKey();
                //System.out.println(named + "  " + index);
                listModel_two.add(idT, named);
                ++idT;
            }

           // List listTable = sortMap(addElementTable0); // Новый отсортированный массив
            List listTable = addElementTable0; // Новый отсортированный массив
            
            Iterator iterTable = listTable.iterator(); // Земенить итератор на For
            idT = 0;
            while (iterTable.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable.next();
                String named = iter_ent.getKey();
                listModel.add(idT, named);
                ++idT;
            }
            validate();
        }
        checkFieldTable = new String[list.getSelectedIndices().length];
        for (int i = 0; i < list.getSelectedIndices().length; ++i) {// Массив индексов
            String datainList = (String) list.getModel().getElementAt(list.getSelectedIndices()[i]);
            //System.out.println(datainList);
            checkFieldTable[i] = datainList;
        }
    }//GEN-LAST:event_jList2MouseClicked

    // --- Кнопка добавления данныхв столбец ---
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        createListOneColimn();
    }//GEN-LAST:event_jButton1ActionPerformed

    // --- Фукция добавления данных в левую колонку -- 
    void createListOneColimn() {
        // сначала очищаем список так как могло остаться от предыдущего открытия файла 
        String[] massDoDi = {"dgo", "di"};// и справа выборка  последовательность обязательна
        listModel.clear();
        listModel_two.clear();
        addElementTable0.clear();
        addElementTable1.clear();
        List list = sortMap(listNamedGraphMap); // Новый отсортированный массив
        ArrayList<String> listTable = workbase.getviewTable();
        for (int i = 0; i < listTable.size(); ++i) {
            boolean findDOI = false;
            String nameT = listTable.get(i);
            int colelIntabl = listModel.size();
            listModel.add(colelIntabl, nameT);
            addElementTable0.add(nameT);

        }
        for (String s : massDoDi) { // ПРогоняем по списку совпадений что бы вручную не гнять и добавляем значения
            for (int i = 0; i < listModel.size(); ++i) {
                String nameEl_one_Table = (String) listModel.get(i);
                if (nameEl_one_Table.equals(s)) { // Если элементы совпадают с списком что ищем
                    listModel_two.add(listModel_two.size(), nameEl_one_Table);
                    addElementTable1.add(nameEl_one_Table);
                    // и удаляем данные из таблицы
                    listModel.remove(i);
                    addElementTable0.remove(nameEl_one_Table);
                    break;
                }

            }

        }

    }

    // метод слушателя нажатия кнопок правого столбца
    private void jList2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList2KeyReleased
        //System.out.println(evt.getKeyCode());  
        switch (evt.getKeyCode()) {
            case (10): {  // если нажали Enter
                getPanelTable(getListMechanizm()); // Вызов перерисовки окна с чем нашли
                    break; // case
            }
            

        }
    }//GEN-LAST:event_jList2KeyReleased

    // --- Фукция формирования двнных исполнительных механизмов
    ArrayList<ArrayList> getListMechanizm(){
          // если нажали Enter
                String[] dataToParser = new String[]{"ON", "OF", "upp", "bp"}; // Список окончания сигналов
                String[] egnoredList = new String[]{"NULL", "Res_", "Res"}; // Список окончания сигналов
                String[] nameColumnList = new String[]{"TAG_NAME_PLC", "Наименование сигнала"}; // наименование колонок для выборки из базы
                ArrayList<ArrayList> listTagName = new ArrayList();
                ArrayList<String> listColumnSelect = new ArrayList();
                ArrayList<ArrayList> findingTagname = new ArrayList();//листы для хранения найденного Что передаем
                for (String nameTable : addElementTable1) { // пробегаем так по списку справа
                    listColumnSelect.clear(); // так как при следующих прогонах оно добавляет данные
                    //String nameTable = entry.getKey(); // Имя таблицы 
                    //Integer value = entry.getValue();
                    List<String> columnsB = workbase.selectColumns(nameTable); // возмем Названия колонок из таблицы
                    //String whatF = "tag_name"; // Название столбца который высчитываем
                    for (String s : nameColumnList) {
                        String nameAlgColumn = compareData(columnsB, s); // передаем лист и строку на анализ соответствия(есть ли подобные столбцы). первый попавшийся возвращаем.
                        if (nameAlgColumn != null) {
                            listColumnSelect.add(nameAlgColumn); // Добавляем колонки для передачи в базу для запроса SELECT 
                        }
                    }
                    if (listColumnSelect.size() > 0) { //если есть что то
                        ArrayList<String[]> dataFromBase = workbase.getData(nameTable, listColumnSelect); // получили массивы листов из базы 
                        listTagName.add(dataFromBase); // Кладем структуру в Лист
                    } else {
                        JOptionPane.showMessageDialog(null, "Не найдены идентификаторы столбцов!"); // Это сообщение
                    }
                }
                if (listTagName != null && listTagName.size() > 1) { // если из выбранного что то есть то
                    // пробегаем по листам и ищем совпадения(рабочий вариант но не равнивает элементы в списках а только в самом себе)
                    ArrayList<String[]> listDO = listTagName.get(0);
                    ArrayList<String[]> listDI = listTagName.get(1);
                    int elMass = 0; // первый элемент Листа
                    int numElTagname = 0;// номер элемента с tag_name массива для сравнения
                    while (listDO.size() > 0) {  // пробегаем по листу но тут только 1 так как Tag_name 
                        ArrayList<String> findTmp = new ArrayList(); // Временный 
                        String[] s = listDO.get(elMass); // это элемент который 
                        if (checkSignal(s[numElTagname], egnoredList)) { // если сигнал из листа игнортрования
                            listDO.remove(elMass);
                        } else {
                            int j = 0;
                            while (j < listDI.size()) { // Проходим по DI
                                String[] sj = listDI.get(j); // это элемент который 
                                String retFinStr = comparSignals(s[numElTagname], sj[numElTagname], dataToParser); // вызываем функцию сравнения(патерн на основе 1 строки)
                                if (retFinStr != null) {
                                    //System.out.println("Совпадения втором массиве " + s + " - " + retFinStr);
                                    findTmp.add(sj[numElTagname]); //если нашлми что там то добавляем
                                    listDI.remove(j);// удаляем из списка
                                } else {
                                    ++j;
                                }
                            }
                            int jLoc = elMass + 1;
                            while (jLoc < listDO.size()) { // прогоняем по самому себе DO
                                String[] locSeco = listDO.get(jLoc); // это элемент который 
                                String retFinStr = comparSignals(s[numElTagname], locSeco[numElTagname], dataToParser); // вызываем функцию сравнения(патерн на основе 1 строки)
                                if (retFinStr != null) {
                                    //System.out.println("Совпадения  в локальном " + s + " - " + retFinStr);
                                    findTmp.add(locSeco[numElTagname]); //если нашлми что там то добавляем
                                    listDO.remove(jLoc);// удаляем из списка
                                } else {
                                    ++jLoc;
                                }
                            }
                            if (s.length > 2){ // для подстраховки если не нашли имена столбцов
                                findTmp.add(s[1]); // Русское имя должно быть
                            }
                            findTmp.add(s[numElTagname]); // Добавляем искомую переменную.
                            findingTagname.add(findTmp);
                            listDO.remove(elMass); // по любому удаляем
                            //}  
                        }
                    }
                    /*  // это для записи в файл
                     for (ArrayList list : findingTagname) { 
                     for (Iterator it = list.iterator(); it.hasNext();) {
                     String s = (String) it.next();
                     //System.out.print(s + " - ");
                     }
                     FileManager.writeCSV("665.txt", list);
                     }
                     */
                    
                }
         return findingTagname;
    }
    
    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) { // двойной клик мыши
            
            int index = list.locationToIndex(evt.getPoint());
            String datainList = (String) list.getSelectedValue(); // Достаем значение из списка
            //System.out.println("index: "+index);
            // listModel_two.add(listModel_two.getSize(), "-- Новая запись --");
            addElementTable1.add(datainList);
            listModel.remove(index);  // удаляем и из модели списка
            addElementTable0.remove(datainList);  // и удаляем
            listModel_two.clear();
            listModel.clear();

            // for (String  value : addElementTable1.keySet()) {
            //     listModel_two.add(listModel_two.getSize(), value);
            // }
            // for (String  value : addElementTable0.keySet()) {
            //     listModel.add(listModel.getSize(), value);
            // }
            List listTable1 = addElementTable1; // Новый отсортированный массив
            Iterator iterTable1 = listTable1.iterator();
            int idT = 0;
            while (iterTable1.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable1.next();
                String named = iter_ent.getKey();
                //System.out.println(named + "  " + index);
                listModel_two.add(idT, named);
                ++idT;
            }

            //List listTable = sortMap(addElementTable0); // Новый отсортированный массив
            List listTable = addElementTable0;
            Iterator iterTable = listTable.iterator();
            idT = 0;
            while (iterTable.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable.next();
                String named = iter_ent.getKey();
                listModel.add(idT, named);
                ++idT;
            }
            validate();
        }
        checkFieldTable = new String[list.getSelectedIndices().length];
        for (int i = 0; i < list.getSelectedIndices().length; ++i) {// Массив индексов
            String datainList = (String) list.getModel().getElementAt(list.getSelectedIndices()[i]);
            //System.out.println(datainList);
            checkFieldTable[i] = datainList;
        }
    }//GEN-LAST:event_jList1MouseClicked

    // --- Кнопка генерации и отображение механизмов ---
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        getPanelTable(getListMechanizm()); // Вызов перерисовки окна с чем нашли
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(ExecutiveMechanism.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExecutiveMechanism.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExecutiveMechanism.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExecutiveMechanism.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExecutiveMechanism().setVisible(true);
            }
        });
    }

    // --- Фукция сортировки из HAsgMap
    List sortMap(HashMap< String, Integer> map) {
        List list = new ArrayList(map.entrySet()); // Новый отсортированный массив
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b) {
                return a.getValue() - b.getValue();
            }
        });
        return list;
    }

    // --- регулярка для сравнения столбцов  --
    String compareData(List<String> columnsB, String str2) {
        String findDat = null;
        String hmiIns = "_plc";
        boolean find = false;
        boolean patt0 = false;
        boolean patt1 = false;
        boolean pattHMI = false;
        Pattern pattern0 = Pattern.compile("^(" + str2 + "|" + str2.toUpperCase() + ")$"); // точное соответствие но с игнорированем CAPS
        //Pattern patternHMI = Pattern.compile("^.*(" + str2 + ".*" + hmiIns + "|" + str2.toUpperCase() + ".*" + hmiIns.toUpperCase() + ").*$"); // если в имени есть HMI
        Pattern patternHMI = Pattern.compile("^.*(" + str2 + ".*" + hmiIns + ")|(" + str2.toUpperCase() + ".*" + hmiIns.toUpperCase() + ").*"); // если в имени есть HMI
        Pattern pattern1 = Pattern.compile("^.*(" + str2 + "|" + str2.toUpperCase() + ").*$"); // Любая строка в верхнем и нижнем регистре
        Matcher matcher0;
        int equalI;
        for (int i = 0; i < columnsB.size(); ++i) { // Пробежим для анализа по ним
            String s = columnsB.get(i);
            matcher0 = pattern0.matcher(s); // Применяем строку к патерну точного совпадения
            if (matcher0.matches()) {
                findDat = s;
                patt0 = true;
                find = true;
                equalI = i;
                //break;
            }
            if (!patt0) { // сюда заходим если никогда не нашли что в списке
                matcher0 = patternHMI.matcher(s); // Применяем строку к патерну все внутри + строка hmiIns после 
                if (matcher0.matches()) {
                    findDat = s;
                    pattHMI = true;
                    find = true;
                    equalI = i;
                    //break;
                }
                if (!pattHMI) {
                    matcher0 = pattern1.matcher(s); // Применяем строку к патерну все внутри
                    if (matcher0.matches()) {
                        findDat = s;
                        patt1 = true;
                        find = true;
                        equalI = i;
                        // break;
                    }
                }
            }
        }
        return findDat;
    }

    // сопоставление имен сигналов, и списком возможных вариантов
    String comparSignals(String str1, String str2, String[] massEnd) {
        String finderS = null;
        String cutString = null;
        for (int i = 0; i < massEnd.length; ++i) { // пробегаем по списку окончаний отрезая окончания
            cutString = "^(.*)" + massEnd[i] + "$";
            Pattern pattern0 = Pattern.compile(cutString);
            Matcher matcher0 = pattern0.matcher(str1);  // впихиваем в сравнение 1 строку из входа
            if (matcher0.matches()) { // попали под патерн 
                //System.out.println(matcher0.group(0)); // Системный вывод
                str1 = matcher0.group(1);
                break; // до первого найденного
            }
        }

        for (int i = 0; i < massEnd.length; ++i) { // пробегаем по списку окончаний
            String patternS = "^(" + str1 + ").*" + massEnd[i] + "$"; // прикручиваем окончание и сравниваем
            Pattern pattern0 = Pattern.compile(patternS);
            Matcher matcher0 = pattern0.matcher(str2);  // впихиваем в сравнение 1 строку из входа
            if (matcher0.matches()) { // попали под патерн 
                finderS = str2;
                break; // до первого найденного
            }
        }
        return finderS;
    }

    // проверка имени сигнала, есть ли совпадения с списком c начала Строки
    boolean checkSignal(String str, String[] massName) {
        boolean enterSig = false;
        String cutString;
        for (int i = 0; i < massName.length; ++i) { // пробегаем по списку окончаний отрезая окончания
            cutString = "^(" + massName[i] + ".*)" + "|" + "^(" + massName[i].toUpperCase() + ".*)" + "$";
            //s= s.substring(0,1).toUpperCase() + s.substring(1); // может пригодится первый символ из строки в верхнем регистре
            //s = Character.toUpperCase(s.charAt(0)) + s.substring(1); // может пригодится первый символ из строки в верхнем регистре
            Pattern pattern0 = Pattern.compile(cutString);
            Matcher matcher0 = pattern0.matcher(str);  // впихиваем в сравнение строку из входа
            if (matcher0.matches()) { // попали под патерн 
                System.out.println("Findind unnecessary sig --> " + matcher0.group(0)); // Системный вывод
                enterSig = true;
                break; // до первого найденного
            }
        }
        return enterSig;
    }

    // метод обновления JScrollPane перерерисовка а перерисовываем Frame
    private void getPanelTable(ArrayList<ArrayList> listDataToTable) {
        JTable jTableMech = new JTable();
        JScrollPane jScrollPaneTable = new javax.swing.JScrollPane(jTableMech);

        //jScrollPaneTable.setViewportView(jTableMech);
        //TableNz tablecreater = new TableNz(); // Собственная модель таблицы
        //jTableMech.setDefaultEditor(Date.class, new DateCellEditor());// Определение редактора ячеек
        //jTableMech.setModel(tablecreater.getModelTable(listDataToTable)); // передаем данные для постройки таблицы
        
        TableNzVer2 jPanelMyTable = new TableNzVer2(listDataToTable);     // Новая таблица
//        
//        jPanel2.removeAll();
//        jPanel2.repaint();
//        jPanel2.revalidate();
//        
//        jPanel2.setLayout(new BoxLayout(jTableMech, BoxLayout.X_AXIS));
//        jPanel2.add(jTableMech); // Добавляем таблицу с скролом
//        jPanel2.repaint();
//        jPanel2.revalidate();
        
        // --- перерисовка Frame  ---
        super.getContentPane().removeAll();
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(jPanelMyTable, BorderLayout.CENTER);
        super.setPreferredSize(new Dimension(800, 600));
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
