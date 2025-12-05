package model.sorting;

import model.item.Fee;

import java.util.Comparator;

public enum FeeSortBy implements Sorter<Fee> {
    ID,
    NAME,
    PERCENTAGE;

    @Override
    public Comparator<Fee> sortAscending() {
        return switch (this) {
            case ID -> Comparator.comparingInt(Fee::getId);
            case NAME -> Comparator.comparing(Fee::getName);
            case PERCENTAGE -> Comparator.comparing(Fee::getPercentage);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ID -> "ID";
            case NAME -> "Name";
            case PERCENTAGE -> "Percentage";
        };
    }
}
