/**
 *
 */
package iscteiul.ista.battleship;

public class Carrack extends Ship {
    private static final Integer SIZE = 3;
    private static final String NAME = "Nau";

    /**
     * @param bearing
     * @param pos
     */
    public Carrack(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Carrack.NAME, bearing, pos);

        switch (bearing) {

            case NORTH:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow() + 1, pos.getColumn()));
                positions.add(new Position(pos.getRow() + 2, pos.getColumn()));
                break;

            case SOUTH:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow() - 1, pos.getColumn()));
                positions.add(new Position(pos.getRow() - 2, pos.getColumn()));
                break;

            case EAST:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow(), pos.getColumn() + 1));
                positions.add(new Position(pos.getRow(), pos.getColumn() + 2));
                break;

            case WEST:
                positions.add(new Position(pos.getRow(), pos.getColumn()));
                positions.add(new Position(pos.getRow(), pos.getColumn() - 1));
                positions.add(new Position(pos.getRow(), pos.getColumn() - 2));
                break;

            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for the carrack");
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see battleship.Ship#getSize()
     */
    @Override
    public Integer getSize() {
        return Carrack.SIZE;
    }

}
