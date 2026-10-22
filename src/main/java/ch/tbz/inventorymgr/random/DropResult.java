package ch.tbz.inventorymgr.random;

import ch.tbz.inventorymgr.item.Item;

public record DropResult(DropRarity rarity, Item item, boolean addedToInventory) {
}
