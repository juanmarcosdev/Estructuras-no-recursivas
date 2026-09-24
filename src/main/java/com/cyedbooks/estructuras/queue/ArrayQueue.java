package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.util.ArrayDynamics;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Cola respaldada por un arreglo usado como <b>buffer circular</b>: se
 * mantienen los índices {@code front} y {@code rear} y ambos "envuelven"
 * (wrap-around) al llegar al final del arreglo mediante aritmética modular,
 * evitando el desplazamiento O(n) de elementos que tendría un arreglo lineal
 * ingenuo en cada {@code dequeue}.
 *
 * @param <T> tipo de los elementos almacenados
 */
public class ArrayQueue<T> implements QueueInterface<T>, Drawable {

    private Object[] elements;
    private int front;
    private int size;

    public ArrayQueue() {
        this(ArrayDynamics.DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayQueue(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("La capacidad inicial debe ser >= 1");
        }
        elements = new Object[initialCapacity];
    }

    @Override
    public void enqueue(T element) {
        if (ArrayDynamics.needsGrowth(size, elements.length)) {
            grow();
        }
        int insertIndex = (front + size) % elements.length;
        elements[insertIndex] = element;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("ArrayQueue", "dequeue");
        }
        T value = (T) elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;
        if (ArrayDynamics.needsShrink(size, elements.length)) {
            shrink();
        }
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("ArrayQueue", "front");
        }
        return (T) elements[front];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T rear() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("ArrayQueue", "rear");
        }
        int rearIndex = (front + size - 1) % elements.length;
        return (T) elements[rearIndex];
    }

    @Override
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[(front + i) % elements.length], element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        return ArrayDynamics.linearizeCircular(elements, front, size, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        elements = new Object[ArrayDynamics.DEFAULT_INITIAL_CAPACITY];
        front = 0;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int visited = 0;

            @Override
            public boolean hasNext() {
                return visited < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = (T) elements[(front + visited) % elements.length];
                visited++;
                return value;
            }
        };
    }

    private void grow() {
        int newCapacity = ArrayDynamics.growCapacity(elements.length);
        elements = ArrayDynamics.linearizeCircular(elements, front, size, newCapacity);
        front = 0;
    }

    private void shrink() {
        int newCapacity = ArrayDynamics.shrinkCapacity(elements.length);
        if (newCapacity < size) {
            return;
        }
        elements = ArrayDynamics.linearizeCircular(elements, front, size, newCapacity);
        front = 0;
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "ArrayQueue: (vacío)";
        }
        StringBuilder sb = new StringBuilder("front -> ");
        for (int i = 0; i < size; i++) {
            sb.append('[').append(elements[(front + i) % elements.length]).append(']');
            if (i < size - 1) {
                sb.append(" ");
            }
        }
        sb.append(" <- rear");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
