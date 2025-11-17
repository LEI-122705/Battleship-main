package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Position.
 * Author: (runtime) System.getProperty("user.name")
 * Date: 2025-11-17 12:00
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getRow(): 1
 * - getColumn(): 1
 * - hashCode(): 1
 * - equals(Object): 4
 * - isAdjacentTo(IPosition): 2
 * - occupy(): 1
 * - shoot(): 1
 * - isOccupied(): 1
 * - isHit(): 1
 * - toString(): 1
 */
class PositionTest {

    private Position pos;

    // store runtime author and generation timestamp for reference in test logs if needed
    private static final String AUTHOR = System.getProperty("user.name");
    private static final String GENERATED_AT = LocalDateTime.now().toString();

    @BeforeEach
    void setUp() {
        // instancia padrão usada em quase todos os testes
        pos = new Position(2, 3);
    }

    @AfterEach
    void tearDown() {
        pos = null;
    }

    // constructor() - CC = 1
    @Test
    void constructor() {
        assertAll(
                () -> assertEquals(2, pos.getRow(), "Error: expected row 2 but got " + pos.getRow()),
                () -> assertEquals(3, pos.getColumn(), "Error: expected column 3 but got " + pos.getColumn()),
                () -> assertFalse(pos.isOccupied(), "Error: expected isOccupied false after construction but got true"),
                () -> assertFalse(pos.isHit(), "Error: expected isHit false after construction but got true")
        );
    }

    // getRow() - CC = 1
    @Test
    void getRow() {
        assertEquals(2, pos.getRow(), "Error: expected row 2 but got " + pos.getRow());
    }

    // getColumn() - CC = 1
    @Test
    void getColumn() {
        assertEquals(3, pos.getColumn(), "Error: expected column 3 but got " + pos.getColumn());
    }

    // hashCode() - CC = 1
    @Test
    void testHashCode() {
        Position equal = new Position(2, 3);
        Position different = new Position(5, 6);
        assertAll(
                () -> assertEquals(equal.hashCode(), pos.hashCode(),
                        "Error: expected same hashCode for equal positions but got different (" + pos.hashCode() + " vs " + equal.hashCode() + ")"),
                () -> assertNotEquals(different.hashCode(), pos.hashCode(),
                        "Error: expected different hashCode for different positions but got same (" + pos.hashCode() + ")")
        );
    }

    // equals(Object) - CC = 4 -> gerar 4 testes cobrindo referências iguais, não-instanceof, igual por coordenadas, diferente
    @Test
    void equals1() {
        // mesma referência -> true
        assertTrue(pos.equals(pos), "Error: expected true when comparing same reference but got false");
    }

    @Test
    void equals2() {
        // outro tipo -> false
        Object other = "not a position";
        assertFalse(pos.equals(other), "Error: expected false when comparing to non-IPosition but got true");
    }

    @Test
    void equals3() {
        // igual por coordenadas -> true
        Position equal = new Position(2, 3);
        assertTrue(pos.equals(equal), "Error: expected true for positions with same row/column but got false");
    }

    @Test
    void equals4() {
        // diferente por coordenadas -> false
        Position diff = new Position(2, 4);
        assertFalse(pos.equals(diff), "Error: expected false for positions with different coordinates but got true");
    }

    // isAdjacentTo(IPosition) - CC = 2 (condição composta). Para branch coverage, testar combinações que afetam o resultado e exceção.
    @Test
    void isAdjacentTo1() {
        // ambas diferenças <= 1 -> true
        Position pAdj = new Position(3, 4); // row diff =1, col diff =1
        assertTrue(pos.isAdjacentTo(pAdj), "Error: expected adjacent positions to return true but got false");
    }

    @Test
    void isAdjacentTo2() {
        // row diff >1, col diff <=1 -> false (testa influência da componente de linha)
        Position pFarRow = new Position(5, 3); // row diff =3, col diff =0
        assertFalse(pos.isAdjacentTo(pFarRow), "Error: expected non-adjacent when row diff >1 but got true");
    }

    @Test
    void isAdjacentTo3() {
        // col diff >1, row diff <=1 -> false (testa influência da componente de coluna)
        Position pFarCol = new Position(2, 6); // row diff =0, col diff =3
        assertFalse(pos.isAdjacentTo(pFarCol), "Error: expected non-adjacent when column diff >1 but got true");
    }

    @Test
    void isAdjacentTo4() {
        // passagem de null deve lançar NullPointerException (implementação atual não faz null-check)
        assertThrows(NullPointerException.class, () -> pos.isAdjacentTo(null),
                "Error: expected NullPointerException when passing null to isAdjacentTo");
    }

    // occupy() - CC = 1
    @Test
    void occupy() {
        assertFalse(pos.isOccupied(), "Error: expected isOccupied false before occupy but got true");
        pos.occupy();
        assertTrue(pos.isOccupied(), "Error: expected isOccupied true after occupy but got false");
    }

    // shoot() - CC = 1
    @Test
    void shoot() {
        assertFalse(pos.isHit(), "Error: expected isHit false before shoot but got true");
        pos.shoot();
        assertTrue(pos.isHit(), "Error: expected isHit true after shoot but got false");
    }

    // isOccupied() - CC = 1
    @Test
    void isOccupied() {
        pos.occupy();
        assertTrue(pos.isOccupied(), "Error: expected isOccupied true after occupy but got false");
    }

    // isHit() - CC = 1
    @Test
    void isHit() {
        pos.shoot();
        assertTrue(pos.isHit(), "Error: expected isHit true after shoot but got false");
    }

    // toString() - CC = 1
    @Test
    void testToString() {
        String s = pos.toString();
        assertAll(
                () -> assertTrue(s.contains("Linha"), "Error: expected toString to contain 'Linha' but was: " + s),
                () -> assertTrue(s.contains("Coluna"), "Error: expected toString to contain 'Coluna' but was: " + s),
                () -> assertTrue(s.contains("2"), "Error: expected toString to contain row '2' but was: " + s),
                () -> assertTrue(s.contains("3"), "Error: expected toString to contain column '3' but was: " + s)
        );
    }

}