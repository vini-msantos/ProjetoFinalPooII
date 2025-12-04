package model.manager;

import model.item.AbstractItem;
import model.item.ItemCollection;
import model.sorting.AbstractItemSortBy;
import model.sorting.SortingOption;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractManager<T extends AbstractItem> implements Serializable {
    private final ItemCollection<T> items;
    private SortingOption<? super T> sortingOption;
    private int idCounter;

    protected AbstractManager(SortingOption<T> sortingOption) {
        this.items = new ItemCollection<>();
        this.sortingOption = sortingOption;
        this.idCounter = 0;
    }

    public Status add(@NotNull T item) {
        if (item.getId() != idCounter) { return Status.ERROR; }
        Status result = items.add(item);
        if (result == Status.OK) {
            idCounter += 1;
        }
        return result;
    }

    public void update(@NotNull T item) {
        items.update(item);
    }

    public T get(int id) {
        return items.get(id);
    }

    public Status remove(int id) {
        return items.remove(id);
    }

    public Comparator<? super T> getComparator() {
        return sortingOption.getComparator();
    }

    public void setSortingOption(@NotNull SortingOption<T> sortingOption) {
        this.sortingOption = sortingOption;
    }

    public SortingOption<? super T> getSortingOption() {
        return sortingOption;
    }

    public List<T> list() {
        return items.list(getComparator());
    }

    public abstract List<T> searchName(@Nullable String prefix);
    protected List<T> search(@NotNull Function<T, Object> getter, @NotNull String prefix) {
        return items.search(getComparator(), getter, prefix);
    }

    public int getIdCounter() {
        return idCounter;
    }

    public abstract Status save();
}
