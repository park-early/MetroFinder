package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MetroFinderApp {
    private static final String JSON_STORE = "./data/planner.json";
    private Planner planner;
    private final Tokyo tokyo;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    //EFFECT: constructs and sets up the MetroFinderApp
    public MetroFinderApp() {
        planner = new Planner();
        tokyo = new Tokyo();
        tokyo.initializeTokyo();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        runMetroFinderApp();
    }

    //EFFECT: runs MetroFinderApp
    //code body from TellerApp
    //https://github.students.cs.ubc.ca/CPSC210/TellerApp.git
    public void runMetroFinderApp() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommandMainMenu(command);
            }
        }

        savePlanner();
        System.out.println("\nSee you next time!");
    }

    //EFFECT: displays menu for user to choose how to interact with the app
    public void displayMainMenu() {
        System.out.println("-------------------------------------");
        System.out.println("Welcome to MetroFinder");
        System.out.println("-------------------------------------");
        System.out.println("How can I help?");
        System.out.println("l -> View lines in " + tokyo.getName());
        System.out.println("p -> View your planner");
        System.out.println("q -> Save and quit app");
        System.out.println("load -> Load your saved planner");
        // "M -> Change metro system"  -------> eventually
    }

    //EFFECT: redirect console display to a new menu based on user input
    public void processCommandMainMenu(String command) {
        command = command.toLowerCase();
        switch (command) {
            case "l":
                displayLines();
                break;
            case "p":
                displayPlanner();
                break;
            case "load":
                loadPlanner();
                break;
            default:
                System.out.println("Sorry, that option doesn't exist");
                break;
        }
    }

    //EFFECT: display menu of all lines in the metro system
    public void displayLines() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            for (Line l : tokyo.getLines()) {
                System.out.println(l.getName() + " Line - " + l.getIdentification());
            }
            System.out.println("-------------------------------------");
            System.out.println("Enter a line name to learn more\nor enter \"b\" to go back");

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                displayLineDetails(command);
            }
        }
    }

    //EFFECT: display detailed information about a line
    public void displayLineDetails(String command) {
        boolean keepGoing = false;
        boolean badInput = true;
        Line line = null;

        for (Line l : tokyo.getLines()) {
            if (l.getName().toLowerCase().equals(command)) {
                viewLineInfo(l);
                keepGoing = true;
                line = l;
                badInput = false;
            }
        }

        processCommandLineMenu(keepGoing, badInput, line);
    }

    //EFFECT: process user input at the menu displaying detailed line info
    private void processCommandLineMenu(boolean keepGoing, boolean badInput, Line line) {
        String command;
        while (badInput) {
            System.out.println("Sorry, that option doesn't exist. Try something else");
            badInput = false;
        }

        while (keepGoing) {
            System.out.println("-------------------------------------");
            System.out.println("Enter a station number to learn more\nor enter \"b\" to go back");
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                displayStationDetails(command, line);
            }
        }
    }

    //EFFECT: display detailed information about a station
    public void displayStationDetails(String command, Line line) {
        boolean badInput = true;

        for (int i = 0; i < line.getStations().size(); i++) {
            String num = String.valueOf(i + 1);
            if (num.equals(command)) {
                viewStationInfo(line.getStations().get(i));
                badInput = false;
            }
        }

        if (badInput) {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //EFFECT: display menu of the user's planner
    public void displayPlanner() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            viewPlanner();
            System.out.println("-------------------------------------");
            System.out.println("Enter a route id to learn more"
                    + "\nEnter \"m\" to plan a new route"
                    + "\nEnter \"c\" to change your current route"
                    + "\nEnter \"r\" to remove a route"
                    + "\nEnter \"b\" to go back");

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else  if (command.equals("c")) {
                displayCurrentRouteMenu();
            } else  if (command.equals("r")) {
                displayRemoveRouteMenu();
            } else {
                processCommandPlannerMenu(command);
            }
        }
    }

    //EFFECT: redirect console display to a new menu based on user input
    public void processCommandPlannerMenu(String command) {
        if (command.equals("m")) {
            displayRouteMaker();
        } else {
            displayRouteInfo(command);
        }
    }

    //EFFECT: display detailed information about a route
    public void displayRouteInfo(String command) {
        Route route = null;

        if (findRouteInPlanned(command) != null) {
            route = findRouteInPlanned(command);
        } else if (findRouteInCompleted(command) != null) {
            route = findRouteInCompleted(command);
        } else {
            String id = String.valueOf(this.planner.getCurrentRoute().getIdentification());
            if (id.equals(command)) {
                route = this.planner.getCurrentRoute();
            } else {
                System.out.println("Sorry, that option doesn't exist");
            }
        }

        if (route != null) {
            viewRouteDetailed(route);
            System.out.println("Change route name? Enter \"yes\"");
            if (input.next().toLowerCase().equals("yes")) {
                displayRouteNameChanger(route);
            }
        }
    }

    //EFFECT: searches for the route in plannedRoutes
    private Route findRouteInPlanned(String command) {
        for (Route r : this.planner.getPlannedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                return r;
            }
        }
        return null;
    }

    //EFFECT: searches for the route in completedRoutes
    private Route findRouteInCompleted(String command) {
        for (Route r : this.planner.getCompletedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                return r;
            }
        }
        return null;
    }

    //MODIFIES: route
    //EFFECT: change the name of a route
    public void displayRouteNameChanger(Route route) {
        System.out.println("-------------------------------------");
        System.out.println("Enter the new route name");

        route.setName(input.next());
    }

    //MODIFIES: this, route
    //EFFECT: displays the menu for adding stations to a route
    public void displayRouteMaker() {
        System.out.println("-------------------------------------");
        System.out.println("Enter a route name: ");

        Route route = new Route(input.next());

        System.out.println("-------------------------------------");
        System.out.println("Enter a starting station: ");
        System.out.println("(Case-Sensitive)");

        processCommandRouteMaker(route);
    }

    //MODIFIES: this, route
    //EFFECT: process the user input for the route maker menu
    private void processCommandRouteMaker(Route route) {
        String command;
        boolean keepGoing = true;

        command = input.next();

        while (keepGoing) {
            Station choice;

            choice = getStartingStation(command, route);
            if (choice == null) {
                System.out.println("Please select a valid station");
            } else {
                chooseStation(choice);
            }

            command = input.next();

            if (command.equals("end")) {
                keepGoing = false;
                route.setEnd(route.getPathToDestination().get(route.getPathToDestination().size() - 1));
                route.setStart(route.getPathToDestination().get(0));
                this.planner.assignIdentification(route);
                this.planner.getPlannedRoutes().add(route);
            }
        }
    }

    //MODIFIES: route
    //EFFECT: search and return a station while adding it to the route
    private Station getStartingStation(String command, Route route) {
        for (Line l : tokyo.getLines()) {
            for (Station s : l.getStations()) {
                if (s.getName().equals(command)) {
                    if (route.addStation(s)) {
                        System.out.println("Added " + s.getName() + " to the route.");
                        return s;
                    } else {
                        System.out.println("Unable to add the station specified");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    //EFFECT: presents the next possible stations to add to the path
    private void chooseStation(Station choice) {
        System.out.println("-------------------------------------");
        System.out.println("Enter the next station from options: ");
        System.out.println("(Case-Sensitive)");
        System.out.println("Enter \"end\" to end the route");
        System.out.println("-------------------------------------");
        if (choice != null) {
            for (Station s : choice.getNextStations()) {
                System.out.println(s.getName());
            }
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //EFFECT: user has to option to complete the current route or select a new one
    private void displayCurrentRouteMenu() {
        String command;

        System.out.println("-------------------------------------");
        System.out.println("Enter \"f\" to finish the current route");
        System.out.println("Enter \"s\" to change the current route");

        command = (input.next());

        if (command.equals("f") && (this.planner.getCurrentRoute() != null)) {
            this.planner.completeRoute();
        } else if (command.equals("s")) {
            displayChangeCurrentRouteMenu();
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //MODIFIES: this
    //EFFECT: change the current route to a new one from the list of planned routes
    public void displayChangeCurrentRouteMenu() {
        Route route;

        System.out.println("-------------------------------------");
        for (Route r : this.planner.getPlannedRoutes()) {
            viewRoute(r);
        }
        System.out.println("Enter the id of the new current route");
        route = findRouteInPlanned(input.next());

        if (route != null) {
            this.planner.newCurrentRoute(route);
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //EFFECT: displays the route menu
    public void displayRemoveRouteMenu() {
        String command;
        Route route;

        System.out.println("-------------------------------------");
        System.out.println("Enter the id of route you wish to remove");
        System.out.println("Note: you cannot remove the current route");
        System.out.println("      unless you change it first");

        command = input.next();

        if (findRouteInPlanned(command) != null) {
            route = findRouteInPlanned(command);
            this.planner.getPlannedRoutes().remove(route);
        } else if (findRouteInCompleted(command) != null) {
            route = findRouteInCompleted(command);
            this.planner.getCompletedRoutes().remove(route);
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //EFFECT: print relevant info about the station (station name, what line it belongs to, and any adjacent stations)
    public void viewStationInfo(Station station) {
        System.out.println(station.getName());
        System.out.println("-------------------------------------");
        System.out.println("This station belongs to the following lines:");
        System.out.println("-------------------------------------");
        for (Line l : station.getLine()) {
            System.out.println(l.getName());
        }
        System.out.println("-------------------------------------");
        System.out.println("From this station you can travel to:");
        System.out.println("-------------------------------------");
        for (Station s : station.getNextStations()) {
            System.out.println(s.getName());
        }
    }

    //EFFECT: print relevant info about the line (list of stations including terminal stations, name of line, line
    //        identification symbol and colour, and other lines that intersect with it
    public void viewLineInfo(Line line) {
        int count = 1;
        System.out.println(line.getName() + " Line - Identifier: " + line.getIdentification());
        System.out.println("-------------------------------------");
        System.out.println("Stations:");
        System.out.println("-------------------------------------");
        for (Station s : line.getStations()) {
            System.out.println(count + ". " + s.getName());
            count++;
        }
        System.out.println("-------------------------------------");
        System.out.println("Lines that can be transferred to from this one:");
        System.out.println("-------------------------------------");
        for (Line t : line.getTransfers()) {
            System.out.println(t.getName());
        }
    }

    //EFFECT: print the route id, name, and total number of stations. Info for condensed view in planner
    public void viewRoute(Route route) {
        System.out.println("ID: " + route.getIdentification() + " Route: " + route.getName());
        System.out.println("Total stations in route: " + route.getPathToDestination().size());
    }

    //EFFECT: print the route id, name, start and end, list of stations in the route, and total number of stations
    public void viewRouteDetailed(Route route) {
        int count = 1;
        System.out.println("ID: " + route.getIdentification() + " Route: " + route.getName());
        System.out.println("Start: " + route.getStartPoint().getName() + " End: " + route.getEndPoint().getName());
        System.out.println("Total stations in route: " + route.getPathToDestination().size());
        System.out.println("-------------------------------------");
        for (Station s : route.getPathToDestination()) {
            System.out.println(count + ". " + s.getName());
            count++;
        }
    }

    //EFFECT: print the contents of the planner (current, planned, and completed routes along with statistics)
    public void viewPlanner() {
        System.out.println("Your Current Route");
        System.out.println("-------------------------------------");
        if (planner.getCurrentRoute() != null) {
            viewRoute(planner.getCurrentRoute());
        } else {
            System.out.println("No current route selected");
        }
        System.out.println("-------------------------------------");
        System.out.println("Your Planned Routes");
        System.out.println("-------------------------------------");
        for (Route r : planner.getPlannedRoutes()) {
            viewRoute(r);
        }
        System.out.println("-------------------------------------");
        System.out.println("Your Completed Routes");
        System.out.println("-------------------------------------");
        for (Route r : planner.getCompletedRoutes()) {
            viewRoute(r);
        }
        System.out.println("-------------------------------------");
        System.out.println("Total # of stations visited: " + planner.tallyStations());
    }

    //EFFECT: saves the planner to file
    public void savePlanner() {
        try {
            jsonWriter.open();
            jsonWriter.write(planner);
            jsonWriter.close();
            System.out.println("Saved your planner to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //EFFECT: loads the planner from file
    public void loadPlanner() {
        try {
            planner = jsonReader.read();
            System.out.println("Loaded your planner from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
