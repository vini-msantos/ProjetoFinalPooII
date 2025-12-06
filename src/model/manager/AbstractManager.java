package model.manager;

import model.item.AbstractItem;
import model.item.ItemCollection;
import model.sorting.SortingOption;
import model.util.Status;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;


/**
 * Os Managers são as classes responsáveis pelos registros dos itens, cada uma de
 * suas implementações concretas é um singleton e podem ser salvas e carregadas
 * da memória.
 * <p>Possui um contador de ID, que é incrementado automaticamente quando um item
 * novo é cadastrado.</p>
 * <P>Suas implementações sempre tentarão se carregar de um arquivo fonte. Apenas
 * caso não consigam, um novo objeto é instanciado.</P>
 * @param <T>
 */
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

    public void remove(int id) {
        items.remove(id);
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
