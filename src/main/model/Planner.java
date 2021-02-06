package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a journal of planned routes made by the user. A planner consists of all the planned routes that are
 * currently saved, the route that is being taken, routes that have been completed, and a tracker to label new routes
 * that are being made.
 *
 * A planner can add new routes, remove routes, check off routes as completed, remove completed routes from the
 * planner, tally the number of stations visited (not unique), and set a route as a current travel plan
 */

public class Planner {
    private List<Route> plannedRoutes;
    private Route currentRoute;
    private List<Route> completedRoutes;
    private int routeIdTracker;

    //EFFECT: constructs a planner with no planned, current, or completed routes
    public Planner() {
        plannedRoutes = new ArrayList<>();
        currentRoute = null;
        completedRoutes = new ArrayList<>();
        routeIdTracker = 1;
    }

    //getters
    public Route getCurrentRoute() {
        return this.currentRoute;
    }

    public List<Route> getPlannedRoutes() {
        return this.plannedRoutes;
    }

    public List<Route> getCompletedRoutes() {
        return this.completedRoutes;
    }

    public int getRouteIdTracker() {
        return this.routeIdTracker;
    }

    //EFFECT: print the contents of the planner (current, planned, and completed routes along with statistics)
    public void viewPlanner() {
        System.out.println("Your Current Route");
        System.out.println("-------------------------------------");
        if (this.currentRoute != null) {
            this.currentRoute.viewRoute();
        } else {
            System.out.println("No current route selected");
        }
        System.out.println("-------------------------------------");
        System.out.println("Your Planned Routes");
        System.out.println("-------------------------------------");
        for (Route r : this.plannedRoutes) {
            r.viewRoute();
        }
        System.out.println("-------------------------------------");
        System.out.println("Your Completed Routes");
        System.out.println("-------------------------------------");
        for (Route r : this.completedRoutes) {
            r.viewRoute();
        }
        System.out.println("-------------------------------------");
        System.out.println("Total # of stations visited: " + tallyStations());
    }


    //REQUIRES: currentRoute is not null
    //MODIFIES: this
    //EFFECT: sets the current route being taken as completed, then removes it as the current route into the list of
    //        completed routes
    public void completeRoute() {
        this.completedRoutes.add(this.currentRoute);
        this.currentRoute = null;
    }

    //REQUIRES: the route being passed is part of the planned routes, not completed routes
    //MODIFIES: this
    //EFFECT: sets the route being passed as the new current route. If there was a current route already set, move it
    //        to the list of planned routes
    public void newCurrentRoute(Route route) {
        if (this.currentRoute == null) {
            this.currentRoute = route;
            this.plannedRoutes.removeIf(r -> r.equals(this.currentRoute));
        } else {
            this.plannedRoutes.add(this.currentRoute);
            this.currentRoute = route;
            this.plannedRoutes.removeIf(r -> r.equals(this.currentRoute));
        }
    }

    //REQUIRES: the route does not already have an identification number
    //MODIFIES: route
    //EFFECT: assigns a unique identification number to the route so that it can be easily accessed
    public void assignIdentification(Route route) {
        route.setIdentification(this.getRouteIdTracker());
        this.routeIdTracker++;
    }

    //EFFECT: total the number of stations visited from the list of completed routes
    public int tallyStations() {
        int count = 0;
        for (Route r : this.completedRoutes) {
            count += r.getPathToDestination().size();
        }

        return count;
    }
}
