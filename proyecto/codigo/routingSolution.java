import java.util.ArrayList;

/**
 * Write a description of class routingSolution here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class routingSolution
{
    ArrayList<Pair<int[],Double>> savings = new ArrayList<>(); //Ahorros de los clientes
    ArrayList<Pair<int[],Double>> stationSavings = new ArrayList<>(); //Ahorros de las estaciones de carga
    static ArrayList<ArrayList<Integer>> routes = new ArrayList<>();
    int lastAdded;
    boolean[] visited;
    int counter;
    boolean[] usedSavings;
    boolean[] usedStations;
    double speed = ElectricVehiclesRoutes.speed;
    int client = ElectricVehiclesRoutes.client;
    double r = ElectricVehiclesRoutes.r;
    double timeNotCharge;
    public void savingsCalculation(Digraph map, int size){

        for(int i = 1; i < size; i++){
            for(int j = i + 1; j < size; j++){
                if(i <= client && j <= client){
                    double sum = (map.getWeight(0, i)/speed)*2 + (map.getWeight(0, j)/speed)*2;
                    double mergedRoutes = (map.getWeight(0,i)/speed) + (map.getWeight(0,j)/speed) + (map.getWeight(i, j)/speed);
                    double total = sum - mergedRoutes;
                    int[] nodes = {i,j};
                    Pair<int[], Double> saves = new Pair(nodes, total);
                    savings.add(saves);
                }else{
                    double sum = (map.getWeight(0, i)/speed)*2 + (map.getWeight(0, j)/speed)*2;
                    double mergedRoutes = (map.getWeight(0,i)/speed) + (map.getWeight(0,j)/speed) + (map.getWeight(i, j)/speed);
                    double total = sum - mergedRoutes;
                    int[] nodes = {i,j};
                    Pair<int[], Double> saves = new Pair(nodes, total);
                    stationSavings.add(saves);
                }
            }
        }
        visited = new boolean[client]; //Clientes visitados
        usedSavings = new boolean[savings.size()]; //Ahorros de clientes utilizados
        RandomizedQsort.sort(savings, 0, savings.size() -1); //Organiza de mayor a menor
        counter = client; //Variable de control para saber cuantos clientes he agregado cada vez que hago ruta
        //Calculo de los ahorros de las estaciones de carga

        RandomizedQsort.sort(stationSavings, 0, stationSavings.size() -1);
        usedStations = new boolean[stationSavings.size()];
    }   

    public void routesConstruction(Digraph map){
        while(counter > 0){
            ArrayList<Integer> actualRoute = new ArrayList<>();
            int node1 = 0;
            int node2 = 0;
            int pos = 0;
            double totalTime = 0;
            timeNotCharge = 0;
            double totalBattery = 16000;

            //Busca el mejor ahorro y que no exceda el tiempo
            for(int i = 0; i < savings.size(); i++){
                totalTime = (map.getWeight(0,savings.get(i).first[0])/speed) + (map.getWeight(0,savings.get(i).first[1])/speed)+
                (map.getWeight(savings.get(i).first[0], savings.get(i).first[1])/speed) + ElectricVehiclesRoutes.st_customer*2;

                if(totalTime > 10.0 || (usedSavings[i])){   
                    continue;
                }else{ 
                    node1 = savings.get(i).first[0];
                    node2 = savings.get(i).first[1];
                    pos = i;
                    timeNotCharge = totalTime - ElectricVehiclesRoutes.st_customer*2;
                    break;
                }
            }

            actualRoute = addNode(actualRoute, node1, node2); 
            if(!(itCanBeAddItBattery(actualRoute, map, timeNotCharge, totalBattery))){
                //metodo buscar estación carga, 
                actualRoute.remove(actualRoute.size() - 1);
                node2 = 0;
                timeNotCharge = actualTime(actualRoute, map);
                totalBattery = chargeStation(node1, map, actualRoute, totalBattery, timeNotCharge);                
            }
            //savings.remove(pos);    
            usedSavings[pos] = true;     
            if(totalBattery != 0){
                for(int j = 0; j < savings.size(); j++){    
                    //Caso cuando es estación de carga
                    if(node1 > client || node2 > client){
                        if(node1 > client){
                            //Cargar
                            totalBattery = batteryCharge(node1, totalBattery, actualTime(actualRoute,  map));
                            if(totalBattery == 0) break;
                            //buscar nodo
                            if(newNode(node1, actualRoute, map,  totalBattery)){
                                node1 = actualRoute.get(0);
                            }else{
                                break;
                            }
                            j--;
                        }else if(node2 > client){
                            //Cargar
                            totalBattery = batteryCharge(node2, totalBattery, actualTime(actualRoute,  map));
                            if(totalBattery == 0) break;
                            //buscar nodo
                            if(newNode(node2, actualRoute, map,  totalBattery)){
                                node2 = actualRoute.get(actualRoute.size()-1);
                            }else{
                                break;
                            }
                            j--;
                        }    
                    }
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

                        //Agrega tiempo que lleva sin cargar
                        if(lastAdded == 1){
                            timeNotCharge = timeNotCharge - (map.getWeight(0, actualRoute.get(1))/speed);

                            timeNotCharge = (map.getWeight(actualRoute.get(0), actualRoute.get(1))/speed) + timeNotCharge + (map.getWeight(actualRoute.get(0), 0)/speed) + timeNotCharge;
                        }else{
                            timeNotCharge = timeNotCharge - (map.getWeight(0, actualRoute.get(actualRoute.size()-2))/speed);

                            timeNotCharge = (map.getWeight(actualRoute.size()-1, actualRoute.size()-2)/speed) + timeNotCharge + (map.getWeight(actualRoute.size()-1, 0)/speed) + timeNotCharge;
                        }

                        if(itCanBeAddItTime(actualRoute, map) && !(visited[ini])  && !(visited[end])){
                            if(itCanBeAddItBattery(actualRoute, map, timeNotCharge, totalBattery)){
                                if(lastAdded ==1){
                                    visited[node1] = true;
                                    node1 = actualRoute.get(0);
                                    totalBattery = consumption(timeNotCharge, totalBattery);
                                }else{
                                    visited[node2] = true;  
                                    node2 = actualRoute.get(actualRoute.size()-1);
                                    totalBattery = consumption(timeNotCharge, totalBattery);
                                }
                                //savings.remove(j);
                                //j--;
                                usedSavings[j] = true;
                            }else{
                                if(lastAdded == 1){
                                    //reseteo el tiempo
                                    timeNotCharge = timeNotCharge- ((map.getWeight(actualRoute.get(0), actualRoute.get(1))/speed)
                                        + (map.getWeight(actualRoute.get(0), 0)/speed));
                                    //Prosigo
                                    actualRoute.remove(0);
                                    int node = actualRoute.get(0);

                                    totalBattery = chargeStation(node,  map,  actualRoute,  totalBattery, timeNotCharge);
                                    if(totalBattery == 0){ 
                                        totalBattery = 1;
                                        break;
                                    }
                                    visited[node] = true;
                                    node1 = actualRoute.get(0);                                
                                }else{
                                    //reseteo el tiempo 
                                    timeNotCharge = timeNotCharge - ((map.getWeight(actualRoute.size()-1, actualRoute.size()-2)/speed) + (map.getWeight(actualRoute.size()-1, 0)/speed));

                                    actualRoute.remove(actualRoute.size()-1);
                                    int node = actualRoute.get(actualRoute.size()-1);

                                    totalBattery = chargeStation( node,  map,  actualRoute,  totalBattery, timeNotCharge);
                                    if(totalBattery == 0) {
                                        totalBattery = 1;
                                        break;
                                    }
                                    visited[node] = true;
                                    node2 = actualRoute.get(actualRoute.size()-1);

                                }
                            }
                        }else{
                            if(lastAdded == 1){
                                timeNotCharge = timeNotCharge- ((map.getWeight(actualRoute.get(0), actualRoute.get(1))/speed)
                                    + (map.getWeight(actualRoute.get(0), 0)/speed));
                                actualRoute.remove(0);
                            }
                            if(lastAdded == 2){
                                timeNotCharge = timeNotCharge - ((map.getWeight(actualRoute.size()-1, actualRoute.size()-2)/speed) + (map.getWeight(actualRoute.size()-1, 0)/speed));
                                actualRoute.remove(actualRoute.size()-1);
                            }
                            /*savings.remove(j);
                            j--;
                             */   
                            usedSavings[j] = true;
                        }
                    }
                } 
            }

            if(totalBattery == 0){
                visited[node1] = true;
                counter = counter - actualRoute.size();
                routes.add(actualRoute);
            }else{
                visited[node1] = true;
                visited[node2] = true;
                counter = counter - actualRoute.size();
                routes.add(actualRoute);
            }
        }
    }

    public boolean newNode(int node, ArrayList<Integer> actualRoute, Digraph map, double totalBattery){
        for(int i = 0; i < stationSavings.size(); i++){
            int ini = stationSavings.get(i).first[0];
            int end = stationSavings.get(i).first[1]; 

            if(ini == node && end == node)continue; 

            if(ini == node || (end == node)){   

                actualRoute = addNode(actualRoute, ini, end); 
                if(ini != node){
                    timeNotCharge = (map.getWeight(ini, node) / speed) + (map.getWeight(0, ini)/speed);
                }else if(end != node){
                    timeNotCharge = (map.getWeight(end, node) / speed) + (map.getWeight(0, end)/speed);
                }

                if(itCanBeAddItTime(actualRoute, map) && !(usedStations[i])){
                    if(itCanBeAddItBattery(actualRoute, map, timeNotCharge, totalBattery)){  
                        usedStations[i] = true;
                        return true;
                    }else{
                        if(lastAdded == 1){
                            //reseto tiempo 
                            timeNotCharge = timeNotCharge - ((map.getWeight(ini, node) / speed) + (map.getWeight(0, ini)/speed));
                            actualRoute.remove(0);
                        }

                        if(lastAdded == 2){
                            //reseteo tiempo 
                            timeNotCharge = timeNotCharge - ((map.getWeight(end, node) / speed) + (map.getWeight(0, end)/speed));
                            actualRoute.remove(actualRoute.size()-1);
                        }
                    }
                }else{
                    if(lastAdded == 1){
                        //reseto tiempo 
                        timeNotCharge = timeNotCharge - ((map.getWeight(ini, node) / speed) + (map.getWeight(0, ini)/speed));
                        actualRoute.remove(0);
                    }

                    if(lastAdded == 2){
                        //reseteo tiempo 
                        timeNotCharge = timeNotCharge - ((map.getWeight(end, node) / speed) + (map.getWeight(0, end)/speed));
                        actualRoute.remove(actualRoute.size()-1);
                    }

                }
            } 
        }
        return false;
    }

    public double batteryCharge(int node,double totalBattery, double currentTime){
        int position = node - client -1;
        int type = ElectricVehiclesRoutes.stationType[position];
        double newBattery = 0;
        if(type == 0){
            if(currentTime +0.28 < 10){
                return newBattery = 31373*(0.28);  
            }else{
                return 0;
            }
        }else if(type == 1){
            if(currentTime +0.55 < 10){
                return newBattery = 15842*(0.55);  
            }else{
                return 0;
            }
        }else{
            if(currentTime+1.1 < 10){
                return newBattery = 7843.1*(1.1);  
            }else{
                return 0;
            }
        }
    }     

    public boolean itCanBeAddItTime(ArrayList<Integer> actualRoute, Digraph map){
        double maxTime = 10.0;
        double visitTime = ElectricVehiclesRoutes.st_customer*actualRoute.size();
        for(int i = 0; i<actualRoute.size(); i++){
            if(i == actualRoute.size()-1){
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i))/speed);
                break;
            }
            if(i == 0){    
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i))/speed);
            }
            maxTime = maxTime-(map.getWeight(actualRoute.get(i), actualRoute.get(i+1))/speed);
        }

        return (maxTime-visitTime)>=0; 
    }   

    public double actualTime(ArrayList<Integer> actualRoute, Digraph map){
        double maxTime = 0;
        for(int i = 0; i<actualRoute.size(); i++){
            if(i == actualRoute.size()-1){
                maxTime = maxTime+(map.getWeight(0,actualRoute.get(i))/speed);
                break;
            }
            if(i == 0){    
                maxTime = maxTime+(map.getWeight(0,actualRoute.get(i))/speed);
            }
            maxTime = maxTime+(map.getWeight(actualRoute.get(i), actualRoute.get(i+1))/speed);
        }

        return maxTime; 
    }   

    public boolean itCanBeAddItBattery(ArrayList<Integer> actualRoute, Digraph map, double currentTime, double totalBattery){
        double consumption = 2000 * currentTime; 
        double expense = totalBattery - consumption;
        return (expense)>=5000; 
        /*
        double distance = speed * currentTime;   
        double consumption = distance * r;
        double expense = totalBattery - consumption;
        return (expense)>=5000; 
         */

    }   

    public double consumption(double currentTime, double totalBattery){
        /*
        double distance = speed * currentTime;   
        double consumption = distance * r;
        double expense = totalBattery - consumption;  
        return expense;
         */
        double consumption = 2000 * currentTime; 
        double expense = totalBattery - consumption;
        return (expense); 
    }

    public double chargeStation(int node, Digraph map, ArrayList<Integer> actualRoute, double totalBattery, double currentTime ){
        for(int i = 0; i < stationSavings.size(); i++){
            int ini = stationSavings.get(i).first[0];
            int end = stationSavings.get(i).first[1]; 
            if(ini == node && end == node)continue;
            if((ini == node) || (end == node)){   

                actualRoute = addNode(actualRoute, ini, end); 
                if(ini > client){
                    currentTime = (map.getWeight(0, ini) / speed) + (map.getWeight(node, ini)/speed) + currentTime;
                }else{
                    currentTime = (map.getWeight(0, end) / speed) + (map.getWeight(node, end)/speed) + currentTime;
                }

                if(itCanBeAddItTime(actualRoute, map) && !(usedStations[i])){
                    if(itCanBeAddItBattery(actualRoute, map, currentTime, totalBattery)){  
                        usedStations[i] = true;
                        double battery = consumption(currentTime, totalBattery);                        
                        return battery;
                    }else{
                        if(lastAdded == 1){
                            //reseto tiempo 
                            currentTime = currentTime - ((map.getWeight(0, ini) / speed) + (map.getWeight(node, ini)/speed));
                            actualRoute.remove(0);
                        }

                        if(lastAdded == 2){
                            //reseteo tiempo 
                            currentTime = currentTime - ((map.getWeight(0, end) / speed) + (map.getWeight(node, end)/speed));
                            actualRoute.remove(actualRoute.size()-1);
                        }
                    }
                }else{
                    if(lastAdded == 1){
                        //reseto tiempo 
                        currentTime = currentTime - ((map.getWeight(0, ini) / speed) + (map.getWeight(node, ini)/speed));
                        actualRoute.remove(0);
                    }

                    if(lastAdded == 2){
                        //reseteo tiempo 
                        currentTime = currentTime - ((map.getWeight(0, end) / speed) + (map.getWeight(node, end)/speed));
                        actualRoute.remove(actualRoute.size()-1);
                    }
                }
            } 
        }
        return 0;
    }

    public ArrayList<Integer> addNode(ArrayList<Integer> actualRoute, int x, int y){

        ArrayList<Integer> actualRoute1 = actualRoute;  

        if(actualRoute1.size() == 1){
            actualRoute1.add(y);
            lastAdded = 2;
            return actualRoute1;
        }

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
