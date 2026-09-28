package com.cyedbooks.estructuras.queue;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.util.ArrayDynamics;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PriorityQueue<T> implements QueueInterface<T>, Drawable {

    private Object[] elements;
    private int size;
    private final Comparator<? super T> comparator;

    public PriorityQueue() {
        this(ArrayDynamics.DEFAULT_INITIAL_CAPACITY, null);
    }

    public PriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    public PriorityQueue(Comparator<? super T> comparator) {
        this(ArrayDynamics.DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public PriorityQueue(com.cyedbooks.estructuras.core.interfaces.Comparable<T> comparator) {
        this(ArrayDynamics.DEFAULT_INITIAL_CAPACITY, Objects.requireNonNull(comparator)::compare);
    }

    public PriorityQueue(int initialCapacity, Comparator<? super T> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("La capacidad inicial debe ser >= 1");
        }
        this.elements = new Object[initialCapacity];
        this.comparator = comparator;
    }

    @Override
    public void enqueue(T element) {
        if (ArrayDynamics.needsGrowth(size, elements.length)) {
            elements = ArrayDynamics.resize(elements, ArrayDynamics.growCapacity(elements.length));
        }
        elements[size] = element;
        siftUp(size);
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("PriorityQueue", "dequeue");
        }
        T top = (T) elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        if (ArrayDynamics.needsShrink(size, elements.length)) {
            elements = ArrayDynamics.resize(elements, ArrayDynamics.shrinkCapacity(elements.length));
        }
        return top;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("PriorityQueue", "front");
        }
        return (T) elements[0];
    }

    @Override
    public T rear() {
        throw new UnsupportedOperationException(
                "PriorityQueue no tiene un elemento 'final' con significado de prioridad; use front()/dequeue().");
    }

    @Override
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[index++];
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare((T) elements[index], (T) elements[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && compare((T) elements[left], (T) elements[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && compare((T) elements[right], (T) elements[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        //sin comparador se usa el orden natural
        return ((Comparable<? super T>) a).compareTo(b);
    }

    private void swap(int i, int j) {
        Object tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "PriorityQueue: (vacío)";
        }
        StringBuilder sb = new StringBuilder("PriorityQueue (orden de heap, raíz primero):\n  ");
        for (int i = 0; i < size; i++) {
            sb.append('[').append(elements[i]).append(']');
            if (i < size - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
