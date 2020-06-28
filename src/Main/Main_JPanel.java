package Main;

import FrameCreate.*;
import java.awt.Toolkit;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;
import DataBaseTools.DataBase;
import Generators.Generator;
import ReadWriteExcel.RWExcel;
import Tools.BackgroundThread;
import Tools.DoIt;
import XMLTools.XMLSAX;
import java.awt.Dimension;
import Tools.FileManager;
import Tools.Tools;
import globalData.globVar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public final class Main_JPanel extends javax.swing.JFrame {

    String url, nameProject, user, pass;

    ArrayList<String> listDropT;
    XMLSAX createXMLSax = new XMLSAX();
    String filepatch, type;
    File excel = null;
    ProgressBar pb = null;

    public Main_JPanel(){
        globVar.DB = DataBase.getInstance();
        initComponents();
        jTextField1.setText(globVar.desDir);
        if(globVar.DB == null){
            this.setTitle("База " + globVar.currentBase + "по пути " + globVar.dbURL + " не найдена");
        }else{
            initMyComponent();
        }
        Tools.isDesDir();
    }
    public void initMyComponent(){
        //initComponents();
        jTextField1.setText(globVar.desDir);
        //globVar.DB = DataBase.getInstance();
        listDropT = globVar.DB.getListTable(); // получи список таблиц при включении
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        DataBase.createAbonentTable();
        jComboBox2.setModel(getComboBoxModelAbonents()); // абоненты из базы
        globVar.abonent = jComboBox2.getItemAt(0);
        this.setTitle("Текущая база:" + globVar.currentBase + ", путь: " + globVar.dbURL); // установить заголовок
    }
    //public void 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameTable = new javax.swing.JFrame();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton10 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        // прикручиваем нашу модель дерева методом getModelTreeNZ()
        jTree1 = new javax.swing.JTree(getModelTreeNZ());
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        jFrameTable.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                jFrameTableWindowClosed(evt);
            }
        });

        javax.swing.GroupLayout jFrameTableLayout = new javax.swing.GroupLayout(jFrameTable.getContentPane());
        jFrameTable.getContentPane().setLayout(jFrameTableLayout);
        jFrameTableLayout.setHorizontalGroup(
            jFrameTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrameTableLayout.setVerticalGroup(
            jFrameTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        jButton1.setText("Создать новый проект");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Загрузить Excel ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "item1", "item2", "item3", "item4" }));
        jComboBox1.setToolTipText("");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton4.setText("Отобразить список");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Архивы");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setText("Переподключение к базе");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton7.setText("исполнительный механизм");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText(".type в базу");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Изменить");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel1.setText("Неактивные кнопки. Функции в разработке");

        jLabel2.setText("Путь к каталогу проекта");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jTextField1.setText("jTextField1");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton10.setText("Добавить абонента");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel3.setText("Текущий абонент");

        // слушатель выделения
        jTree1.addTreeSelectionListener(new SelectionListener(this));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/VegaConLogo.png"))); // NOI18N

        jMenu2.setText("Настройки");

        jMenuItem1.setText("сменить папку проекта");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem3.setText("Сменить БД");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Утилиты");

        jMenuItem2.setText("Переименовать *.type файлы");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9))
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 107, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton10)
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jButton9))))
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton5)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    // --- Реакция кнопки загрузак Excel --
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(!Tools.isDB()) return;
        String processName = "Процесс загрузки данных из Excel";
        if(globVar.processReg.indexOf(processName)>=0){
            JOptionPane.showMessageDialog(null, "Запуск нового процесса загрузки в БД данных из файла Excel заблокирован до окончания предыдущей загрузки");
            return;
        }
        int casedial = JOptionPane.showConfirmDialog(null, "Загрузка в БД информации для абонента \"" + globVar.abonent+"\"");
        if(casedial!=0)return;
        
        JFileChooser fileopen = new JFileChooser(globVar.desDir);
        int ren = fileopen.showDialog(null, "Загрузка данных для "+globVar.abonent);
        if (ren == JFileChooser.APPROVE_OPTION) {
            excel = fileopen.getSelectedFile();// выбираем файл из каталога
        }
        pb = new ProgressBar();
        pb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pb.setTitle(processName);
        pb.setVisible(true);

        DoIt di = () -> {
            int ret = 1;
            try {
                ret = RWExcel.ReadExelFromConfig(excel.getPath(),pb.jProgressBar1); // вызов фукции с формированием базы по файлу конфигурации
            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ret>=0){
                JOptionPane.showMessageDialog(null, "В базу загружено " + ret +" таблиц");
                jTree1.setModel(getModelTreeNZ());// обновить дерево
            }else if(ret<0) JOptionPane.showMessageDialog(null, "При генерации было ошибки. См. файл 'configurer.log'");
            pb.setVisible(false);
            globVar.processReg.remove(processName);
        };
        BackgroundThread bt = new BackgroundThread(processName, di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton3ActionPerformed

    // --- Метод реагирования на выбор поля из списка таблиц ---
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //if(globVar.DB==null) return;
        String selectT = (String) jComboBox1.getSelectedItem();
            showTable(selectT); // вызов метода построения таблицы 
    }//GEN-LAST:event_jComboBox1ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(!Tools.isDB()) return;
        //  globVar.DB.connectionToBase(url,pass,user);
        listDropT = globVar.DB.getListTable();
        Iterator<String> iter_list_table = listDropT.iterator();
        String listTable = "";

        while (iter_list_table.hasNext()) {

            listTable += iter_list_table.next() + " \n";
        }

        //jTextArea1.setText(listTable);
    }//GEN-LAST:event_jButton4ActionPerformed
//---------- Редактирование конфигурации архивов ---------------
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(!Tools.isDB()) return;
        if(!Tools.isDesDir()) return;
        JFrame editArchive = new addArchive();
        editArchive.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        editArchive.setTitle("Редактирование архивов");
        editArchive.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(!Tools.isDB()) return;
        globVar.DB = DataBase.getInstance();// подключится к базе конфигом другого не дано
        if (globVar.DB == null) {
            JOptionPane.showMessageDialog(null, "Подключение к базе не удалось");
            return;
        }
        String nameBD = globVar.DB.getCurrentNameBase();
        String userBD = globVar.DB.getCurrentUser();
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        //String message = null;
        if (nameBD != null) {
            JOptionPane.showMessageDialog(null, "Подключено к базе " + nameBD + " пользователем " + userBD);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    // --- Кнопка вызова окна с исполнительным механизмом ---
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if(!Tools.isDB()) return;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        ExecutiveMechanismFrame frameExecutiveMechanism = new ExecutiveMechanismFrame(); // И передаем туда управление базой
        //frameExecutiveMechanism.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
        frameExecutiveMechanism.setDefaultCloseOperation(frameExecutiveMechanism.DISPOSE_ON_CLOSE); // Закрываем окно а не приложение
        frameExecutiveMechanism.setVisible(true);

    }//GEN-LAST:event_jButton7ActionPerformed


    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//        try {
//            FileManager.renameUUIDinDirectory(globVar.desDir + "\\Design");
//        } catch (IOException ex) {
//            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jButton8ActionPerformed

    // --- реакция на события меню ---
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        setDesDirPath();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
//---------- Изменение пути к рабочему каталогу --------------------------
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        setDesDirPath();
    }//GEN-LAST:event_jButton9ActionPerformed

    // --- событие на закрытие окна ---
    private void jFrameTableWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFrameTableWindowClosed
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
    }//GEN-LAST:event_jFrameTableWindowClosed
// ------ Выбор абонента, для которого будет загружаться ексель-файл
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        globVar.abonent = (String) jComboBox2.getSelectedItem();
    }//GEN-LAST:event_jComboBox2ActionPerformed
//-------------- Создание нового абонента ---------------------
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if(!Tools.isDB()) return;
        JFrame addAb = new AddAbonent(jComboBox2);
        addAb.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addAb.setTitle("Новый абонент");
        addAb.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
	if (evt.getClickCount() == 2) {
            String nameT = jTree1.getSelectionPath().getLastPathComponent().toString();
            showTable(nameT); // вызов метода построения таблицы
	}
    }//GEN-LAST:event_jTree1MouseClicked

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_formFocusLost
//----------- переименование .type файлов ----------------
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if(!Tools.isDesDir()) return;
        try {
            int ret = FileManager.renameTypeFile(globVar.desDir+"\\Design");
            JOptionPane.showMessageDialog(null, "Переименовано " + ret + " файлов");
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
//------ Смена БД -----
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JFrame changeDB = new ChangeDB(jTree1,this);
        changeDB.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        changeDB.setTitle("Сменить БД");
        changeDB.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    public ComboBoxModel getComboBoxModel() { // функция для создания списка из таблиц базы
        if (globVar.DB == null) {
            return null;
        }
        Iterator<String> iter_list_table = listDropT.iterator();

        String listTable = "";
        int l = 0;
        while (iter_list_table.hasNext()) {
            iter_list_table.next();
            //System.out.print(l);
            ++l;
        }
        String[] listarrayTable = new String[l];
        l = 0;

        iter_list_table = listDropT.iterator();
        while (iter_list_table.hasNext()) {
            String res = iter_list_table.next();
            listarrayTable[l] = res;
            ++l;
        }
        return new DefaultComboBoxModel(listarrayTable);
    }

    public ComboBoxModel getComboBoxModelAbonents(){ // создания списка абонентов
        if (globVar.DB == null) {
            return null;
        }
        ArrayList<String[]> abList = globVar.DB.getAbonentArray();
        String[] itemList = {""};
        if (abList != null && !abList.isEmpty()) {
            itemList = new String[abList.size()];
            for (int i = 0; i < abList.size(); i++) {
                itemList[i] = abList.get(i)[1];
            }
        }
        return new DefaultComboBoxModel(itemList);
    }

    // --- структура построения для дерева ---
    public static DefaultTreeModel getModelTreeNZ(){
        if (globVar.DB == null) {
            return null;
        }
        globVar.DB.createAbonentTable();
        ArrayList<String[]> listAbonent = globVar.DB.getAbonentArray(); // лист абонентов [0] только первый запрос 1
        ArrayList<String> listTableBd = globVar.DB.getListTable();
        final String ROOT = "дерево сигналов";

        // Создание древовидной структуры
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);
        // Ветви первого уровня
        for (String[] s : listAbonent) {
            String nameBranch = s[1]; //  1 это префикс
            DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode(nameBranch);
            // Добавление ветвей к корневой записи
            root.add(firstNode);
            // Добавление листьев
            for (String sheet : listTableBd) {
                // Патерн добавления того или иного совпадения по имени абонента
                Pattern pattern1 = Pattern.compile("^" + nameBranch + "(.*)$");
                Matcher matcher1 = pattern1.matcher(sheet);
                //String sheetPatern = ""; // годы месяцы число
                if (matcher1.matches()) {
                    //sheetPatern = matcher1.group(1);
                    firstNode.add(new DefaultMutableTreeNode(sheet, false));
                }

            }

        }
        // Создание стандартной модели и дерево
        return new DefaultTreeModel(root, true);
    }

    // --- метод отображения фрейма таблицы ---
    public void showTable(String table){
        if(!Tools.isDB()) return;
        if(!globVar.DB.isTable(table)) return;
        TableDB tdb = new TableDB(table);
        tdb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        tdb.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JFrame jFrameTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    private boolean exeptCol(String s, String[] exCol) {
        for(String e: exCol) if(e.equals(s)) return true;
        return false;
    }
    
    private void setDesDirPath(){
        JFileChooser fileload = new JFileChooser(new File(globVar.desDir));
        fileload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//эта строка отвечает за путь файла
        //int x = fileload.showOpenDialog(this);//эта строка отвечает за само открытие
        if (fileload.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String newPath = fileload.getSelectedFile().getCanonicalPath();
                if (!newPath.equals(globVar.desDir)) {
                    XMLSAX cfgSax = new XMLSAX();
                    Node cfgSaxRoot = cfgSax.readDocument("Config.xml");
                    Node desDir = cfgSax.returnFirstFinedNode(cfgSaxRoot, "DesignDir");
                    desDir.setTextContent(newPath);
                    cfgSax.writeDocument("Config.xml");
                    globVar.desDir = newPath;
                }
                jTextField1.setText(globVar.desDir);
                // TODO add your handling code here:
            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

// Класс Слушатель выделения узла в дереве
class SelectionListener implements TreeSelectionListener {
//
    Main_JPanel mainPanel;
//
    SelectionListener(Main_JPanel aThis) {
        mainPanel = aThis;
    }
//
    public void valueChanged(TreeSelectionEvent e) {
    }
}
