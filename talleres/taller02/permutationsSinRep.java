import java.util.*;
/**
 * Write a description of class permutations here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class permutationsSinRep
{
    /**
     * Metodo auxiliar que llama al metodo permutations posterios
     * 
     * @param  s la cadena a la cual se le haran las permutaciones
     * @return un ArrayList que contiene las permutaciones
     */  
    public static LinkedList<String> permutations(String s) {
        LinkedList<String> respuesta = new LinkedList<String>();
        permutations("", s, respuesta);
        return respuesta;
    }

    /**
     * Metodo para obtener las posibles permutaciones que se pueden hacer
     * con los caracteres de una cadena dada, recuerde que las letras no se 
     * repiten en este ejercicio
     * 
     * @param  loQueLlevo parte de la cadena que hace parte de la permutacion
     * @param loQueMeFalta parte de cadena que falta por calcular en una permutacion
     * @param list el conjunto que tiene todas las permutaciones
     * 
     */  
    private static void permutations(String loQueLlevo, String loQueMeFalta, LinkedList<String> list) {
        if(loQueMeFalta.length() == loQueLlevo.length()){
            list.add(loQueLlevo);
        }else{
            for(int i = 0; i < loQueMeFalta.length(); i++){
                permutations(loQueLlevo + loQueMeFalta.charAt(i), loQueMeFalta,list);
                
            }
        }
    }
    
     public static void main(String args[]){
        String z = "abc";
        LinkedList<String> list = new LinkedList<String>();
        list = permutations(z);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
    }
}
