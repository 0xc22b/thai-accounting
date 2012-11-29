package gwt.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {
    
    @Test
    public void testCompareDate() {
        assertEquals(-1, Utils.compareDate(14, 1, 2011, 1, 2, 2011));
        assertEquals(-1, Utils.compareDate(14, 1, 2010, 1, 2, 2011));
        assertEquals(-1, Utils.compareDate(14, 1, 2011, 1, 2, 2011));
        assertEquals(-1, Utils.compareDate(14, 2, 2011, 15, 2, 2011));
        
        assertEquals(1, Utils.compareDate(18, 3, 2011, 19, 2, 2011));
        assertEquals(1, Utils.compareDate(18, 3, 2012, 19, 2, 2011));
        assertEquals(1, Utils.compareDate(20, 2, 2011, 19, 2, 2011));
        
        assertEquals(0, Utils.compareDate(1, 1, 2011, 1, 1, 2011));
        assertEquals(0, Utils.compareDate(28, 9, 2011, 28, 9, 2011));
    }
    
    @Test
    public void testGetListDay() {
        assertEquals(31, Utils.getLastDay(1, 2012));
        
        assertEquals(29, Utils.getLastDay(2, 2012));
        assertEquals(28, Utils.getLastDay(2, 2013));
        
        assertEquals(31, Utils.getLastDay(3, 2013));
        assertEquals(30, Utils.getLastDay(4, 2013));
        assertEquals(31, Utils.getLastDay(5, 2013));
        assertEquals(30, Utils.getLastDay(6, 2013));
        assertEquals(31, Utils.getLastDay(7, 2013));
        assertEquals(31, Utils.getLastDay(8, 2013));
        assertEquals(30, Utils.getLastDay(9, 2013));
        assertEquals(31, Utils.getLastDay(10, 2013));
        assertEquals(30, Utils.getLastDay(11, 2013));
        assertEquals(31, Utils.getLastDay(12, 2013));
    }
}
