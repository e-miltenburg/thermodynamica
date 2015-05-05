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
public class Calculations {

    private double boilingPressure;
    public final double r = 8.314472;
    private static int ticker=0;

    public double readbackdouble(double Value, int multiplier) {
        double waarde = Value * multiplier;
        int value = (int) waarde;
        waarde = (double) value;
        waarde = waarde / multiplier;
        return waarde;
    }

    public double boilingTemp(double waterTemp) {

        boilingPressure = -0.000000000000266 * Math.pow(waterTemp, 6) + 0.0000000001731191 * Math.pow(waterTemp, 5)
                - 0.000000030440722 * Math.pow(waterTemp, 4) + 0.0000042211287137 * Math.pow(waterTemp, 3)
                - 0.0002397414508841 * Math.pow(waterTemp, 2) + 0.0086591352573441 * waterTemp - 0.0966981313313458;
        return boilingPressure;

    }
        public void print(String name,String string){
        
        System.out.println(ticker+" "+name+":\n "+string/*+"\n----------/"+name+"----------"*/);
        ticker++;
    }
    
    
}
