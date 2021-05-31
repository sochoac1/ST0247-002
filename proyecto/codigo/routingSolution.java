import java.util.ArrayList;

/**
 * Write a description of class routingSolution here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class routingSolution
{
    ArrayList<Pair<int[],Double>> savings = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> routes = new ArrayList<>();
    int lastAdded;
    boolean[] visited;
    int counter;
    boolean[] usedSavings;
    public void savingsCalculation(Digraph map, int size){
        
        for(int i = 1; i < size; i++){
            for(int j = i + 1; j < size; j++){
                double sum = map.getWeight(0, i)*2 + map.getWeight(0, j)*2;
                double mergedRoutes = map.getWeight(0,i) +map.getWeight(0,j)+map.getWeight(i, j);
                double total = sum - mergedRoutes;
                int[] nodes = {i,j};
                Pair<int[], Double> saves = new Pair(nodes, total);
                savings.add(saves);
            }
        }
        visited = new boolean[size];
        usedSavings = new boolean[savings.size()];
        RandomizedQsort.sort(savings, 0, savings.size() -1);
        counter = size;
        
    }   

    public void routesConstruction(Digraph map){
        while(counter > 0){
            ArrayList<Integer> actualRoute = new ArrayList<>();
            int node1 = 0;
            int node2 = 0;
            int pos = 0;
            double totalTime = 0;
            //Busca el mejor ahorro y que no exceda el tiempo
            for(int i = 0; i < savings.size(); i++){
                totalTime = map.getWeight(0,savings.get(i).first[0]) + map.getWeight(0,savings.get(i).first[1])+map.getWeight(savings.get(i).first[0], savings.get(i).first[1])+
                ElectricVehiclesRoutes.st_customer*2;
                if(totalTime > 10.0 || (usedSavings[i])){   
                    continue;
                }else{ 
                    node1 = savings.get(i).first[0];
                    node2 = savings.get(i).first[1];
                    pos = i;
                    break;
                }
            }   
            actualRoute = addNode(actualRoute, node1, node2); 
            //savings.remove(pos);
            usedSavings[pos] = true;
            for(int j = 0; j < savings.size(); j++){ 
                int ini = savings.get(j).first[0];
                int end = savings.get(j).first[1];    
                double actualTime = 0;
                
                if((usedSavings[j])){
                    continue;
                }
                if((visited[ini]) || (visited[end])){
                    //savings.remove(j);
                    usedSavings[j] = true;
                    //j--;
                    continue;
                }              
                if((ini == node1) && (end == node2) ||(ini == node2) && (end == node1)){
                    //savings.remove(j);
                    usedSavings[j] = true;
                    //j--;
                    continue;
                }
                if((ini == node1) || (ini == node2)||(end == node1) || (end == node2)){

                    actualRoute = addNode(actualRoute, ini, end);                       
                    if(itCanBeAddIt(actualRoute, map) && !(visited[ini])  && !(visited[end])){                          
                        if(lastAdded ==1){
                            visited[node1] = true;
                            node1 = actualRoute.get(0);   
                        }else{
                            visited[node2] = true;  
                            node2 = actualRoute.get(actualRoute.size()-1);
                        }
                        //savings.remove(j);
                        //j--;
                        usedSavings[j] = true;
                    }else{
                        if(lastAdded == 1)actualRoute.remove(0);
                        if(lastAdded == 2)actualRoute.remove(actualRoute.size()-1);
                        /*savings.remove(j);
                        j--;
                        */
                       usedSavings[j] = true;
                    }
                }
                
            }
            visited[node1] = true;
            visited[node2] = true;
            counter = counter - actualRoute.size();
            routes.add(actualRoute);
        }
    }   

    public boolean itCanBeAddIt(ArrayList<Integer> actualRoute, Digraph map){
        double maxTime = 10.0;
        double visitTime = ElectricVehiclesRoutes.st_customer*actualRoute.size();
        for(int i = 0; i<actualRoute.size(); i++){
            if(i == actualRoute.size()-1){
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i)));
                break;
            }
            if(i == 0){    
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i)));
            }
            maxTime = maxTime-(map.getWeight(actualRoute.get(i), actualRoute.get(i+1)));
        }
        return (maxTime-visitTime)>=0; 
    }   

    public ArrayList<Integer> addNode(ArrayList<Integer> actualRoute, int x, int y){
        
        ArrayList<Integer> actualRoute1 = actualRoute;  
        if(actualRoute1.isEmpty()){
            actualRoute1.add(x); 
            actualRoute1.add(y);
   
            return actualRoute1;
        }
        int tam = actualRoute1.size();
        if(x==actualRoute1.get(0)){
            actualRoute1.add(0, y);
            lastAdded = 1;
        }else if(y==actualRoute1.get(0)){
            actualRoute1.add(0, x);
            lastAdded = 1;
        }else if(actualRoute1.get(tam-1)==x){
            actualRoute1.add(y);
            lastAdded = 2;
        }else  if(actualRoute1.get(tam-1)==y){
            actualRoute1.add(x);   
            lastAdded = 2;
        }
        return actualRoute1;
    }

    public static ArrayList<ArrayList<Integer>> obtainRoute() {
        ArrayList<ArrayList<Integer>> solution = routes;
        routes.clear();
        return solution;
    }   

    public static void main(String[] args){
        Digraph map = new DigraphAM(6);
        map.addArc(0,1,35.0);
        map.addArc(0,2,36.0);
        map.addArc(0,3,47.0);
        map.addArc(0,4,45.0);
        map.addArc(0,5,27.0);

        map.addArc(1,2,28.0);
        map.addArc(1,3,24.0);
        map.addArc(1,4,44.0);
        map.addArc(1,5,31.0);

        map.addArc(2,3,26.0);
        map.addArc(2,4,13.0);
        map.addArc(2,5,23.0);
        map.addArc(3,4,26.0);
        map.addArc(3,5,23.0);

        map.addArc(4,5,22.0);

        routingSolution r = new routingSolution();
        r.savingsCalculation(map, map.size);  
        r.routesConstruction(map);
    }
}
