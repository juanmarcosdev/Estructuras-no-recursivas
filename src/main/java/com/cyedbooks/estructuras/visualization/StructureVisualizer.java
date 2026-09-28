package com.cyedbooks.estructuras.visualization;

import com.cyedbooks.estructuras.core.interfaces.Collection;

public interface StructureVisualizer {

    String visualize(Collection<?> structure);

    default void print(String title, Collection<?> structure) {
        System.out.println("=== " + title + " ===");
        System.out.println(visualize(structure));
    }
}
