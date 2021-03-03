import java.util.ArrayList;
/**
 * Clase en la cual se implementan los metodos del Taller 4
 * 
 * @author Miguel Angel Zapata, Santiago Ochoa Castaño
 */
public class Ejercicio1 {
    /**
     * Metodo auxiliar que llama al metodo recorrido posterior
     * con cada uno de los vertices
     * @param g grafo dado 
     * @return cual es el costo que tiene
     */
    public static int caminoHamiltoniano(Digraph g) {
        boolean[] univisited = new boolean[g.size()];
        return caminoHamiltoniano(g, 0, univisited,0);
    }
    private static int caminoHamiltoniano(Digraph g, int v, boolean[] unvisited, int max) {
        int minimo = Integer.MAX_VALUE;
        if(ok(unvisited)&& v == 0)return max;
        if(ok(unvisited)&& v != 0)return Integer.MAX_VALUE;
        unvisited[v]=true;
        ArrayList<Integer> hola = g.getSuccessors(v);
        if (ok(unvisited)){
            for(int i = 0; i < hola.size(); i++){
                int suma = max +  g.getWeight(v, hola.get(i)); 
                minimo = Math.min(minimo, caminoHamiltoniano(g, hola.get(i), unvisited, suma));
    
            }
            return minimo;
        }
        
        for(int i = 0; i < hola.size(); i++){
            if(unvisited[hola.get(i)] == true){
                continue;
            }else{
                int suma = max +  g.getWeight(v, hola.get(i)); 
                minimo = Math.min(minimo, caminoHamiltoniano(g, hola.get(i), unvisited, suma));
                unvisited[hola.get(i)] = false;
            }
        }
        return minimo;
    }
    private static boolean ok(boolean[] szs){ 
        for(int i = 0; i<szs.length; i++){
            if(!szs[i])return false;
        }
        return true;
    }
    public static void main(String args[]){
        Digraph g = new DigraphAM(4);
        g.addArc(0, 1, 7);
        g.addArc(0, 3, 6);
        g.addArc(0, 2, 15);
        g.addArc(1, 0, 2);
        g.addArc(1, 2, 7);
        g.addArc(1, 3, 3);
        g.addArc(3, 0, 10);
        g.addArc(3, 2, 8);
        g.addArc(3, 1, 4);
        g.addArc(2, 0, 9);
        g.addArc(2, 1, 6);
        g.addArc(2, 3, 12);
        System.out.println(caminoHamiltoniano(g));
    }
}