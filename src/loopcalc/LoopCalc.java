/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc;

import processManager.MainThread;

/**
 *
 * @author User
 */
public class LoopCalc {

    private static int frames, ticks;
    private static int sPrintTicker = 0;
    private boolean isRunning, vSync, shouldRender;
    private MainThread calc;
    private static int runTime = 0;
    private static String frameName = " ";

    /**
     * @param args the command line arguments
     */
    public LoopCalc() {
        isRunning = true;
        vSync = false;
        //calc = new Calcer();        
        calc = new MainThread();
        run();

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
    }

    private void frame() {

    }

    private void second() {
        frameName = ticks + " ticks, " + frames + " frames, T+" + runTime + " seconds";
        calc.second();
        runTime++;
        
    }

    public static void main(String[] args) {

        LoopCalc main = new LoopCalc();
    }

    public static String getFrameName() {

        return frameName;
    }
    
    public static void print(String s){
        System.out.println(sPrintTicker+" "+s);
        sPrintTicker++;
    }

}
