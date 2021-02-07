public class Algorithm {

    /*public static boolean DFSColorFC(DigraphAM2 am) {
    return DFSColorFCAux(am.getFirst(), 1);
    }

    private static boolean DFSColorFCAux(int first, int color) {
    return false;

    }
     */

    public static boolean DFSColorFC(DigraphAM2 g){
        String[] visitados = new String[g.size];
        int origen = g.getFirst();
        return DFSColorFCAux(g, origen, visitados, "verde");
    }

    private static boolean DFSColorFCAux(DigraphAM2 g, int origen, String[] v, String color){
        if(v[origen] == null){ // C1
            for(Integer s : g.getSuccessors(origen)){ 
                if(color.equals("verde")){
                    v[origen] = "amarillo"; 
                    if(!DFSColorFCAux(g, s, v, "amarillo")){
                        return false;
                    }
                }else{   
                    v[origen] = "verde";
                    if(!DFSColorFCAux(g, s, v, "verde")){
                        return false;
                    }                    
                }
            }
        }else{
            if(v[origen].equals(color)){
                return false;
            }else{ 
                if(color.equals("verde")){
                    return true;
                }else{
                    return true;
                }
            }            
        }      
        return true;
    }
}
