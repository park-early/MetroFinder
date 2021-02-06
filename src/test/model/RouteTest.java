package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    Line testLine;
    Line testLine2;
    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Station station5;
    Route testRoute;

    @BeforeEach
    public void setup() {
        testLine = new Line("Line A", "Red");
        testLine2 = new Line("Line B", "Blue");
        station1 = new Station("S1", testLine);
        station2 = new Station("S2", testLine);
        station2.getLine().add(testLine2);
        station3 = new Station("S3", testLine2);
        station4 = new Station("S4", testLine);
        station5 = new Station("S5", testLine);
        station1.getNextStations().add(station2);
        station1.getNextStations().add(station5);
        station2.getNextStations().add(station1);
        station2.getNextStations().add(station3);
        station2.getNextStations().add(station4);
        station3.getNextStations().add(station2);
        station3.getNextStations().add(station5);
        station4.getNextStations().add(station2);
        station5.getNextStations().add(station1);
        station5.getNextStations().add(station3);
        testRoute = new Route("Route X");
    }

    @Test
    public void testConstructor() {
        assertEquals("Route X", testRoute.getName());
    }

    @Test
    public void testPlanRouteSameStation() {
        testRoute.planRoute(station1, station1);
        assertEquals(station1, testRoute.getStartPoint());
        assertEquals(station1, testRoute.getEndPoint());
        assertEquals(1, testRoute.getPathToDestination().size());
        assertEquals(station1, testRoute.getPathToDestination().get(0));
    }

    @Test
    public void testPlanRouteTakeTheShorterPath() {
        testRoute.planRoute(station1, station4);
        assertEquals(station1, testRoute.getStartPoint());
        assertEquals(station4, testRoute.getEndPoint());
        assertEquals(3, testRoute.getPathToDestination().size());
        assertEquals(station1, testRoute.getPathToDestination().get(0));
        assertEquals(station2, testRoute.getPathToDestination().get(1));
        assertEquals(station4, testRoute.getPathToDestination().get(2));
    }

    @Test
    public void testPlanRouteTakeTheFirstPathIfEqualLength() {
        testRoute.planRoute(station1, station3);
        assertEquals(3, testRoute.getPathToDestination().size());
        assertEquals(station1, testRoute.getPathToDestination().get(0));
        assertEquals(station2, testRoute.getPathToDestination().get(1));
        assertEquals(station3, testRoute.getPathToDestination().get(2));
    }
}
