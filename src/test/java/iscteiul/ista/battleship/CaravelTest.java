/**
 * Test class for Caravel.
 * Author: LEI-111812
 * Date: 2025-11-13
 *
 * Cyclomatic Complexity (CC):
 * - constructor: 5
 * - getSize(): 1
 */

package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaravelTest {

    private Position start;

    @BeforeEach
    @DisplayName("Setup Position(5,5)")
    void setUp() {
        start = new Position(5, 5);
    }

    @AfterEach
    @DisplayName("Teardown Position cleared")
    void tearDown() {
        start = null;
    }

    @Test
    @DisplayName("Caravel NORTH placement: size=2, vertical forward")
    void constructorNorthTest() {
        Caravel c = new Caravel(Compass.NORTH, start);

        assertAll("NORTH placement",
                () -> assertEquals(2, c.getSize(), "Error: size must be 2"),
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0)),
                () -> assertEquals(new Position(6, 5), c.getPositions().get(1))
        );
    }

    @Test
    @DisplayName("Caravel SOUTH placement: second pos decreases row")
    void constructorSouthTest() {
        Caravel c = new Caravel(Compass.SOUTH, start);

        assertAll("SOUTH placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0)),
                () -> assertEquals(new Position(4, 5), c.getPositions().get(1))
        );
    }

    @Test
    @DisplayName("Caravel EAST placement: second pos increases column")
    void constructorEastTest() {
        Caravel c = new Caravel(Compass.EAST, start);

        assertAll("EAST placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0)),
                () -> assertEquals(new Position(5, 6), c.getPositions().get(1))
        );
    }

    @Test
    @DisplayName("Caravel WEST placement: second pos decreases column")
    void constructorWestTest() {
        Caravel c = new Caravel(Compass.WEST, start);

        assertAll("WEST placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0)),
                () -> assertEquals(new Position(5, 4), c.getPositions().get(1))
        );
    }

    @Test
    @DisplayName("Caravel constructor throws for invalid bearing")
    void constructorInvalidBearingTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new Caravel(Compass.UNKNOWN, start));
    }

    @Test
    @DisplayName("Caravel getSize() must return 2")
    void getSizeTest() {
        assertEquals(2, new Caravel(Compass.NORTH, start).getSize());
    }

    @Test
    @DisplayName("Caravel sinks after both parts are hit")
    void shootingCaravelTest() {
        Caravel c = new Caravel(Compass.EAST, start);

        assertTrue(c.stillFloating());
        c.shoot(new Position(5, 5));
        assertTrue(c.stillFloating());
        c.shoot(new Position(5, 6));
        assertFalse(c.stillFloating());
    }
}
