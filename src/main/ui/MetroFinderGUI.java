package ui;

import model.Line;
import model.Planner;
import model.Station;
import model.Tokyo;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        placeComponentsForLineMenu(lineCard);

        tabbedPane.addTab("Lines", lineCard);
        tabbedPane.addTab("Planner", plannerCard);
        tabbedPane.addTab("Save/Load", dataCard);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lineDisplay) {
            String info = viewLineInfo(tokyo.getLines().get(lineDisplay.getSelectedIndex()));
            lineDisplayInfo.setText("<html>" + info + "</html>");
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
}
