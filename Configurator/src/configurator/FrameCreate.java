/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author cherepanov
 */
public class FrameCreate {

    JTextField text1, text2, text3, text4;
    JLabel label;
    JButton button;
    JFrame frame;
    String nameProject, user, url, pass;
    JComboBox urlBox;
    String test08_DB = "jdbc:postgresql://172.16.35.25:5432/test08_DB";

    FrameCreate(String url, String nameProject, String user, String pass) {
        String[] items = {
            "  ",
            test08_DB
        };
        this.nameProject = nameProject;
        this.user = user;
        this.url = url;
        this.pass = pass;
        frame = new JFrame();//поработать над контейнером ,делать не через фрейм
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Toolkit kit = Toolkit.getDefaultToolkit();//поработать над расположением экрана
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - frame.getWidth()) / 4);
        if (x < 0) {
            x = 0;
        }
        int y = (int) ((screen.getHeight() - frame.getWidth()) / 4);
        if (y < 0) {
            y = 0;
        }
        frame.setBounds(x, y, frame.getWidth(), frame.getHeight());
//        int screenHeight = screen.height;
//        int screenWidght = screen.width;
//        frame.setLocation(screenHeight / 2, screenWidght / 2);

        label = new JLabel("Test label");//создаем текстовые поля и лейбл
        text1 = new JTextField("User", 25);
        text2 = new JTextField("NameProject", 25);
        text3 = new JTextField("Path to project", 25);
        text4 = new JTextField("Password", 25);
        urlBox = new JComboBox(items);

        frame.add(label);//помещаем их в наше окно
        frame.add(text1);
        frame.add(text2);
        frame.add(text3);
        frame.add(text4);
        frame.add(button = new JButton("Action"));
        frame.add(urlBox);

        frame.setPreferredSize(new Dimension(500, 150));//размеры окна
        frame.pack();
        frame.setVisible(true);

        ActionListener listener = new TestActionListener();//слушатели события нашей кнопки
        button.addActionListener(listener);
        //  urlBox.addActionListener(listener);
    }

    public class TestActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            user = text1.getText();//берем значения из текстового поля и помещаем в переменную
            nameProject = text2.getText();
            url = text3.getText();
            pass = text4.getText();
            if (url.isEmpty()) {
                url = (String) urlBox.getSelectedItem();

            }
            WriteConfigFile write = new WriteConfigFile();//вызываем класс записи файла конф
//                try {
//                    write.writeTXT(user, url, nameProject);
//                } catch (IOException ex) {
//                    Logger.getLogger(FrameCreate.class.getName()).log(Level.SEVERE, null, ex);
//                }
            try {
                write.writeXML(user, url, nameProject,pass);

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(FrameCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            //  frame.dispose();

        }

    }
}
