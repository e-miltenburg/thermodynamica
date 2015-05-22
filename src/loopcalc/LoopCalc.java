/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc;

import graphics.DebugPanel;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import processManager.MainThread;

/**
 *
 * @author User
 */
public class LoopCalc {

    private static int frames, ticks;
    private static int sPrintTicker = 0, sDebugTicker = 0;
    private boolean isRunning, vSync, shouldRender;
    private MainThread calc;
    private static int runTime = 0;
    private static String frameName = " ";
    private static DebugPanel debug = new DebugPanel();

    ;

    /**
     * @param args the command line arguments
     */
    public LoopCalc() throws IOException, FileNotFoundException, URISyntaxException {
        isRunning = true;
        vSync = false;
        //calc = new Calcer();        
        calc = new MainThread();
        //gameWindow();
        run();

    }
    private int WIDTH, HEIGHT, topOffset, borderSize;
    private JPanel game, top;
    private JFrame frame;
    private JButton btnBuyPart;
    private Point mousePos;

    private void gameWindow() {
        WIDTH = 800;
        HEIGHT = 600;
        topOffset = 25;
        borderSize = 3;

        game = new JPanel();
        game.setBounds(0, topOffset, WIDTH - borderSize * 2, HEIGHT - (topOffset + borderSize * 9));
        game.setBackground(Color.gray);
        game.setLayout(null);
        game.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.DARK_GRAY));
        game.setVisible(true);

        top = new JPanel();
        top.setBounds(0, 0, WIDTH - borderSize * 2, topOffset);
        top.setBackground(Color.CYAN);
        top.setLayout(null);
        top.setVisible(true);

        btnBuyPart = new JButton("buy");
        btnBuyPart.setBounds(borderSize + 5, borderSize + 5, 80, 25);
        btnBuyPart.setVisible(true);

        btnBuyPart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JPanel newPanel = new JPanel();
                newPanel = calc.getPanel();
                newPanel.setLocation(frame.getMousePosition());
                newPanel.setLocation(newPanel.getX(), newPanel.getY() - topOffset - borderSize);
                newPanel.setVisible(true);
                game.add(newPanel);

            }
        });

        game.add(btnBuyPart);

        frame = new JFrame(frameName);
        frame.add(game);
        // frame.add(top);
        frame.repaint();
        frame.setLayout(null);
        frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);

    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 50D;
        ticks = 0;
        frames = 0;
        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            if (vSync) {
                shouldRender = false;
            }
            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            //maximum frames
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                frame();

            }
            if (System.currentTimeMillis() - lastTimer > 1000) {
                lastTimer += 1000;
                second();

                frames = 0;
                ticks = 0;
            }
        }
    }

    private void tick() {
        calc.tick();
        writeDebug();
    }

    private void frame() {

    }

    private void second() {
        frameName = ticks + " ticks, " + frames + " frames, T+" + runTime + " seconds";
        calc.second();
        runTime++;

    }

    public static void main(String[] args) throws IOException, FileNotFoundException, URISyntaxException {

        LoopCalc main = new LoopCalc();
    }

    public static String getFrameName() {

        return frameName;
    }

    public static void print(String s) {
        if (sPrintTicker < 1000) {
            System.out.println(sPrintTicker + " > " + s);
        } else if (sPrintTicker < 100) {
            System.out.println(sPrintTicker + "  > " + s);
        } else if (sPrintTicker < 10) {
            System.out.println(sPrintTicker + "   > " + s);
        }else{
            System.out.println(sPrintTicker + "> " + s);
        }
        sPrintTicker++;
    }

    public static void debug(String s) {
        debug.setString(s);
    }

    private static void writeDebug() {
        debug.writeStrings();
    }

}
