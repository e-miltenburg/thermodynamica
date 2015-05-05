/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 *
 * @author Laptop
 */
public class Draw extends JPanel{
  
    private int oCircle= 0,oCircleD=50, thickness = 3;
    private double leftX, leftY, topX, topY, bottomX, bottomY;
    private int iCircle= oCircle+thickness, iCircleD=oCircleD-thickness*2;
    
    public Draw(){
    }
    
    
    public void paintComponent(Graphics page) {
        super.paintComponent(page);

        page.setColor(Color.black);
        page.fillOval(oCircle, oCircle, oCircleD, oCircleD);
        page.setColor(Color.white);
        page.fillOval(iCircle, iCircle, iCircleD, iCircleD);
        
        page.setColor(Color.black);
        Graphics2D g2 = (Graphics2D) page;
        g2.setStroke(new BasicStroke(thickness));

        
        
        topX =iCircleD/2+iCircleD*(Math.cos(Math.PI/2.5));
        topY = iCircleD*(1-(Math.sin(Math.PI/2.5)))+thickness;
        bottomX = topX;
        bottomY = iCircleD- iCircleD*(1-(Math.sin(Math.PI/2.5)))+thickness;
        
        
        page.drawLine(oCircle, iCircleD/2+iCircle, (int)topX, (int)topY);
        page.drawLine((int)topX, (int)topY, (int)bottomX, (int)bottomY);
        page.drawLine((int)bottomX, (int)bottomY, oCircle, iCircleD/2+iCircle);

    }    
}
