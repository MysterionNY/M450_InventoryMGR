package ch.tbz.inventorymgr.game;

import ch.tbz.inventorymgr.activity.Activity;
import ch.tbz.inventorymgr.activity.ActivityService;
import ch.tbz.inventorymgr.activity.AttackResult;
import ch.tbz.inventorymgr.item.Item;
import ch.tbz.inventorymgr.item.Tool;
import ch.tbz.inventorymgr.item.ToolType;
import ch.tbz.inventorymgr.player.LevelService;
import ch.tbz.inventorymgr.player.Player;
import ch.tbz.inventorymgr.player.UpgradeService;
import ch.tbz.inventorymgr.random.DropResult;
import ch.tbz.inventorymgr.random.DropService;
import ch.tbz.inventorymgr.shop.ShopService;
import ch.tbz.inventorymgr.shop.RepairResult;
import ch.tbz.inventorymgr.shop.RepairService;
import ch.tbz.inventorymgr.shop.RepairStatus;
import ch.tbz.inventorymgr.shop.InventoryUpgradeService;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class GameMenu {

    private final Player player;
    private final ShopService shopService;
    private final ActivityService activityService;
    private final LevelService levelService;
    private final UpgradeService upgradeService;
    private final RepairService repairService;
    private final InventoryUpgradeService inventoryUpgradeService;
    private final DropService dropService;
    private final Scanner scanner;
    private final PrintStream output;

    public GameMenu(Player player, ShopService shopService, ActivityService activityService,
                    LevelService levelService, UpgradeService upgradeService,
                    RepairService repairService, InventoryUpgradeService inventoryUpgradeService,
                    DropService dropService,
                    Scanner scanner, PrintStream output) {
        this.player = Objects.requireNonNull(player);
        this.shopService = Objects.requireNonNull(shopService);
        this.activityService = Objects.requireNonNull(activityService);
        this.levelService = Objects.requireNonNull(levelService);
        this.upgradeService = Objects.requireNonNull(upgradeService);
        this.repairService = Objects.requireNonNull(repairService);
        this.inventoryUpgradeService = Objects.requireNonNull(inventoryUpgradeService);
        this.dropService = Objects.requireNonNull(dropService);
        this.scanner = Objects.requireNonNull(scanner);
        this.output = Objects.requireNonNull(output);
    }

    public void run() {
        output.println("Willkommen beim Inventory Simulator!");
        boolean running = true;
        while (running) {
            printMainMenu();
            switch (readChoice()) {
                case 1 -> showActivities();
                case 2 -> showInventory();
                case 3 -> showShop();
                case 4 -> showCharacter();
                case 0 -> running = false;
                default -> output.println("Ungültige Auswahl.");
            }
        }
        output.println("Spiel beendet.");
    }

    private void printMainMenu() {
        output.println();
        output.println("================================");
        output.println("       INVENTORY SIMULATOR");
        output.println("================================");
        output.printf("Gold: %d | Level: %d | XP: %d | Goldbonus: %.2fx%n",
                player.getMoney(), player.getLevel(), player.getExperience(),
                player.getGoldMultiplier());
        output.println("[1] Arbeiten");
        output.println("[2] Inventar");
        output.println("[3] Shop");
        output.println("[4] Charakter");
        output.println("[0] Beenden");
    }

    private void showActivities() {
        output.println();
        output.println("======= AKTIVITÄTEN =======");
        output.println("[1] Baum fällen      Axt L1, Spieler L1");
        output.println("[2] Stein abbauen    Spitzhacke L1, Spieler L1");
        output.println("[3] Eisenerz abbauen Spitzhacke L2, Spieler L2");
        output.println("[4] Hartholz fällen   Axt L2, Spieler L3");
        output.println("[5] Kristall abbauen Spitzhacke L3, Spieler L4");
        output.println("[0] Zurück");

        Activity activity = createActivity(readChoice());
        if (activity != null) {
            performActivity(activity);
        }
    }

    private Activity createActivity(int choice) {
        return switch (choice) {
            case 1 -> new Activity("Baum", 30, ToolType.AXE, 1, 1, 15, 30);
            case 2 -> new Activity("Stein", 40, ToolType.PICKAXE, 1, 1, 25, 40);
            case 3 -> new Activity("Eisenerz", 80, ToolType.PICKAXE, 2, 2, 70, 80);
            case 4 -> new Activity("Hartholz", 110, ToolType.AXE, 2, 3, 110, 120);
            case 5 -> new Activity("Kristall", 180, ToolType.PICKAXE, 3, 4, 220, 180);
            default -> null;
        };
    }

    private void performActivity(Activity activity) {
        Tool tool = findBestTool(activity);
        if (tool == null) {
            output.printf("Du brauchst dafür ein intaktes Werkzeug vom Typ %s auf Level %d.%n",
                    toolTypeName(activity.getRequiredToolType()), activity.getRequiredToolLevel());
            return;
        }
        if (player.getLevel() < activity.getRequiredPlayerLevel()) {
            output.printf("Dafür benötigst du Spieler-Level %d.%n", activity.getRequiredPlayerLevel());
            return;
        }

        int requiredHits = activityService.requiredHits(activity, tool);
        if (!activityService.hasEnoughDurability(activity, tool)) {
            output.printf("%s hat nur noch %d Haltbarkeit, benötigt werden %d. Repariere es zuerst.%n",
                    tool.getName(), tool.getDurability(), requiredHits);
            return;
        }

        output.printf("%nDu arbeitest mit %s.%n", tool.getName());
        while (!activity.isCompleted() && !tool.isBroken()) {
            AttackResult result = activityService.attack(player, activity, tool);
            output.printf("Schaden: %d | %s: %d / %d HP | Haltbarkeit: %d%n",
                    result.damage(), activity.getName(), result.remainingHealth(),
                    activity.getMaxHealth(), tool.getDurability());

            if (result.completed()) {
                output.printf("%s abgeschlossen! +%d Gold, +%d XP%n",
                        activity.getName(), result.goldEarned(), result.experienceEarned());
                printDrop(dropService.awardDrop(player));
            }
        }

        if (!activity.isCompleted()) {
            output.printf("%s ist kaputt. Die Aktivität wurde abgebrochen.%n", tool.getName());
        }
    }

    private Tool findBestTool(Activity activity) {
        return player.getInventory().getItems().stream()
                .filter(Tool.class::isInstance)
                .map(Tool.class::cast)
                .filter(tool -> !tool.isBroken())
                .filter(tool -> tool.getType() == activity.getRequiredToolType())
                .filter(tool -> tool.getLevel() >= activity.getRequiredToolLevel())
                .max(Comparator.comparingInt(Tool::getDamage))
                .orElse(null);
    }

    private void printDrop(DropResult drop) {
        if (drop.addedToInventory()) {
            output.printf("Fund: %s (%s)%n", drop.item().getName(), drop.rarity());
        } else {
            output.printf("Fund verloren, weil das Inventar voll ist: %s%n", drop.item().getName());
        }
    }

    private void showInventory() {
        output.println();
        output.printf("======= INVENTAR (%d/%d) =======%n",
                player.getInventory().getItemCount(), player.getInventory().getCapacity());
        List<Item> items = player.getInventory().getItems();
        if (items.isEmpty()) {
            output.println("Dein Inventar ist leer.");
            return;
        }

        for (int index = 0; index < items.size(); index++) {
            output.printf("[%d] %s%n", index + 1, describeItem(items.get(index)));
        }
    }

    private String describeItem(Item item) {
        if (item instanceof Tool tool) {
            return String.format("%s | %s L%d | Schaden %d | Haltbarkeit %d/%d",
                    tool.getName(), toolTypeName(tool.getType()), tool.getLevel(),
                    tool.getDamage(), tool.getDurability(), tool.getMaxDurability());
        }
        return String.format("%s | Verkaufspreis %d", item.getName(), item.getSellPrice());
    }

    private void showShop() {
        boolean shopping = true;
        while (shopping) {
            output.println();
            output.println("======= SHOP =======");
            output.printf("Gold: %d%n", player.getMoney());
            output.println("[1] Kaufen");
            output.println("[2] Verkaufen");
            output.println("[3] Werkzeuge reparieren");
            output.println("[4] Gold-Multiplikator verbessern");
            output.println("[5] Inventar erweitern");
            output.println("[0] Zurück");
            switch (readChoice()) {
                case 1 -> buyItem();
                case 2 -> sellItem();
                case 3 -> repairTool();
                case 4 -> buyGoldUpgrade();
                case 5 -> buyInventoryUpgrade();
                case 0 -> shopping = false;
                default -> output.println("Ungültige Auswahl.");
            }
        }
    }

    private void buyItem() {
        output.println("[1] Holzaxt          50 Gold");
        output.println("[2] Steinspitzhacke 100 Gold");
        output.println("[3] Eisenaxt         220 Gold");
        output.println("[4] Eisenspitzhacke  260 Gold");
        output.println("[5] Diamantaxt       600 Gold");
        output.println("[6] Diamantspitzhacke 700 Gold");
        output.println("[0] Abbrechen");

        Item item = createShopItem(readChoice());
        if (item == null) {
            return;
        }
        if (shopService.buyItem(player, item)) {
            output.printf("%s wurde gekauft.%n", item.getName());
        } else if (player.getInventory().isFull()) {
            output.println("Kauf nicht möglich: Das Inventar ist voll.");
        } else {
            output.println("Kauf nicht möglich: Zu wenig Gold.");
        }
    }

    private Item createShopItem(int choice) {
        return switch (choice) {
            case 1 -> new Tool("Holzaxt", 50, 25, ToolType.AXE, 7, 1, 10);
            case 2 -> new Tool("Steinspitzhacke", 100, 50, ToolType.PICKAXE, 8, 1, 15);
            case 3 -> new Tool("Eisenaxt", 220, 110, ToolType.AXE, 15, 2, 25);
            case 4 -> new Tool("Eisenspitzhacke", 260, 130, ToolType.PICKAXE, 18, 2, 25);
            case 5 -> new Tool("Diamantaxt", 600, 300, ToolType.AXE, 32, 3, 40);
            case 6 -> new Tool("Diamantspitzhacke", 700, 350, ToolType.PICKAXE, 36, 3, 40);
            default -> null;
        };
    }

    private void sellItem() {
        List<Item> items = player.getInventory().getItems();
        if (items.isEmpty()) {
            output.println("Du hast keine Items zum Verkaufen.");
            return;
        }
        for (int index = 0; index < items.size(); index++) {
            Item item = items.get(index);
            output.printf("[%d] %s - %d Gold%n", index + 1, item.getName(), item.getSellPrice());
        }
        output.println("[0] Abbrechen");

        int choice = readChoice();
        if (choice <= 0 || choice > items.size()) {
            return;
        }
        Item item = items.get(choice - 1);
        if (shopService.sellItem(player, item)) {
            output.printf("%s wurde für %d Gold verkauft.%n", item.getName(), item.getSellPrice());
        }
    }

    private void repairTool() {
        List<Tool> tools = player.getInventory().getItems().stream()
                .filter(Tool.class::isInstance)
                .map(Tool.class::cast)
                .filter(tool -> tool.getMissingDurability() > 0)
                .toList();
        if (tools.isEmpty()) {
            output.println("Alle Werkzeuge sind vollständig repariert.");
            return;
        }

        for (int index = 0; index < tools.size(); index++) {
            Tool tool = tools.get(index);
            output.printf("[%d] %s %d/%d - Reparatur %d Gold%n",
                    index + 1, tool.getName(), tool.getDurability(), tool.getMaxDurability(),
                    repairService.calculateRepairCost(tool));
        }
        output.println("[0] Abbrechen");

        int choice = readChoice();
        if (choice <= 0 || choice > tools.size()) {
            return;
        }
        Tool tool = tools.get(choice - 1);
        RepairResult result = repairService.repair(player, tool);
        if (result.status() == RepairStatus.REPAIRED) {
            output.printf("%s wurde für %d Gold vollständig repariert.%n",
                    tool.getName(), result.cost());
        } else if (result.status() == RepairStatus.NOT_ENOUGH_MONEY) {
            output.printf("Zu wenig Gold. Die Reparatur kostet %d Gold.%n", result.cost());
        }
    }

    private void buyGoldUpgrade() {
        if (upgradeService.isMaxLevel(player)) {
            output.println("Der Gold-Multiplikator ist bereits maximal.");
            return;
        }

        int cost = upgradeService.getNextUpgradeCost(player);
        double nextMultiplier = upgradeService.getNextMultiplier(player);
        output.printf("Upgrade auf %.2fx für %d Gold kaufen? [1] Ja [0] Nein%n",
                nextMultiplier, cost);
        if (readChoice() != 1) {
            return;
        }
        if (upgradeService.buyNextGoldMultiplier(player)) {
            output.printf("Gold-Multiplikator auf %.2fx verbessert.%n", player.getGoldMultiplier());
        } else {
            output.println("Zu wenig Gold für dieses Upgrade.");
        }
    }

    private void buyInventoryUpgrade() {
        if (inventoryUpgradeService.isMaxLevel(player)) {
            output.println("Das Inventar hat bereits die maximale Grösse.");
            return;
        }

        int cost = inventoryUpgradeService.getNextUpgradeCost(player);
        int capacity = inventoryUpgradeService.getNextCapacity(player);
        output.printf("Inventar auf %d Slots für %d Gold erweitern? [1] Ja [0] Nein%n",
                capacity, cost);
        if (readChoice() != 1) {
            return;
        }
        if (inventoryUpgradeService.buyNextUpgrade(player)) {
            output.printf("Inventar auf %d Slots erweitert.%n", player.getInventory().getCapacity());
        } else {
            output.println("Zu wenig Gold für die Inventarerweiterung.");
        }
    }

    private void showCharacter() {
        output.println();
        output.println("======= CHARAKTER =======");
        output.printf("Gold: %d%n", player.getMoney());
        output.printf("Level: %d%n", player.getLevel());
        output.printf("XP: %d / %d%n", player.getExperience(),
                levelService.experienceRequiredForNextLevel(player.getLevel()));
        output.printf("Gold-Multiplikator: %.2fx%n", player.getGoldMultiplier());
        output.printf("Inventar: %d / %d Slots%n",
                player.getInventory().getItemCount(), player.getInventory().getCapacity());
    }

    private int readChoice() {
        output.print("> ");
        if (!scanner.hasNextLine()) {
            return 0;
        }
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private String toolTypeName(ToolType type) {
        return switch (type) {
            case AXE -> "Axt";
            case PICKAXE -> "Spitzhacke";
            case GENERIC -> "Werkzeug";
        };
    }
}
