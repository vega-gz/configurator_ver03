package Main;

import DataBaseTools.DataBase;
import DataBaseTools.MergeBases;
import FrameCreate.*;
import ReadWriteExcel.RWExcel;
import ReadWriteExcel.UnloadExcel;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.FileManager;
import Tools.TableTools;
import Tools.Tools;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.w3c.dom.Node;

public final class Main_JPanel extends javax.swing.JFrame {

    String url, nameProject, user, pass;

    ArrayList<String> listDropT;
    XMLSAX createXMLSax = new XMLSAX();
    String filepatch, type;
    File excel = null;
    ProgressBar pb = null;

    public Main_JPanel() {
        globVar.DB = new DataBase();
        initComponents();
        jTextField1.setText(globVar.desDir);
        if (Tools.isDB()) {
            initMyComponent();
        }
        Tools.isDesDir();
    }

    public void initMyComponent() {
        jTextField1.setText(" " + globVar.desDir);
        listDropT = globVar.DB.getListTable(); // получи список таблиц при включении
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        DataBase.createAbonentTable();//
        jComboBox2.setModel(getComboBoxModelAbonents()); // абоненты из базы
        globVar.abonent = jComboBox2.getItemAt(0);
        this.setTitle("Текущая база: " + globVar.dbURL + globVar.currentBase); // установить заголовок
        try {
            this.setIconImage(ImageIO.read(new File("icon.png"))); // иконка на панель и в угол
        } catch (IOException ex) {
            FileManager.loggerConstructor("проблема с иконкой программы");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameTable = new javax.swing.JFrame();
        jDialog1 = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox();
        jDialog_createBase = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jButton2_CreateDB = new javax.swing.JButton();
        jButton10_CanceCreatelDB = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<String>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        // прикручиваем нашу модель дерева методом getModelTreeNZ()
        jTree1 = new javax.swing.JTree(getModelTreeNZ());
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuLoger = new javax.swing.JMenuItem();
        jMenuBase = new javax.swing.JMenu();
        jMenuItem_AnotherBase = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

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

        jDialog1.setTitle("Таблица в которую заносим данные");
        jDialog1.setResizable(false);
        jDialog1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jDialog1KeyPressed(evt);
            }
        });

        jLabel4.setText("user:");

        jLabel6.setText("addres_base:");

        jLabel7.setText("pass:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel8.setText("base:");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton12.setText("Подключить");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2)))
                        .addContainerGap())))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12)
                .addContainerGap())
        );

        jDialog_createBase.setTitle("Создание базы");

        jButton2_CreateDB.setText("Create");
        jButton2_CreateDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2_CreateDBActionPerformed(evt);
            }
        });

        jButton10_CanceCreatelDB.setText("Cancel");
        jButton10_CanceCreatelDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10_CanceCreatelDBActionPerformed(evt);
            }
        });

        label1.setText("Название новой базы");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(label1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jButton2_CreateDB, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10_CanceCreatelDB, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(74, Short.MAX_VALUE))
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2_CreateDB)
                    .addComponent(jButton10_CanceCreatelDB))
                .addContainerGap(124, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_createBaseLayout = new javax.swing.GroupLayout(jDialog_createBase.getContentPane());
        jDialog_createBase.getContentPane().setLayout(jDialog_createBaseLayout);
        jDialog_createBaseLayout.setHorizontalGroup(
            jDialog_createBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_createBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog_createBaseLayout.setVerticalGroup(
            jDialog_createBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_createBaseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 102, 204));
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(null);
        jTextField1.setMargin(new java.awt.Insets(4, 8, 4, 4));
        jTextField1.setName(""); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Каталог проекта: ");

        jButton6.setText("Выгрузить в Excel");
        jButton6.setRequestFocusEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jMenuBar1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jMenu2.setText("Настройки");

        jMenuItem1.setText("сменить папку проекта");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Утилиты");

        jMenuItem6.setText("Переименовать *.int файлы ");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem2.setText("Переименовать *.type файлы");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        menuLoger.setText("Просмотр логов");
        menuLoger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLogerActionPerformed(evt);
            }
        });
        jMenu3.add(menuLoger);

        jMenuBase.setText("Работа с базой данных");

        jMenuItem_AnotherBase.setText("Перенос таблиц в другую базу");
        jMenuItem_AnotherBase.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jMenuItem_AnotherBaseComponentAdded(evt);
            }
        });
        jMenuItem_AnotherBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AnotherBaseActionPerformed(evt);
            }
        });
        jMenuBase.add(jMenuItem_AnotherBase);

        jMenuItem3.setText("Сменить БД");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuBase.add(jMenuItem3);

        jMenuItem7.setText("Инспекция таблиц");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenuBase.add(jMenuItem7);

        jMenuItem8.setText("Создать новую базу");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenuBase.add(jMenuItem8);

        jMenu3.add(jMenuBase);

        jMenuBar1.add(jMenu3);

        jMenu1.setText("Проект");

        jMenuItem4.setText("Абоненты");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("ОРС сервера");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButton1)
                                    .addGap(208, 208, 208)
                                    .addComponent(jButton4))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(jButton7)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9)))
                        .addGap(0, 68, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton6)
                            .addComponent(jButton5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton9)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    // --- Реакция кнопки загрузак Excel --
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (!Tools.isDB()) {
            return;
        }
        String processName = "Процесс загрузки данных из Excel";
        if (globVar.processReg.indexOf(processName) >= 0) {
            JOptionPane.showMessageDialog(null, "Запуск нового процесса загрузки в БД данных из файла Excel заблокирован до окончания предыдущей загрузки");
            return;
        }
        int casedial = JOptionPane.showConfirmDialog(null, "Загрузка в БД информации для абонента \"" + globVar.abonent + "\"");
        if (casedial != 0) {
            return;
        }

        JFileChooser fileopen = new JFileChooser(globVar.desDir);
        int ren = fileopen.showDialog(null, "Загрузка данных для " + globVar.abonent);
        if (ren == JFileChooser.APPROVE_OPTION) {
            excel = fileopen.getSelectedFile();// выбираем файл из каталога
        }
        pb = new ProgressBar();
        pb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pb.setTitle(processName);
        pb.setVisible(true);

        DoIt di = () -> {
            String ret = null;
            try {
                ret = RWExcel.ReadExelFromConfig(excel.getPath(), pb.jProgressBar1); // вызов фукции с формированием базы по файлу конфигурации
            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (ret != null) {
                JOptionPane.showMessageDialog(null, "В базу загружены следующие таблицы:" + ret);
                jTree1.setModel(getModelTreeNZ());// обновить дерево
            } else {
                JOptionPane.showMessageDialog(null, "При загрузке были ошибки. См. файл 'configurer.log'");
            }
            pb.setVisible(false);
            globVar.processReg.remove(processName);
        };
        BackgroundThread bt = new BackgroundThread(processName, di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton3ActionPerformed

    // --- Метод реагирования на выбор поля из списка таблиц ---
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        //if(!globVar.DB.isConnectOK()) return;
        String selectT = (String) jComboBox1.getSelectedItem();
        showTable(selectT); // вызов метода построения таблицы 
    }//GEN-LAST:event_jComboBox1ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!Tools.isDB()) {
            return;
        }
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
        if (!Tools.isDB()) {
            return;
        }
        if (!Tools.isDesDir()) {
            return;
        }
        JFrame editArchive = new addArchive();
        editArchive.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        editArchive.setTitle("Редактирование архивов");
        editArchive.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    // --- Кнопка вызова окна с исполнительным механизмом ---
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (!Tools.isDB()) {
            return;
        }
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

//----------- Лишнее. Удалить!!!!!!!!!!!!!!!! -------------------
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

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

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        if (evt.getClickCount() == 2) {
            String nameT = jTree1.getSelectionPath().getLastPathComponent().toString();
            showTable(nameT); // вызов метода построения таблицы
        }
    }//GEN-LAST:event_jTree1MouseClicked
//----------- Лишнее. Удалить!!!!!!!!!!!!!!!! -------------------
    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_formFocusLost
//=================================================================
//----------- переименование .type файлов ----------------
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (!Tools.isDesDir()) {
            return;
        }
        try {
            int ret = FileManager.renameTypeFile(globVar.desDir + File.separator + "Design");
            JOptionPane.showMessageDialog(null, "Переименовано " + ret + " файлов");
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
//------ Смена БД -----
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JFrame changeDB = new ChangeDB(jTree1, this);
        TableTools.setFrameListener(changeDB, null, null, null); // слушателей фреймов
        changeDB.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        changeDB.setTitle("Сменить БД");
        changeDB.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    // обработчик пукт меню Утилит подключение в второй базе.
    private void jMenuItem_AnotherBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AnotherBaseActionPerformed
        XMLSAX sax = new XMLSAX();
        Node root = sax.readDocument("Config.xml");
        ArrayList<String> listURl = new  ArrayList<>();
        // максимум первых сто адресов базы
        for(int i=0; i<100; ++i){
            String urlDB = "URL";
            if(i != 0){
                urlDB += Integer.toString(i);
            }
            if (sax.returnFirstFinedNode(urlDB) != null){
                String url = sax.returnFirstFinedNode(urlDB).getTextContent();
                StringBuilder sb = new StringBuilder(url);
                String delU = "jdbc:postgresql://"; // что удаляем
                sb.deleteCharAt(url.length()-1); // удаляем последний символ "/"
                sb.delete(0, delU.length()); // и удаляем сервесные заголовки
                listURl.add(sb.toString());
            }
        }
        String[] toComboBox = listURl.toArray(new String[listURl.size()]);
        jComboBox3.setModel(new DefaultComboBoxModel(toComboBox)); // подставили значение в комбобокс
        
        jDialog1.setSize(400, 200);
        jDialog1.setLocationRelativeTo(null); // по центру экрана
        //jTextField2.setText("172.16.35.50:5432");
        jTextField5.setText("kln_kc");
        jTextField3.setText("mutonar");
        jTextField4.setText("Solovin2");
        jDialog1.setVisible(true);

    }//GEN-LAST:event_jMenuItem_AnotherBaseActionPerformed
    // обработчик нажатий клавиш Jdialog подключения к второстепенной базе
    private void jDialog1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDialog1KeyPressed
        System.out.println("Press button");
    }//GEN-LAST:event_jDialog1KeyPressed
    // обработчик для всех текстовых полей окна Jdialog1 и кнопки
    private void jTextField_all_ActionPerformed(java.awt.event.ActionEvent evt) {
        String addresSecondDB = jTextField2.getText(); // адрес базы
        String DB = jTextField5.getText(); // имя базы
        String userSecondDB = jTextField3.getText(); // имя пользователя
        String passSecondDB = jTextField4.getText(); // пароль

        // тестовые данные
        /*
         addresSecondDB = "minoro.ru:5432";
         DB = "kln_gpa";
         userSecondDB = "mutonar";
         passSecondDB = "Solovin2";
         */
        //if (!addresSecondDB.equals(null) || !DB.equals(null) || !userSecondDB.equals(null) || !passSecondDB.equals(null)) {
        //System.out.println("Press button field all " + addresSecondDB + DB + userSecondDB + passSecondDB);
        MergeBases mergeDB = new MergeBases(addresSecondDB, DB, userSecondDB, passSecondDB); // вызов класса слияния
        if (mergeDB.connectAnotherDB() == 0) {
             // прикручиваем прогрессБар Льва  
            pb = new ProgressBar();
            pb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            //pb.setUndecorated(true); // не удалить кнопки если окно инициализированно
            //pb.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            pb.setTitle("Обработка даных");
            pb.setVisible(true);
        
            System.out.println("connect base");
            jDialog1.dispose(); // закрываем окошко при удачном подключении
             DoIt di = () -> {
                mergeDB.editBases(pb.jProgressBar1); // запуск метода обработки баз(и передача прогресс бара) и нужно передать данные в Doit
                pb.dispose();
            };
             
            BackgroundThread bt = new BackgroundThread("Pbar", di);
            bt.start();
            
            
        } else {
            JOptionPane.showMessageDialog(null, "Подключение не возможно \n проверьте введеные данные или доступность сервера");
        }
    }
    //--------Выгрузка из БД информации для абонента в Excel-------------
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (!Tools.isDB()) {
            return;
        }
        String processName = "Процесс выгрузки данных в Excel";
        if (globVar.processReg.indexOf(processName) >= 0) {
            JOptionPane.showMessageDialog(null, "Запуск нового процесса загрузки в БД данных из файла Excel заблокирован до окончания предыдущей загрузки");
            return;
        }
        int casedial = JOptionPane.showConfirmDialog(null, "Выгрузка из БД информации для абонента в Excel \"" + globVar.abonent + "\"");
        if (casedial != 0) {
            return;
        }
        pb = new ProgressBar();
        pb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pb.setTitle(processName);
        pb.setVisible(true);
        //выдергиваем имя абонента и передаем в конструктор
        globVar.abonent = (String) jComboBox2.getSelectedItem();
        DoIt di = () -> {
            int ret = 1;
            try {
                UnloadExcel loadread = new UnloadExcel(globVar.abonent);
                JOptionPane.showMessageDialog(null, "Выгрузка в Excel завершена");
            } catch (ParseException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            pb.setVisible(false);
            globVar.processReg.remove(processName);
        };
        BackgroundThread bt = new BackgroundThread(processName, di);
        bt.start();
        globVar.processReg.add(processName);
    }//GEN-LAST:event_jButton6ActionPerformed
    //Меню: Проект/Абоненты
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if(!globVar.DB.isTable("Abonents")){
            JOptionPane.showMessageDialog(null, "Таблицы абонентов в текущей БД нет");
            return;
        }
        SimpleFrame sf = new SimpleFrame("Abonents");
        sf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        sf.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    //Редактирование ОРС серверов и клиентов
    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (!Tools.isDB()) {
            return;
        }
        if (!Tools.isDesDir()) {
            return;
        }
        JFrame addOPC = new addOPCserver();
        addOPC.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addOPC.setTitle("Конфигурирование ОРС серверов");
        addOPC.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed
    //Переименование IntFile
    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (!Tools.isDesDir()) {
            return;
        }
        try {
            int ret = FileManager.renameIntFile(globVar.desDir + File.separator + "Design");
            JOptionPane.showMessageDialog(null, "Переименовано " + ret + " файлов");
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed
//----------- Лишнее. Удалить!!!!!!!!!!!!!!!! -------------------
    private void jMenuItem_AnotherBaseComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jMenuItem_AnotherBaseComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem_AnotherBaseComponentAdded
//===============================================================
    //Работа с удалёнными таблицами
    private void menuLogerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLogerActionPerformed
        LogerViewerFrame lvf = new LogerViewerFrame();
        TableTools.setFrameListener(lvf, null, null, null); // слушателей фреймов
        lvf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lvf.setVisible(true);
    }//GEN-LAST:event_menuLogerActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        jTextField_all_ActionPerformed(evt); // вызов одинакового метода для всех
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        jTextField_all_ActionPerformed(evt);
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        jTextField_all_ActionPerformed(evt);
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        jTextField_all_ActionPerformed(evt);
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        jTextField_all_ActionPerformed(evt);
    }//GEN-LAST:event_jTextField4ActionPerformed

    //  --- пукт инспекции таблиц ---
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        InspectionTablesFrame itf = new InspectionTablesFrame();
        TableTools.setFrameListener(itf, null, null, null); // слушателей фреймов
        itf.setVisible(true);
        itf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //DISPOSE_ON_CLOSE
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    // -- Лист выбора базы в меню при слиянии --
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        jTextField2.setText((String) jComboBox3.getSelectedItem());
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton2_CreateDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2_CreateDBActionPerformed
         if (!jTextField1.getText().equals("")) { // если не пустое надо еще
            String textField6 = jTextField6.getText();
            int code = globVar.DB.createBase(textField6); 
            if(code == 1){
                JOptionPane.showMessageDialog(null, "База "+ textField6 +" успешно создана."); //сообщение
            }
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jButton2_CreateDBActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        jDialog_createBase.setSize(400, 200);
        jDialog_createBase.setLocationRelativeTo(null); // по центру экрана
        jDialog_createBase.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    // --- Кнопка закрытия диалога ---
    private void jButton10_CanceCreatelDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10_CanceCreatelDBActionPerformed
        jDialog_createBase.dispose();
    }//GEN-LAST:event_jButton10_CanceCreatelDBActionPerformed

    public ComboBoxModel getComboBoxModel() { // функция для создания списка из таблиц базы
        if (!globVar.DB.isConnectOK()) {
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

    public ComboBoxModel getComboBoxModelAbonents() { // создания списка абонентов
        if (!globVar.DB.isConnectOK()) {
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
    public static DefaultTreeModel getModelTreeNZ() {
        if (!globVar.DB.isConnectOK()) {
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
    public void showTable(String table) {
        if (!Tools.isDB()) {
            return;
        }
        if (!globVar.DB.isTable(table)) {
            return;
        }
        TableDB tdb = new TableDB(jTree1, table);
        tdb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        tdb.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10_CanceCreatelDB;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2_CreateDB;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog_createBase;
    private javax.swing.JFrame jFrameTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuBase;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem_AnotherBase;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTree jTree1;
    private java.awt.Label label1;
    private javax.swing.JMenuItem menuLoger;
    // End of variables declaration//GEN-END:variables

    private boolean exeptCol(String s, String[] exCol) {
        for (String e : exCol) {
            if (e.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void setDesDirPath() {
        JFileChooser fileload = new JFileChooser(new File(globVar.desDir));
        fileload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//эта строка отвечает за путь файла
        //int x = fileload.showOpenDialog(this);//эта строка отвечает за само открытие
        if (fileload.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String newPath = fileload.getSelectedFile().getCanonicalPath();
                int ldd = newPath.length();
                if ("design".equalsIgnoreCase(newPath.substring(ldd - 6))) {
                    newPath = newPath.substring(0, ldd - 7);
                }
                if (!newPath.equalsIgnoreCase(globVar.desDir)) {
                    XMLSAX cfgSax = new XMLSAX();
                    Node cfgSaxRoot = cfgSax.readDocument("Config.xml");
                    Node desDir = cfgSax.returnFirstFinedNode(cfgSaxRoot, "DesignDir");
                    desDir.setTextContent(newPath);
                    cfgSax.writeDocument("Config.xml");
                    globVar.desDir = newPath;
                }
                jTextField1.setText(" " + globVar.desDir);
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
