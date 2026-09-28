# Estructuras no recursivas

Implementacion en Java de listas enlazadas, pilas, colas y tabla hash sin usar recursion. Todo se hace con ciclos `for` y `while`.

## Requisitos

- Java 17
- Maven

## Como correrlo

```bash
mvn compile
mvn test
```

## Probar las estructuras en la terminal

Hay una clase `Main` con un menu para usar cada estructura y ver como cambia despues de cada operacion. La pila se dibuja como un tubo vertical, la cola como un tubo horizontal, la cola de prioridad como arbol, las listas con sus nodos y la tabla hash con cada posicion, las colisiones y los borrados.

```bash
mvn compile
java -cp target/classes com.cyedbooks.estructuras.Main
```

Para ver una demo automatica de todas:

```bash
java -cp target/classes com.cyedbooks.estructuras.Main demo
```

Sin Maven tambien se puede:

```bash
javac -d out $(find src/main/java -name "*.java")
java -cp out com.cyedbooks.estructuras.Main
```

## Estructuras

- Listas: `SinglyLinkedList`, `DoublyLinkedList`, `CircularLinkedList`
- Pilas: `ArrayStack`, `LinkedStack`
- Colas: `ArrayQueue`, `LinkedQueue`, `PriorityQueue` (heap)
- Tabla hash: `HashTable`, se le puede cambiar la funcion hash (division, multiplicacion, FNV-1a) y la forma de manejar colisiones (encadenamiento, sondeo lineal, sondeo cuadratico)

El codigo esta en `src/main/java/com/cyedbooks/estructuras/`. En `docs/` esta la explicacion del diseño y las complejidades.

## Ejemplos

```java
LinkedListInterface<String> lista = new DoublyLinkedList<>();
lista.addLast("a");
lista.addFirst("z");

StackInterface<Integer> pila = new ArrayStack<>();
pila.push(1);
pila.push(2);
pila.pop(); //2

PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.enqueue(5);
pq.enqueue(1);
pq.dequeue(); //1

HashTable<String, Integer> tabla = new HashTable<>(16, new MultiplicationHash<>(), new LinearProbing());
tabla.put("uno", 1);
tabla.get("uno"); //1
```

Para ver una estructura en consola:

```java
new ASCIIVisualizer().print("pila", pila);
```

Si se hace `pop`, `dequeue` o `peek` sobre una estructura vacia se lanza `EmptyStructureException`.
