package ch.tbz.inventorymgr.economy;

public class RewardCalculator {

    public int calculateGold(int baseReward, double goldMultiplier, double bonusPercentage) {
        if (baseReward < 0) {
            throw new IllegalArgumentException("Base reward cannot be negative");
        }
        if (!Double.isFinite(goldMultiplier) || goldMultiplier < 0) {
            throw new IllegalArgumentException("Gold multiplier must be finite and non-negative");
        }
        if (!Double.isFinite(bonusPercentage) || bonusPercentage < 0) {
            throw new IllegalArgumentException("Bonus percentage must be finite and non-negative");
        }

        return (int) Math.round(baseReward * goldMultiplier * (1 + bonusPercentage / 100));
    }
}
