package frc.robot.subsystems;

import java.util.Arrays;

public class MedianFilter 
{
    private float[] arrayForFilter;
    private int filterPowerInit = 3;

    public MedianFilter()
    {
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    public MedianFilter(int filterPower)
    {
        this.filterPowerInit = filterPower;
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    public float Filter(float val)
    {
        for (int i = filterPowerInit - 1; i > 0; i--)
        {
            this.arrayForFilter[i] = this.arrayForFilter[i - 1];
        }
        this.arrayForFilter[0] = val;

        float[] copiedArr = Arrays.copyOf(this.arrayForFilter, this.filterPowerInit);
        quickSort(copiedArr, 0, this.filterPowerInit - 1);

        return copiedArr[this.filterPowerInit / 2];
    }

    
    public void quickSort(float arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
     
            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }

    private int partition(float arr[], int begin, int end) {
        float pivot = arr[end];
        int i = (begin-1);
     
        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
     
                float swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
     
        float swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;
     
        return i+1;
    }
}

