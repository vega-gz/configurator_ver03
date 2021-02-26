package FrameCreate;

import Generators.Generator;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.SaveFrameData;
import Tools.TableTools;
import Tools.Tools;
import Tools.isCange;
import globalData.globVar;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/*@author Lev*/
public class addOPCserver extends javax.swing.JFrame {
    DefaultListModel list1 = new DefaultListModel();
    DefaultListModel list2 = new DefaultListModel();
    //String abonent = globVar.abonent;
    //int prevOpcName = 0;
    final String[] nodeIDitems = {"Number","Name"};
    final String[] nodeIDtypeItems = {"Numeric","String"};
    final String[] opcTabCols = {"tagName","nameSpace"};
    ArrayList<String[]> opcList;
    ArrayList<String> plusList = new ArrayList<>();
    boolean isChang = false;
    int opcServName;
    String nameSpace;
    int nodeID;
    int nodeIDtype;
    String comment;
    ArrayList<String> tableList;

    public addOPCserver() {
        if(!globVar.DB.isConnectOK())return;
        
        initComponents();
        
        jComboBox1.setModel(new DefaultComboBoxModel(nodeIDitems));//ID
        jComboBox2.setModel(new DefaultComboBoxModel(nodeIDtypeItems));//ID type
        jComboBox3.setModel(new DefaultComboBoxModel(new String[]{"1","2"}));//namespace
        nameSpace = "1";
        
        tableList = globVar.DB.getListTable("opc");
        if(tableList==null){
            comment = "nodeID:Number; nodeIDtype:Numeric;";
            globVar.DB.createTableEasy("opcServer1", opcTabCols, comment);
            jComboBox4.setModel(new DefaultComboBoxModel(new String[]{"opcServer1"}));
            opcServName = 0;
            nodeID = 0;
            nodeIDtype = 0;
        }else{
            jComboBox4.setModel(new DefaultComboBoxModel(tableList.toArray(new String[tableList.size()])));
            opcServName = 0;
            comment = globVar.DB.getCommentTable(tableList.get(0));
            setServSettings(comment);
        }
        
        opcList = globVar.DB.getData(jComboBox4.getItemAt(opcServName), opcTabCols);
        setLists();
        //-------------  Для правильного списка nameSpace ------------------------------
        //int nameSpaceCnt = 2;
        for(String[]s:opcList){
            int x = Tools.getIndexOfComboBox(jComboBox3,s[1]);//Integer.parseInt(s[2]);
            if(x<0)jComboBox3.addItem(s[1]);
        }
        //------------------------------------------------------------------------------
        //Лямбда для определения изменений
        isCange ich = ()->{//Для того, чтобы сохранение в БД происходило всегда и без переспроса, включаем сохранение в функцию проверки наличия изменений
            resetOpcList();        //сохраняем изменения списака предназначенных к архивированию сигналов
            saveDateInDB(isChang);//сохранение в БД списка сигналов 
            return false;
        };
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            resetOpcList();        //сохраняем изменения списака предназначенных к архивированию сигналов
            saveDateInDB(isChang);//сохранение в БД списка сигналов 
        };
        TableTools.setFrameListener(this, sfd, ich, null);
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
        jTextField1 = new javax.swing.JTextField();

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

        jList2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList2KeyReleased(evt);
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
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
                .addGap(0, 200, Short.MAX_VALUE))
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

        jComboBox3.setEditable(true);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jComboBox3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox3KeyReleased(evt);
            }
        });

        jLabel3.setText("NodeID");

        jLabel4.setText("NodeID type");

        jComboBox4.setEditable(true);
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jTextField1.setText("*");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(2, 2, 2)
                        .addComponent(jComboBox4, 0, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addGap(3, 3, 3))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addGap(3, 3, 3))
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
                String s1 = list1.get(i-offset).toString();
                if(!list2.contains(s1)) list2.addElement(s1);
                list1.remove(i-offset);
                offset++;
                isChang = true;
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    //Кнопка "<"
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetCelectSigsFromList2();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable1ComponentResized
    }//GEN-LAST:event_jTable1ComponentResized

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
    }//GEN-LAST:event_jTable1MousePressed
    //Кнопка "Сохранить"
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        resetOpcList();        //сохраняем изменения списака предназначенных к архивированию сигналов
        saveDateInDB(isChang);//сохранение в БД списка сигналов 
        //isChang = false;
    }//GEN-LAST:event_jButton6ActionPerformed
    //Кнопка "+"
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        plusList.add(jList1.getSelectedValue());
        TableTools.setSignalList(list1, null, null, false, opcList, plusList);
    }//GEN-LAST:event_jButton7ActionPerformed
    //Кнопка "-"
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String s = jList1.getSelectedValue();
        int x = s.indexOf(".");
        if(x>0) plusList.remove(s = s.substring(2, x));
        TableTools.setSignalList(list1, null, null, false, opcList, plusList);
        x = list1.indexOf(s);
        if(x>=0) jList1.setSelectedIndex(x);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed
    //Кнопка "Приложение"
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        resetOpcList();
        saveDateInDB(isChang);//сохранение в БД списка сигналов 
        String processName = "Генерация из таблицы";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }
        DoIt di = () -> {
            int ret = Generator.genOPC((String)jComboBox4.getSelectedItem(), (String)jComboBox1.getSelectedItem(), 
                                       (String)jComboBox2.getSelectedItem(), opcList, jProgressBar1);
            if(ret == 0) JOptionPane.showMessageDialog(null, "Генерация завершена успешно"); // Это сообщение
            else JOptionPane.showMessageDialog(null, "Генерация завершена с ошибками");

            globVar.processReg.remove(processName);
            jProgressBar1.setValue(0);
         };
        BackgroundThread bt = new BackgroundThread("Генерация OPC", di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed
    //Выбор пространства имён
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        String item = (String) jComboBox3.getSelectedItem();
        if(!Tools.isInteger(item)){
            JOptionPane.showMessageDialog(null, "Номер пространства имён - только положительное целое число");
            return;
        }
        resetOpcList();
        int y = jComboBox3.getSelectedIndex();
        if(y<0)jComboBox3.addItem(item);
        nameSpace = item;
        list2.removeAllElements();
        TableTools.setArchiveSignalList(list2, opcList, nameSpace);
        saveDateInDB(isChang);//сохранение в БД списка сигналов 
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox3KeyReleased
//        String item = (String) jComboBox1.getEditor().getItem();
//        int y = -1;
//        try{
//            y = Integer.parseUnsignedInt(item);
//        }catch(NumberFormatException e){
//            JOptionPane.showMessageDialog(null, "Номер пространства имён - тоько положительное целое число");
//            return;
//        }
//
    }//GEN-LAST:event_jComboBox3KeyReleased

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        String item = (String) jComboBox4.getSelectedItem();
        if(item.length() < 3 || !item.substring(0, 3).equals("opc")){
            JOptionPane.showMessageDialog(null, "Имя opc-сервера должно начинаться с \"opc\"");
            return;
        }
        int y = jComboBox4.getSelectedIndex();
        int n = -1;
        if(y<0){
            Object[] options = {"Создать новый", "Переименовать", "Отмена"};

            n = JOptionPane.showOptionDialog(null, "Выберите действие:",
                    "Операции над ОРС серверами", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options,
                    options[2]);
            switch (n) {
                case 2://---------Ничего не делать
                    return;
                case 1://---------Переименовать
                    globVar.DB.renameTable(jComboBox4.getItemAt(opcServName), item);
                    jComboBox4.removeItemAt(opcServName);
                    jComboBox4.insertItemAt(item, opcServName);
                    break;
                default://---------Создать новый
                    resetOpcList();        //сохраняем изменения списака предназначенных к архивированию сигналов
                    saveDateInDB(isChang);//сохранение в БД списка сигналов 
                    jComboBox4.addItem(item);
                    opcServName = jComboBox4.getItemCount()-1;
                    globVar.DB.createTableEasy(item, opcTabCols, comment);
                    tableList.add(item);
                    nameSpace = "1";
                    opcList.clear();
                    list1.clear();
                    list2.clear();
                    setLists();
                    break;
            }
        }else{
            if(opcServName != y){
                resetOpcList();        //сохраняем изменения списака предназначенных к архивированию сигналов
                saveDateInDB(isChang);//сохранение в БД списка сигналов 
                opcServName = y;
                opcList.clear();
                comment = globVar.DB.getCommentTable(item);
                setServSettings(comment);
                opcList = globVar.DB.getData(item, opcTabCols);
                list1.clear();
                list2.clear();
                setLists();
                nameSpace = "1";
            }
        }
        
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String s = jTextField1.getText();
        list1.clear();
        TableTools.setSignalList(list1, null, null, false, opcList, plusList, jTextField1.getText());
        jList1.setModel(list1);
    }//GEN-LAST:event_jTextField1KeyReleased
    //обработчик клавиатуры
    private void jList2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList2KeyReleased
        if(evt.getKeyCode()==127) resetCelectSigsFromList2();
        //System.out.println("evt.getKeyCode()="+evt.getKeyCode());
    }//GEN-LAST:event_jList2KeyReleased

    private void setServSettings(String s){

        if(s !=null ){
           if(!s.equalsIgnoreCase("null")){
                int x = s.indexOf("nodeID:");
                x += 7;
                int y = s.indexOf(";",x);
                nodeID = Tools.indexOfArray(nodeIDitems, s.substring(x,y));
                jComboBox1.setSelectedIndex(nodeID);
                x = s.indexOf("nodeIDtype:");
                x += 11;
                y = s.indexOf(";",x);
                nodeIDtype = Tools.indexOfArray(nodeIDtypeItems, s.substring(x,y));
                jComboBox2.setSelectedIndex(nodeIDtype);
                jComboBox3.setSelectedIndex(0);
            }
        }
    }
    
    private void setLists(){
        if(opcList==null) return;
        TableTools.setArchiveSignalList(list2, opcList, "1");
        Tools.setPlusList(opcList, plusList);
        TableTools.setSignalList(list1, null, null, false, opcList, plusList, jTextField1.getText());
        jList1.setModel(list1);
        jList2.setModel(list2);
    }
    

    private void resetOpcList(){
        if(opcList==null) return;
        for(int i=0;i<opcList.size(); i++){    //пробегаем поо списту предназнгаченных к архивированию сигналов
            if(opcList.get(i)[1].equals(nameSpace)){ // если сигнал имеет номер нужный архива
                opcList.remove(i);                     // удаляем его
                i--;
            }
        }
        if(list2==null) return;
        for(int i=0; i<list2.size(); i++) opcList.add(new String[]{(String)list2.get(i),nameSpace}); //заносим в списов все сигналы из текужего списка второго листа        
    }
    
    private void saveDateInDB(boolean isChng){
        if(!isChng) return;
        comment = "nodeID:"+jComboBox1.getSelectedItem()+"; nodeIDtype:"+jComboBox2.getSelectedItem()+";";
        TableTools.saveListInDB(opcList, globVar.DB, jComboBox4.getItemAt(opcServName), opcTabCols, comment);//сохранение в БД списка сигналов 
        isChang = false;
    }
    private void resetCelectSigsFromList2(){
        int[] x = jList2.getSelectedIndices();
        int offset = 0;
        for(int i: x){
            list2.remove(i-offset);
            offset++;
        }
        resetOpcList();
        Tools.setPlusList(opcList, plusList);
        TableTools.setSignalList(list1, null, null, false, opcList, plusList, jTextField1.getText());
        jList1.setModel(list1);
        isChang = true;
    }
    //=========================================================================================================================
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
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
