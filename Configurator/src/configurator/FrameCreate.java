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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author cherepanov
 */
public class FrameCreate {

    JTextField text1, text2, text3;
    JLabel label;
    JButton button;
    JFrame frame;
    String pass, user, url;

    FrameCreate() {
        frame = new JFrame();//поработать над контейнером ,делать не через фрейм
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));

        Toolkit kit = Toolkit.getDefaultToolkit();//поработать над расположением экрана
        Dimension screen = kit.getScreenSize();
        int screenHeight = screen.height;
        int screenWidght = screen.width;
        frame.setLocation(screenHeight / 2, screenWidght / 2);

        label = new JLabel("Test label");
        text1 = new JTextField("User", 25);
        text2 = new JTextField("NameProject", 25);
        text3 = new JTextField("Path to project", 25);

        frame.add(label);
        frame.add(text1);
        frame.add(text2);
        frame.add(text3);
        frame.add(button = new JButton("Action"));

        frame.setPreferredSize(new Dimension(500, 150));

        frame.pack();
        frame.setVisible(true);

        ActionListener listener = new TestActionListener();
        button.addActionListener(listener);
    }

    public class TestActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            user = text1.getText();
            pass = text2.getText();
            url = text3.getText();
            WriteConfigFile write=new WriteConfigFile();
            try {
                write.write(pass, url, user);
            } catch (IOException ex) {
                Logger.getLogger(FrameCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            frame.dispose();
            

        }

    }
}
