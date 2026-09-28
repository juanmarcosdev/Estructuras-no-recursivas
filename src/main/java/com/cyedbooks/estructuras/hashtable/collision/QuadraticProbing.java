package com.cyedbooks.estructuras.hashtable.collision;

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
