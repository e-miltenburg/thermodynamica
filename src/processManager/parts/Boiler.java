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
public class Boiler extends Part{
    
    private int maxFuelFlow;
    private boolean isRunning;
    
    private int boilerDiameter, boilerHeight;
    private double filled;
    
    //graphics
    private JLabel lblName,lblPower;
    
    public Boiler(int xPos, int yPos){
        super(xPos,yPos);
        isRunning = false;
        boilerDiameter = 0;
        boilerHeight = 0;
        filled=0;
    
        drawPanel();

    }
    protected void drawPanel(){
        super.drawPanel();
        
        lblName = new JLabel("name: "+name);
        lblName.setVisible(true);
        lblName.setBounds(5, 5, 100, 25);
        
        
        addComponents();
        actionHandler();
    }
    private void addComponents(){
        panel.add(lblName);
    }
    private void actionHandler(){
        
    }
    /**
     * Buys a part for the boiler position in the simulator
     * @param name              Name of the boiler
     * @param efficiency        The efficiency in percent
     * @param diameterIn        diameter of the inward pipe in mm
     * @param diameterOut       diameter of the outward pipe in mm
     * @param boilerDiameter    diameter of the boiler in mm
     * @param boilerHeight      height of the boiler in meters
     * @param zeta              zeta value of the boiler
     */
    public void buyPart(String name,int efficiency,int diameterIn,int diameterOut,int boilerDiameter,int boilerHeight,double zeta){
        super.buyPart(name, efficiency, diameterIn, diameterOut);
        this.boilerDiameter = boilerDiameter;
        this.boilerHeight = boilerHeight;
        super.setZeta(zeta);
        print("\n  "+name+" gekocht, id: "+partID+"\n  Rendement: "+efficiency+"%\n  volume: "+Meth.readbackdouble(getVolume(), 0)+
                " m3\n  zeta waarde: "+Meth.readbackdouble(zeta, 1)+"\n  diameter: "+diameterIn+" mm in, "+diameterOut+"mm out");
    }
    public int getHeight(){
        return boilerHeight;
    }
    /**
     * returns the volume in m3
     * @return 
     */
    public double getVolume(){
        double volume = ((Math.pow((boilerDiameter/1000.0),2)*Math.PI)/4)*(boilerHeight/1000);
        return volume;
    }
        public void setFill(double m3){
            if(m3 < 0)m3 =0;
        this.filled = m3;

        
        //print("Capacity: "+Meth.readbackdouble(getVolume(), 2)+" m3, "+Meth.readbackdouble(m3, 2)+" Filled");
    }
        public double getFill(){
            return filled;
        }
            public double getFillHeight(){
        double fillHeight;
        
        fillHeight = filled/((Math.pow((boilerDiameter/1000.0),2)*Math.PI)/4);
        
        return fillHeight;
    }
            public int getBoilerDiameter(){
                return boilerDiameter;
            }

}
