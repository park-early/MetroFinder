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

public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyPlanner() {
        try {
            Planner p = new Planner();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyPlanner.json");
            writer.open();
            writer.write(p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyPlanner.json");
            p = reader.read();
            assertNull(p.getCurrentRoute());
            assertEquals(0, p.getPlannedRoutes().size());
            assertEquals(0, p.getCompletedRoutes().size());
            assertEquals(1, p.getRouteIdTracker());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralPlanner() {
        try {
            Planner p = buildTestPlanner();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPlanner.json");
            writer.open();
            writer.write(p);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralPlanner.json");
            p = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }

    //EFFECT: helper for the test methods
    private Planner buildTestPlanner() {
        Planner p = new Planner();
        Route r1 = buildTestRoute();
        Route r2 = buildTestRoute2();
        Route r3 = buildTestRoute3();
        p.getPlannedRoutes().add(r1);
        p.getCompletedRoutes().add(r2);
        p.newCurrentRoute(r3);
        p.setIdTracker(4);

        return p;
    }
}
