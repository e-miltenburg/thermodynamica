package processManager.parts;

import graphics.Draw;
import graphics.MainView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import loopcalc.Calculations;
import loopcalc.LoopCalc;
import processManager.MainThread;
import processManager.Meth;

public class Pomp extends Part {

    private Calculations calc;
    private int maxCapacity, maxFlowrate, flowrate;
    private double usedCapacity;
    private boolean isRunning;

    //Graphics
    private JButton btnToggle, btnInfo,btnBuy;
    private JLabel lblStatus, lblCapacity;
    private JSlider slStroom;

    private JFrame info;


    public Pomp(int xPos, int yPos) {
        super(xPos, yPos);

 
        drawPanel();
    }
    public void buyPart(String name,int efficiency,int maxCap, int maxFlow,int diameter){
        super.buyPart(name,efficiency,diameter,diameter);
        this.maxCapacity = maxCap;
        this.maxFlowrate = maxFlow;
        slStroom.setMaximum(maxFlowrate);
        print("\n  "+name+" gekocht, id: "+partID+"\n  Rendement: "+efficiency+"%\n  maximum capaciteit: "+maxCapacity+
                " Watt\n  maximum flowrate: "+maxFlowrate+"[m3/h]\n  diameter: "+diameter+" mm");
    }

    public void togglePump() {
        if (isRunning == true) {
            isRunning = false;
            lblStatus.setText("Disabled");
        } else {
            isRunning = true;
            lblStatus.setText("enabled");
        }
    }

    public void tick(double uCap,int Pman) {
        this.usedCapacity = uCap / (efficiency / 100.0);
     

        if (this.usedCapacity > maxCapacity) {
            isRunning = false;
            lblStatus.setText("error!");
            lblCapacity.setText("0");
            slStroom.setValue(0);
            print("capacity: " + this.usedCapacity);
            print("max capacity: " + maxCapacity);
            print("pressure: "+Pman+" Pa");
        } else {
            if(usedCapacity <0)usedCapacity = 0;
            
            String used = " ";
            if(usedCapacity >= 1000000){
                used = "Used: " + Meth.readbackdouble(usedCapacity/1000000,1) + " MW (";
            }else if(usedCapacity >=1000){
                used = "Used: " + Meth.readbackdouble(usedCapacity/1000,1) + " kW (";
            }else{
                used = "Used: " + Meth.readbackdouble(usedCapacity,1) + " Watt (";
            }
            lblCapacity.setText(used+ Meth.readbackdouble((usedCapacity/maxCapacity)*100,1)+"%)");
        }
    }

    protected void drawPanel() {
        super.drawPanel();

     

        btnToggle = new JButton();
        btnToggle.setBounds(borderSize + 5, 5, panel.getWidth() - 15, 15);
        btnToggle.setVisible(true);
        btnToggle.setText("Toggle");

        lblStatus = new JLabel("Off");
        lblStatus.setBounds(btnToggle.getX(), btnToggle.getY() + btnToggle.getHeight(), panel.getWidth() - 15, 20);
        lblStatus.setVisible(true);

        lblCapacity = new JLabel("Used: " + usedCapacity + "("+ (usedCapacity/maxCapacity)*100+"%)");
        lblCapacity.setBounds(lblStatus.getX(), lblStatus.getY() + lblStatus.getHeight(), panel.getWidth() - 15, 20);
        lblCapacity.setVisible(true);

        slStroom = new JSlider(JSlider.HORIZONTAL, 0, maxFlowrate, 0);
        slStroom.setPaintLabels(true);
        slStroom.setOpaque(false);
        slStroom.setMinorTickSpacing(2);
        slStroom.setPaintTicks(false);
        slStroom.setBounds(lblCapacity.getX(), lblCapacity.getY() + lblCapacity.getHeight(), panel.getWidth() - 15, 20);
        slStroom.setVisible(true);

        btnInfo = new JButton();
        btnInfo.setText("show info");
        btnInfo.setBounds(slStroom.getX(), slStroom.getY() + slStroom.getHeight(), panel.getWidth() - 15, 20);
        btnInfo.setVisible(true);
        
        btnBuy = new JButton("buy Pump");
        btnBuy.setBounds(btnInfo.getX(), btnInfo.getY()+btnInfo.getHeight()+10, panel.getWidth()-15, 25);
        btnBuy.setVisible(true);
        
        addComponents();
        actionHandler();
    }
    private void addComponents(){
        panel.add(btnToggle);
        panel.add(lblStatus);
        panel.add(lblCapacity);
        panel.add(slStroom);
        panel.add(btnInfo);
        panel.add(btnBuy);
    }


    private void actionHandler() {

        btnToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                togglePump();
            }
        });
        slStroom.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    flowrate = slider.getValue();
                }
            }
        });
        btnInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                info = new JFrame("info");

                JPanel Info = new JPanel();

                Dimension label = new Dimension(200, 25);
                JLabel lblName, lblMaxCap, lblMaxFlow;

                lblName = new JLabel("ID: " + partID + " name: " + name);
                lblName.setVisible(true);
                lblName.setSize(label);
                lblName.setLocation(5, 5);

                lblMaxCap = new JLabel("maximum Capacity: " + maxCapacity);
                lblMaxCap.setVisible(true);
                lblMaxCap.setSize(label);
                lblMaxCap.setLocation(lblName.getX(), lblName.getY() + (int) label.getHeight());

                lblMaxFlow = new JLabel("Maximum flowrate: " + maxFlowrate);
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

                info.add(Info);
                info.setUndecorated(true);
                info.setLocation(MainView.getMouse());
                info.setSize((int) label.getWidth() + 10, 150);
                info.setResizable(false);
                info.setDefaultCloseOperation(3);
                info.setVisible(true);
                info.setAlwaysOnTop(true);

                btnClose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        info.dispose();
                    }
                });
                
                
              

            }
        });

        btnBuy.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e) {
              
              if(!isRunning){

                info = new JFrame("info");

                JPanel Info = new JPanel();
                Info.setVisible(true);
                
                JLabel lblTitle;
                JButton btnBuy1,btnBuy2,btnCancel;
                int spacing = 5;
                Dimension label = new Dimension(200, 25);
                
                btnBuy1 = new JButton("Buy pump 1");
                btnBuy1.setVisible(true);
                btnBuy1.setSize(label);
                btnBuy1.setLocation(5, 5);
                
                btnBuy2 = new JButton("buy pump 2");
                btnBuy2.setVisible(true);
                btnBuy2.setSize(label);
                btnBuy2.setLocation(btnBuy1.getX(), btnBuy1.getY()+btnBuy1.getHeight()+spacing);
                
                btnCancel = new JButton("cancel");
                btnCancel.setVisible(true);
                btnCancel.setSize(label);
                btnCancel.setLocation(btnBuy2.getX(),btnBuy2.getY()+btnBuy2.getHeight()+spacing);
                
                btnBuy1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buyPart("Pomp 1",80,6000,50,90);
                        info.dispose();
                    }
                });
                btnBuy2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buyPart("Pomp 2",70,2000000,850,90);
                        info.dispose();
                    }
                });
                
                
                
                btnCancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        info.dispose();
                    }
                });
                
                Info.setVisible(true);
                Info.setSize((int) label.getWidth() + 10, 150);
                Info.add(btnBuy1);
                Info.add(btnBuy2);
                Info.add(btnCancel);
                
                
                info.add(Info);
                
                info.setLocation(MainView.getMouse());
                info.setUndecorated(true);
                info.setSize((int) label.getWidth() + 10, 150);
                info.setResizable(false);
                info.setDefaultCloseOperation(3);
                info.setVisible(true);
                info.setAlwaysOnTop(true);
              }else print("System is running!");
                
            }
        });
        

    }

    public int getFlowrate() {
        if (isRunning) {
            return flowrate;
        } else {
            return 0;
        }
    }

}
