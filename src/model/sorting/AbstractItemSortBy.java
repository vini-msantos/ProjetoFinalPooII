package model.sorting;

import model.item.AbstractItem;

import java.util.Comparator;

public enum AbstractItemSortBy implements Sorter<AbstractItem> {
    ID;

    @Override
    public Comparator<AbstractItem> sortAscending() {
        return Comparator.comparingInt(AbstractItem::getId);
    }
}
