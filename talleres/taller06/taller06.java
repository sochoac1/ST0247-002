import java.util.ArrayList;
/**
 * Write a description of class taller06 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class taller06
{
    /**
     * Metodo que recorre todo el grafo con la intencion de buscar un
     * camino que represente el menor costo pasando por todos los vertices exactamente
     * una vez y vuelva al nodo inicial
     * @param g grafo dado 
     * @return cual es el costo que tiene
     */
    public static int recorrido(Digraph g) {
        boolean[] visitados = new boolean[g.size()];
        int costo = 0;
        return recorrido(0, g, visitados, costo);
    }

    public static int recorrido(int v, Digraph g, boolean[] visitados, int costo){
        if(ok(visitados)){
            return costo + g.getWeight(v,0);
        }
        ArrayList<Integer> sucesores = g.getSuccessors(v);
        int nodo = nodoPesoMinimo(sucesores, g, v, visitados);
        costo = costo + g.getWeight(v, nodo);
        visitados[v] = true;
        
        return recorrido(nodo, g, visitados, costo);
    }
    
    private static boolean ok(boolean[] szs){ 
        for(int i = 0; i<szs.length; i++){
            if(!szs[i])return false;
        }
        return true;
    }

    public static int nodoPesoMinimo(ArrayList<Integer> sucesores, Digraph g, int v, boolean[] visitados){
        int pesoMinimo = Integer.MAX_VALUE;
        int nodo = 0;
        for(int i = 0; i < sucesores.size(); i++){
            if(visitados[sucesores.get(i)] == false){
                if(pesoMinimo > g.getWeight(v, sucesores.get(i))){
                    pesoMinimo = g.getWeight(v, sucesores.get(i));
                    nodo = sucesores.get(i);
                }
            }
        }
        return nodo;
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
        System.out.println(recorrido(g));
    }
}
