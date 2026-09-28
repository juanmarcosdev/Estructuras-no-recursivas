package com.cyedbooks.estructuras.visualization;

import com.cyedbooks.estructuras.hashtable.HashTable;
import com.cyedbooks.estructuras.model.Entry;

public final class TerminalView {

    private TerminalView() {
    }

    public static String stack(Object[] items) {
        int w = Math.max(7, maxLen(items) + 4);
        String pad = "      ";
        StringBuilder sb = new StringBuilder();
        sb.append(pad).append('|').append(" ".repeat(w)).append("|\n");
        for (int i = 0; i < items.length; i++) {
            sb.append(pad).append('|').append(center(str(items[i]), w)).append('|');
            if (i == 0) {
                sb.append("  <- tope");
            }
            sb.append('\n');
        }
        if (items.length == 0) {
            sb.append(pad).append('|').append(" ".repeat(w)).append("|\n");
        }
        sb.append(pad).append('+').append("-".repeat(w)).append("+\n");
        if (items.length == 0) {
            sb.append(pad).append(' ').append(center("vacia", w).stripTrailing()).append('\n');
        }
        return sb.toString();
    }

    public static String queue(Object[] items) {
        int w = Math.max(4, maxLen(items) + 2);
        String left = "  sale <- ";
        String pad = " ".repeat(left.length());
        StringBuilder border = new StringBuilder("+");
        StringBuilder middle = new StringBuilder("|");
        int n = Math.max(items.length, 1);
        for (int i = 0; i < n; i++) {
            border.append("-".repeat(w)).append('+');
            String v = items.length == 0 ? "" : str(items[i]);
            middle.append(center(v, w)).append('|');
        }
        StringBuilder sb = new StringBuilder();
        sb.append(pad).append(border).append('\n');
        sb.append(left).append(middle).append(" <- entra\n");
        sb.append(pad).append(border).append('\n');
        if (items.length == 0) {
            sb.append(pad).append(" vacia\n");
            return sb.toString();
        }
        int first = pad.length() + 1 + w / 2;
        int last = pad.length() + 1 + (items.length - 1) * (w + 1) + w / 2;
        char[] arrows = " ".repeat(last + 1).toCharArray();
        arrows[first] = '^';
        arrows[last] = '^';
        sb.append(new String(arrows)).append('\n');
        if (first == last) {
            sb.append(" ".repeat(Math.max(0, first - 5))).append("front/rear\n");
        } else {
            String labels = " ".repeat(Math.max(0, first - 2)) + "front";
            labels += " ".repeat(Math.max(1, last - 1 - labels.length())) + "rear";
            sb.append(labels).append('\n');
        }
        return sb.toString();
    }

    public static String heap(Object[] items) {
        if (items.length == 0) {
            return "  (vacia)\n";
        }
        StringBuilder sb = new StringBuilder("  arreglo: ");
        for (int i = 0; i < items.length; i++) {
            sb.append('[').append(str(items[i])).append(']');
        }
        sb.append("\n\n");

        int levels = 32 - Integer.numberOfLeadingZeros(items.length);
        int cell = Math.max(3, maxLen(items) + 1);
        int width = (1 << (levels - 1)) * (cell + 1);
        for (int level = 0; level < levels; level++) {
            int count = 1 << level;
            int seg = width / count;
            int start = count - 1;
            char[] row = " ".repeat(width + 2).toCharArray();
            char[] links = " ".repeat(width + 2).toCharArray();
            for (int k = 0; k < count && start + k < items.length; k++) {
                int idx = start + k;
                String v = str(items[idx]);
                int c = k * seg + seg / 2;
                int from = Math.max(0, c - v.length() / 2);
                for (int j = 0; j < v.length() && from + j < row.length; j++) {
                    row[from + j] = v.charAt(j);
                }
                if (2 * idx + 1 < items.length) {
                    links[c - seg / 8 - 1] = '/';
                }
                if (2 * idx + 2 < items.length) {
                    links[c + seg / 8 + 1] = '\\';
                }
            }
            sb.append("  ").append(new String(row).stripTrailing()).append('\n');
            String l = new String(links).stripTrailing();
            if (!l.isEmpty()) {
                sb.append("  ").append(l).append('\n');
            }
        }
        return sb.toString();
    }

    public static String list(Object[] items, String type) {
        if (items.length == 0) {
            return "  head -> null   (vacia)\n";
        }
        boolean doubly = type.equals("doble");
        boolean circular = type.equals("circular");
        String link = doubly ? "<-->" : "--->";
        String start = doubly ? "null <---" : "";

        StringBuilder top = new StringBuilder(" ".repeat(start.length()));
        StringBuilder mid = new StringBuilder(start);
        int[] centers = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            String v = str(items[i]);
            int w = v.length() + 2;
            if (i > 0) {
                top.append("    ");
                mid.append(link);
            }
            centers[i] = top.length() + w / 2 + 1;
            top.append('+').append("-".repeat(w)).append('+');
            mid.append('|').append(center(v, w)).append('|');
        }
        if (circular) {
            mid.append("---+");
        } else {
            mid.append("---> null");
        }

        StringBuilder sb = new StringBuilder();
        int first = centers[0] + 2;
        int last = centers[items.length - 1] + 2;
        char[] names = " ".repeat(last + 4).toCharArray();
        put(names, first - 2, "head");
        if (last != first) {
            put(names, last - 2, "tail");
        }
        sb.append(new String(names).stripTrailing()).append('\n');
        char[] bar = " ".repeat(last + 1).toCharArray();
        bar[first] = '|';
        bar[last] = '|';
        sb.append(new String(bar)).append('\n');
        bar[first] = 'v';
        bar[last] = 'v';
        sb.append(new String(bar)).append('\n');

        sb.append("  ").append(top).append('\n');
        sb.append("  ").append(mid).append('\n');
        sb.append("  ").append(top).append('\n');
        if (circular) {
            int end = 2 + mid.length() - 1;
            char[] back = " ".repeat(end + 1).toCharArray();
            back[first] = '^';
            back[end] = '|';
            sb.append(new String(back)).append('\n');
            back[first] = '+';
            for (int i = first + 1; i < end; i++) {
                back[i] = '-';
            }
            back[end] = '+';
            sb.append(new String(back)).append('\n');
        }
        return sb.toString();
    }

    public static <K, V> String hashTable(HashTable<K, V> t, int mark, String markText) {
        int w = 7;
        for (int i = 0; i < t.capacity(); i++) {
            Entry<K, V> e = t.slot(i);
            if (e != null) {
                w = Math.max(w, e.toString().length() + 2);
            }
        }
        int digits = String.valueOf(t.capacity() - 1).length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  capacidad %d | elementos %d | factor de carga %.2f%n%n",
                t.capacity(), t.size(), t.loadFactor()));
        String border = "  " + " ".repeat(digits) + " +" + "-".repeat(w) + "+\n";
        sb.append(border);
        for (int i = 0; i < t.capacity(); i++) {
            sb.append("  ").append(String.format("%" + digits + "d", i)).append(" |");
            Entry<K, V> e = t.slot(i);
            if (t.isDeleted(i)) {
                sb.append(left("xxx", w)).append("|  (borrado)");
            } else if (e == null) {
                sb.append(" ".repeat(w)).append('|');
            } else {
                sb.append(left(e.toString(), w)).append('|');
                if (t.usesChaining()) {
                    Entry<K, V> next = e.getNext();
                    while (next != null) {
                        sb.append(" --> [").append(next).append(']');
                        next = next.getNext();
                    }
                }
            }
            if (i == mark) {
                sb.append("   <- ").append(markText);
            }
            sb.append('\n');
        }
        sb.append(border);
        return sb.toString();
    }

    private static void put(char[] line, int from, String text) {
        for (int i = 0; i < text.length() && from + i < line.length; i++) {
            if (from + i >= 0) {
                line[from + i] = text.charAt(i);
            }
        }
    }

    private static String str(Object o) {
        return String.valueOf(o);
    }

    private static int maxLen(Object[] items) {
        int max = 0;
        for (Object o : items) {
            max = Math.max(max, str(o).length());
        }
        return max;
    }

    private static String left(String s, int w) {
        return " " + s + " ".repeat(Math.max(0, w - s.length() - 1));
    }

    private static String center(String s, int w) {
        if (s.length() >= w) {
            return s;
        }
        int left = (w - s.length()) / 2;
        return " ".repeat(left) + s + " ".repeat(w - s.length() - left);
    }
}
