package ui;

import model.*;

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
        System.out.println("L -> View lines in " + tokyo.getName());
        System.out.println("P -> View your planner");
        System.out.println("Q -> Quit app");
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

        for (Line l : tokyo.getLines()) {
            System.out.println(l.getName() + " Line - " + l.getIdentification());
        }
        System.out.println("-------------------------------------");
        System.out.println("Enter a line name to learn more\nor enter \"b\" to go back");

        while (keepGoing) {
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

        this.planner.viewPlanner();
        System.out.println("-------------------------------------");
        System.out.println("Enter a route id to learn more\nEnter \"b\" to go back\nEnter \"m\" to plan a new route");

        while (keepGoing) {
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else {
                processCommandPlannerMenu(command);
            }
        }
    }

    //EFFECT: display detailed information about a route
    public void processCommandPlannerMenu(String command) {
        boolean badInput = true;

        for (Route r : this.planner.getCompletedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                r.viewRouteDetailed();
                badInput = false;
            }
        }
        for (Route r : this.planner.getPlannedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                r.viewRouteDetailed();
                badInput = false;
            }
        }
        String id = String.valueOf(this.planner.getCurrentRoute().getIdentification());
        if (id.equals(command)) {
            this.planner.getCurrentRoute().viewRouteDetailed();
        }

        if (badInput) {
            System.out.println("Sorry, that option doesn't exist");
            displayLines();
        }
    }
}
