package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.player.Player;

import java.util.Objects;

public class InventoryUpgradeService {

    private static final int[] CAPACITIES = {10, 15, 20};
    private static final int[] COSTS = {200, 500};

    public boolean buyNextUpgrade(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        int index = currentIndex(player);
        if (index >= COSTS.length || !player.removeMoney(COSTS[index])) {
            return false;
        }

        int additionalSlots = CAPACITIES[index + 1] - CAPACITIES[index];
        player.getInventory().expandCapacity(additionalSlots);
        return true;
    }

    public int getNextUpgradeCost(Player player) {
        int index = currentIndex(Objects.requireNonNull(player, "Player cannot be null"));
        return index >= COSTS.length ? 0 : COSTS[index];
    }

    public int getNextCapacity(Player player) {
        int index = currentIndex(Objects.requireNonNull(player, "Player cannot be null"));
        return index >= COSTS.length ? CAPACITIES[index] : CAPACITIES[index + 1];
    }

    public boolean isMaxLevel(Player player) {
        return currentIndex(Objects.requireNonNull(player, "Player cannot be null")) >= COSTS.length;
    }

    private int currentIndex(Player player) {
        int capacity = player.getInventory().getCapacity();
        for (int index = 0; index < CAPACITIES.length; index++) {
            if (capacity == CAPACITIES[index]) {
                return index;
            }
        }
        throw new IllegalStateException("Unknown inventory capacity: " + capacity);
    }
}
