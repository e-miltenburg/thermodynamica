/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processManager.parts;

import javax.swing.JLabel;
import processManager.Meth;

/**
 *
 * @author Laptop
 */
public class Condenser extends Part {

    private int condenserDiameter, condenserHeight;
    private double filled;

    //graphics
    private JLabel lblName, lblVolume;

    public Condenser(int xPos, int yPos) {
        super(xPos, yPos);
        condenserDiameter = 0;
        condenserHeight = 0;
        filled = 0;
        drawPanel();
    }

    protected void drawPanel() {
        super.drawPanel();

        lblName = new JLabel("name: " + name);
        lblName.setVisible(true);
        lblName.setBounds(5, 5, 100, 25);

        lblVolume = new JLabel("Volume:" + Meth.readbackdouble(getVolume(), 1));

        addComponents();
        actionHandler();

    }

    private void addComponents() {
        panel.add(lblName);
        panel.add(lblVolume);
    }

    private void actionHandler() {

    }

    /**
     * Buys a part for the boiler position in the simulator
     *
     * @param name Name of the boiler
     * @param efficiency The efficiency in percent
     * @param diameterIn diameter of the inward pipe in mm
     * @param diameterOut diameter of the outward pipe in mm
     * @param boilerDiameter diameter of the boiler in mm
     * @param boilerHeight height of the boiler in meters
     * @param zeta zeta value of the boiler
     */
    public void buyPart(String name, int efficiency, int diameterIn, int diameterOut, int condenserDiameter, int condenserHeight, double zeta) {
        super.buyPart(name, efficiency, diameterIn, diameterOut);
        this.condenserDiameter = condenserDiameter;
        this.condenserHeight = condenserHeight;
        super.setZeta(zeta);
        print("\n  " + name + " gekocht, id: " + partID + "\n  Rendement: " + efficiency + "%\n  volume: " + Meth.readbackdouble(getVolume(), 0)
                + " m3\n  zeta waarde: " + Meth.readbackdouble(zeta, 1) + "\n  diameter: " + diameterIn + " mm in, " + diameterOut + "mm out");
    }

    /**
     * returns the volume in m3
     *
     * @return
     */
    public double getVolume() {
        double volume = ((Math.pow((condenserDiameter / 1000.0), 2) * Math.PI) / 4) * (condenserHeight / 1000.0);

        return volume;
    }

    public double getFill() {
        return filled;
    }
int count = 0;
    public void setFill(double m3) {
        if (m3 < 0) {
            m3 = 0;
        }
        this.filled = m3;
        //print("Capacity: "+Meth.readbackdouble(getVolume(), 2)+" m3, "+Meth.readbackdouble(m3, 2)+" Filled");
        if (filled <= 0) {
            
            if(count<=500)
            toDebug("empty!");
            
            else if(count >2000){
                 toDebug("tada!");
            }
            count++;
        }
    }

    public double getFillHeight() {
        double fillHeight;

        fillHeight = filled / ((Math.pow((condenserDiameter / 1000.0), 2) * Math.PI) / 4);

        return fillHeight;
    }

    public int getCondenserHeight() {
        return condenserHeight;
    }

    public void doSomething() {
        //test for github from computer
    }

}
