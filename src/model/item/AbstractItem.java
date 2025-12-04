package model.item;

import java.io.Serializable;

public abstract class AbstractItem implements Serializable {
    final private int id;

    public AbstractItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
