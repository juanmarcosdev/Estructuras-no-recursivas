package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

public interface QueueInterface<T> extends LinearStructure<T> {

    void enqueue(T element);

    T dequeue();

    T front();

    T rear();

    @Override
    default T peekFirst() {
        return front();
    }

    @Override
    default T peekLast() {
        return rear();
    }
}
