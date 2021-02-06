package model;

import java.util.ArrayList;

/**
 * Representation of a travel plan from point A to point B. A route consists of a starting point station, a
 * destination station, a list of stations going from the starting point to the end point, and a route name and id.
 * The route name and id is used to distinguish routes in the planner.
 *
 * A route can be planned as long as a starting station and end station are given and the stations are initialized
 * (default Tokyo). A route cannot be edited after made, besides changing the name.
 */

public class Route {

    //getters
    public String getName() {
        return null;
    }

    public int getIdentification() {
        return 0;
    }

    public Station getStartPoint() {
        return null;
    }

    public Station getEndPoint() {
        return null;
    }

    public ArrayList<Station> getPathToDestination() {
        return null;
    }

    //setters
    public void setName() {

    }

    public void setIdentification() {

    }

    //REQUIRES: the starting and end stations belong in the same metro system
    //MODIFIES: this
    //EFFECT: make a route plan from the start point to the end point by traversing through adjacent stations
    public void planRoute(Station start, Station end) {

    }

    //EFFECT: print the route id, name, and total number of stations
    public void viewRoute() {

    }

    //EFFECT: print the route id, name, start and end, list of stations in the route, and total number of stations
    public void viewRouteDetailed() {

    }
}
