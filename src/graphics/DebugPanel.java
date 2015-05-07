/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.Spring.width;
import loopcalc.LoopCalc;

/**
 *
 * @author User
 */
public class DebugPanel {

    private final int WIDTH, HEIGHT, borderSize;
    private static JFrame frame;
    private JPanel panel;
    private final String TITLE = "Mainthread";
    private int theY, labelHeight;
    private JButton btnClose;
    private JLabel lblOne, lblTwo, lblThree, lblFour, lblFive, lblSix, lblSeven,lblEight,lblNine,lblTen,lblEleven,lblTwelve;

    public DebugPanel() {
        WIDTH = 500;
        HEIGHT = 530;
        borderSize = 5;
        theY = borderSize;
        labelHeight = 20;

        drawPanel();
        init();
        actionHandler();

    }

    private void init() {
        frame = new JFrame(TITLE);
        frame.setUndecorated(true);

        frame.repaint();
        //frame.setLayout(null);
        frame.pack();
        frame.setSize(WIDTH + 6, HEIGHT);
        frame.setAlwaysOnTop(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width - WIDTH - 25, screenSize.height - HEIGHT - 50);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        frame.add(panel);
    }

    private void drawPanel() {

        panel = new JPanel();
        panel.setVisible(true);
        panel.setSize(WIDTH + 6, HEIGHT);
        panel.setBackground(Color.black);
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.DARK_GRAY));

        btnClose = new JButton("close");
        btnClose.setSize(80, 20);
        btnClose.setLocation(WIDTH - borderSize - btnClose.getWidth(), borderSize);
        btnClose.setVisible(true);

        lblOne = new JLabel();
        lblOne.setForeground(Color.YELLOW);
        lblOne.setBounds(borderSize + 2, borderSize + 2, WIDTH, labelHeight);
        lblOne.setVisible(true);

        lblTwo = new JLabel();
        lblTwo.setForeground(Color.YELLOW);
        lblTwo.setBounds(lblOne.getX(), setTheY(), WIDTH, labelHeight);
        lblTwo.setVisible(true);

        lblThree = new JLabel();
        lblThree.setForeground(Color.YELLOW);
        lblThree.setBounds(lblTwo.getX(), setTheY(), WIDTH, labelHeight);
        lblThree.setVisible(true);

        lblFour = new JLabel();
        lblFour.setForeground(Color.YELLOW);
        lblFour.setBounds(lblThree.getX(), setTheY(), WIDTH, labelHeight);
        lblFour.setVisible(true);

        lblFive = new JLabel();
        lblFive.setForeground(Color.YELLOW);
        lblFive.setBounds(lblFour.getX(), setTheY(), WIDTH, labelHeight);
        lblFive.setVisible(true);

        lblSix = new JLabel();
        lblSix.setForeground(Color.YELLOW);
        lblSix.setBounds(lblFive.getX(), setTheY(), WIDTH, labelHeight);
        lblSix.setVisible(true);

        lblSeven = new JLabel("test");
        lblSeven.setForeground(Color.YELLOW);
        lblSeven.setBounds(lblSix.getX(), setTheY(), WIDTH, labelHeight);
        lblSeven.setVisible(true);
        
        

        panel.add(btnClose);
        panel.add(lblOne);
        panel.add(lblTwo);
        panel.add(lblThree);
        panel.add(lblFour);
        panel.add(lblFive);
        panel.add(lblSix);
        panel.add(lblSeven);

    }

    public void setStrings(String one, String two, String three, String four, String five, String six, String seven) {
        lblOne.setText("1-> " + one);
        lblTwo.setText("2-> " + two);
        lblThree.setText("3-> " + three);
        lblFour.setText("4-> " + four);
        lblFive.setText("5-> " + five);
        lblSix.setText("6-> " + six);
        lblSeven.setText("7-> " + seven);

    }

    private int setTheY() {
        theY += labelHeight;
        return theY;
    }

    private void print(String s) {
        String message = "Debugpanel: " + s;
        LoopCalc.print(message);
    }

    private void actionHandler() {
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(3);

            }
        });
    }


}
