package ch.tbz.inventorymgr.activity;

public record AttackResult(
        int damage,
        int remainingHealth,
        boolean completed,
        int goldEarned,
        int experienceEarned
) {
}
