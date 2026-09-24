package com.cyedbooks.estructuras.util;

import java.util.Arrays;

/**
 * Funciones auxiliares, puramente iterativas, para la gestión de arreglos
 * dinámicos usados como almacenamiento interno de {@code ArrayStack},
 * {@code ArrayQueue}, {@code PriorityQueue} y {@code HashTable}.
 * <p>
 * Centralizar aquí la política de crecimiento/reducción evita duplicar la
 * lógica (y sus posibles errores) en cada estructura basada en arreglo.
 */
public final class ArrayDynamics {

    /** Capacidad inicial por defecto para un arreglo dinámico recién creado. */
    public static final int DEFAULT_INITIAL_CAPACITY = 8;

    /** Factor de crecimiento aplicado cuando el arreglo se llena. */
    public static final int GROWTH_FACTOR = 2;

    /** Fracción de ocupación por debajo de la cual conviene reducir el arreglo. */
    public static final double SHRINK_THRESHOLD = 0.25;

    private ArrayDynamics() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * @param size     cantidad de elementos actualmente almacenados
     * @param capacity capacidad actual del arreglo
     * @return {@code true} si, tras insertar un elemento más, el arreglo
     *         necesitaría crecer
     */
    public static boolean needsGrowth(int size, int capacity) {
        return size >= capacity;
    }

    /**
     * @param size     cantidad de elementos actualmente almacenados
     * @param capacity capacidad actual del arreglo
     * @return {@code true} si conviene reducir el arreglo para no
     *         desperdiciar memoria (nunca por debajo de
     *         {@link #DEFAULT_INITIAL_CAPACITY})
     */
    public static boolean needsShrink(int size, int capacity) {
        return capacity > DEFAULT_INITIAL_CAPACITY && size <= capacity * SHRINK_THRESHOLD;
    }

    /**
     * @param capacity capacidad actual
     * @return la próxima capacidad a usar al crecer el arreglo
     */
    public static int growCapacity(int capacity) {
        int base = Math.max(capacity, 1);
        return base * GROWTH_FACTOR;
    }

    /**
     * @param capacity capacidad actual
     * @return la próxima capacidad a usar al reducir el arreglo, nunca
     *         menor que {@link #DEFAULT_INITIAL_CAPACITY}
     */
    public static int shrinkCapacity(int capacity) {
        return Math.max(DEFAULT_INITIAL_CAPACITY, capacity / GROWTH_FACTOR);
    }

    /**
     * Copia {@code source} a un nuevo arreglo de tamaño {@code newCapacity},
     * de forma iterativa (delegando en {@link Arrays#copyOf}, que internamente
     * usa {@code System.arraycopy}, una operación nativa O(n) sin recursión).
     *
     * @param source      arreglo de origen
     * @param newCapacity nueva capacidad deseada
     * @param <T>         tipo de los elementos
     * @return nuevo arreglo con el contenido de {@code source}
     */
    public static <T> T[] resize(T[] source, int newCapacity) {
        return Arrays.copyOf(source, newCapacity);
    }

    /**
     * Reordena de forma iterativa el contenido de un buffer circular
     * (usado por {@code ArrayQueue}) hacia un nuevo arreglo de mayor
     * tamaño, dejando el elemento lógicamente en la posición 0 y
     * preservando el orden front→rear.
     *
     * @param source arreglo circular de origen
     * @param front  índice del frente lógico dentro de {@code source}
     * @param size   cantidad de elementos lógicos almacenados
     * @param newCapacity nueva capacidad deseada (debe ser {@code >= size})
     * @param <T>    tipo de los elementos
     * @return nuevo arreglo linealizado, de tamaño {@code newCapacity}
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] linearizeCircular(Object[] source, int front, int size, int newCapacity) {
        Object[] result = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            result[i] = source[(front + i) % source.length];
        }
        return (T[]) result;
    }
}
