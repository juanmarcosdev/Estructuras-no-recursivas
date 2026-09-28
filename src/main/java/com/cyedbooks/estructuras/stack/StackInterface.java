package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

public interface StackInterface<T> extends LinearStructure<T> {

    void push(T element);

    T pop();

    T peek();

    @Override
    default T peekFirst() {
        return peek();
    }

    @Override
    default T peekLast() {
        return peek();
    }
}
