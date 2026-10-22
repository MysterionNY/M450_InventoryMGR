package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.item.Item;
import ch.tbz.inventorymgr.player.Player;

import java.util.Objects;

public class ShopService {

    public boolean buyItem(Player player, Item item) {
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        if (player.getMoney() < item.getBuyPrice() || player.getInventory().isFull()) {
            return false;
        }

        player.getInventory().addItem(item);
        player.removeMoney(item.getBuyPrice());
        return true;
    }

    public boolean sellItem(Player player, Item item) {
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        if (!player.getInventory().removeItem(item)) {
            return false;
        }

        player.addMoney(item.getSellPrice());
        return true;
    }
}
