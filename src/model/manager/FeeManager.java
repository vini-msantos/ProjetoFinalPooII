package model.manager;

import model.item.Fee;
import model.sorting.SortingConfig;
import model.sorting.SortingOption;
import model.util.Serializer;
import model.util.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class FeeManager extends AbstractManager<Fee> implements Serializable {
    private static final String filePath = "feeManager.ser";
    private static FeeManager instance;

    private FeeManager() {
        super();
    }

    @Override
    public SortingOption<Fee> getSortingOption() {
        return SortingConfig.getFeeSortingOption();
    }

    @Override
    public void setSortingOption(SortingOption<Fee> sortingOption) {
        SortingConfig.setFeeSortingOption(sortingOption);
    }

    public static FeeManager getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public static FeeManager load() {
        FeeManager manager = Serializer.load(filePath);
        if (manager == null) {
            manager = new FeeManager();
        }

        return manager;
    }

    public Status createFee(String name, BigDecimal percentage) {
        Fee fee = new Fee(getIdCounter(), name, percentage);
        return add(fee);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Fee> searchName(String prefix) {
        return search(Fee::getName, Objects.requireNonNullElse(prefix, ""));
    }
}
