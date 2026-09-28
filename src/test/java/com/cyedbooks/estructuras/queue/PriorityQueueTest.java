package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PriorityQueue")
class PriorityQueueTest {

    private PriorityQueue<Integer> pq;

    @BeforeEach
    void setUp() {
        pq = new PriorityQueue<>();
    }

    @Test
    void emptyQueueThrows() {
        assertThrows(EmptyStructureException.class, () -> pq.dequeue());
        assertThrows(EmptyStructureException.class, () -> pq.front());
    }

    @Test
    void rearIsUnsupported() {
        pq.enqueue(1);
        assertThrows(UnsupportedOperationException.class, () -> pq.rear());
    }

    @Test
    @DisplayName("dequeue siempre devuelve el menor elemento (min-heap con orden natural)")
    void dequeueReturnsAscendingOrderByDefault() {
        int[] values = {5, 3, 8, 1, 9, 2, 7, 4, 6, 0};
        for (int v : values) {
            pq.enqueue(v);
        }
        for (int expected = 0; expected <= 9; expected++) {
            assertEquals(expected, pq.dequeue());
        }
        assertTrue(pq.isEmpty());
    }

    @Test
    void frontDoesNotRemove() {
        pq.enqueue(5);
        pq.enqueue(1);
        pq.enqueue(3);
        assertEquals(1, pq.front());
        assertEquals(3, pq.size());
    }

    @Test
    @DisplayName("con Comparator personalizado se comporta como max-heap")
    void customComparatorReversesOrder() {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        int[] values = {5, 3, 8, 1, 9};
        for (int v : values) {
            maxHeap.enqueue(v);
        }
        assertEquals(9, maxHeap.dequeue());
        assertEquals(8, maxHeap.dequeue());
        assertEquals(5, maxHeap.dequeue());
        assertEquals(3, maxHeap.dequeue());
        assertEquals(1, maxHeap.dequeue());
    }

    @Test
    @DisplayName("acepta la interfaz Comparable propia del proyecto")
    void acceptsProjectComparable() {
        com.cyedbooks.estructuras.core.interfaces.Comparable<Integer> byDescending =
                (a, b) -> Integer.compare(b, a);
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(byDescending);
        maxHeap.enqueue(2);
        maxHeap.enqueue(10);
        maxHeap.enqueue(5);
        assertEquals(10, maxHeap.dequeue());
        assertEquals(5, maxHeap.dequeue());
        assertEquals(2, maxHeap.dequeue());
    }

    @Test
    @DisplayName("mantiene el orden de prioridad tras muchas inserciones/remociones aleatorias (stress)")
    void stressRandomOperationsMaintainHeapOrder() {
        Random random = new Random(42);
        java.util.TreeMap<Integer, Integer> reference = new java.util.TreeMap<>();
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        for (int op = 0; op < 5000; op++) {
            if (random.nextBoolean() || heap.isEmpty()) {
                int value = random.nextInt(100_000);
                heap.enqueue(value);
                reference.merge(value, 1, Integer::sum);
            } else {
                int expected = reference.firstKey();
                assertEquals(expected, heap.dequeue());
                reference.computeIfPresent(expected, (k, count) -> count == 1 ? null : count - 1);
            }
        }
        while (!reference.isEmpty()) {
            int expected = reference.firstKey();
            assertEquals(expected, heap.dequeue());
            reference.computeIfPresent(expected, (k, count) -> count == 1 ? null : count - 1);
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    void containsChecksHeapArray() {
        pq.enqueue(1);
        pq.enqueue(2);
        pq.enqueue(3);
        assertTrue(pq.contains(2));
        assertFalse(pq.contains(99));
    }

    @Test
    void clearResetsHeap() {
        pq.enqueue(1);
        pq.enqueue(2);
        pq.clear();
        assertTrue(pq.isEmpty());
        pq.enqueue(5);
        assertEquals(5, pq.front());
    }

    @Test
    void constructorRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new PriorityQueue<Integer>(0));
    }
}
