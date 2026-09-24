package com.cyedbooks.estructuras.model;

/**
 * Nodo genérico de doble dirección ({@code prev} / {@code next}), usado por
 * {@code DoublyLinkedList} para permitir recorrido y remoción en O(1) desde
 * ambos extremos sin necesidad de recorrer la lista.
 *
 * @param <T> tipo del valor almacenado en el nodo
 */
public class BiNode<T> {

    private T data;
    private BiNode<T> prev;
    private BiNode<T> next;

    public BiNode(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BiNode<T> getPrev() {
        return prev;
    }

    public void setPrev(BiNode<T> prev) {
        this.prev = prev;
    }

    public BiNode<T> getNext() {
        return next;
    }

    public void setNext(BiNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "BiNode[" + data + "]";
    }
}
