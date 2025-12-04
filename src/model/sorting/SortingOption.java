package model.sorting;

import java.io.Serializable;
import java.util.Comparator;

public final class SortingOption<T> implements Serializable {
    private Sorter<? super T> sorter;
    private boolean reversed;

    public SortingOption(Sorter<? super T> sorter, boolean reversed) {
        this.sorter = sorter;
        this.reversed = reversed;
    }

    public Comparator<? super T> getComparator() {
        if (reversed) {
            return sorter.sortDescending();
        } else {
            return sorter.sortAscending();
        }
    }

    public void setSorter(Sorter<T> sorter) {
        this.sorter = sorter;
    }

    public Sorter<? super T> getSorter() {
        return sorter;
    }
}
