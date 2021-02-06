package model;

import java.util.ArrayList;

/**
 * Representation of a metro line, a specified list of stations a train will pass through. Each line consists of
 * the line name, identification (letter and colour for Tokyo Metro), list of stations belonging to that line, and
 * a list of other lines that may share any stations as this one.
 *
 * Lines are initialized based on the metro system chosen (default Tokyo)
 */

public class Line {

    //getters
    public String getName() {
        return null;
    }

    public String getIdentification() {
        return null;
    }

    public ArrayList<Station> getStations() {
        return null;
    }

    public ArrayList<Line> getTransfers() {
        return null;
    }

    //EFFECT: print relevant info about the line (list of stations including terminal stations, name of line, line
    //        identification symbol and colour, and other lines that intersect with it
    public void viewLineInfo() {

    }
}
