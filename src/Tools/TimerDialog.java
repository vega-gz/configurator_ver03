/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import FrameCreate.TextEdit;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author nazarov
 */
public class TimerDialog {

    // --- выводом диалогового окна c Таймером с Отчетом времени КОТОРОЕ СЕЙЧАС  на компбьютере с  ---
    public static void showJOptionPaneClockTime() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    Date now = Calendar.getInstance().getTime();
                    final DateFormat time = new SimpleDateFormat("hh:mm:ss a.");

                    String s = time.format(now);

                    final JLabel label = new JLabel(s, JLabel.CENTER);
                    label.setFont(new Font("DigifaceWide Regular", Font.PLAIN, 20));

                    Timer t = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Date now = Calendar.getInstance().getTime();
                            label.setText(time.format(now));
                        }
                    });
                    t.setRepeats(true);
                    t.start();

                    int choice = JOptionPane.showConfirmDialog(null, label, "Alarm Clock", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    t.stop();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    {
                        Logger.getLogger(TextEdit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    // --- выводом диалогового окна c Таймером с Отчетом времени КОТОРОЕ СЕЙЧАС  на компбьютере с  ( не закрыть может доработать) ---
    public static void showJOptionReverseTime(String mes) {
        int reversTime = 5;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    JOptionPane jOptionPane1 = new javax.swing.JOptionPane();
                    final JButton btn = new JButton("Close");
                    btn.addActionListener(new ActionListener() { // Слушатель кнопки
                        public void actionPerformed(ActionEvent evt) {
                            
                            Window w = SwingUtilities.getWindowAncestor(btn);
                            if (w != null) {
                                w.setVisible(false);
                            }
                        }
                    });

                    final JLabel label = new JLabel(mes, JLabel.CENTER);
                    label.setFont(new Font("DigifaceWide Regular", Font.PLAIN, 20));

                    Timer t = new Timer(1000, new ActionListener() { // Каждую секунду
                        int reversTimeEnter = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ++reversTimeEnter;
                            int control = reversTime - reversTimeEnter;
                            label.setText(mes + " " + Integer.toString(control));
                            if (control <= 0) {
                                reversTimeEnter = 0;
                                //  надо видимо отказаться от этого а использовать свои панели
                            }
                        }
                    });
                    t.setRepeats(true);
                    t.start();

                    int choice = jOptionPane1.showConfirmDialog(null, label, "Alarm Clock", JOptionPane.PLAIN_MESSAGE);

                    t.stop();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    {
                        Logger.getLogger(TextEdit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    // --- тестовый таймер вызов таймера каждые int сек ---
    public static void getTimer(int milSec) {
        Timer timer = new Timer(milSec, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("WOW!");
            }
        });
        timer.start();
    }

    
    public static void main(String args[]) {


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }
}
