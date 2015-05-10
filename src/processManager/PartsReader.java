/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.in;
import static loopcalc.LoopCalc.print;

/**
 *
 * @author User
 */
public class PartsReader {
    BufferedReader in;
    
    public PartsReader() throws FileNotFoundException, IOException{
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
    }
    

