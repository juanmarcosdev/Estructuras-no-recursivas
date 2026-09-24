package com.cyedbooks.estructuras.visualization;

import com.cyedbooks.estructuras.core.interfaces.Collection;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.core.interfaces.SequentialCollection;

/**
 * Implementación de {@link StructureVisualizer} para consola: si la
 * estructura implementa {@link Drawable}, delega en su propio
 * {@code draw()} (que conoce mejor que nadie cómo representarse); en caso
 * contrario, cae a un renderizado genérico basado en iterar la estructura
 * (si es una {@link SequentialCollection}) o en su {@code toString()}.
 */
public class ASCIIVisualizer implements StructureVisualizer {

    @Override
    public String visualize(Collection<?> structure) {
        if (structure == null) {
            return "(estructura nula)";
        }
        if (structure instanceof Drawable drawable) {
            return drawable.draw();
        }
        if (structure instanceof SequentialCollection<?> sequential) {
            return genericSequentialRender(sequential);
        }
        return structure.toString();
    }

    private String genericSequentialRender(SequentialCollection<?> structure) {
        if (structure.isEmpty()) {
            return "(vacío)";
        }
        StringBuilder sb = new StringBuilder("[ ");
        boolean first = true;
        for (Object element : structure) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(element);
            first = false;
        }
        sb.append(" ]");
        return sb.toString();
    }
}
