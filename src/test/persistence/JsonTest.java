package persistence;

import model.Line;
import model.Route;
import model.Station;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    //EFFECT: helper to make assertions for comparing routes
    protected void checkRoute(Route expectedRoute, Route actualRoute) {
        assertEquals(expectedRoute.getName(), actualRoute.getName());
        assertEquals(expectedRoute.getIdentification(), actualRoute.getIdentification());
        assertEquals(expectedRoute.getStartPoint().getName(), actualRoute.getStartPoint().getName());
        assertEquals(expectedRoute.getEndPoint().getName(), actualRoute.getEndPoint().getName());

        for (int i = 0; i < actualRoute.getPathToDestination().size(); i++) {
            Station se = expectedRoute.getPathToDestination().get(i);
            Station sa = actualRoute.getPathToDestination().get(i);

            assertEquals(se.getName(), sa.getName());
        }
    }

    //EFFECT: helper for the test methods
    protected Route buildTestRoute() {
        Route r1 = new Route("Route 1");
        Line l1 = new Line("Line 1", "id 1");
        Station s1 = new Station("Station 1", l1);
        Station s2 = new Station("Station 2", l1);
        Station s3 = new Station("Station 3", l1);

        r1.addStationFromSave(s1);
        r1.addStationFromSave(s2);
        r1.addStationFromSave(s3);
        r1.setStart(s1);
        r1.setEnd(s3);

        r1.setIdentification(1);

        return r1;
    }

    //EFFECT: helper for the test methods
    protected Route buildTestRoute2() {
        Route r2 = new Route("Route 2");
        Line l2 = new Line("Line 2", "id 2");
        Station s1 = new Station("Station 4", l2);
        Station s2 = new Station("Station 5", l2);
        Station s3 = new Station("Station 6", l2);

        r2.addStationFromSave(s1);
        r2.addStationFromSave(s2);
        r2.addStationFromSave(s3);
        r2.setStart(s1);
        r2.setEnd(s3);

        r2.setIdentification(2);

        return r2;
    }

    //EFFECT: helper for the test methods
    protected Route buildTestRoute3() {
        Route r3 = new Route("Route 3");
        Line l3 = new Line("Line 3", "id 3");
        Station s1 = new Station("Station 7", l3);
        Station s2 = new Station("Station 8", l3);
        Station s3 = new Station("Station 9", l3);

        r3.addStationFromSave(s1);
        r3.addStationFromSave(s2);
        r3.addStationFromSave(s3);
        r3.setStart(s1);
        r3.setEnd(s3);

        r3.setIdentification(3);

        return r3;
    }
}
