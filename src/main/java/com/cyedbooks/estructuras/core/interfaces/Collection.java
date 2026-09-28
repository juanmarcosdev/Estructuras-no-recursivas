package com.cyedbooks.estructuras.core.interfaces;

public interface Collection<T> {

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    void clear();
}
