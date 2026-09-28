package com.cyedbooks.estructuras.hashtable.collision;

public interface CollisionStrategy {

    int probe(int baseHash, int attempt, int capacity);

    boolean usesChaining();
}
