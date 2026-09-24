package com.cyedbooks.estructuras.core.interfaces;

/**
 * Estrategia de comparación externa entre dos elementos de tipo {@code T}.
 * <p>
 * <b>Nota de diseño:</b> este tipo comparte nombre con {@code java.lang.Comparable}
 * pero tiene una forma distinta a propósito: en vez de exigir que cada
 * elemento sepa compararse consigo mismo ({@code a.compareTo(b)}), aquí la
 * lógica de comparación vive fuera de los elementos ({@code compare(a, b)}),
 * al estilo de {@code java.util.Comparator}. Esto permite que estructuras
 * como {@code PriorityQueue} u operaciones de búsqueda/orden reciban
 * criterios de comparación intercambiables (por ejemplo, ordenar los mismos
 * objetos por distintos campos) sin obligar a los elementos almacenados a
 * implementar {@code java.lang.Comparable}.
 * <p>
 * Al tener la misma forma funcional que {@code java.util.Comparator<T>}
 * (un método {@code (T, T) -> int}), cualquier instancia de esta interfaz
 * puede adaptarse trivialmente a un {@code Comparator} mediante referencia
 * de método: {@code Comparator<T> c = miComparable::compare;}
 *
 * @param <T> tipo de los elementos a comparar
 */
@FunctionalInterface
public interface Comparable<T> {

    /**
     * Compara dos elementos, devolviendo:
     * <ul>
     *   <li>un valor negativo si {@code a} precede a {@code b}</li>
     *   <li>cero si {@code a} y {@code b} son equivalentes en el orden</li>
     *   <li>un valor positivo si {@code a} sucede a {@code b}</li>
     * </ul>
     *
     * @param a primer elemento
     * @param b segundo elemento
     * @return resultado de la comparación, con la misma convención de signo
     *         que {@code java.util.Comparator#compare}
     */
    int compare(T a, T b);
}
