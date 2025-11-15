package iscteiul.ista.battleship;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

class GalleonTest {
    /**
     * Test class for Galleon.
     * Author: ${user.name}
     * Date: 2025-11-15 23:59
     *
     * Cyclomatic Complexity:
     * - constructor: 6
     * - getSize(): 1
     */

    private IPosition basePos;
    private Galleon g;

    @BeforeEach
    public void setUp() {
        basePos = new Position(5, 5);
        g = null;
    }

    @AfterEach
    public void takeDown() {
        basePos = null;
        g = null;
    }

    @Test
    @DisplayName("Construtor quando galleon aponta North")
    public void constructor_north() {
        g = new Galleon(Compass.NORTH, basePos);
        List<IPosition> p = g.getPositions();

        assertAll("NORTH positions",
                () -> assertEquals(5, p.size(),
                        "Error: expected 5 positions for NORTH but got " + p.size()),
                () -> assertEquals(new Position(5,5), p.get(0),
                        "Error: NORTH pos0 incorrect"),
                () -> assertEquals(new Position(5,6), p.get(1),
                        "Error: NORTH pos1 incorrect"),
                () -> assertEquals(new Position(5,7), p.get(2),
                        "Error: NORTH pos2 incorrect"),
                () -> assertEquals(new Position(6,6), p.get(3),
                        "Error: NORTH pos3 incorrect"),
                () -> assertEquals(new Position(7,6), p.get(4),
                        "Error: NORTH pos4 incorrect")
        );
        //verificar se tem um tamanho de 5, e que esta nas posicoes corretas
    }

    @Test
    @DisplayName("Construtor quando galleon aponta East")
    public void constructor_east() {
        g = new Galleon(Compass.EAST, basePos);
        List<IPosition> p = g.getPositions();

        assertAll("EAST positions",
                () -> assertEquals(5, p.size(),
                        "Error: expected 5 positions for EAST but got " + p.size()),
                () -> assertEquals(new Position(5,5), p.get(0),
                        "Error: EAST pos0 incorrect"),
                () -> assertEquals(new Position(6,3), p.get(1),
                        "Error: EAST pos1 incorrect"),
                () -> assertEquals(new Position(6,4), p.get(2),
                        "Error: EAST pos2 incorrect"),
                () -> assertEquals(new Position(6,5), p.get(3),
                        "Error: EAST pos3 incorrect"),
                () -> assertEquals(new Position(7,5), p.get(4),
                        "Error: EAST pos4 incorrect")
        );
        //verificar se tem um tamanho de 5, e que esta nas posicoes corretas
    }

    @Test
    @DisplayName("Construtor quando galleon aponta South")
    public void constructor_south() {
        g = new Galleon(Compass.SOUTH, basePos);
        List<IPosition> p = g.getPositions();

        assertAll("SOUTH positions",
                () -> assertEquals(5, p.size(),
                        "Error: expected 5 positions for SOUTH"),
                () -> assertEquals(new Position(5,5), p.get(0),
                        "Error: SOUTH pos0 incorrect"),
                () -> assertEquals(new Position(6,5), p.get(1),
                        "Error: SOUTH pos1 incorrect"),
                () -> assertEquals(new Position(7,4), p.get(2), //changed (7,5) to (7,4)
                        "Error: SOUTH pos2 incorrect"),
                () -> assertEquals(new Position(7,5), p.get(3), //changed (7,6) to (7,5)
                        "Error: SOUTH pos3 incorrect"),
                () -> assertEquals(new Position(7,6), p.get(4), //changed (7,7) to (7,6)
                        "Error: SOUTH pos4 incorrect")
        );
        //verificar se tem um tamanho de 5, e que esta nas posicoes corretas
    }

    @Test
    @DisplayName("Construtor quando galleon aponta West")
    public void constructor_west() {
        g = new Galleon(Compass.WEST, basePos);
        List<IPosition> p = g.getPositions();

        assertAll("WEST positions",
                () -> assertEquals(5, p.size(),
                        "Error: expected 5 positions for WEST"),
                () -> assertEquals(new Position(5,5), p.get(0),
                        "Error: WEST pos0 incorrect"),
                () -> assertEquals(new Position(6,5), p.get(1),
                        "Error: WEST pos1 incorrect"),
                () -> assertEquals(new Position(6,6), p.get(2),
                        "Error: WEST pos2 incorrect"),
                () -> assertEquals(new Position(6,7), p.get(3),
                        "Error: WEST pos3 incorrect"),
                () -> assertEquals(new Position(7,5), p.get(4),
                        "Error: WEST pos4 incorrect")
        );
        //verificar se tem um tamanho de 5, e que esta nas posicoes corretas
    }

    @Test
    @DisplayName("Construtor quando galleon nao sabe onde apontar")
    public void constructor_compass_unknown() {
        assertThrows(IllegalArgumentException.class,
                () -> new Galleon(Compass.UNKNOWN, basePos),
                "Error: expected IllegalArgumentException for Compass.UNKNOWN");
    }

    //test getSize()
    @Test
    @DisplayName("Testar getSize()")
    public void getSize() {
        g = new Galleon(Compass.NORTH, basePos);
        assertEquals(5, g.getSize(), "Error: expected size 5 but got " + g.getSize());
    }
}