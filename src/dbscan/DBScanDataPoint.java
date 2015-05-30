package dbscan;

import common.DataPoint;

/**
 * Created by Johan on 2015-05-30.
 */
public class DBScanDataPoint extends DataPoint {
    private boolean isVisited = false;
    private boolean isNoise;
    private int cluster = -1;
    private int id;

    public DBScanDataPoint(double first, double second, int id) {
        super(first, second);
        this.id = id;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited() {
        isVisited = true;
    }

    public void setIsNoise() {
        isNoise = true;
    }

    public void setCluster(int c) {
        cluster = c;
    }

    public boolean belongsToACluster() {
        return cluster != -1;
    }

    public double distanceTo(DBScanDataPoint other) {
        double yDist = other.getSecond() - getSecond();
        double xDist = other.getFirst() - getFirst();
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public int getId() {
        return id;
    }
}
