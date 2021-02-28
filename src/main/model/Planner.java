package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

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

public class Planner implements Writable {
    private final List<Route> plannedRoutes;
    private Route currentRoute;
    private final List<Route> completedRoutes;
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

    //setters
    public void setIdTracker(int id) {
        this.routeIdTracker = id;
    }

    //MODIFIES: this
    //EFFECT: sets the current route being taken as completed, then removes it as the current route into the list of
    //        completed routes; if the current route is null, nothing happens
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

    //EFFECT: save this planner as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (this.currentRoute != null) {
            json.put("current", this.currentRoute.toJson());
        }
        if (!this.plannedRoutes.isEmpty()) {
            json.put("planned", plannedRoutesToJson());
        }
        if (!this.completedRoutes.isEmpty()) {
            json.put("completed", completedRoutesToJson());
        }
        json.put("id", this.routeIdTracker);
        return json;
    }

    //EFFECT: save the planned routes from the planner as JSON array
    public JSONArray plannedRoutesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Route r : this.getPlannedRoutes()) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }

    //EFFECT: save the completed routes from the planner as JSON array
    public JSONArray completedRoutesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Route r : this.getCompletedRoutes()) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }
}
