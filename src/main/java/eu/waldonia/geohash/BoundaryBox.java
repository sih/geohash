package eu.waldonia.geohash;

/**
 * @author sih
 */
final class BoundaryBox {

    private Double min;
    private Double max;

    BoundaryBox(Double min, Double max) {
        if (max < min) throw new IllegalArgumentException("Max and min incorrect");
        this.min = min;
        this.max = max;
    }

    Double midpoint() {
        return min + (max - min)/2D;
    }

    /**
     * @param coordinate
     * @return true (1) if the coordinate is to the "right" of the midpoint and false (0) if otherwise
     */
    boolean classify(Double coordinate) {
        return coordinate > midpoint();
    }

    BoundaryBox next(Double coordinate) {
        if (classify(coordinate)) {
            return new BoundaryBox(midpoint(), max);
        }
        else {
            return new BoundaryBox(min, midpoint());
        }
    }

    Double min() {
        return min;
    }

    Double max() {
        return max;
    }
}
