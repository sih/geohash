package eu.waldonia.geohash;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * @author sih
 */
public class GeohasherTest {

    private Geohasher geohasher;

    @Before
    public void setUp() {
        geohasher = new Geohasher();
    }

    @Test
    public void interleaveShouldReturnValidResult() throws Exception {

        Double longitude = 61.19D;
        Double latitude = 34.16D;

        boolean[] interleavings = geohasher.interleave(longitude,latitude,6);
        assertEquals(true, interleavings[0]);
        assertEquals(true, interleavings[1]);
        assertEquals(false, interleavings[2]);
        assertEquals(false, interleavings[3]);
        assertEquals(true, interleavings[4]);
        assertEquals(true, interleavings[5]);

    }

    @Test
    public void toBase2ShouldReturnValidResult() {
        int expectedResult = 51;

        boolean[] interleavings = {true,true,false,false,true,true};
        assertEquals(expectedResult, geohasher.toBase2(interleavings));
    }

    /**
     * https://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void hashShouldReturnValidValue() {
        Double latitude = 57.64911D;
        Double longitude = 10.40744D;

        int precision = 5;

        String hash = geohasher.hash(longitude,latitude,precision);
        assertEquals("u4pru",hash);
    }

    /**
     * https://en.wikipedia.org/wiki/Geohash
     */
    @Test
    public void hashShouldRespectPrecision() {
        Double latitude = 57.64911D;
        Double longitude = 10.40744D;

        int precision = 11;

        String hash = geohasher.hash(longitude,latitude,precision);
        assertEquals("u4pruydqqvj",hash);
    }

    @Test
    public void hashShouldRespectMinimumPrecision() {
        Double latitude = 57.64911D;
        Double longitude = 10.40744D;

        int precision = 1;

        String hash = geohasher.hash(longitude,latitude,precision);
        assertEquals("u4p",hash);

    }

    @Test
    public void hashShouldRespectMaximumPrecision() {
        Double latitude = 57.64911D;
        Double longitude = 10.40744D;

        int precision = 100;

        String hash = geohasher.hash(longitude,latitude,precision);
        assertEquals("u4pruydqqvj8",hash);

    }


    @Test
    public void hashShouldValidateLongitude() {
        try {
            geohasher.hash(-1000D,34D,5);
            fail("Should have thrown an IAE");
        }
        catch(IllegalArgumentException iae) {
            // all good
        }
        try {
            geohasher.hash(1000D,34D,5);
            fail("Should have thrown an IAE");
        }
        catch(IllegalArgumentException iae) {
            // all good
        }
    }

    @Test
    public void hashShouldValidateLatitude() {
        try {
            geohasher.hash(-100D,344D,5);
            fail("Should have thrown an IAE");
        }
        catch(IllegalArgumentException iae) {
            // all good
        }
        try {
            geohasher.hash(-100D,344D,5);
            fail("Should have thrown an IAE");
        }
        catch(IllegalArgumentException iae) {
            // all good
        }
    }

}