package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

/**
 * Contrato LIFO (last-in, first-out) implementado por {@code ArrayStack} y
 * {@code LinkedStack}.
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface StackInterface<T> extends LinearStructure<T> {

    /**
     * Apila {@code element} en el tope. O(1) amortizado.
     */
    void push(T element);

    /**
     * Remueve y devuelve el elemento del tope.
     *
     * @throws EmptyStructureException si la pila está vacía
     */
    T pop();

    /**
     * Devuelve el elemento del tope sin removerlo.
     *
     * @throws EmptyStructureException si la pila está vacía
     */
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
