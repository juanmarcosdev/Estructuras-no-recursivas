# Decisiones de arquitectura

## Objetivo del diseño

El nombre del proyecto —*estructuras no recursivas*— es también su
restricción central: cada algoritmo (recorridos, inserciones, remociones,
redimensionamiento de arreglos, `sift-up`/`sift-down` del heap binario,
rehashing de la tabla hash) está implementado con bucles `while`/`for`,
nunca con llamadas recursivas. Esto evita el riesgo de
`StackOverflowError` en estructuras grandes y hace explícito, en el propio
código, el costo real de cada operación.

## Jerarquía de interfaces

```
Collection<T>                         (size, isEmpty, clear)
  └── SequentialCollection<T>         (+ contains, toArray, Iterable<T>)
        └── LinearStructure<T>        (+ peekFirst, peekLast)
              ├── LinkedListInterface<T>   (listas: acceso por índice)
              ├── StackInterface<T>        (LIFO: push/pop/peek)
              └── QueueInterface<T>        (FIFO: enqueue/dequeue/front/rear)

Collection<Entry<K,V>> + Iterable<Entry<K,V>>
  └── HashTableInterface<K, V>        (put/get/remove/containsKey/keys/values/entries)
```

`HashTableInterface` no extiende `SequentialCollection`/`LinearStructure`
porque una tabla hash no tiene un orden lineal significativo entre sus
entradas; en cambio comparte con el resto de la jerarquía el contrato
mínimo de `Collection` (tratando cada par clave-valor como "el elemento"
que cuenta para `size()`).

Esta jerarquía permite escribir código genérico contra la interfaz más
abstracta que se necesite: un método que solo necesita saber el tamaño de
"lo que sea" recibe un `Collection<?>`; uno que necesita iterar recibe un
`SequentialCollection<T>`; uno que necesita `push`/`pop` recibe
específicamente un `StackInterface<T>`.

## Por qué `Comparable` propio en vez de `java.lang.Comparable`

`core.interfaces.Comparable<T>` comparte nombre con
`java.lang.Comparable` a propósito, pero tiene una forma distinta: en vez
de `a.compareTo(b)` (comparación "interna" al elemento), define
`compare(a, b)` (comparación "externa", al estilo `java.util.Comparator`).
Esto permite que `PriorityQueue` reciba un criterio de orden intercambiable
sin exigir que los elementos almacenados implementen
`java.lang.Comparable`. Al compartir la misma forma funcional que
`Comparator<T>`, se adapta trivialmente con una referencia de método
(`comparador::compare`).

Para evitar cualquier ambigüedad con `java.lang.Comparable` (implícitamente
importado en todo archivo Java), las clases que usan orden natural
(`PriorityQueue`) nunca importan `core.interfaces.Comparable`: lo referencian
con su nombre completamente calificado solo en el único constructor que lo
acepta, y dejan que `Comparable` sin calificar siga significando
`java.lang.Comparable`.

## `util.Iterator<T>` vs `java.util.Iterator<T>`

`util.Iterator<T>` existe como interfaz con nombre propio del proyecto,
pero extiende `java.util.Iterator<T>` sin añadir miembros. Así, cualquier
`Iterator<T>` de este paquete es un `java.util.Iterator<T>` genuino y
puede devolverse desde `Iterable#iterator()` sin adaptadores, manteniendo
compatibilidad total con el `for-each` estándar de Java.

## Listas enlazadas: tres variantes, un contrato

Las tres implementaciones (`SinglyLinkedList`, `DoublyLinkedList`,
`CircularLinkedList`) satisfacen el mismo `LinkedListInterface`, pero cada
una hace un trade-off distinto:

| Variante | `addLast` | `removeLast` | `get(medio)` | Notas |
|---|---|---|---|---|
| `SinglyLinkedList` | O(1) (referencia a `tail`) | O(n) (hay que hallar el penúltimo) | O(n) | La más simple; menor overhead de memoria por nodo. |
| `DoublyLinkedList` | O(1) | O(1) | O(n), pero recorre desde el extremo más cercano | El mejor trade-off general cuando se necesita `removeLast` frecuente. |
| `CircularLinkedList` | O(1) | O(n) | O(n) | Solo mantiene `tail` (`tail.next == head`); útil para buffers cíclicos, turnos round-robin, etc. |

## Pilas y colas: arreglo vs. enlazada

`ArrayStack`/`ArrayQueue` amortizan el costo de crecer el arreglo dinámico
(`ArrayDynamics`, factor de crecimiento 2×) entre muchas operaciones, y
también se encogen cuando la ocupación cae por debajo de 25% de la
capacidad, para no retener memoria de más tras picos de uso. `ArrayQueue`
usa el arreglo como **buffer circular**: `front` y `rear` avanzan con
aritmética modular, así que `dequeue` nunca desplaza elementos.

`LinkedStack`/`LinkedQueue` no necesitan redimensionar nada: cada operación
es O(1) estricto, a cambio de un nodo extra por elemento (overhead de
memoria y peor localidad de caché que un arreglo).

## `PriorityQueue`: heap binario iterativo

Implementado como *min-heap* (por defecto) sobre un arreglo dinámico, con
`siftUp`/`siftDown` iterativos. El criterio de orden se resuelve en este
orden de prioridad: `Comparator` explícito → `core.interfaces.Comparable`
propio (adaptado a `Comparator`) → orden natural (`java.lang.Comparable`)
si no se provee ninguno.

A diferencia de una lista, el arreglo interno de un heap solo garantiza la
*propiedad de heap* (cada nodo ≤ sus hijos), no un orden total de extremo a
extremo; por eso `iterator()`/`toArray()` devuelven los elementos en orden
de heap, documentado explícitamente para no inducir a error.

## `HashTable`: un solo arreglo, dos familias de resolución de colisiones

`HashTable<K, V>` usa el patrón *Strategy* dos veces: una vez para la
función de hash (`HashFunction<K>`) y otra para la resolución de
colisiones (`CollisionStrategy`), de forma completamente ortogonal —
cualquier combinación de las tres funciones de hash con cualquiera de las
tres estrategias de colisión funciona sobre el mismo arreglo interno
`Entry<K, V>[]`.

- **Encadenamiento separado** (`ChainingStrategy`): cada slot es cabeza de
  una lista enlazada de `Entry` (usando el propio campo `next` de
  `Entry`, sin envolturas adicionales). Tolera un factor de carga más alto
  (umbral por defecto 0.75) porque una colisión no degrada el acceso a
  otros buckets.
- **Direccionamiento abierto** (`LinearProbing`, `QuadraticProbing`): cada
  slot almacena a lo sumo una entrada; las colisiones sondean índices
  alternativos dentro del mismo arreglo. Las remociones dejan una marca
  "tombstone" (un centinela compartido `TOMBSTONE`, distinto de `null`)
  para no cortar prematuramente la secuencia de sondeo de entradas
  insertadas después. Usa un umbral de carga más conservador por defecto
  (0.5) porque el sondeo se degrada con la ocupación.

El redimensionamiento (duplicar la capacidad y reinsertar cada entrada
recalculando su hash) es el único punto donde los tombstones se descartan
definitivamentente, lo que también limpia la degradación acumulada del
sondeo tras muchas remociones.

### Limitación conocida

El crecimiento de capacidad es siempre ×2 (potencia de dos), no
necesariamente primo. El método de la división (`DivisionHash`) distribuye
mejor con capacidades primas; en la práctica, combinarlo con capacidades
potencia de dos puede concentrar más colisiones que con un `m` primo. El
método de la multiplicación (`MultiplicationHash`) no tiene esta
limitación. Una mejora futura razonable sería redimensionar al siguiente
primo mayor a `capacidad × 2`.

## Visualización

`Drawable` es un contrato opcional: todas las estructuras concretas de
esta biblioteca lo implementan, proveyendo su propio arte ASCII (`draw()`).
`ASCIIVisualizer` (la única implementación de `StructureVisualizer` por
ahora) delega en `draw()` cuando está disponible, y cae a un renderizado
genérico basado en iteración para cualquier otra `SequentialCollection`
que no lo implemente.

## Excepciones

Las tres excepciones de `core.exceptions` son todas no verificadas
(`RuntimeException`), consistente con el resto de la biblioteca (similar a
`java.util`, donde `NoSuchElementException` o
`IndexOutOfBoundsException` tampoco son verificadas): representan errores
de uso de la API, no condiciones externas recuperables.

- `EmptyStructureException`: operar sobre una estructura vacía.
- `DuplicateKeyException`: reservada para operaciones de inserción
  estricta que deban rechazar claves duplicadas (`HashTable.put` por
  defecto actualiza en vez de rechazar).
- `CapacityExceededException`: red de seguridad para direccionamiento
  abierto, si por algún motivo se agotan los intentos de sondeo sin
  redimensionar a tiempo.
