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
import ReadWriteExcel.WriteXMLsignals;

/**
 *
 * @author cherepanov
 */
public class Main_JPanel extends javax.swing.JFrame {

    String APurl = "jdbc:postgresql://172.16.35.25:5432/test08_DB";
    String url, nameProject, user, pass;
    //String FILECONFIG = "Config.xml";
    RWExcel excel = new RWExcel();
    String path;
    DataBase DB = DataBase.getInstance();
    public String signal;
    ArrayList<String> listDropT = new ArrayList();
    //ReadConfigFile readConfig = new ReadConfigFile();

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
        jComboBox1.setModel(getComboBoxModel()); // обновить сразу лист таблиц в выбранной базе
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jProgressBar1 = new javax.swing.JProgressBar();
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

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

        jButton2.setText("Подключение к базе");
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

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(24, 24, 24)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton6)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        CreateFrame frame = new CreateFrame(url, nameProject, user, pass);//вызываем второре окно для записи конф файла
        


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
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
                
        System.out.println(tmpStr);
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
        JFrame frame = new JFrame();
        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
        frame.setContentPane(frameTable); // Передаем нашу форму
        frame.setVisible(true);


    }//GEN-LAST:event_jComboBox1ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //  DB.connectionToBase(url,pass,user);
        listDropT = DB.getviewTable();
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
                String filename = fileload.getSelectedFile().getName();//записывает в переменную путь,так что теперь надо обЪяснить методу как его юзать
                try {
                    filepatch = fileload.getSelectedFile().getCanonicalPath();
                } catch (IOException ex) {
                    Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);

                }

                switch (signal) {
                    case "dies_ai":

                        nameSignal1 = "T_GPA_AI_FromProcessing";
                        nameSignal2 = "T_GPA_AI_ToProcessing";
                        nameSignal3 = "T_GPA_AI_Settings";
                        nameSignal4 = "T_GPA_AI_OnlyToHMI";
                        nameSignal5 = "T_GPA_AI_Internal";

                        UUID_Type1 = UUID_AI_FromProcessing;
                        UUID_Type2 = UUIDType_AI_ToProcessing;
                        UUID_Type3 = UUIDType_AI_Settings;
                        UUID_Type4 = UUID_AI_OnlyToHMI;
                        UUID_Type5 = "744A7D367B498F7EF3E4B8BC15643AB2";

                        UUID_Parent1 = UUID_Parent_AI_FromProcessing;
                        UUID_Parent2 = UUID_Parent_AI_ToProcessing;
                        UUID_Parent3 = UUID_Parent_AI_Settings;
                        UUID_Parent4 = UUID_Parent_AI_OnlyToHMI;
                        UUIDParent5 = "006A03A8C74962DC3E0724A92DA854AF";

                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal1, UUID_Type1, UUID_Parent1);//
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal2, UUID_Type2, UUID_Parent2);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal3, UUID_Type3, UUID_Parent3);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal4, UUID_Type4, UUID_Parent4);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal5, UUID_Type5, UUIDParent5);

                        break;
                    case "dies_ao":

                        nameSignal1 = "T_GPA_AO_FromHMI";
                        nameSignal2 = "T_GPA_AO_ToHMI";

                        UUID_Type1 = UUID_AO_FromHMI;
                        UUID_Type2 = UUID_AO_ToHMI;

                        UUID_Parent1 = UUID_Parent_AO_FromHMI;
                        UUID_Parent2 = UUID_Parent_AO_ToHMI;

                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal1, UUID_Type1, UUID_Parent1);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal2, UUID_Type2, UUID_Parent2);

                        break;
                    case "dies_do":

                        nameSignal1 = "T_GPA_DO_FromHMI";
                        nameSignal2 = "T_GPA_DO_ToHMI";

                        UUID_Type1 = UUID_DO_FromHMI;
                        UUID_Type2 = UUID_DO_ToHMI;

                        UUID_Parent1 = UUID_Parent_DO_FromHMI;
                        UUID_Parent2 = UUID_Parent_DO_ToHMI;

                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal1, UUID_Type1, UUID_Parent1);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal2, UUID_Type2, UUID_Parent2);

                        break;
                    case "dies_di":

                        nameSignal1 = "T_GPA_DI_FromProcessing";
                        nameSignal2 = "T_GPA_DI_Settings";
                        nameSignal3 = "T_GPA_DI_ToProcessing";

                        UUID_Type1 = UUID_DI_FromProcessing;
                        UUID_Type2 = UUID_DI_Settings;
                        UUID_Type3 = UUID_DI_ToProcessing;

                        UUID_Parent1 = UUID_Parent_DI_FromProcessing;
                        UUID_Parent2 = UUID_Parent_DI_Setting;
                        UUID_Parent3 = UUID_Parent_DI_ToProcessing;

                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal1, UUID_Type1, UUID_Parent1);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal2, UUID_Type2, UUID_Parent2);
                        createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal3, UUID_Type3, UUID_Parent3);

                }

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);

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

        DataBase.getInstance();// подключится к базе конфигом другого не дано
        jComboBox1.setModel(getComboBoxModel());
        //JOptionPane.showMessageDialog(null, "Подключение к базе прошло успешно!");

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

    public ComboBoxModel getComboBoxModel() // функция для создания списка из таблиц базы
    {

        //String db = "test08_DB";
        //workbase.connectionToBase();
        listDropT = DB.getviewTable();
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
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
