package com.cyedbooks.estructuras.hashtable.collision;

/**
 * Direccionamiento abierto con sondeo lineal: {@code h(k, i) = (h'(k) + i) mod m}.
 * <p>
 * Es la estrategia de sondeo más simple y con mejor localidad de caché,
 * pero es propensa a "clustering primario" (agrupamientos largos de slots
 * ocupados consecutivos) cuando el factor de carga crece.
 */
public class LinearProbing implements CollisionStrategy {

    @Override
    public int probe(int baseHash, int attempt, int capacity) {
        return Math.floorMod(baseHash + attempt, capacity);
    }

    @Override
    public boolean usesChaining() {
        return false;
    }
}
