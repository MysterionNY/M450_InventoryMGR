package ch.tbz.inventorymgr.inventory;

import ch.tbz.inventorymgr.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Inventory {

    private int capacity;
    private final List<Item> items = new ArrayList<>();

    public Inventory(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

        this.capacity = capacity;
    }

    public boolean addItem(Item item) {
        Objects.requireNonNull(item, "Item cannot be null");
        if (isFull()) {
            return false;
        }

        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        Objects.requireNonNull(item, "Item cannot be null");
        return items.remove(item);
    }

    public boolean contains(Item item) {
        return items.contains(item);
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void expandCapacity(int additionalSlots) {
        if (additionalSlots <= 0) {
            throw new IllegalArgumentException("Additional slots must be greater than zero");
        }
        capacity = Math.addExact(capacity, additionalSlots);
    }

    public int getItemCount() {
        return items.size();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
}
