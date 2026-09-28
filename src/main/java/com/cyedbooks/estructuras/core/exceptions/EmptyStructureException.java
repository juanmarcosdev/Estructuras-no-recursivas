package com.cyedbooks.estructuras.core.exceptions;

public class EmptyStructureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyStructureException() {
        super("La estructura está vacía.");
    }

    public EmptyStructureException(String message) {
        super(message);
    }

    public static EmptyStructureException forOperation(String structureName, String operation) {
        return new EmptyStructureException(
                "No se puede ejecutar '" + operation + "' sobre " + structureName + ": la estructura está vacía.");
    }
}
