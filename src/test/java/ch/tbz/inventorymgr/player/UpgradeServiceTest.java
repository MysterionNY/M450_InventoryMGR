package ch.tbz.inventorymgr.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpgradeServiceTest {

    private final UpgradeService upgradeService = new UpgradeService();

    @Test
    void playerShouldBuyNextGoldMultiplier() {
        Player player = new Player(200);

        assertTrue(upgradeService.buyNextGoldMultiplier(player));
        assertEquals(50, player.getMoney());
        assertEquals(1.25, player.getGoldMultiplier());
        assertEquals(300, upgradeService.getNextUpgradeCost(player));
    }

    @Test
    void upgradeShouldFailWithoutEnoughMoney() {
        Player player = new Player(149);

        assertFalse(upgradeService.buyNextGoldMultiplier(player));
        assertEquals(149, player.getMoney());
        assertEquals(1.0, player.getGoldMultiplier());
    }

    @Test
    void multiplierShouldStopAtMaximumLevel() {
        Player player = new Player(5000);

        assertTrue(upgradeService.buyNextGoldMultiplier(player));
        assertTrue(upgradeService.buyNextGoldMultiplier(player));
        assertTrue(upgradeService.buyNextGoldMultiplier(player));
        assertTrue(upgradeService.buyNextGoldMultiplier(player));

        assertTrue(upgradeService.isMaxLevel(player));
        assertFalse(upgradeService.buyNextGoldMultiplier(player));
        assertEquals(3.0, player.getGoldMultiplier());
        assertEquals(0, upgradeService.getNextUpgradeCost(player));
    }
}
