package battleship.pieces;

import battleship.logic.Cell;

public abstract class Boat {
    String name;
    int length;
    Cell[] cells;
    boolean destroyed = false;

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    // Check whether a ship is destroyed
    public boolean checkDestroyed() {
        for (Cell cell : cells) {
            if (!cell.isHit()) {
                return false;
            }
        }
        return destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
