package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a travel plan from point A to point B. A route consists of a starting point station, a
 * destination station, a list of stations going from the starting point to the end point, and a route name and id.
 * The route name and id is used to distinguish routes in the planner.
 *
 * A route can be planned as long as a starting station is given and the stations are initialized (default Tokyo).
 * A route cannot be edited after made, besides changing the name. addStation builds a route in conjunction with
 * user inputs in MetroFinderApp.
 */

public class Route {
    private String name;
    private int identification;
    private Station start;
    private Station end;
    private final List<Station> pathToDestination;

    //EFFECT: construct a route with only a route name. Other fields need to be filled with planRoute. Identification
    //        of 0 indicates it has not been given a unique id by the planner
    public Route(String name) {
        this.name = name;
        this.identification = 0;
        this.start = null;
        this.end = null;
        this.pathToDestination = new ArrayList<>();
    }

    //getters
    public String getName() {
        return this.name;
    }

    public int getIdentification() {
        return this.identification;
    }

    public Station getStartPoint() {
        return this.start;
    }

    public Station getEndPoint() {
        return this.end;
    }

    public List<Station> getPathToDestination() {
        return this.pathToDestination;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setIdentification(int id) {
        this.identification = id;
    }

    public void setStart(Station start) {
        this.start = start;
    }

    public void setEnd(Station end) {
        this.end = end;
    }

    //MODIFIES: this
    //EFFECT: add a station to the pathToDestination, returns true if successful
    public boolean addStation(Station station) {
        if (this.getPathToDestination().isEmpty()) {
            this.pathToDestination.add(station);
            return true;
        } else if (this.pathToDestination.get(this.pathToDestination.size() - 1).equals(station)) {
            return false;
        }
        this.pathToDestination.add(station);
        return true;
    }

    //REQUIRES: pathToDestination for this is not empty
    //MODIFIES: this
    //EFFECT: remove the last added station from the pathToDestination
    public void removeStation() {
        this.pathToDestination.remove(this.pathToDestination.size() - 1);
    }
}
