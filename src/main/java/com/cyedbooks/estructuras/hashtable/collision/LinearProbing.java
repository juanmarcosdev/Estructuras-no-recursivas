package com.cyedbooks.estructuras.hashtable.collision;

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
