package battleship.logic;

import battleship.utility.IOHelper;
import battleship.pieces.Boat;

import java.util.ArrayList;

public class BattleshipGame {

    private Gameboard player1Board;
    private Gameboard player2Board;
    private final IOHelper ioHelper;
    private Ruleset ruleset;
    private boolean player1Turn = true;



    public BattleshipGame(IOHelper ioHelper) {
        this.ioHelper = ioHelper;
    }







    /*
     * The following methods handle setting up the game boards before playing
     * Methods include initializing the board to a specific ruleset,
     * having the users set up their boards to their specifications,
     * and some additional helper methods
     */

    public void init(Ruleset ruleset) {
        this.ruleset = ruleset;
        player1Board = new Gameboard("Player 1", ruleset);
        player2Board = new Gameboard("Player 2", ruleset);

        populatePlayerGameboard(player1Board);
        passTurn();
        populatePlayerGameboard(player2Board);
        passTurn();
    }


    /*
     * set up the player's gameboard to their specifications
     */
    private void populatePlayerGameboard(Gameboard board) {
        ioHelper.output(board.getOwner() + ", place your ships on the game field");

        // get user input on where to place boats
        for (Boat boat : board.getBoats()) {
            ioHelper.output(board.toString());

            ioHelper.output(String.format("Enter the coordinates of the %s (%d cells):\n",
                    boat.getName(), boat.getLength()));

            boolean validPlacement = false;

            // ask user for coordinates until input is accepted
            do {
                String[] coords = ioHelper.getInput().split(" ");

                try {
                    validPlacement = tryPlace(board, boat,
                            parseCoordinate(coords[0]), parseCoordinate(coords[1]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException
                        | StringIndexOutOfBoundsException e) {
                    ioHelper.output("\nError! This is not a coordinate! Try again!\n");
                }
            } while (!validPlacement);
        }
        ioHelper.output(board.toString());
    }


    /*
     * Logic for checking various conditions to verify that the boat placement
     * is valid. Return false if it is not
     */
    private boolean tryPlace(Gameboard board, Boat boat, int[] coord1, int[] coord2) {

        if (coord1 == null || coord2 == null) {
            return false;
        }

        int y1 = coord1[0];
        int x1 = coord1[1];
        int y2 = coord2[0];
        int x2 = coord2[1];

        // space between given x and y coordinates for validation logic
        int lenX = Math.abs(x1 - x2) + 1;
        int lenY = Math.abs(y1 - y2) + 1;

        // check that both x or both y coordinates match, no diagonals!
        if ((lenX == 1) == (lenY == 1)) {
            ioHelper.output("\nError! Wrong ship location! Try again:\n");
            return false;
        }

        // check that the length of the coordinates matches the length of ship
        if ((lenX == boat.getLength()) == (lenY == boat.getLength())) {
            ioHelper.output(String.format("\nError! Wrong length of the %s! Try again:\n",
                    boat.getName()));
            return false;
        }

        // check that the boat is not too close to another boat
        if (!checkEnoughRoom(board, boat, x1, y1, x2, y2)) {
            ioHelper.output("\nError! You placed it too close to another one. Try again:\n");
            return false;
        }

        return true;
    }



    /*
     * Logic for checking that the boat has enough room to be placed
     * Only above and below are checked, not diagonals
     */
    private boolean checkEnoughRoom(Gameboard board, Boat boat, int x1, int y1, int x2, int y2) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);


        for (int i = minY - 1; i <= maxY + 1; i++) {

            // Skip row if out of bounds
            if (i < 0 || i >= board.getGameboard().length) {
                continue;
            }

            for (int j = minX - 1; j <= maxX + 1; j++) {

                // Skip column if out of bounds
                if (j < 0 || j >= board.getGameboard()[i].length) {
                    continue;
                }

                // Skip corners
                if (!(i == minY - 1 || i == maxY + 1)
                        && (j == minX - 1 || j == maxX + 1)) {
                    if (board.getGameboard()[i][j].getContentIdentifier() == 'O') {
                        return false; // cell already populated
                    }
                }
            }
        }

        // If all checks pass, send to gameboard to place the boat
        board.placeBoat(boat, minX, minY, maxX, maxY);

        return true;
    }






    /*
     * The following methods handle the general flow of the game
     * Methods include controlling the game flow, checking if user input
     * is an allowed move, and carrying out the turn
     */

    /*
     * Main body of the program, controls the game flow
     */
    public void play() {
        boolean gameOver = false;
        Gameboard playerBoard;
        Gameboard enemyBoard;
        int expectedShots = ruleset.getShotsPerTurn();

        // ArrayList of coords to fire on, for example, 5 shots in salvo mode
        ArrayList<int[]> coords;

        while (!gameOver) {

            // select correct boards depending whose turn it is
            if (player1Turn) {
                playerBoard = player1Board;
                enemyBoard = player2Board;
            } else {
                playerBoard = player2Board;
                enemyBoard = player1Board;
            }

            // Display current board before the player takes their turn
            displayBoards(enemyBoard, playerBoard);
            ioHelper.output(playerBoard.getOwner() + " , it's your turn:\n");

                boolean validCoords = false;

                // Loop until we have valid coordinates to fire on
                do {
                    coords = new ArrayList<>();
                    String[] strCoords = ioHelper.getInput().split("\\s+");

                    // Check that user has entered the expected number of coordinates
                    if (strCoords.length != expectedShots) {
                        ioHelper.output("\nYou have entered an incorrect number of coordinates!\n");
                        continue;
                    }

                    // Check for duplicate values in input
                    if (expectedShots > 1 && checkForDuplicates(strCoords)) {
                        ioHelper.output("\nYou have entered two of the same coordinate!\n");
                        continue;
                    }

                    // Parse each coordinate and check that they are valid
                    for (String str : strCoords) {
                        int[] coord;

                        try {
                            coord = parseCoordinate(str);
                        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                            ioHelper.output("\nError! This is not a coordinate! Try again!\n");
                            break;
                        }

                        //Verify not out of bounds or already selected
                        if (coord[0] < 0 || coord[0] >= enemyBoard.getGameboard().length ||
                                coord[1] < 0 || coord[1] >= enemyBoard.getGameboard().length) {
                            ioHelper.output("\nError! You entered the wrong coordinates! Try again:\n");
                        } else if (enemyBoard.getObfuscatedBoard()[coord[0]][coord[1]]
                                .getContentIdentifier() != '~') {
                            ioHelper.output("\nYou have already fired on this location!\n");
                            break;
                        } else {
                            coords.add(coord);
                        }

                    }

                    // end loop if we have the correct number of coordinates
                    validCoords = coords.size() == expectedShots;
                } while (!validCoords);


                // fire on each coordinate queued
                StringBuilder turnResults = new StringBuilder();
                for (int i = 0; i < coords.size(); i++) {

                    String result = fireOn(enemyBoard, coords.get(i));
                    if (coords.size() > 1) {
                        turnResults.append("Turn ").append(i + 1).append(": ");
                    }
                    turnResults.append(result);
                }
                ioHelper.output(turnResults.toString());

                // Check for game over
                if (enemyBoard.checkAllDestroyed()) {
                    gameOver = true;
                    ioHelper.output(playerBoard.getOwner()
                            + ", you sank the last ship! You won! Congratulations!");
                }

                if (!gameOver) {
                    passTurn();
                }

        }
    }

    /*
     * Handle the act of firing at a cell and finding the result
     */
    private String fireOn(Gameboard board, int[] coord) {
        Cell cell = board.getGameboard()[coord[0]][coord[1]];
        String result = "";

        switch (cell.getContentIdentifier()) {
            case 'O':
                cell.setContents(Cell.Contents.SHIP_HIT);
                Boat boat = cell.getBoat();
                if (boat.isDestroyed()) {
                    result = String.format("You sank a %s!\n",
                            boat.getName());
                } else {
                    result = "You hit a ship!\n";
                }
                break;
            case '~':
                cell.setContents(Cell.Contents.MISSED_SHOT);
                result = "You missed!\n";
            }
        return result;
    }






    /*
     * The following methods are more universal utility that is used
     * in both setting up and playing. This includes checking that coordinates
     * are valid, passing the turn to the next player, and showing a game
     * representation of the boards to the users
     */

    /*
     * Parse a string received from user into usable coordinates
     */
    private int[] parseCoordinate(String coord) throws NumberFormatException, StringIndexOutOfBoundsException {
        int x;
        int y;

        x = Integer.parseInt(coord.substring(1)) - 1;
        y = coord.charAt(0) - 65;

        return new int[] {y, x};
    }

    // Request the player to pass the game, then switch the turn once they hit enter
    private void passTurn() {
        ioHelper.output("Press Enter and pass the move to another player\n...");
        ioHelper.getInput();
        player1Turn = !player1Turn;
    }

    // Display obfuscated enemy board, a separator, then the player's board
    private void displayBoards(Gameboard enemyBoard, Gameboard playerBoard) {

        String str = enemyBoard.toString(true) +
                "-".repeat(enemyBoard.getSize() * 2 + 1) +
                playerBoard.toString();
        ioHelper.output(str);
    }

    // Check inputs for duplicates
    private boolean checkForDuplicates(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j].equals(arr[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
