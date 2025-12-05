package model.sorting;

import java.io.Serializable;
import java.util.Comparator;

public final class SortingOption<T> implements Serializable {
    private Sorter<T> sorter;
    private boolean reversed;

    public SortingOption(Sorter<T> sorter, boolean reversed) {
        this.sorter = sorter;
        this.reversed = reversed;
    }

    public Comparator<T> getComparator() {
        if (reversed) {
            return sorter.sortDescending();
        } else {
            return sorter.sortAscending();
        }
    }

    public void setSorter(Sorter<T> sorter) {
        this.sorter = sorter;
    }

    public Sorter<T> getSorter() {
        return sorter;
    }

    public boolean isReversed() {
        return reversed;
    }
}
