package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.hashtable.collision.ChainingStrategy;
import com.cyedbooks.estructuras.hashtable.collision.CollisionStrategy;
import com.cyedbooks.estructuras.hashtable.collision.LinearProbing;
import com.cyedbooks.estructuras.hashtable.collision.QuadraticProbing;
import com.cyedbooks.estructuras.hashtable.impl.DivisionHash;
import com.cyedbooks.estructuras.hashtable.impl.FNV1aHash;
import com.cyedbooks.estructuras.hashtable.impl.MultiplicationHash;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Funciones de hash y estrategias de colisión")
class CollisionStrategyTest {

    // ---- HashFunction: rango de salida ----

    @Test
    void divisionHashAlwaysInRange() {
        assertHashInRangeForManyKeys(new DivisionHash<>(), 17);
    }

    @Test
    void multiplicationHashAlwaysInRange() {
        assertHashInRangeForManyKeys(new MultiplicationHash<>(), 17);
    }

    @Test
    void fnv1aHashAlwaysInRangeForStrings() {
        int tableSize = 31;
        FNV1aHash hash = new FNV1aHash();
        for (String key : new String[] {"a", "ab", "hola mundo", "", "estructuras-no-recursivas"}) {
            int index = hash.hash(key, tableSize);
            assertTrue(index >= 0 && index < tableSize);
        }
    }

    @Test
    @DisplayName("FNV-1a es determinístico: la misma cadena siempre produce el mismo índice")
    void fnv1aIsDeterministic() {
        FNV1aHash hash = new FNV1aHash();
        assertEquals(hash.hash("clave", 100), hash.hash("clave", 100));
    }

    private void assertHashInRangeForManyKeys(com.cyedbooks.estructuras.hashtable.HashFunction<Integer> fn, int tableSize) {
        Random random = new Random(123);
        for (int i = 0; i < 10_000; i++) {
            int key = random.nextInt();
            int index = fn.hash(key, tableSize);
            assertTrue(index >= 0 && index < tableSize,
                    "índice " + index + " fuera de rango [0, " + tableSize + ") para clave " + key);
        }
    }

    // ---- CollisionStrategy: forma de la secuencia de sondeo ----

    @Test
    void linearProbingAdvancesByOne() {
        CollisionStrategy strategy = new LinearProbing();
        int capacity = 10;
        int base = 3;
        assertEquals(3, strategy.probe(base, 0, capacity));
        assertEquals(4, strategy.probe(base, 1, capacity));
        assertEquals(5, strategy.probe(base, 2, capacity));
        assertFalse(strategy.usesChaining());
    }

    @Test
    void linearProbingWrapsAroundCapacity() {
        CollisionStrategy strategy = new LinearProbing();
        int capacity = 5;
        assertEquals(0, strategy.probe(3, 2, capacity)); // (3+2) % 5 == 0
    }

    @Test
    void linearProbingVisitsEveryIndexExactlyOnceOverFullCycle() {
        CollisionStrategy strategy = new LinearProbing();
        int capacity = 13;
        boolean[] seen = new boolean[capacity];
        for (int attempt = 0; attempt < capacity; attempt++) {
            int idx = strategy.probe(4, attempt, capacity);
            assertFalse(seen[idx], "índice " + idx + " visitado más de una vez");
            seen[idx] = true;
        }
        for (boolean visited : seen) {
            assertTrue(visited);
        }
    }

    @Test
    void quadraticProbingGrowsQuadratically() {
        CollisionStrategy strategy = new QuadraticProbing();
        int capacity = 100;
        int base = 0;
        assertEquals(0, strategy.probe(base, 0, capacity));
        assertEquals(1, strategy.probe(base, 1, capacity));
        assertEquals(4, strategy.probe(base, 2, capacity));
        assertEquals(9, strategy.probe(base, 3, capacity));
        assertFalse(strategy.usesChaining());
    }

    @Test
    void chainingStrategyReportsUsesChaining() {
        CollisionStrategy strategy = new ChainingStrategy();
        assertTrue(strategy.usesChaining());
        assertEquals(3, strategy.probe(3, 0, 10));
    }

    // ---- Integración: la HashTable funciona igual (semánticamente) con cualquier combinación ----

    static Stream<CollisionStrategy> allStrategies() {
        return Stream.of(new ChainingStrategy(), new LinearProbing(), new QuadraticProbing());
    }

    @ParameterizedTest
    @MethodSource("allStrategies")
    @DisplayName("cualquier combinación de HashFunction + CollisionStrategy preserva la semántica put/get/remove")
    void anyStrategyPreservesSemantics(CollisionStrategy strategy) {
        HashTable<Integer, String> table = new HashTable<>(8, new DivisionHash<>(), strategy);
        Map<Integer, String> reference = new HashMap<>();
        Random random = new Random(strategy.getClass().getSimpleName().hashCode());

        for (int i = 0; i < 2000; i++) {
            int key = random.nextInt(300);
            int op = random.nextInt(3);
            if (op == 0) {
                String value = "v" + key;
                table.put(key, value);
                reference.put(key, value);
            } else if (op == 1) {
                assertEquals(reference.remove(key), table.remove(key));
            } else {
                assertEquals(reference.get(key), table.get(key));
            }
        }
        assertEquals(reference.size(), table.size());
        for (Map.Entry<Integer, String> e : reference.entrySet()) {
            assertEquals(e.getValue(), table.get(e.getKey()));
        }
    }

    @ParameterizedTest
    @MethodSource("allStrategies")
    @DisplayName("con MultiplicationHash también preserva la semántica")
    void anyStrategyWithMultiplicationHash(CollisionStrategy strategy) {
        HashTable<String, Integer> table = new HashTable<>(8, new MultiplicationHash<>(), strategy);
        for (int i = 0; i < 100; i++) {
            table.put("clave" + i, i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get("clave" + i));
        }
    }

    @Test
    @DisplayName("FNV1aHash combinado con ChainingStrategy funciona para claves String")
    void fnv1aHashWithChaining() {
        HashTable<String, Integer> table = new HashTable<>(8, new FNV1aHash(), new ChainingStrategy());
        String[] words = {"pila", "cola", "lista", "tabla", "hash", "nodo", "arreglo", "heap"};
        for (int i = 0; i < words.length; i++) {
            table.put(words[i], i);
        }
        for (int i = 0; i < words.length; i++) {
            assertEquals(i, table.get(words[i]));
        }
    }
}
