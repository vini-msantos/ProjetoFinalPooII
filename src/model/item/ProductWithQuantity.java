package model.item;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ProductWithQuantity extends Product {
    private int quantity;

    public ProductWithQuantity(int id, @NotNull String name, @NotNull BigDecimal price, int quantity) {
        super(id, name, price);
        if (quantity <= 0) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) { return; }
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
    return getQuantity() + "x   (" + getId() + ")   " + getName() + "   $" + getPrice() + "  ----  $" + getTotal();
    }
}
