package model;

import model.exceptions.AdjacentStationException;
import model.exceptions.EmptyRouteException;
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
        try {
            assertTrue(testRoute.addStation(station1));
        } catch (AdjacentStationException e) {
            fail("Unexpected AdjacentStationException");
        }
        assertEquals(station1, testRoute.getPathToDestination().get(0));
    }

    @Test
    public void testAddStationMultiple() {
        try {
            assertTrue(testRoute.addStation(station1));
            assertTrue(testRoute.addStation(station2));
        } catch (AdjacentStationException e) {
            fail("Unexpected AdjacentStationException");
        }
        assertEquals(station1, testRoute.getPathToDestination().get(0));
        assertEquals(station2, testRoute.getPathToDestination().get(1));
    }

    @Test
    public void testAddStationNotAdjacent() {
        Station station3 = new Station("S3", testLine);
        station3.getNextStations().add(station2);
        try {
            assertTrue(testRoute.addStation(station1));
            testRoute.addStation(station3);
            fail("AdjacentStationException expected");
        } catch (AdjacentStationException e) {
            //pass
        }
    }

    @Test
    public void testAddStationSameStation() {
        try {
            assertTrue(testRoute.addStation(station1));
            testRoute.addStation(station1);
            fail("AdjacentStationException expected");
        } catch (AdjacentStationException e) {
            //pass
        }
    }

    @Test
    public void testRemoveStation() {
        testRoute.getPathToDestination().add(station1);
        try {
            testRoute.removeStation();
        } catch (EmptyRouteException e) {
            fail("Unexpected EmptyRouteException");
        }
        assertEquals(0, testRoute.getPathToDestination().size());
    }

    @Test
    public void testRemoveStationMultiple() {
        testRoute.getPathToDestination().add(station1);
        testRoute.getPathToDestination().add(station2);
        try {
            testRoute.removeStation();
            testRoute.removeStation();
        } catch (EmptyRouteException e) {
            fail("Unexpected EmptyRouteException");
        }
        assertEquals(0, testRoute.getPathToDestination().size());
    }

    @Test
    public void testRemoveStationEmptyException() {
        try {
            testRoute.removeStation();
            fail("EmptyRouteException expected");
        } catch (EmptyRouteException e) {
            //pass
        }
    }
}
