package model.sorting;
import model.item.Bill;
import model.item.Product;

import java.util.Comparator;

public enum BillSortBy implements Sorter<Bill> {
    ID,
    CLIENT_NAME,
    TOTAL;

    @Override
    public Comparator<? super Bill> sortAscending() {
        return switch (this) {
            case ID -> Comparator.comparingInt(Bill::getId);
            case CLIENT_NAME -> Comparator.comparing(Bill::getClientName);
            case TOTAL -> Comparator.comparing(Bill::getTotal);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ID -> "ID";
            case CLIENT_NAME -> "Client Name";
            case TOTAL -> "Total";
        };
    }
}
