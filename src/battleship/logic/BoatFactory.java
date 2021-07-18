package battleship.logic;

import battleship.pieces.*;

public class BoatFactory {

    // Takes care of building new boats so we don't end up with duplicates
    public Boat[] buildBoats(String[] order) {
        Boat[] boats = new Boat[order.length];

        for (int i = 0; i < order.length; i++) {
            boats[i] = buildBoat(order[i]);
        }
        return boats;
    }

    public Boat buildBoat(String str) {
        Boat boat;
        switch (str.toUpperCase()) {
            case "AIRCRAFT CARRIER":
                boat = new AircraftCarrier();
                break;
            case "BATTLESHIP":
                boat = new Battleship();
                break;
            case "SUBMARINE":
                boat = new Submarine();
                break;
            case "CRUISER":
                boat = new Cruiser();
                break;
            default:
                boat = new Destroyer();
                break;
        }
        return boat;
    }
}
