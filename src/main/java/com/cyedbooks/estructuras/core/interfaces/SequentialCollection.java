package com.cyedbooks.estructuras.core.interfaces;

public interface SequentialCollection<T> extends Collection<T>, Iterable<T> {

    boolean contains(T element);

    Object[] toArray();
}
