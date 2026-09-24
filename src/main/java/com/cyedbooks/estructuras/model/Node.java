package com.cyedbooks.estructuras.model;

/**
 * Nodo genérico de una sola dirección, usado por las listas simplemente
 * enlazadas, la lista circular, las pilas basadas en enlaces y las colas
 * basadas en enlaces.
 * <p>
 * Es intencionalmente una clase simple (sin encapsulamiento estricto de sus
 * campos) para que las estructuras del paquete puedan manipular el enlace
 * {@code next} directamente en sus algoritmos iterativos, evitando la
 * sobrecarga de getters/setters en el camino caliente de operaciones O(1).
 *
 * @param <T> tipo del valor almacenado en el nodo
 */
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node[" + data + "]";
    }
}
