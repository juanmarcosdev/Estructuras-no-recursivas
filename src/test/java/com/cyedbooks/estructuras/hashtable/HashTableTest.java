package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.model.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HashTable (configuración por defecto: DivisionHash + encadenamiento)")
class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    void emptyTable() {
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("ausente"));
        assertFalse(table.containsKey("ausente"));
    }

    @Test
    void putAndGet() {
        assertNull(table.put("uno", 1));
        assertNull(table.put("dos", 2));
        assertEquals(1, table.get("uno"));
        assertEquals(2, table.get("dos"));
        assertEquals(2, table.size());
    }

    @Test
    @DisplayName("put sobre una clave existente actualiza el valor y devuelve el anterior")
    void putUpdatesExistingKey() {
        table.put("clave", 1);
        Integer previous = table.put("clave", 2);
        assertEquals(1, previous);
        assertEquals(2, table.get("clave"));
        assertEquals(1, table.size());
    }

    @Test
    void putNullKeyThrows() {
        assertThrows(NullPointerException.class, () -> table.put(null, 1));
    }

    @Test
    void nullValuesAreAllowed() {
        table.put("clave", null);
        assertTrue(table.containsKey("clave"));
        assertNull(table.get("clave"));
    }

    @Test
    void removeExistingKey() {
        table.put("a", 1);
        table.put("b", 2);
        assertEquals(1, table.remove("a"));
        assertFalse(table.containsKey("a"));
        assertEquals(1, table.size());
        assertNull(table.remove("a"));
    }

    @Test
    void removeAbsentKeyReturnsNull() {
        assertNull(table.remove("nunca-existió"));
    }

    @Test
    void containsValueFindsAnyMatchingEntry() {
        table.put("a", 1);
        table.put("b", 2);
        assertTrue(table.containsValue(2));
        assertFalse(table.containsValue(999));
    }

    @Test
    @DisplayName("colisión explícita: dos claves distintas en el mismo bucket coexisten correctamente")
    void handlesExplicitCollision() {
        HashTable<Integer, String> smallTable = new HashTable<>(4);
        smallTable.put(1, "uno");
        smallTable.put(5, "cinco");
        smallTable.put(9, "nueve");
        assertEquals("uno", smallTable.get(1));
        assertEquals("cinco", smallTable.get(5));
        assertEquals("nueve", smallTable.get(9));
        assertEquals(3, smallTable.size());

        smallTable.remove(5);
        assertNull(smallTable.get(5));
        assertEquals("uno", smallTable.get(1));
        assertEquals("nueve", smallTable.get(9));
    }

    @Test
    @DisplayName("keys(), values() y entries() son consistentes entre sí")
    void keysValuesEntriesConsistency() {
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        Set<String> keys = new HashSet<>();
        table.keys().forEach(keys::add);
        assertEquals(Set.of("a", "b", "c"), keys);

        Set<Integer> values = new HashSet<>();
        table.values().forEach(values::add);
        assertEquals(Set.of(1, 2, 3), values);

        Map<String, Integer> asMap = new HashMap<>();
        for (Entry<String, Integer> e : table.entries()) {
            asMap.put(e.getKey(), e.getValue());
        }
        assertEquals(Map.of("a", 1, "b", 2, "c", 3), asMap);
    }

    @Test
    void iteratorVisitsAllEntriesExactlyOnce() {
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);
        int count = 0;
        for (Entry<String, Integer> ignored : table) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void clearEmptiesAndAllowsReuse() {
        table.put("a", 1);
        table.put("b", 2);
        table.clear();
        assertTrue(table.isEmpty());
        assertNull(table.get("a"));
        table.put("c", 3);
        assertEquals(3, table.get("c"));
    }

    @Test
    @DisplayName("stress: put/get/remove contra un java.util.HashMap de referencia")
    void stressAgainstReferenceMap() {
        Random random = new Random(7);
        Map<String, Integer> reference = new HashMap<>();
        for (int i = 0; i < 5000; i++) {
            String key = "k" + random.nextInt(500);
            int op = random.nextInt(3);
            if (op == 0) {
                int value = random.nextInt();
                table.put(key, value);
                reference.put(key, value);
            } else if (op == 1) {
                assertEquals(reference.remove(key), table.remove(key));
            } else {
                assertEquals(reference.get(key), table.get(key));
            }
            assertEquals(reference.size(), table.size());
        }
        for (Map.Entry<String, Integer> e : reference.entrySet()) {
            assertEquals(e.getValue(), table.get(e.getKey()));
        }
    }

    @Test
    void drawNeverReturnsNull() {
        assertNotNull(table.draw());
        table.put("a", 1);
        assertTrue(table.draw().contains("a=1"));
    }
}
