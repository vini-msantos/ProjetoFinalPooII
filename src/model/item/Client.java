package model.item;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Client extends AbstractItem {
    private String name;
    private String phoneNumber;

    public Client(int id, @NotNull String name) {
        this(id, name, null);
    }

    public Client(int id, @NotNull String name, String phoneNumber) {
        super(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        if (phoneNumber.isBlank()) { return; }
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        if (name.isBlank()) { return; }
        this.name = name;
    }

    @Override
    public String toString() {
        String contact = Objects.requireNonNullElse(phoneNumber, "No phone number");
        return "(" +getId() + ")   " + getName() + "   " + contact;
    }
}
