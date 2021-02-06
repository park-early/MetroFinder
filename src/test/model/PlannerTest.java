package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlannerTest {
    private Planner testPlanner;
    private Route testRoute1;
    private Route testRoute2;
    private Route testRoute3;
    private Line testLine;
    private Station testStation;

    @BeforeEach
    public void setup() {
        testPlanner = new Planner();
        testRoute1 = new Route("Route A");
        testRoute1.setIdentification(1);
        testRoute2 = new Route("Route B");
        testRoute2.setIdentification(2);
        testRoute3 = new Route("Route C");
        testRoute3.setIdentification(3);
        testLine = new Line("Line A", "Red");
        testStation = new Station("S", testLine);
        testRoute1.addStation(testStation);
        testPlanner.getPlannedRoutes().add(testRoute1);
        testPlanner.getPlannedRoutes().add(testRoute3);
        testPlanner.getCompletedRoutes().add(testRoute2);
    }

    @Test
    public void testAssignIdentification() {
        testPlanner.assignIdentification(testRoute1);
        assertEquals(1, testRoute1.getIdentification());
        assertEquals(2, testPlanner.getRouteIdTracker());
    }

    @Test
    public void testNewCurrentRouteFromPlanned() {
        testPlanner.newCurrentRoute(testRoute3);
        assertEquals(testRoute3, testPlanner.getCurrentRoute());
        assertEquals(1, testPlanner.getPlannedRoutes().size());
        assertEquals(1, testPlanner.getCompletedRoutes().size());
    }

    @Test
    public void testNewCurrentRouteChangeAndReplaceFromPlanned() {
        testPlanner.newCurrentRoute(testRoute3);
        testPlanner.newCurrentRoute(testRoute1);
        assertEquals(testRoute1, testPlanner.getCurrentRoute());
        assertEquals(1, testPlanner.getPlannedRoutes().size());
        assertEquals(testRoute3, testPlanner.getPlannedRoutes().get(0));
        assertEquals(1, testPlanner.getCompletedRoutes().size());
    }

    @Test
    public void testCompleteRoute() {
        testPlanner.newCurrentRoute(testRoute3);
        testPlanner.completeRoute();
        assertNull(testPlanner.getCurrentRoute());
        assertEquals(1, testPlanner.getPlannedRoutes().size());
        assertEquals(2, testPlanner.getCompletedRoutes().size());
        assertEquals(testRoute2, testPlanner.getCompletedRoutes().get(0));
        assertEquals(testRoute3, testPlanner.getCompletedRoutes().get(1));
    }

    @Test
    public void testTallyStationsNoneCompleted() {
        testPlanner.getCompletedRoutes().remove(0);
        assertEquals(0, testPlanner.tallyStations());
    }

    @Test
    public void testTallyStations() {
        testRoute2.getPathToDestination().add(testStation);
        testRoute2.getPathToDestination().add(testStation);
        testRoute3.getPathToDestination().add(testStation);
        testRoute3.getPathToDestination().add(testStation);
        testRoute3.getPathToDestination().add(testStation);
        testPlanner.newCurrentRoute(testRoute3);
        testPlanner.completeRoute();
        assertEquals(5, testPlanner.tallyStations());
    }
}
