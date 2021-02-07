package ui;

import model.*;

import java.util.List;
import java.util.Scanner;

public class MetroFinderApp {
    private Planner planner;
    private final Tokyo tokyo;
    private Scanner input;

    //EFFECT: runs the MetroFinder app
    //code body from TellerApp
    public MetroFinderApp() {
        boolean keepGoing = true;
        String command = null;

        planner = new Planner();
        tokyo = new Tokyo();
        tokyo.initializeTokyo();
        input = new Scanner(System.in);

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
        System.out.println("q -> Quit app");
        // "M -> Change metro system"  -------> eventually
    }

    //EFFECT: redirect console display to a new menu based on user input
    public void processCommandMainMenu(String command) {
        if (command.equals("l")) {
            displayLines();
        } else if (command.equals("p")) {
            displayPlanner();
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }

    //EFFECT: display menu of all lines in the metro system
    public void displayLines() {
        boolean keepGoing = true;
        String command = null;

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
                l.viewLineInfo();
                keepGoing = true;
                line = l;
                badInput = false;
            }
        }

        processCommandLineMenu(keepGoing, badInput, line);
    }

    //EFFECT: process user input at the menu displaying detailed line info
    private void processCommandLineMenu(boolean keepGoing, boolean badInput, Line line) {
        String command = null;
        while (badInput) {
            System.out.println("Sorry, that option doesn't exist. Try something else");
            badInput = false;
        }

        while (keepGoing) {
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
                line.getStations().get(i).viewStationInfo();
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
        String command = null;

        while (keepGoing) {
            this.planner.viewPlanner();
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
                //displayChangeCurrentRouteMenu();
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
            route.viewRouteDetailed();
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
    public void displayRouteMaker() {
        String command = null;
        boolean keepGoing = true;

        System.out.println("-------------------------------------");
        System.out.println("Enter a route name: ");

        Route route = new Route(input.next());

        System.out.println("-------------------------------------");
        System.out.println("Enter a starting station: ");
        System.out.println("(Case-Sensitive)");

        command = input.next();

        while (keepGoing) {
            Station choice;

            choice = getStation(command, route);
            chooseStation(choice);
            command = input.next();

            if (command.equals("end")) {
                keepGoing = false;
                route.setEnd(choice);
                route.setStart(route.getPathToDestination().get(0));
                this.planner.assignIdentification(route);
                this.planner.getPlannedRoutes().add(route);
            }
        }
    }

    //MODIFIES: route
    //EFFECT: search and return a station while adding it to the route
    private Station getStation(String command, Route route) {
        for (Line l : tokyo.getLines()) {
            for (Station s : l.getStations()) {
                if (s.getName().equals(command)) {
                    route.getPathToDestination().add(s);
                    return s;
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
        if (choice != null) {
            for (Station s : choice.getNextStations()) {
                System.out.println(s.getName());
            }
        } else {
            System.out.println("Sorry, that option doesn't exist");
        }
    }
}
