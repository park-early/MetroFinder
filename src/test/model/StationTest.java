package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationTest {
    Line testLine;
    Line testLine2;
    Station station1;
    Station station2;
    Station station3;

    @BeforeEach
    public void setup() {
        testLine = new Line("Line A", "Red");
        testLine2 = new Line("Line B", "Blue");
        station1 = new Station("S1", testLine);
        station2 = new Station("S2", testLine);
        station3 = new Station("S3", testLine2);
        station1.getNextStations().add(station2);
        station1.getNextStations().add(station3);
    }

    @Test
    public void testConstructor() {
        assertEquals("S1", station1.getName());
        assertEquals("Line A", station1.getLine().get(0).getName());
        assertEquals(2, station1.getNextStations().size());
    }
}