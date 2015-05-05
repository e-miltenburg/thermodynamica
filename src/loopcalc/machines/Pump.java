/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc.machines;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author User
 */
public class Pump {

    private double maxFlowRate, flowRate;
    private final String name;

    public Pump(String name, int maxFlowRate) {

        this.name = name;
        this.maxFlowRate = maxFlowRate;
        flowRate = 0;

    }

    private int topOffset, borderSize;
    private JPanel game, top;
    private JLabel lblTitle, lblPump;
    private Font defaultFont;
    private JSlider pump;

    public JPanel drawPanel(int x, int y, int width, int height) {

        topOffset = 25;
        borderSize = 5;
        defaultFont = new Font("verdana", 2, 10);

        game = new JPanel();
        game.setBounds(x, y, width, height);
        game.setBackground(Color.gray);
        game.setLayout(null);
        game.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.red));
        game.setVisible(true);

        top = new JPanel();
        top.setBounds(5, 5, width - 10, topOffset);
        top.setBackground(Color.green);
        top.setLayout(null);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.red));
        top.setVisible(true);

        lblTitle = new JLabel();
        lblTitle.setText(name);
        lblTitle.setBounds(borderSize, 0, width, 20);
        lblTitle.setVisible(true);

        lblPump = new JLabel();
        lblPump.setText(name+":");
        lblPump.setBounds(borderSize+5, topOffset+borderSize, width-borderSize*2, 20);
        lblPump.setVisible(true);
        lblPump.setFont(defaultFont);

        pump = new JSlider(JSlider.VERTICAL, 0, 20, 0);
        pump.setPaintLabels(true);
        pump.setOpaque(false);
        pump.setMinorTickSpacing(2);
        pump.setPaintTicks(true);
        pump.setBounds(lblPump.getX(), lblPump.getY()+lblPump.getHeight()+5, 25, height-(topOffset+borderSize*2+lblPump.getY()));
        pump.setVisible(true);

        top.add(lblTitle);
        game.add(lblPump);
        game.add(pump);
        game.add(top);

        sliderHandler();

        return game;

    }

    private void sliderHandler() {

        pump.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();

                    flowRate = value * maxFlowRate / 20;
                }
            }
        });

    }
    
    //-----------------
    //-----Getters-----
    //-----------------
    public double getFlowRate(){
        return flowRate;
    }

}
