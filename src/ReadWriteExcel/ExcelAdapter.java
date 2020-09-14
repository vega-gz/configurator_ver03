/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.util.*;
import Tools.TableTools;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables. The
 * clipboard data format used by the adapter is compatible with the clipboard
 * format used by Excel. This provides for clipboard interoperability between
 * enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener {

    private String rowstring, value;
    private Clipboard system;
    private StringSelection stsel;
    private JTable jTable1;
    TableTools tt = new TableTools();

    /**
     * The Excel Adapter is constructed with a JTable on which it enables
     * Copy-Paste and acts as a Clipboard listener.
     */
    public ExcelAdapter(JTable myJTable) {
        jTable1 = myJTable;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        // Identifying the copy KeyStroke user can modify this
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false);
        // Identifying the Paste KeyStroke user can modify this
        KeyStroke cancel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Delete", delete, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Cancel", cancel, JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public JTable getJTable() {
        return jTable1;
    }

    public void setJTable(JTable jTable1) {
        this.jTable1 = jTable1;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Cancel") == 0) {
            ArrayList ch = new ArrayList();
            ArrayList<int[]> rows = (ArrayList<int[]>) ch.get(0);
            ArrayList<int[]> cols = (ArrayList<int[]>) ch.get(1);
            ArrayList<int[]> value = (ArrayList<int[]>) ch.get(2);
        }
        if (e.getActionCommand().compareTo("Delete") == 0) {//удаление ячейки по кнопке
            int numcols = jTable1.getSelectedColumnCount();
            int numrows = jTable1.getSelectedRowCount();
            int[] rowsselected = jTable1.getSelectedRows();
            int[] colsselected = jTable1.getSelectedColumns();
            if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0]
                    && numrows == rowsselected.length)
                    && (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0]
                    && numcols == colsselected.length))) {
                JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                        "Invalid Copy Selection",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < numrows; i++) {
                for (int j = 0; j < numcols; j++) {
                    jTable1.setValueAt("", rowsselected[i], colsselected[j]);
                }
            }
        }
        if (e.getActionCommand().compareTo("Copy") == 0) {
            StringBuffer sbf = new StringBuffer();
            int numcols = jTable1.getSelectedColumnCount();
            int numrows = jTable1.getSelectedRowCount();
            int[] rowsselected = jTable1.getSelectedRows();
            int[] colsselected = jTable1.getSelectedColumns();
            if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0]
                    && numrows == rowsselected.length)
                    && (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0]
                    && numcols == colsselected.length))) {
                JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                        "Invalid Copy Selection",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < numrows; i++) {
                for (int j = 0; j < numcols; j++) {
                    sbf.append(jTable1.getValueAt(rowsselected[i], colsselected[j]));
                    if (j < numcols - 1) {
                        sbf.append("\t");
                    }
                }
                sbf.append("\n");
            }
            stsel = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
        }
        if (e.getActionCommand().compareTo("Paste") == 0) {
            System.out.println("Trying to Paste");
            int startRow = (jTable1.getSelectedRows())[0];
            int startCol = (jTable1.getSelectedColumns())[0];
            try {
                String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                System.out.println("String is:" + trstring);
                StringTokenizer st1 = new StringTokenizer(trstring, "\n");
                for (int i = 0; st1.hasMoreTokens(); i++) {
                    rowstring = st1.nextToken();
                    StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
                    for (int j = 0; st2.hasMoreTokens(); j++) {
                        value = (String) st2.nextToken();
                        if (startRow + i < jTable1.getRowCount()
                                && startCol + j < jTable1.getColumnCount()) {
                            jTable1.setValueAt(value, startRow + i, startCol + j);
                        }
                        System.out.println("Putting " + value + "atrow=" + startRow + i + "column=" + startCol + j);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getActionCommand().compareTo("Paste") == 0) {
            System.out.println("Trying to Paste");
            int statrRow = (jTable1.getSelectedRows())[0];
            int startCol = (jTable1.getSelectedColumns())[0];
            try {
                String trstring = (String) (system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                System.out.println("String is:"+trstring);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
