# Estructuras No Recursivas

Biblioteca educativa en Java 17 de estructuras de datos lineales y de
clave-valor —listas enlazadas, pilas, colas (incluida una cola de
prioridad) y tablas hash— implementadas con algoritmos **estrictamente
iterativos**: ningún método de esta biblioteca usa recursión, ni siquiera
internamente (recorridos, redimensionamiento de arreglos, `sift-up`/`sift-down`
del heap, rehashing, etc. son todos bucles `while`/`for`).

Construida con Maven, probada con JUnit 5, y organizada como una jerarquía
de interfaces (`Collection` → `SequentialCollection` → `LinearStructure`)
que todas las estructuras comparten, más patrones Strategy para las
funciones de hash y las estrategias de resolución de colisiones de la
tabla hash.

## Requisitos

- Java 17 o superior
- Maven 3.8+

## Compilar y probar

```bash
mvn compile        # compila las fuentes principales
mvn test           # corre toda la suite de JUnit 5
mvn package         # genera el .jar en target/
```

## Estructura del proyecto

```
src/main/java/com/cyedbooks/estructuras/
├── core/
│   ├── interfaces/   Collection, SequentialCollection, LinearStructure, Drawable, Comparable
│   └── exceptions/   EmptyStructureException, DuplicateKeyException, CapacityExceededException
├── model/            Node<T>, BiNode<T>, Entry<K,V>
├── linkedlist/        LinkedListInterface + SinglyLinkedList, DoublyLinkedList, CircularLinkedList
├── stack/             StackInterface + ArrayStack, LinkedStack
├── queue/              QueueInterface + ArrayQueue, LinkedQueue, PriorityQueue
├── hashtable/
│   ├── HashTable, HashTableInterface, HashFunction
│   ├── impl/          DivisionHash, MultiplicationHash, FNV1aHash
│   └── collision/     CollisionStrategy + LinearProbing, QuadraticProbing, ChainingStrategy
├── util/              Iterator, ArrayDynamics, ComplexityLogger
└── visualization/     StructureVisualizer + ASCIIVisualizer
```

Ver [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) para las decisiones de
diseño y [`docs/COMPLEXITY_ANALYSIS.md`](docs/COMPLEXITY_ANALYSIS.md) para
la tabla completa de complejidades.

## Guía de uso rápida

### Listas enlazadas

```java
LinkedListInterface<String> lista = new DoublyLinkedList<>();
lista.addLast("a");
lista.addFirst("z");
lista.addAt(1, "m");
for (String s : lista) {
    System.out.println(s);
}
```

Las tres implementaciones (`SinglyLinkedList`, `DoublyLinkedList`,
`CircularLinkedList`) comparten el mismo contrato `LinkedListInterface`, así
que se pueden intercambiar sin tocar el código cliente.

### Pilas

```java
StackInterface<Integer> pila = new ArrayStack<>(); // o new LinkedStack<>()
pila.push(1);
pila.push(2);
pila.pop();  // 2
```

### Colas

```java
QueueInterface<Integer> cola = new ArrayQueue<>(); // o new LinkedQueue<>()
cola.enqueue(1);
cola.enqueue(2);
cola.dequeue(); // 1

// Cola de prioridad (min-heap por defecto)
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.enqueue(5);
pq.enqueue(1);
pq.enqueue(3);
pq.dequeue(); // 1

// Con orden personalizado (max-heap)
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
```

### Tabla hash

```java
// Configuración por defecto: DivisionHash + encadenamiento separado, factor de carga 0.75
HashTable<String, Integer> tabla = new HashTable<>();
tabla.put("uno", 1);
tabla.put("dos", 2);
tabla.get("uno"); // 1

// Configuración explícita: multiplicación de Knuth + sondeo cuadrático
HashTable<String, Integer> abierta = new HashTable<>(
    16,
    new MultiplicationHash<>(),
    new QuadraticProbing()
);

for (Entry<String, Integer> entrada : tabla) {
    System.out.println(entrada.getKey() + " -> " + entrada.getValue());
}
```

Los tres pares de función-hash/estrategia-de-colisión son intercambiables
entre sí: cualquier `HashFunction<K>` funciona con cualquier
`CollisionStrategy`, gracias al patrón Strategy usado en `HashTable`.

### Visualización ASCII

```java
StructureVisualizer visualizador = new ASCIIVisualizer();
visualizador.print("Pila tras 3 push", pila);
```

Cualquier estructura que implemente `Drawable` (todas las de esta
biblioteca lo hacen) se dibuja con su propio formato ASCII; el resto cae a
un renderizado genérico basado en iteración.

## Manejo de errores

Todas las operaciones que requieren una estructura no vacía (`pop`,
`dequeue`, `removeFirst`, `peek`, etc.) lanzan
`EmptyStructureException` (no verificada) si se invocan sobre una
estructura vacía. Los accesos por índice fuera de rango lanzan
`IndexOutOfBoundsException` estándar de Java.
