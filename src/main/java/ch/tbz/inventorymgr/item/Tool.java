package ch.tbz.inventorymgr.item;

public class Tool extends Item {

    private final ToolType type;
    private final int damage;
    private final int level;
    private final int maxDurability;
    private int durability;

    public Tool(String name, int buyPrice, int sellPrice, int damage, int level, int durability) {
        this(name, buyPrice, sellPrice, ToolType.GENERIC, damage, level, durability);
    }

    public Tool(String name, int buyPrice, int sellPrice, ToolType type,
                int damage, int level, int durability) {
        super(name, buyPrice, sellPrice);
        if (type == null) {
            throw new IllegalArgumentException("Tool type cannot be null");
        }
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be greater than zero");
        }
        if (level <= 0) {
            throw new IllegalArgumentException("Level must be greater than zero");
        }
        if (durability <= 0) {
            throw new IllegalArgumentException("Durability must be greater than zero");
        }

        this.type = type;
        this.damage = damage;
        this.level = level;
        this.maxDurability = durability;
        this.durability = durability;
    }

    public int use() {
        if (isBroken()) {
            throw new IllegalStateException("Tool is broken");
        }

        durability--;
        return damage;
    }

    public int getDamage() {
        return damage;
    }

    public ToolType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public int getMissingDurability() {
        return maxDurability - durability;
    }

    public void repairToFull() {
        durability = maxDurability;
    }

    public boolean isBroken() {
        return durability == 0;
    }
}
