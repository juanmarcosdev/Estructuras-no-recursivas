package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrayQueue")
class ArrayQueueTest {

    private ArrayQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayQueue<>();
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
        queue.enqueue(3);
        assertEquals(1, queue.front());
        assertEquals(3, queue.rear());
    }

    @Test
    @DisplayName("el buffer circular envuelve correctamente (wrap-around) sin redimensionar")
    void circularWrapAroundWithoutResizing() {
        ArrayQueue<Integer> q = new ArrayQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());
        q.enqueue(4);
        q.enqueue(5);
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    @DisplayName("crece automáticamente preservando el orden lógico front→rear")
    void growsPreservingOrder() {
        ArrayQueue<Integer> q = new ArrayQueue<>(2);
        for (int i = 0; i < 50; i++) {
            q.enqueue(i);
        }
        assertEquals(50, q.size());
        for (int i = 0; i < 50; i++) {
            assertEquals(i, q.dequeue());
        }
    }

    @Test
    @DisplayName("crece correctamente incluso cuando el buffer estaba envuelto")
    void growsWhileWrapped() {
        ArrayQueue<Integer> q = new ArrayQueue<>(4);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.dequeue();
        q.dequeue();
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);
        assertEquals(List_of(3, 4, 5, 6, 7), drainAll(q));
    }

    @Test
    void shrinksAfterManyDequeues() {
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 95; i++) {
            queue.dequeue();
        }
        assertEquals(5, queue.size());
        queue.enqueue(999);
        assertEquals(95, queue.dequeue());
    }

    @Test
    void containsAndToArrayRespectFrontToRearOrder() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertTrue(queue.contains(2));
        assertFalse(queue.contains(99));
        assertArrayEquals(new Object[] {1, 2, 3}, queue.toArray());
    }

    @Test
    void iteratorRespectsFrontToRearOrder() {
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
    }

    @Test
    void constructorRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayQueue<Integer>(0));
    }

    private static java.util.List<Integer> List_of(Integer... values) {
        return java.util.Arrays.asList(values);
    }

    private java.util.List<Integer> drainAll(ArrayQueue<Integer> q) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        while (!q.isEmpty()) {
            result.add(q.dequeue());
        }
        return result;
    }
}
