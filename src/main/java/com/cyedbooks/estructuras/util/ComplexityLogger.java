package com.cyedbooks.estructuras.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utilidad ligera para anotar y, opcionalmente, registrar en consola la
 * complejidad temporal (Big-O) de las operaciones de la biblioteca.
 * <p>
 * Su propósito es didáctico: permite a quien estudia el código consultar,
 * en tiempo de ejecución, la complejidad declarada de la operación que se
 * acaba de invocar (ver {@code docs/COMPLEXITY_ANALYSIS.md} para la tabla
 * completa por estructura). El logging está <b>desactivado por defecto</b>
 * para no interferir con la salida de los tests ni de aplicaciones que usen
 * la biblioteca; se activa explícitamente con {@link #setEnabled(boolean)}.
 */
public final class ComplexityLogger {

    private static final AtomicBoolean ENABLED = new AtomicBoolean(false);

    private ComplexityLogger() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * Activa o desactiva el registro por consola de las llamadas a
     * {@link #log(String, String, String)}.
     *
     * @param enabled {@code true} para activar el logging
     */
    public static void setEnabled(boolean enabled) {
        ENABLED.set(enabled);
    }

    /**
     * @return si el logging de complejidad está actualmente activo
     */
    public static boolean isEnabled() {
        return ENABLED.get();
    }

    /**
     * Registra, si el logging está activo, la complejidad de una operación.
     *
     * @param structureName nombre de la estructura (por ejemplo, "SinglyLinkedList")
     * @param operation     nombre de la operación (por ejemplo, "addFirst")
     * @param timeComplexity complejidad temporal en notación Big-O (por ejemplo, "O(1)")
     */
    public static void log(String structureName, String operation, String timeComplexity) {
        if (ENABLED.get()) {
            System.out.printf("[complejidad] %s.%s -> %s%n", structureName, operation, timeComplexity);
        }
    }
}
