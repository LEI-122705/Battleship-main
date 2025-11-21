/**
 *
 */
package iscteiul.ista.battleship;

public class Caravel extends Ship {
    private static final Integer SIZE = 2;
    private static final String NAME = "Caravela";

    /**
     * @param bearing the bearing where the Caravel heads to
     * @param pos     initial point for positioning the Caravel
     */
    public Caravel(Compass bearing, IPosition pos) throws NullPointerException, IllegalArgumentException {
        super(Caravel.NAME, bearing, pos);

        switch (bearing) {

            case NORTH:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow() + 1, pos.getColumn()));
                break;

            case SOUTH:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow() - 1, pos.getColumn()));
                break;

            case EAST:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow(), pos.getColumn() + 1));
                break;

            case WEST:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow(), pos.getColumn() - 1));
                break;

            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for the caravel");
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.Ship#getSize()
     */
    @Override
    public Integer getSize() {
        return SIZE;
    }

}
