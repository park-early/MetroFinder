package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LineTest {
    private Line testLine;
    private Line testLine2;
    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    public void setup() {
        testLine = new Line("Line A", "Red");
        station1 = new Station("S1", testLine);
        station2 = new Station("S2", testLine);
        station3 = new Station("S3", testLine);
        testLine.getStations().add(station1);
        testLine.getStations().add(station2);
        testLine.getStations().add(station3);
        testLine.getTransfers().add(testLine2);
    }

    @Test
    public void testConstructor() {
        assertEquals("Line A", testLine.getName());
        assertEquals("Red", testLine.getIdentification());
        assertEquals(1, testLine.getTransfers().size());
    }
}
