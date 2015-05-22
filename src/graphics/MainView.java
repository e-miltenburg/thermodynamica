/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import loopcalc.LoopCalc;
import processManager.Meth;

/**
 *
 * @author User
 */
public class MainView extends JPanel {

    private String frameName;
    private JPanel controls;
    private final int WIDTH, HEIGHT, topOffset, borderSize;
    private Font defaultFont;
    private static JFrame frame;
    private JPanel game, top;
    private JLabel lblPman, lblSpeed, lblBoiler, lblCondenser, lblTemperature;
    private final String TITLE = "Mainthread";
    private JPanel jpPompWindow, jpBoilerWindow, jpInfo;
    private JButton btnInfo;
    private JProgressBar pgbBoiler, pgbCondenser;

    public MainView(JPanel pomp, JPanel boiler) {
        WIDTH = 800;
        HEIGHT = 600;
        topOffset = 25;
        borderSize = 3;
        frameName = "game";
        jpPompWindow = pomp;
        jpBoilerWindow = boiler;
        drawPanel(0, topOffset, WIDTH - borderSize * 2, HEIGHT - (topOffset + borderSize * 9));
        init();

    }

    private void init() {
        frame = new JFrame(frameName);
        frame.add(game);
        frame.add(top);
        frame.repaint();
        frame.setLayout(null);
        frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

    }

    public static Point getMouse() {
        return frame.getMousePosition();
    }

    public void drawPanel(int x, int y, int width, int height) {

        defaultFont = new Font("verdana", 2, 10);

        game = new JPanel();
        game.setBounds(x, y, width, height);
        game.setBackground(Color.gray);
        game.setLayout(null);
        game.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.DARK_GRAY));
        game.setVisible(true);
        
        controls = new JPanel();
        controls.setBounds(0, height - 150, width, 150);
        controls.setBackground(Color.DARK_GRAY);
        controls.setLayout(null);
        controls.setVisible(true);

        lblPman = new JLabel();
        lblPman.setText("test");
        lblPman.setBounds(borderSize + 5, borderSize, width, 20);
        lblPman.setVisible(true);

        lblSpeed = new JLabel();
        lblSpeed.setText("test");
        lblSpeed.setBounds(lblPman.getX(), lblPman.getY() + 25, width, 20);
        lblSpeed.setVisible(true);
        
        lblTemperature = new JLabel("temperature");
        lblTemperature.setBounds(lblSpeed.getX(), lblSpeed.getY()+lblSpeed.getHeight(), width, 20);
        lblTemperature.setVisible(true);
        

        jpInfo = new JPanel();
        jpInfo.setBounds(5, 5, controls.getWidth() - 60, controls.getHeight() - 10);
        jpInfo.setBackground(Color.WHITE);
        jpInfo.setLayout(null);
        jpInfo.setVisible(false);

        pgbBoiler = new JProgressBar(1, 0, 100);
        pgbBoiler.setBounds(game.getWidth() / 2 - 150, 150, 60, 100);
        pgbBoiler.setVisible(true);
        pgbBoiler.setValue(60);

        lblBoiler = new JLabel("test");
        lblBoiler.setBounds(pgbBoiler.getX(), pgbBoiler.getY() + pgbBoiler.getHeight() + 5, pgbBoiler.getWidth(), 25);
        lblBoiler.setVisible(true);

        pgbCondenser = new JProgressBar(1, 0, 100);
        pgbCondenser.setBounds(pgbBoiler.getX() + pgbBoiler.getWidth() + 20, pgbBoiler.getY(),
                pgbBoiler.getWidth(), pgbBoiler.getHeight());
        pgbCondenser.setVisible(true);
        pgbCondenser.setValue(20);

        lblCondenser = new JLabel("test");
        lblCondenser.setBounds(pgbCondenser.getX(), pgbCondenser.getY() + pgbCondenser.getHeight() + 5, pgbCondenser.getWidth(), 25);
        lblCondenser.setVisible(true);

        btnInfo = new JButton("info");
        btnInfo.setBounds(controls.getWidth() - 50, 5, 40, 25);
        btnInfo.setVisible(true);

        jpPompWindow.setVisible(true);
        jpPompWindow.setLocation(10, 10);

        jpBoilerWindow.setVisible(true);
        jpBoilerWindow.setLocation(jpPompWindow.getX() + jpPompWindow.getWidth() + 5, jpPompWindow.getY());

        controls.add(jpPompWindow);
        controls.add(jpBoilerWindow);
        controls.add(jpInfo);
        controls.add(btnInfo);

        game.add(lblPman);
        game.add(lblSpeed);
        game.add(lblTemperature);
        game.add(controls);
        game.add(pgbBoiler);
        game.add(lblCondenser);
        game.add(lblBoiler);
        game.add(pgbCondenser);
        
        top = new JPanel();
        top.setBounds(0, 0, width, topOffset);
        top.setBackground(Color.CYAN);
        top.setLayout(null);
        top.setVisible(true);
        actionHandler();

    }

    public void tick() {

        frame.setTitle(TITLE + "   " + LoopCalc.getFrameName());
        repaint();
    }

    public void setInfo(JPanel information) {

        if (jpInfo.isVisible()) {
            jpInfo.setVisible(false);
            jpPompWindow.setVisible(true);
            jpBoilerWindow.setVisible(true);
        } else {
            jpInfo.setVisible(true);
            jpPompWindow.setVisible(false);
            jpBoilerWindow.setVisible(false);
            for (int i = 0; i < information.getComponentCount(); i++) {
                jpInfo.add(information.getComponent(i));
            }

        }
    }

    public void setPman(int Pman, int pompVermogen) {
        lblPman.setText("Pman = " + Pman + " vermogen pomp: " + pompVermogen);
    }

    public void setSpeed(double Zsnelheid, double Psnelheid) {
        lblSpeed.setText("zuigSnelheid: " + Zsnelheid + ", persSnelheid: " + Psnelheid);

    }
    public void setTemperature(double Temp){
        lblTemperature.setText("Temperature: "+Meth.readbackdouble(Temp, 1));
    }

    public void setBars(double condenser, double boiler) {
        pgbCondenser.setValue((int) condenser);
        pgbBoiler.setValue((int) boiler);
        lblBoiler.setText(boiler + "%");
        lblCondenser.setText(condenser + "%");

    }

    private void actionHandler() {
        btnInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JPanel Info = new JPanel();

                Dimension label = new Dimension(200, 25);
                JLabel lblName, lblMaxCap, lblMaxFlow;

                lblName = new JLabel("ID: " + " name: ");
                lblName.setVisible(true);
                lblName.setSize(label);
                lblName.setLocation(5, 5);

                lblMaxCap = new JLabel("maximum Capacity: ");
                lblMaxCap.setVisible(true);
                lblMaxCap.setSize(label);
                lblMaxCap.setLocation(lblName.getX(), lblName.getY() + (int) label.getHeight());

                lblMaxFlow = new JLabel("Maximum flowrate: ");
                lblMaxFlow.setVisible(true);
                lblMaxFlow.setSize(label);
                lblMaxFlow.setLocation(lblMaxCap.getX(), lblMaxCap.getY() + (int) label.getHeight());

                JButton btnClose = new JButton("close");
                btnClose.setVisible(true);

                Info.setVisible(true);
                Info.setSize((int) label.getWidth() + 10, 150);
                Info.add(lblName);
                Info.add(lblMaxCap);
                Info.add(lblMaxFlow);
                Info.add(btnClose);

                setInfo(Info);
            }
        });
    }

    private void print(String s) {
        String message = "MainView " + s;
        LoopCalc.print(message);
    }

}
