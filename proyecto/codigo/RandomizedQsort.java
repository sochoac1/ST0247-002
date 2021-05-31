import java.util.*;

class RandomizedQsort
{    
    // This Function helps in calculating
    // random numbers between low(inclusive)
    // and high(inclusive)
    static void random(ArrayList<Pair<int[],Double>>arr,int low,int high)
    {
     
        Random rand= new Random();
        int pivot = rand.nextInt(high-low)+low;
         
        Pair<int[],Double> temp1=arr.get(pivot); 
        arr.set(pivot, arr.get(high));
        arr.set(high, temp1);
    }
     
    /* This function takes last element as pivot,
    places the pivot element at its correct
    position in sorted array, and places all
    smaller (smaller than pivot) to left of
    pivot and all greater elements to right
    of pivot */
    static int partition(ArrayList<Pair<int[],Double>>arr, int low, int high)
    {
        // pivot is choosen randomly
        random(arr,low,high);
        double pivot = arr.get(high).second;
     
 
        int i = (low-1); // index of smaller element
        for (int j = low; j < high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr.get(j).second > pivot)
            {
                i++;
 
                // swap arr[i] and arr[j]
                Pair<int[],Double> temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j,temp);
            }
        }
 
        // swap arr[i+1] and arr[high] (or pivot)
        Pair<int[],Double> temp = arr.get(i+1);
        arr.set(i+1, arr.get(high));
        arr.set(high, temp);
 
        return i+1;
    }
 
 
    /* The main function that implements QuickSort()
    arr[] --> Array to be sorted,
    low --> Starting index,
    high --> Ending index */
    static void sort(ArrayList<Pair<int[],Double>>arr, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
            now at right place */
            int pi = partition(arr, low, high);
 
            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }
 
    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }
 
    // Driver Code
    public static void main(String args[])
    {
        /*
        int arr[] = {10, 7, 8, 9, 1, 5};
        int n = arr.length;
 
        sort(arr, 0, n-1);
 
        System.out.println("Sorted array");
        printArray(arr);
        */
    }
}