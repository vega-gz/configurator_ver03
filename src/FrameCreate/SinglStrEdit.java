package FrameCreate;

import Table.ConnectBaseTable;
import Table.NZDefaultTableModel;
import Table.PopUpMenuJtableSetupsSignal;
import Table.TableTools;
import Tools.MyTableModel;
import Tools.SaveFrameData;
import Tools.SimpleTable;
import Tools.isCange;
import globalData.globVar;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/*@author Lev*/
public class SinglStrEdit  extends javax.swing.JFrame{
    MyTableModel tableModel;
    int[] colsWidth;
    int curr=0;
    JTextField number = new JTextField();
    int qCols;
    int qRows;
    JTextField[] field;
    JLabel[] labels;
    String title;
    JTable jTable1;
    JScrollPane jScrollPane1=null;
    JButton shiftL;
    JButton shiftR;
    JButton start;
    JButton end;
    JButton save;
    int labLen = 0;
    int fieldLen = 0;
    SimpleTable st;
    int H_SIZE = 20;
    int H_GAP = 2;
    String relatedTable=null;
    
    // --- Фрейм работа с строкой таблицы (тут же и уставки)---
    public SinglStrEdit(MyTableModel tableModel, String title, ArrayList<JFrame> listJF) {
        Container container = this.getContentPane();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); 
        int gpw = 0;
        
        if(title!=null) this.setTitle(title + ": 1");
        this.tableModel = tableModel;
        qCols = tableModel.getColumnCount();
        qRows = tableModel.getRowCount();
        field = new JTextField[qCols];
        labels = new JLabel[qCols];
        colsWidth = new int[qCols];
        double simbWidth = 7.7;
        TableTools.setWidthCols(null, tableModel, colsWidth, simbWidth);
        
        shiftL = new JButton("<");
        shiftL.setMargin(new java.awt.Insets(2, 2, 2, 2));
        container.add(shiftL);
        shiftL.addActionListener((java.awt.event.ActionEvent evt) -> { shiftLActionPerformed(evt); });
        
        number.setText("1");
        number.setHorizontalAlignment(SwingConstants.CENTER);
        container.add(number);
        number.setName("0");
        number.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberFocusLost(evt);
            }
        });
        
        shiftR = new JButton(">");
        shiftR.setMargin(new java.awt.Insets(2, 2, 2, 2));
        shiftR.addActionListener((java.awt.event.ActionEvent evt) -> { shiftRActionPerformed(evt); });
        
        start = new JButton("Первая");
        start.setMargin(new java.awt.Insets(2, 2, 2, 2));
        start.addActionListener((java.awt.event.ActionEvent evt) -> { startActionPerformed(evt);});
        
        end = new JButton("Последняя");
        end.setMargin(new java.awt.Insets(2, 2, 2, 2));
        end.addActionListener((java.awt.event.ActionEvent evt) -> { endActionPerformed(evt); });
        
        save = new JButton("Сохранить");
        save.setMargin(new java.awt.Insets(2, 2, 2, 2));
        save.addActionListener((java.awt.event.ActionEvent evt) -> { saveActionPerformed(evt); });
        
        this.title = title;
        int x = title.indexOf("_");
        String abonent = title.substring(0,x);
        x = title.indexOf("_mb_");
        if(x<0) x = title.lastIndexOf("_");
        String nodeName = title.substring(x+1);
        
        // Уставки из отдельной таблицы
        String table_name = "SignalSetups";
        NZDefaultTableModel madelTable = new NZDefaultTableModel(globVar.DB.getData(table_name), globVar.DB.getListColumns(table_name), table_name);
        DefaultTableModel modelBase = new ConnectBaseTable(madelTable);
        jTable1 = new JTable(modelBase);
        new PopUpMenuJtableSetupsSignal().setPopMenu(jTable1);
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jTable1);

        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                //numberFocusLost(evt);
                System.out.println("FocusListener " + evt.isTemporary());
            }
        });
        //if(st.isCreate()) st.setSimpleTableSettings(jTable1);
        gpw = 600;
        //}

        this.addMouseWheelListener((MouseWheelEvent e) -> {
            curr += e.getWheelRotation();
            if(curr<0) curr = 0;
            if(curr>=qRows) curr = qRows-1;
            setFields(curr);
        });
        
        for(int i=1; i<qCols; i++){
            labels[i] = new JLabel();
            labels[i].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            labels[i].setText(tableModel.getColumnName(i));
            int labLenTmp = (int)(labels[i].getText().length() * simbWidth + 10);
            if(labLen < labLenTmp) labLen = labLenTmp;
            
            field[i] = new JTextField();
            field[i].setText(tableModel.getValueAt(curr, i));
            int fieldLenTmp = (int)(field[i].getText().length() * simbWidth + 30);
            if(fieldLen < fieldLenTmp) fieldLen = fieldLenTmp;
            field[i].setName(""+i);
            field[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    numberFocusLost(evt);
                }
            });
        }
        setLayout();
        if(gpw<400) gpw=400;
        this.setSize(new Dimension(gpw+40,(qCols+2)*(H_SIZE + H_GAP) + 2*H_GAP));
        //Лямбда для операций при закрытии окна архивов
        SaveFrameData sfd = ()->{
            st.saveTableInDB();
        };
        isCange ich = ()->{
            if(st == null) return false; // 
            return st.isNew(); // тут возращаем реализованный интерфейс(падает )
        };
        TableTools.setFrameListener(this, sfd, ich, null);//cjf);
        
        this.setTitle("Редактирование сигнала");//table + ": "+comment);
    }

    private void setLayout(){    
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        SequentialGroup sgButtons = layout.createSequentialGroup().addComponent(shiftL).addPreferredGap(RELATED);
        sgButtons =  sgButtons.addComponent(number,  PREFERRED_SIZE, 30, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .addComponent(shiftR)
                .addPreferredGap(RELATED)
                .addComponent(start)
                .addPreferredGap(RELATED)
                .addComponent(end)
                .addPreferredGap(RELATED, DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save);
                //.addGap(0, 0, Short.MAX_VALUE);
        
        ParallelGroup pgLabels = layout.createParallelGroup();
        for(int i = 2; i < field.length; i++)
            pgLabels = pgLabels.addComponent(labels[i], PREFERRED_SIZE, labLen, PREFERRED_SIZE);
        
        ParallelGroup pgFields = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        for(int i = 2; i < field.length; i++) 
            pgFields = pgFields.addComponent(field[i], PREFERRED_SIZE, fieldLen, PREFERRED_SIZE);
       
        SequentialGroup table = layout.createSequentialGroup()
                        .addGroup(pgLabels)
                        .addGap(5)//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pgFields)
                        //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                ;
        if(jScrollPane1!=null) table = table.addComponent(jScrollPane1, DEFAULT_SIZE, 614, Short.MAX_VALUE);
        
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sgButtons)
                    .addComponent(field[1])
                    .addGroup(table))
                .addContainerGap())
        );
        
        ParallelGroup pgButtons = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shiftL)
                    .addComponent(number, PREFERRED_SIZE, H_SIZE, PREFERRED_SIZE)
                    .addComponent(shiftR)
                    .addComponent(start)
                    .addComponent(end)
                    .addComponent(save);
        
        SequentialGroup sgFields = layout.createSequentialGroup();
        for(int i = 2; i < field.length; i++) 
            sgFields = sgFields.addGroup(layout.createParallelGroup()
                                .addComponent(labels[i], PREFERRED_SIZE, H_SIZE, PREFERRED_SIZE)
                                .addComponent(field[i], PREFERRED_SIZE, H_SIZE, PREFERRED_SIZE)).addGap(H_GAP);
        sgFields = sgFields.addGap(0, 152, Short.MAX_VALUE);
        
        ParallelGroup table2 = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(sgFields);
        if(jScrollPane1!=null) table2 = table2.addComponent(jScrollPane1, PREFERRED_SIZE, 0, Short.MAX_VALUE);
        
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(H_GAP)//addContainerGap()
                .addGroup(pgButtons)
                .addGap(H_GAP)//, 5, 5)
                .addComponent(field[1], PREFERRED_SIZE, H_SIZE, PREFERRED_SIZE)
                .addGap(H_GAP)//.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(table2)
                .addContainerGap()
            )
        );
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
        if(st!=null) st.reSetTableContent(tableModel.getValueAt(curr, 2));//"TAG_NAME_AnPar",
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
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {  
        st.saveTableInDB(); // тут тоже вываливается из таблицы APS
    }                                       
 
    public void updateRelatedTable(){
        ArrayList<String[]> fromDB;
        fromDB = globVar.DB.getData(relatedTable);
        
    }
    
}
