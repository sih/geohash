package eu.waldonia.geohash;

/**
 * This is a simple boundary box with a min, max and operations to provide the midpoint and geohash classification.
 * @author sih
 */
final class BoundaryBox {

    private Double min;
    private Double max;

    /**
     * This performs basic validation to ensure that the max is greater than the min
     * @param min The lower bound of the box
     * @param max The upper bound of the box
     */
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

    /**
     * Based on the classification of this coordinate, this method returns the "next" boundary
     * box that supports the algorithm - i.e. the half of the box that contains the coordinate.
     * @param coordinate The coordinate to classify
     * @return The box half containing this coordinate
     */
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
