/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameCreate;

import DataBaseTools.DataBase;
import DataBaseTools.Update;
import Main.ProgressBar;
import ReadWriteExcel.ExcelAdapter;
import Tools.BackgroundThread;
import Tools.DoIt;
import Tools.FileManager;
import Tools.MyTableModel;
import Tools.RegistrationJFrame;
import Tools.TableTools;
import Tools.Tools;
import Tools.Utilities;
import Tools.closeJFrame;
import globalData.globVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ChangerTagNamed extends javax.swing.JFrame {
    ProgressBar pb = null;
    Update update = new Update();
    DataBase db = new DataBase();
    FileManager fm = new FileManager();
    Utilities util=new Utilities();
    int idArrayFindigData = 0;
    ArrayList<Integer> findDataRows = new ArrayList<>();
    
    MyTableModel tableModel; // модель таблицы
    //ArrayList<String[]> fromDB; // Что получим из базы
    int tableSize = 0;
    int qCol = 0;
    ArrayList<String[]> newName;
    ArrayList<JFrame> listJF = new ArrayList();
    JPopupMenu popupMenu = new JPopupMenu();
    ArrayList<String[]> listItemList = new ArrayList<>();
    String [] tableName;
    String comment,alg, newComment, newAlg;
    TableDB table;
    String[] cols;
    String tmp[];
    int tagName;
    int rusName;

    public ChangerTagNamed(TableDB tbl) {
        this.table = tbl;
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        if (!globVar.DB.isConnectOK()) {
            return;
        }
        newName = new ArrayList<>();
        tableModel = new MyTableModel();
        List<String> listColumn = new ArrayList<>(Arrays.asList("Наименование", "TAG_NAME_PLC"));//получили лист 
        if (listColumn == null || listColumn.isEmpty()) {
            return;
        }
        cols = listColumn.toArray(new String[listColumn.size()]);//преобразовали лист в стринговый массив
        tableModel.setColumnIdentifiers(new String[]{"Наименование", "TAG_NAME_PLC", "Новое_Наименование", "New_TAG_NAME_PLC"});

        //fromDB = globVar.DB.getData(tableDB, cols);//получили данные из БД
        tagName = table.getNumberCol("TAG_NAME_PLC");
        rusName = table.getNumberCol("Наименование");
        for (int i = 0; i < table.tableSize; i++) {
            tmp = new String[4];
            tmp[1] = table.getCell("TAG_NAME_PLC", i);
            tmp[0] = table.getCell("Наименование", i);
            tmp[2] = "";
            tmp[3] = "";
            tableModel.addRow(tmp);
        }
        //fromDB.forEach((rowData) -> tableModel.addRow(table.getCell("TAG_NAME_PLC",i), qCol).getctableName, qCol)Stream.concat(Arrays.stream(rowData), Arrays.stream(new String[]{"", ""})).toArray(String[]::new)));//вставляем данные по ячейкам в таблицу
        //comment = globVar.DB.getCommentTable(table);
        tableSize = table.tableSize;//fromDB.size();//размер строк
        qCol = 4;//listColumn.size();//размер столбцов
        int[] align = new int[qCol];
        int[] colsWidth = new int[qCol];

        TableTools.setWidthCols(cols, tableModel, colsWidth, 7.8);
//        if (tableSize > 0) {
//            TableTools.setAlignCols(fromDB.get(0), align);
//        }

        initComponents();

        RegistrationJFrame rgf = (JFrame jf) -> {
            listJF.add(jf);
        };
        closeJFrame cjf = () -> {
            for (JFrame jf : listJF) {
                jf.setVisible(false);
            }
        };

        TableTools.setPopUpMenu(jTable1, popupMenu, tableModel, "Замена имён в таблице " + table.tableName, rgf, listJF);
        // TableTools.setTableSetting(jTable1, colsWidth, align, 25);

        jTable1.setRowSelectionAllowed(true);           // Разрешаю выделять по строкам
        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.setColumnSelectionAllowed(true);    // Разрешение выделения столбца
        columnModel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);// Режим выделения интервала
        int qCol = jTable1.getColumnCount();                //Определяю количество столбцов
        if (qCol > colsWidth.length) {
            qCol = colsWidth.length;  //чтобы не вылететь за границы таблицы, если переданный массив ширин неправильный
        }

        ExcelAdapter editT = new ExcelAdapter(jTable1);//редактирование таблицы контрл В Ц

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(tableModel);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Переименовать");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(62, 62, 62)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!Tools.isDesDir()) {
            return;
        }
        String processName = "Переименование";
        if (globVar.processReg.indexOf(processName) >= 0) {
            JOptionPane.showMessageDialog(null, "Запуск нового процесса генерации заблокирован до окончания предыдущей генерации");
            return;
        }

        DoIt di = () -> {
            newName=tableModel.toArrayList();//получили всю таблицу целиком
            util.DeleteEmptyString(newName);//удалили строки в которых нет редактирования
            update.ReNameAllData(tableModel, table, tmp, rusName, tagName); // вызов фукции с формированием базы по файлу конфигурации
            String tableName = table.jTree1.getSelectionPath().getLastPathComponent().toString();//нашли имя таблицы
            
            if(tableName.lastIndexOf("(")!=-1){
            tableName=tableName.substring(tableName.lastIndexOf("(")+1, tableName.lastIndexOf(")"));
            }
            fm.ChangeIntTypeFile(globVar.desDir, newName, tableName,jProgressBar1);//запускаем метод переименования
          
            globVar.processReg.remove(processName);
        };

        BackgroundThread bt = new BackgroundThread(processName, di);
        bt.start();
        globVar.processReg.add(processName);
  
        //-------блок кода который ищет одинаковые строки и удаляет лишнее,дабы не загружать память
        // String tableName = table.jTree1.getSelectionPath().getLastPathComponent().toString();//нашли имя таблицы
        //  fm.ChangeIntTypeFile(globVar.desDir, newName, tableName);
        // BackgroundThread bt = new BackgroundThread("Переименование", di);
        //  bt.start();

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ChangerTagNamed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangerTagNamed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangerTagNamed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangerTagNamed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ChangerTagNamed().setVisible(true);
            }
        });
    }

    boolean compareTable(ArrayList<String[]> fromDB, DefaultTableModel tableModel) {
        int x = tableModel.getColumnCount();
        int y = tableModel.getRowCount();
        if (fromDB.size() != y || fromDB.get(0).length != x) {
            return true;
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                if (!fromDB.get(i)[j].equals(tableModel.getValueAt(i, j))) {
                    return true;
                }

            }
        }
        return false;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
