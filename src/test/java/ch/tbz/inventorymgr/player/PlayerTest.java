package ch.tbz.inventorymgr.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @Test
    void playerShouldStartWithGivenAmountOfMoney() {
        Player player = new Player(100);

        assertEquals(100, player.getMoney());
    }

    @Test
    void playerShouldReceiveMoney() {
        Player player = new Player(100);

        player.addMoney(50);

        assertEquals(150, player.getMoney());
    }

    @Test
    void playerShouldNotReceiveNegativeMoney() {
        Player player = new Player(100);

        assertThrows(IllegalArgumentException.class, () -> player.addMoney(-50));
        assertEquals(100, player.getMoney());
    }

    @Test
    void playerShouldPayWhenEnoughMoneyIsAvailable() {
        Player player = new Player(100);

        assertTrue(player.removeMoney(40));
        assertEquals(60, player.getMoney());
    }

    @Test
    void playerShouldNotPayMoreMoneyThanAvailable() {
        Player player = new Player(100);

        assertFalse(player.removeMoney(101));
        assertEquals(100, player.getMoney());
    }

    @Test
    void playerShouldNotStartWithNegativeMoney() {
        assertThrows(IllegalArgumentException.class, () -> new Player(-1));
    }

    @Test
    void moneyShouldNotOverflow() {
        Player player = new Player(Integer.MAX_VALUE);

        assertThrows(ArithmeticException.class, () -> player.addMoney(1));
        assertEquals(Integer.MAX_VALUE, player.getMoney());
    }
}
