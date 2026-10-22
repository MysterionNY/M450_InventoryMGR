package ch.tbz.inventorymgr.activity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityTest {

    @Test
    void activityShouldStartWithMaximumHealth() {
        Activity activity = new Activity("Tree", 30, 1);

        assertEquals("Tree", activity.getName());
        assertEquals(30, activity.getMaxHealth());
        assertEquals(30, activity.getRemainingHealth());
        assertEquals(1, activity.getRequiredToolLevel());
    }

    @Test
    void activityShouldRejectInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new Activity(" ", 30, 1));
        assertThrows(IllegalArgumentException.class, () -> new Activity("Tree", 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Activity("Tree", 30, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Activity("Tree", 30, 1, 0, 5, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Activity("Tree", 30, 1, 1, -1, 5));
    }
}
