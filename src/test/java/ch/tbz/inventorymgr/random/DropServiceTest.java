package ch.tbz.inventorymgr.random;

import ch.tbz.inventorymgr.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DropServiceTest {

    @Test
    void mockedRandomValueShouldProduceTreasure() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.99);
        DropService dropService = new DropService(randomGenerator);

        assertEquals(DropRarity.TREASURE, dropService.rollDrop());
        verify(randomGenerator).nextDouble();
    }

    @Test
    void mockedRandomValueShouldProduceRareDrop() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.95);

        assertEquals(DropRarity.RARE, new DropService(randomGenerator).rollDrop());
    }

    @Test
    void mockedRandomValueShouldProduceNormalDrop() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.50);

        assertEquals(DropRarity.NORMAL, new DropService(randomGenerator).rollDrop());
    }

    @Test
    void invalidRandomValueShouldBeRejected() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(1.0);

        assertThrows(IllegalStateException.class,
                () -> new DropService(randomGenerator).rollDrop());
    }

    @Test
    void nonFiniteRandomValueShouldBeRejected() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(Double.NaN);

        assertThrows(IllegalStateException.class,
                () -> new DropService(randomGenerator).rollDrop());
    }

    @Test
    void mockedTreasureShouldBeAddedToPlayerInventory() {
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.99);
        Player player = new Player(0);

        DropResult result = new DropService(randomGenerator).awardDrop(player);

        assertEquals(DropRarity.TREASURE, result.rarity());
        assertEquals("Schatztruhe", result.item().getName());
        assertTrue(result.addedToInventory());
        assertEquals(1, player.getInventory().getItemCount());
    }
}
