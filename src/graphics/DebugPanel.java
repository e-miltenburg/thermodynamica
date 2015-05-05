/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class DebugPanel {

    private final int WIDTH, HEIGHT, borderSize;
    private static JFrame frame;
    private JPanel panel;
    private final String TITLE = "Mainthread";

    private JLabel lblOne, lblTwo, lblThree, lblFour, lblFive, lblSix, lblSeven;

    public DebugPanel() {
        WIDTH = 500;
        HEIGHT = 530;
        borderSize = 5;

        drawPanel();
        init();

    }

    private void init() {
        frame = new JFrame(TITLE);
        frame.setUndecorated(true);
        frame.add(panel);
        frame.repaint();
        frame.setLayout(null);
        frame.pack();
        frame.setSize(WIDTH + 6, HEIGHT);
        frame.setAlwaysOnTop(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width-WIDTH-25, screenSize.height-HEIGHT-50);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

    }

    private void drawPanel() {

        panel = new JPanel();
        panel.setVisible(true);
        panel.setSize(WIDTH + 6, HEIGHT);
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.DARK_GRAY));

        lblOne = new JLabel("test");
        lblOne.setForeground(Color.YELLOW);
        lblOne.setBounds(3, 3, WIDTH, WIDTH);
        lblOne.setVisible(true);

        lblTwo = new JLabel("test");
        lblTwo.setForeground(Color.YELLOW);
        lblTwo.setBounds(3, 3, WIDTH, WIDTH);
        lblTwo.setVisible(true);

        lblThree = new JLabel("test");
        lblThree.setForeground(Color.YELLOW);
        lblThree.setBounds(3, 3, WIDTH, WIDTH);
        lblThree.setVisible(true);

        lblFour = new JLabel("test");
        lblFour.setForeground(Color.YELLOW);
        lblFour.setBounds(3, 3, WIDTH, WIDTH);
        lblFour.setVisible(true);

        lblFive = new JLabel("test");
        lblFive.setForeground(Color.YELLOW);
        lblFive.setBounds(3, 3, WIDTH, WIDTH);
        lblFive.setVisible(true);

        lblSix = new JLabel("test");
        lblSix.setForeground(Color.YELLOW);
        lblSix.setBounds(3, 3, WIDTH, WIDTH);
        lblSix.setVisible(true);

        lblSeven = new JLabel("test");
        lblSeven.setForeground(Color.YELLOW);
        lblSeven.setBounds(3, 3, WIDTH, WIDTH);
        lblSeven.setVisible(true);

        panel.add(lblOne);
        panel.add(lblTwo);
        panel.add(lblThree);
        panel.add(lblFour);
        panel.add(lblFive);
        panel.add(lblSix);
        panel.add(lblSeven);

    }

    public void setStrings(String one, String two, String three,String four,String five,String six) {
        lblOne.setText(one);
        lblTwo.setText(two);
        lblThree.setText(three);
        lblFour.setText(four);
        lblFive.setText(five);
        lblSix.setText(six);
    }

}
