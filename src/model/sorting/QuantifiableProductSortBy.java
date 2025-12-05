package model.sorting;
import model.item.Product;
import model.item.ProductWithQuantity;

import java.util.Comparator;

public enum QuantifiableProductSortBy implements Sorter<ProductWithQuantity> {
    ID,
    NAME,
    PRICE,
    QUANTITY;

    @Override
    public Comparator<ProductWithQuantity> sortAscending() {
        return switch (this) {
            case ID -> Comparator.comparingInt(Product::getId);
            case NAME -> Comparator.comparing(Product::getName);
            case PRICE -> Comparator.comparing(Product::getPrice);
            case QUANTITY -> Comparator.comparingInt(ProductWithQuantity::getQuantity);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ID -> "ID";
            case NAME -> "Name";
            case PRICE -> "Price";
            case QUANTITY -> "Quantity";
        };
    }
}
