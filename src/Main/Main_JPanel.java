package Main;

import FrameCreate.*;

import java.awt.Toolkit;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import FrameCreate.CreateFrame;

import ReadWriteExcel.RWExcel;
import DataBaseConnect.StructSelectData;
import DataBaseConnect.DataBase;
import XMLTools.XMLSAX;
import java.awt.Dimension;
import DataBaseConnect.*;
import FileConfigWork.SignalTypeToBase;
import ReadWriteExcel.WriteXMLsignals;
import fileTools.FileManager;
import globalData.globVar;

/**
 *
 * @author cherepanov
 */
public class Main_JPanel extends javax.swing.JFrame {

    String APurl = "jdbc:postgresql://172.16.35.25:5432/test08_DB";
    String url, nameProject, user, pass;
   
    RWExcel excel = new RWExcel();
    String path;
    DataBase DB = DataBase.getInstance();
    public String signal;
    ArrayList<String> listDropT = new ArrayList();
    
    

    XMLSAX createXMLSax = new XMLSAX();
    int filepath;
    String filepatch, type;
    String nameSignal1, nameSignal2, nameSignal3, nameSignal4, nameSignal5,
            UUID_Type1, UUID_Type2, UUID_Type3, UUID_Type4, UUID_Type5,
            UUID_Parent1, UUID_Parent2, UUID_Parent3, UUID_Parent4, UUIDParent5;

    private final String UUIDType_AI_ToProcessing = "187F76AE49C59E950138DDA3067101D0";//уиды типов
    private final String UUIDType_AI_Settings = "668FE9B94A603427C8EDBBB8917A7594";
    private final String UUID_AI_OnlyToHMI = "3F156B284DE592D62F031C9791BF07EF";
    private final String UUID_AI_FromProcessing = "2318F94F4C3CEA2681DE4C8C432A66E9";
    private final String UUID_DI_FromProcessing = "9F0EDD3143DC10ACF03CBA8C4A870F2D";
    private final String UUID_DI_Settings = "94C521C642227325371AE7BCC363E527";
    private final String UUID_DI_ToProcessing = "55B37D104E6E69A8BD7466B4968651A1";
    private final String UUID_AO_FromHMI = "28BBA2F94E53F5E7D050CBA8820F76E4";
    private final String UUID_AO_ToHMI = "D225A65348D2770B341BAC9762DA1766";
    private final String UUID_DO_FromHMI = "032CB0403C4B8FB66F9B68B2E5D50373";
    private final String UUID_DO_ToHMI = "7474D14B284DB0A574420580A1FFD7C0";

    private final String UUID_Parent_AI_FromProcessing = "D48C3F1549ACD994A366A1A51FE00772";
    private final String UUID_Parent_AI_OnlyToHMI = "ABDF10D048FB61AB90F474996CF9B3B2";
    private final String UUID_Parent_AI_Settings = "E10A238347B7C26C7C1FDFA77F91B1F5";
    private final String UUID_Parent_AI_ToProcessing = "2BDAC6F849D13DED3A2A5B9190720FD7";
    private final String UUID_Parent_AO_FromHMI = "136536604B0AD5911BA545A82E7DF913";
    private final String UUID_Parent_AO_ToHMI = "00B934F5461BB0C8938D42A9118D2143";
    private final String UUID_Parent_DI_FromProcessing = "2694959C4B5180695BAA0E9209463FE3";
    private final String UUID_Parent_DI_Setting = "9D3CCA014F76318C4B750981ED2CEA67";
    private final String UUID_Parent_DI_ToProcessing = "2A86C1F64D326C195FE5CB898889601B";
    private final String UUID_Parent_DO_FromHMI = "E151BCB02540B85F326AF1B1DD593EE6";
    private final String UUID_Parent_DO_ToHMI = "150F53385749F03C0BB1E29A7ED64D95";

    public Main_JPanel() {
        initComponents();
        jTextField1.setText(globVar.desDir);
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        this.setTitle("Текущая база:" + globVar.currentBase + ", путь: " + globVar.PathToProject); // установить заголовок
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jProgressBar1 = new javax.swing.JProgressBar();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jFrameTable = new javax.swing.JFrame();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

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

        jButton1.setText("Создать новый проект");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Загрузить Excel файл");
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

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton4.setText("Отобразить список");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Сгенерировать сигнал");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Очистить ");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
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

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem1.setText("сменить папку проекта");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9)
                        .addComponent(jLabel2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton5)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton6)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CreateFrame frame = new CreateFrame(url, nameProject, user, pass);//вызываем второре окно для записи конф файла      
    }//GEN-LAST:event_jButton1ActionPerformed

    // --- Реакция кнопки загрузак Excel --
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fileopen = new JFileChooser(globVar.desDir);
        int ren = fileopen.showDialog(null, "DownloadToBase");
        if (ren == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();// выбираем файл из каталога

            path = file.toString();
            excel.setPatchF(path);
          int casedial = JOptionPane.showConfirmDialog(null, "Загрузить в базу используя конфигурационный файл?\n Выбрав No файл загрузиться полностью."); // сообщение с выбором
            switch (casedial) {
                case 0: 
                    Main.fillBaseConfig(file.getPath()); // вызов фукции с формированием базы по файлу конфигурации
                    break;
                case 1:
                    Main.fillBaseAlldata(file.getPath()); // Заполнение базы полностью из файла
                    break;
                case 2:
                    System.out.println(casedial);
                    break;
                default:
                    System.out.println(casedial);
                    break;
            }
        }
        jComboBox1.setModel(getComboBoxModel());//если мы сделам ваот так чтобыникто не узнал

        JOptionPane.showMessageDialog(null, "Загрузка в базу завершена!");


    }//GEN-LAST:event_jButton3ActionPerformed

    // --- Метод реагирования на выбор поля из списка таблиц ---
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selectT = (String) jComboBox1.getSelectedItem();
        //DB.connectionToBase();
        List<String> listColumn = DB.selectColumns(selectT);
        ArrayList<String> columns = new ArrayList<>();
        String tmpStr = " ";
        for (String s : listColumn) { // ограничение что не выводим из столбцов
            String[] remCol = {"UUID", "uuid"}; // список исключений
            boolean find = false;
            for (String rC : remCol) {
                if (rC.equals(s)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                find = false;
                continue;
            } else {
                columns.add(s); // конечный список столбцов к запросу базы
            }
        }      
        //System.out.println(tmpStr);
        jTextArea1.setText(tmpStr);

        if (selectT.equals("dies_ai")) {
            signal = "dies_ai";
        } else if (selectT.equals("dies_ao")) {
            signal = "dies_ao";
        } else if (selectT.equals("dies_do")) {
            signal = "dies_do";
        } else if (selectT.equals("dies_di")) {
            signal = "dies_di";
        }

        jTextArea1.setText((String) jComboBox1.getSelectedItem());// выводим что выбрали 
        String selectElem = (String) jComboBox1.getSelectedItem();//j String комбо бок
        StructSelectData.setnTable(selectElem); // вносим в структуру название таблицы для печати того же файла Максима  LUA
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;//это размещение 
        FrameTabel frameTable = new FrameTabel(selectT, columns); // Вызов класса Название таблицы и данные для нее
        jFrameTable.add(frameTable); // с заголовком имя таблицы
        jFrameTable.setTitle(selectT + " " + DB.getCommentTable(selectT)); // установить заголовок имя таблицы и если есть ее коммент
        jFrameTable.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
        jFrameTable.setContentPane(frameTable); // Передаем нашу форму
        jFrameTable.setVisible(true);
        
    }//GEN-LAST:event_jComboBox1ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //  DB.connectionToBase(url,pass,user);
        listDropT = DB.getListTable();
        Iterator<String> iter_list_table = listDropT.iterator();
        String listTable = "";

        while (iter_list_table.hasNext()) {

            listTable += iter_list_table.next() + " \n";
        }

        jTextArea1.setText(listTable);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        JFileChooser fileload = new JFileChooser();
        fileload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//эта строка отвечает за путь файла
        filepath = fileload.showOpenDialog(this);//эта строка отвечает за само открытие
        if (filepath == JFileChooser.APPROVE_OPTION) {
                try {
                    filepatch = fileload.getSelectedFile().getCanonicalPath();
                } catch (IOException ex) {
                    Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
                    FileManager.loggerConstructor("error testing 44352");

                }


        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // workbase.connectionToBase();
        if (!listDropT.isEmpty()) {  // если есть что удалять передаем лист в обработчик баз
            DB.dropTable(listDropT);
        } else;
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DB = DataBase.getInstance();// подключится к базе конфигом другого не дано
        String nameBD = DB.getCurrentNameBase();
        String userBD = DB.getCurrentUser();
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
        String message = null; 
        if(nameBD != null){
            JOptionPane.showMessageDialog(null, "Подключено к базе "+ nameBD +" пользователем " +userBD);
        }
        

    }//GEN-LAST:event_jButton2ActionPerformed
    
    // --- Кнопка вызова окна с исполнительным механизмом ---
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        ExecutiveMechanism frameExecutiveMechanism = new ExecutiveMechanism(DB); // И передаем туда управление базой
        frameExecutiveMechanism.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
        frameExecutiveMechanism.setDefaultCloseOperation(frameExecutiveMechanism.DISPOSE_ON_CLOSE); // Закрываем окно а не приложение
        frameExecutiveMechanism.setVisible(true); 

    }//GEN-LAST:event_jButton7ActionPerformed

    
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
        int ren = fileopen.showDialog(null, ".type");
        if (ren == JFileChooser.APPROVE_OPTION) {
            
            File file = fileopen.getSelectedFile();// выбираем файл из каталога
            String pathFileType = file.toString();
            //System.out.println(file.getNaтяme());
            if (pathFileType.endsWith(".type")){
                new SignalTypeToBase(pathFileType);
            }else JOptionPane.showMessageDialog(null, "Расширение файла не .type"); // Это сообщение
        }
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
    }//GEN-LAST:event_jButton8ActionPerformed
    
    // --- реакция на события меню ---
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int ren = jFileChooser1.showDialog(null, "папка с проектом");
        if (ren == JFileChooser.APPROVE_OPTION) {
                globVar.desDir = jFileChooser1.getSelectedFile().toString(); // установить новый путь 
                this.setTitle("Текущая база:" + globVar.currentBase + " путь " + globVar.desDir); // установить заголовок
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JFileChooser fileload = new JFileChooser(new File(globVar.desDir));
        fileload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//эта строка отвечает за путь файла
        //int x = fileload.showOpenDialog(this);//эта строка отвечает за само открытие
        if (fileload.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String newPath = fileload.getSelectedFile().getCanonicalPath();
                if(!newPath.equals(globVar.desDir)){
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
    }//GEN-LAST:event_jButton9ActionPerformed

    // --- событие на закрытие окна ---
    private void jFrameTableWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jFrameTableWindowClosed
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
    }//GEN-LAST:event_jFrameTableWindowClosed

    public ComboBoxModel getComboBoxModel() // функция для создания списка из таблиц базы
    {

        //String db = "test08_DB";
        //workbase.connectionToBase();
        listDropT = DB.getListTable();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFrame jFrameTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
