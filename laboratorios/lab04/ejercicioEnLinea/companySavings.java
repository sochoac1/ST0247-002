import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 * Write a description of class punto2 here.
 * 
 * @author (Miguel Angel Zapata Jimenez && Santiago Ochoa Castaño) 
 * @version (1)
 */
public class companySavings
{
    static int n,d,r;
    static int[] dayHours;
    static int[] nightHours;
    String filename;
    public companySavings(String filename){
        this.filename = filename;
        BufferedReader reader;
        String line;
        String splittedLine[];
        try{
            reader = new BufferedReader(new FileReader(filename));
            line = reader.readLine();
            splittedLine = line.split(" ");
            boolean exit = false;
            while(!(exit)){
                n = Integer.parseInt(splittedLine[0]);
                d = Integer.parseInt(splittedLine[1]);
                r = Integer.parseInt(splittedLine[2]);
                line = reader.readLine();
                splittedLine = line.split(" ");
                dayHours = new int[n];
                for(int i = 0; i < n; i++){
                    dayHours[i] = Integer.parseInt(splittedLine[i]);
                }
                line = reader.readLine();
                splittedLine = line.split(" ");
                nightHours = new int[n];
                for(int i = 0; i < n; i++){
                    nightHours[i] = Integer.parseInt(splittedLine[i]);
                }
                line = reader.readLine();
                splittedLine = line.split(" ");
                if(splittedLine[0].equals("0") && splittedLine[1].equals("0") &&
                splittedLine[2].equals("0")){
                    exit = true;
                }
                savings();
            }
        }catch(Exception ex){
            System.out.println(ex);
        }  
    }
    public static void savings(){ 
        boolean[] visitedNight = new boolean[n];
        int savingsSum = 0;
        int v = 0;
        int actualSum = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(!visitedNight[j]){
                    int sum = dayHours[i] + nightHours[j];
                    if(actualSum > sum){
                        actualSum = sum;  
                        v = j;
                    }
                }
            }
            visitedNight[v] = true;
            if(actualSum > d){
                savingsSum += (actualSum-d);
            }
            actualSum =Integer.MAX_VALUE;
        }
        //Impresion
        System.out.println(savingsSum*r);
    }
}