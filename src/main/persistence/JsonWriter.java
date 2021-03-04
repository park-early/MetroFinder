package persistence;

import model.Planner;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Represents a writer that writes JSON representation of Planner to file
 *
 * Methods from JsonSerializationDemo
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    //EFFECT: constructs a writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        this.writer = new PrintWriter(new File(this.destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Planner p) {
        JSONObject json = p.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        this.writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        this.writer.print(json);
    }
}
