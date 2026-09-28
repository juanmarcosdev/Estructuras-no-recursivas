package com.cyedbooks.estructuras.visualization;

import com.cyedbooks.estructuras.core.interfaces.Collection;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.core.interfaces.SequentialCollection;

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
