package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {
    private Station station1;
    private Station station2;
    private Line testLine;
    private Route testRoute;

    @BeforeEach
    public void setup() {
        testLine = new Line("Line A", "Red");
        station1 = new Station("S1", testLine);
        station2 = new Station("S2", testLine);
        station1.getNextStations().add(station2);
        station2.getNextStations().add(station1);
        testRoute = new Route("Route X");
    }

    @Test
    public void testConstructor() {
        assertEquals("Route X", testRoute.getName());
        assertNull(testRoute.getStartPoint());
        assertNull(testRoute.getEndPoint());
        assertEquals(0, testRoute.getPathToDestination().size());
    }

    @Test
    public void testSetters() {
        testRoute.setName("Line Y");
        testRoute.setStart(station1);
        testRoute.setEnd(station2);
        assertEquals("Line Y", testRoute.getName());
        assertEquals(station1, testRoute.getStartPoint());
        assertEquals(station2, testRoute.getEndPoint());
    }

    @Test
    public void testAddStation() {
        assertTrue(testRoute.addStation(station1));
        assertEquals(station1, testRoute.getPathToDestination().get(0));
    }

    @Test
    public void testAddStationMultiple() {
        assertTrue(testRoute.addStation(station1));
        assertTrue(testRoute.addStation(station2));
        assertEquals(station1, testRoute.getPathToDestination().get(0));
        assertEquals(station2, testRoute.getPathToDestination().get(1));
    }

    @Test
    public void testAddStationNotAdjacent() {
        Station station3 = new Station("S3", testLine);
        station3.getNextStations().add(station2);
        assertTrue(testRoute.addStation(station1));
        assertFalse(testRoute.addStation(station3));
    }

    @Test
    public void testAddStationSameStation() {
        assertTrue(testRoute.addStation(station1));
        assertFalse(testRoute.addStation(station1));
    }

    @Test
    public void testRemoveStation() {
        testRoute.getPathToDestination().add(station1);
        testRoute.removeStation();
        assertEquals(0, testRoute.getPathToDestination().size());
    }

    @Test
    public void testRemoveStationMultiple() {
        testRoute.getPathToDestination().add(station1);
        testRoute.getPathToDestination().add(station2);
        testRoute.removeStation();
        testRoute.removeStation();
        assertEquals(0, testRoute.getPathToDestination().size());
    }
}
