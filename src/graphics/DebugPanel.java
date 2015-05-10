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
import java.util.Arrays;
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

    private final int WIDTH, HEIGHT, borderSize, labelX;
    private static JFrame frame;
    private JPanel panel;
    private final String TITLE = "Mainthread";
    private int labelY, labelHeight;
    private JButton btnClose;
    private JLabel[] labels = new JLabel[30];
    private int labelTicker;
    
    public DebugPanel() {
        WIDTH = 500;
        HEIGHT = 530;
        borderSize = 5;
        labelY = borderSize;
        labelX = borderSize + 2;
        labelHeight = 20;
        labelTicker = 0;

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
        panel.add(btnClose);

    }

    public void setString(String S) {

        if (labelTicker < labels.length) {
            if (labels[labelTicker] == null) {

                labels[labelTicker] = new JLabel(S);
                labels[labelTicker].setVisible(true);
                labels[labelTicker].setForeground(Color.YELLOW);
                labels[labelTicker].setBounds(labelX, labelY, WIDTH, labelHeight);
                panel.add(labels[labelTicker]);
                labelY += labelHeight;
                
                frame.add(panel);
                frame.repaint();
            } else {

                labels[labelTicker].setText(S);
            }
labelTicker++;
        }

    }


    public void writeStrings() {

        labelTicker = 0;
    
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
