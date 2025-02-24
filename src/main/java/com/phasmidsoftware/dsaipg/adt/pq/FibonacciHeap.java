package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 * Fibonacci Heap implementation for priority queues.
 *
 * @param <K> The type of elements maintained by this heap.
 */
public class FibonacciHeap<K> {

    private Node<K> min; // Pointer to the minimum node
    private int size; // Number of elements in the heap
    private final Comparator<K> comparator;
    private long prioritySum = 0;
    private int removedElementsCount = 0;

    /**
     * Constructor for FibonacciHeap with a custom comparator.
     *
     * @param comparator Comparator for ordering elements.
     */
    public FibonacciHeap(Comparator<K> comparator) {
        this.comparator = comparator;
        this.size = 0;
        this.min = null;
    }

    /**
     * Insert a new key into the heap.
     *
     * @param key the key to insert
     */
    public void insert(K key) {
        Node<K> node = new Node<>(key);
        min = mergeLists(min, node);
        size++;
    }

    /**
     * Extracts and returns the minimum element from the heap.
     *
     * @return the minimum key
     */
    public K extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }

        Node<K> minNode = min;
        if (minNode.child != null) {
            Node<K> child = minNode.child;
            do {
                child.parent = null;
                child = child.next;
            } while (child != minNode.child);

            // Merge the child list with the root list
            min = mergeLists(min, minNode.child);
        }

        // Remove minNode from the root list
        if (minNode.next == minNode) {
            min = null;
        } else {
            minNode.prev.next = minNode.next;
            minNode.next.prev = minNode.prev;
            min = minNode.next;
            consolidate();
        }

        size--;
        if (minNode.key instanceof Integer) {
            prioritySum += (Integer) minNode.key;
            removedElementsCount++;
        }
        return minNode.key;
    }

    /**
     * Peek at the minimum element without removing it.
     *
     * @return the minimum key
     */
    public K peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return min.key;
    }

    /**
     * Merge two Fibonacci heaps.
     *
     * @param other another FibonacciHeap to merge with this heap.
     */
    public void merge(FibonacciHeap<K> other) {
        min = mergeLists(min, other.min);
        size += other.size;
        other.min = null;
        other.size = 0;
    }

    /**
     * Check if the heap is empty.
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return min == null;
    }

    /**
     * Returns the number of elements in the heap.
     *
     * @return the size of the heap
     */
    public int size() {
        return size;
    }
    public double getRemovedElementsMeanPriority() {
        return removedElementsCount == 0 ? 0 : (double) prioritySum / removedElementsCount;
    }

    public void clear(){
        min = null;
        size = 0;
    }

    // Helper function to merge two circular doubly linked lists
    private Node<K> mergeLists(Node<K> a, Node<K> b) {
        if (a == null) return b;
        if (b == null) return a;

        Node<K> temp = a.next;
        a.next = b.next;
        a.next.prev = a;
        b.next = temp;
        b.next.prev = b;

        return comparator.compare(a.key, b.key) < 0 ? a : b;
    }

    // Consolidate the heap after extracting min
    private void consolidate() {
        Node<K>[] table = new Node[45]; // Should be enough for practical heap sizes
        Node<K>[] rootList = getRootList();

        for (Node<K> node : rootList) {
            int degree = node.degree;
            while (table[degree] != null) {
                Node<K> other = table[degree];
                if (comparator.compare(node.key, other.key) > 0) {
                    Node<K> temp = node;
                    node = other;
                    other = temp;
                }
                linkHeaps(other, node);
                table[degree] = null;
                degree++;
            }
            table[degree] = node;
        }

        min = null;
        for (Node<K> node : table) {
            if (node != null) {
                node.next = node.prev = node;
                min = mergeLists(min, node);
            }
        }
    }

    // Link two heaps of the same degree
    private void linkHeaps(Node<K> child, Node<K> parent) {
        child.next.prev = child.prev;
        child.prev.next = child.next;

        child.parent = parent;
        if (parent.child == null) {
            parent.child = child;
            child.next = child;
            child.prev = child;
        } else {
            child.next = parent.child.next;
            child.prev = parent.child;
            parent.child.next.prev = child;
            parent.child.next = child;
        }
        parent.degree++;
    }

    // Convert the circular linked list into an array for consolidation
    private Node<K>[] getRootList() {
        if (min == null) return new Node[0];

        Node<K> current = min;
        int count = 0;
        do {
            count++;
            current = current.next;
        } while (current != min);

        @SuppressWarnings("unchecked")
        Node<K>[] list = new Node[count];
        current = min;
        for (int i = 0; i < count; i++) {
            list[i] = current;
            current = current.next;
        }

        return list;
    }

    /**
     * Node class for Fibonacci heap elements.
     *
     * @param <K> the type of element stored in the node
     */
    private static class Node<K> {
        K key;
        Node<K> parent;
        Node<K> child;
        Node<K> next;
        Node<K> prev;
        int degree;

        Node(K key) {
            this.key = key;
            this.next = this;
            this.prev = this;
        }
    }
}
