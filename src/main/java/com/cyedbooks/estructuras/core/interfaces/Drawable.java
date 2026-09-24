package com.cyedbooks.estructuras.core.interfaces;

/**
 * Contrato opcional para estructuras capaces de producir su propia
 * representación textual "dibujada" (por ejemplo, arte ASCII).
 * <p>
 * {@code com.cyedbooks.estructuras.visualization.ASCIIVisualizer} usa este
 * contrato cuando está disponible en lugar de su renderizado genérico
 * basado en iteración, permitiendo que cada estructura decida cómo
 * representarse mejor (una lista como cadena de nodos, una pila como torre
 * vertical, una tabla hash como buckets, etc.).
 */
public interface Drawable {

    /**
     * @return una representación textual (ASCII) de la estructura, lista
     *         para imprimirse en consola. Nunca debe devolver {@code null};
     *         una estructura vacía devuelve una representación que lo deje
     *         explícito (por ejemplo {@code "(vacío)"}).
     */
    String draw();
}
