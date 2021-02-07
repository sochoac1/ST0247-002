import java.util.ArrayList;
import java.util.Arrays;

public class DigraphAM2 extends Graph {
    //Grafo con matriz
    private int[][] mat;

    public DigraphAM2(int size){
        super(size);
        mat = new int[size+1][size+1];

        for (int i = 1; i <= size; ++i) {
            mat[i][0] = 0;
            mat[0][i] = 0;
        }
    }

    void imprimir() {
        for (int[] ints : mat) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public void addArc(int source, int destination, int weight){
        if (!(source == 0 && destination == 0)) {
            mat[source][destination] = weight;
        }
    }

    public int getWeight(int source, int destination){
        return mat[source][destination];
    }

    public ArrayList<Integer> getSuccessors(int vertex)
    {
        ArrayList<Integer> np= new ArrayList<>();
        for(int i=0; i<=size; i++){
            if(mat[vertex][i]!=0){
                np.add(i);
            }
        }
        return np;   
    }

    public int getFirst() {
        return mat[0][0];
    }
}
