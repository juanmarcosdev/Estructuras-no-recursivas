package com.cyedbooks.estructuras.linkedlist;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.model.BiNode;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

public class DoublyLinkedList<T> implements LinkedListInterface<T>, Drawable {

    private BiNode<T> head;
    private BiNode<T> tail;
    private int size;

    @Override
    public void addFirst(T element) {
        BiNode<T> newNode = new BiNode<>(element);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(T element) {
        BiNode<T> newNode = new BiNode<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPrev(tail);
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
        BiNode<T> current = nodeAt(index);
        BiNode<T> prev = current.getPrev();
        BiNode<T> newNode = new BiNode<>(element);
        newNode.setPrev(prev);
        newNode.setNext(current);
        prev.setNext(newNode);
        current.setPrev(newNode);
        size++;
    }

    @Override
    public T removeFirst() {
        if (head == null) {
            throw EmptyStructureException.forOperation("DoublyLinkedList", "removeFirst");
        }
        return unlink(head);
    }

    @Override
    public T removeLast() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("DoublyLinkedList", "removeLast");
        }
        return unlink(tail);
    }

    @Override
    public T removeAt(int index) {
        checkElementIndex(index);
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(T element) {
        BiNode<T> current = head;
        while (current != null) {
            if (Objects.equals(current.getData(), element)) {
                unlink(current);
                return true;
            }
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
        BiNode<T> node = nodeAt(index);
        T old = node.getData();
        node.setData(element);
        return old;
    }

    @Override
    public int indexOf(T element) {
        int index = 0;
        BiNode<T> current = head;
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
            throw EmptyStructureException.forOperation("DoublyLinkedList", "peekFirst");
        }
        return head.getData();
    }

    @Override
    public T peekLast() {
        if (tail == null) {
            throw EmptyStructureException.forOperation("DoublyLinkedList", "peekLast");
        }
        return tail.getData();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        BiNode<T> current = head;
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
        BiNode<T> current = head;
        while (current != null) {
            BiNode<T> next = current.getNext();
            current.setNext(null);
            current.setPrev(null);
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private BiNode<T> current = head;

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

    public Iterator<T> descendingIterator() {
        return new Iterator<>() {
            private BiNode<T> current = tail;

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
                current = current.getPrev();
                return data;
            }
        };
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "DoublyLinkedList: (vacío)";
        }
        StringBuilder sb = new StringBuilder("null <-> ");
        BiNode<T> current = head;
        while (current != null) {
            sb.append('[').append(current.getData()).append(']');
            if (current.getNext() != null) {
                sb.append(" <-> ");
            }
            current = current.getNext();
        }
        sb.append(" <-> null");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }

    private T unlink(BiNode<T> node) {
        BiNode<T> prev = node.getPrev();
        BiNode<T> next = node.getNext();
        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }
        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }
        node.setNext(null);
        node.setPrev(null);
        size--;
        return node.getData();
    }

    private BiNode<T> nodeAt(int index) {
        if (index <= size / 2) {
            BiNode<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current;
        }
        BiNode<T> current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.getPrev();
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
