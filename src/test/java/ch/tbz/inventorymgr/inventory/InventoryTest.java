package ch.tbz.inventorymgr.inventory;

import ch.tbz.inventorymgr.item.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryTest {

    private final Item axe = new Item("Wooden Axe", 50, 25);

    @Test
    void itemShouldBeAddedAndRemoved() {
        Inventory inventory = new Inventory(2);

        assertTrue(inventory.addItem(axe));
        assertTrue(inventory.contains(axe));
        assertTrue(inventory.removeItem(axe));
        assertFalse(inventory.contains(axe));
    }

    @Test
    void itemShouldNotBeAddedWhenInventoryIsFull() {
        Inventory inventory = new Inventory(1);
        inventory.addItem(axe);

        assertFalse(inventory.addItem(new Item("Pickaxe", 80, 40)));
        assertTrue(inventory.isFull());
        assertEquals(1, inventory.getItemCount());
    }

    @Test
    void inventoryShouldRejectInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new Inventory(0));
    }

    @Test
    void exposedItemListShouldNotBeModifiable() {
        Inventory inventory = new Inventory(1);

        assertThrows(UnsupportedOperationException.class, () -> inventory.getItems().add(axe));
    }
}
