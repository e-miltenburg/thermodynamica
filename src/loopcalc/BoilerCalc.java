/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package loopcalc;

/**
 *
 * @author User
 */
public class BoilerCalc {
    
public BoilerCalc(){
    
}

    private static double readbackdouble(double Value,int multiplier){
    double waarde = Value*multiplier;
    int value = (int)waarde;
    waarde = (double)value;
    waarde=waarde/multiplier;
    return waarde; 
}
        public double Druk(double waterTemp) {

        double pressure = -0.000000000000266 * Math.pow(waterTemp, 6) + 0.0000000001731191 * Math.pow(waterTemp, 5) - 0.000000030440722 * Math.pow(waterTemp, 4) + 0.0000042211287137 * Math.pow(waterTemp, 3) - 0.0002397414508841 * Math.pow(waterTemp, 2) + 0.0086591352573441 * waterTemp - 0.0966981313313458;
        return pressure;
    }
    public void printWW(double tIn1 ,double tIn2, double tOut1,double tOut2,double waterTemp, double pompSnelheid){
     
     
        double Tin1 = readbackdouble(tIn1,100);
        double Tin2 = readbackdouble(tIn2,100);
        double Tout1 = readbackdouble(tOut1,100);
        double Tout2 = readbackdouble(tOut2,100);        
        double Wtemp = readbackdouble(waterTemp, 100);
        
        
        System.out.println("\nData for the heat exchanger:\n T1 in: "+Tin1+" T1 out: "+Tin2+" T2 in: "+Tout1+" T2 out: "+ Tout2+"\n Watertemp: "+Wtemp+" PompSnelheid: "+pompSnelheid);
        
        
        
    }

    public void printBoil(int second, double KJ, double watertemp, double Volume, double pressure, double Steam, double airVolume, double airpressure, double WaterKj) {
        int Pressure = (int) pressure;
        int Kj = (int) KJ;
        double Airvolume = readbackdouble(airVolume,1000);
        double waterTemp = readbackdouble(watertemp,100);
        double volume = readbackdouble(Volume,100);
        int steam = (int) Steam;       
        double airPressure = readbackdouble(airpressure,100);
        int waterKJ = (int) WaterKj;

        System.out.println(" --------------------------------------------\nsecond: " + second + " Total Kj: " + Kj + " waterKj: " + waterKJ + "\n water is: " + waterTemp + " waterpressure: " + Pressure + " airpressure: " + airPressure + "\n volume: " + volume + " airvolume: " + Airvolume +" Steam: "+steam);
    }
    
    
}
