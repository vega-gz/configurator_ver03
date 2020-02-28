/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author cherepanov
 */
public class CreateFrame extends JFrame {

    JLabel label1, label2, label3, label4, label5;
    JTextField text1, text2, text3, text4;
    JComboBox urlbox;
    JButton button;
    JFrame frame;
    String nameProject, user, url, pass;
    String test08_DB = "jdbc:postgresql://172.16.35.25:5432/test08_DB";

    public CreateFrame(String nameProject, String user, String url, String pass) {
        super();

        this.nameProject = nameProject;
        this.url = url;
        this.pass = pass;
        this.user = user;

//        frame=new JFrame();
        String[] items = {
            "  ",
            test08_DB
        };
        setSize(600, 600);
        setLocation(100, 100);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        GridLayout layout = new GridLayout(5, 5, 0, 5);
        label1 = new JLabel("User");
        label2 = new JLabel("nameProject");
        label3 = new JLabel("Password");
        label4 = new JLabel("Path to project");
        label5 = new JLabel("Chosse path");

        text1 = new JTextField(25);
        text2 = new JTextField(25);
        text3 = new JTextField(25);
        text4 = new JTextField(25);

        urlbox = new JComboBox(items);
        button = new JButton("Action");

        panel1.setLayout(layout);
        panel1.add(label1);
        panel1.add(text1);
        panel1.add(label2);
        panel1.add(text2);
        panel1.add(label3);
        panel1.add(text3);
        panel1.add(label4);
        panel1.add(text4);
        panel1.add(label5);
        panel1.add(urlbox);

        panel2.add(button);

        panel3.add(panel1, BorderLayout.CENTER);
        panel3.add(panel2, BorderLayout.NORTH);

        getContentPane().add(panel3);
        pack();
        setVisible(true);
        ActionListener listener = new TestActionListener();
        button.addActionListener(listener);
        // button1.addActionListener(listener);

    }

    public class TestActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            user = text1.getText();//берем значения из текстового поля и помещаем в переменную
            nameProject = text2.getText();
            url = text4.getText();
            pass = text3.getText();

            if (url.isEmpty()) {
                url = (String) urlbox.getSelectedItem();

            }
            WriteConfigFile write = new WriteConfigFile();//вызываем класс записи файла конф
//                
            try {
                write.writeXML(user, url, nameProject, pass);

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(CreateFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();//закрывает окно после отработки

        }

    }
    

}
