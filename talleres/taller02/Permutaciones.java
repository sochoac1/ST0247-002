/** 
 * @author Santiago Ochoa Castaño y Miguel Angel Zapata
 * @version 1
*/

import java.util.LinkedList;
public class Permutaciones {
    public static LinkedList<String> permutation(String a){
        LinkedList<String> res = new LinkedList<>();
        permutationAux(res, "", a);
        return res;
    } 

    private static void permutationAux(LinkedList<String> res, String pos, String pre){
        if(pre.length() == 0){
            res.add(pos);
        }
        else{
            for(int i = 0; i < pre.length(); i++){
                permutationAux(res, pos+pre.substring(i, i+1), pre.substring(0,i) + pre.substring(i+1));
            }
        }  
    }

    public static void main(String[] args){
        String s = "abc";
        LinkedList<String> res = Permutaciones.permutation(s);
        for(int i = 0; i < res.size(); i++){
            System.out.println(res.get(i));
        }
    }
}
