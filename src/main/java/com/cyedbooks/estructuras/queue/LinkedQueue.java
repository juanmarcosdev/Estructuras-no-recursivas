package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.model.Node;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Cola respaldada por una lista enlazada de nodos simples, con referencias
 * a {@code head} (frente) y {@code tail} (final), lo que garantiza
 * {@code enqueue}/{@code dequeue} en O(1) estricto sin necesidad de
 * redimensionar nada.
 *
 * @param <T> tipo de los elementos almacenados
 */
public class LinkedQueue<T> implements QueueInterface<T>, Drawable {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    @Override
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    @Override
    public T dequeue() {
        if (head == null) {
            throw EmptyStructureException.forOperation("LinkedQueue", "dequeue");
        }
        T data = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    @Override
    public T front() {
        if (head == null) {
            throw EmptyStructureException.forOperation("LinkedQueue", "front");
        }
        return head.getData();
    }

    @Override
    public T rear() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("LinkedQueue", "rear");
        }
        return tail.getData();
    }

    @Override
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(current.getData(), element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            result[i++] = current.getData();
            current = current.getNext();
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(null);
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "LinkedQueue: (vacío)";
        }
        StringBuilder sb = new StringBuilder("front -> ");
        Node<T> current = head;
        while (current != null) {
            sb.append('[').append(current.getData()).append(']');
            if (current.getNext() != null) {
                sb.append(" ");
            }
            current = current.getNext();
        }
        sb.append(" <- rear");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
