/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package loopcalc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class Chart {
    
    private final String name;
    private final int height,ticks;
    
    public Chart(String name, int height, int ticks){
        this.name = name;
        this.height = height;
        this.ticks = ticks;
    }
    public void tick(int pos){
        
    }
    private int topOffset, borderSize;
    private Font defaultFont;
    private JLabel lblTitle;
    private JPanel game,top,chart;
    
    public JPanel drawPanel(int x,int y,int width,int height){
     
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
        
        chart = new JPanel();
        chart.setBounds(borderSize+5,topOffset+borderSize+5,width-borderSize*2-10,height-(borderSize*2+topOffset+10));
        chart.setBackground(Color.blue);
        chart.setLayout(null);
        chart.setVisible(true);
        chart.setBorder(BorderFactory.createMatteBorder(borderSize,borderSize,borderSize,borderSize, Color.pink));
        

        
        game.add(chart);
        
        game.add(top);
        return game;
    }
}
