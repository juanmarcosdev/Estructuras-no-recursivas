package com.cyedbooks.estructuras.stack;

import com.cyedbooks.estructuras.core.exceptions.EmptyStructureException;
import com.cyedbooks.estructuras.core.interfaces.Drawable;
import com.cyedbooks.estructuras.util.ArrayDynamics;
import com.cyedbooks.estructuras.util.Iterator;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Pila respaldada por un arreglo dinámico. El tope de la pila es siempre el
 * último elemento ocupado del arreglo, lo que hace que {@code push}/{@code pop}
 * sean O(1) amortizado (el costo de redimensionar se reparte entre muchas
 * operaciones).
 *
 * @param <T> tipo de los elementos almacenados
 */
public class ArrayStack<T> implements StackInterface<T>, Drawable {

    private Object[] elements;
    private int size;

    public ArrayStack() {
        this(ArrayDynamics.DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayStack(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("La capacidad inicial debe ser >= 1");
        }
        elements = new Object[initialCapacity];
    }

    @Override
    public void push(T element) {
        if (ArrayDynamics.needsGrowth(size, elements.length)) {
            elements = ArrayDynamics.resize(elements, ArrayDynamics.growCapacity(elements.length));
        }
        elements[size++] = element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("ArrayStack", "pop");
        }
        T value = (T) elements[size - 1];
        elements[--size] = null; // evita fugas de memoria (referencia colgante)
        if (ArrayDynamics.needsShrink(size, elements.length)) {
            elements = ArrayDynamics.resize(elements, ArrayDynamics.shrinkCapacity(elements.length));
        }
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) {
            throw EmptyStructureException.forOperation("ArrayStack", "peek");
        }
        return (T) elements[size - 1];
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
        // Orden: tope primero, base al final (orden de desapilado).
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[size - 1 - i];
        }
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
        elements = new Object[ArrayDynamics.DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[index--];
            }
        };
    }

    @Override
    public String draw() {
        if (isEmpty()) {
            return "ArrayStack: (vacío)";
        }
        StringBuilder sb = new StringBuilder("ArrayStack (tope arriba):\n");
        for (int i = size - 1; i >= 0; i--) {
            sb.append("  | ").append(elements[i]).append(" |\n");
        }
        sb.append("  +---------+");
        return sb.toString();
    }

    @Override
    public String toString() {
        return draw();
    }
}
