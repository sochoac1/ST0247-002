/**
 * Write a description of class C here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;
public class routeConstruction{     
    //Attributes     
    
    double[][] savings;
    int lastAdded;
    int counter;
    int[] visited;
    public static ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
    
    public void savingsCalculation(Digraph map, int size){
        counter = size-1;
        visited = new int[size];
        visited[0] = 1;      
        savings = new double[size][size];
        for(int i = 1; i<size; i++){
            double first = map.getWeight(0,i)*2;
            savings[0][i]= first;
        }
        for(int i = 1; i<size;i++){
            for(int j = 1; j<size; j++){
                if(i==j){
                    savings[i][j]=0;
                    continue;
                }
                double sum = savings[0][i]+savings[0][j];
                double mergedRoutes = map.getWeight(0,i) +map.getWeight(0,j)+map.getWeight(i, j);
                savings[i][j] = sum - mergedRoutes;
                savings[j][i] = sum - mergedRoutes;
            }
        }
        for(int i = 1; i<size; i++){
            savings[0][i]= 0;
            savings[i][0]= 0;
        }
    }

    public void calculadorRuta(Digraph map){
        if(counter <= 0)return;  
        ArrayList<Integer> actualRoute = new ArrayList<Integer>();
        double mayor = 0;
        int firstIndex = 0;
        int lastIndex = 0;
        for(int i = 1; i < map.size; i++){
            for(int j = i; j < map.size; j++){
                if(mayor < savings[i][j]){
                    mayor = savings[i][j];
                    firstIndex = i;
                    lastIndex = j;
                }
            }
        }
        if(mayor == 0) return;
        while(true){  
            actualRoute = addClient(actualRoute, firstIndex, lastIndex);
            if(!itCanBeAddIt(actualRoute, map)){
                if(lastAdded == 1)actualRoute.remove(0);
                if(lastAdded == 2)actualRoute.remove(actualRoute.size()-1);
                routes.add(actualRoute);
                //actualRoute.clear();
                alreadyVisited(actualRoute, map.size);
                break;
            }
            alreadyUsedSavings(actualRoute, map.size);
            savings[firstIndex][lastIndex]=0;
            savings[lastIndex][firstIndex]=0;
            mayor= 0;
            if(counter - actualRoute.size()==0){
                routes.add(actualRoute);
                break;
            }
            firstIndex = actualRoute.get(0);
            lastIndex = actualRoute.get(actualRoute.size()-1);
            int posx, posy;     
            posx = posy = 0;
            for(int i = 1; i<map.size; i++){
                if(visited[i] == 1){
                    continue;
                }
                double temp = 0;
                int pos = 0;
                if(savings[firstIndex][i]>savings[lastIndex][i]){
                    temp = savings[firstIndex][i];
                    pos = firstIndex;
                }else{
                    temp = savings[lastIndex][i];
                    pos = lastIndex;
                }
                if (i == firstIndex || i == lastIndex) continue;
                if(mayor < temp){
                    mayor = temp;
                    posx = i;
                    posy = pos;
                }    
            }
            firstIndex = posx;
            lastIndex = posy;
        }
        counter = counter - (routes.get(routes.size()-1)).size();
        calculadorRuta(map);
    }

    public void alreadyUsedSavings (ArrayList<Integer> actualRoute, int tam) {
        if (actualRoute.size() <= 2) return;
        for (int i = 1; i <= actualRoute.size()-2; i++) {
            if(visited[actualRoute.get(i)] == 1)continue;
            visited[actualRoute.get(i)] = 1;
            for (int j = 0; j < tam; j++) {
                savings[j][actualRoute.get(i)] = 0;
                savings[actualRoute.get(i)][j] = 0;
            }
        }
    }

    public void alreadyVisited (ArrayList<Integer> actualRoute, int tam) {
        for (int j = 0; j < tam; j++) {
            savings[j][actualRoute.get(0)] = 0;
            savings[actualRoute.get(0)][j] = 0;
            savings[j][actualRoute.get(actualRoute.size()-1)] = 0;
            savings[actualRoute.get(actualRoute.size()-1)][j] = 0;
        }
        visited[actualRoute.get(0)] = 1;
        visited[actualRoute.get(actualRoute.size()-1)] = 1;
    }

    public boolean itCanBeAddIt(ArrayList<Integer> actualRoute, Digraph map){
        double maxTime = 10;
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

    public ArrayList<Integer> addClient(ArrayList<Integer> actualRoute, int x, int y){
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
    
}