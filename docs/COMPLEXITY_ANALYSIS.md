# Complejidades

n = cantidad de elementos.

## Listas

| Operacion | Simple | Doble | Circular |
|---|---|---|---|
| addFirst, addLast | O(1) | O(1) | O(1) |
| addAt, removeAt | O(n) | O(n) | O(n) |
| removeFirst | O(1) | O(1) | O(1) |
| removeLast | O(n) | O(1) | O(n) |
| get, set, indexOf | O(n) | O(n) | O(n) |
| peekFirst, peekLast | O(1) | O(1) | O(1) |

## Pilas

| Operacion | ArrayStack | LinkedStack |
|---|---|---|
| push, pop | O(1) amortizado | O(1) |
| peek | O(1) | O(1) |
| contains | O(n) | O(n) |

## Colas

| Operacion | ArrayQueue | LinkedQueue | PriorityQueue |
|---|---|---|---|
| enqueue | O(1) amortizado | O(1) | O(log n) |
| dequeue | O(1) amortizado | O(1) | O(log n) |
| front | O(1) | O(1) | O(1) |
| rear | O(1) | O(1) | no soportado |
| contains | O(n) | O(n) | O(n) |

## Tabla hash

| Operacion | Promedio | Peor caso |
|---|---|---|
| put, get, remove | O(1) | O(n) |
| resize | O(n) | O(n) |
| containsValue, keys, values | O(n) | O(n) |

El peor caso pasa cuando muchas claves caen en la misma posicion.

Las funciones hash de division y multiplicacion son O(1). FNV-1a es O(k), con k el largo del string.
