package ui;

import model.*;
import model.Line;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MetroFinderGUI implements ActionListener {
    private Tokyo tokyo;
    private Planner planner;
    private Route routeBeingMade;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    private JFrame frame;
    private JComboBox lineDisplay;
    private JLabel lineDisplayInfo;
    private JLabel plannerDisplayInfo;
    private JLabel rmDisplayInfo;
    private JLabel dataDisplayInfo;
    private JButton save;
    private JButton load;
    private JButton complete;
    private JButton setCurrent;
    private JButton remove;
    private JButton rmConfirm;
    private JButton rmEnd;
    private JRadioButton planned;
    private JRadioButton completed;
    private JRadioButton current;
    private JTextField plannerNameBox;
    private JTextField rmNameBox;

    private static final String JSON_STORE = "./data/planner.json";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final File COMPLETE_SOUND = new File("Wood Plank Flicks.wav");
    private static final File REMOVE_SOUND = new File("Metallic Clank.wav");

    //EFFECT: constructs the GUI for the MetroFinder app
    public MetroFinderGUI() {
        tokyo = new Tokyo();
        planner = new Planner();
        routeBeingMade = new Route("");
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

        setUpBGM();
    }

    //EFFECT: initiates background music to start playing (and looping)
    public void setUpBGM() {
        File file = new File("Night Snow - Asher Fulero.wav");
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //MODIFIES: pane
    //EFFECT: builds the main interface; calls other methods to build individual panels
    public void placeComponentsForMainMenu(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel lineCard = new JPanel();
        JPanel plannerCard = new JPanel();
        JPanel routeMakerCard = new JPanel();
        JPanel dataCard = new JPanel();
        JPanel helpCard = new JPanel();

        placeComponentsForLineMenu(lineCard);
        placeComponentsForPlannerMenu(plannerCard);
        placeComponentsForRouteMakerCard(routeMakerCard);
        placeComponentsForDataCard(dataCard);
        placeComponentsForHelpCard(helpCard);

        tabbedPane.addTab("Lines", lineCard);
        tabbedPane.addTab("Planner", plannerCard);
        tabbedPane.addTab("Route Maker", routeMakerCard);
        tabbedPane.addTab("Save/Load", dataCard);
        tabbedPane.addTab("Help", helpCard);

        tabbedPane.setSelectedIndex(4);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }

    //MODIFIES: panel
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

    //MODIFIES: panel
    //EFFECT: places buttons and components for the planner menu
    public void placeComponentsForPlannerMenu(JPanel panel) {
        planned = new JRadioButton("Planned");
        completed = new JRadioButton("Completed");
        current = new JRadioButton("Current Route");
        ButtonGroup plannerButtons = new ButtonGroup();
        plannerDisplayInfo = new JLabel();
        complete = new JButton("Complete");
        plannerNameBox = new JTextField(7);
        setCurrent = new JButton("Set Current");
        remove = new JButton("Remove");

        planned.addActionListener(this);
        completed.addActionListener(this);
        current.addActionListener(this);
        complete.addActionListener(this);
        setCurrent.addActionListener(this);
        remove.addActionListener(this);
        plannerButtons.add(current);
        plannerButtons.add(planned);
        plannerButtons.add(completed);
        current.setSelected(true);

        panel.setLayout(new GridBagLayout());
        layoutPlannerMenuTopHalf(panel);
    }

    //MODIFIES: panel
    //EFFECT: arranges components for it to fit in the panel
    public void layoutPlannerMenuTopHalf(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.PAGE_START;
        panel.add(current, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(planned, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(completed, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(plannerDisplayInfo, gbc);

        layoutPlannerMenuBottomHalf(panel, gbc);
    }

    //EFFECT: sets up components for the text box and confirm / remove buttons
    public void layoutPlannerMenuBottomHalf(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
        panel.add(complete, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.PAGE_END;
        panel.add(plannerNameBox, gbc);

        gbc.gridx = 1;
        panel.add(setCurrent, gbc);

        gbc.gridx = 2;
        panel.add(remove, gbc);
    }

    //MODIFIES: panel
    //EFFECT: places components for the route maker menu
    public void placeComponentsForRouteMakerCard(JPanel panel) {
        rmConfirm = new JButton("Confirm");
        rmEnd = new JButton("End");
        rmNameBox = new JTextField(12);
        rmDisplayInfo = new JLabel("Enter the name of the new route.");
        rmConfirm.addActionListener(this);
        rmEnd.addActionListener(this);
        panel.add(rmNameBox);
        panel.add(rmConfirm);
        panel.add(rmEnd);
        panel.add(rmDisplayInfo);
    }

    //MODIFIES: panel
    //EFFECT: places buttons and components for the save/load data menu
    public void placeComponentsForDataCard(JPanel panel) {
        save = new JButton("Save Planner");
        load = new JButton("Load Planner");
        dataDisplayInfo = new JLabel();
        save.addActionListener(this);
        load.addActionListener(this);
        panel.add(save);
        panel.add(load);
        panel.add(dataDisplayInfo, Component.BOTTOM_ALIGNMENT);
    }

    //EFFECT: places components for the help tab
    public void placeComponentsForHelpCard(JPanel panel) {
        JLabel label = new JLabel();
        String info = "<html>Welcome to MetroFinder!<br/><br/>"
                + "<b>Lines</b> -> View information about lines<br/>"
                + "<b>Planner</b> -> View information about your saved routes<br/>"
                + "<b>Route Maker</b> -> Make a new route to add to the planner<br/>"
                + "<b>Save/Load</b> -> Save or load your planner from a save file<br/><br/>"
                + "The text box at the bottom of the planner page<br/>will only accept Route ID numbers.<br/><br/>"
                + "The planner will not update immediately,<br/>clicking on \"Current\", \"Planned\", or \"Completed\""
                + "<br/>will update it without error.<br/><br/>"
                + "The \"Complete\" button will only register if<br/>you have a current route set.<br/><br/>"
                + "The \"Remove\" button will only remove routes<br/>in your planned or completed list.";
        label.setText(info);
        panel.add(label);
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
        } else if (e.getSource() == complete) {
            planner.completeRoute();
            playSound(COMPLETE_SOUND);
        } else if (e.getSource() == setCurrent) {
            changeRoute(plannerNameBox.getText());
        } else if (e.getSource() == remove) {
            removeRoute(plannerNameBox.getText());
        } else if (e.getSource() == rmConfirm) {
            actionPerformedRouteMakerConfirmButton();
        } else if (e.getSource() == rmEnd) {
            endRoute();
        }
    }

    //EFFECT: response for scroll box interaction in the line menu
    public void actionPerformedLineMenu() {
        String info = viewLineInfo(tokyo.getLines().get(lineDisplay.getSelectedIndex()));
        lineDisplayInfo.setText("<html>" + info + "</html>");
    }

    //EFFECT: sets the text for the label depending on what planner element needs to be shown
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

    //EFFECT: redirects control flow depending on the state of the route maker
    public void actionPerformedRouteMakerConfirmButton() {
        if ((!rmNameBox.getText().equals("")) && routeBeingMade.getName().equals("")) {
            routeMakerSetName();
        } else if ((!rmNameBox.getText().equals("")) && routeBeingMade.getPathToDestination().size() == 0) {
            routeMakerSetStartStation();
        } else if (!rmNameBox.getText().equals("")) {
            routeMakerAddStation();
        }
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

    //EFFECT: displays current route information
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

    //EFFECT: displays planned route information
    public String viewPlannedRoutesInfo() {
        String title = "<b>Your Planned Routes</b><br/><br/>";
        String body = "";
        for (Route r : planner.getPlannedRoutes()) {
            body += viewRoute(r) + "<br/><br/>";
        }

        return title + body;
    }

    //EFFECT: displays completed route information
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

    //EFFECT: searches for the route in plannedRoutes
    public Route findRouteInPlanned(String command) {
        for (Route r : this.planner.getPlannedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                return r;
            }
        }
        return null;
    }

    //EFFECT: searches for the route in completedRoutes
    public Route findRouteInCompleted(String command) {
        for (Route r : this.planner.getCompletedRoutes()) {
            String id = String.valueOf(r.getIdentification());
            if (id.equals(command)) {
                return r;
            }
        }
        return null;
    }

    //EFFECT: changes the current route to the new route based on id; does nothing if passed a route that doesn't
    //        exist or is not in the planned routes
    public void changeRoute(String id) {
        Route route;

        if (findRouteInPlanned(id) != null) {
            route = findRouteInPlanned(id);
            this.planner.newCurrentRoute(route);
        }
    }

    //EFFECT: removes route from planned or completed based on id; does nothing if route is in completed
    //        or not in planner
    public void removeRoute(String id) {
        Route route;

        if (findRouteInPlanned(id) != null) {
            route = findRouteInPlanned(id);
            this.planner.getPlannedRoutes().remove(route);
        } else if (findRouteInCompleted(id) != null) {
            route = findRouteInCompleted(id);
            this.planner.getCompletedRoutes().remove(route);
        }
        playSound(REMOVE_SOUND);
    }

    //MODIFIES: this
    //EFFECT: sets the name of the route being made
    public void routeMakerSetName() {
        routeBeingMade.setName(rmNameBox.getText());
        rmDisplayInfo.setText("Now enter a starting station (case-sensitive).");
    }

    //MODIFIES: this
    //EFFECT: sets the starting station of the route being made
    public void routeMakerSetStartStation() {
        Station start = findStation(rmNameBox.getText());
        if (start == null) {
            rmDisplayInfo.setText("Unable to add the station specified\n"
                    + "Enter a starting station (case-sensitive).");
        } else {
            routeBeingMade.setStart(start);
            rmDisplayInfo.setText("<html><b>Added " + start.getName() + " to the route</b><br/>"
                    + displayNextStations(start) + "<br/><br/>" + displayRouteSoFar() + "</html>");
        }
    }

    //MODIFIES: this
    //EFFECT: adds a station to the route being made
    public void routeMakerAddStation() {
        Station station = findStation(rmNameBox.getText());
        String info = rmDisplayInfo.getText();
        if (station == null) {
            rmDisplayInfo.setText("<html>Unable to add the station specified<br/>"
                    + "Enter a valid station (case-sensitive).<br/><br/>"
                    + info);
        } else {
            rmDisplayInfo.setText("<html><b>Added " + station.getName() + " to the route</b><br/>"
                    + displayNextStations(station) + "<br/><br/>" + displayRouteSoFar() + "</html>");
        }
    }

    //MODIFIES: route
    //EFFECT: search and return a station
    public Station findStation(String command) {
        for (Line l : tokyo.getLines()) {
            for (Station s : l.getStations()) {
                if (s.getName().equals(command)) {
                    if (routeBeingMade.addStation(s)) {
                        return s;
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    //EFFECT: presents the next possible stations to add to the path
    public String displayNextStations(Station choice) {
        String head = "Enter the next station from options<br/>and click \"Confirm\" (case-sensitive)<br/>";
        String head2 = "Click \"End\" to end the route<br/><br/>";
        String body = "";
        for (Station s : choice.getNextStations()) {
            body += s.getName() + "<br/>";
        }
        return head + head2 + body;
    }

    //EFFECT: returns a string of the route being made so far
    public String displayRouteSoFar() {
        String head = "<b>Route so far:</b><br/>";
        String body = "";
        int count = 1;
        for (Station s : routeBeingMade.getPathToDestination()) {
            body += count + ". " + s.getName() + "<br/>";
            count++;
        }
        return head + body;
    }

    //MODIFIES: this
    //EFFECT: adds the current route being made to the planner and resets the field for the next route
    //        if the "End" button is clicked prematurely, does nothing
    public void endRoute() {
        if (!routeBeingMade.getPathToDestination().isEmpty()) {
            routeBeingMade.setEnd(
                    routeBeingMade.getPathToDestination().get(routeBeingMade.getPathToDestination().size() - 1));
            planner.assignIdentification(routeBeingMade);
            planner.getPlannedRoutes().add(routeBeingMade);
            routeBeingMade = new Route("");
            rmDisplayInfo.setText("Enter the name of the new route.");
        }
    }

    //EFFECT: plays sound effect based on file passed
    public void playSound(File type) {
        AudioInputStream audioInputStream;
        Clip clip;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(type);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
