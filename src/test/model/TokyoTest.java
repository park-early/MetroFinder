package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokyoTest {
    Tokyo testTokyo = new Tokyo();

    @Test
    public void testInitializeTokyo() {
        assertEquals(3, testTokyo.getLines().size());
        assertEquals("Asakusa", testTokyo.getLines().get(0).getName());
        assertEquals(20, testTokyo.getLines().get(0).getStations().size());
        assertEquals("Mita", testTokyo.getLines().get(1).getName());
        assertEquals(27, testTokyo.getLines().get(1).getStations().size());
        assertEquals("Shinjuku", testTokyo.getLines().get(2).getName());
        assertEquals(21, testTokyo.getLines().get(2).getStations().size());
    }
}
