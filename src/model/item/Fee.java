package model.item;

import java.math.BigDecimal;

public class Fee extends AbstractItem {
    private String name;
    private BigDecimal percentage;

    public Fee(int id, String name, BigDecimal percentage) {
        super(id);
        if (name == null || name.isBlank()) {
            this.name = "Fee with no name";
        } else {
            this.name = name;
        }
        if (percentage == null || percentage.doubleValue() <= -1) {
            this.percentage = BigDecimal.ONE;
        } else {
            this.percentage = percentage;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) { return; }
        this.name = name;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        if (percentage == null || percentage.doubleValue() <= -1) { return; }
        this.percentage = percentage;
    }

    public Fee copy() {
        return new Fee(
                getId(),
                getName().substring(0),
                getPercentage().multiply(BigDecimal.ONE)
        );
    }

    @Override
    public String toString() {
        return "(" + getId() + ")   " + getName() + "   " + getPercentage().multiply(BigDecimal.valueOf(100)).intValue() + "%";
    }
}
