/**
 * Test class for Barge.
 * Author: LEI-111812
 * Date: 2025-11-13
 */

package iscteiul.ista.battleship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class BargeTest {

    private Barge barge;

    @BeforeEach
    @DisplayName("Setup Barge at (3,4)")
    void setUp() {
        barge = new Barge(Compass.NORTH, new Position(3, 4));
    }

    @AfterEach
    @DisplayName("Teardown Barge cleared")
    void tearDown() {
        barge = null;
    }

    @Test
    @DisplayName("Barge constructor creates single-cell ship at correct position")
    void constructorTest() {
        assertAll("Constructor",
                () -> assertEquals(1, barge.getSize()),
                () -> assertEquals(1, barge.getPositions().size()),
                () -> assertEquals(new Position(3, 4), barge.getPositions().get(0))
        );
    }

    @Test
    @DisplayName("Barge getSize() always returns 1")
    void getSizeTest() {
        assertEquals(1, barge.getSize());
    }

    @Test
    @DisplayName("Barge getPositions() returns correct position list")
    void getPositionsTest() {
        assertAll(
                () -> assertEquals(1, barge.getPositions().size()),
                () -> assertEquals(new Position(3, 4), barge.getPositions().get(0))
        );
    }

    @Test
    @DisplayName("Barge occupies() works for matching and non-matching coordinates")
    void occupiesTest() {
        assertTrue(barge.occupies(new Position(3, 4)));
        assertFalse(barge.occupies(new Position(3, 5)));
    }

    @Test
    @DisplayName("Barge stillFloating() becomes false after being hit once")
    void stillFloatingTest() {
        assertTrue(barge.stillFloating());
        barge.shoot(new Position(3, 4));
        assertFalse(barge.stillFloating());
    }

    @Test
    @DisplayName("Barge shoot() sinks it immediately")
    void shootTest() {
        assertTrue(barge.stillFloating());
        barge.shoot(new Position(3, 4));
        assertFalse(barge.stillFloating());
    }

}
