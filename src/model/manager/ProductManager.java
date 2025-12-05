package model.manager;

import model.item.Product;
import model.sorting.SortingConfig;
import model.sorting.SortingOption;
import model.util.Serializer;
import model.util.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductManager extends AbstractManager<Product> {
    private static final String filePath = "productManager.ser";
    private static ProductManager instance;

    protected ProductManager() {
        super();
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

    public Status createProduct(String name, BigDecimal price) {
        Product product = new Product(getIdCounter(), name, price);
        return add(product);
    }

    @Override
    public SortingOption<Product> getSortingOption() {
        return SortingConfig.getProductSortingOption();
    }

    @Override
    public void setSortingOption(SortingOption<Product> sortingOption) {
        SortingConfig.setProductSortingOption(sortingOption);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Product> searchName(String prefix) {
        return search(Product::getName, Objects.requireNonNullElse(prefix, ""));
    }
}
