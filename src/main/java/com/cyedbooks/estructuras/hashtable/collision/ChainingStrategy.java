package com.cyedbooks.estructuras.hashtable.collision;

/**
 * Encadenamiento separado (separate chaining): cada slot de la tabla es la
 * cabeza de una lista enlazada de {@code Entry}, y las colisiones
 * simplemente se agregan a esa lista en lugar de sondear otros índices.
 * <p>
 * {@link #probe} nunca se invoca en el camino de colisión real de
 * {@code HashTable} para esta estrategia (se usa directamente el índice
 * base como bucket), pero se implementa devolviendo dicho índice base por
 * completitud y para facilitar pruebas unitarias de la estrategia en
 * aislamiento.
 */
public class ChainingStrategy implements CollisionStrategy {

    @Override
    public int probe(int baseHash, int attempt, int capacity) {
        return Math.floorMod(baseHash, capacity);
    }

    @Override
    public boolean usesChaining() {
        return true;
    }
}
