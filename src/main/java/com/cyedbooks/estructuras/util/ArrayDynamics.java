package com.cyedbooks.estructuras.util;

import java.util.Arrays;

public final class ArrayDynamics {

    public static final int DEFAULT_INITIAL_CAPACITY = 8;

    public static final int GROWTH_FACTOR = 2;

    public static final double SHRINK_THRESHOLD = 0.25;

    private ArrayDynamics() {
    }

    public static boolean needsGrowth(int size, int capacity) {
        return size >= capacity;
    }

    public static boolean needsShrink(int size, int capacity) {
        return capacity > DEFAULT_INITIAL_CAPACITY && size <= capacity * SHRINK_THRESHOLD;
    }

    public static int growCapacity(int capacity) {
        int base = Math.max(capacity, 1);
        return base * GROWTH_FACTOR;
    }

    public static int shrinkCapacity(int capacity) {
        return Math.max(DEFAULT_INITIAL_CAPACITY, capacity / GROWTH_FACTOR);
    }

    public static <T> T[] resize(T[] source, int newCapacity) {
        return Arrays.copyOf(source, newCapacity);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] linearizeCircular(Object[] source, int front, int size, int newCapacity) {
        Object[] result = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            result[i] = source[(front + i) % source.length];
        }
        return (T[]) result;
    }
}
