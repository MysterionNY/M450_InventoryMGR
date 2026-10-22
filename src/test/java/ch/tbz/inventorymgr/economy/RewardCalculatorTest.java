package ch.tbz.inventorymgr.economy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RewardCalculatorTest {

    private RewardCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RewardCalculator();
    }

    @Test
    void rewardShouldIncludeMultiplierAndBonus() {
        assertEquals(220, calculator.calculateGold(100, 2.0, 10));
    }

    @Test
    void rewardShouldBeRoundedToNearestGold() {
        assertEquals(158, calculator.calculateGold(100, 1.5, 5));
    }

    @Test
    void zeroBaseRewardShouldReturnZero() {
        assertEquals(0, calculator.calculateGold(0, 2.0, 10));
    }

    @Test
    void rewardShouldRejectNegativeValues() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateGold(-1, 1.0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateGold(100, -1.0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateGold(100, 1.0, -1));
    }

    @Test
    void rewardShouldRejectNonFiniteFactors() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateGold(100, Double.NaN, 0));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateGold(100, 1.0, Double.POSITIVE_INFINITY));
    }
}
