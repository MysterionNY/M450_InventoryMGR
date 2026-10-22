package ch.tbz.inventorymgr.player;

import java.util.Objects;

public class LevelService {

    public void addExperience(Player player, int amount) {
        Objects.requireNonNull(player, "Player cannot be null");
        if (amount < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }

        player.addExperience(amount);
        while (player.getExperience() >= experienceRequiredForNextLevel(player.getLevel())) {
            player.levelUp();
        }
    }

    public int experienceRequiredForNextLevel(int currentLevel) {
        if (currentLevel <= 0) {
            throw new IllegalArgumentException("Level must be greater than zero");
        }

        return Math.addExact(Math.multiplyExact(50, Math.multiplyExact(currentLevel, currentLevel)), 50);
    }
}
