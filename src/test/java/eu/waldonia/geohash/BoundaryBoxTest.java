package eu.waldonia.geohash;

import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * @author sih
 */
public class BoundaryBoxTest {

    @Test
    public void constructorShouldThrowAnIAEWhenMaxLessThanMin() {
        try {
            BoundaryBox bb = new BoundaryBox(30D,20D);
            fail("Should have thrown an IAE");
        }
        catch (IllegalArgumentException iae) {
            // all good
        }
    }

    @Test
    public void classifyShouldReturnFalseWhenLeftOfMidpoint() {
        BoundaryBox bb = new BoundaryBox(30D,50D);
        assertFalse(bb.classify(35D));
    }

    @Test
    public void classifyShouldReturnTrueWhenRightOfMidpoint() {
        BoundaryBox bb = new BoundaryBox(30D,50D);
        assertTrue(bb.classify(45D));
    }

    @Test
    public void classifyShouldReturnFalseWhenEqualToMidpoint() {
        BoundaryBox bb = new BoundaryBox(30D,50D);
        assertFalse(bb.classify(40D));
    }

    @Test
    public void midpointShouldReturnValidMidpointForNormalRange( ){
        BoundaryBox bb = new BoundaryBox(30D,50D);
        assertEquals(40D, bb.midpoint());
    }

    @Test
    public void midpointShouldReturnValidMidpointForTrivialRange( ){
        BoundaryBox bb = new BoundaryBox(40D,40D);
        assertEquals(40D, bb.midpoint());
    }

    @Test
    public void nextShouldReturnCorrectHalfPositive() {
        BoundaryBox initial = new BoundaryBox(-90D, 90D);
        BoundaryBox next = initial.next(60D);
        assertEquals(0D, next.min());
        assertEquals(90D, next.max());
    }


    @Test
    public void nextShouldReturnCorrectHalfMidpoint() {
        BoundaryBox initial = new BoundaryBox(-90D, 90D);
        BoundaryBox next = initial.next(0D);
        assertEquals(-90D, next.min());
        assertEquals(0D, next.max());
    }

    @Test
    public void nextShouldReturnCorrectHalfNegative() {
        BoundaryBox initial = new BoundaryBox(-90D, 90D);
        BoundaryBox next = initial.next(-60D);
        assertEquals(-90D, next.min());
        assertEquals(0D, next.max());
    }


    @Test
    public void nextShouldReturnCorrectHalfUltimateMin() {
        BoundaryBox initial = new BoundaryBox(-90D, 90D);
        BoundaryBox next = initial.next(-90D);
        assertEquals(-90D, next.min());
        assertEquals(0D, next.max());
    }

    @Test
    public void nextShouldReturnCorrectHalfUltimateMax() {
        BoundaryBox initial = new BoundaryBox(-90D, 90D);
        BoundaryBox next = initial.next(90D);
        assertEquals(0D, next.min());
        assertEquals(90D, next.max());
    }



}