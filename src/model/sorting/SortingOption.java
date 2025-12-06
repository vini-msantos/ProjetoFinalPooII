package model.sorting;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Classe que guarda as configurações de ordenamento para um determinado tipo de item.
 * @param sorter Dita qual atributo do item vai ser comparado.
 * @param reversed Ordem crescente caso false, decrescente caso true.
 * @param <T> Tipo a ser ordenado.
 */
public record SortingOption<T>(Sorter<T> sorter, boolean reversed) implements Serializable {
    public Comparator<T> getComparator() {
        if (reversed) {
            return sorter.sortDescending();
        } else {
            return sorter.sortAscending();
        }
    }
}
