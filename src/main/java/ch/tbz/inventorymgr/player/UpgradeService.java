package ch.tbz.inventorymgr.player;

import java.util.Objects;

public class UpgradeService {

    private static final double[] MULTIPLIERS = {1.0, 1.25, 1.5, 2.0, 3.0};
    private static final int[] COSTS = {150, 300, 600, 1200};

    public boolean buyNextGoldMultiplier(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        int index = currentIndex(player);
        if (index >= COSTS.length || !player.removeMoney(COSTS[index])) {
            return false;
        }

        player.setGoldMultiplier(MULTIPLIERS[index + 1]);
        return true;
    }

    public int getNextUpgradeCost(Player player) {
        int index = currentIndex(Objects.requireNonNull(player, "Player cannot be null"));
        return index >= COSTS.length ? 0 : COSTS[index];
    }

    public double getNextMultiplier(Player player) {
        int index = currentIndex(Objects.requireNonNull(player, "Player cannot be null"));
        return index >= COSTS.length ? player.getGoldMultiplier() : MULTIPLIERS[index + 1];
    }

    public boolean isMaxLevel(Player player) {
        return currentIndex(Objects.requireNonNull(player, "Player cannot be null")) >= COSTS.length;
    }

    private int currentIndex(Player player) {
        for (int index = 0; index < MULTIPLIERS.length; index++) {
            if (Double.compare(player.getGoldMultiplier(), MULTIPLIERS[index]) == 0) {
                return index;
            }
        }
        throw new IllegalStateException("Unknown gold multiplier");
    }
}
