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
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import loopcalc.Calculations;
import loopcalc.machines.tickCalc.BoilerTick;

/**
 *
 * @author User
 */
public class Vat {

    private int volume, inlet, outlet;
    private String name;
    private double maxTemperature, maxPressure;
    private boolean exploded, molten;
    private Calculations calc;
    private double airPressure, kWaterTemp, r, steamDensity;
    private double fillPercentage, pressure, waterTemp;

    private JPanel game, top;
    private JLabel lblTitle, lblProgressVolume;
    private JProgressBar pgbVolume, pgbPressure, pgbTemperature;
    private JButton btnReset;
    private int topOffset, borderSize;

    public Vat(String name, int volume, int maxPressure, int maxTemperature, int inlet, int outlet) {

        this.name = name;

        this.volume = volume + 1; // the maximum fill amount for the boiler
        fillPercentage = 0.0;
        this.maxPressure = (double) maxPressure; // pressure before it blows
        this.maxTemperature = (double) maxTemperature;
        this.inlet = inlet; // radius of the inlet pipe
        this.outlet = outlet; // radius of the outlet pipe
        r = 8.314472;
        calc = new Calculations();
        calc.print(name,"Volume: " + volume + " Maximum pressure: " + maxPressure);
        exploded = false;
        molten = false;
        waterTemp = 20;
        fill(80);

    }

    /**
     * volume = the percentage you want to fill it
     *
     * @param volume
     */
    public void fill(int volume) {

        fillPercentage = (double) ((this.volume - 1) * (volume / 100.00));

        setSteamMass(this.volume, fillPercentage, waterTemp);
        calc.print(name, ("Filled to: " + fillPercentage));
    }

    private double airVolume, steamMass;

    public void setSteamMass(int volume, double fillPercentage, double waterTemp) {

        //steamMass = (volume-fillPercentage)/r/18.02;
        airVolume = (double) volume - fillPercentage;
        steamMass = airVolume / 134;
    }


    public void tick() {

        steamDensity = steamMass / airVolume;
        kWaterTemp = waterTemp + 273;
        //airPressure = kWaterTemp * r * (steamDensity / 18.02);
        airPressure = 1;

        updateLabels();

        if (waterTemp > 100) {

            double oldFill = fillPercentage;
            fillPercentage = fillPercentage - ((calc.boilingTemp(waterTemp)-airPressure) * 4.2);

            waterTemp = waterTemp - ((oldFill - fillPercentage) * 4.2);
        }

        if (!exploded && !molten) {

            if (waterTemp >= maxTemperature) {

                exploded = true;
                System.out.println("boiler melted");

            }
            if (pressure >= maxPressure) {
                exploded = true;
                System.out.println("boiler exploded");
            }

        }

    }

    private void updateLabels() {

        lblProgressVolume.setText("Level: " + calc.readbackdouble(fillPercentage, 100));
        pgbVolume.setValue((int) fillPercentage);
        pgbPressure.setString("barA: " + calc.readbackdouble(airPressure, 100));
        pgbPressure.setValue((int) (airPressure * 10));
        pgbTemperature.setValue((int) waterTemp * 10);
        pgbTemperature.setString("C \u00b0 " + calc.readbackdouble(waterTemp, 100));

    }

    //-----------------
    //-----Getters-----
    //-----------------
    public double getFill() {
        return fillPercentage;
    }

    public double getTemp() {
        return waterTemp;
    }

    //-----------------
    //-----Setters-----
    //-----------------
    public void setTemp(double waterTemp) {

        this.waterTemp = waterTemp;

    }

    //-----------------
    //-----JPanel------
    //-----------------
    public JPanel drawPanel(int x, int y, int width, int height) {

        topOffset = 25;
        borderSize = 5;

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
        lblTitle.setBounds(borderSize, 0, width, 20);
        lblTitle.setText(name);
        lblTitle.setVisible(true);
        top.add(lblTitle);

        pgbVolume = new JProgressBar(JProgressBar.VERTICAL, 0, volume);
        pgbVolume.setBounds(width - borderSize - 110, topOffset + 25, 100, 150);
        pgbVolume.setVisible(true);
        pgbVolume.setStringPainted(true);
        pgbVolume.setForeground(Color.red);

        lblProgressVolume = new JLabel();
        lblProgressVolume.setText("Level: ");
        lblProgressVolume.setBounds(pgbVolume.getX(), pgbVolume.getY() - 20, 100, 20);
        lblProgressVolume.setVisible(true);
        game.add(lblProgressVolume);

        pgbPressure = new JProgressBar(JProgressBar.HORIZONTAL, 0, (int) (maxPressure * 10));
        pgbPressure.setBounds(pgbVolume.getX(), pgbVolume.getY() + pgbVolume.getHeight() + 10, pgbVolume.getWidth(), 20);
        pgbPressure.setStringPainted(true);
        pgbPressure.setForeground(Color.red);
        pgbPressure.setString("Pressure");
        pgbPressure.setVisible(true);

        pgbTemperature = new JProgressBar(JProgressBar.HORIZONTAL, 0, (int) (maxTemperature * 10));
        pgbTemperature.setBounds(pgbVolume.getX(), pgbPressure.getY() + pgbPressure.getHeight() + 10, pgbPressure.getWidth(), 20);
        pgbTemperature.setVisible(true);
        pgbTemperature.setStringPainted(true);
        pgbTemperature.setForeground(Color.red);
        pgbTemperature.setString("Temperature");

        btnReset = new JButton();
        btnReset.setText("reset");
        btnReset.setBounds(borderSize + 5, topOffset + borderSize * 2, 80, 20);
        btnReset.setVisible(true);

        game.add(pgbVolume);
        game.add(top);
        game.add(pgbTemperature);
        game.add(pgbPressure);
        game.add(btnReset);
        buttonHandler();
        return game;

    }

    private void buttonHandler() {

        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                reset();

            }

        });
    }

    private void reset() {

        exploded = false;
        molten = false;
        waterTemp = 20;
        steamMass = 0;
        fill(80);
    }
}
