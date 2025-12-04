package model.manager;

import model.item.Client;
import model.item.Fee;
import model.util.Serializer;
import model.sorting.FeeSortBy;
import model.sorting.SortingOption;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class FeeManager extends AbstractManager<Fee> implements Serializable {
    private static final String filePath = "data/feeManager.ser";
    private static FeeManager instance;

    private FeeManager() {
        super(new SortingOption<>(FeeSortBy.NAME, false));
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

    public Status createFee(@NotNull String name, @NotNull BigDecimal percentage) {
        Fee fee = new Fee(getIdCounter(), name, percentage);
        return add(fee);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Fee> searchName(@Nullable String prefix) {
        return search(Fee::getName, Objects.requireNonNullElse(prefix, ""));
    }
}
