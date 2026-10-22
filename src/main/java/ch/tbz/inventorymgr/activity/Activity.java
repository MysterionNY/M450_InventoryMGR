package ch.tbz.inventorymgr.activity;

import ch.tbz.inventorymgr.item.ToolType;

public class Activity {

    private final String name;
    private final int maxHealth;
    private final ToolType requiredToolType;
    private final int requiredToolLevel;
    private final int requiredPlayerLevel;
    private final int baseGoldReward;
    private final int experienceReward;
    private int remainingHealth;

    public Activity(String name, int maxHealth, int requiredToolLevel) {
        this(name, maxHealth, ToolType.GENERIC, requiredToolLevel, 1, 0, 0);
    }

    public Activity(String name, int maxHealth, int requiredToolLevel,
                    int requiredPlayerLevel, int baseGoldReward, int experienceReward) {
        this(name, maxHealth, ToolType.GENERIC, requiredToolLevel,
                requiredPlayerLevel, baseGoldReward, experienceReward);
    }

    public Activity(String name, int maxHealth, ToolType requiredToolType, int requiredToolLevel,
                    int requiredPlayerLevel, int baseGoldReward, int experienceReward) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("Health must be greater than zero");
        }
        if (requiredToolType == null) {
            throw new IllegalArgumentException("Required tool type cannot be null");
        }
        if (requiredToolLevel <= 0) {
            throw new IllegalArgumentException("Required tool level must be greater than zero");
        }
        if (requiredPlayerLevel <= 0) {
            throw new IllegalArgumentException("Required player level must be greater than zero");
        }
        if (baseGoldReward < 0 || experienceReward < 0) {
            throw new IllegalArgumentException("Rewards cannot be negative");
        }

        this.name = name;
        this.maxHealth = maxHealth;
        this.requiredToolType = requiredToolType;
        this.requiredToolLevel = requiredToolLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.baseGoldReward = baseGoldReward;
        this.experienceReward = experienceReward;
        this.remainingHealth = maxHealth;
    }

    void receiveDamage(int damage) {
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be greater than zero");
        }

        remainingHealth = Math.max(0, remainingHealth - damage);
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getRemainingHealth() {
        return remainingHealth;
    }

    public int getRequiredToolLevel() {
        return requiredToolLevel;
    }

    public ToolType getRequiredToolType() {
        return requiredToolType;
    }

    public int getRequiredPlayerLevel() {
        return requiredPlayerLevel;
    }

    public int getBaseGoldReward() {
        return baseGoldReward;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public boolean isCompleted() {
        return remainingHealth == 0;
    }
}
