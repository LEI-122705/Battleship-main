package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Compass.
 * Author: (runtime) System.getProperty("user.name")
 * Date: (runtime) LocalDateTime.now()
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getDirection(): 1
 * - toString(): 1
 * - charToCompass(char): 5
 * - values(): 1
 * - valueOf(String): 1
 */
class CompassTest {

    private Compass compass;

    private static final String AUTHOR = System.getProperty("user.name");
    private static final String GENERATED_AT = LocalDateTime.now().toString();

    @BeforeEach
    void setUp() {
        // usa uma constante enum como instância para os testes
        compass = Compass.NORTH;
    }

    @AfterEach
    void tearDown() {
        compass = null;
    }

    // constructor() - CC = 1
    @Test
    void constructor() {
        assertNotNull(Compass.NORTH, "Error: expected Compass.NORTH not null but got null");
        assertEquals('n', Compass.NORTH.getDirection(), "Error: expected NORTH direction 'n' but got " + Compass.NORTH.getDirection());
    }

    // getDirection() - CC = 1
    @Test
    void getDirection() {
        assertEquals('n', compass.getDirection(), "Error: expected compass.getDirection() to be 'n' but got " + compass.getDirection());
    }

    // toString() - CC = 1
    @Test
    void toStringMethod() {
        assertEquals("n", compass.toString(), "Error: expected compass.toString() to be \"n\" but got " + compass.toString());
    }

    // charToCompass(char) - CC = 5 -> gerar 5 testes para cada case + default

    @Test
    void charToCompass1() {
        // case 'n' -> NORTH
        Compass result = Compass.charToCompass('n');
        assertEquals(Compass.NORTH, result, "Error: expected charToCompass('n') to return NORTH but got " + result);
    }

    @Test
    void charToCompass2() {
        // case 's' -> SOUTH
        Compass result = Compass.charToCompass('s');
        assertEquals(Compass.SOUTH, result, "Error: expected charToCompass('s') to return SOUTH but got " + result);
    }

    @Test
    void charToCompass3() {
        // case 'e' -> EAST
        Compass result = Compass.charToCompass('e');
        assertEquals(Compass.EAST, result, "Error: expected charToCompass('e') to return EAST but got " + result);
    }

    @Test
    void charToCompass4() {
        // case 'o' -> WEST
        Compass result = Compass.charToCompass('o');
        assertEquals(Compass.WEST, result, "Error: expected charToCompass('o') to return WEST but got " + result);
    }

    @Test
    void charToCompass5() {
        // default -> UNKNOWN
        Compass result = Compass.charToCompass('x');
        assertEquals(Compass.UNKNOWN, result, "Error: expected charToCompass('x') to return UNKNOWN but got " + result);
    }

    // values() - CC = 1
    @Test
    void valuesMethod() {
        Compass[] expected = new Compass[]{Compass.NORTH, Compass.SOUTH, Compass.EAST, Compass.WEST, Compass.UNKNOWN};
        assertArrayEquals(expected, Compass.values(), "Error: expected values() to return the enum constants in declaration order");
    }

    // valueOf(String) - CC = 1 (test both normal and exception paths in a single grouped test)
    @Test
    void valueOfMethod() {
        assertAll(
                () -> assertEquals(Compass.NORTH, Compass.valueOf("NORTH"),
                        "Error: expected valueOf(\"NORTH\") to return Compass.NORTH but got " + Compass.valueOf("NORTH")),
                () -> assertThrows(IllegalArgumentException.class, () -> Compass.valueOf("NO_SUCH"),
                        "Error: expected IllegalArgumentException when calling valueOf with invalid name but none was thrown")
        );
    }
}