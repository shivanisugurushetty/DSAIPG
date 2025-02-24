package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Benchmarking class for various heap implementations.
 */
public class HeapBenchmarkTest {

    private static final int M = 4095;
    private static final int INSERTIONS = 16000;
    private static final int DELETIONS = 4000;

    public static void main(String[] args) {
        Random random = new Random();
        Comparator<Integer> comparator = Integer::compareTo;

        // Binary Heap with Floyd's Trick
        BinaryHeapWithFloyd<Integer> binaryHeapWithFloyd = new BinaryHeapWithFloyd<>(M, comparator);
        runBenchmark("Binary Heap with Floyd's Trick", binaryHeapWithFloyd, random);

        // 4-ary Heap
        FourAryHeap<Integer> fourAryHeap = new FourAryHeap<>(M, comparator);
        runBenchmark("4-ary Heap", fourAryHeap, random);

        // 4-ary Heap with Floyd's Trick
        FourAryHeapWithFloyd<Integer> fourAryHeapWithFloyd = new FourAryHeapWithFloyd<>(M, comparator);
        runBenchmark("4-ary Heap with Floyd's Trick", fourAryHeapWithFloyd, random);

        // Fibonacci Heap
        FibonacciHeap<Integer> fibonacciHeap = new FibonacciHeap<>(comparator);
        runFibonacciBenchmark("Fibonacci Heap", fibonacciHeap, random);
    }

    private static void runBenchmark(String name, PriorityQueue<Integer> heap, Random random) {
        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                name,
                null,
                (PriorityQueue<Integer> h) -> {
                    // Clear the heap for a fresh run
                    h.clear();
    
                    // Insert elements
                    for (int i = 0; i < INSERTIONS; i++) {
                        h.give(random.nextInt(100000)); // Proper insertion
                    }
                    System.out.println(name + " - Elements inserted: " + h.size());
    
                    // Remove elements
                    if (h.size() >= DELETIONS) {
                        for (int i = 0; i < DELETIONS; i++) {
                            try {
                                h.take();
                            } catch (PQException e) {
                                System.err.println(name + " - Deletion error: " + e.getMessage());
                            }
                        }
                    } else {
                        System.err.println(name + " - Not enough elements to delete. Heap size: " + h.size());
                    }
    
                    // Spilled elements & mean priority tracking
                    System.out.println(name + " - Elements remaining after deletions: " + h.size());
                    System.out.println(name + " - Spilled Elements: " + h.getSpilledElementsCount());
                    System.out.println(name + " - Mean Priority of Removed Elements: " + h.getRemovedElementsMeanPriority());
                },
                null
        );
    
        double time = benchmark.runFromSupplier(() -> heap, 1); // Run on fresh heap
        System.out.println(name + " - Average Time: " + time + " ms");
    }
    

    private static void runFibonacciBenchmark(String name, FibonacciHeap<Integer> heap, Random random) {
        Benchmark_Timer<FibonacciHeap<Integer>> benchmark = new Benchmark_Timer<>(
                name,
                null,
                (FibonacciHeap<Integer> h) -> {
                    // Insert elements
                    for (int i = 0; i < INSERTIONS; i++) {
                        h.insert(random.nextInt(100000));
                    }

                    // Remove elements
                    if (h.size() >= DELETIONS) {
                        for (int i = 0; i < DELETIONS; i++) {
                            try {
                                h.extractMin();
                            } catch (NoSuchElementException e) {
                                System.err.println(name + " - Deletion error: " + e.getMessage());
                            }
                        }
                    } else {
                        System.err.println(name + " - Not enough elements to delete. Heap size: " + h.size());
                    }

                    System.out.println(name + " - Elements remaining after deletions: " + h.size());
                },
                null
        );

        double time = benchmark.runFromSupplier(() -> heap, 1); // Reuse the same heap
        System.out.println(name + " - Average Time: " + time + " ms");
    }
}
