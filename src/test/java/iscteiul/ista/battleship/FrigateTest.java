package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

class FrigateTest {
    //Codigo IA
    /**
     * Test class for Frigate.
     * Author: ${user.name}
     * Date: 2025-11-15
     *
     * Cyclomatic Complexity:
     * - Frigate(bearing, pos): 5
     * - getSize(): 1
     */

    private Frigate frigate;
    private IPosition basePos;

    @BeforeEach
    public void setUp() {
        basePos = new Position(5, 5);
    }

    @AfterEach
    public void takeDown() {
        frigate = null;
        basePos = null;
    }

    //testar construtor
    @Test
    @DisplayName("Construtor quando frigate aponta North")
    public void constructor_North() {
        frigate = new Frigate(Compass.NORTH, basePos);

        assertAll("NORTH bearing path",
                () -> assertEquals(4, frigate.getPositions().size(),
                        "Error: expected 4 positions but got a different number"),
                () -> assertEquals(5, frigate.getPositions().get(0).getRow(),
                        "Error: row mismatch for NORTH path"),
                () -> assertEquals(5, frigate.getPositions().get(0).getColumn(),
                        "Error: column mismatch for NORTH path"),
                () -> assertEquals(8, frigate.getPositions().get(3).getRow(),
                        "Error: expected last NORTH row to be pos.row+3"),
                () -> assertEquals(5, frigate.getPositions().get(3).getColumn(),
                        "Error: expected constant column for NORTH path")
        );
        //verificar se tem um tamanho de 4, se a primeira posicao e (5,5), e se a final e (8,5)
        //codigo de frigate trata o caso de SOUTH e o caso de NORTH da mesma forma
    }

    @Test
    @DisplayName("Construtor quando frigate aponta South")
    public void constructor_Sout() {
        frigate = new Frigate(Compass.SOUTH, basePos);

        assertAll("SOUTH bearing path",
                () -> assertEquals(4, frigate.getPositions().size(),
                        "Error: expected 4 positions but got incorrect list size"),
                () -> assertEquals(5, frigate.getPositions().get(0).getRow(),
                        "Error: row mismatch for SOUTH path"),
                () -> assertEquals(5, frigate.getPositions().get(0).getColumn(), //adicionei esta linha como o chat nao tinha
                        "Error: column mismatch for SOUTH path"),
                () -> assertEquals(8, frigate.getPositions().get(3).getRow(),
                        "Error: expected last SOUTH row to be pos.row+3"),
                () -> assertEquals(5, frigate.getPositions().get(3).getColumn(),
                        "Error: expected constant column for SOUTH path")
        );
        //verificar se tem um tamanho de 4, se a primeira posicao e (5,5), e se a final e (8,5)
    }

    @Test
    @DisplayName("Construtor quando frigate aponta East")
    public void constructor_East() {
        frigate = new Frigate(Compass.EAST, basePos);

        assertAll("EAST bearing path",
                () -> assertEquals(4, frigate.getPositions().size(),
                        "Error: expected 4 positions but got incorrect number"),
                () -> assertEquals(5, frigate.getPositions().get(0).getRow(),
                        "Error: expected constant row for EAST path"),
                () -> assertEquals(5, frigate.getPositions().get(0).getColumn(),
                        "Error: expected starting column for EAST path"),
                () -> assertEquals(5, frigate.getPositions().get(3).getRow(),
                        "Error: expected constant row for EAST path"),
                () -> assertEquals(8, frigate.getPositions().get(3).getColumn(),
                        "Error: expected last column to be pos.column+3")
        );
        //verificar se tem um tamanho de 4, se a primeira posicao e (5,5), e se a final e (5,8)
        //codigo de frigate trata o caso de EAST e o caso de WEST da mesma forma
    }

    @Test
    @DisplayName("Construtor quando frigate aponta West")
    public void constructor_West() {
        frigate = new Frigate(Compass.WEST, basePos);

        assertAll("WEST bearing path",
                () -> assertEquals(4, frigate.getPositions().size(),
                        "Error: expected 4 positions"),
                () -> assertEquals(5, frigate.getPositions().get(0).getRow(),
                        "Error: row mismatch for WEST path"),
                () -> assertEquals(5, frigate.getPositions().get(0).getColumn(),
                        "Error: incorrect starting column for WEST path"),
                () -> assertEquals(5, frigate.getPositions().get(3).getRow(),
                        "Error: incorrect constant row for WEST path"),
                () -> assertEquals(8, frigate.getPositions().get(3).getColumn(),
                        "Error: expected last WEST column to be pos.column+3")
        );
        //verificar se tem um tamanho de 4, se a primeira posicao e (5,5), e se a final e (5,8)
    }

    @Test
    @DisplayName("Construtor quando frigate nao sabe onde apontar")
    public void constructor_invalid() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> new Frigate(Compass.UNKNOWN, basePos),
                    "Error: expected IllegalArgumentException for UNKNOWN bearing");

        assertTrue(ex.getMessage().contains("invalid bearing"), "Error: exception message should indicate invalid bearing");
    }

    //teste getSize()
    @Test
    @DisplayName("Testar getSize()")
    public void getSize() {
        frigate = new Frigate(Compass.NORTH, basePos);

        assertEquals(4, frigate.getSize(), "Error: expected size 4 but got different value");
    }
}