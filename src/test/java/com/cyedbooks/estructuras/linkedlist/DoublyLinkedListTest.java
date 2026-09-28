package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DoublyLinkedList")
class DoublyLinkedListTest {

    private DoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test
    void emptyListThrowsOnAccess() {
        assertThrows(EmptyStructureException.class, () -> list.removeFirst());
        assertThrows(EmptyStructureException.class, () -> list.removeLast());
        assertThrows(EmptyStructureException.class, () -> list.peekFirst());
        assertThrows(EmptyStructureException.class, () -> list.peekLast());
    }

    @Test
    void addFirstAndAddLast() {
        list.addLast(2);
        list.addFirst(1);
        list.addLast(3);
        assertEquals(List.of(1, 2, 3), toList());
    }

    @Test
    @DisplayName("removeLast es O(1) y no requiere recorrer toda la lista: verificado indirectamente")
    void removeLastRepeatedly() {
        for (int i = 0; i < 100; i++) {
            list.addLast(i);
        }
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, list.removeLast());
        }
        assertTrue(list.isEmpty());
    }

    @Test
    void removeFirstRepeatedly() {
        for (int i = 0; i < 100; i++) {
            list.addLast(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, list.removeFirst());
        }
        assertTrue(list.isEmpty());
    }

    @Test
    void addAtMiddleFixesPrevAndNextLinks() {
        list.addLast(1);
        list.addLast(3);
        list.addAt(1, 2);
        assertEquals(List.of(1, 2, 3), toList());
        assertEquals(List.of(3, 2, 1), toReversedList());
    }

    @Test
    void removeAtMiddleFixesPrevAndNextLinks() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        assertEquals(2, list.removeAt(1));
        assertEquals(List.of(1, 3, 4), toList());
        assertEquals(List.of(4, 3, 1), toReversedList());
    }

    @Test
    void removeByValueUnlinksCorrectly() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.remove(2));
        assertEquals(List.of(1, 3), toList());
        assertEquals(List.of(3, 1), toReversedList());
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
        assertTrue(list.contains(99));
        assertFalse(list.contains(20));
    }

    @Test
    void indexAccessFromEitherEndYieldsSameResult() {
        for (int i = 0; i < 21; i++) {
            list.addLast(i);
        }
        assertEquals(2, list.get(2));
        assertEquals(18, list.get(18));
        assertEquals(10, list.get(10));
    }

    @Test
    void descendingIteratorReversesOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        List<Integer> descending = new ArrayList<>();
        var it = list.descendingIterator();
        while (it.hasNext()) {
            descending.add(it.next());
        }
        assertEquals(List.of(3, 2, 1), descending);
    }

    @Test
    void clearThenReuse() {
        list.addLast(1);
        list.addLast(2);
        list.clear();
        assertTrue(list.isEmpty());
        list.addFirst(5);
        list.addLast(6);
        assertEquals(List.of(5, 6), toList());
    }

    @Test
    void indexOutOfBoundsExceptions() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(2, 1));
    }

    @Test
    void singleElementRemoveLeavesConsistentEmptyState() {
        list.addLast(42);
        assertEquals(42, list.removeFirst());
        assertTrue(list.isEmpty());
        list.addLast(1);
        list.addLast(2);
        assertEquals(List.of(1, 2), toList());
    }

    private List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        for (Integer i : list) {
            result.add(i);
        }
        return result;
    }

    private List<Integer> toReversedList() {
        List<Integer> result = new ArrayList<>();
        var it = list.descendingIterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }
}
