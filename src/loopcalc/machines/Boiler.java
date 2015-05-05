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
public class Boiler {

    private int watt, volume, inlet, outlet;
    private String name;
    private double maxTemperature, maxPressure, fillPercentage, waterTemp, fillOld, joule;
    private boolean isRunning, exploded, blowOff;
    private BoilerTick bTick;
    private Calculations calc;

    private JPanel game, top;
    private JLabel lblTitle, lblPower, lblBlowOff, lblSlidePower, lblProgressVolume, lblSteam;
    private JProgressBar pgbVolume, pgbPressure, pgbTemperature;
    private JButton btnPower, btnBlowOff, btnFill, btnReset;
    private JSlider power;
    private int topOffset, borderSize;
    private Font defaultFont;
    private JTextField txtFill;

    public Boiler(int watt, int volume, int maxPressure, int maxTemperature, int inlet, int outlet) {

        name = "boiler";
        this.watt = watt; //maximum capacity for the heater
        joule = 0;
        this.volume = volume + 1; // the maximum fill amount for the boiler
        fillPercentage = 0.0;
        this.maxPressure = (double) maxPressure; // pressure before it blows
        this.maxTemperature = (double) maxTemperature;
        this.inlet = inlet; // radius of the inlet pipe
        this.outlet = outlet; // radius of the outlet pipe
        isRunning = false;
        blowOff = false;
        waterTemp = 0;
        bTick = new BoilerTick(name);
        calc = new Calculations();
        calc.print(name, "max Capacity: " + calc.readbackdouble(watt / 1000, 100) + "kW, Volume: " + volume + " cc" + "\n Maximum pressure: " + maxPressure + " Bara");

    }

    /**
     * volume = the percentage you want to fill it
     *
     * @param volume
     */
    public void fill(int volume) {
        if (!isRunning) {
            fillPercentage = (double) ((this.volume - 1) * (volume / 100.00));

            bTick.setSteamMass(this.volume, fillPercentage, waterTemp);
            calc.print(name, "Filled to: " + fillPercentage);

        } else {
            calc.print(name, "Boiler is still running, not possible to refill");
        }
    }

    public void tick() {

        updateLabels();

        bTick.blowOff(blowOff);
        if (!exploded && !bTick.getMolten()) {

            bTick.tick(joule, waterTemp, fillPercentage, volume, isRunning);
            waterTemp = bTick.getWaterTemp();
            fillOld = fillPercentage;
            fillPercentage = bTick.getFillPercentage();

            if (waterTemp >= maxTemperature) {

                exploded = true;
                calc.print(name, " boiler melted");
            }
            if (bTick.getPressure() >= maxPressure) {
                exploded = true;
                bTick.printBoil();
                calc.print(name, " boiler exploded");
            }

        }

    }

    private void updateLabels() {

        lblProgressVolume.setText("Level: " + getFillPercentage());
        pgbVolume.setValue(getFillPercentage());
        lblSlidePower.setText("throttle: " + joule);
        pgbPressure.setString("barA: " + calc.readbackdouble(getPressure(), 100));
        pgbPressure.setValue((int) (getPressure() * 10));
        pgbTemperature.setValue(getTemperature());
        pgbTemperature.setString("C \u00b0 " + getTemperature());
        
        lblSteam.setText("KJ: "+calc.readbackdouble(bTick.getKj(), 100)+" enthalpy: " + calc.readbackdouble(bTick.getEnthalpy(),100));
        //lblSteam.setText("steam in boiler: " + calc.readbackdouble(bTick.getSteam(),100));

    }

    //-----------------
    //-----Getters-----
    //-----------------
    public boolean isRunning() {
        return isRunning;
    }

    public boolean isBlowOff() {
        return blowOff;
    }

    public int getEnthalpy() {

        return (int) bTick.getEnthalpy();
    }

    public int getTemperature() {
        return (int) bTick.getWaterTemp();
    }

    public double getPressure() {
        double pressure = calc.readbackdouble(bTick.getPressure(), 1000);

        return pressure;
    }

    public int getVolume() {
        return volume;
    }

    public int getFillPercentage() {
        return (int) fillPercentage;
    }

    public int getMaxPressure() {
        return (int) maxPressure;
    }

    public int getMaxTemperature() {
        return (int) maxTemperature;
    }

    public double getFill() {
        return fillPercentage;
    }

    public double getTemp() {
        return waterTemp;
    }

    //-----------------
    //-----Setters-----
    //-----------------
    public double takeSteam() {
        return bTick.takeSteam();
    }

    public void setTemp(double waterTemp) {

        this.waterTemp = waterTemp;

    }

    public void running(boolean isRunning) {
        this.isRunning = isRunning;
        if (isRunning) {
            calc.print(name, " The system is running");
        } else {
            calc.print(name, " The system is not running");
        }

    }

    public void setPower(int wattPercentage) {
        joule = (watt * (wattPercentage / 100.00));
        System.out.println("Joules: " + joule);

    }

    public void blowOff(boolean blowOff) {
        this.blowOff = blowOff;
    }

    //-----------------
    //-----JPanel------
    //-----------------
    public JPanel drawPanel(int x, int y, int width, int height) {

        topOffset = 25;
        borderSize = 5;
        defaultFont = new Font("verdana", 2, 8);

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

        pgbVolume = new JProgressBar(JProgressBar.VERTICAL, 0, getVolume());
        pgbVolume.setBounds(width - borderSize - 110, topOffset + 25, 100, 150);
        pgbVolume.setVisible(true);
        pgbVolume.setStringPainted(true);
        pgbVolume.setForeground(Color.red);

        lblProgressVolume = new JLabel();
        lblProgressVolume.setText("Level: ");
        lblProgressVolume.setBounds(pgbVolume.getX(), pgbVolume.getY() - 20, 100, 20);
        lblProgressVolume.setVisible(true);
        game.add(lblProgressVolume);

        pgbPressure = new JProgressBar(JProgressBar.HORIZONTAL, 0, getMaxPressure() * 10);
        pgbPressure.setBounds(pgbVolume.getX(), pgbVolume.getY() + pgbVolume.getHeight() + 10, pgbVolume.getWidth(), 20);
        pgbPressure.setStringPainted(true);
        pgbPressure.setForeground(Color.red);
        pgbPressure.setString("Pressure");
        pgbPressure.setVisible(true);

        pgbTemperature = new JProgressBar(JProgressBar.HORIZONTAL, 0, getMaxTemperature());
        pgbTemperature.setBounds(pgbVolume.getX(), pgbPressure.getY() + pgbPressure.getHeight() + 10, pgbPressure.getWidth(), 20);
        pgbTemperature.setVisible(true);
        pgbTemperature.setStringPainted(true);
        pgbTemperature.setForeground(Color.red);
        pgbTemperature.setString("Temperature");

        btnPower = new JButton();
        btnPower.setText("On");
        btnPower.setLayout(null);
        btnPower.setBounds(borderSize + 10, height - borderSize - 60, 55, 55);
        btnPower.setVisible(true);
        game.add(btnPower);

        lblPower = new JLabel();
        lblPower.setText("Boiler:");
        lblPower.setBounds(btnPower.getX(), btnPower.getY() - 17, 200, 20);
        lblPower.setVisible(true);

        btnBlowOff = new JButton();
        btnBlowOff.setText("On");
        btnBlowOff.setBounds(borderSize + 70, height - borderSize - 60, 55, 55);
        btnBlowOff.setVisible(true);

        lblBlowOff = new JLabel();
        lblBlowOff.setFont(defaultFont);
        lblBlowOff.setText("Blow off valve:");
        lblBlowOff.setBounds(btnBlowOff.getX(), btnBlowOff.getY() - 17, 200, 20);
        lblBlowOff.setVisible(true);

        btnFill = new JButton();
        btnFill.setText("O");
        btnFill.setBounds(borderSize + 5, topOffset + 10, 55, 55);
        btnFill.setVisible(true);

        txtFill = new JTextField();
        txtFill.setBounds(btnFill.getX(), btnFill.getY() + btnFill.getHeight() + 10, btnFill.getWidth(), 20);

        lblSteam = new JLabel();
        lblSteam.setText("steam in boiler: " + calc.readbackdouble(bTick.getSteam(),100));
        lblSteam.setBounds(txtFill.getX(), txtFill.getY() + txtFill.getHeight() + 10, 200, 50);
        lblSteam.setVisible(true);

        btnReset = new JButton();
        btnReset.setText("reset");
        btnReset.setBounds(btnFill.getX() + btnFill.getWidth() + 10, btnFill.getY(), width - (btnFill.getX() + btnFill.getWidth()) - pgbVolume.getWidth() - borderSize - 25, 20);
        btnReset.setVisible(true);

        power = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        power.setPaintLabels(true);
        power.setOpaque(false);
        power.setMajorTickSpacing(50);
        power.setMinorTickSpacing(10);
        power.setPaintTicks(true);
        power.setBounds(btnBlowOff.getX() + btnBlowOff.getWidth() + 10, height - borderSize - 60, width - btnBlowOff.getX() - btnBlowOff.getWidth() - borderSize * 2 - 20, 50);
        power.setVisible(true);

        lblSlidePower = new JLabel();
        lblSlidePower.setText("throttle: ");
        lblSlidePower.setBounds(power.getX(), power.getY() - 25, 200, 20);
        lblSlidePower.setVisible(true);

        game.add(pgbVolume);
        game.add(top);
        game.add(lblSteam);
        game.add(pgbTemperature);
        game.add(pgbPressure);
        game.add(lblPower);
        game.add(btnBlowOff);
        game.add(lblBlowOff);
        game.add(btnFill);
        game.add(txtFill);
        game.add(btnReset);
        game.add(power);
        game.add(lblSlidePower);
        sliderHandler();
        buttonHandler();
        return game;

    }

    private void sliderHandler() {

        power.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    setPower(value);
                }
            }
        });

    }

    private void buttonHandler() {

        btnPower.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                powerOn();
            }
        });
        btnBlowOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                blowOff();

            }
        });
        btnFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int value;
                String dit = txtFill.getText();
                try {
                    value = Integer.parseInt(dit);
                } catch (NumberFormatException ex) {
                    value = 1;
                }
                if (value >= 100) {
                    value = 100;

                }
                fill(value);

            }
        });
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    reset();
                } else {
                    System.out.println("System still running!");
                }
            }
        });
    }

    private void powerOn() {
        if (isRunning()) {
            running(false);
            btnPower.setText("On");
        } else {
            running(true);
            btnPower.setText("Off");
        }
    }

    private void blowOff() {
        if (isBlowOff()) {
            blowOff(false);
            btnBlowOff.setText("On");
        } else {
            blowOff(true);
            btnBlowOff.setText("Off");
        }
    }

    private void reset() {
        bTick.reset();
        exploded = false;
        waterTemp = 20;
        bTick.setSteamMass(volume, fillPercentage, waterTemp);
    }
}
