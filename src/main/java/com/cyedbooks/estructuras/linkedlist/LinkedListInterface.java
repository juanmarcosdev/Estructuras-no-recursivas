package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.LinearStructure;

/**
 * Contrato común a todas las variantes de lista enlazada de la biblioteca
 * ({@code SinglyLinkedList}, {@code DoublyLinkedList}, {@code CircularLinkedList}).
 * <p>
 * Todas las implementaciones garantizan que ninguna de estas operaciones
 * usa recursión: los recorridos se hacen con bucles {@code while}/{@code for}.
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface LinkedListInterface<T> extends LinearStructure<T> {

    /**
     * Inserta {@code element} al inicio de la lista. O(1).
     */
    void addFirst(T element);

    /**
     * Inserta {@code element} al final de la lista. O(1) si la
     * implementación mantiene referencia a la cola; O(n) en caso contrario.
     */
    void addLast(T element);

    /**
     * Inserta {@code element} en la posición {@code index}, desplazando
     * hacia la derecha los elementos siguientes. O(n).
     *
     * @throws IndexOutOfBoundsException si {@code index < 0 || index > size()}
     */
    void addAt(int index, T element);

    /**
     * Remueve y devuelve el primer elemento. O(1).
     *
     * @throws EmptyStructureException si la lista está vacía
     */
    T removeFirst();

    /**
     * Remueve y devuelve el último elemento. O(n) en lista simple/circular,
     * O(1) en lista doble.
     *
     * @throws EmptyStructureException si la lista está vacía
     */
    T removeLast();

    /**
     * Remueve y devuelve el elemento en la posición {@code index}. O(n).
     *
     * @throws IndexOutOfBoundsException si {@code index < 0 || index >= size()}
     */
    T removeAt(int index);

    /**
     * Remueve la primera ocurrencia de {@code element} (comparado con
     * {@link Object#equals(Object)}).
     *
     * @return {@code true} si se encontró y removió el elemento
     */
    boolean remove(T element);

    /**
     * @param index posición a consultar
     * @return el elemento almacenado en {@code index}
     * @throws IndexOutOfBoundsException si {@code index < 0 || index >= size()}
     */
    T get(int index);

    /**
     * Reemplaza el elemento en la posición {@code index}.
     *
     * @return el elemento previamente almacenado en esa posición
     * @throws IndexOutOfBoundsException si {@code index < 0 || index >= size()}
     */
    T set(int index, T element);

    /**
     * @param element elemento a buscar
     * @return el índice de la primera ocurrencia de {@code element}, o -1
     *         si no está presente
     */
    int indexOf(T element);
}
