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
    double[][] stations ;
    static int client = ElectricVehiclesRoutes.client;
    public void savingsCalculation(Digraph map, int size){
        stations = new double[size][size];
        for(int i = 1; i < size; i++){
            for(int j = i + 1; j < size; j++){
                if(i < client && j < client){
                    double sum = (map.getWeight(0, i)/ElectricVehiclesRoutes.speed)*2 + (map.getWeight(0, j)/ElectricVehiclesRoutes.speed)*2;
                    double mergedRoutes = (map.getWeight(0,i)/ElectricVehiclesRoutes.speed) + (map.getWeight(0,j)/ElectricVehiclesRoutes.speed) + (map.getWeight(i, j)/ElectricVehiclesRoutes.speed);
                    double total = sum - mergedRoutes;
                    int[] nodes = {i,j};
                    Pair<int[], Double> saves = new Pair(nodes, total);
                    savings.add(saves);
                }else{
                    stations[i][j] = map.getWeight(i, j); //Guarda distancia de (estación-estación)//(cliente-estacion)
                    //Estaciones recorremos columna
                    //Cuando busca en un cliente, se para en la fila, y busca de la 1ra columna estación para adelante.
                }
            }
        }
        visited = new boolean[client]; //Clientes visitados
        usedSavings = new boolean[savings.size()]; //Ahorros de clientes utilizados
        RandomizedQsort.sort(savings, 0, savings.size() -1); //Organiza de mayor a menor
        counter = client; //Variable de control para saber cuantos clientes he agregado cada vez que hago ruta
        //Calculo de los ahorros de las estaciones de carga
    } 

    public void routesConstruction(Digraph map){
        while(counter > 0){
            ArrayList<Integer> actualRoute = new ArrayList<>();
            int node1 = 0;
            int node2 = 0;
            int pos = 0;
            double totalTime = 0;
            ArrayList<Integer> removeSavings = new ArrayList<Integer>(); //Guarda el indice de los usedSavings acabados de poner en True
            //Busca el mejor ahorro y que no exceda el tiempo
            for(int i = 0; i < savings.size(); i++){
                totalTime = (map.getWeight(0,savings.get(i).first[0])/ElectricVehiclesRoutes.speed) + (map.getWeight(0,savings.get(i).first[1])/ElectricVehiclesRoutes.speed)+(map.getWeight(savings.get(i).first[0], savings.get(i).first[1])/ElectricVehiclesRoutes.speed)+
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
            removeSavings.add(pos);
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
                    removeSavings.add(j);
                    //j--;
                    continue;
                }              
                if((ini == node1) && (end == node2) ||(ini == node2) && (end == node1)){
                    //savings.remove(j);
                    usedSavings[j] = true;
                    removeSavings.add(j);
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
                        removeSavings.add(j);
                    }else{
                        if(lastAdded == 1)actualRoute.remove(0);
                        if(lastAdded == 2)actualRoute.remove(actualRoute.size()-1);
                        /*savings.remove(j);
                        j--;
                         */
                        removeSavings.add(j);
                        usedSavings[j] = true;
                    }
                }

            }
            visited[node1] = true;
            visited[node2] = true;
            counter = counter - actualRoute.size();
            //Nuevos metodos implementados
            //Ponemos en False todos los usedSavings colocados en true anteriormente
            for(int i = 0; i < removeSavings.size(); i++){
                usedSavings[removeSavings.get(i)] = false;
            }
            //Ponemos en false todos los nodos de la rutaActual
            for(int i = 0; i < actualRoute.size();i++ ){
                visited[actualRoute.get(i)] = false;
            }
            //Formamos nueva ruta
            ArrayList<Integer> newRoute = routeReconstruction(actualRoute, map, stations);
            //Se colocan en true usedSavings de acuerdo a la nueva ruta
            for(int i = 0; i < removeSavings.size();i++){
                for(int j = 0; j < newRoute.size(); j++){
                    if(savings.get(removeSavings.get(i)).first[0] == newRoute.get(j)){
                        usedSavings[removeSavings.get(i)] = true;
                    }else if(savings.get(removeSavings.get(i)).first[1] == newRoute.get(j)){
                        usedSavings[removeSavings.get(i)] = true;
                    }
                }
            }
            //Ponemos en true los nodos visitados
            for(int i = 0; i < newRoute.size();i++ ){
                if(newRoute.get(i) <= client){
                    visited[newRoute.get(i)] = true;
                }
            }

            routes.add(newRoute);
        }
    }   

    public boolean itCanBeAddIt(ArrayList<Integer> actualRoute, Digraph map){
        double maxTime = 10.0;
        double visitTime = ElectricVehiclesRoutes.st_customer*actualRoute.size();
        for(int i = 0; i<actualRoute.size(); i++){
            if(i == actualRoute.size()-1){
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i)) / ElectricVehiclesRoutes.speed);
                break;
            }
            if(i == 0){    
                maxTime = maxTime-(map.getWeight(0,actualRoute.get(i)) / ElectricVehiclesRoutes.speed);
            }
            maxTime = maxTime-(map.getWeight(actualRoute.get(i), actualRoute.get(i+1)) / ElectricVehiclesRoutes.speed);
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

    //Codigo no sirve 
    /*
     * @param actualRoute Means the route which is going to be modified including charge stations
     * @param map It contains the adjacency matrix
     * @param chargeStations It contains the distances between each node inlcuding charge stations
     */
    public static ArrayList<Integer> routeReconstruction(ArrayList<Integer> actualRoute, Digraph map, double[][] chargeStations){

        ArrayList<Integer> newRoute = new ArrayList<Integer>(); //Nueva ruta
        double actualBattery = 16000.0; //Se inicializa en el tope, se irá consumiendo
        double maxTime = 10.0; //Trabajador no exceda las 10 horas
        double consumption = 5000.0; //Tasa de consumo, speed * r
        int s = 0;
        for(int i = 0; i < actualRoute.size(); i++){
            //Cliente conectado con el deposito
            if(i==0){
                double spentBattery = (map.getWeight(0, actualRoute.get(i)) / ElectricVehiclesRoutes.speed) * consumption;
                //Preguntamos si alcanza o no la bateria
                if(actualBattery - spentBattery < 6000){
                    //Buscamos estación de carga más cercana a i
                    int station = chargeSt(actualRoute, chargeStations, actualRoute.get(i), map.size);
                    newRoute.add(station); //Añade estación a la ruta nueva
                    newRoute.add(actualRoute.get(0));
                    spentBattery = (map.getWeight(0, station) / ElectricVehiclesRoutes.speed) *consumption;
                    double spentTime = chargingTime(actualBattery-spentBattery, station); //Envío batería que gasta yendo
                    spentBattery = (map.getWeight(station, actualRoute.get(i))/ElectricVehiclesRoutes.speed)*consumption; //Gasto de batería yendo al nodo que antes no podía
                    actualBattery = actualBattery - spentBattery;
                    maxTime = maxTime- (map.getWeight(0, station)/ElectricVehiclesRoutes.speed) - spentTime - (map.getWeight(station, actualRoute.get(i)) / ElectricVehiclesRoutes.speed);
                }else{
                    actualBattery = actualBattery - spentBattery;
                    maxTime = maxTime -(map.getWeight(0, actualRoute.get(i)));
                    newRoute.add(actualRoute.get(i));
                }
            }else if(i == actualRoute.size()-1){
                //Preguntamos por ultimo registro agregado, si o no
                double spentBattery = map.getWeight(i,0)*consumption;
                actualBattery = actualBattery - spentBattery;
                if(actualBattery > 0){
                    return newRoute;
                }else{
                    newRoute.remove(actualRoute.size()-1);
                    return newRoute;
                }
            }
            //Bateria consumida para ir al nodo que sigue
            double spentBattery = (map.getWeight(i, actualRoute.get(i+1)) / ElectricVehiclesRoutes.speed) * consumption;
            //Tiempo consumido para ir al nodo que sigue
            double tNextN = maxTime - (map.getWeight(i, actualRoute.get(i+1))/ElectricVehiclesRoutes.speed);
            if(tNextN <= 2){
                return newRoute;
            }else if(tNextN > 2){
                if(actualBattery - spentBattery < 6000){
                    //Buscamos estación de carga más cercana a i +1
                    int station = chargeSt(actualRoute, chargeStations, actualRoute.get(i + 1), map.size);
                    newRoute.add(station); //Añade estación a la ruta nueva
                    newRoute.add(actualRoute.get(i+1));
                    spentBattery = (map.getWeight(i, station) / ElectricVehiclesRoutes.speed) *consumption;
                    double spentTime = chargingTime(actualBattery-spentBattery, station); //Envío batería que gasta yendo
                    spentBattery = (map.getWeight(station, actualRoute.get(i+1))/ElectricVehiclesRoutes.speed)*consumption; //Gasto de batería yendo al nodo que antes no podía
                    actualBattery = actualBattery - spentBattery;
                    maxTime = maxTime- (map.getWeight(i, station)/ElectricVehiclesRoutes.speed) - spentTime - (map.getWeight(station, actualRoute.get(i+1)) / ElectricVehiclesRoutes.speed);
                }else{
                    actualBattery = actualBattery - spentBattery;
                    maxTime = maxTime -(map.getWeight(i, actualRoute.get(i+1)));
                    newRoute.add(actualRoute.get(i+1));
                }
            }
        }
        return newRoute;
        
    }

    /**
     * Metodo que calcula la estacion de carga más cercana
     */   
    public static int chargeSt(ArrayList<Integer> actualRoute, double[][] chargeStations, int node, int size){
        double l = Double.MAX_VALUE; //Variable de control
        int s = 0;

        //Si es clente, revisa la fila en la columna cliente+1

        for(int i = client + 1; i < size; i++){
            if(l >= chargeStations[node][i] && chargeStations[node][i] != 0){
                l = chargeStations[node][i];
                s = i;
            }
        }  

        return s;
    }

    /**
     * Metodo que calcula el tiempo en cargar la bateria al 100%
     * */
    public static double chargingTime(double batteryAc, int s){
        short t = ElectricVehiclesRoutes.stationType[s-client-1];
        double slope = ElectricVehiclesRoutes.slopeFunctionLoad[t];
        if(batteryAc >= 0){
            double needItBattery = 16000 - batteryAc;
            return needItBattery/slope;
        }else{
            return 16000/slope;
        }
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
