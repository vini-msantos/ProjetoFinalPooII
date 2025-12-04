package model.item;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class Product extends AbstractItem {
    private String name;
    private BigDecimal price;

    public Product(int id, @NotNull String name, @NotNull BigDecimal price) {
        super(id);
        if (name.isBlank()) {
            this.name = "Product with no name";
        } else {
            this.name = name;
        }
        if (price.doubleValue() <= 0) {
            this.price = BigDecimal.ONE;
        } else {
            this.price = price;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        if (name.isBlank()) { return; }
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull BigDecimal price) {
        if (price.doubleValue() <= 0) { return; }
        this.price = price;
    }

    public Product copy() {
        return new Product(
                getId(),
                getName().substring(0),
                getPrice().multiply(BigDecimal.ONE)
        );
    }

    public ProductWithQuantity withQuantity(int quantity) {
        Product copy = this.copy();
        return new ProductWithQuantity(copy.getId(), copy.getName(), copy.getPrice(), quantity);
    }

    @Override
    public String toString() {
        return "(" + getId() + ")   " + getName() + "   $" + getPrice();
    }
}
