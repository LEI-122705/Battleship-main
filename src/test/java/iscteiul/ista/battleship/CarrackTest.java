/**
 * Test class for Carrack.
 * Author: LEI-111812
 * Date: 2025-11-13
 *
 * Cyclomatic Complexity (CC):
 * - constructor: 5 (NORTH, SOUTH, EAST, WEST, default)
 * - getSize(): 1
 */

package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarrackTest {

    private Position start;

    @BeforeEach
    @DisplayName("Setup: Creating initial Position(5,5)")
    void setUp() {
        start = new Position(5, 5);
    }

    @AfterEach
    @DisplayName("Teardown: Clearing initial position")
    void tearDown() {
        start = null;
    }

    // ------------------------------------------------------------
    // CONSTRUCTOR TESTS (CC = 5)
    // ------------------------------------------------------------

    @Test
    @DisplayName("Carrack NORTH constructor places 3 positions vertically downward")
    void constructorNorthTest() {
        Carrack c = new Carrack(Compass.NORTH, start);

        assertAll("NORTH placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0),
                        "Error: NORTH first pos incorrect"),
                () -> assertEquals(new Position(6, 5), c.getPositions().get(1),
                        "Error: NORTH second pos incorrect"),
                () -> assertEquals(new Position(7, 5), c.getPositions().get(2),
                        "Error: NORTH third pos incorrect")
        );
    }

    @Test
    @DisplayName("Carrack SOUTH constructor places 3 positions vertically upward")
    void constructorSouthTest() {
        Carrack c = new Carrack(Compass.SOUTH, start);

        assertAll("SOUTH placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0),
                        "Error: SOUTH first pos incorrect"),
                () -> assertEquals(new Position(4, 5), c.getPositions().get(1),
                        "Error: SOUTH should decrease row by 1"),
                () -> assertEquals(new Position(3, 5), c.getPositions().get(2),
                        "Error: SOUTH should decrease row by 2")
        );
    }

    @Test
    @DisplayName("Carrack EAST constructor places 3 positions horizontally increasing column")
    void constructorEastTest() {
        Carrack c = new Carrack(Compass.EAST, start);

        assertAll("EAST placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0),
                        "Error: EAST first pos incorrect"),
                () -> assertEquals(new Position(5, 6), c.getPositions().get(1),
                        "Error: EAST should increase column by 1"),
                () -> assertEquals(new Position(5, 7), c.getPositions().get(2),
                        "Error: EAST should increase column by 2")
        );
    }

    @Test
    @DisplayName("Carrack WEST constructor places 3 positions horizontally decreasing column")
    void constructorWestTest() {
        Carrack c = new Carrack(Compass.WEST, start);

        assertAll("WEST placement",
                () -> assertEquals(new Position(5, 5), c.getPositions().get(0),
                        "Error: WEST first pos incorrect"),
                () -> assertEquals(new Position(5, 4), c.getPositions().get(1),
                        "Error: WEST should decrease column by 1"),
                () -> assertEquals(new Position(5, 3), c.getPositions().get(2),
                        "Error: WEST should decrease column by 2")
        );
    }

    @Test
    @DisplayName("Carrack constructor throws when bearing is invalid")
    void constructorInvalidBearingTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new Carrack(Compass.UNKNOWN, start),
                "Error: invalid bearing should throw IllegalArgumentException"
        );
    }

    // ------------------------------------------------------------
    // getSize()
    // ------------------------------------------------------------

    @Test
    @DisplayName("Carrack getSize() must always return 3")
    void getSizeTest() {
        Carrack c = new Carrack(Compass.NORTH, start);

        assertEquals(3, c.getSize(),
                "Error: Carrack size must be 3");
    }

    // ------------------------------------------------------------
    // shoot() + stillFloating()
    // ------------------------------------------------------------

    @Test
    @DisplayName("Carrack sinks only after all 3 parts are hit")
    void shootingCarrackTest() {
        Carrack c = new Carrack(Compass.EAST, start);

        assertTrue(c.stillFloating(), "Error: Carrack should float at start");

        c.shoot(new Position(5, 5));
        assertTrue(c.stillFloating(), "Error: must float after 1 hit");

        c.shoot(new Position(5, 6));
        assertTrue(c.stillFloating(), "Error: must float after 2 hits");

        c.shoot(new Position(5, 7));
        assertFalse(c.stillFloating(), "Error: must sink after all parts are hit");
    }
}
