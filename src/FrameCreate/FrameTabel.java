package FrameCreate;

import XMLTools.XMLSAX;

import fileTools.FileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import DataBaseConnect.StructSelectData;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.xml.sax.SAXException;
import DataBaseConnect.DataBase;
import Main.Main_JPanel;
import XMLTools.UUID;
import DataBaseConnect.*;
import FileConfigWork.Generator;
import FrameCreate.TableNzVer2;
import FileConfigWork.Generator;
import FileConfigWork.SignalTypeToBase;
import FileConfigWork.SignalTypeConvertTagName;
import globalData.globVar;
import java.awt.Component;
import java.io.File;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author cherepanov
 */
public class FrameTabel extends javax.swing.JPanel {

    Main_JPanel mj = new Main_JPanel();
    String nameTable = "";
    private ArrayList<String[]> dataFromDb; // данные из таблицы бызы на основе которых строим нашу
    private ArrayList<String> columns;  // Колонки базы переданные в конструкторе
    ArrayList<ArrayList> listToTable = new ArrayList<>(); // Лист для передачи в таблицу
    String[] columnstoMass = null; // Массив столбцов для передачи в таблицу
    //TableModel tableFrameModel = null;
    //NZDefaultTableModel tableFrameModel = null; // Моя таблица полностью извращенная
    TableNzVer3 tableFrameModel = null;
    XMLSAX sax = new XMLSAX();
    int filepath;
    String filepatch;
    DataBase workbase = DataBase.getInstance();

    public int tableSize() {//возвращает размер таблицы
        return jTable1.getRowCount();
    }

    public String tableName() {//возвращает имя таблицы
        return nameTable;
    }

    public Object getCell(String colName, int row) {//возвращает содержимое ячейки по имени столбца и номеру строки
        return tableFrameModel.getDataNameColumn(colName, row);
    }

    public FrameTabel(String nameTable) {
        this.nameTable = nameTable;
        //this.tableFrameModel = getTableData();
        initComponents();
    }

    // --- Конструктор с вызовом таблицы TableNzVer2 и преобразованными данными для нее ---
    public FrameTabel(String selectT, ArrayList<String> columns) {
        this.nameTable = selectT;
        this.columns = columns;
        columns.toArray(new String[columns.size()]); // Преобразование в массив
        this.dataFromDb = DataBase.getInstance().getData(selectT, columns); // получили данные с базы 
        //преобразовать данные для переваривания таблицей
        for (String[] mass : dataFromDb) {
            ArrayList<String> tmpList = new ArrayList<>();
            for (String s : mass) {
                tmpList.add(s);
            }
            listToTable.add(tmpList);
        }
        tableFrameModel = new TableNzVer3(nameTable, columns, listToTable);
        //jTable1 = new TableNzVer3(nameTable, columnstoMass, listToTable).getJTable();
        //this.tableFrameModel = (NZDefaultTableModel) new TableNzVer2().getModelTable(nameTable, columnstoMass, listToTable);
//        System.out.println("FIND_0 " + tableFrameModel.getDataNameColumn("TAG_NAME_PLC", 0));
//        System.out.println("FIND_1 " + tableFrameModel.getDataNameColumn("TAG_NAME_PLC", 1));
//        System.out.println("FIND_2 " + tableFrameModel.getDataNameColumn("TAG_NAME_PLC", 2));
//        System.out.println("countRow " + tableFrameModel.getRowCount());
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = tableFrameModel.getJTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(999, 530));

        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Добавить в мнемосхему");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Сигнал нижнего уровня");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("просканировать файл type с автозаменой");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Генерация_НЗ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 999, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 535, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fileload = new JFileChooser(new File(globVar.desDir));
        fileload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//эта строка отвечает за путь файла
        filepath = fileload.showOpenDialog(this);//эта строка отвечает за само открытие
        if (filepath == JFileChooser.APPROVE_OPTION) {
            try {
                globVar.desDir = fileload.getSelectedFile().getCanonicalPath();
                Generator.GenTypeFile(this);

            } catch (IOException ex) {
                Logger.getLogger(Main_JPanel.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

        // --- Временная кнопка для преобразования файлов type ---
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fileopen = new JFileChooser("C:\\Users\\cherepanov\\Desktop\\сигналы");
        int ren = fileopen.showDialog(null, ".type");
        if (ren == JFileChooser.APPROVE_OPTION) {

            File file = fileopen.getSelectedFile();// выбираем файл из каталога
            String pathFileType = file.toString();
            //System.out.println(file.getName());
            if (pathFileType.endsWith(".type")) {
                new SignalTypeConvertTagName(pathFileType);
            } else {
                JOptionPane.showMessageDialog(null, "Расширение файла не .type"); // Это сообщение
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
            Generator.GenTypeFile(this);
    }//GEN-LAST:event_jButton4ActionPerformed

        public TableModel getTableData() { // функция для создания списка из талиц базы так же возращаем объект для конструкции таблицы при запуске
            // Можно так сложно не соединять, аппендицит от предыдущего что бы не запутаться
            String[] columnDop = {"Выбор"};// до поля для галок или еще чего
            String[] columnNames = StructSelectData.getColumns();
            String[] resultColumn = Stream.concat(Arrays.stream(columnDop), Arrays.stream(columnNames))
                    .toArray(String[]::new); // соединяем два массива
            Object[][] data = StructSelectData.getcurrentSelectTable(); // От куда беру данные
            Object[] streamArray;
            Object[] streamNull = new Object[1];
            streamNull[0] = null;
            Object[][] tmp2 = new Object[data.length][];
            for (int i = 0; i < data.length; i++) {
                streamArray = new Object[data[i].length + 1];
                // преобразовываем массив
                Object[] testStream = Stream.concat(Arrays.stream(streamNull), Arrays.stream(data[i])).toArray(Object[]::new);
                tmp2[i] = testStream;
            }
            return new DefaultTableModel(tmp2, resultColumn) {
                @Override
                public Class<?> getColumnClass(int columnIndex) { // структура для отображения таблицы с галками
                    Class clazz = String.class;
                    switch (columnIndex) {
                        case 0:
                            clazz = Boolean.class;
                            break;
                    }
                    return clazz;
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == column;
                }

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                    // Условие проверки галочки скрывать легенду
                    if (aValue instanceof Boolean && column == 0) {
                        System.out.println("Posution - > " + row + " " + aValue);
                        Vector rowData = (Vector) getDataVector().get(row); // не помню для чего но без этого только скрывает =(
                        rowData.set(0, (boolean) aValue);
                        fireTableCellUpdated(row, column);

                        try {
                            // Само действие не реализованно
                            if ((boolean) aValue == true) {
                                System.out.println("true");
                            }
                            if ((boolean) aValue == false) {
                                System.out.println("false");
                            }
                        } catch (NullPointerException e) {
                            JOptionPane.showMessageDialog(null, "Трудности с добавлением");
                        }
                    }

                }
            };
        }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}



   

