package model;

import model.exceptions.AdjacentStationException;
import model.exceptions.EmptyRouteException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

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

public class Route implements Writable {
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
    //EFFECT: add a station to the pathToDestination and returns true; if the station is not adjacent to the
    //        previous station in the route, throws AdjacentStationException
    public boolean addStation(Station station) throws AdjacentStationException {
        if (!this.pathToDestination.isEmpty()) {
            if (!this.pathToDestination.get(this.pathToDestination.size() - 1).getNextStations().contains(station)) {
                throw new AdjacentStationException();
            }
        }
        this.pathToDestination.add(station);
        return true;
    }

    //MODIFIES: this
    //EFFECT: variation of addStation, allows JSON to build routes regardless since we are making new objects
    public void addStationFromSave(Station station) {
        this.getPathToDestination().add(station);
    }

    //MODIFIES: this
    //EFFECT: remove the last added station from the pathToDestination; throws EmptyRouteException if the route is
    //        empty and no station can be removed
    public void removeStation() throws EmptyRouteException {
        if (this.pathToDestination.isEmpty()) {
            throw new EmptyRouteException();
        }
        this.pathToDestination.remove(this.pathToDestination.size() - 1);
    }

    //EFFECT: save this route as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("id", this.identification);
        json.put("start", this.start.toJson());
        json.put("end", this.end.toJson());
        json.put("route", stationsToJson());
        return json;
    }

    //EFFECT: save each station in the route as JSON array for the route
    public JSONArray stationsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Station s : this.getPathToDestination()) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
