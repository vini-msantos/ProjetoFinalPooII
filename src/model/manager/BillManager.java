package model.manager;

import model.item.Bill;
import model.item.Client;
import model.sorting.BillSortBy;
import model.sorting.SortingOption;
import model.util.Serializer;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BillManager extends AbstractManager<Bill> {
    private static final String filePath = "data/billManager.ser";
    private static BillManager instance;

    protected BillManager() {
        super(new SortingOption<>(BillSortBy.ID, true));
    }
    
    public static BillManager getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public static BillManager load() {
        BillManager manager = Serializer.load(filePath);
        if (manager == null) {
            manager = new BillManager();
        }

        return manager;
    }

    public Status createBill(@NotNull Client client) {
        Bill Bill = new Bill(getIdCounter(), client);
        return add(Bill);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Bill> searchName(@Nullable String prefix) {
        return search(Bill::getClientName, Objects.requireNonNullElse(prefix, ""));
    }
}
