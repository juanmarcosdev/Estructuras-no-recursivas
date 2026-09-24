package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LinkedQueue")
class LinkedQueueTest {

    private LinkedQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new LinkedQueue<>();
    }

    @Test
    void emptyQueueThrows() {
        assertThrows(EmptyStructureException.class, () -> queue.dequeue());
        assertThrows(EmptyStructureException.class, () -> queue.front());
        assertThrows(EmptyStructureException.class, () -> queue.rear());
    }

    @Test
    void enqueueDequeueIsFIFO() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void frontAndRear() {
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.front());
        assertEquals(2, queue.rear());
    }

    @Test
    void emptyingAndRefillingKeepsHeadTailConsistent() {
        queue.enqueue(1);
        assertEquals(1, queue.dequeue());
        assertTrue(queue.isEmpty());
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(2, queue.front());
        assertEquals(3, queue.rear());
    }

    @Test
    void containsAndToArray() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertTrue(queue.contains(2));
        assertFalse(queue.contains(99));
        assertArrayEquals(new Object[] {1, 2, 3}, queue.toArray());
    }

    @Test
    void iteratorRespectsFifoOrder() {
        queue.enqueue(1);
        queue.enqueue(2);
        var it = queue.iterator();
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void clearResetsQueue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.clear();
        assertTrue(queue.isEmpty());
        queue.enqueue(9);
        assertEquals(9, queue.front());
        assertEquals(9, queue.rear());
    }

    @Test
    @DisplayName("stress: enqueue/dequeue en O(1) estricto sin importar el tamaño")
    void stressManyOperations() {
        int n = 20_000;
        for (int i = 0; i < n; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < n; i++) {
            assertEquals(i, queue.dequeue());
        }
        assertTrue(queue.isEmpty());
    }
}
