package com.cyedbooks.estructuras.core.exceptions;

/**
 * Se lanza al intentar leer o remover un elemento (por ejemplo,
 * {@code pop}, {@code dequeue}, {@code peek}, {@code removeFirst}) de una
 * estructura que no tiene elementos.
 * <p>
 * Es una excepción no verificada ({@link RuntimeException}): representa un
 * error de uso de la API (el llamador debería haber consultado
 * {@code isEmpty()} antes), no una condición externa recuperable.
 */
public class EmptyStructureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyStructureException() {
        super("La estructura está vacía.");
    }

    public EmptyStructureException(String message) {
        super(message);
    }

    /**
     * Construye la excepción indicando qué estructura y operación la
     * originaron, para mensajes de error más útiles.
     *
     * @param structureName nombre de la estructura (por ejemplo, "ArrayStack")
     * @param operation     operación que falló (por ejemplo, "pop")
     */
    public static EmptyStructureException forOperation(String structureName, String operation) {
        return new EmptyStructureException(
                "No se puede ejecutar '" + operation + "' sobre " + structureName + ": la estructura está vacía.");
    }
}
