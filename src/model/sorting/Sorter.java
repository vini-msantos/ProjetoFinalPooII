package model.sorting;

import model.item.Product;

import java.util.Comparator;

public interface Sorter<T> {
    Comparator<? super T> sortAscending();
    default Comparator<? super T> sortDescending() {
        return sortAscending().reversed();
    }
}
