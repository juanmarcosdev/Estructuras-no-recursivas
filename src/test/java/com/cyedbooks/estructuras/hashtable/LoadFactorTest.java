package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.hashtable.collision.ChainingStrategy;
import com.cyedbooks.estructuras.hashtable.collision.LinearProbing;
import com.cyedbooks.estructuras.hashtable.impl.DivisionHash;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HashTable: factor de carga y redimensionamiento")
class LoadFactorTest {

    @Test
    @DisplayName("el factor de carga aumenta con cada put y nunca supera el umbral configurado")
    void loadFactorNeverExceedsThreshold() {
        double threshold = 0.6;
        HashTable<Integer, Integer> table =
                new HashTable<>(8, new DivisionHash<>(), new ChainingStrategy(), threshold);

        for (int i = 0; i < 1000; i++) {
            table.put(i, i);
            assertTrue(table.loadFactor() <= threshold + 1e-9,
                    "factor de carga " + table.loadFactor() + " superó el umbral " + threshold);
        }
    }

    @Test
    @DisplayName("la capacidad interna crece (se duplica) automáticamente al superar el umbral")
    void capacityGrowsAutomatically() {
        HashTable<Integer, Integer> table = new HashTable<>(4);
        int initialCapacity = table.capacity();
        for (int i = 0; i < 100; i++) {
            table.put(i, i);
        }
        assertTrue(table.capacity() > initialCapacity);
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get(i));
        }
    }

    @Test
    @DisplayName("resize preserva todas las entradas también con direccionamiento abierto")
    void resizePreservesEntriesWithOpenAddressing() {
        HashTable<Integer, String> table =
                new HashTable<>(4, new DivisionHash<>(), new LinearProbing());
        for (int i = 0; i < 200; i++) {
            table.put(i, "valor" + i);
        }
        assertEquals(200, table.size());
        for (int i = 0; i < 200; i++) {
            assertEquals("valor" + i, table.get(i));
        }
    }

    @Test
    @DisplayName("las remociones (tombstones) también disparan un redimensionamiento correcto")
    void resizeWorksAfterRemovalsLeaveTombstones() {
        HashTable<Integer, String> table =
                new HashTable<>(4, new DivisionHash<>(), new LinearProbing());
        for (int i = 0; i < 20; i++) {
            table.put(i, "v" + i);
        }
        for (int i = 0; i < 10; i++) {
            table.remove(i);
        }
        for (int i = 20; i < 40; i++) {
            table.put(i, "v" + i);
        }
        for (int i = 10; i < 40; i++) {
            assertEquals("v" + i, table.get(i));
        }
        for (int i = 0; i < 10; i++) {
            assertNull(table.get(i));
        }
    }

    @Test
    void constructorRejectsInvalidLoadFactorThreshold() {
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, Integer>(8, new DivisionHash<>(), new ChainingStrategy(), 0.0));
        assertThrows(IllegalArgumentException.class,
                () -> new HashTable<Integer, Integer>(8, new DivisionHash<>(), new ChainingStrategy(), 1.0));
    }

    @Test
    void constructorRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<Integer, Integer>(0));
    }
}
