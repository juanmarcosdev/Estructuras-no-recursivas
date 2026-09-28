package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

public interface LinkedListInterface<T> extends LinearStructure<T> {

    void addFirst(T element);

    void addLast(T element);

    void addAt(int index, T element);

    T removeFirst();

    T removeLast();

    T removeAt(int index);

    boolean remove(T element);

    T get(int index);

    T set(int index, T element);

    int indexOf(T element);
}
