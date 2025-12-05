package model.sorting;

import java.util.Comparator;

public interface Sorter<T> {
    Comparator<T> sortAscending();
    default Comparator<T> sortDescending() {
        return sortAscending().reversed();
    }
}
