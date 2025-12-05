package model.item;

import model.manager.FeeManager;
import model.manager.ProductManager;
import model.sorting.SortingConfig;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

enum BillState {
    PAID,
    UNPAID,
    CANCELLED;

    @Override
    public String toString() {
        return switch (this) {
            case UNPAID -> "UNPAID";
            case PAID -> "PAID";
            case CANCELLED -> "CANCELLED";
        };
    }
}

public class Bill extends AbstractItem {
    private final ItemCollection<ProductWithQuantity> products;
    private final ItemCollection<Fee> fees;
    private BigDecimal total;
    private boolean recentChanges;
    private BillState state;
    private Client client;

    public Bill(int id, @NotNull Client client) {
        super(id);
        this.products = new ItemCollection<>();
        this.fees = new ItemCollection<>();
        this.recentChanges = true;
        this.state = BillState.UNPAID;
        this.total = BigDecimal.ZERO;
        this.client = client;
    }

    public Status addProduct(int id, @Nullable BigDecimal price, @Nullable Integer quantity) {
        Product fromCatalog = ProductManager.getInstance().get(id);
        if (fromCatalog == null) { return Status.ERROR; }

        recentChanges = true;
        Product copy = fromCatalog.copy();
        if (price != null) { copy.setPrice(price); }
        return products.add(copy.withQuantity(Objects.requireNonNullElse(quantity, 1)));
    }

    public Status addProduct(ProductWithQuantity product) {
        recentChanges = true;
        products.add(product);
        return Status.OK;
    }
    public Status addFee(Fee fee) {
        recentChanges = true;
        fees.add(fee);
        return Status.OK;
    }

    public Status addFee(int id, @Nullable BigDecimal percentage) {
        Fee fromCatalog = FeeManager.getInstance().get(id);
        if (fromCatalog == null) { return Status.ERROR; }

        recentChanges = true;
        Fee copy = fromCatalog.copy();
        if (percentage != null) { copy.setPercentage(percentage); }
        return fees.add(copy);
    }

    public Fee getFee(int id) {
        recentChanges = true;
        return fees.get(id);
    }

    public Status removeProduct(int id) {
        recentChanges = true;
        return products.remove(id);
    }

    public Status removeFee(int id) {
        recentChanges = true;
        return fees.remove(id);
    }

    public ProductWithQuantity getProduct(int id) {
        recentChanges = true;
        return products.get(id);
    }

    public List<ProductWithQuantity> listProducts() {
        return products.list(SortingConfig.getQuantifiableProductSortingOption());
    }

    public List<ProductWithQuantity> searchProducts(String query) {
        return products.search(SortingConfig.getQuantifiableProductSortingOption(), Product::getName, query);
    }

    public List<Fee> searchFees(String query) {
        return fees.search(SortingConfig.getFeeSortingOption(), Fee::getName, query);
    }

    public List<Fee> listFees() {
        return fees.list(SortingConfig.getFeeSortingOption());
    }

    public BigDecimal getTotal() {
        if (!recentChanges) { return total; }

        BigDecimal beforeFees = listProducts().stream()
                .map(ProductWithQuantity::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        total = listFees().stream()
                .map(Fee::getPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(beforeFees)
                .add(beforeFees);

        recentChanges = false;
        return total;
    }

    public String getState() {
        return state.toString();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(@NotNull Client client) {
        this.client = client;
    }

    public String getClientName() {
        return client.getName();
    }

    @Override
    public String toString() {
        return "(" + getId() + ")   " + getClientName() + "   $" + getTotal() + "   " + getState();
    }

    public void setAsPaid() { state = BillState.PAID; }
    public void setAsUnpaid() { state = BillState.UNPAID; }
    public void setAsCancelled() { state = BillState.CANCELLED; }
}
