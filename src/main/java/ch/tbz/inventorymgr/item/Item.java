package ch.tbz.inventorymgr.item;

import java.util.Objects;

public class Item {

    private final String name;
    private final int buyPrice;
    private final int sellPrice;

    public Item(String name, int buyPrice, int sellPrice) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (buyPrice < 0 || sellPrice < 0) {
            throw new IllegalArgumentException("Prices cannot be negative");
        }

        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Item item)) {
            return false;
        }
        return buyPrice == item.buyPrice
                && sellPrice == item.sellPrice
                && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, buyPrice, sellPrice);
    }
}
