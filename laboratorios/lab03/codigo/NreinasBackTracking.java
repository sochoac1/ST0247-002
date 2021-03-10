import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Santiago Ochoa Castaño & Miguel Angel Zapata Jimenez
 */
public class NreinasBackTracking {
    /**
     * Metodo que verifica si es posible poner las reinas hasta la columna c
     * 
     * @param  c hasta esta columna revisa
     * @param  tablero el tablero
     * @return true si es posible, false de lo contrario
     */  
    private static boolean puedoPonerReina(int c, int[] tablero) {
        for(int i = 0; i< c; i++){
            if(tablero[i]==tablero[c]){
                return false;
            }else if(Math.abs(tablero[i]-tablero[c])==Math.abs(i-c)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Metodo auxiliar para llamar el metodo posterior
     * 
     * @param  n numero de reinas
     * @return numero de soluciones
     */  
    public static int nReinas(int n) {
        return nReinas(0, n, new int[n]);
    }
    
    /**
     * Metodo devuelve el numero de soluciones que tiene el problema
     * 
     * @param  c columna
     * @param  n numero de reinas
     * @return numero de soluciones
     */  
    private static int nReinas(int c, int n, int[] tablero) {
        int result = 0;
        if(c == n)return 1;
        for(int i = 0;i<n; i++){
            tablero[c]=i;
            if(puedoPonerReina(c,tablero)){
                result = result +nReinas(c+1, n,tablero);
            }
        }
        return result;
    }
    
    public static void imprimirTablero(int[] tablero) {
        int n = tablero.length;
        System.out.print("    ");
        for (int i = 0; i < n; ++i)
            System.out.print(i + " ");
        System.out.println("\n");
        for (int i = 0; i < n; ++i) {
            System.out.print(i + "   ");
            for (int j = 0; j < n; ++j)
                System.out.print((tablero[i] == j ? "Q" : "#") + " ");
            System.out.println();
        }
        System.out.println();
    } 
    
    public static void main(String[] args){
        System.out.println(nReinas(9));
    }
}
