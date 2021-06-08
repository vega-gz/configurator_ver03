/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Table;

import DataBaseTools.DataBase;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import XMLTools.XMLSAX;
import globalData.globVar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nazarov
 * 
 * Тестовый класс работы с таблицами и базой (измененние в таблицы сразу меняет в базе)
 * 
 */
public class MainTestTable extends JFrame {

    public static void createGUI() {
        JFrame frame = new JFrame("Test frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XMLSAX.getConnectBaseConfig("Config.xml");
        DataBase db = new DataBase();
        globVar.DB = db;
        String table_name = "SignalSetups";

        db.getListColumns(table_name);
        NZDefaultTableModel madelTable = new NZDefaultTableModel(db.getData(table_name), db.getListColumns(table_name), table_name);
        DefaultTableModel modelBase = new ConnectBaseTable(madelTable);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        final JTable table = new JTable(modelBase);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel selLabel = new JLabel("Selected:");
        bottomPanel.add(selLabel);

        final JLabel currentSelectionLabel = new JLabel("");
        currentSelectionLabel.setAutoscrolls(true);
        bottomPanel.add(currentSelectionLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        table.getSelectionModel().setSelectionInterval(15, 15); // выделить строку программно
        table.scrollRectToVisible(table.getCellRect(15, 0, true));  // скролю на нужное

        frame.getContentPane().add(mainPanel);

        frame.setPreferredSize(new Dimension(550, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                createGUI();
            }
        });
    }
}
