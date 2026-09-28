package com.cyedbooks.estructuras.hashtable;

import com.cyedbooks.estructuras.core.exceptions.CapacityExceededException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.hashtable.collision.ChainingStrategy;
import com.cyedbooks.estructuras.hashtable.collision.CollisionStrategy;
import com.cyedbooks.estructuras.hashtable.impl.DivisionHash;
import com.cyedbooks.estructuras.model.Entry;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

public class HashTable<K, V> implements HashTableInterface<K, V>, Drawable {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_CHAINING_LOAD_FACTOR = 0.75;
    private static final double DEFAULT_OPEN_ADDRESSING_LOAD_FACTOR = 0.5;

    //marca de borrado, si se deja null se corta la busqueda por sondeo
    @SuppressWarnings("rawtypes")
    private static final Entry TOMBSTONE = new Entry<>(null, null);

    private Entry<K, V>[] table;
    private int capacity;
    private int size;
    private int occupiedSlots;

    private final HashFunction<K> hashFunction;
    private final CollisionStrategy collisionStrategy;
    private final double loadFactorThreshold;

    public HashTable() {
        this(DEFAULT_CAPACITY, new DivisionHash<>(), new ChainingStrategy());
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, new DivisionHash<>(), new ChainingStrategy());
    }

    public HashTable(HashFunction<K> hashFunction, CollisionStrategy collisionStrategy) {
        this(DEFAULT_CAPACITY, hashFunction, collisionStrategy);
    }

    public HashTable(int initialCapacity, HashFunction<K> hashFunction, CollisionStrategy collisionStrategy) {
        this(initialCapacity, hashFunction, collisionStrategy,
                collisionStrategy.usesChaining() ? DEFAULT_CHAINING_LOAD_FACTOR : DEFAULT_OPEN_ADDRESSING_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, HashFunction<K> hashFunction, CollisionStrategy collisionStrategy,
                      double loadFactorThreshold) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("La capacidad inicial debe ser >= 1");
        }
        if (loadFactorThreshold <= 0.0 || loadFactorThreshold >= 1.0) {
            throw new IllegalArgumentException("El factor de carga umbral debe estar en (0, 1)");
        }
        this.capacity = initialCapacity;
        this.table = new Entry[initialCapacity];
        this.hashFunction = Objects.requireNonNull(hashFunction, "hashFunction");
        this.collisionStrategy = Objects.requireNonNull(collisionStrategy, "collisionStrategy");
        this.loadFactorThreshold = loadFactorThreshold;
    }

    @Override
    public V put(K key, V value) {
        Objects.requireNonNull(key, "La clave no puede ser null");
        ensureCapacityForInsert();
        return collisionStrategy.usesChaining() ? putChaining(key, value) : putOpenAddressing(key, value);
    }

    private V putChaining(K key, V value) {
        int index = hashFunction.hash(key, capacity);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.getKey().equals(key)) {
                V old = current.getValue();
                current.setValue(value);
                return old;
            }
            current = current.getNext();
        }
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.setNext(table[index]);
        table[index] = newEntry;
        size++;
        occupiedSlots++;
        return null;
    }

    @SuppressWarnings("unchecked")
    private V putOpenAddressing(K key, V value) {
        int baseIndex = hashFunction.hash(key, capacity);
        int firstTombstone = -1;
        for (int attempt = 0; attempt < capacity; attempt++) {
            int index = normalize(collisionStrategy.probe(baseIndex, attempt, capacity));
            Entry<K, V> slot = table[index];
            if (slot == null) {
                if (firstTombstone != -1) {
                    //se reusa el primer borrado que se encontro
                    table[firstTombstone] = new Entry<>(key, value);
                } else {
                    table[index] = new Entry<>(key, value);
                    occupiedSlots++;
                }
                size++;
                return null;
            }
            if (slot == TOMBSTONE) {
                if (firstTombstone == -1) {
                    firstTombstone = index;
                }
                continue;
            }
            if (slot.getKey().equals(key)) {
                V old = slot.getValue();
                slot.setValue(value);
                return old;
            }
        }
        if (firstTombstone != -1) {
            table[firstTombstone] = new Entry<>(key, value);
            size++;
            return null;
        }
        throw CapacityExceededException.forCapacity(capacity);
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public V remove(K key) {
        Objects.requireNonNull(key, "La clave no puede ser null");
        return collisionStrategy.usesChaining() ? removeChaining(key) : removeOpenAddressing(key);
    }

    private V removeChaining(K key) {
        int index = hashFunction.hash(key, capacity);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;
        while (current != null) {
            if (current.getKey().equals(key)) {
                if (prev == null) {
                    table[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                occupiedSlots--;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private V removeOpenAddressing(K key) {
        int baseIndex = hashFunction.hash(key, capacity);
        for (int attempt = 0; attempt < capacity; attempt++) {
            int index = normalize(collisionStrategy.probe(baseIndex, attempt, capacity));
            Entry<K, V> slot = table[index];
            if (slot == null) {
                return null;
            }
            if (slot != TOMBSTONE && slot.getKey().equals(key)) {
                table[index] = TOMBSTONE;
                size--;
                return slot.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (Entry<K, V> entry : entries()) {
            if (Objects.equals(entry.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> findEntry(K key) {
        if (key == null) {
            return null;
        }
        int baseIndex = hashFunction.hash(key, capacity);
        if (collisionStrategy.usesChaining()) {
            Entry<K, V> current = table[baseIndex];
            while (current != null) {
                if (current.getKey().equals(key)) {
                    return current;
                }
                current = current.getNext();
            }
            return null;
        }
        for (int attempt = 0; attempt < capacity; attempt++) {
            int index = normalize(collisionStrategy.probe(baseIndex, attempt, capacity));
            Entry<K, V> slot = table[index];
            if (slot == null) {
                return null;
            }
            if (slot != TOMBSTONE && slot.getKey().equals(key)) {
                return slot;
            }
        }
        return null;
    }

    @Override
    public Iterable<K> keys() {
        java.util.List<K> result = new java.util.ArrayList<>(size);
        for (Entry<K, V> entry : entries()) {
            result.add(entry.getKey());
        }
        return result;
    }

    @Override
    public Iterable<V> values() {
        java.util.List<V> result = new java.util.ArrayList<>(size);
        for (Entry<K, V> entry : entries()) {
            result.add(entry.getValue());
        }
        return result;
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        java.util.List<Entry<K, V>> result = new java.util.ArrayList<>(size);
        if (collisionStrategy.usesChaining()) {
            for (int i = 0; i < capacity; i++) {
                Entry<K, V> current = table[i];
                while (current != null) {
                    result.add(current);
                    current = current.getNext();
                }
            }
        } else {
            for (int i = 0; i < capacity; i++) {
                Entry<K, V> slot = table[i];
                if (slot != null && slot != TOMBSTONE) {
                    result.add(slot);
                }
            }
        }
        return result;
    }

    @Override
    public double loadFactor() {
        return (double) size / capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Entry[capacity];
        size = 0;
        occupiedSlots = 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        java.util.Iterator<Entry<K, V>> delegate = entries().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return delegate.next();
            }
        };
    }

    private void ensureCapacityForInsert() {
        int projectedOccupancy = collisionStrategy.usesChaining() ? (size + 1) : (occupiedSlots + 1);
        double projectedLoadFactor = (double) projectedOccupancy / capacity;
        if (projectedLoadFactor > loadFactorThreshold) {
            resize(capacity * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = capacity;
        CollisionStrategy strategy = collisionStrategy;

        this.table = new Entry[newCapacity];
        this.capacity = newCapacity;
        this.size = 0;
        this.occupiedSlots = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> current = oldTable[i];
            if (strategy.usesChaining()) {
                while (current != null) {
                    Entry<K, V> next = current.getNext();
                    current.setNext(null);
                    reinsert(current.getKey(), current.getValue());
                    current = next;
                }
            } else if (current != null && current != TOMBSTONE) {
                reinsert(current.getKey(), current.getValue());
            }
        }
    }

    private void reinsert(K key, V value) {
        if (collisionStrategy.usesChaining()) {
            putChaining(key, value);
        } else {
            putOpenAddressing(key, value);
        }
    }

    private int normalize(int index) {
        return Math.floorMod(index, capacity);
    }

    public int capacity() {
        return capacity;
    }

    public Entry<K, V> slot(int index) {
        Entry<K, V> e = table[index];
        return e == TOMBSTONE ? null : e;
    }

    public boolean isDeleted(int index) {
        return table[index] == TOMBSTONE;
    }

    public boolean usesChaining() {
        return collisionStrategy.usesChaining();
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "HashTable: (vacía)";
        }
        StringBuilder sb = new StringBuilder("HashTable [capacidad=").append(capacity)
                .append(", tamaño=").append(size)
                .append(", factorCarga=").append(String.format("%.2f", loadFactor()))
                .append("]\n");
        for (int i = 0; i < capacity; i++) {
            sb.append("  [").append(i).append("] ");
            if (collisionStrategy.usesChaining()) {
                Entry<K, V> current = table[i];
                if (current == null) {
                    sb.append("(vacío)");
                } else {
                    while (current != null) {
                        sb.append(current.getKey()).append('=').append(current.getValue());
                        current = current.getNext();
                        if (current != null) {
                            sb.append(" -> ");
                        }
                    }
                }
            } else {
                Entry<K, V> slot = table[i];
                if (slot == null) {
                    sb.append("(vacío)");
                } else if (slot == TOMBSTONE) {
                    sb.append("(eliminado)");
                } else {
                    sb.append(slot.getKey()).append('=').append(slot.getValue());
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
