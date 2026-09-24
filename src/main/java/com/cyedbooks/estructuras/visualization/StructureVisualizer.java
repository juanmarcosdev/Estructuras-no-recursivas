package com.cyedbooks.estructuras.visualization;

import com.cyedbooks.estructuras.core.interfaces.Collection;

/**
 * Contrato para componentes capaces de producir una representación visual
 * (en el sentido amplio: puede ser texto ASCII, o en el futuro cualquier
 * otro formato) de cualquier estructura de la biblioteca.
 */
public interface StructureVisualizer {

    /**
     * @param structure la estructura a visualizar (cualquier {@code Collection},
     *                  incluida {@code HashTable})
     * @return una representación textual lista para mostrarse
     */
    String visualize(Collection<?> structure);

    /**
     * Visualiza la estructura y la imprime directamente en la salida
     * estándar, con un título identificador antes del contenido.
     *
     * @param title     título descriptivo (por ejemplo, "Pila tras 3 push")
     * @param structure estructura a visualizar
     */
    default void print(String title, Collection<?> structure) {
        System.out.println("=== " + title + " ===");
        System.out.println(visualize(structure));
    }
}
