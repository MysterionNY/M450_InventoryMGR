package ch.tbz.inventorymgr.activity;

import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.item.ToolType;
import ch.tbz.inventorymgr.inventory.Inventory;
import ch.tbz.inventorymgr.player.Player;
import ch.tbz.inventorymgr.player.UpgradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityServiceTest {

    private ActivityService activityService;
    private Player player;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService();
        player = new Player(0, new Inventory(5));
    }

    @Test
    void attackShouldDamageActivityAndTool() {
        Activity stone = new Activity("Stone", 30, 1);
        Tool pickaxe = new Tool("Stone Pickaxe", 100, 50, 7, 1, 10);

        AttackResult result = activityService.attack(player, stone, pickaxe);

        assertEquals(7, result.damage());
        assertEquals(23, result.remainingHealth());
        assertFalse(result.completed());
        assertEquals(0, result.goldEarned());
        assertEquals(0, result.experienceEarned());
        assertEquals(9, pickaxe.getDurability());
    }

    @Test
    void finalAttackShouldNotReduceHealthBelowZero() {
        Activity tree = new Activity("Tree", 5, 1);
        Tool axe = new Tool("Axe", 50, 25, 7, 1, 10);

        AttackResult result = activityService.attack(player, tree, axe);

        assertEquals(0, result.remainingHealth());
        assertTrue(result.completed());
    }

    @Test
    void attackShouldRejectToolWithInsufficientLevel() {
        Activity ironOre = new Activity("Iron Ore", 100, 2);
        Tool pickaxe = new Tool("Stone Pickaxe", 100, 50, 7, 1, 10);

        assertThrows(IllegalArgumentException.class,
                () -> activityService.attack(player, ironOre, pickaxe));
        assertEquals(100, ironOre.getRemainingHealth());
        assertEquals(10, pickaxe.getDurability());
    }

    @Test
    void completedActivityShouldNotBeAttackedAgain() {
        Activity tree = new Activity("Tree", 5, 1);
        Tool axe = new Tool("Axe", 50, 25, 7, 1, 10);
        activityService.attack(player, tree, axe);

        assertThrows(IllegalStateException.class, () -> activityService.attack(player, tree, axe));
        assertEquals(9, axe.getDurability());
    }

    @Test
    void attackShouldRejectBrokenTool() {
        Activity stone = new Activity("Stone", 30, 1);
        Tool pickaxe = new Tool("Stone Pickaxe", 100, 50, 7, 1, 1);
        pickaxe.use();

        assertThrows(IllegalStateException.class,
                () -> activityService.attack(player, stone, pickaxe));
        assertEquals(30, stone.getRemainingHealth());
    }

    @Test
    void activityShouldRejectPlayerWithInsufficientLevel() {
        Activity mine = new Activity("Mine", 30, 1, 2, 20, 10);
        Tool pickaxe = new Tool("Stone Pickaxe", 100, 50, 7, 1, 10);

        assertThrows(IllegalArgumentException.class,
                () -> activityService.attack(player, mine, pickaxe));
        assertEquals(30, mine.getRemainingHealth());
        assertEquals(10, pickaxe.getDurability());
    }

    @Test
    void activityShouldRejectWrongToolType() {
        Activity tree = new Activity("Tree", 30, ToolType.AXE, 1, 1, 10, 10);
        Tool pickaxe = new Tool("Pickaxe", 100, 50, ToolType.PICKAXE, 7, 1, 10);

        assertThrows(IllegalArgumentException.class,
                () -> activityService.attack(player, tree, pickaxe));
        assertEquals(30, tree.getRemainingHealth());
        assertEquals(10, pickaxe.getDurability());
    }

    @Test
    void completingActivityShouldRewardPlayerAndApplyLevelUp() {
        Activity treasure = new Activity("Treasure", 5, 1, 1, 100, 100);
        Tool shovel = new Tool("Shovel", 50, 25, 5, 1, 10);

        AttackResult result = activityService.attack(player, treasure, shovel);

        assertTrue(result.completed());
        assertEquals(100, result.goldEarned());
        assertEquals(100, result.experienceEarned());
        assertEquals(100, player.getMoney());
        assertEquals(100, player.getExperience());
        assertEquals(2, player.getLevel());
    }

    @Test
    void goldUpgradeShouldIncreaseActivityReward() {
        player.addMoney(150);
        new UpgradeService().buyNextGoldMultiplier(player);
        Activity tree = new Activity("Tree", 5, 1, 1, 100, 0);
        Tool axe = new Tool("Axe", 50, 25, 5, 1, 10);

        AttackResult result = activityService.attack(player, tree, axe);

        assertEquals(125, result.goldEarned());
        assertEquals(125, player.getMoney());
    }

    @Test
    void durabilityCheckShouldCalculateRequiredHits() {
        Activity tree = new Activity("Tree", 30, 1);
        Tool weakAxe = new Tool("Weak Axe", 10, 5, 7, 1, 4);
        Tool sufficientAxe = new Tool("Axe", 50, 25, 10, 1, 3);

        assertEquals(5, activityService.requiredHits(tree, weakAxe));
        assertFalse(activityService.hasEnoughDurability(tree, weakAxe));
        assertTrue(activityService.hasEnoughDurability(tree, sufficientAxe));
    }
}
