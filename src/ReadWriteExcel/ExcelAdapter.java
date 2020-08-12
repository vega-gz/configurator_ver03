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

public class ExcelAdapter implements ActionListener {

    private String rowstring, value;
    private Clipboard system;
    private StringSelection stsel;
    private JTable jTable1;
    public ArrayList<Integer> currentValues;
    public int version = 0;
    public int highestVersion = 0;
    public Map<Integer, ArrayList<Integer>> history = new HashMap<Integer, ArrayList<Integer>>();

    public ExcelAdapter(JTable myJTable) {
        jTable1 = myJTable;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        // Identifying the copy KeyStroke user can modify this
        // to copy on some other Key combination.
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        KeyStroke cancel = KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK, false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Delete", delete, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Cancel", cancel, JComponent.WHEN_FOCUSED);

        system = Toolkit.getDefaultToolkit().getSystemClipboard();

    }
    
    

//    //-----UNDO REDO
//    public Integer[] getValues() {
//        return (Integer[]) getCurrentValues().toArray();//конвертируем наш список в массив
//    }
//
//    public ArrayList<Integer> getCurrentValues() {//метод проверки,если список пустой (то есть его нет)то инициализируем
//        if (currentValues == null) {
//            currentValues = new ArrayList<Integer>();
//        }
//        return currentValues;
//    }
//
//    public void add(Integer[] newValues) {//метод добавления значений в список текущих значений
//        incrementHistory();//обновляем версию
//       // getCurrentValues().addAll(Arrays.asList(newValues));
//        
//       
//    }
//
//    public void incrementHistory() {//метод обновления версий
//        if (history.get(version) != null) {//проверяем ,не пустая ли история
//            throw new IllegalArgumentException("Cannot change history");
//        }
//        history.put(version, getCurrentValues());//помещаем индекс
//        if (version > 2) {
//            history.remove(version - 2);//удаляем элемент из списка
//        }
//        version++;
//        if (version > highestVersion) {//если текущая верся(индекс)больше главное,то текущая становится главной(то бишь последней версией)
//            highestVersion = version;
//        }
//    }
//    
//    
//    public void delete(Integer[] endValues) {//метод очистки массива текущих значений
//        incrementHistory();//обновляем версию
//        int currentLength = getCurrentValues().size();//количество совершенных изменений
//        int i = endValues.length-1;//-1 скорее всего потому что отсчет начинается от 0
//        for (int deleteIndex = currentLength - 1//опять таки потому что от 0
//                ; deleteIndex > currentLength - endValues.length
//                ; deleteIndex--) {
//            if (!endValues[i].equals(getCurrentValues().get(deleteIndex))//проверка на наличие,если не равны,значит этого символа нет
//                    ) {
//                throw new IllegalArgumentException("Cannot delete element(" + endValues[i] + ") that isn't there");                
//            }
//            getCurrentValues().remove(deleteIndex);
//        }
//    }
//
// //---UNDO REDO
    /**
     * Public Accessor methods for the Table on which this adapter acts.
     */
    public JTable getJTable() {
        return jTable1;
    }

    public void setJTable(JTable jTable1) {
        this.jTable1 = jTable1;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Cancel") == 0) {

            version--;//откатываемся и получаем значение
            if (history.get(version) == null) {
                throw new RuntimeException("Undo operation only supports 2 undos");
            }
            this.currentValues = history.get(version);
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
            // Check to ensure we have selected only a contiguous block of
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
                      //  System.out.println("Putting " + value + "atrow=" + startRow + i + "column=" + startCol + j);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
