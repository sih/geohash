package eu.waldonia.geohash;

/**
 * @author sih
 */
public class Geohasher {

    private static final int DEFAULT_PRECISION = 6;
    private static final int MAX_PRECISION = 12;
    private static final int WORD_LENGTH = 5;

    private static final Double MIN_LNG = -180D;
    private static final Double MAX_LNG = 180D;

    private static final Double MIN_LAT = -90D;
    private static final Double MAX_LAT = 90D;

    private static final char[] BASE32 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };


    public String hash(Double longitude, Double latitude, int precision) {
        StringBuffer buffy = new StringBuffer();
        int iterations = precision * WORD_LENGTH;
        boolean[] interleavings = interleave(longitude, latitude, iterations);
        for (int i = 0; i < precision; i++) {
            boolean[] word = new boolean[WORD_LENGTH];
            for (int j = 0; j < WORD_LENGTH; j++) {
                word[j] = interleavings[i*WORD_LENGTH+j];
            }
            int index = toBase2(word);
            buffy.append(BASE32[index]);
        }

        return buffy.toString();
    }


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
