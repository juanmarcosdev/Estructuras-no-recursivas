package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

/**
 * Contrato FIFO (first-in, first-out) implementado por {@code ArrayQueue},
 * {@code LinkedQueue} y, con semántica de prioridad en vez de orden de
 * llegada, {@code PriorityQueue}.
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface QueueInterface<T> extends LinearStructure<T> {

    /**
     * Encola {@code element} al final. O(1) amortizado.
     */
    void enqueue(T element);

    /**
     * Remueve y devuelve el elemento al frente de la cola.
     *
     * @throws EmptyStructureException si la cola está vacía
     */
    T dequeue();

    /**
     * Devuelve el elemento al frente de la cola sin removerlo.
     *
     * @throws EmptyStructureException si la cola está vacía
     */
    T front();

    /**
     * Devuelve el elemento al final (rear) de la cola sin removerlo.
     * <p>
     * En {@code PriorityQueue} no existe un "final" con significado propio
     * (el orden lo determina la prioridad, no la llegada), por lo que esa
     * implementación puede lanzar {@code UnsupportedOperationException}.
     *
     * @throws EmptyStructureException si la cola está vacía
     */
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
