package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LinkedStack")
class LinkedStackTest {

    private LinkedStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedStack<>();
    }

    @Test
    void emptyStackThrows() {
        assertThrows(EmptyStructureException.class, () -> stack.pop());
        assertThrows(EmptyStructureException.class, () -> stack.peek());
    }

    @Test
    void pushPopIsLIFO() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void peekDoesNotRemove() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void containsAndToArray() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertTrue(stack.contains(2));
        assertFalse(stack.contains(99));
        assertArrayEquals(new Object[] {3, 2, 1}, stack.toArray());
    }

    @Test
    void iteratorOrderIsTopFirstAndRespectsContract() {
        stack.push(1);
        stack.push(2);
        var it = stack.iterator();
        assertEquals(2, it.next());
        assertEquals(1, it.next());
        assertFalse(it.hasNext());
        assertThrows(java.util.NoSuchElementException.class, it::next);
    }

    @Test
    void clearEmptiesAndAllowsReuse() {
        stack.push(1);
        stack.push(2);
        stack.clear();
        assertTrue(stack.isEmpty());
        stack.push(5);
        assertEquals(5, stack.peek());
    }

    @Test
    @DisplayName("stress: no depende de arreglo, no debería degradarse con muchos elementos")
    void stressManyPushPop() {
        int n = 20_000;
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }
        assertEquals(n, stack.size());
        for (int i = n - 1; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
        assertTrue(stack.isEmpty());
    }

    @Test
    void drawNeverReturnsNull() {
        assertNotNull(stack.draw());
    }
}
