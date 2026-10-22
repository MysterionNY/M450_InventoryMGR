package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryUpgradeServiceTest {

    private final InventoryUpgradeService upgradeService = new InventoryUpgradeService();

    @Test
    void playerShouldBuyFirstInventoryUpgrade() {
        Player player = new Player(300);

        assertTrue(upgradeService.buyNextUpgrade(player));

        assertEquals(100, player.getMoney());
        assertEquals(15, player.getInventory().getCapacity());
    }

    @Test
    void upgradeShouldFailWithoutEnoughMoney() {
        Player player = new Player(199);

        assertFalse(upgradeService.buyNextUpgrade(player));

        assertEquals(199, player.getMoney());
        assertEquals(10, player.getInventory().getCapacity());
    }

    @Test
    void inventoryShouldStopAtMaximumCapacity() {
        Player player = new Player(1000);

        assertTrue(upgradeService.buyNextUpgrade(player));
        assertTrue(upgradeService.buyNextUpgrade(player));

        assertTrue(upgradeService.isMaxLevel(player));
        assertFalse(upgradeService.buyNextUpgrade(player));
        assertEquals(20, player.getInventory().getCapacity());
        assertEquals(300, player.getMoney());
    }
}
