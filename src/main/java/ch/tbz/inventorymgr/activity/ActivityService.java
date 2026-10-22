package ch.tbz.inventorymgr.activity;

import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.item.ToolType;
import ch.tbz.inventorymgr.economy.RewardCalculator;
import ch.tbz.inventorymgr.player.LevelService;
import ch.tbz.inventorymgr.player.Player;

import java.util.Objects;

public class ActivityService {

    private final RewardCalculator rewardCalculator;
    private final LevelService levelService;

    public ActivityService() {
        this(new RewardCalculator(), new LevelService());
    }

    public ActivityService(RewardCalculator rewardCalculator, LevelService levelService) {
        this.rewardCalculator = Objects.requireNonNull(rewardCalculator, "RewardCalculator cannot be null");
        this.levelService = Objects.requireNonNull(levelService, "LevelService cannot be null");
    }

    public AttackResult attack(Player player, Activity activity, Tool tool) {
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(activity, "Activity cannot be null");
        Objects.requireNonNull(tool, "Tool cannot be null");

        if (activity.isCompleted()) {
            throw new IllegalStateException("Activity is already completed");
        }
        if (tool.getLevel() < activity.getRequiredToolLevel()) {
            throw new IllegalArgumentException("Tool level is too low");
        }
        if (activity.getRequiredToolType() != ToolType.GENERIC
                && tool.getType() != activity.getRequiredToolType()) {
            throw new IllegalArgumentException("Wrong tool type");
        }
        if (player.getLevel() < activity.getRequiredPlayerLevel()) {
            throw new IllegalArgumentException("Player level is too low");
        }

        int damage = tool.use();
        activity.receiveDamage(damage);

        int goldEarned = 0;
        int experienceEarned = 0;
        if (activity.isCompleted()) {
            goldEarned = rewardCalculator.calculateGold(
                    activity.getBaseGoldReward(), player.getGoldMultiplier(), 0);
            experienceEarned = activity.getExperienceReward();
            player.addMoney(goldEarned);
            levelService.addExperience(player, experienceEarned);
        }

        return new AttackResult(
                damage,
                activity.getRemainingHealth(),
                activity.isCompleted(),
                goldEarned,
                experienceEarned
        );
    }

    public int requiredHits(Activity activity, Tool tool) {
        Objects.requireNonNull(activity, "Activity cannot be null");
        Objects.requireNonNull(tool, "Tool cannot be null");
        return (int) Math.ceil((double) activity.getRemainingHealth() / tool.getDamage());
    }

    public boolean hasEnoughDurability(Activity activity, Tool tool) {
        return tool.getDurability() >= requiredHits(activity, tool);
    }
}
