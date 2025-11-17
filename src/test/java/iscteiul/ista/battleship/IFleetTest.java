package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IFleetTest {

    @Test
    @DisplayName("constantes: BOARD_SIZE presente e igual a 10")
    void boardSizeConstant() throws Exception {
        Field f = IFleet.class.getField("BOARD_SIZE");
        assertEquals(Integer.class, f.getType());
        assertEquals(Integer.valueOf(10), f.get(null));
    }

    @Test
    @DisplayName("constantes: FLEET_SIZE presente e igual a 10")
    void fleetSizeConstant() throws Exception {
        Field f = IFleet.class.getField("FLEET_SIZE");
        assertEquals(Integer.class, f.getType());
        assertEquals(Integer.valueOf(10), f.get(null));
    }

    @Test
    @DisplayName("método: getShips() retorno List<IShip>")
    void getShipsMethod() throws Exception {
        Method m = IFleet.class.getMethod("getShips");
        assertEquals(List.class, m.getReturnType());
        assertEquals(0, m.getParameterCount());
    }

    @Test
    @DisplayName("método: addShip(IShip) retorno boolean")
    void addShipMethod() throws Exception {
        Method m = IFleet.class.getMethod("addShip", IShip.class);
        assertEquals(boolean.class, m.getReturnType());
        assertEquals(1, m.getParameterCount());
    }

    @Test
    @DisplayName("método: getShipsLike(String) retorno List<IShip>")
    void getShipsLikeMethod() throws Exception {
        Method m = IFleet.class.getMethod("getShipsLike", String.class);
        assertEquals(List.class, m.getReturnType());
        assertEquals(1, m.getParameterCount());
    }

    @Test
    @DisplayName("método: getFloatingShips() retorno List<IShip>")
    void getFloatingShipsMethod() throws Exception {
        Method m = IFleet.class.getMethod("getFloatingShips");
        assertEquals(List.class, m.getReturnType());
        assertEquals(0, m.getParameterCount());
    }

    @Test
    @DisplayName("método: shipAt(IPosition) retorno IShip")
    void shipAtMethod() throws Exception {
        Method m = IFleet.class.getMethod("shipAt", IPosition.class);
        assertEquals(IShip.class, m.getReturnType());
        assertEquals(1, m.getParameterCount());
    }

    @Test
    @DisplayName("método: printStatus() retorno void")
    void printStatusMethod() throws Exception {
        Method m = IFleet.class.getMethod("printStatus");
        assertEquals(void.class, m.getReturnType());
        assertEquals(0, m.getParameterCount());
    }
}
