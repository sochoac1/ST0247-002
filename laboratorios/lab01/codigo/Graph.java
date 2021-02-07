import java.util.ArrayList;
/**
 * Abstract class for implementations of Digraphs
 * 
 * @author Mauricio Toro
 * @version 1
 */
public abstract class Graph
{
   protected int size;
   public  Graph(int vertices) 
   {
       size = vertices;
   }
   public  abstract void addArc(int source, int destination, double weight, String nombre);
   public abstract ArrayList<Integer> getSuccessors(int vertice);
   public abstract Pareja getWeight(int source, int destination);
   public  int size() {return size;}
}
