package model.sorting;

import java.util.Comparator;

/**
 * As classes que implementam essa interface descrevem as formas que uma lista
 * de itens pode ser ordenada.
 * @param <T> Objeto que será ordenado.
 */
public interface Sorter<T> {
    Comparator<T> sortAscending();
    default Comparator<T> sortDescending() {
        return sortAscending().reversed();
    }
}
