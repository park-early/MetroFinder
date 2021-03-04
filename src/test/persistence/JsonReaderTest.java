package persistence;

import model.Planner;
import model.Route;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests largely modelled after tests from JsonSerializationDemo
 *
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Planner p = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyPlanner() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPlanner.json");
        try {
            Planner p = reader.read();
            assertNull(p.getCurrentRoute());
            assertEquals(0, p.getPlannedRoutes().size());
            assertEquals(0, p.getCompletedRoutes().size());
            assertEquals(1, p.getRouteIdTracker());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralPlanner() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPlanner.json");
        try {
            Planner p = reader.read();
            assertEquals(4, p.getRouteIdTracker());
            List<Route> planned = p.getPlannedRoutes();
            List<Route> completed = p.getCompletedRoutes();
            Route current = p.getCurrentRoute();
            assertEquals(1, planned.size());
            assertEquals(1, completed.size());
            checkRoute(buildTestRoute(), planned.get(0));
            checkRoute(buildTestRoute2(), completed.get(0));
            checkRoute(buildTestRoute3(), current);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
