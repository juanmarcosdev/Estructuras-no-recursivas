package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.model.Node;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedStack<T> implements StackInterface<T>, Drawable {

    private Node<T> top;
    private int size;

    @Override
    public void push(T element) {
        top = new Node<>(element, top);
        size++;
    }

    @Override
    public T pop() {
        if (top == null) {
            throw EmptyStructureException.forOperation("LinkedStack", "pop");
        }
        T data = top.getData();
        top = top.getNext();
        size--;
        return data;
    }

    @Override
    public T peek() {
        if (top == null) {
            throw EmptyStructureException.forOperation("LinkedStack", "peek");
        }
        return top.getData();
    }

    @Override
    public boolean contains(T element) {
        Node<T> current = top;
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
        Node<T> current = top;
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
        Node<T> current = top;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(null);
            current = next;
        }
        top = null;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = top;

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
            return "LinkedStack: (vacío)";
        }
        StringBuilder sb = new StringBuilder("LinkedStack (tope arriba):\n");
        Node<T> current = top;
        while (current != null) {
            sb.append("  | ").append(current.getData()).append(" |\n");
            current = current.getNext();
        }
        sb.append("  +---------+");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
