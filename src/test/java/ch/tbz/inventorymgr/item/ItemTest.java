package ch.tbz.inventorymgr.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    @Test
    void itemShouldExposeItsProperties() {
        Item item = new Item("Wooden Axe", 50, 25);

        assertEquals("Wooden Axe", item.getName());
        assertEquals(50, item.getBuyPrice());
        assertEquals(25, item.getSellPrice());
    }

    @Test
    void itemShouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Item(" ", 50, 25));
    }

    @Test
    void itemShouldRejectNegativePrices() {
        assertThrows(IllegalArgumentException.class, () -> new Item("Axe", -1, 25));
        assertThrows(IllegalArgumentException.class, () -> new Item("Axe", 50, -1));
    }
}
