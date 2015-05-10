package processManager;

import loopcalc.LoopCalc;

public class Meth {

    public static double readbackdouble(double Value, int multiplier) {
        double waarde = Value * Math.pow(10, multiplier);
        int value = (int) waarde;
        waarde = (double) value;
        waarde = waarde / Math.pow(10, multiplier);
        return waarde;

    }

    private static void print(String s) {
        String message = "Math class " + s;
        LoopCalc.print(message);
    }

    public static double boilingPressure(double waterTemp) {

        double pressure = -0.000000000000266 * Math.pow(waterTemp, 6) + 0.0000000001731191 * Math.pow
        (waterTemp, 5) - 0.000000030440722 * Math.pow(waterTemp, 4) + 0.0000042211287137 * Math.pow(waterTemp, 3)
                - 0.0002397414508841 * Math.pow(waterTemp, 2) + 0.0086591352573441 * waterTemp - 0.0966981313313458;
        return pressure;
    }
        public static double getArea(double diameter) { //takes diameter in mm, returns Area in m2
        double area = (Math.pow((diameter / 1000), 2) * Math.PI) / 4;
        return area;
    }
}
