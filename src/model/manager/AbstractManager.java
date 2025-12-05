package model.manager;

import model.item.AbstractItem;
import model.item.ItemCollection;
import model.sorting.SortingOption;
import model.util.Status;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractManager<T extends AbstractItem> implements Serializable {
    private final ItemCollection<T> items;
    private int idCounter;

    protected AbstractManager() {
        this.items = new ItemCollection<>();
        this.idCounter = 0;
    }

    public Status add(T item) {
        if (item.getId() != idCounter) { return Status.ERROR; }
        Status result = items.add(item);
        if (result == Status.OK) {
            idCounter += 1;
        }
        return result;
    }

    public void update(T item) {
        items.update(item);
    }

    public T get(int id) {
        return items.get(id);
    }

    public Status remove(int id) {
        return items.remove(id);
    }

    public abstract SortingOption<T> getSortingOption();
    public abstract void setSortingOption(SortingOption<T> sortingOption);

    public List<T> list() {
        return items.list(getSortingOption());
    }

    public abstract List<T> searchName(String prefix);
    protected List<T> search(Function<T, Object> getter, String prefix) {
        return items.search(getSortingOption(), getter, prefix);
    }

    public int getIdCounter() {
        return idCounter;
    }

    public abstract Status save();
}
