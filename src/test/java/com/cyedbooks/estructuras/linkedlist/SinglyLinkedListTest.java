package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SinglyLinkedList")
class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new SinglyLinkedList<>();
    }

    @Nested
    @DisplayName("estructura vacía")
    class EmptyBehavior {
        @Test
        void isEmptyInitially() {
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }

        @Test
        void removeFirstThrows() {
            assertThrows(EmptyStructureException.class, () -> list.removeFirst());
        }

        @Test
        void removeLastThrows() {
            assertThrows(EmptyStructureException.class, () -> list.removeLast());
        }

        @Test
        void peekFirstThrows() {
            assertThrows(EmptyStructureException.class, () -> list.peekFirst());
        }

        @Test
        void peekLastThrows() {
            assertThrows(EmptyStructureException.class, () -> list.peekLast());
        }

        @Test
        void getThrowsIndexOutOfBounds() {
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        }

        @Test
        void containsReturnsFalse() {
            assertFalse(list.contains(1));
        }

        @Test
        void toArrayIsEmptyArray() {
            assertEquals(0, list.toArray().length);
        }
    }

    @Test
    @DisplayName("addFirst inserta en orden inverso al frente")
    void addFirstBuildsReverseOrder() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertEquals(List.of(1, 2, 3), toList());
    }

    @Test
    @DisplayName("addLast inserta en orden de llegada")
    void addLastBuildsForwardOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(List.of(1, 2, 3), toList());
        assertEquals(1, list.peekFirst());
        assertEquals(3, list.peekLast());
    }

    @Test
    @DisplayName("addAt inserta en posiciones intermedias, inicio y fin")
    void addAtVariousPositions() {
        list.addLast(1);
        list.addLast(3);
        list.addAt(1, 2);
        list.addAt(0, 0);
        list.addAt(4, 4);
        assertEquals(List.of(0, 1, 2, 3, 4), toList());
    }

    @Test
    void addAtInvalidIndexThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(1, 1));
    }

    @Test
    @DisplayName("removeFirst/removeLast devuelven y quitan el elemento correcto")
    void removeFromEnds() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertEquals(1, list.removeFirst());
        assertEquals(3, list.removeLast());
        assertEquals(List.of(2), toList());
        assertEquals(1, list.size());

        assertEquals(2, list.removeFirst());
        assertTrue(list.isEmpty());
        list.addLast(99);
        assertEquals(99, list.peekFirst());
        assertEquals(99, list.peekLast());
    }

    @Test
    @DisplayName("removeAt en posición intermedia mantiene enlaces consistentes")
    void removeAtMiddle() {
        for (int i = 0; i < 5; i++) {
            list.addLast(i);
        }
        assertEquals(2, list.removeAt(2));
        assertEquals(List.of(0, 1, 3, 4), toList());
        assertEquals(4, list.size());
    }

    @Test
    @DisplayName("removeAt del último actualiza la cola correctamente")
    void removeAtLastUpdatesTail() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.removeAt(2);
        assertEquals(2, list.peekLast());
        list.addLast(4);
        assertEquals(List.of(1, 2, 4), toList());
    }

    @Test
    void removeAtInvalidIndexThrows() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(1));
    }

    @Test
    @DisplayName("remove(element) encuentra y quita la primera ocurrencia")
    void removeByValue() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(2);
        list.addLast(3);

        assertTrue(list.remove(2));
        assertEquals(List.of(1, 2, 3), toList());
        assertFalse(list.remove(99));
    }

    @Test
    @DisplayName("remove por valor del único elemento deja la lista vacía y consistente")
    void removeOnlyElementByValue() {
        list.addLast(42);
        assertTrue(list.remove(42));
        assertTrue(list.isEmpty());
        list.addLast(7);
        assertEquals(7, list.peekLast());
    }

    @Test
    void getAndSet() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(20, list.get(1));
        assertEquals(20, list.set(1, 99));
        assertEquals(99, list.get(1));
    }

    @Test
    void indexOfAndContains() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(1, list.indexOf(20));
        assertEquals(-1, list.indexOf(999));
        assertTrue(list.contains(30));
        assertFalse(list.contains(999));
    }

    @Test
    @DisplayName("clear vacía la lista y permite reutilizarla")
    void clearResetsList() {
        list.addLast(1);
        list.addLast(2);
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        list.addLast(5);
        assertEquals(List.of(5), toList());
    }

    @Test
    @DisplayName("iterator recorre en orden y respeta el contrato de Iterator")
    void iteratorContract() {
        list.addLast(1);
        list.addLast(2);
        var it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertFalse(it.hasNext());
        assertThrows(java.util.NoSuchElementException.class, it::next);
    }

    @Test
    void toArrayPreservesOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertArrayEquals(new Object[] {1, 2, 3}, list.toArray());
    }

    @Test
    @DisplayName("soporta muchos elementos sin romper enlaces (stress)")
    void manyElementsStress() {
        int n = 10_000;
        for (int i = 0; i < n; i++) {
            list.addLast(i);
        }
        assertEquals(n, list.size());
        assertEquals(0, list.peekFirst());
        assertEquals(n - 1, list.peekLast());
        for (int i = 0; i < n; i++) {
            assertEquals(i, list.removeFirst());
        }
        assertTrue(list.isEmpty());
    }

    @Test
    void drawNeverReturnsNull() {
        assertNotNull(list.draw());
        list.addLast(1);
        assertNotNull(list.draw());
        assertTrue(list.draw().contains("1"));
    }

    private List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        for (Integer i : list) {
            result.add(i);
        }
        return result;
    }
}
