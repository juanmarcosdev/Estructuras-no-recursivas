package com.cyedbooks.estructuras.core.exceptions;

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
