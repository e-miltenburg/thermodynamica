/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processManager;

import graphics.DebugPanel;
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
    private DebugPanel debug;

    //Thermodynamics
    private double Ptemp, Ztemp, Gtemp, Bpressure, heatCap, verdampingswarmte, r, Kelvin, boilingPressure, airVolume;

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

        boiler = new Boiler(0, 50);
        boiler.buyPart("Nieuwe boiler", 30, 25, 50, 1000, 2000, 3.6);

        condenser = new Condenser(12, 50);
        condenser.buyPart("Condenser", 100, 90, 90, 1500, 500, 1.3);

        turbine = new Turbine(10, 15);

        frame = new MainView(pomp.getJPanel(), boiler.getJPanel());
        debug = new DebugPanel();
        sumThings();
        fill(50);

    }

    public void tick() {
        //get stuff
        Volumestroom = (double) (pomp.getFlowrate()) / 3600;
        Pdiameter = pomp.getDiameterOut();

        //do stuff
        Pman();
        fluidLevels();
        opvoerDruk();
        heat();

        //set stuff
        frame.setPman(Pman, (int) pompVermogen);
        frame.setSpeed(Meth.readbackdouble(Zsnelheid, 2), Meth.readbackdouble(Psnelheid, 2));
        frame.setTemperature(Ptemp);
        pomp.tick(pompVermogen, Pman);
        debug();
        frame.tick();

    }
    String STzdruk,STpdruk;

    private void debug() {

        String STdichtheid = "dichtheid: " + sDichtheid + " gas: " + gas + " airVolume: " + airVolume;

        String STfluid = "total fluid: " + Meth.readbackdouble(fluid, 3) + " Zfluid: " + Meth.readbackdouble(Zfluid, 1)
                + " Pfluid: " + Meth.readbackdouble(Pfluid, 1) + " gaslevel: " + Meth.readbackdouble(gas, 1);

        String STpman = "Pman: " + Pman + " opvoerdruk: " + Meth.readbackdouble(opVoerDruk, 1) + "m Pwpl: " + Meth.readbackdouble(Pwpl, 1)
                + " Pwzl: " + Meth.readbackdouble(Pwzl, 1);

        String STpwzl = "Pwzl: " + Meth.readbackdouble(Pwzl, 1) + "Pa Ztotaallengte: " + Meth.readbackdouble(ZtotaalLengte, 1)
                + "m Zdiameter: " + Zdiameter / 1000 + " Zsnelheid: " + Meth.readbackdouble(Zsnelheid, 1);

        debug.setStrings(STpman, STdichtheid, STfluid, STpwzl, STzdruk,STpdruk);
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

    private void fill(int Zpercentage) {
        if (Zpercentage > 100) {
            print("Tried to fill more than 100%! percentage is 100%");
            Zpercentage = 100;
        }
        int Ppercentage = 0;
        gas = 0;
        Zfluid = 0;
        Pfluid = 0;

        //double volume = boiler.getVolume() + condenser.getVolume() + (((Math.pow((0.09), 2) * Math.PI) / 4) * (PtotaalLengte + ZtotaalLengte));
        Zfluid = condenser.getVolume() * Rho * ((double) Zpercentage / 100);
        print("volume condenser: " + Meth.readbackdouble(condenser.getVolume(), 2) + " Zfluid: " + Meth.readbackdouble(Zfluid, 1));

        condenser.setFill((Zfluid / Rho) - ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte);
        
        Pfluid = boiler.getVolume() * Rho * ((double) Ppercentage / 100);
        boiler.setFill((Zfluid / Rho) - ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte);

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
        
        String zType = "niks";
        String pType = "niks";

        if (Zfluid <= (Hzuig * (Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4)) {
            zuigHoogte = 0;
            zType = "eerste";
        } else if (Zfluid <= ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte) {
            zuigHoogte = ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) / (Zfluid - (Hzuig * (Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4));
            zType = "tweede";
        } else {
            zuigHoogte = Zfluid;
            zType = "derde";
        }
        zuigKolom = zuigHoogte + condenser.getFillHeight();
        if (Pfluid <= (Hpers * (Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4)*Rho) {
            persHoogte = 0;
            pType = "eerste";
            
        } else if (Pfluid <= ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) * PtotaalLengte*Rho) {
            persHoogte = ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) / (Pfluid - (Hpers * (Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4));
            pType = "tweede";
        } else {
            persHoogte = Vpers;
            pType = "derde";
        }
        persKolom = persHoogte + boiler.getFillHeight();
        opVoerDruk = persKolom - zuigKolom;

        STzdruk = "Zuigkolom: " + Meth.readbackdouble(zuigKolom, 1) + " zuighoogte: " + Meth.readbackdouble(zuigHoogte, 1)
                + " condenser vulgraad: " + Meth.readbackdouble(condenser.getFillHeight(), 3)+" type: "+zType;
        
        STpdruk = "Perskolom: "
                + Meth.readbackdouble(persKolom, 1) + " pershoogte: " + Meth.readbackdouble(persHoogte, 1) + " boiler vulgraad: "
                + Meth.readbackdouble(boiler.getFillHeight(), 3)+" type: "+pType;

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
        Pwzl = Labda * (ZtotaalLengte / (Zdiameter / 1000)) * (1 / 2) * Rho * Math.pow(Zsnelheid, 2) + Zeta * 0.5 * Rho * Math.pow(Zsnelheid, 2);
        Abuis = Math.pow(Pdiameter / 1000, 2) * (Math.PI / 4);
        Psnelheid = Volumestroom / Abuis;
        Pwpl = Labda * (PtotaalLengte / (Pdiameter / 1000)) * 0.5 * Rho * Math.pow(Psnelheid, 2) + Zeta * 0.5 * Rho * Math.pow(Psnelheid, 2);
        pompVermogen = (Volumestroom * Pman);
        Pman = (int) ((opVoerDruk) * Rho * g + Pwpl + Pwzl);

    }

    private double sDichtheid, heatJoule = 500;

    public void heat() {
        //Work in Progress

    }
}
