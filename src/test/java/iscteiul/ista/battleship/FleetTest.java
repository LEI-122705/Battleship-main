package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    @Test
    @DisplayName("getShips: frota inicialmente vazia")
    void getShips() {
        Fleet fleet = new Fleet();
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("addShip: adiciona navio válido dentro do tabuleiro")
    void addShip() {
        Fleet fleet = new Fleet();

        IShip ship = makeShip("Fragata", 0, 0, 0, 0, true, false);
        boolean added = fleet.addShip(ship);

        assertTrue(added);
        assertEquals(1, fleet.getShips().size());
        assertSame(ship, fleet.getShips().get(0));
    }

    @Test
    @DisplayName("addShip: rejeita navio fora do tabuleiro (right > BOARD_SIZE-1)")
    void addShipOutsideBoard() {
        Fleet fleet = new Fleet();

        IShip outside = makeShip("Nau", 0, IFleet.BOARD_SIZE, 0, 0, true, false);
        boolean added = fleet.addShip(outside);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("addShip: rejeita navio com coordenadas negativas (left < 0)")
    void addShipWithNegativeCoordinates() {
        Fleet fleet = new Fleet();

        IShip invalid = makeShip("Caravela", -1, 0, 0, 0, true, false);
        boolean added = fleet.addShip(invalid);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("addShip: rejeita navio em colisão com outro (colisionRisk)")
    void addShipWithCollisionRisk() {
        Fleet fleet = new Fleet();

        final IShip[] secondHolder = new IShip[1];

        IShip first = makeShip(
                "Fragata",
                0, 0, 0, 0,
                true,
                false,
                other -> other == secondHolder[0], // true só para o segundo
                position -> false
        );

        IShip second = makeShip("Fragata", 1, 1, 1, 1, true, false);
        secondHolder[0] = second;

        assertTrue(fleet.addShip(first));          // entra sem colisão
        assertFalse(fleet.addShip(second));        // deve ser recusado pela colisão
        assertEquals(1, fleet.getShips().size());  // só o primeiro ficou na frota
    }

    @Test
    @DisplayName("getShipsLike: filtra por categoria")
    void getShipsLike() {
        Fleet fleet = new Fleet();

        IShip s1 = makeShip("Galeao", 0, 0, 0, 0, true, false);
        IShip s2 = makeShip("Fragata", 1, 1, 1, 1, true, false);
        IShip s3 = makeShip("Galeao", 2, 2, 2, 2, true, false);

        assertTrue(fleet.addShip(s1));
        assertTrue(fleet.addShip(s2));
        assertTrue(fleet.addShip(s3));

        assertEquals(2, fleet.getShipsLike("Galeao").size());
        assertEquals(1, fleet.getShipsLike("Fragata").size());
        assertEquals(0, fleet.getShipsLike("Nau").size());
    }

    @Test
    @DisplayName("getFloatingShips: retorna apenas navios ainda flutuando")
    void getFloatingShips() {
        Fleet fleet = new Fleet();

        IShip afloat = makeShip("Caravela", 0, 0, 0, 0, true, false);
        IShip sunk = makeShip("Barca", 1, 1, 1, 1, false, false);

        assertTrue(fleet.addShip(afloat));
        assertTrue(fleet.addShip(sunk));

        assertEquals(1, fleet.getFloatingShips().size());
        assertSame(afloat, fleet.getFloatingShips().get(0));
    }

    @Test
    @DisplayName("shipAt: devolve navio que ocupa a posição dada")
    void shipAt() {
        Fleet fleet = new Fleet();

        IPosition pos = makePosition("p1");

        IShip occupier = makeShip("Galeao", 0, 0, 0, 0,
                true,
                false,
                other -> false,
                position -> position == pos);
        assertTrue(fleet.addShip(occupier));

        IShip found = fleet.shipAt(pos);
        assertSame(occupier, found);
    }

    @Test
    @DisplayName("shipAt: devolve null quando nenhum navio ocupa a posição")
    void shipAtReturnsNullWhenNoShip() {
        Fleet fleet = new Fleet();

        IPosition pos = makePosition("p2");

        IShip nonOccupier = makeShip("Nau", 0, 0, 0, 0,
                true,
                false,
                other -> false,
                position -> false);

        assertTrue(fleet.addShip(nonOccupier));

        IShip found = fleet.shipAt(pos);
        assertNull(found);
    }

    @Test
    @DisplayName("addShip: rejeita quando a frota já está (mais do que) cheia (branch ships.size() > FLEET_SIZE)")
    void addShipWhenFleetIsFull() {
        Fleet fleet = new Fleet();

        for (int i = 0; i < IFleet.FLEET_SIZE + 1; i++) {
            IShip s = makeShip("Fragata", 0, 0, 0, 0, true, false);
            assertTrue(fleet.addShip(s), "navio " + i + " devia ser aceite");
        }
        assertEquals(IFleet.FLEET_SIZE + 1, fleet.getShips().size());

        IShip extra = makeShip("Fragata", 0, 0, 0, 0, true, false);

        boolean added = fleet.addShip(extra);

        assertFalse(added);
        assertEquals(IFleet.FLEET_SIZE + 1, fleet.getShips().size());
    }


    @Test
    @DisplayName("addShip: navio com coordenadas negativas faz isInsideBoard devolver false")
    void addShipWithNegativeCoordinatesTriggersIsInsideBoardFalse() {
        Fleet fleet = new Fleet();

        IShip invalid = makeShip("Caravela", -1, 0, 0, 0, true, false);

        boolean added = fleet.addShip(invalid);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("printShipsByCategory: lança AssertionError se categoria for null")
    void printShipsByCategoryWithNullCategory() {
        Fleet fleet = new Fleet();

        assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null));
    }

    @Test
    @DisplayName("addShip: navio com top negativo está fora do tabuleiro (topMost < 0)")
    void addShipWithNegativeTopIsOutsideBoard() {
        Fleet fleet = new Fleet();

        IShip invalid = makeShip("Fragata", 0, 0, -1, 0, true, false);

        boolean added = fleet.addShip(invalid);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
    }

    @Test
    @DisplayName("addShip: navio com bottom > BOARD_SIZE-1 está fora do tabuleiro (bottomMost > max)")
    void addShipWithBottomOutsideBoard() {
        Fleet fleet = new Fleet();

        IShip invalid = makeShip("Fragata", 0, 0, 0, IFleet.BOARD_SIZE, true, false);

        boolean added = fleet.addShip(invalid);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
    }


    // ======================
    //   TESTES DE IMPRESSÃO
    // ======================

    @Test
    @DisplayName("printShips (static): imprime todos os navios da lista")
    void printShipsStaticPrintsAllShips() {
        IShip s1 = makeShip("Fragata", 0, 0, 0, 0, true, false);
        IShip s2 = makeShip("Nau", 1, 1, 1, 1, true, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));

        try {
            Fleet.printShips(Arrays.asList(s1, s2));
        } finally {
            System.setOut(original);
        }

        String output = baos.toString().trim();
        String[] lines = output.split("\\R"); // separa por quebras de linha

        assertArrayEquals(
                new String[]{"MockShip[Fragata]", "MockShip[Nau]"},
                lines
        );
    }

    @Test
    @DisplayName("printAllShips / printFloatingShips / printShipsByCategory / printStatus:")
    void printHelpersExecuteAndUseFilters() {
        Fleet fleet = new Fleet();

        IShip galeao = makeShip("Galeao", 0, 0, 0, 0, true, false);
        IShip fragata = makeShip("Fragata", 1, 1, 1, 1, true, false);
        IShip barcaSunk = makeShip("Barca", 2, 2, 2, 2, false, false);

        assertTrue(fleet.addShip(galeao));
        assertTrue(fleet.addShip(fragata));
        assertTrue(fleet.addShip(barcaSunk));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));

        try {
            fleet.printAllShips();
            fleet.printFloatingShips();
            fleet.printShipsByCategory("Galeao");
            fleet.printStatus();
        } finally {
            System.setOut(original);
        }

        String output = baos.toString();

        assertTrue(output.contains("MockShip[Galeao]"));
        assertTrue(output.contains("MockShip[Fragata]"));

        assertTrue(output.contains("MockShip[Barca]"));
    }

    @SuppressWarnings("unchecked")
    private static IShip makeShip(String category,
                                  int left, int right, int top, int bottom,
                                  boolean stillFloating,
                                  boolean tooCloseDefault) {
        return makeShip(category, left, right, top, bottom, stillFloating, tooCloseDefault,
                other -> tooCloseDefault, position -> false);
    }

    @SuppressWarnings("unchecked")
    private static IShip makeShip(String category,
                                  int left, int right, int top, int bottom,
                                  boolean stillFloating,
                                  boolean tooCloseDefault,
                                  TooClosePredicate tooClosePredicate,
                                  OccupiesPredicate occupiesPredicate) {
        ClassLoader cl = IShip.class.getClassLoader();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                String name = method.getName();
                switch (name) {
                    case "getCategory":
                        return category;
                    case "stillFloating":
                        return stillFloating;
                    case "getLeftMostPos":
                        return left;
                    case "getRightMostPos":
                        return right;
                    case "getTopMostPos":
                        return top;
                    case "getBottomMostPos":
                        return bottom;
                    case "tooCloseTo":
                        return tooClosePredicate.test(args[0]);
                    case "occupies":
                        return occupiesPredicate.test(args[0]);
                    case "toString":
                        return "MockShip[" + category + "]";
                    default:
                        Class<?> ret = method.getReturnType();
                        if (ret.equals(boolean.class)) return false;
                        if (ret.equals(int.class)) return 0;
                        return null;
                }
            }
        };
        return (IShip) Proxy.newProxyInstance(cl, new Class[]{IShip.class}, handler);
    }

    @SuppressWarnings("unchecked")
    private static IPosition makePosition(String id) {
        ClassLoader cl = IPosition.class.getClassLoader();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                String name = method.getName();
                if ("toString".equals(name)) return "Position[" + id + "]";
                if ("equals".equals(name) && args != null && args.length == 1) return proxy == args[0];
                if ("hashCode".equals(name)) return Objects.hash(id);
                return null;
            }
        };
        return (IPosition) Proxy.newProxyInstance(cl, new Class[]{IPosition.class}, handler);
    }

    private interface TooClosePredicate {
        boolean test(Object otherShip);
    }

    private interface OccupiesPredicate {
        boolean test(Object position);
    }
}
