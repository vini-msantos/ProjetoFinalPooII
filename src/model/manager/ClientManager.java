package model.manager;

import model.item.Bill;
import model.item.Client;
import model.sorting.ClientSortBy;
import model.sorting.SortingOption;
import model.util.Serializer;
import model.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ClientManager extends AbstractManager<Client> {
    private static final String filePath = "data/clientManager.ser";
    private static ClientManager instance;

    protected ClientManager() {
        super(new SortingOption<>(ClientSortBy.NAME, false));
    }
    
    public static ClientManager getInstance() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public static ClientManager load() {
        ClientManager manager = Serializer.load(filePath);
        if (manager == null) {
            manager = new ClientManager();
        }

        return manager;
    }

    public Status createClient(@NotNull String name, String phoneNumber) {
        Client client = new Client(getIdCounter(), name, phoneNumber);
        return add(client);
    }

    @Override
    public Status save() {
        return Serializer.save(this, filePath);
    }

    @Override
    public List<Client> searchName(@Nullable String prefix) {
        return search(Client::getName, Objects.requireNonNullElse(prefix, ""));
    }
}
