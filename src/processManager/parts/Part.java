/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processManager.parts;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import loopcalc.LoopCalc;

/**
 *
 * @author User
 */
public class Part{
  
    protected int partID, efficiency, xPos, yPos,diameterIn,diameterOut;
    protected double zeta;
    protected static int ID = 0;
    protected String name;
    
    //graphics
    protected JPanel panel;
    protected int borderSize = 2;
    

    public Part(int xPos, int yPos){
        
        this.partID = ID;ID++;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zeta = 0;
    }
    
    protected void drawPanel(){
        
        print("creating new panel: "+name+"  ID: "+partID);
        
   
        panel = new JPanel();
        panel.setLayout(null);

        panel.setVisible(true);
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, Color.black));
        panel.setLayout(null);
        panel.setSize(150, 130);
        
    }
        public JPanel getJPanel() {
            print("returned panel: " + name);
        return panel;
    }
    protected void buyPart(String name, int efficiency,int diameterIn,int diameterOut){
        this.name = name;
        this.efficiency = efficiency;
        this.diameterIn = diameterIn;
        this.diameterOut = diameterOut;
    }
    
    protected void setZeta(double zeta){
        this.zeta = zeta;
    }
    
    public static int getID(){
        return ID;
    }
    public int getpartID(){
        return partID;
    }
    public String getName(){
        return name;
    }
    public int getEffeciency(){
        return efficiency;
    }
    public int getXpos(){
        return xPos;
    }
    public int getYpos(){
        return yPos;
    }
    public double getZeta(){
        return zeta;
    }
    public int getDiameterIn(){
        return diameterIn;
    }
    public int getDiameterOut(){
        return diameterOut;
    }
        protected void print(String s) {
        String message = "PartID: "+partID+" name: "+name+"; "+s;
        LoopCalc.print(message);
    }
        protected void toDebug(String s){
            LoopCalc.debug(s);
            print("test (un)succesful");
        }
    
    
    
    
    
}
