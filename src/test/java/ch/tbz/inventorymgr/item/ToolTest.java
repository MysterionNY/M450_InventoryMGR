package ch.tbz.inventorymgr.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolTest {

    @Test
    void usingToolShouldDealDamageAndReduceDurability() {
        Tool tool = new Tool("Stone Pickaxe", 100, 50, 7, 1, 2);

        assertEquals(7, tool.use());
        assertEquals(1, tool.getDurability());
        assertEquals(2, tool.getMaxDurability());
        assertEquals(1, tool.getMissingDurability());
    }

    @Test
    void toolShouldBreakAtZeroDurability() {
        Tool tool = new Tool("Stone Pickaxe", 100, 50, 7, 1, 1);

        tool.use();

        assertTrue(tool.isBroken());
        assertThrows(IllegalStateException.class, tool::use);

        tool.repairToFull();

        assertEquals(1, tool.getDurability());
        assertEquals(7, tool.use());
    }

    @Test
    void toolShouldRejectInvalidCombatValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new Tool("Pickaxe", 100, 50, 0, 1, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Tool("Pickaxe", 100, 50, 5, 0, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Tool("Pickaxe", 100, 50, 5, 1, 0));
    }
}
