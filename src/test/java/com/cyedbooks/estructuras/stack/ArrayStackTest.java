package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrayStack")
class ArrayStackTest {

    private ArrayStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new ArrayStack<>();
    }

    @Test
    void emptyStackThrows() {
        assertTrue(stack.isEmpty());
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
    void constructorRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayStack<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new ArrayStack<Integer>(-5));
    }

    @Test
    @DisplayName("crece automáticamente más allá de la capacidad inicial (resize)")
    void growsBeyondInitialCapacity() {
        ArrayStack<Integer> small = new ArrayStack<>(2);
        for (int i = 0; i < 50; i++) {
            small.push(i);
        }
        assertEquals(50, small.size());
        for (int i = 49; i >= 0; i--) {
            assertEquals(i, small.pop());
        }
    }

    @Test
    @DisplayName("se reduce tras muchos pop y sigue funcionando correctamente")
    void shrinksAfterManyPops() {
        for (int i = 0; i < 100; i++) {
            stack.push(i);
        }
        for (int i = 0; i < 95; i++) {
            stack.pop();
        }
        assertEquals(5, stack.size());
        stack.push(999);
        assertEquals(999, stack.pop());
    }

    @Test
    void containsChecksAnyPosition() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertTrue(stack.contains(1));
        assertTrue(stack.contains(3));
        assertFalse(stack.contains(99));
    }

    @Test
    void toArrayOrderIsTopFirst() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertArrayEquals(new Object[] {3, 2, 1}, stack.toArray());
    }

    @Test
    void iteratorOrderIsTopFirst() {
        stack.push(1);
        stack.push(2);
        var it = stack.iterator();
        assertEquals(2, it.next());
        assertEquals(1, it.next());
        assertFalse(it.hasNext());
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
    void peekFirstAndPeekLastAreBothTheTop() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.peekFirst());
        assertEquals(2, stack.peekLast());
    }

    @Test
    void drawNeverReturnsNull() {
        assertNotNull(stack.draw());
        stack.push(1);
        assertTrue(stack.draw().contains("1"));
    }
}
