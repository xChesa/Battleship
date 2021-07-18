package battleship.logic;

import battleship.pieces.*;

public class Gameboard {

    private final Cell[][] gameboard;
    private final Boat[] boats;
    private final String owner;


    public Gameboard(String owner, Ruleset ruleset) {
        this.owner = owner;
        this.gameboard = new Cell[ruleset.getBoardHeight()][ruleset.getBoardWidth()];
        this.boats = ruleset.getBoats();

        // Fill gameboard with empty cells
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                gameboard[i][j] = new Cell(i, j);
            }
        }
    }


    /*
     * After placement is verified correct, this method is used for
     * updating all relevant cells with the new contents
     */
    void placeBoat(Boat boat, int minX, int minY, int maxX, int maxY) {
        Cell[] cells = new Cell[boat.getLength()];

        int count = 0;
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                gameboard[i][j].setContents(Cell.Contents.SHIP);
                gameboard[i][j].setBoat(boat);
                cells[count++] = (gameboard[i][j]);
            }
        }

        boat.setCells(cells);
    }

    Cell[][] getGameboard() {
        return gameboard;
    }


    /*
     * Return an obfuscated version of the game board (what the opponent sees)
     */
    Cell[][] getObfuscatedBoard() {
        Cell[][] obfuscated = new Cell[gameboard.length][gameboard.length];

        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard[i].length; j++) {
                char identifier = gameboard[i][j].getContentIdentifier();
                obfuscated[i][j] = new Cell(i, j);

                if (identifier == 'O') {
                    obfuscated[i][j].setContents(Cell.Contents.WATER);
                } else {
                    obfuscated[i][j].setContents(gameboard[i][j].getContent());
                }
            }
        }
        return obfuscated;
    }

    Boat[] getBoats() {
        return boats;
    }


    /*
     * Methods for creating a string representation of the game board
     */
    @Override
    public String toString() {
        return toString(gameboard);
    }

    public String toString(boolean obfuscated) {
        return obfuscated ? toString(getObfuscatedBoard()) : toString();
    }

    public String toString(Cell[][] gameboard) {

        StringBuilder str = new StringBuilder();

        str.append("\n ");
        for (int i = 1; i <= gameboard.length; i++) {
            str.append(" ").append(i);
        }


        for (int i = 0; i < gameboard.length; i++) {
            str.append("\n").append((char) (i + 65));
            for (int j = 0; j < gameboard[i].length; j++) {
                char identifier = gameboard[i][j].getContentIdentifier();
                str.append(" ").append(identifier);
            }
        }
        str.append("\n");

        return str.toString();
    }


    // Check for win condition (all boats present on board are destroyed)
    public boolean checkAllDestroyed() {
        for (Boat boat : boats) {
            if (!boat.isDestroyed()) {
                return false;
            }
        }
        return true;
    }


    public String getOwner() {
        return owner;
    }

    public int getSize() { return gameboard.length; }
}
