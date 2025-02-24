package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;

/**
 * FourAryHeapWithFloyd extends FourAryHeap with Floyd’s trick enabled by default.
 *
 * @param <K> The type of elements maintained by this priority queue.
 */
public class FourAryHeapWithFloyd<K> extends FourAryHeap<K> {

    /**
     * Constructor for FourAryHeapWithFloyd.
     *
     * @param n          the capacity of the heap
     * @param comparator comparator for ordering elements
     */
    public FourAryHeapWithFloyd(int n, Comparator<K> comparator) {
        super(n, comparator, true); 
    }
}
