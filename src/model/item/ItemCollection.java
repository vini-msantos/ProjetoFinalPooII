package model.item;

import model.sorting.SortingOption;
import model.util.Status;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class ItemCollection<T extends AbstractItem> implements Serializable {
    private final Map<Integer, T> items;

    public ItemCollection() {
        this.items = new HashMap<>();
    }

    public List<T> list(@NotNull SortingOption<T> sortingOption) {
        return items.values().stream()
                .sorted(sortingOption.getComparator())
                .toList();
    }

    public List<T> search(@NotNull SortingOption<T> sortingOption, @NotNull Function<T, Object> getter, @NotNull String prefix) {
        return list(sortingOption).stream()
                .filter(item -> getter.apply(item).toString().toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }

    public Status add(@NotNull T item) {
        if (items.putIfAbsent(item.getId(), item) != null) { return Status.ERROR; }
        return Status.OK;
    }

    public Status remove(int id) {
        if (items.remove(id) == null) { return Status.ERROR; }
        return Status.OK;
    }

    public void update(T item) {
        if (!items.containsKey(item.getId())) { return; }
        items.put(item.getId(), item);
    }

    public T get(int id) {
        return items.get(id);
    }
}
