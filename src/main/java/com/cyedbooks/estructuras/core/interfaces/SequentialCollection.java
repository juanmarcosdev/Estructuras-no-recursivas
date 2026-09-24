package com.cyedbooks.estructuras.core.interfaces;

/**
 * Estructuras cuyos elementos pueden recorrerse en un orden bien definido.
 * <p>
 * Extiende {@link Collection} añadiendo la capacidad de iterar sobre los
 * elementos con un {@code for-each} estándar de Java (gracias a
 * {@link Iterable}), volcarlos a un arreglo y consultar pertenencia.
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface SequentialCollection<T> extends Collection<T>, Iterable<T> {

    /**
     * Indica si {@code element} está presente en la estructura, usando
     * {@link Object#equals(Object)} para la comparación.
     * <p>
     * Complejidad: O(n) para la mayoría de las implementaciones, recorriendo
     * la estructura de forma iterativa.
     *
     * @param element elemento a buscar (puede ser {@code null} si la
     *                implementación lo permite)
     * @return {@code true} si algún elemento almacenado es igual a
     *         {@code element}
     */
    boolean contains(T element);

    /**
     * Vuelca los elementos de la estructura a un nuevo arreglo, en el mismo
     * orden en que los recorrería el iterador.
     *
     * @return arreglo nuevo (independiente de la estructura interna) con
     *         todos los elementos
     */
    Object[] toArray();
}
