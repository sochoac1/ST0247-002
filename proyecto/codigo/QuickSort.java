import java.util.ArrayList;
/**
 * Write a description of class sa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */


class QuickSort
{   
    /* This function takes last element as pivot,
    places the pivot element at its correct
    position in sorted array, and places all
    smaller (smaller than pivot) to left of
    pivot and all greater elements to right
    of pivot */
    static int partition(ArrayList<Pair<int[],Double>>arr, int low, int high)
    {
        double pivot = arr.get(high).second; 
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
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
        arr.set(high,temp);            

        return i+1;
    }

    /* The main function that implements QuickSort()
    arr[] --> Array to be sorted,
    low  --> Starting index,
    high  --> Ending index */
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
    
}

