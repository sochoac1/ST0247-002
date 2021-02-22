import java.util.ArrayList;

/**
 * Clase en la cual se implementan los metodos del Taller 5
 * 
 * @author Mauricio Toro, Camilo Paez
 */
public class punto2 {

    /**
     * Metodo que dado un grafo y un numero m, se asigna un color
     * a cada nodo, de manera que dos nodos adyacentes no poseean el mismo color
     * @param g grafo dado 
     * @param m numero de colores
     * @return true si es posible, false de lo contrario
     */
    public static int mColoring(Digraph g, int m) {
        int[] colors = new int[g.size()];
        mColoring(g, 0, colors, m);
        int mayor = 0;
        for(int i = 0; i < colors.length; i++){
            if(colors[i] > mayor){
                mayor = colors[i];
            }
        }
        return mayor;
    }

    /**
     * Metodo que dado un grafo y un vertice v, intenta asignar un color
     * al nodo, de manera que dos nodos adyacentes no poseean el mismo color
     * @param g grafo dado 
     * @param m numero de colores
     * @param v vertice 
     * @param colors conjunto de colores
     * @return true si es posible, false de lo contrario
     */
    private static boolean mColoring(Digraph g, int v, int[] colors, int m) {
        if(v == g.size()) return true;
        for(int i = 1; i < m+1; i++){
            if(isSafe(g, v, colors, i)){
                colors[v] = i;
                if(mColoring(g,v+1, colors, m)){
                    return true;
                }
            }  
        }
        return false;        
    }

    /**
     * Metodo que dado un grafo y un vertice v, intenta asignar un color colors en la 
     * posicion c al nodo v, de manera que dos nodos adyacentes no poseean el mismo color
     * @param g grafo dado 
     * @param c indice de colores
     * @param v vertice 
     * @param colors conjunto de colores
     * @return true si es posible, false de lo contrario
     */
    private static boolean isSafe(Digraph g, int v, int[] colors, int c) {
        ArrayList<Integer> sucesores = g.getSuccessors(v);
        for(int i = 0; i < sucesores.size(); i++){
            if(colors[sucesores.get(i)] == c){
                return false;
            }
        }
        return true;
    }

}
