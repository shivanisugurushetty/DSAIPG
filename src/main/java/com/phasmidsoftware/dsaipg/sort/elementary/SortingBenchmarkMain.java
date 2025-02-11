package com.phasmidsoftware.dsaipg.sort.elementary;

import java.util.Random;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

public class SortingBenchmarkMain {
    public static void main(String[] args) {
        int[] sizes = {500, 1000, 2000, 4000, 8000}; // Doubling method
        for (int n : sizes) {
            runBenchmark(n);
        }
    }

    private static void runBenchmark(int n) {
        System.out.println("\nRunning benchmarks for n = " + n);

        // Generate different types of input arrays
        Integer[] randomArray = generateRandomArray(n);
        Integer[] orderedArray = generateOrderedArray(n);
        Integer[] partialOrderedArray = generatePartiallyOrderedArray(n);
        Integer[] reverseOrderedArray = generateReverseOrderedArray(n);

        // Define the sorting function (InsertionSort in this case)
        Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<>("InsertionSort", 
            arr -> insertionSort(arr)
        );

        // Run and print benchmarks
        System.out.printf("Random: %.3f ms%n", benchmark.runFromSupplier(() -> randomArray.clone(), 10));
        System.out.printf("Ordered: %.3f ms%n", benchmark.runFromSupplier(() -> orderedArray.clone(), 10));
        System.out.printf("Partially Ordered: %.3f ms%n", benchmark.runFromSupplier(() -> partialOrderedArray.clone(), 10));
        System.out.printf("Reverse Ordered: %.3f ms%n", benchmark.runFromSupplier(() -> reverseOrderedArray.clone(), 10));
    }

    // Implement the sorting algorithm (Insertion Sort in this case)
    private static void insertionSort(Integer[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Helper functions to generate arrays
    private static Integer[] generateRandomArray(int n) {
        Random random = new Random();
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = random.nextInt(n);
        return arr;
    }

    private static Integer[] generateOrderedArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        return arr;
    }

    private static Integer[] generatePartiallyOrderedArray(int n) {
        Integer[] arr = generateOrderedArray(n);
        Random random = new Random();
        for (int i = 0; i < n / 10; i++) { // Shuffle 10% of elements
            int index = random.nextInt(n);
            int swapIndex = random.nextInt(n);
            int temp = arr[index];
            arr[index] = arr[swapIndex];
            arr[swapIndex] = temp;
        }
        return arr;
    }

    private static Integer[] generateReverseOrderedArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }
}

