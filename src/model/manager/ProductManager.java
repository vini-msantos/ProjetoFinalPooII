package model.manager;

import model.item.Client;
import model.item.Product;
import model.util.Serializer;
import model.sorting.ProductSortBy;
import model.sorting.SortingOption;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductManager extends AbstractManager<Product> {
    private static final String filePath = "data/productManager.ser";
    private static ProductManager instance;

    protected ProductManager() {
        super(new SortingOption<>(ProductSortBy.NAME, false));
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public static ProductManager load() {
        ProductManager manager = Serializer.load(filePath);
        if (manager == null) {
            manager = new ProductManager();
        }

        return manager;
    }

    public Status createProduct(@NotNull String name, @NotNull BigDecimal price) {
        Product product = new Product(getIdCounter(), name, price);
        return add(product);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Product> searchName(@Nullable String prefix) {
        return search(Product::getName, Objects.requireNonNullElse(prefix, ""));
    }
}
