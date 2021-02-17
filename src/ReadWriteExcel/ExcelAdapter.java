/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.datatransfer.*;
import java.util.*;

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

    private TableCellListener listener;

    private final UndoRedoSystem undoSystem = new UndoRedoSystem(15);

    /**
     * The Excel Adapter is constructed with a JTable on which it enables
     * Copy-Paste and acts as a Clipboard listener.
     */
    public ExcelAdapter(JTable myJTable) {

        jTable1 = myJTable;
        listener = new TableCellListener(jTable1, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                undoSystem.put(tcl.getOldData());
            }
        });
        undoSystem.put(((DefaultTableModel) jTable1.getModel()).getDataVector());
        KeyStroke deleteCopy = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false);
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        // Identifying the copy KeyStroke user can modify this
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false);
        // Identifying the Paste KeyStroke user can modify this
        KeyStroke cancel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK, false);
        KeyStroke redo = KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Delete", delete, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Cancel", cancel, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Redo", redo, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "DeleteCopy", deleteCopy, JComponent.WHEN_FOCUSED);
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
            TableModel model = jTable1.getModel();
            Vector<Vector> data = undoSystem.undo();
            if (data == null) {
                return;
            }
            for (int i = 0; i < data.size(); ++i) {
                for (int j = 0; j < data.get(0).size(); ++j) {
                    model.setValueAt(data.get(i).get(j), i, j);
                }
            }
        }
        if (e.getActionCommand().compareTo("Redo") == 0) {
            TableModel model = jTable1.getModel();
            Vector<Vector> data = undoSystem.redo();
            if (data == null) {
                return;
            }
            for (int i = 0; i < data.size(); ++i) {
                for (int j = 0; j < data.get(0).size(); ++j) {
                    model.setValueAt(data.get(i).get(j), i, j);
                }
            }
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

            listener.fixOldData();
            for (int i = 0; i < numrows; i++) {
                for (int j = 0; j < numcols; j++) {
                    jTable1.setValueAt("", rowsselected[i], colsselected[j]);
                }
            }
            listener.tableChanged();
        }
        if (e.getActionCommand().compareTo("DeleteCopy") == 0) {
            StringBuffer sbf = new StringBuffer();
            int row = jTable1.getColumnCount();
            int rowsselected = jTable1.getSelectedRow();//здесь получаем индекс
            for (int i = 0; i < row; i++) {//в цикле мы получаем строку
                
                sbf.append(jTable1.getValueAt(rowsselected, i));
                System.out.println(jTable1.getValueAt(rowsselected, i));
                sbf.append("\t");//разделяет построчно
                
            }
            stsel = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
            
           
            
            
            listener.fixOldData();
            for(int i = 0; i < row; i++){
                jTable1.setValueAt("", rowsselected, i);
            }
            
            
            
            
            
            listener.tableChanged();
            

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
                listener.fixOldData();
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
                listener.tableChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class UndoRedoSystem {

        private final Deque<Vector<Vector>> history = new LinkedList<>();
        private final Deque<Vector<Vector>> trash = new LinkedList<>();
        private final int capacity;

        public UndoRedoSystem(int capacity) {
            this.capacity = capacity;
        }

        public Vector<Vector> undo() {
            if (history.isEmpty()) {
                return null;
            }
            Vector<Vector> data = history.removeLast();
            trash.addLast(data);
            return data;
        }

        public Vector<Vector> redo() {
            if (trash.isEmpty()) {
                return null;
            }
            Vector<Vector> data = trash.removeLast();
            history.addLast(data);
            return data;
        }

        public void put(Vector<Vector> data) {
            trash.clear();
            history.addLast(data);
            if (history.size() == capacity) {
                history.removeFirst();
            }
        }
    }
}
