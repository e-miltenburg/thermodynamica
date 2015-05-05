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
import loopcalc.Calculations;

/**
 *
 * @author User
 */
public class Turbine {

    private double airPressure, airTemperature, efficiency, steamMass;
    private double eAirPressure, eAirTemperature, rotationSpeed, rotorLoad, friction;
    private Calculations calc;

    private double entryKJ;

    private final int maxRPM, maxPressure, rotorWeight, inlet, outlet;
    private final double r, heatCap;
    private final String name;
    private boolean isRunning;

    public Turbine(String name, int maxRPM, int maxPressure, int rotorWeight, int efficiency, int inlet, int outlet) {

        this.name = name;
        r = 8.314472;
        heatCap = 4.2;
        this.maxRPM = maxRPM;
        this.maxPressure = maxPressure;
        this.rotorWeight = rotorWeight;
        this.efficiency = efficiency;
        this.inlet = inlet;
        this.outlet = outlet;
        this.rotorLoad = 1;
        this.rotationSpeed = 0;
        calc = new Calculations();

        friction = 0.5;

    }

    public void tick(double steamMass, double airPressure, double airTemperature, double rotorLoad) {

        this.steamMass = steamMass;
        this.airPressure = airPressure;
        this.airTemperature = airTemperature;
        this.rotorLoad = rotorLoad;
        if (isRunning) {
            work();
            blade();
            blade();
            blade();
        }
        //updateLabels();
    }

    private double newPressure, kWaterTemp, airVolume,exitKJ,exitPressure, exitTemperature;

    private void work() {

      /*  steamMass = 0.05;
        airTemperature = 120;
        airPressure = 2;*/
        entryKJ = (airTemperature * (steamMass*airPressure) * heatCap / 1000);
        rotationSpeed = rotationSpeed + (entryKJ*efficiency/rotorWeight);
        

        exitKJ = entryKJ/2;

    }
    private void blade(){

                entryKJ = exitKJ;
        rotationSpeed = rotationSpeed + (entryKJ*efficiency/rotorWeight);
        exitKJ = entryKJ/2;
    }

    public void friction() {

       rotationSpeed -= rotationSpeed * friction / rotorWeight;

        /*if (rotationSpeed > 0.0) {
         rotationSpeed -= 0.01;
         }*/
    }

    public void updateLabels() {

        lblRPM.setText("speed: " + calc.readbackdouble(rotationSpeed, 100) + " rpm");
        lblSteam.setText("Steam: " + calc.readbackdouble(exitKJ, 100) + " g");

    }

    private int topOffset, borderSize;
    private Font defaultFont;
    private JPanel game, top;
    private JLabel lblTitle, lblPump, lblRPM, lblSteam;
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
        btnStart.setBounds(borderSize + 5, height - borderSize - 25, width - borderSize * 2 - 10, 20);
        btnStart.setText("On");
        btnStart.setVisible(true);

        lblRPM = new JLabel();
        lblRPM.setText("speed: " + calc.readbackdouble(rotationSpeed, 100) + " rpm");
        lblRPM.setBounds(borderSize + 5, topOffset + borderSize + 5, width - borderSize * 2, 20);
        lblRPM.setVisible(true);
        lblRPM.setFont(defaultFont);

        lblSteam = new JLabel();
        lblSteam.setText("Steam: " + calc.readbackdouble(steamMass, 100) + " g");
        lblSteam.setBounds(lblRPM.getX(), lblRPM.getY() + lblRPM.getHeight() + 5, width - borderSize * 2, 20);
        lblSteam.setVisible(true);
        lblSteam.setFont(defaultFont);

        lblPump = new JLabel();
        lblPump.setText("Pump1: ");
        lblPump.setBounds(30, 50, 20, 20);
        lblPump.setVisible(false);
        lblPump.setFont(defaultFont);

        //flowOut1, temp2, temp1, flowRate1
        top.add(lblTitle);
        game.add(lblRPM);
        game.add(lblSteam);
        game.add(lblPump);
        game.add(btnStart);
        game.add(top);
        buttonHandler();

        return game;

    }

    private void buttonHandler() {
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (isRunning) {
                    isRunning = false;
                    btnStart.setText("On");

                } else {
                    isRunning = true;
                    btnStart.setText("Off");
                }

            }
        });
    }

    //-----------------
    //-----Getters-----
    //-----------------
    public double getAirPressure() {
        return eAirPressure;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public double getAirTemperature() {
        return airTemperature;
    }

    //-----------------
    //-----Setters-----
    //-----------------
    public void Run(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
