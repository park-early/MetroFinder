package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a metro line, a specified list of stations a train will pass through. Each line consists of
 * the line name, identification (letter and colour for Tokyo Metro), list of stations belonging to that line, and
 * a list of other lines that may share any stations as this one.
 *
 * Lines are initialized based on the metro system chosen (default Tokyo)
 */

public class Line {
    private final String name;
    private final String id;
    private List<Station> stations;
    private List<Line> transfers;

    //EFFECT: constructs a new line with a name, identification, and empty stations and transfers
    public Line(String name, String id) {
        this.name = name;
        this.id = id;
        this.stations = new ArrayList<>();
        this.transfers = new ArrayList<>();
    }

    //getters
    public String getName() {
        return this.name;
    }

    public String getIdentification() {
        return this.id;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public List<Line> getTransfers() {
        return this.transfers;
    }

    //EFFECT: print relevant info about the line (list of stations including terminal stations, name of line, line
    //        identification symbol and colour, and other lines that intersect with it
    public void viewLineInfo() {
        int count = 1;
        System.out.println(this.name + " Line - Identifier: " + this.id);
        System.out.println("Stations:");
        for (Station s : this.stations) {
            System.out.println(count + ". " + s.getName());
            count++;
        }
        System.out.println("Lines that can be transferred to from this one:");
        for (Line t : this.transfers) {
            System.out.println(t.name);
        }
    }
}
