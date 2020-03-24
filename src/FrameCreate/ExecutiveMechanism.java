/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import DataBaseConnect.DataBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author nazarov
 */
public class ExecutiveMechanism extends javax.swing.JFrame {

    HashMap< String, Integer> addElementTable0 = new HashMap<>(); // элементы для отображения в левом полях
    HashMap< String, Integer> addElementTable1 = new HashMap<>(); // элементы для отображения в правом поле
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
        createListOneColimn();
        initComponents();
    }

    public ExecutiveMechanism() {
        this.workbase = new DataBase();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new JList<String>(listModel);
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new JList<String>(listModel_two);
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //jList1.addListSelectionListener(new listSelectionListener());
        jList1.setToolTipText("выбрать столбцы Ctrl + л.кнопка мыши");
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(92, 92, 92)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGap(92, 92, 92)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(421, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jButton5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton6)))
                    .addGap(38, 38, 38)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            int index = list.locationToIndex(evt.getPoint());
            String datainList = (String) list.getSelectedValue(); // Достаем значение из списка
            //System.out.println("index: "+index);
            // listModel_two.add(listModel_two.getSize(), "-- Новая запись --");
            addElementTable1.put(datainList, listNamedGraphMap.get(datainList));
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
            List listTable1 = sortMap(addElementTable1); // Новый отсортированный массив
            Iterator iterTable1 = listTable1.iterator();
            int idT = 0;
            while (iterTable1.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable1.next();
                String named = iter_ent.getKey();
                //System.out.println(named + "  " + index);
                listModel_two.add(idT, named);
                ++idT;
            }

            List listTable = sortMap(addElementTable0); // Новый отсортированный массив
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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (checkFieldTable != null) {
            listModel_two.clear();
            listModel.clear();
            for (int i = 0; i < checkFieldTable.length; ++i) {
                //  System.out.println(checkFieldTable[i]);
                addElementTable1.put(checkFieldTable[i], listNamedGraphMap.get(checkFieldTable[i]));
                addElementTable0.remove(checkFieldTable[i]);
            }
            for (String value : addElementTable1.keySet()) {
                listModel_two.add(listModel_two.getSize(), value);
            }
            for (String value : addElementTable0.keySet()) {
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
                addElementTable0.put(checkFieldTable[i], listNamedGraphMap.get(checkFieldTable[i]));
                addElementTable1.remove(checkFieldTable[i]);
            }
            for (String value : addElementTable1.keySet()) {
                listModel_two.add(listModel_two.getSize(), value);
            }
            for (String value : addElementTable0.keySet()) {
                listModel.add(listModel.getSize(), value);
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
            addElementTable0.put(datainList, listNamedGraphMap.get(datainList));
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
            List listTable1 = sortMap(addElementTable1); // Новый отсортированный массив
            Iterator iterTable1 = listTable1.iterator();
            int idT = 0;
            while (iterTable1.hasNext()) {
                Map.Entry<String, Integer> iter_ent = (Map.Entry<String, Integer>) iterTable1.next();
                String named = iter_ent.getKey();
                //System.out.println(named + "  " + index);
                listModel_two.add(idT, named);
                ++idT;
            }

            List listTable = sortMap(addElementTable0); // Новый отсортированный массив
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
    }//GEN-LAST:event_jList2MouseClicked

    // --- Кнопка добавления данныхв столбец ---
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        createListOneColimn();
    }//GEN-LAST:event_jButton1ActionPerformed

    // --- Фукция добавления данных из в левую колонку -- 
    void createListOneColimn() {
        // сначала очищаем список так как могло остаться от предыдущего открытия файла 
        listModel.clear();
        listModel_two.clear();
        addElementTable0.clear();
        addElementTable1.clear();
        List list = sortMap(listNamedGraphMap); // Новый отсортированный массив
        ArrayList<String> listTable = workbase.getviewTable();

        for (int i = 0; i < listTable.size(); ++i) {
            String nameT = listTable.get(i);
            // так надо помещать данные по фор
            listModel.add(i, nameT);
            addElementTable0.put(nameT, i);
        }
    }

    // метод слушателя нажатия кнопок правого столбца
    private void jList2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList2KeyReleased
        //System.out.println(evt.getKeyCode());  
        switch (evt.getKeyCode()) {
            case (10): {  // если нажали Enter
                String[] dataToParser = new String[]{"ON", "OF", "upp", "bp"}; // Список окончания сигналов
                ArrayList<ArrayList> listTagName = new ArrayList();
                for (Map.Entry<String, Integer> entry : addElementTable1.entrySet()) { // пробегаем так по списку справа
                    String nameTable = entry.getKey(); // Имя таблицы 
                    //Integer value = entry.getValue();
                    List<String> columnsB = workbase.selectColumns(nameTable); // возмем Названия колонок из таблицы
                    String whatF = "tag_name"; // Название столбца который высчитываем
                    String nameAlgColumn = compareData(columnsB, whatF); // передаем лист и строку на анализ соответствия. первый попавшийся возвращаем.
                    if (nameAlgColumn != null) {
                        String[] columns = new String[]{nameAlgColumn}; // колонки для передачи в базу SELECT
                        ArrayList<String[]> dataFromBase = workbase.getData(nameTable, columns); // получили массивы листов из базы 
                        //преобразовать его в нормальный вид
                        ArrayList<String> tagNameList = new ArrayList();
                        for (int i = 0; i < dataFromBase.size(); ++i) {  // пробегаем по листу но тут только 
                            String[] interMass = (String[]) dataFromBase.get(i);
                            String s = interMass[0]; // это элемент который 1 так как Tag_name 
                            if (s.equals("NULL")) { // с нул пропускаем
                                continue;
                            }
                            tagNameList.add(s);
                        }

                        listTagName.add(tagNameList); // Кладем структуру в Лист
                    } else {
                        JOptionPane.showMessageDialog(null, "Не найдены идентификаторы столбцов!"); // Это сообщение
                    }
                }
                // пробегаем по листам и ищем совпадения(рабочий вариант но не равнивает элементы в списках а только в самом себе)
                //for (int L = 0; L < listTagName.size(); ++L) {
                ArrayList<String> listDO = listTagName.get(0);
                ArrayList<String> listDI = listTagName.get(1);

                // Еще листы для хранения найденного
                ArrayList<ArrayList> findingTagname = new ArrayList();
                
                int elMass = 0; // первый элемент Листа
                while (listDO.size() > 0) {  // пробегаем по листу но тут только 1 так как Tag_name 
                    ArrayList<String> findTmp = new ArrayList(); // Временный 
                    String s = listDO.get(elMass); // это элемент который 
                    if (!s.equals("NULL")) { // если не это значение то

                        int j = 0;
                        while (j < listDI.size()) { // Проходим по ДО
                            String sj = listDI.get(j); // это элемент который 
                            String retFinStr = comparSignals(s, sj, dataToParser); // вызываем функцию сравнения(патерн на основе 1 строки)
                            if (retFinStr != null) {
                                System.out.println("Совпадения втором массиве " + s + " - " + retFinStr);
                                findTmp.add(sj); //если нашлми что там то добавляем
                                listDO.remove(j);// удаляем из списка
                            } else {
                                ++j;
                            }
                        }
                        int jLoc = elMass + 1;
                        while (jLoc < listDO.size()) { // прогоняем по самому себе
                            String locSeco = listDO.get(jLoc); // это элемент который 
                            String retFinStr = comparSignals(s, locSeco, dataToParser); // вызываем функцию сравнения(патерн на основе 1 строки)
                            if (retFinStr != null) {
                                System.out.println("Совпадения  в локальном " + s + " - " + retFinStr);
                                findTmp.add(locSeco); //если нашлми что там то добавляем
                                listDO.remove(jLoc);// удаляем из списка
                            } else {
                                ++jLoc;
                            }
                        }
                        findTmp.add(s); // Добавляем искомую переменную.
                        findingTagname.add(findTmp);
                        listDO.remove(elMass); // по любому удаляем
                    }
                }

       
                for (ArrayList list : findingTagname) {
                    for (Iterator it = list.iterator(); it.hasNext();) {
                        String s = (String) it.next();
                        System.out.print(s + " - ");
                    }
                    System.out.println();
                }
                break; // case
            }
        }
    }//GEN-LAST:event_jList2KeyReleased

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
