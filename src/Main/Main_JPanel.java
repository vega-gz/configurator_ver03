package Main;

import FrameCreate.FrameTabel;

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

/**
 *
 * @author cherepanov
 */
public class Main_JPanel extends javax.swing.JFrame {

    String APurl = "jdbc:postgresql://172.16.35.25:5432/test08_DB";
    String url, nameProject, user, pass;
    String FILENAME = "Config.xml";
    RWExcel excel = new RWExcel();
    String path;
    DataBase DB = new DataBase();
    public String signal;
    ArrayList<String> listDropT = new ArrayList();
    XMLSAX createXMLSax = new XMLSAX();
    int filepath;
    String filepatch, type;
    String nameSignal, UUID_Type;

    private final String UUIDType_AI = "5bac053cff7f4ef8a74048f428228aee";//уиды типов
    private final String UUIDType_DO = "94C521C642227325371AE7BCC36E527";
    private final String UUIDType_DI = "A1F5648246D48275D6786689DC1498B9";
    private final String UUIDType_AO = "E02C7B0E4F1236B149113594A642B5FB";

    public Main_JPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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

        jButton2.setText("Подключиться к базе");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addGap(24, 24, 24)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)))
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
                    .addComponent(jButton2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton6)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        CreateFrame frame = new CreateFrame(url, nameProject, user, pass);//вызываем второре окно для записи конф файла


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            final File xmlFile = new File(System.getProperty("user.dir") //user.dir-это путь до домашнего каталога(каталог где хранится прога)
                    + File.separator + FILENAME);//separator это разделитель =="\"

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(xmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Наш файл:" + doc.getDocumentElement().getNodeName());
            System.out.println("=================");

            NodeList nodeList = doc.getElementsByTagName("config");

            for (int i = 0; i < nodeList.getLength(); i++) {
                //выводим инфу по каждому их элементов
                Node node = nodeList.item(i);
                System.out.println();
                System.out.println("Текущий элемент: " + node.getNodeName());
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
//                    
                    pass = element.getElementsByTagName("PASS").item(0).getTextContent();
                    user = element.getElementsByTagName("USER").item(0).getTextContent();
                    url = element.getElementsByTagName("URL").item(0).getTextContent();
                }

            }
            System.out.println(pass + " " + url + " " + user);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        DataBase db = new DataBase();
        db.connectionToBase();

        JOptionPane.showMessageDialog(null, "Соединение установлено!");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
        int ren = fileopen.showDialog(null, "DownloadToBase");
        if (ren == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();// выбираем файл из каталога

            path = file.toString();
            excel.setPatchF(path);
            try {
                excel.readAllfile();//это всего лишь читает файл но не записыыает его

                Main.fillDB(file.getPath());
            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);//
            } catch (SQLException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jComboBox1.setModel(getComboBoxModel());//если мы сделам ваот так чтобыникто не узнал

        JOptionPane.showMessageDialog(null, "Загрузка в базу завершена!");


    }//GEN-LAST:event_jButton3ActionPerformed


    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        String selectT = (String) jComboBox1.getSelectedItem();
        DB.connectionToBase();
        List<String> listColumn = DB.selectColumns(selectT);
        String[] columns = new String[listColumn.size()];
        String tmpStr = " ";
        int j = 0;

        for (String s : listColumn) {

            columns[j] = s;
            ++j;
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
        ArrayList<String[]> dataFromDb = new ArrayList<>();//создание списка 
        //String[] columns = {"uuid_plc","colum_18","Наименование сигнала"}; // тут у нас что отоброжать 
        String selectElem = (String) jComboBox1.getSelectedItem();//j String комбо бок
        //  DB.connectionToBase(url,pass,user);
        DB.selectData(selectElem, columns); //внесли данные в сущность 
        StructSelectData.setnTable(selectElem); // вносим в структуру название таблицы для печати того же файла Максима  LUA
        dataFromDb = DB.getcurrentSelectTable();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
        int sizeWidth = 800;
        int sizeHeight = 600;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;//это размещение 
        FrameTabel frameTable = new FrameTabel();
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
            String filename = fileload.getSelectedFile().getName();//записывает в переменную путь,так что теперь надо обЪяснить методу как его юзать 
            try {
                filepatch = fileload.getSelectedFile().getCanonicalPath();
            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);

            }

            switch (signal) {
                case "dies_ai":
                    nameSignal = "T_GPA_AI";
                    UUID_Type = UUIDType_AI;
                    break;
                case "dies_ao":
                    nameSignal = "T_GPA_AO";
                    UUID_Type = UUIDType_AO;
                    break;
                case "dies_do":
                    nameSignal = "T_GPA_DO";
                    UUID_Type = UUIDType_DO;
                    break;
                case "dies_di":
                    nameSignal = "T_GPA_DI";
                    UUID_Type = UUIDType_DI;
            }
            FrameTabel ft = new FrameTabel();
            ft.setSignal(signal);

            try {
                createXMLSax.runBasecreateTypeAll(filepatch, signal, nameSignal, UUID_Type);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
//
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // workbase.connectionToBase();
        if (!listDropT.isEmpty()) {  // если есть что удалять передаем лист в обработчик баз
            DB.dropTable(listDropT);
        } else ;
    }//GEN-LAST:event_jButton6ActionPerformed

    private ComboBoxModel getComboBoxModel() // функция для создания списка из таблиц базы
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
