package com.cyedbooks.estructuras.core.interfaces;

/**
 * Interfaz raíz de toda la jerarquía de estructuras de datos del proyecto.
 * <p>
 * Define el contrato mínimo que cualquier estructura de datos —lineal o no—
 * debe cumplir: conocer su tamaño, saber si está vacía y poder vaciarse.
 * Todas las implementaciones de esta biblioteca garantizan que estas
 * operaciones se resuelven en tiempo O(1) y sin recursión.
 *
 * @param <T> tipo de los elementos almacenados (o, en el caso de estructuras
 *            clave-valor, tipo de la entrada almacenada)
 */
public interface Collection<T> {

    /**
     * @return la cantidad de elementos actualmente almacenados. O(1).
     */
    int size();

    /**
     * @return {@code true} si la estructura no contiene elementos. O(1).
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Elimina todos los elementos de la estructura, dejándola vacía.
     * Debe implementarse de forma iterativa (no recursiva).
     */
    void clear();
}
