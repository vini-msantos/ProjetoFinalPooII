package model.item;

import model.sorting.SortingOption;
import model.util.Status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Uma coleção genérica que permite algumas operações como adição, remoção, edição, busca e listagem.
 * <p>A implementação usa um HashMap cujas chaves são o ID de seus respectivos itens.</p>
 * @param <T> O tipo de item que será armazenado.
 */
public class ItemCollection<T extends AbstractItem> implements Serializable {
    private final Map<Integer, T> items;

    public ItemCollection() {
        this.items = new HashMap<>();
    }

    public List<T> list(SortingOption<T> sortingOption) {
        return items.values().stream()
                .sorted(sortingOption.getComparator())
                .toList();
    }

    /**
     * @param sortingOption A opção de ordenamento que será usada.
     * @param getter Função que pega o atributo do item que será filtrado.
     * @param prefix Usado para selecionar quais itens serão retornados.
     * @return Uma lista com os itens que correspondem com a busca.
     */
    public List<T> search(SortingOption<T> sortingOption, Function<T, Object> getter, String prefix) {
        return list(sortingOption).stream()
                .filter(item -> getter.apply(item).toString().toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }

    public Status add(T item) {
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
