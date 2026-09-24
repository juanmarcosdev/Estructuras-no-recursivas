package com.cyedbooks.estructuras.core.exceptions;

/**
 * Se lanza cuando una estructura de capacidad fija (o temporalmente
 * incapaz de redimensionarse) no puede admitir un nuevo elemento.
 * <p>
 * En esta biblioteca la mayoría de las estructuras basadas en arreglo se
 * redimensionan automáticamente y por lo tanto no deberían lanzar esta
 * excepción en uso normal; existe para los casos explícitamente configurados
 * con capacidad máxima fija, o para direccionamiento abierto en tablas hash
 * cuando, por un error de implementación de la estrategia de colisión, no
 * se encuentra un slot libre.
 */
public class CapacityExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CapacityExceededException() {
        super("Se alcanzó la capacidad máxima de la estructura.");
    }

    public CapacityExceededException(String message) {
        super(message);
    }

    public static CapacityExceededException forCapacity(int capacity) {
        return new CapacityExceededException("Capacidad máxima alcanzada (" + capacity + " elementos).");
    }
}
