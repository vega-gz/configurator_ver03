package FrameCreate;

import DataBaseTools.DataBase;
import DataBaseTools.MergeBases;
import FrameCreate.*;
import Main.ProgressBar;
import ReadWriteExcel.RWExcel;
import ReadWriteExcel.UnloadExcel;
import Settings.CreateFTable;
import SetupSignals.AllSetingsSignalFromeDB;
import SetupSignals.ConfigSig;
import SetupSignals.ConfigSigDB;
import SetupSignals.ConfigSigExcell;
import SetupSignals.ConfigSigStorageInterface;
import Table.TableTools;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.FileManager;
import Tools.LoggerFile;
import Tools.LoggerInterface;
import Tools.Tools;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.w3c.dom.Node;

public final class Main_JPanel extends javax.swing.JFrame {
    String url, nameProject, user, pass;
    LoggerInterface loggerFile = new LoggerFile();
    ArrayList<String> listDropT;
    XMLSAX createXMLSax = new XMLSAX();
    TableTools tt=new TableTools();
    String filepatch, type;
    
    ProgressBar pb = null;
    DefaultListModel listModel = new DefaultListModel(); // модель списка баз


    public Main_JPanel() {
        Tools.isDesDir();
        globVar.DB = new DataBase();
       
        initComponents();
        jTextField1.setText(globVar.desDir);
        if (Tools.isDB()) {
            initMyComponent();
        }
        
    }

    public void initMyComponent() {
        jTextField1.setText(" " + globVar.desDir);
        jMenuItem16.setEnabled(false);
        jMenuItem17.setEnabled(false);
        jMenu12.setEnabled(false);
        listDropT = globVar.DB.getListTable(); // получи список таблиц при включении
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        DataBase.createAbonentTable();//
        jComboBox2.setModel(getComboBoxModelAbonents()); // абоненты из базы
        globVar.abonent = jComboBox2.getItemAt(0);
        this.setTitle("Текущая база: " + globVar.dbURL + globVar.currentBase); // установить заголовок
        try {
            this.setIconImage(ImageIO.read(new File("icon.png"))); // иконка на панель и в угол
        } catch (IOException ex) {
            loggerFile.writeLog("проблема с иконкой программы");
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
        jDialog_ListBase = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jDialog_AskedPassAdmin = new javax.swing.JDialog();
        jTextFieldLoginAdminPanel = new javax.swing.JTextField();
        jTextFieldjPassAdminPanel = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        // прикручиваем нашу модель дерева методом getModelTreeNZ()
        jTree1 = new javax.swing.JTree(getModelTreeNZ());
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<String>();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuLoger = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem_AnotherBase = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();

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

        jDialog_ListBase.setTitle("Список баз");

        jList1.setModel(listModel);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jDialog_ListBaseLayout = new javax.swing.GroupLayout(jDialog_ListBase.getContentPane());
        jDialog_ListBase.getContentPane().setLayout(jDialog_ListBaseLayout);
        jDialog_ListBaseLayout.setHorizontalGroup(
            jDialog_ListBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_ListBaseLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(199, Short.MAX_VALUE))
        );
        jDialog_ListBaseLayout.setVerticalGroup(
            jDialog_ListBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
        );

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList2);

        jMenuItem5.setText("jMenuItem5");

        jMenu10.setText("jMenu10");

        jTextFieldLoginAdminPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLoginAdminPanelActionPerformed(evt);
            }
        });

        jTextFieldjPassAdminPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldjPassAdminPanelActionPerformed(evt);
            }
        });

        jButton1.setText("Вход");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Отмена");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setText("login");

        jLabel11.setText("pass");

        javax.swing.GroupLayout jDialog_AskedPassAdminLayout = new javax.swing.GroupLayout(jDialog_AskedPassAdmin.getContentPane());
        jDialog_AskedPassAdmin.getContentPane().setLayout(jDialog_AskedPassAdminLayout);
        jDialog_AskedPassAdminLayout.setHorizontalGroup(
            jDialog_AskedPassAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_AskedPassAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_AskedPassAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldLoginAdminPanel)
                    .addComponent(jTextFieldjPassAdminPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(jDialog_AskedPassAdminLayout.createSequentialGroup()
                        .addGroup(jDialog_AskedPassAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addGroup(jDialog_AskedPassAdminLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog_AskedPassAdminLayout.setVerticalGroup(
            jDialog_AskedPassAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_AskedPassAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldLoginAdminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldjPassAdminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_AskedPassAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/VegaConLogo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Каталог проекта: ");

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(0, 102, 204));
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(null);
        jTextField1.setMargin(new java.awt.Insets(4, 8, 4, 4));
        jTextField1.setName(""); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        // слушатель выделения
        jTree1.addTreeSelectionListener(new SelectionListener(this));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addGap(71, 71, 71))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 303, Short.MAX_VALUE))
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "item1", "item2", "item3", "item4" }));
        jComboBox1.setToolTipText("");
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton7.setText("исполнительный механизм");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel1.setText("Неактивные кнопки. Функции в разработке");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("v.12.15");

        jLabel3.setText("Текущий абонент");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton3.setText("Загрузить Excel ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("Выгрузить в Excel");
        jButton6.setRequestFocusEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton6))
                .addGap(29, 29, 29)
                .addComponent(jLabel9)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jMenuBar1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jMenu2.setText("Меню");

        jMenu6.setText("Генерация");

        jMenuItem11.setText("OPC сервера");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuItem12.setText("Архивы");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem12);

        jMenu2.add(jMenu6);

        jMenu3.setText("Утилиты");

        jMenuItem6.setText("Переименовать *.int файлы ");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem10.setText("Переименовать файлы opcServer");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

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

        jMenuItem20.setText("Загрузка уставок из Excel файла");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem20);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        jMenu11.setText("Опции");

        jMenuItem16.setText("Замена имен в файлах через базу данных");
        jMenu11.add(jMenuItem16);

        jMenuItem17.setText("Добавить структуру сигналов");
        jMenu11.add(jMenuItem17);

        jMenuBar1.add(jMenu11);

        jMenu7.setText("Настройки");

        jMenu1.setText("Настройки проекта");

        jMenu5.setText("Работа с таблицами");

        jMenu9.setText("Создать таблицу");

        jMenuItem13.setText("Новую");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem13);

        jMenu5.add(jMenu9);

        jMenuItem7.setText("Инспекция таблиц");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenu1.add(jMenu5);

        jMenuItem4.setText("Абоненты");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem1.setText("Сменить папку проекта");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenu7.add(jMenu1);

        jMenu12.setText("Данные");
        jMenu7.add(jMenu12);

        jMenu13.setText("Таблица");
        jMenu7.add(jMenu13);

        jMenu4.setText("Настройки базы данных");

        jMenuItem3.setText("Сменить базу данных");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem8.setText("Создать новую базу данных");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setText("Список баз данных текущего сервера");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

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
        jMenu4.add(jMenuItem_AnotherBase);

        jMenuItem19.setText("Администрироване базы данных");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenu7.add(jMenu4);

        jMenuItem18.setText("Локальные пользователи");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem18);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("Помощь");

        jMenuItem14.setText("Инструкция по эксплуатации");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem14);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        
        File excel = FileManager.getChoiserExcelFile(); //  Отдельный метод выбора Excel файла
        if (excel == null) return;
        
        
        pb = new ProgressBar();
        pb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pb.setTitle(processName);
        pb.setVisible(true);
        
        String pathFileExcell = excel.getPath(); // Для лямбды в отдельный блок
        
        DoIt di = () -> {
            String ret = null;
            RWExcel readWriterWxcell = new RWExcel();
            ret = readWriterWxcell.ReadExelFromConfig(pathFileExcell, null, null, pb.jProgressBar1); // вызов фукции с формированием базы по файлу конфигурации
            
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

    // --- реакция на события меню ---
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        setDesDirPath();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // --- событие на закрытие окна ---
    private void jFrameTableWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFrameTableWindowClosed
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
    }//GEN-LAST:event_jFrameTableWindowClosed
    // ------ Выбор абонента, для которого будет загружаться ексель-файл
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        globVar.abonent = (String) jComboBox2.getSelectedItem();
       
    }//GEN-LAST:event_jComboBox2ActionPerformed
//------слушатель нажатя мышки по дереву JTree-----
    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        if (evt.getClickCount() == 2) {
            if (jTree1.getSelectionPath() !=null && jTree1.getSelectionPath().getLastPathComponent() != null) {
                String nameT = jTree1.getSelectionPath().getLastPathComponent().toString();
                showTable(nameT); // вызов метода построения таблицы
            }
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
        ArrayList<String> listURl = new ArrayList<>();
        // максимум первых сто адресов базы
        for (int i = 0; i < 100; ++i) {
            String urlDB = "URL";
            if (i != 0) {
                urlDB += Integer.toString(i);
            }
            if (sax.returnFirstFinedNode(urlDB) != null) {
                String url = sax.returnFirstFinedNode(urlDB).getTextContent();
                StringBuilder sb = new StringBuilder(url);
                String delU = "jdbc:postgresql://"; // что удаляем
                sb.deleteCharAt(url.length() - 1); // удаляем последний символ "/"
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

            UnloadExcel loadread = new UnloadExcel();
            boolean workUploadExcel = loadread.runUnloadExcel(globVar.abonent, pb);
            if (workUploadExcel) {
                JOptionPane.showMessageDialog(null, "Выгрузка в Excel завершена");
            } else {
                JOptionPane.showMessageDialog(null, "Ошибки в выгрузке.");
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
        if (!globVar.DB.isTable("Abonents")) {
            JOptionPane.showMessageDialog(null, "Таблицы абонентов в текущей БД нет");
            return;
        }
        
        AbonentFrame sf = new AbonentFrame("Abonents",jComboBox2);
        sf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        sf.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem4ActionPerformed

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
            String textField6 = jTextField6.getText();//.toLowerCase(); // в нижний регистр
            int code = globVar.DB.createBase(textField6);
            if (code == 1) {
                JOptionPane.showMessageDialog(null, "База " + textField6 + " успешно создана."); //сообщение

                // занести базу в список
                {
                    XMLSAX sax = new XMLSAX();
                    Node root = sax.readDocument(globVar.mainConf);
                    Node tmp = sax.returnFirstFinedNode(root, globVar.nodeDBid);
                    int sumBase = 0;
                    ArrayList<String> aList = new ArrayList<>();
                    for (int i = 1; tmp != null; i++) {
                        sumBase = i;
                        aList.add(tmp.getTextContent());
                        tmp = sax.returnFirstFinedNode(root, globVar.nodeDBid + i);
                    }
                    Node Settings = sax.returnFirstFinedNode(root, "Settings");
                    Node naodeB = sax.insertChildNode(Settings, globVar.nodeDBid + Integer.toString(sumBase));
                    naodeB.setTextContent(textField6); // метод самой ноды
                    sax.writeDocument();
                }
            } else {
                System.out.println("Error create DB");
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

    // --- вывод баз подключенного сервера ---
    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        listModel.clear();
        ArrayList<String> listDB = globVar.DB.getListBase();
        for (int i = 0; i < listDB.size(); i++) {
            listModel.add(i, listDB.get(i));
        }

        //validate();
        jDialog_ListBase.setSize(500, 200);
        jDialog_ListBase.setLocationRelativeTo(null); // по центру экрана
        jDialog_ListBase.setVisible(true);


    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        if (!Tools.isDesDir()) {
            return;
        }

        try {
            int ret = FileManager.ChangeOPCNameFile(globVar.desDir);
            JOptionPane.showMessageDialog(null, "Переименовано " + ret + " файлов");
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        JFrame createTable=new CreateFTable(jTree1);
        createTable.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createTable.setTitle("Окно создания таблиц");
        createTable.setVisible(true);
        
        // JOptionPane.showMessageDialog(null, "Таблица создана");
    }//GEN-LAST:event_jMenuItem13ActionPerformed
//---формирование OPC серверов----
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
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
    }//GEN-LAST:event_jMenuItem11ActionPerformed
//--Редактирование конфигурации архивов----
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
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

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed
//вызов файла с описанием конфигуратора
    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            Desktop desktop = Desktop.getDesktop();
            File file=new File(globVar.myDir+ File.separator +"ОписаниеКонфигуратораПроектов.html");
             URI url=file.toURI();
            desktop.browse(url);
        } catch (IOException ex) {
            Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        UserFrame userframe = new UserFrame();
        TableTools.setFrameListener(userframe, null, null, null); // слушателей фреймов
        userframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userframe.setVisible(true);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        //меню администрирования
        jDialog_AskedPassAdmin.setSize(500, 200);
        jDialog_AskedPassAdmin.setLocationRelativeTo(null); // по центру экрана
        jDialog_AskedPassAdmin.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // кнопка логина админской панели  
        getAdminPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldLoginAdminPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLoginAdminPanelActionPerformed
        getAdminPanel();
    }//GEN-LAST:event_jTextFieldLoginAdminPanelActionPerformed

    private void jTextFieldjPassAdminPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldjPassAdminPanelActionPerformed
        getAdminPanel();
    }//GEN-LAST:event_jTextFieldjPassAdminPanelActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDialog_AskedPassAdmin.setVisible(false);
        jTextFieldLoginAdminPanel.setText(null);
        jTextFieldjPassAdminPanel.setText(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        /*пункт меню загрузки данных для уставок из Excel*/
        File excel = FileManager.getChoiserExcelFile(); //  Отдельный метод выбора Excel файла
        if (excel == null) return;
        
        DoIt di = () -> {
            
            ConfigSigStorageInterface configSigStorageExcell = new ConfigSigExcell(excel.getPath());
            ArrayList<ConfigSig> configsSignal = configSigStorageExcell.getConfigsSignal();
            
            if (configsSignal != null) {
                int casedial = JOptionPane.showConfirmDialog(null, "Удалить все существующие и сформировать новые?\n"
                        + "Нет, то будут добавлены или замены существующие."); // сообщение с выбором
                ConfigSigStorageInterface configSigStorageInterface = new ConfigSigDB();
                switch (casedial) {//0 - yes, 1 - no, 2 - cancel
                    case 0:
                        ConfigSigDB configSigDB = (ConfigSigDB) configSigStorageInterface;
                        configSigDB.clearDBSetups();

                        break;
                    case 1:

                        break;
                    case 2:
                        return;
                    //break;
                    default:
                        break;
                }
                for (ConfigSig configSignal : configsSignal) {
                    configSigStorageInterface.addSignal(configSignal);
                }
                JOptionPane.showMessageDialog(null, "Загруженны уставки в количестве " + configsSignal.size());
            } else {
                ConfigSigExcell configSigExcell = (ConfigSigExcell) configSigStorageExcell;
                JOptionPane.showMessageDialog(null, "При загрузке были ошибки. См. файл 'configurer.log'\n"
                        + " проверте именование столбцов загрузки\n"
                        + configSigExcell.getNameColumnSetings().toString());
            }
        };
        BackgroundThread bt = new BackgroundThread("Execude settings to signals", di);
        bt.start();
        
    }//GEN-LAST:event_jMenuItem20ActionPerformed
    
    private void getAdminPanel(){
    // Вход в админскую панель
        String getLoginPanel = jTextFieldLoginAdminPanel.getText();
        String getPassPanel = jTextFieldjPassAdminPanel.getText();
        if(getLoginPanel.equals("1") & getPassPanel.equals("1")){ // пасс 1 1 
            jDialog_AskedPassAdmin.setVisible(false);
            jTextFieldLoginAdminPanel.setText(null);
            jTextFieldjPassAdminPanel.setText(null);
            AdminFrame adminFrame = new AdminFrame();
            adminFrame.setVisible(true);
            TableTools.setFrameListener(adminFrame, null, null, null); // слушателей фреймов
        }else{
            JOptionPane.showMessageDialog(null, "Не верный логин или пароль."); //сообщение
        }
    
    }
    public ArrayList<String> getTableNode(){//метод получения нод для построения таблиц AI AO DI DO  и тд
        ArrayList<String> nodeTable=new ArrayList<>();
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);
        for (Node n : nList){
           String nodeName = globVar.sax.getDataAttr(n, "excelSheetName");
           nodeTable.add(nodeName);
        }
        return nodeTable;
    }
    
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
        
        Arrays.sort(listarrayTable);

                
        return new DefaultComboBoxModel(listarrayTable);
    }

    // -- создания списка абонентов  для передачи в ComboBox -- 
    public ComboBoxModel getComboBoxModelAbonents() { 
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
         String comment;
        if (!globVar.DB.isConnectOK()) {
            return null;
        }
        globVar.DB.createAbonentTable();
        ArrayList<String[]> listAbonent = globVar.DB.getAbonentArray(); // лист абонентов [0] только первый запрос 1
        ArrayList<String> listTableBd = globVar.DB.getListTable();//список таблиц в базе, НО НЕ В ДЕРЕВЕ
        final String ROOT = "Абоненты";
        // Создание древовидной структуры
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);
        // Ветви первого уровня(добавили абонента)
        for (String[] s : listAbonent) {
            String nameBranch = s[1]; //  1 это префикс(в нашем случае это имя абонента в структуре GPA KC  и тд)
            DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode(nameBranch);
            // Добавление ветвей к корневой записи
            root.add(firstNode);
            // Добавление листьев(добавление таблиц)
            for (String sheet : listTableBd) {
                // Патерн добавления того или иного совпадения по имени абонента
                Pattern pattern1 = Pattern.compile("^" + nameBranch + "(.*)$");
                Matcher matcher1 = pattern1.matcher(sheet);
                //String sheetPatern = ""; // годы месяцы число
                   comment=globVar.DB.getCommentTable(sheet);
                if (matcher1.matches()) {
                    //sheetPatern = matcher1.group(1);
                    
                     firstNode.add(new DefaultMutableTreeNode(comment+"("+sheet+")", false));//здесь у нас добавление имени в дерево
                   // firstNode.add(new DefaultMutableTreeNode(sheet, false));//здесь у нас добавление имени в дерево
                }

            }

        }
        // Создание стандартной модели и дерево
        return new DefaultTreeModel(root, true);
    }
    // --- метод отображения фрейма таблицы ---
    protected void showTable(String table) {
        if (!Tools.isDB()) {//проверка ,есть ли подключение к базе
            return;
        }
        if(table.lastIndexOf("(")!=-1){//проверка,с комментарием ли таблица
           table=table.substring(table.lastIndexOf("(")+1, table.lastIndexOf(")"));
        }
              
         
        if (!globVar.DB.isTable(table)) {//проверка.есть ли таблица в базе с заданным именем
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton2_CreateDB;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog_AskedPassAdmin;
    private javax.swing.JDialog jDialog_ListBase;
    private javax.swing.JDialog jDialog_createBase;
    private javax.swing.JFrame jFrameTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItem_AnotherBase;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextFieldLoginAdminPanel;
    private javax.swing.JTextField jTextFieldjPassAdminPanel;
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
    //выбор пути к папке проекта
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
                    globVar.desDir = newPath;
                    if (!Tools.isDesDir()) {
                       return;
                    }
                    XMLSAX cfgSax = new XMLSAX();
                    Node cfgSaxRoot = cfgSax.readDocument("Config.xml");
                    Node desDir = cfgSax.returnFirstFinedNode(cfgSaxRoot, "DesignDir");
                    desDir.setTextContent(newPath);
                    cfgSax.writeDocument("Config.xml");
                    
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

