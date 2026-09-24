package com.cyedbooks.estructuras.core.exceptions;

/**
 * Se lanza cuando una operación requiere que una clave sea única dentro de
 * una estructura (típicamente una tabla hash) y la clave provista ya existe.
 * <p>
 * Nota: {@code HashTable.put} por defecto actualiza el valor de una clave
 * existente en lugar de lanzar esta excepción; queda disponible para
 * operaciones explícitas de inserción estricta (por ejemplo,
 * {@code HashTable.putIfAbsent} o {@code insertStrict}) que sí deben
 * rechazar duplicados.
 */
public class DuplicateKeyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateKeyException(Object key) {
        super("La clave ya existe en la estructura: " + key);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
