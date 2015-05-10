/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processManager;

import graphics.DebugPanel;
import graphics.MainView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import loopcalc.LoopCalc;
import processManager.parts.*;

public class MainThread {

    private int Pman, slow = 0;

    private JPanel panel;
    private double Vzuig, Vpers, Hzuig, Hpers, Pzuig, Ppers, Rho, g, Pwzl, Pwpl, Labda, zuigZeta, persZeta,
            ZtotaalLengte, PtotaalLengte, viscositeit, Pdiameter, Zdiameter, ruwheid,
            Psnelheid, Zsnelheid, Volumestroom, Abuis, rendement, pompVermogen;
    private double opVoerDruk;
    private double Zfluid = 0, Pfluid = 0, gas = 0, fluid = 0;//fluid in kg's in the system
    private double oldFluid = 0;
    private Pomp pomp;
    private Boiler boiler;
    private Condenser condenser;
    private Turbine turbine;
    private BufferedReader in;
    private boolean printOnce = false;

    private MainView frame;

    //Thermodynamics
    public MainThread() throws FileNotFoundException, IOException {

        //readTXT();

        init();
    }

    private void readTXT() throws IOException {
        in = new BufferedReader(new FileReader("C:/test.txt"));
        String line;
        while ((line = in.readLine()) != null) {
            if(line.equals("boiler")){
                print("yay! "+line);
            }
    //print("booh"+line);
        }
        in.close();
    }

    public void init() throws IOException {

        g = 9.81;

        //water
        Rho = 1000;
        viscositeit = 0.00000131;

        Labda = 0.038;

        pomp = new Pomp(10, 0);
        pomp.buyPart("nieuwe pomp", 80, 1000, 100, 150);

        boiler = new Boiler(0, 5);
        boiler.buyPart("Nieuwe boiler", 30, 100, 100, 1000, 9000, 3.6);

        condenser = new Condenser(12, 5);
        condenser.buyPart("Condenser", 100, 80, 80, 1000, 9000, 1.3);

        turbine = new Turbine(10, 15);

        frame = new MainView(pomp.getJPanel(), boiler.getJPanel());
        sumThings();
        fill(100, 50);

    }

    public void tick() {
        //get stuff
        Volumestroom = (double) (pomp.getFlowrate()) / 3600;

        //do stuff
        Pman();
        fluidLevels();
        opvoerDruk();
        heat();

        //set stuff
        frame.setPman(Pman, (int) pompVermogen);
        frame.setSpeed(Meth.readbackdouble(Zsnelheid, 2), Meth.readbackdouble(Psnelheid, 2));
        pomp.tick(pompVermogen, Pman);
        debug("Pman: [" + Pman + " Pa] Pwpl: [" + Meth.readbackdouble(Pwpl, 1)
                + " Pa] Pwzl: [" + Meth.readbackdouble(Pwzl, 1) + " Pa]");

        debug("Zsnelheid: " + Meth.readbackdouble(Zsnelheid, 2) + "m/s Zdiameter: " + Meth.readbackdouble(Zdiameter, 1)
                + "mm Volumestroom: " + Meth.readbackdouble(Volumestroom * 3600, 1) + "m3/hour");

        debug("Psnelheid: " + Meth.readbackdouble(Psnelheid, 2) + "m/s Pdiameter: " + Meth.readbackdouble(Pdiameter, 1)
                + "mm Volumestroom: " + Meth.readbackdouble(Volumestroom, 1) + "m3/sec");

        debug("Pwzl: " + Meth.readbackdouble(Pwzl, 1) + "Pa Ztotaallengte: " + Meth.readbackdouble(ZtotaalLengte, 1)
                + "m Zdiameter: " + Zdiameter / 1000 + " Zsnelheid: " + Meth.readbackdouble(Zsnelheid, 1));

        debug("total fluid: " + Meth.readbackdouble(fluid, 3) + " Zfluid: " + Meth.readbackdouble(Zfluid, 1)
                + " Pfluid: " + Meth.readbackdouble(Pfluid, 1) + " gaslevel: " + Meth.readbackdouble(gas, 1));
        debug();

        frame.tick();

    }

    private void debug() {

    }

    public void second() {

        totalFluid();
        sumThings();

    }

    private void print(String s) {
        String message = "MT " + s;
        LoopCalc.print(message);
    }

    private void debug(String s) {
        String message = "MT: " + s;
        LoopCalc.debug(message);
    }

    private void sumThings() {
        double oldP = Pdiameter, oldZ = Zdiameter;

        zuigZeta = pomp.getZeta() + condenser.getZeta();
        persZeta = boiler.getZeta() + turbine.getZeta();

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

        if (pomp.getDiameterIn() > condenser.getDiameterOut()) {
            Zdiameter = condenser.getDiameterOut();

        } else {
            Zdiameter = pomp.getDiameterIn();
        }
        if (oldP != Pdiameter || oldZ != Zdiameter) {
            print("Pdiameter: " + Pdiameter);
            print("Zdiameter: " + Zdiameter);
        }

    }

    private void fill(int Zpercentage, int Ppercentage) {
        if (Zpercentage > 100) {
            print("Tried to fill more than 100%! Zpercentage is 100%");
            Zpercentage = 100;
        }
        if (Ppercentage > 100) {
            print("Tried to fill more than 100%! Ppercentage is 100%");
            Ppercentage = 100;
        }
        gas = 0;
        Zfluid = 0;
        Pfluid = 0;

        //double volume = boiler.getVolume() + condenser.getVolume() + (((Math.pow((0.09), 2) * Math.PI) / 4) * (PtotaalLengte + ZtotaalLengte));
        Zfluid = condenser.getVolume() * Rho * ((double) Zpercentage / 100) + Meth.getArea(Zdiameter) * ZtotaalLengte * Rho;
        print("volume condenser: " + Meth.readbackdouble(condenser.getVolume(), 2) + " Zfluid: " + Meth.readbackdouble(Zfluid, 1));

        condenser.setFill((Zfluid / Rho) - Meth.getArea(Zdiameter) * ZtotaalLengte);

        Pfluid = boiler.getVolume() * Rho * ((double) Ppercentage / 100) + Meth.getArea(Pdiameter) * PtotaalLengte * Rho;
        boiler.setFill((Pfluid / Rho) - Meth.getArea(Pdiameter) * PtotaalLengte);

        fluid = Zfluid + Pfluid + gas;
        oldFluid = fluid;

    }

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

            print("Zfluid is empty!\n  fluid: " + fluid);

        }
        if (Pfluid > boiler.getVolume() * Rho + Meth.getArea(Pdiameter) * PtotaalLengte * Rho) {
            overFlow();
        }
        if (Zfluid > condenser.getVolume() * Rho + Meth.getArea(Zdiameter) * ZtotaalLengte * Rho) {
            overFlow();
        }

        boiler.setFill((Pfluid / Rho) - ((Math.pow((Pdiameter / 1000), 2) * Math.PI) / 4) * PtotaalLengte);;
        condenser.setFill((Zfluid / Rho) - ((Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4) * ZtotaalLengte);

        frame.setBars((condenser.getFill() / condenser.getVolume() * 100), boiler.getFill() / boiler.getVolume() * 100);

    }
    private boolean emptyPrint = false;

    private void opvoerDruk() {
        double zuigKolom, persKolom, zuigHoogte, persHoogte;
        //zuig

        String zType = "niks";
        String pType = "niks";

        if (Zfluid <= (Hzuig * (Math.pow((Zdiameter / 1000), 2) * Math.PI) / 4)) {
            zuigHoogte = 0;
            zType = "eerste";
        } else if (Zfluid <= Meth.getArea(Zdiameter) * ZtotaalLengte) {

            zuigHoogte = (((Zfluid / Rho - (Hzuig * Meth.getArea(Zdiameter))))
                    / Meth.getArea(Zdiameter));

            zType = "tweede";
        } else {
            zuigHoogte = Vzuig;
            zType = "derde";
        }

        if (Pfluid <= (Hpers * Meth.getArea(Pdiameter)) * Rho) {
            persHoogte = 0;
            pType = "eerste";

        } else if (Pfluid <= Meth.getArea(Pdiameter) * PtotaalLengte * Rho) {
            persHoogte = (((Pfluid / Rho - (Hpers * Meth.getArea(Pdiameter))))
                    / Meth.getArea(Pdiameter));

            pType = "tweede";
        } else {
            persHoogte = Vpers;
            pType = "derde";
        }
        zuigKolom = zuigHoogte + condenser.getFillHeight();
        persKolom = persHoogte + boiler.getFillHeight();
        opVoerDruk = persKolom - zuigKolom;

        debug("Zuigkolom: " + Meth.readbackdouble(zuigKolom, 1) + " zuighoogte: " + Meth.readbackdouble(zuigHoogte, 1)
                + " condenser vulgraad: " + Meth.readbackdouble(condenser.getFillHeight(), 3) + " type: " + zType);

        debug("Perskolom: " + Meth.readbackdouble(persKolom, 1) + " pershoogte: "
                + Meth.readbackdouble(persHoogte, 1) + " boiler vulgraad: "
                + Meth.readbackdouble(boiler.getFillHeight(), 3) + " type: " + pType);

    }

    private void totalFluid() {

        fluid = Zfluid + Pfluid + gas;
        if (Meth.readbackdouble(fluid, 6) != Meth.readbackdouble(oldFluid, 6)) {
            print("Error line 186! leakage! Change: " + Meth.readbackdouble(Math.abs((fluid - oldFluid) * 100000), 2) + " liter");
        }
        oldFluid = fluid;
    }

    private void Pman() {

        Zsnelheid = Volumestroom / Meth.getArea(Zdiameter);
        Pwzl = Labda * (ZtotaalLengte / (Zdiameter / 1000)) * 0.5 * Rho * Math.pow(Zsnelheid, 2) + zuigZeta * 0.5 * Rho * Math.pow(Zsnelheid, 2);
        Psnelheid = Volumestroom / Meth.getArea(Pdiameter);
        Pwpl = Labda * (PtotaalLengte / (Pdiameter / 1000)) * 0.5 * Rho * Math.pow(Psnelheid, 2) + persZeta * 0.5 * Rho * Math.pow(Psnelheid, 2);
        pompVermogen = (Volumestroom * Pman);
        Pman = (int) ((opVoerDruk) * Rho * g + Pwpl + Pwzl);

    }

    public void heat() {
        //Work in Progress

    }

    private void overFlow() {
        Pfluid = boiler.getVolume() * Rho + Meth.getArea(Pdiameter) * PtotaalLengte * Rho;
    }
}
