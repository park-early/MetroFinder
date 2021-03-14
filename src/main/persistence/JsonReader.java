package persistence;

import model.Planner;
import model.Route;
import model.Station;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a reader that reads Planner from JSON data stored in file
 *
 * Most methods from JsonSerializationDemo
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Planner from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public Planner read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePlanner(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Planner from JSON object and returns it
    private Planner parsePlanner(JSONObject jsonObject) {
        Planner p = new Planner();
        addPlanned(p, jsonObject);
        addCompleted(p, jsonObject);
        addCurrent(p, jsonObject);
        addId(p, jsonObject);
        return p;
    }

    // MODIFIES: p
    // EFFECTS: parses planned routes from JSON object and adds them to the planner
    private void addPlanned(Planner p, JSONObject jsonObject) {
        JSONArray routes = jsonObject.getJSONArray("planned");
        if (routes.length() != 0) {
            for (Object json : routes) {
                JSONObject nextRoute = (JSONObject) json;
                p.getPlannedRoutes().add(buildRoute(nextRoute));
            }
        }
    }

    // MODIFIES: p
    // EFFECTS: parses completed routes from JSON object and adds them to the planner
    private void addCompleted(Planner p, JSONObject jsonObject) {
        JSONArray routes = jsonObject.getJSONArray("completed");
        if (routes.length() != 0) {
            for (Object json : routes) {
                JSONObject nextRoute = (JSONObject) json;
                p.getCompletedRoutes().add(buildRoute(nextRoute));
            }
        }
    }

    // MODIFIES: p
    // EFFECTS: parses current route from JSON object and adds to the planner
    private void addCurrent(Planner p, JSONObject jsonObject) {
        if (jsonObject.getBoolean("currentExists")) {
            Route route = buildRoute(jsonObject.getJSONObject("current"));
            p.newCurrentRoute(route);
        }
    }

    //MODIFIES: p
    //EFFECT: parses id from JSON object and adds to the planner
    private void addId(Planner p, JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        p.setIdTracker(id);
    }

    //MODIFIES: jsonObject (Route)
    //EFFECT: parses the route from JSON object then returns it
    private Route buildRoute(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        JSONObject start = jsonObject.getJSONObject("start");
        Station startStation = new Station(start.getString("name"), null);
        JSONObject end = jsonObject.getJSONObject("end");
        Station endStation = new Station(end.getString("name"), null);
        JSONArray stations = jsonObject.getJSONArray("route");

        Route route = new Route(name);
        route.setIdentification(id);
        route.setStart(startStation);
        route.setEnd(endStation);

        for (Object json : stations) {
            JSONObject nextStation = (JSONObject) json;
            addStationToRoute(route, nextStation);
        }
        return route;
    }

    // MODIFIES: p
    // EFFECTS: parses station from JSON object and adds it to the route
    private void addStationToRoute(Route r, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Station station = new Station(name, null);
        r.addStationFromSave(station);
    }
}
