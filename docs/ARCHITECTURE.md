# Diseño

La idea del proyecto es no usar recursion en ninguna parte. Recorridos, inserciones, el heap de la cola de prioridad y el rehash de la tabla hash se hacen con ciclos. Asi no hay riesgo de `StackOverflowError` con estructuras grandes.

## Interfaces

```
Collection<T>                  size, isEmpty, clear
  SequentialCollection<T>      + contains, toArray, iterable
    LinearStructure<T>         + peekFirst, peekLast
      LinkedListInterface<T>
      StackInterface<T>
      QueueInterface<T>

HashTableInterface<K, V>       extiende Collection, no LinearStructure
```

La tabla hash no extiende `LinearStructure` porque sus elementos no tienen un orden.

`core.interfaces.Comparable` se usa como comparador externo (`compare(a, b)`), parecido a `Comparator`. Sirve para pasarle un orden a `PriorityQueue` sin que los elementos implementen `java.lang.Comparable`.

`util.Iterator` extiende `java.util.Iterator` sin agregar nada, para que funcione con el for-each.

## Listas

| | addLast | removeLast | get |
|---|---|---|---|
| Simple | O(1) | O(n) | O(n) |
| Doble | O(1) | O(1) | O(n), empieza por el extremo mas cercano |
| Circular | O(1) | O(n) | O(n) |

La circular solo guarda `tail`, y `tail.next` es la cabeza.

## Pilas y colas

Las versiones con arreglo crecen al doble cuando se llenan y se reducen a la mitad cuando quedan al 25%. `ArrayQueue` usa el arreglo como buffer circular, entonces `dequeue` no mueve elementos.

Las versiones enlazadas son O(1) siempre pero usan un nodo por elemento.

## Cola de prioridad

Es un min-heap sobre un arreglo. Se le puede pasar un `Comparator`, un `Comparable` del proyecto o nada (usa el orden natural). El iterador recorre el arreglo en orden de heap, no ordenado.

## Tabla hash

Recibe una funcion hash y una estrategia de colisiones, y se pueden combinar como sea.

- Encadenamiento: cada posicion es una lista de `Entry`. Factor de carga por defecto 0.75.
- Sondeo lineal y cuadratico: una entrada por posicion. Al borrar se deja un tombstone para no cortar la busqueda. Factor de carga por defecto 0.5.

Cuando se pasa del factor de carga la tabla duplica su tamaño y reinserta todo. Ahi se eliminan los tombstones.

Pendiente: la capacidad siempre es potencia de 2. Eso no le conviene al metodo de division, que funciona mejor con primos. Tambien hace que el sondeo cuadratico no pase por todas las posiciones, y puede lanzar `CapacityExceededException` aunque la tabla no este llena.

## Excepciones

Todas son `RuntimeException`.

- `EmptyStructureException`: operar sobre una estructura vacia
- `CapacityExceededException`: el sondeo no encontro espacio
- `DuplicateKeyException`: no se usa por ahora, `put` reemplaza el valor
