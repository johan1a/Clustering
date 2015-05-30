package kmeans;

import common.DataPoint;

public class BoundingBox {
    public final double ymax;
    public final double xmin;
    public final double xmax;
    public final double ymin;

    public BoundingBox(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }
}
