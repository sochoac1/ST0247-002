
/**
 * Write a description of class Prim here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 * En prim buscamos el minimo costo de aristas para recorrer el grafo de forma completa.
 * Selecionamos el que tenga menor peso de incidencia y no forme ciclos.
 */
public class Prim
{
    public static int prim(Digraph g, int inicio ){
        boolean[] visitados = new boolean[g.size()]; //Vertices visitados
        int[] visit = new int[g.size()];    //Se ponen los visitados en el orden de visita
        int sum = 0; //
        visit[0] = inicio;
        visitados[0] = true;
        return primAux(g, visitados, 1, sum, visit);        //1 = num = cantidad de visitados
    }

    public static int primAux(Digraph g, boolean[] v, int num, int sum, int[] vs){
        if(num == g.size()) return sum;
        int pos = -1;
        int menor = Integer.MAX_VALUE;
        for(int i = 0; i<num; i++){     // Elegir el menor arco
            ArrayList<Integer> vecinos = g.getSuccessors(vs[i]);
            if(vecinos == null)continue;
            for(int j = 0; j<vecinos.size(); j++){
                if(!v[vecinos.get(j)] && menor>g.getWeight(vs[i], vecinos.get(j))){
                    menor = g.getWeight(vs[i], vecinos.get(j));
                    pos = vecinos.get(j);       //no. vecino
                }
            }
        }
        if(pos==-1)return sum;
        sum = sum + menor;
        v[pos] = true;
        vs[num] = pos;
        return primAux(g, v, num+1, sum, vs);      // Recursión  
    }
}
