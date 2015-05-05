package loopcalc;

import loopcalc.machines.Boiler;
import javax.swing.JFrame;
import javax.swing.JPanel;
import loopcalc.machines.HeatExchanger;
import loopcalc.machines.Pump;
import loopcalc.machines.Turbine;
import loopcalc.machines.Vat;

/**
 *
 * @author User
 */
public class Calcer {

    private Boiler boiler;
    private HeatExchanger heatExchanger;
    private Vat vat;
    private Pump pump1,pump2;
    private Turbine turbine;
    private Chart chart;

    
    private int width, height;
    private JFrame frame;
    private JPanel Boiler, HeatExchanger, Vat,Pump1,Pump2,Turbine,Chart;
    private String frameName;

    public Calcer() {

        boiler = new Boiler(90000, 2000, 10, 200, 80, 80);
        boiler.fill(50);
        heatExchanger = new HeatExchanger("heatexchanger",10,200,80,80,80,80);
        vat=new Vat("vat",1000,10,200,80,80);
        pump1 = new Pump("Pump1",10);
        pump2 = new Pump("Pump2",10);
        turbine = new Turbine("Turbine",2000,10,100,70,80,80);
        chart = new Chart("testChart",50,10);
        width = 800;
        height = 600;
        frameName = "game";
        init();

    }

    public void tick() {
        boiler.tick();
        vat.tick();
        heatExchanger.tick(boiler.getTemp(),boiler.getFill(),vat.getTemp(),vat.getFill(),pump1.getFlowRate(),pump1.getFlowRate());
        
        if(turbine.isRunning()){
        
        turbine.tick(boiler.takeSteam(),boiler.getPressure(),boiler.getTemp(),0);
        
        }
        turbine.updateLabels();
        turbine.friction();
        
        //boiler.setTemp(turbine.getAirTemperature());
        if(heatExchanger.Running()){
        boiler.setTemp(heatExchanger.getTemp1());
        }
        vat.setTemp(heatExchanger.getTemp2());

    }

    public void init() {

        Boiler = boiler.drawPanel(5, height-385, 300, 350);
        HeatExchanger = heatExchanger.drawPanel(Boiler.getX()+Boiler.getWidth()+5, Boiler.getY(), 180, 200);
        Vat = vat.drawPanel(HeatExchanger.getX()+HeatExchanger.getWidth()+5, HeatExchanger.getY(), width-(HeatExchanger.getX()+HeatExchanger.getWidth())-15, 350);
        Pump1 = pump1.drawPanel(Boiler.getX()+Boiler.getWidth()+5, HeatExchanger.getY()+HeatExchanger.getHeight()+5, HeatExchanger.getWidth()/2-5, height-(HeatExchanger.getY()+HeatExchanger.getHeight()+40));
        Pump2 = pump2.drawPanel(Pump1.getX()+Pump1.getWidth()+10, HeatExchanger.getY()+HeatExchanger.getHeight()+5, HeatExchanger.getWidth()/2-5, height-(HeatExchanger.getY()+HeatExchanger.getHeight()+40));
        Turbine = turbine.drawPanel(Boiler.getX(), 5, 100, Boiler.getY()-10);
        Chart = chart.drawPanel(Turbine.getWidth()+Turbine.getX()+5, 5, 200, Turbine.getHeight());
        //Chart = chart.drawPanel(200,100,50,50);
        frame = new JFrame(frameName);
        frame.add(Boiler);
        frame.add(HeatExchanger);
        frame.add(Vat);
        frame.add(Pump1);
        frame.add(Pump2);
        frame.add(Turbine);
        frame.add(Chart);
        frame.repaint();
        frame.setLayout(null);
        frame.pack();
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }

    public void setFrameName(String name) {

        frame.setTitle(name);
    }

}

/*
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
 double steamDensity; // mass / pVolume
 double nGas = 0; //gas in Mol
 //waterproperties
 double pVolume; //volume in grams
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
 javaapplication3.Boiler boiler;
 Scanner scanner = new Scanner(System.in);
 //warmtewisselaar
 double pompSnelheid, wwKj, tIn1, tIn2, tOut1, tOut2;
 double tiKj1, tiKj2, toKj1, toKj2;

 double WWtank, WWtemp, WWkj;

 public Calcer() {
 pompSnelheid = 100;
 WWtank = 1000;
 WWtemp = 20;

 }
 public void run(){
 //System.out.println("press 1 for 500 watt, 1000 ml and 1 bar \n 2 for: 1000 watt, 1000 ml and 1 bar \n 3 for 500 watt, 1000 ml and 2 bar \n 4 for: 1000 watt, 1000 ml and 2 bar \n 5 for 500 watt, 1000 ml and 10 bar \n 6 for: 1000 watt, 1000 ml and 10 bar \n 7 to fill in manually");
 boiler = new javaapplication3.Boiler(scanner.nextInt());
 watt = boiler.ketel("watt");
 pVolume = boiler.ketel("pVolume");
 pressure = boiler.ketel("pressure");
 cVolume = boiler.ketel("cVolume");
        
 System.out.println(" watt: " + watt + " pVolume: " + pVolume + " pressure: " + pressure);

 System.out.println("insert watertemp:");
 waterTemp = scanner.nextInt();

 while (pVolume > 0) {
 second++;
 currentPressure = bCalc.Druk(waterTemp);
 KwaterTemp = waterTemp + 273;
 airVolume = cVolume - pVolume;
 //steam pressure
 steamDensity = steamMass / airVolume;
 airPressure = KwaterTemp * r * (steamDensity / 18.02) + pressure;
 //steam pressure
 if (currentPressure <= airPressure) {
 waterTemp = waterTemp + watt / (pVolume * heatCap);
 } else {
 pVolume = pVolume - (watt / verdampingsWarmte);
 steamMass = steamMass + watt / verdampingsWarmte;
 steamVol = steamMass * 1600;
 }
 Kj = Kj + watt / 1000;
 waterKJ = waterTemp * (pVolume + steamMass * airPressure) * heatCap / 1000;
            
 //print the boiler status
 teller++;
 if(teller == 1){
 bCalc.printBoil(second, Kj, waterTemp, pVolume, pressure, steamVol, airVolume, airPressure, waterKJ);
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
                
 wwKj = (pVolume - pompSnelheid)*waterTemp*heatCap/1000;
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
               
 }
 }
 System.out.println("done!");
 System.exit(0);

 }
        
 */
