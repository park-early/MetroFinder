package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MetroFinderGUI implements ActionListener {
    private Tokyo tokyo;
    private Planner planner;
    private final Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    private JFrame frame;
    private JComboBox lineDisplay;
    private JLabel lineDisplayInfo;
    private JLabel plannerDisplayInfo;
    private JLabel dataDisplayInfo;
    private JButton save;
    private JButton load;
    private JRadioButton planned;
    private JRadioButton completed;
    private JRadioButton current;

    private static final String JSON_STORE = "./data/planner.json";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    //EFFECT: constructs the GUI for the MetroFinder app
    public MetroFinderGUI() {
        tokyo = new Tokyo();
        planner = new Planner();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        frame = new JFrame();

        tokyo.initializeTokyo();

        frame.setTitle("MetroFinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);

        placeComponentsForMainMenu(frame);
        frame.setVisible(true);
    }

    //EFFECT: builds the main interface; calls other methods to build individual panels
    public void placeComponentsForMainMenu(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel lineCard = new JPanel();
        JPanel plannerCard = new JPanel();
        JPanel dataCard = new JPanel();
        JPanel helpCard = new JPanel();

        placeComponentsForLineMenu(lineCard);
        placeComponentsForPlannerMenu(plannerCard);
        placeComponentsForDataCard(dataCard);
        //placeComponentsForHelpCard(helpCard);

        tabbedPane.addTab("Lines", lineCard);
        tabbedPane.addTab("Planner", plannerCard);
        tabbedPane.addTab("Save/Load", dataCard);
        tabbedPane.addTab("Help", helpCard);

        tabbedPane.setSelectedIndex(3);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    //EFFECT: places scroll box to select line and JLabel to display info
    public void placeComponentsForLineMenu(JPanel panel) {
        String[] lines = {tokyo.getLines().get(0).getName(),
                tokyo.getLines().get(1).getName(),
                tokyo.getLines().get(2).getName()};
        lineDisplay = new JComboBox(lines);
        lineDisplayInfo = new JLabel();
        lineDisplay.addActionListener(this);
        panel.add(lineDisplay);
        panel.add(lineDisplayInfo);
    }

    //EFFECT: places buttons and components for the planner menu
    public void placeComponentsForPlannerMenu(JPanel panel) {
        planned = new JRadioButton("Planned");
        completed = new JRadioButton("Completed");
        current = new JRadioButton("Current Route");
        ButtonGroup plannerButtons = new ButtonGroup();
        plannerDisplayInfo = new JLabel();

        planned.addActionListener(this);
        completed.addActionListener(this);
        current.addActionListener(this);
        plannerButtons.add(current);
        plannerButtons.add(planned);
        plannerButtons.add(completed);
        current.setSelected(true);

        panel.add(current);
        panel.add(planned);
        panel.add(completed);
        panel.add(plannerDisplayInfo);
    }

    //EFFECT: places buttons and components for the save/load data menu
    public void placeComponentsForDataCard(JPanel panel) {
        save = new JButton("Save Planner");
        load = new JButton("Load Planner");
        dataDisplayInfo = new JLabel();
        save.addActionListener(this);
        load.addActionListener(this);
        panel.add(save);
        panel.add(load);
        panel.add(dataDisplayInfo);
    }

    //EFFECT: redirects control flow to a method to deal with what button/action was selected
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lineDisplay) {
            actionPerformedLineMenu();
        } else if (e.getSource() == save) {
            savePlanner();
        } else if (e.getSource() == load) {
            loadPlanner();
        } else if (e.getSource() == current || e.getSource() == planned || e.getSource() == completed) {
            actionPerformedPlannerMenu();
        }
    }

    //EFFECT: response for scroll box interaction in the line menu
    public void actionPerformedLineMenu() {
        String info = viewLineInfo(tokyo.getLines().get(lineDisplay.getSelectedIndex()));
        lineDisplayInfo.setText("<html>" + info + "</html>");
    }

    //EFFECT: converts all the line info into an HTML string format to display as a JLabel
    public String viewLineInfo(Line line) {
        int count = 1;
        String space = "<br/>";
        String title = "<b>" + line.getName() + " Line - Identifier: " + line.getIdentification() + "</b>";
        String stationTitle = "<b>Stations:</b>";
        String stations = "";
        for (Station s : line.getStations()) {
            stations += count + ". " + s.getName() + space;
            count++;
        }
        String transferTitle = "<b>Lines that can be transferred to from this one:</b>";
        String transfers = "";
        for (Line t : line.getTransfers()) {
            transfers += t.getName() + space;
        }
        String info = title + space + stationTitle + space + stations + space + transferTitle + space + transfers;
        return info;
    }

    public void actionPerformedPlannerMenu() {
        String info;
        if (current.isSelected()) {
            info = viewCurrentRouteInfo();
        } else if (planned.isSelected()) {
            info = viewPlannedRoutesInfo();
        } else {
            info = viewCompletedRoutesInfo();
        }
        plannerDisplayInfo.setText("<html>" + info + "</html>");
    }

    public String viewCurrentRouteInfo() {
        String title = "<b>Your Current Route</b><br/><br/>";
        String body;
        if (planner.getCurrentRoute() != null) {
            body = viewRouteDetailed(planner.getCurrentRoute());
        } else {
            body = "<b>No current route selected</b>";
        }

        return title + body;
    }

    public String viewPlannedRoutesInfo() {
        String title = "<b>Your Planned Routes</b><br/><br/>";
        String body = "";
        for (Route r : planner.getPlannedRoutes()) {
            body += viewRoute(r) + "<br/><br/>";
        }

        return title + body;
    }

    public String viewCompletedRoutesInfo() {
        String title = "<b>Your Completed Routes</b><br/><br/>";
        String body = "";
        for (Route r : planner.getCompletedRoutes()) {
            body += viewRoute(r) + "<br/><br/>";
        }
        String tally = "<b>Total # of stations visited: " + planner.tallyStations() + "</b>";

        return title + body + tally;
    }

    //EFFECT: print the route id, name, and total number of stations. Info for condensed view in planner
    public String viewRoute(Route route) {
        String head = "ID: " + route.getIdentification() + " | Route: " + route.getName() + "<br/>";
        String body = "Total stations in route: " + route.getPathToDestination().size() + "<br/>";
        return head + body;
    }

    //EFFECT: print the route id, name, start and end, list of stations in the route, and total number of stations
    public String viewRouteDetailed(Route route) {
        int count = 1;
        String head = "ID: " + route.getIdentification() + " | Route: " + route.getName() + "<br/>";
        String terminals = "Start: " + route.getStartPoint().getName()
                + " | End: " + route.getEndPoint().getName() + "<br/><br/>";
        String number = "<b>Total stations in route: " + route.getPathToDestination().size() + "</b><br/><br/>";
        String body = "";
        for (Station s : route.getPathToDestination()) {
            body += count + ". " + s.getName() + "<br/>";
            count++;
        }
        return head + terminals + number + body;
    }

    //EFFECT: saves the planner to file
    public void savePlanner() {
        try {
            jsonWriter.open();
            jsonWriter.write(planner);
            jsonWriter.close();
            dataDisplayInfo.setText("Saved your planner to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            dataDisplayInfo.setText("Unable to write to file: " + JSON_STORE);
        }
    }

    //EFFECT: loads the planner from file
    public void loadPlanner() {
        try {
            planner = jsonReader.read();
            dataDisplayInfo.setText("Loaded your planner from " + JSON_STORE);
        } catch (IOException e) {
            dataDisplayInfo.setText("Unable to read from file: " + JSON_STORE);
        }
    }
}
