package com.cyedbooks.estructuras.model;

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
