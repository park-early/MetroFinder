package persistence;

import org.json.JSONObject;

/**
 * Interface for objects that need to be saved/loaded
 */

public interface Writable {

    //EFFECTS: returns this as JSON object
    //from JsonSerializationDemo
    JSONObject toJson();
}
