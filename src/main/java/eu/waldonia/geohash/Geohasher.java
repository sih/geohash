package eu.waldonia.geohash;

/**
 * This is an implementation of the Niemeyer Geohash algorithm described at
 * https://en.wikipedia.org/wiki/Geohash
 * @author sih
 */
public class Geohasher {

    private static final int MIN_PRECISION = 3;
    private static final int MAX_PRECISION = 12;
    private static final int WORD_LENGTH = 5;

    private static final Double MIN_LNG = -180D;
    private static final Double MAX_LNG = 180D;

    private static final Double MIN_LAT = -90D;
    private static final Double MAX_LAT = 90D;

    private static final char[] BASE32 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };


    /**
     *
     * @param longitude The decimal longitude of the point to classify
     * @param latitude The decimal latitude of the point to classify
     * @param precision How precise the geohash should be. The more precise the geohash, the more characters in the hash
     * @return The geohash value for this coordinate
     */
    public String hash(Double longitude, Double latitude, int precision) {

        validate(longitude,latitude);

        int revisedPrecision = Math.min((Math.max(MIN_PRECISION, precision)),MAX_PRECISION);

        StringBuffer buffy = new StringBuffer();

        // turn this in to N 5-bit "words" so we can encode to base32
        int iterations = revisedPrecision * WORD_LENGTH;
        boolean[] interleavings = interleave(longitude, latitude, iterations);

        // split the total flag array in to 5-bit chunks ...
        for (int i = 0; i < revisedPrecision; i++) {
            boolean[] word = new boolean[WORD_LENGTH];
            for (int j = 0; j < WORD_LENGTH; j++) {
                word[j] = interleavings[i*WORD_LENGTH+j];
            }
            // ... and turn this in to a base32 encoded char
            int index = toBase2(word);
            buffy.append(BASE32[index]);
        }

        return buffy.toString();
    }

    private void validate(Double longitude, Double latitude) {
        if (longitude < -180D || longitude > 180D) {
            throw new IllegalArgumentException("Longitude is invalid. Should be between (-180D,180D) was "+longitude);
        }
        if (latitude < -90D || latitude > 90D) {
            throw new IllegalArgumentException("Latitude is invalid. Should be between (-90D,90D) was "+latitude);
        }
    }


    /**
     * This performs the hash calculation by in turn classifying the longitude
     * and latitude as per the geohash algorithm (see https://en.wikipedia.org/wiki/Geohash)
     * @param longitude The decimal longitude of the point to classify
     * @param latitude The decimal latitude of the point to classify
     * @param iterations The number of classifications to perform
     * @return The array of interleaved classifications
     */
    boolean[] interleave(Double longitude, Double latitude, int iterations) {

        boolean[] interleavings = new boolean[iterations];

        BoundaryBox bbLng = new BoundaryBox(MIN_LNG,MAX_LNG);
        BoundaryBox bbLat = new BoundaryBox(MIN_LAT,MAX_LAT);

        for (int i = 0; i < iterations; i++) {

            // even iterations => do longitude
            if (i % 2 ==  0) {
                interleavings[i] = bbLng.classify(longitude);
                bbLng = bbLng.next(longitude);
            }
            // odd iterations => do latitude
            else {
                interleavings[i] = bbLat.classify(latitude);
                bbLat = bbLat.next(latitude);
            }
        }

        return interleavings;
    }

    /**
     * Each item in the flags array represents a bit in a base 2 number.
     * <p/>
     * So for example {true, true, false, false, true} would represent the
     * binary number x11001, which is 25 in decimal.
     * <p/>
     * This method performs that conversion.
     * @param flags An array of booleans representing bits in a binary number
     * @return The decimal value of this "binary" representation
     */
    int toBase2(boolean[] flags) {
        int result = 0;
        int exp = flags.length-1;

        for (int i = 0; i < flags.length; i++) {
            if (flags[i]) {
                result += Math.pow(2,exp);
            }
            exp--;
        }

        return result;
    }



}
