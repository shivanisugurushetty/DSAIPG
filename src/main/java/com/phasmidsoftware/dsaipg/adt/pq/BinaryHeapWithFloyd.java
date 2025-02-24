package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;

/**
 * BinaryHeapWithFloyd extends PriorityQueue and enables Floyd’s trick by default.
 *
 * @param <K> The type of elements maintained by this priority queue.
 */
public class BinaryHeapWithFloyd<K> extends PriorityQueue<K> {

    /**
     * Constructor for BinaryHeapWithFloyd.
     *
     * @param n          the capacity of the heap
     * @param comparator comparator for ordering elements
     */
    public BinaryHeapWithFloyd(int n, Comparator<K> comparator) {
        super(n, true, comparator, true); // Enable Floyd’s trick by setting floyd = true
    }
}
