/** 
 * @author Santiago Ochoa Castaño y Miguel Angel Zapata
 * @version 1
*/
import java.util.LinkedList;
public class SubGrupos {
    public static LinkedList<String> Combination(String s){
        LinkedList<String> respuesta = new LinkedList<>();
        CombinationAux(respuesta, "", s);
        return respuesta;
    }

    private static void CombinationAux(LinkedList<String> res, String pos, String pre){
        if(pre.length() == 0) {
            res.add(pos);
        }else{
            CombinationAux(res, pos + pre.substring(0,1), pre.substring(1));
            CombinationAux(res, pos, pre.substring(1));
        }
        
    }

    public static void main(String[] args){
        String str = "abc";
        LinkedList<String> res = SubGrupos.Combination(str);
        for(int i = 0; i < res.size(); i++){
            System.out.println(res.get(i));
        }
    }
}

