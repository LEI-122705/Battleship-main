package iscteiul.ista.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.Arrays;

class ShipTest {
    //Codigo do IA
    /**
     * Test class for class Ship.
     * Author: ${user.name}
     * Date: 2025-11-13 16:40
     * Cyclomatic Complexity:
     * - constructor: 1
     * - buildShip(): 6
     * - stillFloating(): 2
     * - getTopMostPos(): 2
     * - getBottomMostPos(): 2
     * - getLeftMostPos(): 2
     * - getRightMostPos(): 2
     * - occupies(): 2
     * - tooCloseTo(IShip): 2
     * - tooCloseTo(IPosition): 2
     * - shoot(): 2
     * - toString(): 1
     */

    private Ship ship;
    private Position basePos;

    @BeforeEach
    void setUp(){
        basePos = new Position(3, 3);
        ship = new Barge(Compass.NORTH, basePos); // Barge is a subclass of Ship

        // Set up positions for consistent testing
        ship.positions = Arrays.asList(
                new Position(3, 3),
                new Position(3, 4),
                new Position(3, 5)
        );
    }

    @AfterEach
    void takeDown(){
        ship = null; //liberta memoria
        basePos = null;
    }

    //testes construtores
    @Test
    @DisplayName("Construtor para barca")
    void buildShip_barca() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
        assertTrue(s instanceof Barge, "Error: expected Barge instance for 'barca'");
    }

    @Test
    @DisplayName("Construtor para caravela")
    void buildShip_caravela() {
        Ship s = Ship.buildShip("caravela", Compass.EAST, new Position(1, 1));
        assertTrue(s instanceof Caravel, "Error: expected Caravel instance for 'caravela'");
    }

    @Test
    @DisplayName("Construtor para nau")
    void buildShip_nau() {
        Ship s = Ship.buildShip("nau", Compass.SOUTH, new Position(1, 1));
        assertTrue(s instanceof Carrack, "Error: expected Carrack instance for 'nau'");
    }

    @Test
    @DisplayName("Construtor para fragata")
    void buildShip_fragata() {
        Ship s = Ship.buildShip("fragata", Compass.WEST, new Position(1, 1));
        assertTrue(s instanceof Frigate, "Error: expected Frigate instance for 'fragata'");
    }

    @Test
    @DisplayName("Construtor para galeao")
    void buildShip_galeao() {
        Ship s = Ship.buildShip("galeao", Compass.NORTH, new Position(1, 1));
        assertTrue(s instanceof Galleon, "Error: expected Galleon instance for 'galeao'");
    }

    @Nested
    @DisplayName("Valores invalidos para construtores")
    class InvalidConstructorValues {
        @Test
        @DisplayName("Construtor com categoria invalida")
        void buildShip_invalidKind() {
            Ship s= Ship.buildShip("submarine", Compass.NORTH, new Position(1, 1));
            assertNull(s, "Error: expected null for unknown ship type");
        }

        @Test
        @DisplayName("Construtor com bearing invalido")
        public void constructor_invalidBearing() {
            assertThrows(NullPointerException.class,
                    () -> Ship.buildShip("barca", null, new Position(1, 1)), "Error: expected to throw NullPointerException for null bearing");
        }

        @Test
        @DisplayName("Construtor com posicao invalida")
        public void constructor_invalidPosition() {
            assertThrows(NullPointerException.class,
                    () ->  Ship.buildShip("barca", Compass.NORTH, null), "Error: expected NullPointerException for null position");
        }
    }

    @Nested
    @DisplayName("Testes para getters")
    class Getters {
        //testes para getCategory
        @Test
        @DisplayName("Correct category: barge")
        void getCategory_Barge() {
            Ship s= Ship.buildShip("barca", Compass.NORTH, new Position(1, 1));
            assertEquals(s.getCategory(), "Barca", "Error: expected 'Barca' for category");
        }

        @Test
        @DisplayName("Correct category: caravel")
        void getCategory_Caravel() {
            Ship s= Ship.buildShip("caravela", Compass.NORTH, new Position(1, 1));
            assertEquals(s.getCategory(), "Caravela", "Error: expected 'Caravela' for category");
        }

        @Test
        @DisplayName("Correct category: carrack")
        void getCategory_Carrack() {
            Ship s= Ship.buildShip("nau", Compass.NORTH, new Position(1, 1));
            assertEquals(s.getCategory(), "Nau", "Error: expected 'Nau' for category");
        }

        @Test
        @DisplayName("Correct category: galleon")
        void getCategory_Galleon() {
            Ship s= Ship.buildShip("galeao", Compass.NORTH, new Position(1, 1));
            assertEquals(s.getCategory(), "Galeao", "Error: expected 'Galeao' for category");
        }

        @Test
        @DisplayName("Correct category: frigate")
        void getCategory() {
            Ship s= Ship.buildShip("fragata", Compass.NORTH, new Position(1, 1));
            assertEquals(s.getCategory(), "Fragata", "Error: expected 'Fragata' for category");
        }

        //testes para getBearing
        @Test
        @DisplayName("Correctly returning bearing")
        void getBearing() {
            assertAll("Getting Bearing",
                    () -> assertTrue(ship.getBearing() instanceof Compass, "Error: Expected to return an object of the type Compass"),
                    () -> assertEquals(ship.getBearing(), Compass.NORTH, "Error: Expected to return Compass.NORTH")
            );
        }

        //testes para getPosition e getPositions
        @Test
        @DisplayName("Checking correct position returned")
        void getPosition(){
            assertEquals(ship.getPosition(), basePos, "Error: Incorrect position returned");
        }

        @Test
        @DisplayName("Checking correct list of positions returned")
        void getPositions(){
            assertAll("Checking correct list of positions returned",
                    () -> assertEquals(ship.getPositions().size(), 3,
                            "Error: Expected 3 positions to be stored"),
                    () -> assertEquals(ship.getPositions().get(0), new Position(3, 3),
                            "Error: Expected position (3,3) to be stored at index 0"),
                    () -> assertEquals(ship.getPositions().get(1), new Position(3, 4),
                            "Error: Expected position (3,4) to be stored at index 1"),
                    () -> assertEquals(ship.getPositions().get(2), new Position(3, 5),
                            "Error: Expected position (3,5) to be stored at index 2")
            );
        }
    }

    //testes para still floating
    @Test
    @DisplayName("Still floating when not hit")
    void stillFloating_allIntact() {
        ship.getPositions().forEach(p -> assertFalse(p.isHit()));
        assertTrue(ship.stillFloating(), "Error: expected ship to be floating when not hit");
    }

    @Test
    @DisplayName("Not floating when all hit")
    void stillFloating_allHit() {
        ship.getPositions().forEach(pos -> ship.shoot(pos)); //ia estava a dar erro entao alterei
        assertFalse(ship.stillFloating(), "Error: expected ship to sink when all positions are hit");
    }

    //testes pare get__MostPos
    @Test
    @DisplayName("Top most position stored correctly")
    void getTopMostPos_topIsFirst() {
        int top = ship.getTopMostPos();
        assertEquals(3, top, "Error: expected top-most row = 3");
    }

    @Test
    @DisplayName("Top most position changed, stored correctly")
    void getTopMostPos_topChanges() {
        ship.positions = Arrays.asList(new Position(2, 3), new Position(5, 3));
        assertEquals(2, ship.getTopMostPos(), "Error: expected top-most row = 2");
    }

    @Test
    @DisplayName("Bottom most position stored correctly")
    void getBottomMostPos_bottomIsLast() { //bottom depende de row :)
        ship.positions = Arrays.asList(new Position(1, 1), new Position(3, 1));
        assertEquals(3, ship.getBottomMostPos(), "Error: expected bottom-most row = 3");
        //ship tem pos [(3,1), (1,1)] therefore o maior valor e a da row 3 => (3,1)
    }

    @Test
    @DisplayName("Bottom most position changed, stored correctly")
    void getBottomMostPos_bottomChanges() {
        ship.positions = Arrays.asList(new Position(6, 1), new Position(2, 1));
        assertEquals(6, ship.getBottomMostPos(), "Error: expected bottom-most row = 6");
    }

    @Test
    @DisplayName("Left most position stored correctly")
    void getLeftMostPos_leftIsFirst() {
        assertEquals(3, ship.getLeftMostPos(), "Error: expected left-most column = 3");
    }

    @Test
    @DisplayName("Left most position changed, stored correctly")
    void getLeftMostPos_leftChanges() {
        ship.positions = Arrays.asList(new Position(3, 1), new Position(3, 5));
        assertEquals(1, ship.getLeftMostPos(), "Error: expected left-most column = 1");
    }

    @Test
    @DisplayName("Right most position stored correctly")
    void getRightMostPos_rightIsLast() { //right most depende da coluna
        assertEquals(5, ship.getRightMostPos(), "Error: expected right-most column = 5");
        //ship tem pos [(3,3), (3,4), (3,5)] therefore a coluna maior e 5
    }

    @Test
    @DisplayName("Right most position changed, stored correctly")
    void getRightMostPos_rightChanges() {
        ship.positions = Arrays.asList(new Position(3, 7), new Position(3, 1));
        assertEquals(7, ship.getRightMostPos(), "Error: expected right-most column = 7");
    }

    //testes para occupies
    @Test
    @DisplayName("Confirm ship occupies position")
    void occupies_positionContained() {
        Position target = new Position(3, 3);
        assertTrue(ship.occupies(target), "Error: expected ship to occupy position (3,3)");
    }

    @Test
    @DisplayName("Confirm ship does not occupy position")
    void occupies_positionNotContained() {
        Position target = new Position(10, 10);
        assertFalse(ship.occupies(target), "Error: expected ship not to occupy (10,10)");
    }

    //testes para tooCloseto baseado num barco
    @Test
    @DisplayName("Confirming ships are too close to each other")
    void tooCloseToShip_trueWhenAdjacent() {
        Ship other = new Barge(Compass.NORTH, new Position(3, 6));
        other.positions = Arrays.asList(new Position(3, 6));
        assertTrue(ship.tooCloseTo(other), "Error: ships should be too close when adjacent");
        //verificamos se nao estamos muito perto do barco ship [(3,3), (3,4), (3,5)]
    }

    @Test
    @DisplayName("Confirming ships aren't too close to each other")
    void tooCloseToShip_falseWhenFar() {
        Ship other = new Barge(Compass.NORTH, new Position(10, 10));
        other.positions = Arrays.asList(new Position(10, 10));
        assertFalse(ship.tooCloseTo(other), "Error: ships should not be too close when distant");
    }

    //testes para tooCloseto baseado numa posicao
    @Test
    @DisplayName("Confirming ship is too close to position")
    void tooCloseToPosition_trueWhenAdjacent() {
        Position near = new Position(3, 6); // adjacent to (3,5)
        assertTrue(ship.tooCloseTo(near), "Error: expected true for adjacent position");
    }

    @Test
    @DisplayName("Confirming ship isn't too close to position")
    void tooCloseToPosition_falseWhenNotAdjacent() {
        Position far = new Position(10, 10);
        assertFalse(ship.tooCloseTo(far), "Error: expected false for distant position");
    }

    //testes para shoot
    @Test
    @DisplayName("Confirming ship hits valid position")
    void shoot_positionHit() {
        Position target = (Position) ship.getPositions().get(1);
        ship.shoot(target);
        assertTrue(target.isHit(), "Error: expected position to be hit after shooting");
    }

    @Test
    @DisplayName("Confirming ship does not hit invalid position")
    void shoot_positionMissed() {
        Position outside = new Position(10, 10);
        ship.shoot(outside);
        boolean anyHit = ship.getPositions().stream().anyMatch(pos -> pos.isHit()); //ia estava a dar erro, corrigi
        assertFalse(anyHit, "Error: no position should be hit for a missed shot");
    }

    //testes para toString
    @Test
    @DisplayName("Confirming correct format for toString")
    void toString_correctFormat() {
        String str = ship.toString();

        //alterei condicao para nao ser tao "case sensitive"
        assertTrue(str.contains("Barca") || str.contains("barca") || str.contains("Barge") || str.contains("barge"),
                "Error: toString should contain category name");
    }

    @Nested
    @DisplayName("Testes para null values")
    class NullValues {
        @Test //test for occupies()
        @DisplayName("Throw exception when occupying null position")
        void occupies_nullPosition(){
            assertThrows(NullPointerException.class,
                    () -> ship.occupies(null), "Error: expected NullPointerException when null position is passed");
        }

        @Test //test for tooCloseTo()
        @DisplayName("Throw exception when checking if close to null ship")
        void tooCloseToShip_nullShip(){
            assertThrows(NullPointerException.class,
                    () -> ship.tooCloseTo((IShip) null), "Error: expected NullPointerException when null ship is passed");
        }

        @Test
        @DisplayName("Throw exception when shooting null position")
        void shoot_nullPosition(){
            assertThrows(NullPointerException.class,
                    () -> ship.shoot(null), "Error: expected NullPointerException when null position is passed");
        }
    }
}