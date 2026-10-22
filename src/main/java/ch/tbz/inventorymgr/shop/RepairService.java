package ch.tbz.inventorymgr.shop;

import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.player.Player;

import java.util.Objects;

public class RepairService {

    private static final double FULL_REPAIR_PRICE_FACTOR = 0.40;

    public int calculateRepairCost(Tool tool) {
        Objects.requireNonNull(tool, "Tool cannot be null");
        if (tool.getMissingDurability() == 0) {
            return 0;
        }

        double missingShare = (double) tool.getMissingDurability() / tool.getMaxDurability();
        return Math.max(1, (int) Math.ceil(tool.getBuyPrice() * FULL_REPAIR_PRICE_FACTOR * missingShare));
    }

    public RepairResult repair(Player player, Tool tool) {
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(tool, "Tool cannot be null");

        int cost = calculateRepairCost(tool);
        if (cost == 0) {
            return new RepairResult(RepairStatus.ALREADY_FULL, 0);
        }
        if (!player.removeMoney(cost)) {
            return new RepairResult(RepairStatus.NOT_ENOUGH_MONEY, cost);
        }

        tool.repairToFull();
        return new RepairResult(RepairStatus.REPAIRED, cost);
    }
}
