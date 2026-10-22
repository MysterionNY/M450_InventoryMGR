package ch.tbz.inventorymgr.random;

import ch.tbz.inventorymgr.item.Item;
import ch.tbz.inventorymgr.player.Player;

import java.util.Objects;

public class DropService {

    private final RandomGenerator randomGenerator;

    public DropService(RandomGenerator randomGenerator) {
        this.randomGenerator = Objects.requireNonNull(randomGenerator, "RandomGenerator cannot be null");
    }

    public DropRarity rollDrop() {
        double value = randomGenerator.nextDouble();
        if (!Double.isFinite(value) || value < 0 || value >= 1) {
            throw new IllegalStateException("Random value must be between 0 (inclusive) and 1 (exclusive)");
        }
        if (value < 0.90) {
            return DropRarity.NORMAL;
        }
        if (value < 0.98) {
            return DropRarity.RARE;
        }
        return DropRarity.TREASURE;
    }

    public DropResult awardDrop(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");

        DropRarity rarity = rollDrop();
        Item item = createDropItem(rarity);
        boolean added = player.getInventory().addItem(item);
        return new DropResult(rarity, item, added);
    }

    private Item createDropItem(DropRarity rarity) {
        return switch (rarity) {
            case NORMAL -> new Item("Ressource", 0, 5);
            case RARE -> new Item("Rubin", 0, 40);
            case TREASURE -> new Item("Schatztruhe", 0, 100);
        };
    }
}
