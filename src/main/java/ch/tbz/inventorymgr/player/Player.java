package ch.tbz.inventorymgr.player;

import ch.tbz.inventorymgr.inventory.Inventory;

import java.util.Objects;

public class Player {

    private int money;
    private final Inventory inventory;
    private int experience;
    private int level = 1;
    private double goldMultiplier = 1.0;

    public Player(int money) {
        this(money, new Inventory(10));
    }

    public Player(int money, Inventory inventory) {
        if (money < 0) {
            throw new IllegalArgumentException("Money cannot be negative");
        }

        this.money = money;
        this.inventory = Objects.requireNonNull(inventory, "Inventory cannot be null");
    }

    public int getMoney() {
        return money;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }

    public void addMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        money = Math.addExact(money, amount);
    }

    public boolean removeMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (amount > money) {
            return false;
        }

        money -= amount;
        return true;
    }

    void addExperience(int amount) {
        experience += amount;
    }

    void levelUp() {
        level++;
    }

    void setGoldMultiplier(double goldMultiplier) {
        this.goldMultiplier = goldMultiplier;
    }
}
