package ch.tbz.inventorymgr.game;

import ch.tbz.inventorymgr.activity.ActivityService;
import ch.tbz.inventorymgr.player.LevelService;
import ch.tbz.inventorymgr.player.Player;
import ch.tbz.inventorymgr.player.UpgradeService;
import ch.tbz.inventorymgr.random.DropService;
import ch.tbz.inventorymgr.random.JavaRandomGenerator;
import ch.tbz.inventorymgr.shop.ShopService;
import ch.tbz.inventorymgr.shop.RepairService;
import ch.tbz.inventorymgr.shop.InventoryUpgradeService;

import java.util.Scanner;

public class Game {

    public void run() {
        Player player = new Player(100);
        GameMenu menu = new GameMenu(
                player,
                new ShopService(),
                new ActivityService(),
                new LevelService(),
                new UpgradeService(),
                new RepairService(),
                new InventoryUpgradeService(),
                new DropService(new JavaRandomGenerator()),
                new Scanner(System.in),
                System.out
        );
        menu.run();
    }
}
