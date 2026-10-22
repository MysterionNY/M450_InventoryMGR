package ch.tbz.inventorymgr.game;

import ch.tbz.inventorymgr.activity.ActivityService;
import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.item.ToolType;
import ch.tbz.inventorymgr.player.LevelService;
import ch.tbz.inventorymgr.player.Player;
import ch.tbz.inventorymgr.player.UpgradeService;
import ch.tbz.inventorymgr.random.DropService;
import ch.tbz.inventorymgr.shop.ShopService;
import ch.tbz.inventorymgr.shop.RepairService;
import ch.tbz.inventorymgr.shop.InventoryUpgradeService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameMenuTest {

    @Test
    void playerShouldBuyToolAndCompleteActivityThroughMenu() {
        Player player = new Player(100);
        Scanner input = new Scanner("3\n1\n1\n0\n1\n1\n0\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        GameMenu menu = new GameMenu(
                player,
                new ShopService(),
                new ActivityService(),
                new LevelService(),
                new UpgradeService(),
                new RepairService(),
                new InventoryUpgradeService(),
                new DropService(() -> 0.99),
                input,
                output
        );

        menu.run();

        String console = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(console.contains("Holzaxt wurde gekauft."));
        assertTrue(console.contains("Baum abgeschlossen! +15 Gold, +30 XP"));
        assertTrue(console.contains("Fund: Schatztruhe (TREASURE)"));
        assertEquals(65, player.getMoney());
        assertEquals(30, player.getExperience());
        assertEquals(2, player.getInventory().getItemCount());
    }

    @Test
    void playerShouldRepairDamagedToolThroughMenu() {
        Player player = new Player(20);
        Tool axe = new Tool("Holzaxt", 50, 25, ToolType.AXE, 7, 1, 10);
        for (int hit = 0; hit < 5; hit++) {
            axe.use();
        }
        player.getInventory().addItem(axe);
        Scanner input = new Scanner("3\n3\n1\n0\n0\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        createMenu(player, input, bytes).run();

        assertEquals(10, player.getMoney());
        assertEquals(10, axe.getDurability());
        assertTrue(bytes.toString(StandardCharsets.UTF_8)
                .contains("Holzaxt wurde für 10 Gold vollständig repariert."));
    }

    @Test
    void playerShouldBuyGoldUpgradeThroughMenu() {
        Player player = new Player(150);
        Scanner input = new Scanner("3\n4\n1\n0\n0\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        createMenu(player, input, bytes).run();

        assertEquals(0, player.getMoney());
        assertEquals(1.25, player.getGoldMultiplier());
        assertTrue(bytes.toString(StandardCharsets.UTF_8)
                .contains("Gold-Multiplikator auf 1,25x verbessert."));
    }

    @Test
    void playerShouldBuyInventoryUpgradeThroughMenu() {
        Player player = new Player(200);
        Scanner input = new Scanner("3\n5\n1\n0\n0\n");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        createMenu(player, input, bytes).run();

        assertEquals(0, player.getMoney());
        assertEquals(15, player.getInventory().getCapacity());
        assertTrue(bytes.toString(StandardCharsets.UTF_8)
                .contains("Inventar auf 15 Slots erweitert."));
    }

    private GameMenu createMenu(Player player, Scanner input, ByteArrayOutputStream bytes) {
        return new GameMenu(
                player,
                new ShopService(),
                new ActivityService(),
                new LevelService(),
                new UpgradeService(),
                new RepairService(),
                new InventoryUpgradeService(),
                new DropService(() -> 0.50),
                input,
                new PrintStream(bytes, true, StandardCharsets.UTF_8)
        );
    }
}
