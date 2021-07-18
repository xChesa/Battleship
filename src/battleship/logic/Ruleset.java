package battleship.logic;

import battleship.pieces.*;

public enum Ruleset {

    CLASSIC(10, 10, 1, new String[] {
            "Aircraft Carrier",
            "Battleship",
            "Submarine",
            "Cruiser",
            "Destroyer"
    }),
    SALVO(10, 10, 5, new String[] {
            "Aircraft Carrier",
            "Battleship",
            "Submarine",
            "Cruiser",
            "Destroyer"
    }),
    BATTLESHIPS(10, 10, 5, new String[] {
            "Battleship",
            "Battleship",
            "Battleship",
            "Battleship",
            "Battleship"
    })/*,
    CUSTOM(0, 0, 0, new String[0])*/;

        int boardWidth;
        int boardHeight;
        int shotsPerTurn;
        String[] boats;

        Ruleset(int boardWidth, int boardHeight, int shotsPerTurn, String[] boats) {
            initCustomRuleset(boardWidth, boardHeight, shotsPerTurn, boats);
        }

        void initCustomRuleset(int boardWidth, int boardHeight, int shotsPerTurn, String[] boats) {
            this.boardWidth = boardWidth;
            this.boardHeight = boardHeight;
            this.shotsPerTurn = shotsPerTurn;
            this.boats = boats;
        }

        int getBoardWidth() {
            return boardWidth;
        }

        int getBoardHeight() {
            return boardHeight;
        }

        int getShotsPerTurn() {
            return shotsPerTurn;
        }


        // Build a copy of the boats from the ruleset template
        Boat[] getBoats() {
            BoatFactory factory = new BoatFactory();
            return factory.buildBoats(boats);
        }
}
