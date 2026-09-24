# Análisis de complejidad

Notación: `n` = cantidad de elementos almacenados en la estructura al
momento de la operación. Todas las complejidades temporales corresponden a
implementaciones **iterativas** (sin recursión), por lo que la complejidad
espacial *auxiliar* (más allá del espacio de los propios elementos) es
O(1) salvo que se indique lo contrario.

## Listas enlazadas

| Operación | `SinglyLinkedList` | `DoublyLinkedList` | `CircularLinkedList` |
|---|---|---|---|
| `addFirst` | O(1) | O(1) | O(1) |
| `addLast` | O(1) | O(1) | O(1) |
| `addAt(i)` | O(n) | O(n) | O(n) |
| `removeFirst` | O(1) | O(1) | O(1) |
| `removeLast` | O(n) | O(1) | O(n) |
| `removeAt(i)` | O(n) | O(n) | O(n) |
| `remove(valor)` | O(n) | O(n) | O(n) |
| `get(i)` / `set(i)` | O(n) | O(n) — recorre desde el extremo más cercano (≤ n/2) | O(n) |
| `indexOf` / `contains` | O(n) | O(n) | O(n) |
| `peekFirst` / `peekLast` | O(1) | O(1) | O(1) |
| `clear` | O(n) | O(n) | O(n) |
| `size` / `isEmpty` | O(1) | O(1) | O(1) |
| Iteración completa | O(n) | O(n) | O(n) |

## Pilas

| Operación | `ArrayStack` | `LinkedStack` |
|---|---|---|
| `push` | O(1) amortizado (O(n) en el redimensionamiento ocasional) | O(1) estricto |
| `pop` | O(1) amortizado | O(1) estricto |
| `peek` | O(1) | O(1) |
| `contains` | O(n) | O(n) |
| `clear` | O(n) | O(n) |
| Espacio auxiliar | O(n) (arreglo, con hasta 4× de sobreasignación entre redimensionamientos) | O(n) (un nodo por elemento) |

## Colas

| Operación | `ArrayQueue` | `LinkedQueue` | `PriorityQueue` |
|---|---|---|---|
| `enqueue` | O(1) amortizado | O(1) estricto | O(log n) (`siftUp`) |
| `dequeue` | O(1) amortizado | O(1) estricto | O(log n) (`siftDown`) |
| `front` | O(1) | O(1) | O(1) |
| `rear` | O(1) | O(1) | No soportado (`UnsupportedOperationException`) |
| `contains` | O(n) | O(n) | O(n) |
| `clear` | O(1) (reasigna el arreglo) | O(n) | O(n) |

`ArrayQueue` usa el arreglo como buffer circular (índices `front`/`rear`
con aritmética modular), por lo que `dequeue` nunca desplaza elementos
como haría un arreglo lineal ingenuo.

## Tabla hash (`HashTable`)

Complejidad **esperada** (caso promedio, con una función de hash de buena
calidad y factor de carga acotado por el umbral configurado):

| Operación | Encadenamiento (`ChainingStrategy`) | Direccionamiento abierto (`LinearProbing` / `QuadraticProbing`) |
|---|---|---|
| `put` | O(1) esperado, O(1 + α) donde α = factor de carga | O(1) esperado bajo α ≤ 0.5 |
| `get` | O(1) esperado | O(1) esperado bajo α ≤ 0.5 |
| `remove` | O(1) esperado | O(1) esperado bajo α ≤ 0.5 |
| `resize` (redimensionamiento) | O(n) — reinserta todas las entradas | O(n) |
| `containsValue` | O(n) (recorre todas las entradas) | O(n) |
| `keys` / `values` / `entries` | O(n) | O(n) |

**Peor caso**: O(n) para `put`/`get`/`remove` si todas las claves colisionan
en el mismo bucket (encadenamiento) o generan la misma secuencia de sondeo
(direccionamiento abierto) — un escenario extremadamente improbable con
las funciones de hash provistas, pero matemáticamente posible con
adversarios que conozcan la función de hash.

### Funciones de hash

| Función | Complejidad | Notas |
|---|---|---|
| `DivisionHash` | O(1) | Un `hashCode()` + una operación de módulo. |
| `MultiplicationHash` | O(1) | `hashCode()` + una multiplicación en punto flotante. |
| `FNV1aHash` | O(k), k = longitud de la cadena | Procesa la cadena carácter a carácter. |

### Estrategias de colisión

| Estrategia | Complejidad por sondeo | Notas |
|---|---|---|
| `ChainingStrategy` | O(1) por acceso al bucket, O(long. de la cadena) en el peor caso | No sondea; la "colisión" se resuelve en la lista del bucket. |
| `LinearProbing` | O(1) por intento, hasta O(capacidad) en el peor caso | Buena localidad de caché; propenso a *clustering primario*. |
| `QuadraticProbing` | O(1) por intento, hasta O(capacidad) en el peor caso | Reduce el *clustering primario* a cambio de peor localidad de caché. |

## Utilidades

| Operación | Complejidad |
|---|---|
| `ArrayDynamics.resize` | O(n) (copia el arreglo) |
| `ArrayDynamics.linearizeCircular` | O(n) |
| `ASCIIVisualizer.visualize` | O(n) (recorre la estructura completa) |
