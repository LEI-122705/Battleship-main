package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
    @DisplayName("addShip: rejeita navio fora do tabuleiro")
    void addShipOutsideBoard() {
        Fleet fleet = new Fleet();

        IShip outside = makeShip("Nau", 0, IFleet.BOARD_SIZE, 0, 0, true, false);
        boolean added = fleet.addShip(outside);

        assertFalse(added);
        assertTrue(fleet.getShips().isEmpty());
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

        IShip occupier = makeShip("Galeao", 0, 0, 0, 0, true, false, (other) -> false, (position) -> position == pos);
        assertTrue(fleet.addShip(occupier));

        IShip found = fleet.shipAt(pos);
        assertSame(occupier, found);
    }

    //helpers para criar proxies de IShip e IPosition

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
