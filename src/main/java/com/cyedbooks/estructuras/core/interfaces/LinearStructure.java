package com.cyedbooks.estructuras.core.interfaces;

public interface LinearStructure<T> extends SequentialCollection<T> {

    T peekFirst();

    T peekLast();
}
