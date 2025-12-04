package model.sorting;
import model.item.Product;

import java.util.Comparator;

public enum ProductSortBy implements Sorter<Product> {
    ID,
    NAME,
    PRICE;

    @Override
    public Comparator<? super Product> sortAscending() {
        return switch (this) {
            case ID -> Comparator.comparingInt(Product::getId);
            case NAME -> Comparator.comparing(Product::getName);
            case PRICE -> Comparator.comparing(Product::getPrice);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ID -> "ID";
            case NAME -> "Name";
            case PRICE -> "Price";
        };
    }
}
