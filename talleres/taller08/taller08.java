/**
 * Clase en la cual se implementan los metodos del Taller 8
 * 
 * @author Santiago Ochoa Castaño & Miguel Angel Zapata
 */
public class taller08 {
	/**
	* Metodo que pretende implementar el funcionamiento del algoritmo MergeSort
	* de un conjunto de elementos
	* @param a un arreglo con elementos
	* 
	* para mas informacion ver
	* @see <a href="https://www.youtube.com/watch?v=JSceec-wEyw">
	*
	*/
	public static void mergesort(int[] a) {
        mergesort(a, 0, a.length - 1);
	}

    private static void mergesort(int[] a, int left, int right){
        if(left < right){
            //Encuentra el punto medio del vector.
            int middle = (left + right) / 2;

            //Divide la primera y segunda mitad (llamada recursiva).
            mergesort(a, left, middle);
            mergesort(a, middle+1, right);

            //Une las mitades.
            merge(a, left, middle, right);
        }
    }
    
    public static void merge(int arr[], int left, int middle, int right) {
        //Encuentra el tamaño de los sub-vectores para unirlos.
        int n1 = middle - left + 1;
        int n2 = right - middle;

        //Vectores temporales.
        int leftArray[] = new int [n1];
        int rightArray[] = new int [n2];

        //Copia los datos a los arrays temporales.
        for (int i=0; i < n1; i++) {
            leftArray[i] = arr[left+i];
        }
        for (int j=0; j < n2; j++) {
            rightArray[j] = arr[middle + j + 1];
        }
        /* Une los vectorestemporales. */

        //Indices inicial del primer y segundo sub-vector.
        int i = 0, j = 0;

        //Indice inicial del sub-vector arr[].
        int k = left;

        //Ordenamiento.
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                j++;
            }
            k++;
        }//Fin del while.

        /* Si quedan elementos por ordenar */
        //Copiar los elementos restantes de leftArray[].
        while (i < n1) {
            arr[k] = leftArray[i];
            i++;
            k++;
        }
        //Copiar los elementos restantes de rightArray[].
        while (j < n2) {
            arr[k] = rightArray[j];
            j++;
            k++;
        }
    }
	/**
	* Metodo que pretende implementar el funcionamiento del algoritmo MergeSort
	* de un conjunto de elementos
	* @param a un arreglo con elementos
	* 
	* para mas informacion ver
	* @see <a href="https://www.youtube.com/watch?v=PgBzjlCcFvc">
	*
	*/
	public static void quicksort(int[] a) {
        quicksort(a, 0, a.length - 1);
	}

    private static void quicksort(int[] a, int i, int f){
        if( i <= f){
            int pos = partition(a, i, f); // mueve el pivote y deja menores a la izq, mayores a la der y retorna la pos
            quicksort(a, i, pos-1); // Mejor caso está la mitad, en el peor nada
            quicksort(a, pos+1, f); // Mejor caso está la mitad, en el peor todo
        }
    }
     
    private static int partition(int[] a, int i, int f){
        int pivote = a[f]; //Ultima posición del arreglo
        int pIndex = i;
        for(int indice = i; indice < f; indice++){
            if(a[indice] < pivote){
                swap(a, indice, pIndex);
                pIndex++;
            }
        }       
        swap(a,pIndex, f);
        return pIndex;
    }
     
    private static void swap(int[] a, int i, int j){
        int temp = a[i]; // En Python: a[i], a[j] = a[j], a[i]
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args){
        int[] a = {8, 3, 7, 5, 1, 4};

        mergesort(a);
        for(int i = 0; i < a.length; i++){
            System.out.println(a[i]);

        }
    }
     

}
