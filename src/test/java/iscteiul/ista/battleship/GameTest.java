package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Fleet fleet;
    private Game game;

    @BeforeEach
    void setUp() {
        fleet = new Fleet();
        fleet.addShip(new Barge(Compass.NORTH, new Position(3, 4)));
        fleet.addShip(new Caravel(Compass.EAST, new Position(1, 1)));
        fleet.addShip(new Frigate(Compass.SOUTH, new Position(6, 6)));

        game = new Game(fleet);
    }

    @AfterEach
    void tearDown() {
        fleet = null;
        game = null;
    }

    // --------------------------------------------------------------
    // fire()
    // --------------------------------------------------------------
    @Test
    void fire() {

        int max = Fleet.BOARD_SIZE;

        // invalid: row < 0
        game.fire(new Position(-1, 5));
        assertEquals(1, game.getInvalidShots());

        // invalid: column < 0
        game.fire(new Position(3, -1));
        assertEquals(2, game.getInvalidShots());

        // invalid: row > BOARD_SIZE
        game.fire(new Position(max + 1, 2));
        assertEquals(3, game.getInvalidShots());

        // invalid: column > BOARD_SIZE
        game.fire(new Position(2, max + 1));
        assertEquals(4, game.getInvalidShots());

        // miss
        IShip miss = game.fire(new Position(0, 0));
        assertNull(miss);

        // hit & sink barge
        IShip sunk = game.fire(new Position(3, 4));
        assertNotNull(sunk);
        assertEquals(1, game.getHits());
        assertEquals(1, game.getSunkShips());

        // repeated shot
        game.fire(new Position(3, 4));
        assertEquals(1, game.getRepeatedShots());
    }

    // --------------------------------------------------------------
    // getShots()
    // --------------------------------------------------------------
    @Test
    void getShots() {
        assertTrue(game.getShots().isEmpty());
        game.fire(new Position(3, 4));
        assertEquals(1, game.getShots().size());
    }

    // --------------------------------------------------------------
    // getRepeatedShots()
    // --------------------------------------------------------------
    @Test
    void getRepeatedShots() {
        Position p = new Position(1, 1);
        game.fire(p);
        game.fire(p);
        assertEquals(1, game.getRepeatedShots());
    }

    // --------------------------------------------------------------
    // getInvalidShots()
    // --------------------------------------------------------------
    @Test
    void getInvalidShots() {
        int max = Fleet.BOARD_SIZE;

        game.fire(new Position(-5, 3));                  // invalid
        game.fire(new Position(max + 1, max + 1));      // invalid
        assertEquals(2, game.getInvalidShots());
    }

    // --------------------------------------------------------------
    // getHits()
    // --------------------------------------------------------------
    @Test
    void getHits() {
        assertEquals(0, game.getHits());
        game.fire(new Position(3, 4));
        assertEquals(1, game.getHits());
    }

    // --------------------------------------------------------------
    // getSunkShips()
    // --------------------------------------------------------------
    @Test
    void getSunkShips() {
        assertEquals(0, game.getSunkShips());
        game.fire(new Position(3, 4));
        assertEquals(1, game.getSunkShips());
    }

    // --------------------------------------------------------------
    // getRemainingShips()
    // --------------------------------------------------------------
    @Test
    void getRemainingShips() {
        assertEquals(3, game.getRemainingShips());
        game.fire(new Position(3, 4));
        assertEquals(2, game.getRemainingShips());
    }

    // --------------------------------------------------------------
    // printBoard()
    // --------------------------------------------------------------
    @Test
    void printBoard() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game.printBoard(List.of(new Position(3, 4)), 'X');

        assertTrue(out.toString().contains("X"));
    }

    // --------------------------------------------------------------
    // printValidShots()
    // --------------------------------------------------------------
    @Test
    void printValidShots() {
        game.fire(new Position(3, 4));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game.printValidShots();

        assertTrue(out.toString().contains("X"));
    }

    // --------------------------------------------------------------
    // printFleet()
    // --------------------------------------------------------------
    @Test
    void printFleet() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        game.printFleet();

        assertTrue(out.toString().contains("#"));
    }
}
