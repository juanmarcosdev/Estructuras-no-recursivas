package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.model.Node;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista circular simplemente enlazada: se mantiene solo la referencia a la
 * cola ({@code tail}), y {@code tail.getNext()} siempre apunta a la cabeza
 * (o a {@code null} lógico cuando la lista está vacía). Esto hace que tanto
 * {@code addFirst} como {@code addLast} sean O(1).
 * <p>
 * La iteración se detiene tras exactamente {@code size} pasos —nunca
 * comparando contra {@code null}— para no ciclar infinitamente sobre la
 * estructura circular.
 *
 * @param <T> tipo de los elementos almacenados
 */
public class CircularLinkedList<T> implements LinkedListInterface<T>, Drawable {

    private Node<T> tail; // tail.next == head
    private int size;

    @Override
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            tail = newNode;
            newNode.setNext(newNode);
        } else {
            newNode.setNext(tail.getNext());
            tail.setNext(newNode);
        }
        size++;
    }

    @Override
    public void addLast(T element) {
        addFirst(element);
        // La nueva cabeza pasa a ser la cola, para que el elemento recién
        // insertado quede al final del orden lógico.
        tail = tail.getNext();
    }

    @Override
    public void addAt(int index, T element) {
        checkInsertIndex(index);
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size) {
            addLast(element);
            return;
        }
        Node<T> prev = nodeAt(index - 1);
        Node<T> newNode = new Node<>(element, prev.getNext());
        prev.setNext(newNode);
        size++;
    }

    @Override
    public T removeFirst() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("CircularLinkedList", "removeFirst");
        }
        Node<T> head = tail.getNext();
        T data = head.getData();
        if (head == tail) {
            tail = null;
        } else {
            tail.setNext(head.getNext());
        }
        size--;
        return data;
    }

    @Override
    public T removeLast() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("CircularLinkedList", "removeLast");
        }
        if (tail.getNext() == tail) {
            T data = tail.getData();
            tail = null;
            size--;
            return data;
        }
        Node<T> current = tail.getNext();
        while (current.getNext() != tail) {
            current = current.getNext();
        }
        T data = tail.getData();
        current.setNext(tail.getNext());
        tail = current;
        size--;
        return data;
    }

    @Override
    public T removeAt(int index) {
        checkElementIndex(index);
        if (index == 0) {
            return removeFirst();
        }
        Node<T> prev = nodeAt(index - 1);
        Node<T> target = prev.getNext();
        prev.setNext(target.getNext());
        if (target == tail) {
            tail = prev;
        }
        size--;
        return target.getData();
    }

    @Override
    public boolean remove(T element) {
        if (tail == null) {
            return false;
        }
        Node<T> prev = tail;
        Node<T> current = tail.getNext();
        for (int i = 0; i < size; i++) {
            if (Objects.equals(current.getData(), element)) {
                if (current == tail) {
                    if (current.getNext() == current) {
                        tail = null;
                    } else {
                        prev.setNext(current.getNext());
                        tail = prev;
                    }
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                return true;
            }
            prev = current;
            current = current.getNext();
        }
        return false;
    }

    @Override
    public T get(int index) {
        checkElementIndex(index);
        return nodeAt(index).getData();
    }

    @Override
    public T set(int index, T element) {
        checkElementIndex(index);
        Node<T> node = nodeAt(index);
        T old = node.getData();
        node.setData(element);
        return old;
    }

    @Override
    public int indexOf(T element) {
        if (tail == null) {
            return -1;
        }
        Node<T> current = tail.getNext();
        for (int i = 0; i < size; i++) {
            if (Objects.equals(current.getData(), element)) {
                return i;
            }
            current = current.getNext();
        }
        return -1;
    }

    @Override
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    @Override
    public T peekFirst() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("CircularLinkedList", "peekFirst");
        }
        return tail.getNext().getData();
    }

    @Override
    public T peekLast() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("CircularLinkedList", "peekLast");
        }
        return tail.getData();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        if (tail != null) {
            Node<T> current = tail.getNext();
            for (int i = 0; i < size; i++) {
                result[i] = current.getData();
                current = current.getNext();
            }
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (tail != null) {
            // Rompe el ciclo explícitamente para facilitar la recolección
            // de basura y evitar que un iterador externo quede colgado.
            Node<T> current = tail.getNext();
            for (int i = 0; i < size; i++) {
                Node<T> next = current.getNext();
                current.setNext(null);
                current = next;
            }
        }
        tail = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = (tail == null) ? null : tail.getNext();
            private int visited = 0;

            @Override
            public boolean hasNext() {
                return visited < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.getData();
                current = current.getNext();
                visited++;
                return data;
            }
        };
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "CircularLinkedList: (vacío)";
        }
        StringBuilder sb = new StringBuilder();
        Node<T> current = tail.getNext();
        for (int i = 0; i < size; i++) {
            sb.append('[').append(current.getData()).append(']');
            if (i < size - 1) {
                sb.append(" -> ");
            }
            current = current.getNext();
        }
        sb.append(" -> (vuelve al inicio)");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }

    private Node<T> nodeAt(int index) {
        Node<T> current = tail.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice " + index + " fuera de rango [0, " + size + ")");
        }
    }

    private void checkInsertIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Índice de inserción " + index + " fuera de rango [0, " + size + "]");
        }
    }
}
