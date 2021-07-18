package battleship.utility;

import java.util.Scanner;

public class ConsoleIOHelper extends IOHelper {

    Scanner scan;

    public ConsoleIOHelper() {
        scan = new Scanner(System.in);
    }

    public void close() {
        scan.close();
    }

    @Override
    public String getInput() {
        return scan.nextLine().trim();
    }

    @Override
    public void output(String str) {
        System.out.println(str);
    }
}
