package com.cyedbooks.estructuras;

import com.cyedbooks.estructuras.hashtable.HashFunction;
import com.cyedbooks.estructuras.hashtable.HashTable;
import com.cyedbooks.estructuras.hashtable.collision.ChainingStrategy;
import com.cyedbooks.estructuras.hashtable.collision.CollisionStrategy;
import com.cyedbooks.estructuras.hashtable.collision.LinearProbing;
import com.cyedbooks.estructuras.hashtable.collision.QuadraticProbing;
import com.cyedbooks.estructuras.hashtable.impl.DivisionHash;
import com.cyedbooks.estructuras.hashtable.impl.FNV1aHash;
import com.cyedbooks.estructuras.hashtable.impl.MultiplicationHash;
import com.cyedbooks.estructuras.linkedlist.CircularLinkedList;
import com.cyedbooks.estructuras.linkedlist.DoublyLinkedList;
import com.cyedbooks.estructuras.linkedlist.LinkedListInterface;
import com.cyedbooks.estructuras.linkedlist.SinglyLinkedList;
import com.cyedbooks.estructuras.model.Entry;
import com.cyedbooks.estructuras.queue.ArrayQueue;
import com.cyedbooks.estructuras.queue.LinkedQueue;
import com.cyedbooks.estructuras.queue.PriorityQueue;
import com.cyedbooks.estructuras.queue.QueueInterface;
import com.cyedbooks.estructuras.stack.ArrayStack;
import com.cyedbooks.estructuras.stack.LinkedStack;
import com.cyedbooks.estructuras.stack.StackInterface;
import com.cyedbooks.estructuras.visualization.TerminalView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static String msg = "";

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("demo")) {
            demo();
            return;
        }
        while (true) {
            clear();
            System.out.println("=== Estructuras no recursivas ===\n");
            System.out.println("1. Pila");
            System.out.println("2. Cola");
            System.out.println("3. Cola de prioridad");
            System.out.println("4. Tabla hash");
            System.out.println("5. Lista enlazada");
            System.out.println("6. Demo automatica");
            System.out.println("0. Salir");
            switch (read("\nopcion: ")) {
                case "1" -> stackMenu();
                case "2" -> queueMenu();
                case "3" -> priorityMenu();
                case "4" -> hashMenu();
                case "5" -> listMenu();
                case "6" -> demo();
                case "0" -> {
                    return;
                }
                default -> {
                }
            }
        }
    }

    private static void stackMenu() {
        String op = choose("Pila", "1. ArrayStack", "2. LinkedStack");
        StackInterface<String> s = op.equals("2") ? new LinkedStack<>() : new ArrayStack<>();
        String name = s.getClass().getSimpleName();
        msg = "";
        while (true) {
            screen("Pila (" + name + ")", TerminalView.stack(s.toArray()));
            System.out.println("1. push  2. pop  3. peek  4. vaciar  0. volver");
            try {
                switch (read("\nopcion: ")) {
                    case "1" -> {
                        String v = read("valor: ");
                        s.push(v);
                        msg = "push(" + v + ")";
                    }
                    case "2" -> msg = "pop() -> " + s.pop();
                    case "3" -> msg = "peek() -> " + s.peek();
                    case "4" -> {
                        s.clear();
                        msg = "se vacio la pila";
                    }
                    case "0" -> {
                        return;
                    }
                    default -> msg = "";
                }
            } catch (RuntimeException e) {
                msg = "error: " + e.getMessage();
            }
        }
    }

    private static void queueMenu() {
        String op = choose("Cola", "1. ArrayQueue", "2. LinkedQueue");
        QueueInterface<String> q = op.equals("2") ? new LinkedQueue<>() : new ArrayQueue<>();
        String name = q.getClass().getSimpleName();
        msg = "";
        while (true) {
            screen("Cola (" + name + ")", TerminalView.queue(q.toArray()));
            System.out.println("1. enqueue  2. dequeue  3. front  4. rear  5. vaciar  0. volver");
            try {
                switch (read("\nopcion: ")) {
                    case "1" -> {
                        String v = read("valor: ");
                        q.enqueue(v);
                        msg = "enqueue(" + v + ")";
                    }
                    case "2" -> msg = "dequeue() -> " + q.dequeue();
                    case "3" -> msg = "front() -> " + q.front();
                    case "4" -> msg = "rear() -> " + q.rear();
                    case "5" -> {
                        q.clear();
                        msg = "se vacio la cola";
                    }
                    case "0" -> {
                        return;
                    }
                    default -> msg = "";
                }
            } catch (RuntimeException e) {
                msg = "error: " + e.getMessage();
            }
        }
    }

    private static void priorityMenu() {
        String op = choose("Cola de prioridad", "1. min-heap (sale el menor)", "2. max-heap (sale el mayor)");
        PriorityQueue<Integer> pq = op.equals("2")
                ? new PriorityQueue<>(Comparator.reverseOrder())
                : new PriorityQueue<>();
        String name = op.equals("2") ? "max-heap" : "min-heap";
        msg = "";
        while (true) {
            screen("Cola de prioridad (" + name + ")", TerminalView.heap(pq.toArray()));
            System.out.println("1. enqueue  2. dequeue  3. front  4. vaciar  0. volver");
            try {
                switch (read("\nopcion: ")) {
                    case "1" -> {
                        int v = Integer.parseInt(read("numero: ").trim());
                        pq.enqueue(v);
                        msg = "enqueue(" + v + ")";
                    }
                    case "2" -> msg = "dequeue() -> " + pq.dequeue();
                    case "3" -> msg = "front() -> " + pq.front();
                    case "4" -> {
                        pq.clear();
                        msg = "se vacio la cola";
                    }
                    case "0" -> {
                        return;
                    }
                    default -> msg = "";
                }
            } catch (NumberFormatException e) {
                msg = "error: tiene que ser un numero";
            } catch (RuntimeException e) {
                msg = "error: " + e.getMessage();
            }
        }
    }

    private static void hashMenu() {
        String f = choose("Tabla hash - funcion hash", "1. division", "2. multiplicacion", "3. FNV-1a");
        String c = choose("Tabla hash - colisiones", "1. encadenamiento", "2. sondeo lineal", "3. sondeo cuadratico");
        HashFunction<String> hf = switch (f) {
            case "2" -> new MultiplicationHash<>();
            case "3" -> new FNV1aHash();
            default -> new DivisionHash<>();
        };
        CollisionStrategy cs = switch (c) {
            case "2" -> new LinearProbing();
            case "3" -> new QuadraticProbing();
            default -> new ChainingStrategy();
        };
        String name = hf.getClass().getSimpleName() + " + " + cs.getClass().getSimpleName();
        HashTable<String, String> t = new HashTable<>(8, hf, cs);
        int mark = -1;
        msg = "";
        while (true) {
            screen("Tabla hash (" + name + ")", TerminalView.hashTable(t, mark, "aqui"));
            System.out.println("1. put  2. get  3. remove  4. vaciar  0. volver");
            mark = -1;
            try {
                switch (read("\nopcion: ")) {
                    case "1" -> {
                        String k = read("clave: ");
                        String v = read("valor: ");
                        int before = t.capacity();
                        t.put(k, v);
                        String extra = before != t.capacity()
                                ? "la tabla crecio de " + before + " a " + t.capacity() + " y se reinserto todo\n"
                                : "";
                        List<Integer> path = path(t, hf, cs, k);
                        mark = path.get(path.size() - 1);
                        msg = extra + "put(" + k + ", " + v + ")\n" + explain(t, hf, k, path);
                    }
                    case "2" -> {
                        String k = read("clave: ");
                        String v = t.get(k);
                        List<Integer> path = path(t, hf, cs, k);
                        mark = v == null ? -1 : path.get(path.size() - 1);
                        msg = "get(" + k + ") -> " + v + "\n" + explain(t, hf, k, path);
                    }
                    case "3" -> {
                        String k = read("clave: ");
                        List<Integer> path = path(t, hf, cs, k);
                        String v = t.remove(k);
                        msg = "remove(" + k + ") -> " + v;
                        if (v != null) {
                            mark = path.get(path.size() - 1);
                            msg += "\n" + explain(t, hf, k, path);
                        }
                    }
                    case "4" -> {
                        t.clear();
                        msg = "se vacio la tabla";
                    }
                    case "0" -> {
                        return;
                    }
                    default -> msg = "";
                }
            } catch (RuntimeException e) {
                msg = "error: " + e.getMessage();
            }
        }
    }

    //recorre las mismas posiciones que la tabla para mostrar por donde busco
    private static List<Integer> path(HashTable<String, String> t, HashFunction<String> hf,
                                      CollisionStrategy cs, String key) {
        List<Integer> path = new ArrayList<>();
        int base = hf.hash(key, t.capacity());
        if (t.usesChaining()) {
            path.add(base);
            return path;
        }
        for (int i = 0; i < t.capacity(); i++) {
            int idx = Math.floorMod(cs.probe(base, i, t.capacity()), t.capacity());
            path.add(idx);
            Entry<String, String> e = t.slot(idx);
            if (e == null && !t.isDeleted(idx)) {
                break;
            }
            if (e != null && e.getKey().equals(key)) {
                break;
            }
        }
        return path;
    }

    private static String explain(HashTable<String, String> t, HashFunction<String> hf, String key, List<Integer> path) {
        int base = hf.hash(key, t.capacity());
        StringBuilder sb = new StringBuilder("h(\"" + key + "\") = " + base);
        if (t.usesChaining()) {
            int n = 0;
            Entry<String, String> e = t.slot(base);
            while (e != null) {
                n++;
                e = e.getNext();
            }
            if (n > 1) {
                sb.append(", hay ").append(n).append(" claves en esa posicion (colision, se encadenan)");
            }
            return sb.toString();
        }
        if (path.size() > 1) {
            sb.append(", colision. posiciones probadas: ");
            for (int i = 0; i < path.size(); i++) {
                sb.append(i > 0 ? " -> " : "").append(path.get(i));
            }
        }
        return sb.toString();
    }

    private static void listMenu() {
        String op = choose("Lista enlazada", "1. simple", "2. doble", "3. circular");
        LinkedListInterface<String> l;
        String type;
        switch (op) {
            case "2" -> {
                l = new DoublyLinkedList<>();
                type = "doble";
            }
            case "3" -> {
                l = new CircularLinkedList<>();
                type = "circular";
            }
            default -> {
                l = new SinglyLinkedList<>();
                type = "simple";
            }
        }
        msg = "";
        while (true) {
            screen("Lista " + type, TerminalView.list(l.toArray(), type));
            System.out.println("1. agregar al inicio  2. agregar al final  3. agregar en posicion");
            System.out.println("4. quitar primero     5. quitar ultimo     6. quitar en posicion");
            System.out.println("7. buscar             0. volver");
            try {
                switch (read("\nopcion: ")) {
                    case "1" -> {
                        String v = read("valor: ");
                        l.addFirst(v);
                        msg = "addFirst(" + v + ")";
                    }
                    case "2" -> {
                        String v = read("valor: ");
                        l.addLast(v);
                        msg = "addLast(" + v + ")";
                    }
                    case "3" -> {
                        int i = Integer.parseInt(read("posicion: ").trim());
                        String v = read("valor: ");
                        l.addAt(i, v);
                        msg = "addAt(" + i + ", " + v + ")";
                    }
                    case "4" -> msg = "removeFirst() -> " + l.removeFirst();
                    case "5" -> msg = "removeLast() -> " + l.removeLast();
                    case "6" -> {
                        int i = Integer.parseInt(read("posicion: ").trim());
                        msg = "removeAt(" + i + ") -> " + l.removeAt(i);
                    }
                    case "7" -> {
                        String v = read("valor: ");
                        msg = "indexOf(" + v + ") -> " + l.indexOf(v);
                    }
                    case "0" -> {
                        return;
                    }
                    default -> msg = "";
                }
            } catch (NumberFormatException e) {
                msg = "error: la posicion tiene que ser un numero";
            } catch (RuntimeException e) {
                msg = "error: " + e.getMessage();
            }
        }
    }

    private static void demo() {
        StackInterface<String> s = new ArrayStack<>();
        for (String v : new String[]{"10", "20", "30"}) {
            s.push(v);
            step("Pila", TerminalView.stack(s.toArray()), "push(" + v + ")");
        }
        String top = s.pop();
        step("Pila", TerminalView.stack(s.toArray()), "pop() -> " + top + ", sale el ultimo que entro");

        QueueInterface<String> q = new ArrayQueue<>();
        for (String v : new String[]{"A", "B", "C"}) {
            q.enqueue(v);
            step("Cola", TerminalView.queue(q.toArray()), "enqueue(" + v + ")");
        }
        String out = q.dequeue();
        step("Cola", TerminalView.queue(q.toArray()), "dequeue() -> " + out + ", sale el primero que entro");

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int v : new int[]{50, 20, 40, 10, 30, 5}) {
            pq.enqueue(v);
            step("Cola de prioridad (min-heap)", TerminalView.heap(pq.toArray()), "enqueue(" + v + ")");
        }
        int min = pq.dequeue();
        step("Cola de prioridad (min-heap)", TerminalView.heap(pq.toArray()), "dequeue() -> " + min + ", siempre sale el menor");

        HashFunction<String> hf = new DivisionHash<>();
        CollisionStrategy cs = new LinearProbing();
        HashTable<String, String> t = new HashTable<>(8, hf, cs);
        String[][] data = {{"ana", "20"}, {"luis", "31"}, {"sofia", "25"}, {"pedro", "40"}, {"juan", "18"}};
        for (String[] d : data) {
            int before = t.capacity();
            t.put(d[0], d[1]);
            List<Integer> path = path(t, hf, cs, d[0]);
            String extra = before != t.capacity() ? "la tabla crecio de " + before + " a " + t.capacity() + "\n" : "";
            step("Tabla hash (division + sondeo lineal)",
                    TerminalView.hashTable(t, path.get(path.size() - 1), "aqui"),
                    extra + "put(" + d[0] + ", " + d[1] + ")\n" + explain(t, hf, d[0], path));
        }
        List<Integer> path = path(t, hf, cs, "luis");
        t.remove("luis");
        step("Tabla hash (division + sondeo lineal)",
                TerminalView.hashTable(t, path.get(path.size() - 1), "aqui"),
                "remove(luis) -> queda marcado como borrado para no cortar la busqueda");

        LinkedListInterface<String> l = new CircularLinkedList<>();
        for (String v : new String[]{"a", "b", "c"}) {
            l.addLast(v);
            step("Lista circular", TerminalView.list(l.toArray(), "circular"), "addLast(" + v + ")");
        }
        l.removeFirst();
        step("Lista circular", TerminalView.list(l.toArray(), "circular"), "removeFirst()");

        System.out.println("fin de la demo");
    }

    private static void step(String title, String drawing, String text) {
        msg = text;
        screen(title, drawing);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void screen(String title, String drawing) {
        clear();
        System.out.println("=== " + title + " ===\n");
        System.out.println(drawing);
        if (!msg.isEmpty()) {
            System.out.println(msg);
            System.out.println();
        }
    }

    private static String choose(String title, String... options) {
        clear();
        System.out.println("=== " + title + " ===\n");
        for (String o : options) {
            System.out.println(o);
        }
        return read("\nopcion: ");
    }

    private static String read(String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            System.exit(0);
        }
        return in.nextLine();
    }

    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
