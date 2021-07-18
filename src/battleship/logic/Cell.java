package battleship.logic;

import battleship.pieces.Boat;

public class Cell {

    int posX;
    int posY;
    Contents contents;
    Boat boat;

    public Cell(int posY, int posX) {
        this.posY = posY;
        this.posX = posX;
        setContents(Contents.WATER);
    }

    // Update the contents of the cell (empty -> boat -> hit)
    void setContents(Contents contents) {
        this.contents = contents;
        if (contents == Contents.SHIP_HIT && boat != null) {
            boat.checkDestroyed();
        }
    }

    void setBoat(Boat boat) {
        this.boat = boat;
    }

    Boat getBoat() {
        return boat;
    }

    char getContentIdentifier() {
        return contents.getIdentifier();
    }

    public boolean isHit() {
        return getContent() == Contents.SHIP_HIT;
    }

    Contents getContent() { return contents; }

    enum Contents {

        WATER("Water", '~'),
        SHIP("A ship", 'O'),
        SHIP_HIT("A ship that was hit", 'X'),
        MISSED_SHOT("A missed shot", 'M');


        String content;
        char identifier;

        Contents(String content, char identifier) {
            this.content = content;
            this.identifier = identifier;
        }

        char getIdentifier() {
            return identifier;
        }
    }
}
