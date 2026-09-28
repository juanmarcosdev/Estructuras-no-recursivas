package com.cyedbooks.estructuras.core.exceptions;

public class DuplicateKeyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateKeyException(Object key) {
        super("La clave ya existe en la estructura: " + key);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
