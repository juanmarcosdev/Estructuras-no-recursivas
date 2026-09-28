package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CircularLinkedList")
class CircularLinkedListTest {

    private CircularLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CircularLinkedList<>();
    }

    @Test
    void emptyListThrowsOnAccess() {
        assertThrows(EmptyStructureException.class, () -> list.removeFirst());
        assertThrows(EmptyStructureException.class, () -> list.removeLast());
        assertThrows(EmptyStructureException.class, () -> list.peekFirst());
        assertThrows(EmptyStructureException.class, () -> list.peekLast());
    }

    @Test
    void addFirstAndAddLastBothO1() {
        list.addLast(1);
        list.addLast(2);
        list.addFirst(0);
        assertEquals(List.of(0, 1, 2), toList());
        assertEquals(0, list.peekFirst());
        assertEquals(2, list.peekLast());
    }

    @Test
    @DisplayName("la iteración se detiene exactamente en size() pasos, sin ciclar infinitamente")
    void iterationStopsAfterExactlySizeElements() {
        for (int i = 0; i < 5; i++) {
            list.addLast(i);
        }
        int count = 0;
        for (Integer ignored : list) {
            count++;
            assertTrue(count <= 5, "El iterador no debe ciclar más allá de size() elementos");
        }
        assertEquals(5, count);
    }

    @Test
    void singleElementRemoveFirstEmptiesList() {
        list.addFirst(42);
        assertEquals(42, list.removeFirst());
        assertTrue(list.isEmpty());
        list.addLast(1);
        list.addLast(2);
        assertEquals(List.of(1, 2), toList());
    }

    @Test
    void singleElementRemoveLastEmptiesList() {
        list.addFirst(42);
        assertEquals(42, list.removeLast());
        assertTrue(list.isEmpty());
    }

    @Test
    void removeLastWithMultipleElements() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(3, list.removeLast());
        assertEquals(List.of(1, 2), toList());
        assertEquals(2, list.peekLast());
    }

    @Test
    void removeAtMiddleAndWrapAround() {
        for (int i = 0; i < 5; i++) {
            list.addLast(i);
        }
        assertEquals(2, list.removeAt(2));
        assertEquals(List.of(0, 1, 3, 4), toList());
    }

    @Test
    void removeByValueHandlesTailElement() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.remove(3));
        assertEquals(List.of(1, 2), toList());
        assertEquals(2, list.peekLast());
        list.addLast(9);
        assertEquals(List.of(1, 2, 9), toList());
    }

    @Test
    void removeByValueNotFound() {
        list.addLast(1);
        assertFalse(list.remove(999));
    }

    @Test
    void getSetIndexOfContains() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(20, list.get(1));
        assertEquals(20, list.set(1, 99));
        assertEquals(1, list.indexOf(99));
        assertTrue(list.contains(30));
        assertFalse(list.contains(20));
    }

    @Test
    void indexOutOfBoundsExceptions() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(5));
    }

    @Test
    void clearBreaksCycleAndAllowsReuse() {
        for (int i = 0; i < 5; i++) {
            list.addLast(i);
        }
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        list.addLast(100);
        assertEquals(List.of(100), toList());
    }

    @Test
    @DisplayName("stress: muchas inserciones/remociones mantienen la circularidad íntegra")
    void stressManyOperations() {
        int n = 5000;
        for (int i = 0; i < n; i++) {
            list.addLast(i);
        }
        assertEquals(n, list.size());
        for (int i = 0; i < n; i++) {
            assertEquals(i, list.removeFirst());
        }
        assertTrue(list.isEmpty());
    }

    @Test
    void drawShowsWrapAroundNotation() {
        list.addLast(1);
        list.addLast(2);
        assertTrue(list.draw().contains("vuelve al inicio"));
    }

    private List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        for (Integer i : list) {
            result.add(i);
        }
        return result;
    }
}
