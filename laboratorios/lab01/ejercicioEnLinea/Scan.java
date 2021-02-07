import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scan {

    public Scan() {
    }    

    public void readBicolor() throws FileNotFoundException {
        Scanner read = new Scanner(new File("Entrada.txt"));
        String line = read.nextLine();
        Scanner scanLine = new Scanner(line);
        DigraphAM2 am = null;   
        int nodos = scanLine.nextInt();
        while (nodos != 0) {
            am = new DigraphAM2(nodos);
            line = read.nextLine();
            scanLine = new Scanner(line);
            int aris = scanLine.nextInt();
            for (int i = 0; i < aris; i++) {
                line = read.nextLine();
                scanLine = new Scanner(line);
                int v1 = scanLine.nextInt();
                int v2 = scanLine.nextInt();
                am.addArc(v1, v2, 1);
                am.addArc(v2, v1, 1);
            }
            line = read.nextLine();
            scanLine = new Scanner(line);
            nodos = scanLine.nextInt();
            if (Algorithm.DFSColorFC(am)){
                System.out.println("BICOLORABLE");
            }else{
                System.out.println("NO BICOLORABLE");
            }
        }
    }
}
