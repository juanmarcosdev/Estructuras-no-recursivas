package com.cyedbooks.estructuras.hashtable.collision;

/**
 * Direccionamiento abierto con sondeo cuadrático: {@code h(k, i) = (h'(k) + i²) mod m}.
 * <p>
 * Reduce el "clustering primario" del sondeo lineal a cambio de un patrón
 * de acceso menos amigable con la caché. {@code HashTable} mantiene el
 * factor de carga bajo (redimensionando de forma proactiva) para que, en la
 * práctica, esta secuencia encuentre un slot libre muy rápido incluso
 * aunque —a diferencia del sondeo lineal— no está garantizado
 * matemáticamente que cubra el 100% de los slots para cualquier capacidad.
 */
public class QuadraticProbing implements CollisionStrategy {

    @Override
    public int probe(int baseHash, int attempt, int capacity) {
        long offset = (long) attempt * attempt;
        return Math.floorMod(baseHash + offset, capacity);
    }

    @Override
    public boolean usesChaining() {
        return false;
    }
}
