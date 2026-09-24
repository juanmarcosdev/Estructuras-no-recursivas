package com.cyedbooks.estructuras.hashtable.collision;

/**
 * Estrategia de resolución de colisiones para {@code HashTable}.
 * <p>
 * Cubre tanto <b>direccionamiento abierto</b> (donde cada colisión sondea
 * un índice alternativo dentro del mismo arreglo, vía {@link #probe}) como
 * <b>encadenamiento separado</b> (donde cada bucket es en sí una lista de
 * entradas y no se necesita sondeo, ver {@link #usesChaining()}).
 */
public interface CollisionStrategy {

    /**
     * Calcula el índice candidato número {@code attempt} (empezando en 0)
     * de la secuencia de sondeo para una clave cuyo hash base es
     * {@code baseHash}.
     * <p>
     * Para estrategias de encadenamiento este método no se usa para
     * localizar colisiones (el propio bucket es la lista), pero se
     * implementa igualmente devolviendo el índice base, por completitud
     * de la interfaz.
     *
     * @param baseHash índice base calculado por la {@code HashFunction}
     * @param attempt  número de intento, {@code 0} para el primer sondeo
     * @param capacity capacidad actual de la tabla
     * @return índice en {@code [0, capacity)} a probar
     */
    int probe(int baseHash, int attempt, int capacity);

    /**
     * @return {@code true} si esta estrategia resuelve colisiones
     *         encadenando entradas en el mismo bucket, en lugar de sondear
     *         otros índices del arreglo.
     */
    boolean usesChaining();
}
