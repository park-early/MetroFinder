package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a station. Each station consists of the station name, the lines it belongs to, and a list of
 * adjacent stations that can be travelled to from this one.
 *
 * Stations are initialized based on the metro system chosen (default Tokyo)
 */

public class Station {
    private final String name;
    private List<Line> line;
    private List<Station> nextStations;

    //EFFECT: constructs a new station with a name and line it belongs to. Additional lines and adjacent stations must
    //        be added separately
    public Station(String name, Line line) {
        this.name = name;
        this.line = new ArrayList<>();
        this.line.add(line);
        this.nextStations = new ArrayList<>();
    }

    //getters
    public String getName() {
        return this.name;
    }

    public List<Line> getLine() {
        return this.line;
    }

    public List<Station> getNextStations() {
        return this.nextStations;
    }

    //EFFECT: print relevant info about the station (station name, what line it belongs to, and any adjacent stations)
    public void viewStationInfo() {
        System.out.println(this.name);
        System.out.println("This station belongs to the following lines:");
        for (Line l : this.line) {
            System.out.println(l.getName());
        }
        System.out.println("From this station you can travel to:");
        for (Station s : this.nextStations) {
            System.out.println(s.getName());
        }
    }
}
