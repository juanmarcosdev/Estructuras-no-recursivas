package com.cyedbooks.estructuras.util;

/**
 * Interfaz de iteración propia del proyecto.
 * <p>
 * Extiende {@code java.util.Iterator} sin añadir miembros nuevos: existe
 * como punto de extensión con nombre propio del paquete (por ejemplo, para
 * documentación y para que las estructuras internas hablen en términos del
 * vocabulario del proyecto) y, al ser un subtipo genuino de
 * {@code java.util.Iterator}, cualquier {@code Iterator<T>} de este paquete
 * puede devolverse directamente desde {@code Iterable#iterator()} y usarse
 * con normalidad en un bucle {@code for-each} de Java.
 *
 * @param <T> tipo de los elementos recorridos
 */
public interface Iterator<T> extends java.util.Iterator<T> {
}
