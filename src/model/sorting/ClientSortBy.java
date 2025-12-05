package model.sorting;
import model.item.Client;

import java.util.Comparator;

public enum ClientSortBy implements Sorter<Client> {
    ID,
    NAME;

    @Override
    public Comparator<Client> sortAscending() {
        return switch (this) {
            case ID -> Comparator.comparingInt(Client::getId);
            case NAME -> Comparator.comparing(Client::getName);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ID -> "ID";
            case NAME -> "Name";
        };
    }
}
