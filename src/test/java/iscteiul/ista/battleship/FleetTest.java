package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FleetTest {

    // ---------------------------------------------------------------------
    // getShips
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("getShips")
    class GetShipsTests {

        @Test
        @DisplayName("frota inicialmente vazia")
        void getShipsInitiallyEmpty() {
            Fleet fleet = new Fleet();
            assertTrue(fleet.getShips().isEmpty());
        }
    }

    // ---------------------------------------------------------------------
    // addShip
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("addShip")
    class AddShipTests {

        @Test
        @DisplayName("adiciona navio válido dentro do tabuleiro")
        void addShip() {
            Fleet fleet = new Fleet();

            IShip ship = makeShip("Fragata", 0, 0, 0, 0, true, false);
            boolean added = fleet.addShip(ship);

            assertTrue(added);
            assertEquals(1, fleet.getShips().size());
            assertSame(ship, fleet.getShips().get(0));
        }

        @Test
        @DisplayName("rejeita navio fora do tabuleiro (right > BOARD_SIZE-1)")
        void addShipOutsideBoardRight() {
            Fleet fleet = new Fleet();

            IShip outside = makeShip("Nau", 0, IFleet.BOARD_SIZE, 0, 0, true, false);
            boolean added = fleet.addShip(outside);

            assertFalse(added);
            assertTrue(fleet.getShips().isEmpty());
        }

        @Test
        @DisplayName("rejeita navio com leftMostPos < 0")
        void addShipOutsideBoardLeft() {
            Fleet fleet = new Fleet();

            IShip outside = makeShip("Fragata", -1, -1, 0, 0, true, false);
            boolean added = fleet.addShip(outside);

            assertFalse(added);
            assertTrue(fleet.getShips().isEmpty());
        }

        @Test
        @DisplayName("rejeita navio com topMostPos < 0")
        void addShipOutsideBoardTop() {
            Fleet fleet = new Fleet();

            IShip outside = makeShip("Fragata", 0, 0, -1, -1, true, false);
            boolean added = fleet.addShip(outside);

            assertFalse(added);
            assertTrue(fleet.getShips().isEmpty());
        }

        @Test
        @DisplayName("rejeita navio com bottomMostPos > BOARD_SIZE-1")
        void addShipOutsideBoardBottom() {
            Fleet fleet = new Fleet();

            IShip outside = makeShip("Fragata", 0, 0, 0, IFleet.BOARD_SIZE, true, false);
            boolean added = fleet.addShip(outside);

            assertFalse(added);
            assertTrue(fleet.getShips().isEmpty());
        }

        @Test
        @DisplayName("rejeita navio demasiado próximo de outro")
        void addShipTooCloseToOther() {
            Fleet fleet = new Fleet();

            // Primeiro navio: considera qualquer outro como "too close"
            IShip first = makeShip("Fragata", 0, 0, 0, 0, true, true);
            assertTrue(fleet.addShip(first));

            // Segundo navio: não interessa o tooClose, porque quem é chamado é o first
            IShip second = makeShip("Fragata", 0, 0, 0, 0, true, false);

            boolean added = fleet.addShip(second);

            assertFalse(added);
            assertEquals(1, fleet.getShips().size());
        }

        @Test
        @DisplayName("rejeita quando a frota já excedeu FLEET_SIZE")
        void addShipTooManyShips() {
            Fleet fleet = new Fleet();

            // Enche a frota com FLEET_SIZE+1 navios válidos
            // Todos com a mesma posição (0,0,0,0) -> sempre dentro do tabuleiro
            for (int i = 0; i <= IFleet.FLEET_SIZE; i++) {
                IShip ship = makeShip("Fragata", 0, 0, 0, 0, true, false);
                assertTrue(fleet.addShip(ship));
            }

            // Agora a frota já tem FLEET_SIZE+1 navios.
            // O próximo deve ser rejeitado porque ships.size() > FLEET_SIZE
            IShip extra = makeShip("Extra", 0, 0, 0, 0, true, false);

            boolean added = fleet.addShip(extra);

            assertFalse(added);
            assertEquals(IFleet.FLEET_SIZE + 1, fleet.getShips().size());
        }
    }

    // ---------------------------------------------------------------------
    // getShipsLike
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("getShipsLike")
    class GetShipsLikeTests {

        @Test
        @DisplayName("filtra por categoria")
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
        }
    }

    // ---------------------------------------------------------------------
    // getFloatingShips
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("getFloatingShips")
    class GetFloatingShipsTests {

        @Test
        @DisplayName("retorna apenas navios ainda flutuando")
        void getFloatingShips() {
            Fleet fleet = new Fleet();

            IShip afloat = makeShip("Caravela", 0, 0, 0, 0, true, false);
            IShip sunk   = makeShip("Barca",    1, 1, 1, 1, false, false);

            assertTrue(fleet.addShip(afloat));
            assertTrue(fleet.addShip(sunk));

            assertEquals(1, fleet.getFloatingShips().size());
            assertSame(afloat, fleet.getFloatingShips().get(0));
        }
    }

    // ---------------------------------------------------------------------
    // shipAt
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("shipAt")
    class ShipAtTests {

        @Test
        @DisplayName("devolve navio que ocupa a posição dada")
        void shipAt() {
            Fleet fleet = new Fleet();

            IPosition pos = makePosition("p1");

            IShip occupier = makeShip("Galeao", 0, 0, 0, 0, true, false,
                    other -> false,
                    position -> position == pos);
            assertTrue(fleet.addShip(occupier));

            IShip found = fleet.shipAt(pos);
            assertSame(occupier, found);
        }

        @Test
        @DisplayName("devolve null quando nenhuma embarcação ocupa a posição (com navios)")
        void shipAtReturnsNullIfNoShip() {
            Fleet fleet = new Fleet();

            IPosition pos = makePosition("pX");

            // Navio que nunca ocupa a posição pedida
            IShip ship = makeShip("Fragata", 0, 0, 0, 0, true, false,
                    other -> false,
                    position -> false);
            assertTrue(fleet.addShip(ship));

            IShip found = fleet.shipAt(pos);

            assertNull(found);
        }

        @Test
        @DisplayName("devolve null quando a frota está vazia")
        void shipAtEmptyFleet() {
            Fleet fleet = new Fleet();
            IPosition pos = makePosition("pEmpty");

            IShip found = fleet.shipAt(pos);

            assertNull(found);
        }
    }

    // ---------------------------------------------------------------------
    // Impressão (printStatus, printAllShips, etc.)
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("impressão")
    class PrintTests {

        @Test
        @DisplayName("printAllShips imprime todos os navios")
        void printAllShipsPrintsAll() {
            Fleet fleet = new Fleet();

            IShip s1 = makeShip("Galeao", 0, 0, 0, 0, true, false);
            IShip s2 = makeShip("Fragata", 1, 1, 1, 1, true, false);

            assertTrue(fleet.addShip(s1));
            assertTrue(fleet.addShip(s2));

            String out = captureOutput(fleet::printAllShips);

            assertTrue(out.contains("MockShip[Galeao]"));
            assertTrue(out.contains("MockShip[Fragata]"));
        }

        @Test
        @DisplayName("printShipsByCategory imprime apenas a categoria pedida")
        void printShipsByCategoryPrintsOnlyThatCategory() {
            Fleet fleet = new Fleet();

            IShip s1 = makeShip("Galeao", 0, 0, 0, 0, true, false);
            IShip s2 = makeShip("Fragata", 1, 1, 1, 1, true, false);

            assertTrue(fleet.addShip(s1));
            assertTrue(fleet.addShip(s2));

            String out = captureOutput(() -> fleet.printShipsByCategory("Galeao"));

            assertTrue(out.contains("MockShip[Galeao]"));
            assertFalse(out.contains("MockShip[Fragata]"));
        }

        @Test
        @DisplayName("printFloatingShips imprime apenas navios a flutuar")
        void printFloatingShipsPrintsOnlyFloating() {
            Fleet fleet = new Fleet();

            IShip afloat = makeShip("Caravela", 0, 0, 0, 0, true, false);
            IShip sunk   = makeShip("Barca",    1, 1, 1, 1, false, false);

            assertTrue(fleet.addShip(afloat));
            assertTrue(fleet.addShip(sunk));

            String out = captureOutput(fleet::printFloatingShips);

            assertTrue(out.contains("MockShip[Caravela]"));
            assertFalse(out.contains("MockShip[Barca]"));
        }

        @Test
        @DisplayName("printStatus mostra o estado completo da frota")
        void printStatusCoversAllPrints() {
            Fleet fleet = new Fleet();

            IShip galeao   = makeShip("Galeao",   0, 0, 0, 0, true,  false);
            IShip fragata  = makeShip("Fragata",  1, 1, 1, 1, true,  false);
            IShip nau      = makeShip("Nau",      2, 2, 2, 2, true,  false);
            IShip caravela = makeShip("Caravela", 3, 3, 3, 3, true,  false);
            IShip barca    = makeShip("Barca",    4, 4, 4, 4, false, false);

            fleet.addShip(galeao);
            fleet.addShip(fragata);
            fleet.addShip(nau);
            fleet.addShip(caravela);
            fleet.addShip(barca);

            String out = captureOutput(fleet::printStatus);

            // Não precisamos de ser super rígidos, só garantir que aparecem
            assertTrue(out.contains("MockShip[Galeao]"));
            assertTrue(out.contains("MockShip[Fragata]"));
            assertTrue(out.contains("MockShip[Nau]"));
            assertTrue(out.contains("MockShip[Caravela]"));
            assertTrue(out.contains("MockShip[Barca]"));
        }
    }

    // =====================================================================
    // Helpers (IShip, IPosition, captura de System.out)
    // =====================================================================

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

    private static String captureOutput(Runnable action) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString();
    }
}

