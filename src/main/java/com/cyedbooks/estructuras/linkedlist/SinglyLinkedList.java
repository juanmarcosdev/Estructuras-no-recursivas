package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.model.Node;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SinglyLinkedList<T> implements LinkedListInterface<T>, Drawable {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    @Override
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element, head);
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    @Override
    public void addLast(T element) {
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
        if (head == null) {
            throw EmptyStructureException.forOperation("SinglyLinkedList", "removeFirst");
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
    public T removeLast() {
        if (head == null) {
            throw EmptyStructureException.forOperation("SinglyLinkedList", "removeLast");
        }
        if (head == tail) {
            T data = head.getData();
            head = null;
            tail = null;
            size--;
            return data;
        }
        Node<T> current = head;
        while (current.getNext() != tail) {
            current = current.getNext();
        }
        T data = tail.getData();
        current.setNext(null);
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
        Node<T> current = head;
        Node<T> prev = null;
        while (current != null) {
            if (Objects.equals(current.getData(), element)) {
                if (prev == null) {
                    head = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                if (current == tail) {
                    tail = prev;
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
        int index = 0;
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(current.getData(), element)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    @Override
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }

    @Override
    public T peekFirst() {
        if (head == null) {
            throw EmptyStructureException.forOperation("SinglyLinkedList", "peekFirst");
        }
        return head.getData();
    }

    @Override
    public T peekLast() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("SinglyLinkedList", "peekLast");
        }
        return tail.getData();
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
            return "SinglyLinkedList: (vacío)";
        }
        StringBuilder sb = new StringBuilder("head -> ");
        Node<T> current = head;
        while (current != null) {
            sb.append('[').append(current.getData()).append(']');
            if (current.getNext() != null) {
                sb.append(" -> ");
            }
            current = current.getNext();
        }
        sb.append(" -> null");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }

    private Node<T> nodeAt(int index) {
        Node<T> current = head;
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
