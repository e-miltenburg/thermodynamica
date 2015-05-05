/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopcalc;

import loopcalc.machines.Boiler;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author User
 */
public class JavaApplication3 {

 
    public static void main(String[] args) {
        

        BoilerCalc bCalc = new BoilerCalc();

        //programproperties
        int second = 0;
        double heatCap = 4.2;
        double r = 8.314472; //R de gasconstante
        int teller = 0;

        //steamproperties
        double airVolume; //volume in boiler that is air;
        double paPressure; //pressure in pascal
        double airPressure; //in pa/m3
        double steamMass = 0;
        double steamVol = 0;
        double steamDensity; // mass / volume
        double nGas = 0; //gas in Mol
        //waterproperties
        double volume; //volume in grams
        double waterTemp;
        double pressure; //omgevingsdruk in bara
        double KwaterTemp; //watertemperatuur in kelvin;
        double waterKJ; //waterKJ is KJ berekend met water en temp
        //boilerproperties
        double Kj = 0;
        double cVolume = 10000; //inhoud van de boiler in cm3
        double watt;
        double currentPressure; //druk waarbij de vloeistof zou koken op huidige temperatuur in bara
        double maxPressure = 10; //pressure before boiler blows in bara
        double verdampingsWarmte = 2250; //Kj/kg
        Boiler boiler;
        Scanner scanner = new Scanner(System.in);
        //warmtewisselaar
        double pompSnelheid, wwKj, tIn1, tIn2, tOut1, tOut2;
        double tiKj1, tiKj2, toKj1, toKj2;
        pompSnelheid = 100;
        double WWtank, WWtemp, WWkj;
        WWtank = 1000;
        WWtemp = 20;
        volume =2;
        waterTemp = 0;
        pressure = 0;
        watt = 0;
        


        while (volume > 0) {
            second++;
            currentPressure = bCalc.Druk(waterTemp);
            KwaterTemp = waterTemp + 273;
            airVolume = cVolume - volume;
            //steam pressure
            steamDensity = steamMass / airVolume;
            airPressure = KwaterTemp * r * (steamDensity / 18.02) + pressure;
            //steam pressure
            if (currentPressure <= airPressure) {
                waterTemp = waterTemp + watt / (volume * heatCap);
            } else {
                volume = volume - (watt / verdampingsWarmte);
                steamMass = steamMass + watt / verdampingsWarmte;
                steamVol = steamMass * 1600;
            }
            Kj = Kj + watt / 1000;
            waterKJ = waterTemp * (volume + steamMass * airPressure) * heatCap / 1000;
            
            //print the boiler status
            teller++;
            if(teller == 1){
                bCalc.printBoil(second, Kj, waterTemp, volume, pressure, steamVol, airVolume, airPressure, waterKJ);
                teller = 0;
            }
            
            

            //warmtewisselaar
            if (waterTemp > 90) {
                

                
                
                tOut1 = WWtemp;
                tIn1 = waterTemp;
                tIn2 = tOut1*1.1;
                tiKj1 = (pompSnelheid/1000)*heatCap*tIn1;
                tiKj2 = (pompSnelheid/1000)*heatCap*tIn2;
                toKj1 = (pompSnelheid/1000)*heatCap*tOut1;
                
                toKj2 = tiKj1+toKj1-tiKj2;
                tOut2 = toKj2/(pompSnelheid/1000)/heatCap;
                
                wwKj = (volume - pompSnelheid)*waterTemp*heatCap/1000;
                waterTemp = (1000*(tiKj2+wwKj)/volume/heatCap);
                WWkj = WWtemp*heatCap*((WWtank-pompSnelheid)/1000);                
                WWtemp = (1000*(toKj2+WWkj)/WWtank/heatCap);
                
                bCalc.printWW(tIn1,tIn2, tOut1,tOut2, waterTemp, pompSnelheid); 
            }
            

            if (airPressure >= maxPressure) {
                System.out.println("Boiler haaaaas exploded!");
                break;
            }
            if (currentPressure >= maxPressure) {
                System.out.println("Boiler has exploded!!");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("done!");
        System.exit(0);

    }

}
