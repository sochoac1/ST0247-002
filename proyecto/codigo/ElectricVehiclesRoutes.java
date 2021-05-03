/**
 *
 * @author ljpalaciom
 */
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
public class ElectricVehiclesRoutes {
    ArrayList<ArrayList<Integer>> routes;
    int numberRoutes;
    int n, m, u, breaks;
    public static double r, speed, Tmax, Smax, st_customer, Q;
    static int client;
    Digraph map;
    static short stationType[];
    float slopeFunctionLoad[];
    String filename;
    ArrayList<Pair<Float, Float>> coordinates;
    double solutionTime;
    public ElectricVehiclesRoutes(String filename) {    
        this.filename = filename;   
        BufferedReader reader;
        String line;
        String splittedLine[];                                        
        try {
            reader = new BufferedReader(new FileReader(filename));
            double[] values = new double[10];
            for (int i = 0; i < 10; i++) {
                line = reader.readLine();
                splittedLine = line.split(" ");
                values[i] = Float.parseFloat(splittedLine[2]);
            }
            n = (int) values[0];
            m = (int) values[1];
            u = (int) values[2];
            breaks = (int) values[3];
            r = values[4];
            speed = values[5];
            Tmax = values[6];    
            Smax = values[7];
            st_customer = values[8];
            Q = values[9];
            reader.readLine();       
            reader.readLine();
            reader.readLine();        
            coordinates = new ArrayList<Pair<Float, Float>>();
            map = new DigraphAM(n);
            for (int i = 0; i <= m; i++) {
                line = reader.readLine();
                splittedLine = line.split(" ");
                coordinates.add(new Pair(Float.parseFloat(splittedLine[2]), Float.parseFloat(splittedLine[3])));
            }
            stationType = new short[u];
            for (int i = 0; i < u; i++) {    
                line = reader.readLine();
                splittedLine = line.split(" ");
                coordinates.add(new Pair(Float.parseFloat(splittedLine[2]), Float.parseFloat(splittedLine[3])));
                stationType[i] = Short.parseShort(splittedLine[5]);
            }   
            //Cambiamos m por n
            client = m;
            for (int i = 0; i < n; i++) {   
                for (int j = 0; j < n; j++) {
                    double distance = (Math.sqrt(Math.pow(coordinates.get(i).first - coordinates.get(j).first,2)+ 
                                Math.pow(coordinates.get(i).second - coordinates.get(j).second, 2))); //Cambiamos velocidad
                    map.addArc(i, j, distance);
                }              
            }                  
            slopeFunctionLoad = new float[3];
            reader.readLine();     
            reader.readLine();
            reader.readLine();
            for (int i = 0; i < 3; ++i) {
                line = reader.readLine();
                splittedLine = line.split(" ");
                slopeFunctionLoad[i] = Float.parseFloat(splittedLine[3]);
            }
            reader.readLine();
            reader.readLine();       
            reader.readLine();
            for (int i = 0; i < 3; ++i) {
                line = reader.readLine();
                splittedLine = line.split(" ");
                slopeFunctionLoad[i] = Float.parseFloat(splittedLine[3]) / slopeFunctionLoad[i];
            }
            solutionTime = Double.MAX_VALUE;
        } catch (Exception ex) {
            System.out.println(ex);
        }   
    }

    @Override  
    public String toString() {
        return "ElectricVehiclesRoutes{" + "r=" + r + ", speed=" + speed + ", Tmax=" + Tmax + ", Smax=" + Smax + ", st_customer=" + st_customer + ", Q=" + Q + ", solutionTime=" + solutionTime + '}';
    }

    public void exportarPuntosCSV() {
        try {
            PrintStream writecoordinates = new PrintStream(new File("ArchivosGenerados\\coordinates.csv"));
            writecoordinates.println("X,Y");
            for (Pair<Float, Float> coordenada : coordinates) {
                writecoordinates.println(coordenada.first + "," + coordenada.second);
            }
            writecoordinates.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public void solution(boolean test) {
        routingSolution routeResult = new routingSolution();
        routeResult.savingsCalculation(this.map, this.map.size());
        routeResult.routesConstruction(this.map);      
        if (test) {
            this.routes = routeResult.obtainRoute(); 
        } else {
            this.routes = routeResult.routes;
        }
        this.routes.trimToSize();
        this.numberRoutes = this.routes.size();
        System.out.println();  
    }

    /**
     * Este metodo es un test para verificar que la solucion es correcta. 
     * @param rutas Es un contenedor de rutas representadas por un arraylist de parejas donde el primer elemento indica el nodo
     * y el segundo elemento el time que se quedo en ese nodo
     * @return Verdadero si el time de solucion expresado concuerda y si la bateria nunca esta por debajo de 0.
     */
    public static String[][] proveSolution(){
        File f = new File("../DataSets");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));
        String[][] tableResult = new String[names.size()][7];
        int cont = 0;
        for(String file: names) {
            System.gc();
            Runtime runtime = Runtime.getRuntime();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            long bestTime, worstTime, average;
            bestTime = Long.MAX_VALUE;
            worstTime = 0;
            average = 0;
            for (int i = 0; i < 100; i++) {
                long ti = System.currentTimeMillis();
                ElectricVehiclesRoutes solutionR = new ElectricVehiclesRoutes("../DataSets/"+file);
                tableResult[cont][2] = ""+solutionR.m;
                solutionR.solution(true);
                long tf = System.currentTimeMillis();
                long total = tf - ti;
                bestTime = total < bestTime ? total : bestTime;
                worstTime = total > worstTime ? total : worstTime;
            }
            ElectricVehiclesRoutes solutionR = new ElectricVehiclesRoutes("../DataSets/"+file);
            solutionR.solution(false);
            tableResult[cont][3] = "" + solutionR.numberRoutes;
            tableResult[cont][4] = "" + routeTime(solutionR.routes, solutionR.map);
            tableResult[cont][5] = "" + bestTime;
            average = (worstTime + bestTime) /2; 
            tableResult[cont][6] = "" + average;            
            System.gc();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = ((memoryAfter - memoryBefore)/1024); //Memoria en KB
            tableResult[cont][0] = "" + memoryUsed;
            tableResult[cont][1] = "" + worstTime;
            cont++;
        }
        return tableResult;
    } 

    private static void printResults(String[][] tableResult) {            
        for(int i = 0; i < tableResult.length; i++ ) {
            System.out.printf("%s KB, %s ms (WorstTime), %s clients, %s vehicles, %s totalTime, %s ms (BestTime), %s ms (average) \n",
                tableResult[i][0], tableResult[i][1], tableResult[i][2], tableResult[i][3], tableResult[i][4], tableResult[i][5], tableResult[i][6]);
        } 
    } 

    public static double routeTime(ArrayList<ArrayList<Integer>> rutas, Digraph map){
        double tF = 0;
        ArrayList<Integer> r;
        for(int i = 0; i < rutas.size(); i++){
            r = rutas.get(i);
            tF += map.getWeight(0, r.get(0));
            for(int j = 0; j < r.size() - 1; j++){
                tF = map.getWeight(r.get(j), r.get(j+1)) + tF;                
            }
            tF += map.getWeight(0, r.get(r.size()-1));
        }
        tF = Math.round(tF * 100.0) / 100.0;
        return tF;
    }

    public static void main(String[] args) {

        String[][] tableResult = proveSolution();
        printResults(tableResult);

    }
}
