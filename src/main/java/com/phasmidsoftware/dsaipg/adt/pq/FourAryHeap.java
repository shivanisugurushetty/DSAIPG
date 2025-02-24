package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;

/**
 * FourAryHeap extends PriorityQueue to implement a 4-ary heap.
 *
 * @param <K> The type of elements maintained by this priority queue.
 */
public class FourAryHeap<K> extends PriorityQueue<K> {

    /**
     * Constructor for FourAryHeap.
     *
     * @param n          the capacity of the heap
     * @param comparator comparator for ordering elements
     */
    public FourAryHeap(int n, Comparator<K> comparator) {
        super(n, true, comparator, false); // Floyd's trick is disabled by default
    }

    /**
     * Override firstChild method for 4-ary heap logic.
     *
     * @param k the index of the parent node
     * @return index of the first child
     */

     public FourAryHeap(int n, Comparator<K> comparator, boolean floyd) {
        super(n, true, comparator, floyd); // Pass floyd flag
    }
    
    @Override
    protected int firstChild(int k) {
        return (k - first) * 4 + 1 + first;
    }

    /**
     * Override parent method for 4-ary heap logic.
     *
     * @param k the index of the child node
     * @return index of the parent node
     */
    @Override
    protected int parent(int k) {
        return ((k - first - 1) / 4) + first;
    }
}
