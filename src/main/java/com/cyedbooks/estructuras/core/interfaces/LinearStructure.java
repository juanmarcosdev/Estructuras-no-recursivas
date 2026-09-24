package com.cyedbooks.estructuras.core.interfaces;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;

/**
 * Base común para las estructuras lineales de la biblioteca: listas
 * enlazadas, pilas y colas. Una estructura lineal es aquella en la que cada
 * elemento (salvo el primero y el último) tiene exactamente un predecesor y
 * un sucesor dentro del orden de la estructura.
 * <p>
 * Aporta operaciones de acceso a los extremos, comunes a todas las
 * estructuras lineales, con un contrato de excepciones uniforme: acceder a
 * un extremo de una estructura vacía lanza {@link EmptyStructureException}.
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface LinearStructure<T> extends SequentialCollection<T> {

    /**
     * @return el primer elemento de la estructura, sin removerlo
     * @throws EmptyStructureException si la estructura está vacía
     */
    T peekFirst();

    /**
     * @return el último elemento de la estructura, sin removerlo
     * @throws EmptyStructureException si la estructura está vacía
     */
    T peekLast();
}
