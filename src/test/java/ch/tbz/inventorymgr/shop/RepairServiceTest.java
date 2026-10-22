package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepairServiceTest {

    private RepairService repairService;
    private Tool axe;

    @BeforeEach
    void setUp() {
        repairService = new RepairService();
        axe = new Tool("Wooden Axe", 50, 25, 7, 1, 10);
    }

    @Test
    void brokenToolShouldBeRepairedForFortyPercentOfBuyPrice() {
        for (int hit = 0; hit < 10; hit++) {
            axe.use();
        }
        Player player = new Player(50);

        RepairResult result = repairService.repair(player, axe);

        assertEquals(RepairStatus.REPAIRED, result.status());
        assertEquals(20, result.cost());
        assertEquals(30, player.getMoney());
        assertEquals(10, axe.getDurability());
    }

    @Test
    void partialRepairShouldOnlyChargeForMissingDurability() {
        axe.use();
        axe.use();
        Player player = new Player(50);

        RepairResult result = repairService.repair(player, axe);

        assertEquals(RepairStatus.REPAIRED, result.status());
        assertEquals(4, result.cost());
        assertEquals(46, player.getMoney());
    }

    @Test
    void repairShouldFailWithoutEnoughMoneyAndKeepDurability() {
        axe.use();
        Player player = new Player(0);

        RepairResult result = repairService.repair(player, axe);

        assertEquals(RepairStatus.NOT_ENOUGH_MONEY, result.status());
        assertEquals(9, axe.getDurability());
    }

    @Test
    void fullToolShouldNotCostMoney() {
        Player player = new Player(50);

        RepairResult result = repairService.repair(player, axe);

        assertEquals(RepairStatus.ALREADY_FULL, result.status());
        assertEquals(50, player.getMoney());
    }
}
