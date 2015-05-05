/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processManager;

import graphics.MainView;
import graphics.Snowman;
import javax.swing.JPanel;
import loopcalc.LoopCalc;
import processManager.parts.*;

public class MainThread {

    private int Pman, slow = 0;

    private JPanel panel;
    private double Vzuig, Vpers, Hzuig, Hpers, Pzuig, Ppers, Rho, g, Pwzl, Pwpl, Labda, Zeta,
            ZtotaalLengte, PtotaalLengte, viscositeit, Pdiameter, Zdiameter, ruwheid,
            Psnelheid, Zsnelheid, Volumestroom, Abuis, rendement, pompVermogen;
    private Pomp pomp;
    private Boiler boiler;
    private Condenser condenser;
    private Turbine turbine;

    private MainView frame;

    
    //Thermodynamics
    private double Ptemp,Ztemp,Gtemp,Bpressure,heatCap,verdampingswarmte,r,Kelvin,boilingPressure,airVolume;
    
    
    
    
    public MainThread() {

        init();
    }

    public void init() {

        g = 9.81;
        Ptemp = 20;
        Ztemp = 20;
        Gtemp = 20;

        //water
        Rho = 1000;
        viscositeit = 0.00000131;
        heatCap = 4.186;
        verdampingswarmte = 2250;
        r = 8.314472;
        
        
        
        

        Labda = 0.038;

        Hzuig = 0;
        Hpers = 20;

        ZtotaalLengte = 0;

        pomp = new Pomp(10, 0);
        pomp.buyPart("nieuwe pomp", 80, 1000, 100, 30);

        boiler = new Boiler(0, 5);
        boiler.buyPart("Nieuwe boiler", 30, 25, 50, 1000, 2000, 3.6);

        condenser = new Condenser(12, 12);
        condenser.buyPart("Condenser", 100, 90, 90, 1500, 500, 1.3);

        turbine = new Turbine(10, 15);

        frame = new MainView(pomp.getJPanel(), boiler.getJPanel());
        sumThings();
        fill(100);

    }

    public void tick() {
        //get stuff
        Volumestroom = (double) (pomp.getFlowrate()) / 3600;
        Pdiameter = pomp.getDiameterOut();

        //do stuff
        Pman();
        fluidLevels();
        opvoerDruk();

        //set stuff
        frame.setPman(Pman, (int) pompVermogen);
        frame.setSpeed(Meth.readbackdouble(Zsnelheid, 2), Meth.readbackdouble(Psnelheid, 2));
        pomp.tick(pompVermogen, Pman);
        frame.tick();

    }

    public void second() {

        totalFluid();
    }

    private void print(String s) {
        String message = "MainThread " + s;
        LoopCalc.print(message);
    }

    //-----------------
    //--Thread setup---
    //-----------------
    private void sumThings() {
        Zeta = pomp.getZeta() + boiler.getZeta() + condenser.getZeta() + turbine.getZeta();

        Vpers = boiler.getYpos() - pomp.getYpos();
        if (pomp.getXpos() >= boiler.getXpos()) {
            Hpers = pomp.getXpos() - boiler.getXpos();
        } else {
            Hpers = boiler.getXpos() - pomp.getXpos();
        }
        Vzuig = condenser.getYpos() - pomp.getYpos();
        if (pomp.getXpos() >= condenser.getXpos()) {
            Hzuig = pomp.getXpos() - condenser.getXpos();
        } else {
            Hzuig = condenser.getXpos() - pomp.getXpos();
        }

        PtotaalLengte = Math.abs(Vpers) + Hpers;
        ZtotaalLengte = Math.abs(Vzuig) + Hzuig;

        if (pomp.getDiameterOut() > boiler.getDiameterIn()) {
            Pdiameter = boiler.getDiameterIn();
        } else {
            Pdiameter = pomp.getDiameterOut();
        }
        print("Pdiameter: " + Pdiameter);
        if (pomp.getDiameterIn() > condenser.getDiameterOut()) {
            Zdiameter = condenser.getDiameterOut();

        } else {
            Zdiameter = pomp.getDiameterIn();
        }
        print("Zdiameter: " + Zdiameter);
    }

    private void fill(int percentage) {
        if (percentage > 100) {
            print("Tried to fill more than 100%! percentage is 100%");
            percentage = 100;
        }
        gas = 0;
        Zfluid = 0;
        Pfluid = 0;

        //double volume = boiler.getVolume() + condenser.getVolume() + (((Math.pow((0.09), 2) * Math.PI) / 4) * (PtotaalLengte + ZtotaalLengte));
        Zfluid = condenser.getVolume() * Rho * ((double) percentage / 100);
        print("volume condenser: " + Meth.readbackdouble(condenser.getVolume(), 2) + " Zfluid: " + Meth.readbackdouble(Zfluid, 1));

        condenser.setFill((Zfluid / Rho) - ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte);

        fluid = Zfluid + Pfluid + gas;
        oldFluid = fluid;

    }
    private double Zfluid = 0, Pfluid = 0, gas = 0, fluid = 0;//fluid in kg's in the system
    private double oldFluid = 0;

    //-----------------------
    //--Thread Calculations--
    //-----------------------
    private void fluidLevels() {

        double deltaVol = (1000 * Volumestroom) / 50;

        if (Zfluid > 0) {
            if (Zfluid <= deltaVol) {
                Pfluid += Zfluid;
                Zfluid = 0;
            }
            Pfluid += deltaVol;
            Zfluid -= deltaVol;
        } else {
            print("Zfluid is empty!");
            print("fluid: " + fluid);
        }

        boiler.setFill((Pfluid / Rho) - ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) * PtotaalLengte);;
        condenser.setFill((Zfluid / Rho) - ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte);

        frame.setBars((condenser.getFill() / condenser.getVolume() * 100), boiler.getFill() / boiler.getVolume() * 100);

    }
    private double opVoerDruk;

    private void opvoerDruk() {
        double zuigKolom, persKolom, zuigHoogte, persHoogte;
         //zuig

        if (Zfluid <= (Hzuig * (Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4)) {
            zuigHoogte = 0;
        } else if (Zfluid <= ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte) {
            zuigHoogte = ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) / (Zfluid - (Hzuig * (Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4));
        } else {
            zuigHoogte = Zfluid;
        }
        zuigKolom = zuigHoogte + condenser.getFillHeight();
        if (Pfluid <= (Pzuig * (Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4)) {
            persHoogte = 0;
        } else if (Pfluid <= ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) * PtotaalLengte) {
            persHoogte = ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) / (Pfluid - (Pzuig * (Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4));
        } else {
            persHoogte = Pfluid;
        }
        persKolom = persHoogte + boiler.getFillHeight();
        opVoerDruk = persKolom - zuigKolom;
    }

    private void totalFluid() {
        fluid = Zfluid + Pfluid + gas;
        if (Meth.readbackdouble(fluid, 6) != Meth.readbackdouble(oldFluid, 6)) {
            print("Error line 186! leakage! Change: " + Meth.readbackdouble(Math.abs((fluid - oldFluid) * 100000), 2) + " liter");
        }
        oldFluid = fluid;
    }

    private void Pman() {

        Abuis = Math.pow(Zdiameter / 1000, 2) * (Math.PI / 4);
        Zsnelheid = Volumestroom / Abuis;
        Pwzl = Labda * (ZtotaalLengte / (Zdiameter / 1000)) * (1 / 2) * Rho * Math.pow(Zsnelheid, 2) + Zeta * (1 / 2) * Rho * Math.pow(Zsnelheid, 2);
        Abuis = Math.pow(Pdiameter / 1000, 2) * (Math.PI / 4);
        Psnelheid = Volumestroom / Abuis;
        Pwpl = Labda * (PtotaalLengte / (Pdiameter / 1000)) * 0.5 * Rho * Math.pow(Psnelheid, 2) + Zeta * 0.5 * Rho * Math.pow(Psnelheid, 2);
        pompVermogen = (Volumestroom * Pman);
        Pman = (int) ((opVoerDruk) * Rho * g + Pwpl + Pwzl);
    }
    
    private double sDichtheid, heatJoule;
    
        public void heat() {
            //Work in Progress

            boolean isRunning = true;
            double ticks = 50;
            double fillPercentage = boiler.getFillHeight()*((Math.pow(boiler.getBoilerDiameter()/1000, 2)*Math.PI)/4);
        
            boilingPressure = Meth.boilingPressure(Ptemp);
            Kelvin = Ptemp + 273;
            airVolume = boiler.getVolume() - fillPercentage;
            //steam pressure
            sDichtheid = gas / airVolume;
            opVoerDruk = Kelvin * r * (sDichtheid / 18.02);
            //steam pressure
            if (boilingPressure <= opVoerDruk) {
                if (isRunning) {
                    Ptemp = Ptemp + (heatJoule / ((gas * opVoerDruk) + fillPercentage * heatCap)) / ticks;
                }
            } else {

                double oldFill = fillPercentage;
                fillPercentage = fillPercentage - ((boilingPressure - opVoerDruk) * heatCap);
                Ptemp = Ptemp - ((oldFill - fillPercentage) * heatCap);

                if (isRunning) {
                    fillPercentage = fillPercentage - (heatJoule / verdampingswarmte) / ticks;
                    gas = gas + (heatJoule / verdampingswarmte) / ticks;
                }
            }
            }
        

    }

