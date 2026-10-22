package ch.tbz.inventorymgr.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelServiceTest {

    private LevelService levelService;
    private Player player;

    @BeforeEach
    void setUp() {
        levelService = new LevelService();
        player = new Player(0);
    }

    @Test
    void playerShouldStartAtLevelOneWithoutExperience() {
        assertEquals(1, player.getLevel());
        assertEquals(0, player.getExperience());
    }

    @Test
    void playerShouldLevelUpAtRequiredExperience() {
        levelService.addExperience(player, 100);

        assertEquals(2, player.getLevel());
        assertEquals(100, player.getExperience());
    }

    @Test
    void playerShouldGainMultipleLevelsAtOnce() {
        levelService.addExperience(player, 500);

        assertEquals(4, player.getLevel());
        assertEquals(500, player.getExperience());
    }

    @Test
    void levelThresholdsShouldFollowDefinedProgression() {
        assertEquals(100, levelService.experienceRequiredForNextLevel(1));
        assertEquals(250, levelService.experienceRequiredForNextLevel(2));
        assertEquals(500, levelService.experienceRequiredForNextLevel(3));
    }

    @Test
    void negativeExperienceShouldBeRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> levelService.addExperience(player, -1));
        assertEquals(0, player.getExperience());
    }
}
