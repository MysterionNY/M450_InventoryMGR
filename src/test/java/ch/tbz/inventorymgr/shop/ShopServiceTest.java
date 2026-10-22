package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.inventory.Inventory;
import ch.tbz.inventorymgr.item.Item;
import ch.tbz.inventorymgr.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopServiceTest {

    private final Item axe = new Item("Wooden Axe", 50, 25);
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        shopService = new ShopService();
    }

    @Test
    void playerShouldBuyItem() {
        Player player = new Player(100, new Inventory(2));

        assertTrue(shopService.buyItem(player, axe));
        assertEquals(50, player.getMoney());
        assertTrue(player.getInventory().contains(axe));
    }

    @Test
    void playerShouldNotBuyItemWithoutEnoughMoney() {
        Player player = new Player(49, new Inventory(2));

        assertFalse(shopService.buyItem(player, axe));
        assertEquals(49, player.getMoney());
        assertFalse(player.getInventory().contains(axe));
    }

    @Test
    void playerShouldNotBuyItemWhenInventoryIsFull() {
        Inventory inventory = new Inventory(1);
        inventory.addItem(new Item("Pickaxe", 80, 40));
        Player player = new Player(100, inventory);

        assertFalse(shopService.buyItem(player, axe));
        assertEquals(100, player.getMoney());
        assertFalse(player.getInventory().contains(axe));
    }

    @Test
    void playerShouldSellOwnedItem() {
        Player player = new Player(100, new Inventory(2));
        player.getInventory().addItem(axe);

        assertTrue(shopService.sellItem(player, axe));
        assertEquals(125, player.getMoney());
        assertFalse(player.getInventory().contains(axe));
    }

    @Test
    void playerShouldNotSellItemThatIsNotOwned() {
        Player player = new Player(100, new Inventory(2));

        assertFalse(shopService.sellItem(player, axe));
        assertEquals(100, player.getMoney());
    }
}
