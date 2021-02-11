package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokyoTest {
    private Tokyo testTokyo;

    @BeforeEach
    public void setup() {
        testTokyo = new Tokyo();
        testTokyo.initializeTokyo();
    }

    @Test
    public void testInitializeTokyo() {
        assertEquals("Tokyo", testTokyo.getName());
        assertEquals(3, testTokyo.getLines().size());
        assertEquals("Asakusa", testTokyo.getLines().get(0).getName());
        assertEquals(8, testTokyo.getLines().get(0).getStations().size());
        assertEquals("Mita", testTokyo.getLines().get(1).getName());
        assertEquals(10, testTokyo.getLines().get(1).getStations().size());
        assertEquals("Shinjuku", testTokyo.getLines().get(2).getName());
        assertEquals(6, testTokyo.getLines().get(2).getStations().size());
    }
}
