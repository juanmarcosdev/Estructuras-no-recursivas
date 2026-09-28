package com.cyedbooks.estructuras.hashtable.collision;

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
