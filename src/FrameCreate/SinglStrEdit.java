package FrameCreate;

import Tools.MyTableModel;
import Tools.TableTools;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*@author Lev*/
public class SinglStrEdit  extends javax.swing.JFrame{
    MyTableModel tableModel;
    int[] colsWidth;
    int curr=0;
    JTextField number = new JTextField();
    int qCols;
    int qRows;
    JTextField[] field;
    String title;

    public SinglStrEdit(MyTableModel tableModel, String title) {
        this.title = title;
        if(title!=null) this.setTitle(title + ": 1");
        Container container = this.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); 
        this.tableModel = tableModel;
        qCols = tableModel.getColumnCount();
        qRows = tableModel.getRowCount();
        field = new JTextField[qCols];
        colsWidth = new int[qCols];
        int simbWidth = 8;
        TableTools.setWidthCols(null, tableModel, colsWidth, simbWidth);
        int gpw = 0;
        
        JButton shiftL = new JButton("<");
        shiftL.setMargin(new java.awt.Insets(2, 2, 2, 2));
        container.add(shiftL);
        shiftL.setBounds(5, 5, 20, 20);
        shiftL.addActionListener((java.awt.event.ActionEvent evt) -> { shiftLActionPerformed(evt); });
        
        number.setSize(30, 20);
        number.setText("1");
        number.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(number);
        number.setBounds(30, 5, 30, 20);
        number.setName("0");
        number.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberFocusLost(evt);
            }
        });
        
        JButton shiftR = new JButton(">");
        shiftR.setMargin(new java.awt.Insets(2, 2, 2, 2));
        container.add(shiftR);
        shiftR.setBounds(65, 5, 20, 20);
        shiftR.addActionListener((java.awt.event.ActionEvent evt) -> { shiftRActionPerformed(evt); });
        
        JButton start = new JButton("Первая");
        start.setMargin(new java.awt.Insets(2, 2, 2, 2));
        container.add(start);
        start.setBounds(90, 5, 70, 20);
        start.addActionListener((java.awt.event.ActionEvent evt) -> { startActionPerformed(evt);});
        
        JButton end = new JButton("Последняя");
        end.setMargin(new java.awt.Insets(2, 2, 2, 2));
        container.add(end);
        end.setBounds(165, 5, 80, 20);
        end.addActionListener((java.awt.event.ActionEvent evt) -> { endActionPerformed(evt); });
        
        this.addMouseWheelListener((MouseWheelEvent e) -> {
            curr += e.getWheelRotation();
            if(curr<0) curr = 0;
            if(curr>=qRows) curr = qRows-1;
            setFields(curr);
        });
        
        for(int i=1; i<qCols; i++){
            JLabel label = new JLabel();
            label.setText(tableModel.getColumnName(i));
            int labLen = label.getText().length() * simbWidth + 10;
            if(labLen < 24) labLen = 24;
            
            if(colsWidth[i] < 24) colsWidth[i] = 24;
            int pw = labLen + colsWidth[i] + 5;
            
            field[i] = new JTextField();
            field[i].setMinimumSize(new Dimension(colsWidth[i], 20));
            field[i].setSize(colsWidth[i], 20);
            field[i].setText(tableModel.getValueAt(curr, i));
            field[i].setName(""+i);
            field[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    numberFocusLost(evt);
            }
        });
            
            JPanel panel = new JPanel();
            panel.setLayout (null);//new FlowLayout(FlowLayout.LEFT));
            panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            panel.setSize(pw+10, 24);
            panel.setLocation(5, 5+i*26);
            container.add(panel);//,constraints);//,BorderLayout.AFTER_LINE_ENDS);
            
            panel.add(label);
            label.setBounds(4, 2, labLen, 20);
            panel.add(field[i]);
            field[i].setBounds(labLen, 2, colsWidth[i]+10, 20);

            if(gpw < pw)gpw = pw;
        }
        
        JPanel panel = new JPanel();
        this.add(panel);
        if(gpw<400) gpw=400;
        this.setMinimumSize(new Dimension(gpw+40,(qCols+2)*26));
    }

    public void numberFocusLost(FocusEvent e) {
        if(!e.isTemporary()){
            int i = Integer.parseInt(e.getComponent().getName());
            if(i>0) tableModel.setValueAt(field[i].getText(), curr, i);
        }
    }    
    public void setFields(int j){
        if(j>=qRows){
            JOptionPane.showMessageDialog(null, "В таблице нет " + (j+1) +" строки");
            curr = qRows-1;
        }else curr = j;
        number.setText(""+(curr+1));
        if(title!=null) this.setTitle(title + ": "+(curr+1));
        for(int i=1; i<qCols; i++)
            field[i].setText(tableModel.getValueAt(curr, i));
    }
    
    private void shiftLActionPerformed(java.awt.event.ActionEvent evt) {  
        curr--;
        if(curr<0) curr = 0;
        setFields(curr);
    }                                       
 
    private void shiftRActionPerformed(java.awt.event.ActionEvent evt) {  
        curr++;
        if(curr>=qRows) curr = qRows-1;
        setFields(curr);
    }                                       
 
    private void startActionPerformed(java.awt.event.ActionEvent evt) {  
        curr = 0;
        setFields(curr);
    }                                       
 
    private void endActionPerformed(java.awt.event.ActionEvent evt) {  
        curr = qRows-1;
        setFields(curr);
    }                                       
 
    
}
