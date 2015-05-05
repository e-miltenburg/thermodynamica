/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc.machines.tickCalc;

import loopcalc.Calculations;

/**
 *
 * @author User
 */
public class BoilerTick {

    private double waterTemp, joule, kWaterTemp, steamDensity, airPressure, airVolume, volume, fillPercentage, boilingPressure, heatCap,
            steamMass, waterKJ, Kj, steamVol, verdampingsWarmte, r, ticks;

    private int i;//printcalc counter
    private Calculations calc;
    private String name;
    private boolean molten = false, blowOff = false, isRunning;

    public BoilerTick(String name) {

        this.name = name;
        heatCap = 4.2;
        verdampingsWarmte = 2250;
        steamMass = 0;
        r = 8.314472;
        waterKJ = 0;
        ticks = 50;
        calc = new Calculations();

    }

    public void tick(double joule, double waterTemp, double fillPeercentage, double volume, boolean isRunning) {

        this.isRunning = isRunning;
        this.fillPercentage = fillPeercentage;
        this.volume = volume;
        this.joule = joule;
        this.waterTemp = waterTemp;

        heat();
    }

    public void heat() {

        if (blowOff) {
            blowOffValve();
        }

        if (fillPercentage > 0.0) {
            boilingPressure = boilingTemp();
            kWaterTemp = waterTemp + 273;
            airVolume = volume - fillPercentage;
            //steam pressure
            steamDensity = steamMass / airVolume;
            airPressure = kWaterTemp * r * (steamDensity / 18.02);
            //steam pressure
            if (boilingPressure <= airPressure) {

                if (isRunning) {
                    waterTemp = waterTemp + (joule / ((steamMass * airPressure) + fillPercentage * heatCap)) / ticks;
                }

            } else {

                double oldFill = fillPercentage;
                fillPercentage = fillPercentage - ((boilingPressure - airPressure) * heatCap);
                waterTemp = waterTemp - ((oldFill - fillPercentage) * heatCap);

                if (isRunning) {
                    fillPercentage = fillPercentage - (joule / verdampingsWarmte) / ticks;
                    steamMass = steamMass + (joule / verdampingsWarmte) / ticks;

                }

                steamVol = steamMass * 1600;
            }
            if (isRunning) {
                Kj = Kj + (joule / 1000) / ticks;
                //waterKJ = waterTemp * fillPercentage * heatCap / 1000;
                
                waterKJ = waterTemp * (fillPercentage+ steamMass * airPressure) * heatCap / 1000;

                /*if(i==ticks){
                
                 i=0;
                 printBoil();
                 }else{
                 i++;
                 }*/
            }
        }

    }

    private double boilingTemp() {

        boilingPressure = -0.000000000000266 * Math.pow(waterTemp, 6) + 0.0000000001731191 * Math.pow(waterTemp, 5)
                - 0.000000030440722 * Math.pow(waterTemp, 4) + 0.0000042211287137 * Math.pow(waterTemp, 3)
                - 0.0002397414508841 * Math.pow(waterTemp, 2) + 0.0086591352573441 * waterTemp - 0.0966981313313458;
        return boilingPressure;

    }

    //-----------------
    //-----Getters-----
    //-----------------
    public boolean getBlowOff() {
        return blowOff;
    }

    public boolean getMolten() {
        return molten;
    }

    public double getWaterTemp() {
        return waterTemp;
    }

    public double getEnthalpy() {
        return waterKJ;
    }
    public double getKj(){
        return Kj;
    }

    public double getPressure() {
        return airPressure;
    }

    public double getFillPercentage() {
        return fillPercentage;
    }
    public double getSteam(){
        return steamMass;
    }
    public double takeSteam(){
        
        double selection = (airPressure-1)*0.05;
        
       /* if(selection >0.05){
            selection = 0.05;
        }*/
        
        steamMass -= selection;
       return selection;
        
        
        /*
        
        double selection = 0.01;
        
        if(airPressure > 1){
            steamMass -= 0.01;
            return selection;
        }else{
            return 0;
        }*/
    }

    public void printBoil() {
        int KJ = (int) Kj;
        double Airvolume = calc.readbackdouble(airVolume, 1000);
        double watertemp = calc.readbackdouble(waterTemp, 100);
        double Volume = calc.readbackdouble(fillPercentage, 100);
        double steam = calc.readbackdouble(steamMass, 100);
        double airpressure = calc.readbackdouble(airPressure, 10000);
        int waterKj = (int) waterKJ;
        double x = calc.readbackdouble(boilingPressure, 100);

        System.out.println(" --------------------------------------------\n Total Kj: " + KJ + " waterKj: "
                + waterKj + "\n water is: " + watertemp + " waterpressure: " + x + " airpressure: " + airpressure
                + "\n volume: " + Volume + " airvolume: " + Airvolume + " Steam: " + steam);
    }

    public void blowOffValve() {

        if (airPressure > 1.01) {

            steamMass = steamMass - airPressure * 0.08;
        }
        if (airPressure < 0.99) {
            steamMass = steamMass + airPressure * 0.02;
        }
    }

    //-----------------
    //-----Setters-----
    //-----------------
    private double n, t, v, p;

    public void setSteamMass(int volume, double fillPercentage, double waterTemp) {

        /*airVolume = (double) volume - fillPercentage;
         steamMass = airVolume / 134;
         //1 only for the printer
         steamDensity = steamMass / airVolume;
         kWaterTemp = waterTemp + 273;
         airPressure = kWaterTemp * r * (steamDensity / 18.02);*/
        //1
        kWaterTemp = waterTemp + 273;
        airVolume = (double) volume - fillPercentage;
        if (airVolume > 0) {
            while (airPressure <1.001) {
                steamMass = steamMass + 0.001;
                steamDensity = steamMass / airVolume;
                airPressure = kWaterTemp * r * (steamDensity / 18.02);
            }
        }
        if (airPressure > 1) {
            while (airPressure > 1.001) {
                steamMass = steamMass - 0.001;
                steamDensity = steamMass / airVolume;
                airPressure = kWaterTemp * r * (steamDensity / 18.02);
            }
        }

        calc.print(name, "steamDensity: " + calc.readbackdouble(steamDensity, 10000) + " steamMass: " + calc.readbackdouble(steamMass, 1000) + " pressure:" + calc.readbackdouble(airPressure, 100));
    }

    public void blowOff(boolean blowOff) {
        this.blowOff = blowOff;

    }

    public void reset() {
        molten = false;

    }

}
