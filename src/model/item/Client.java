package model.item;

import java.util.Objects;

public class Client extends AbstractItem {
    private String name;
    private String phoneNumber;

    public Client(int id, String name) {
        this(id, name, null);
    }

    public Client(int id, String name, String phoneNumber) {
        super(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank()) { return; }
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isBlank()) { return; }
        this.name = name;
    }

    @Override
    public String toString() {
        String contact = Objects.requireNonNullElse(phoneNumber, "No phone number");
        return "(" +getId() + ")   " + getName() + "   " + contact;
    }
}
