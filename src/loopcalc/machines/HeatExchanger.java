/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc.machines;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import loopcalc.Calculations;

/**
 *
 * @author User
 */
public class HeatExchanger {

    private final int maxPressure, maxTemperature, pipe1in, pipe2in, pipe1out, pipe2out;
    private final String name;
    private Calculations calc;
    private boolean isRunning;
    private JLabel lblInlet1, lblInlet2, lblOutlet1, lblOutlet2, lblPump1, lblPump2;

    private double flow1inKJ, flow1outKJ, flow2inKJ, flow2outKJ, temp1KJ, temp2KJ, efficiency;
    private double flowIn1, flowIn2, flowOut1, flowOut2, flowRate1, flowRate2, heatCap;

    public HeatExchanger(String name, int maxPressure, int maxTemperature, int inlet1, int inlet2, int outlet1, int outlet2) {

        this.maxPressure = maxPressure;
        this.maxTemperature = maxTemperature;
        this.name = name;
        this.pipe1in = inlet1;
        this.pipe1out = outlet1;
        this.pipe2in = inlet2;
        this.pipe2out = outlet2;
        this.efficiency = 1.0;

        heatCap = 4.2;
        flowRate1 = 5;
        flowRate2 = 5;

        calc = new Calculations();

    }

    public void tick(double temp1, double volume1, double temp2, double volume2, double flowRate1, double flowRate2) {
        run(temp1, volume1, temp2, volume2, flowRate1, flowRate2);
        updateLabels();

    }

    private void updateLabels() {
        lblInlet1.setText("Inlet1: " + calc.readbackdouble(flowIn1, 10));
        lblInlet2.setText("Inlet2: " + calc.readbackdouble(flowIn2, 10));
        lblOutlet1.setText("Ou1: " + calc.readbackdouble(flowOut1, 10));
        lblOutlet2.setText("Ou2: " + calc.readbackdouble(flowOut2, 10));
        lblPump1.setText("Pump1: " + flowRate1);
        lblPump2.setText("Pump2: " + flowRate2);
    }
    private double Temp2;

    private void run(double temp1, double volume1, double temp2, double volume2, double flowRate1, double flowRate2) {

        this.flowRate1 = flowRate1;
        this.flowRate2 = flowRate2;

        this.flowIn1 = temp1;
        this.flowIn2 = temp2;

        if (isRunning) {

            flowOut1 = flowIn2 * ((Math.abs(flowIn1 - flowIn2) * 0.005) + 1);
            //flowOut1 = flowIn2 * efficiency;            
            flow1inKJ = (flowRate1 / 1000) * heatCap * flowIn1;
            flow1outKJ = (flowRate1 / 1000) * heatCap * flowOut1;
            flow2inKJ = (flowRate2 / 1000) * heatCap * flowIn2;

            flow2outKJ = flow1inKJ + flow2inKJ - flow1outKJ;
            flowOut2 = flow2outKJ / (flowRate2 / 1000) / heatCap;

            temp1KJ = (volume1 - flowRate1) * flowIn1 * heatCap / 1000;
            flowIn1 = (1000 * (flow1outKJ + temp1KJ) / volume1 / heatCap);
            temp2KJ = temp2 * heatCap * ((volume2 - flowRate2) / 1000);
            temp2 = (1000 * (flow2outKJ + temp2KJ) / volume2 / heatCap);
            Temp2 = temp2;
            
        }

    }

    public void printWW(double tIn1, double tIn2, double tOut1, double tOut2, double waterTemp, double pompSnelheid) {

        double Tin1 = calc.readbackdouble(tIn1, 100);
        double Tin2 = calc.readbackdouble(tIn2, 100);
        double Tout1 = calc.readbackdouble(tOut1, 100);
        double Tout2 = calc.readbackdouble(tOut2, 100);
        double Wtemp = calc.readbackdouble(waterTemp, 100);

        System.out.println("\nData for the heat exchanger:\n T1 in: " + Tin1 + " T1 out: " + Tin2 + " T2 in: " + Tout1 + " T2 out: " + Tout2 + "\n Watertemp: " + Wtemp + " PompSnelheid: " + pompSnelheid);

    }
    public boolean Running(){
        return isRunning;
    }

    public double getTemp1() {
        return flowIn1;

    }

    public double getTemp2() {
        if (isRunning) {
            return Temp2;
        } else {
            return flowIn2;
        }
    }

    private JPanel game, top;
    private int borderSize, topOffset;
    private Font defaultFont;
    private JLabel lblTitle;
    private JButton btnStart;

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

        btnStart = new JButton();
        btnStart.setBounds(borderSize + 10, topOffset + borderSize + 10, 80, 50);
        btnStart.setText("test");
        btnStart.setVisible(true);

        lblInlet1 = new JLabel();
        lblInlet1.setText("Inlet1: " + flowIn1);
        lblInlet1.setBounds(borderSize + 5, btnStart.getY() + btnStart.getHeight() + 10, width - (width / 2) - (borderSize * 2), 20);
        lblInlet1.setVisible(true);
        lblInlet1.setFont(defaultFont);

        lblInlet2 = new JLabel();
        lblInlet2.setText("Inlet2: " + flowIn2);
        lblInlet2.setBounds(lblInlet1.getX() + lblInlet1.getWidth() + 5, lblInlet1.getY(), lblInlet1.getWidth(), 20);
        lblInlet2.setVisible(true);
        lblInlet2.setFont(defaultFont);

        lblOutlet1 = new JLabel();
        lblOutlet1.setText("Outlet1: " + flowIn2);
        lblOutlet1.setBounds(lblInlet1.getX(), lblInlet1.getY() + lblInlet1.getHeight() + 10, lblInlet1.getWidth(), 20);
        lblOutlet1.setVisible(true);
        lblOutlet1.setFont(defaultFont);

        lblOutlet2 = new JLabel();
        lblOutlet2.setText("Outlet2: " + flowIn2);
        lblOutlet2.setBounds(lblInlet2.getX(), lblInlet2.getY() + lblInlet2.getHeight() + 10, lblInlet2.getWidth(), 20);
        lblOutlet2.setVisible(true);
        lblOutlet2.setFont(defaultFont);

        lblPump1 = new JLabel();
        lblPump1.setText("Pump1: " + flowRate1);
        lblPump1.setBounds(lblOutlet1.getX(), lblOutlet1.getY() + lblOutlet1.getHeight() + 10, lblOutlet1.getWidth(), 20);
        lblPump1.setVisible(true);
        lblPump1.setFont(defaultFont);

        lblPump2 = new JLabel();
        lblPump2.setText("Pump2: " + flowRate2);
        lblPump2.setBounds(lblOutlet2.getX(), lblOutlet2.getY() + lblOutlet2.getHeight() + 10, lblOutlet2.getWidth(), 20);
        lblPump2.setVisible(true);
        lblPump2.setFont(defaultFont);

        top.add(lblTitle);

        game.add(lblInlet1);
        game.add(lblInlet2);
        game.add(lblOutlet1);
        game.add(lblOutlet2);
        game.add(lblPump1);
        game.add(lblPump2);
        game.add(btnStart);
        game.add(top);
        buttonHandler();

        return game;
        
        

    }

    private void buttonHandler() {
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                running();
            }
        });
    }

    private void running() {
        if (isRunning) {
            isRunning = false;
            btnStart.setText("Turn on");

        } else {
            isRunning = true;
            btnStart.setText("Turn off");
        }
    }

}
