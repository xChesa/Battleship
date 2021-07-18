package battleship;

import battleship.logic.BattleshipGame;
import battleship.logic.Ruleset;
import battleship.utility.ConsoleIOHelper;

import java.util.InputMismatchException;

public class Main {

    private static ConsoleIOHelper ioHelper;

    public static void main(String[] args) {
        ioHelper = new ConsoleIOHelper();
        BattleshipGame game = new BattleshipGame(ioHelper);

        // Get options from the user and have the game start accordingly
        do {
            Ruleset ruleset = chooseGamemode();

            ioHelper.output("Selected " + ruleset.name() + "\n");

            game.init(ruleset);
            game.play();

            ioHelper.output("Would you like to play again? (Y/N)");

        } while (ioHelper.getInput().trim().equalsIgnoreCase("y"));

        ioHelper.output("Goodbye!");
        ioHelper.close();
    }

    // Helper method for letting the user choose the game mode
    private static Ruleset chooseGamemode() {
        ioHelper.output("Enter the number of the game mode that you wouldl ike to play: " +
                "1) Classic   2) Salvo   3) ALL BATTLESHIPS?!");

        do {
            int selection;

            try {
                selection = Integer.parseInt(ioHelper.getInput().trim());
            } catch (InputMismatchException e) {
                selection = -1;
            }

            switch (selection) {
                case 1:
                    return Ruleset.CLASSIC;
                case 2:
                    return Ruleset.SALVO;
                case 3:
                    return Ruleset.BATTLESHIPS;
                default:
                    ioHelper.output("Invalid selection");
                    break;
            }
        } while (true);
    }
}
