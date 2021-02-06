package model;

import java.util.ArrayList;

/**
 * Represents a journal of planned routes made by the user. A planner consists of all the planned routes that are
 * currently saved, the route that is being taken, routes that have been completed, and a tracker to label new routes
 * that are being made.
 *
 * A planner can add new routes, remove routes, check off routes as completed, remove completed routes from the
 * planner, tally the number of stations visited (not unique), and set a route as a current travel plan
 */

public class Planner {

    //getters
    public Route getCurrentRoute() {
        return null;
    }

    public ArrayList<Route> getPlannedRoutes() {
        return null;
    }

    public ArrayList<Route> getCompletedRoutes() {
        return null;
    }

    //EFFECT: print the contents of the planner (current, planned, and completed routes along with statistics)
    public void viewPlanner() {

    }

    //MODIFIES: this
    //EFFECT: sets the current route being taken as completed, then removes it as the current route into the list of
    //        completed routes
    public void completeRoute() {

    }

    //REQUIRES: the route being passed is part of the planned routes, not completed routes
    //MODIFIES: this
    //EFFECT: sets the route being passed as the new current route. If there was a current route already set, move it
    //        to the list of planned routes
    public void newCurrentRoute(Route route) {

    }

    //REQUIRES: the route does not already have an identification number
    //MODIFIES: route
    //EFFECT: assigns a unique identification number to the route so that it can be easily accessed
    public void assignIdentification(Route route) {

    }

    //EFFECT: total the number of stations visited from the list of completed routes
    public int tallyStations() {
        return 0;
    }
}
