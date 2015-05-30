package dbscan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Johan on 2015-05-30.
 */
public class DBScan {
    private final List<DBScanDataPoint> dataPoints;
    private final Map<Integer, Set<DBScanDataPoint>> neighbors;
    private Map<Integer, Set<DBScanDataPoint>> clusters;
    private int nbrClusters;

    public DBScan(List<DBScanDataPoint> dataPoints, Map<Integer, Set<DBScanDataPoint>> neighbors) {
        this.dataPoints = dataPoints;
        this.neighbors = neighbors;
        clusters = new HashMap<>();
    }

    public void dBScan(int minPts) {
        int c = -1;
        for (DBScanDataPoint point : dataPoints) {
            if (point.isVisited()) {
                continue;
            }
            point.setVisited();
            Set<DBScanDataPoint> neighborPoints = regionQuery(point);

            if (neighborPoints.size() < minPts) {
                point.setIsNoise();
                System.out.println("Setting point to noise: " + point);
            } else {
                c++;
                expandCluster(point, neighborPoints, c, minPts);
            }
        }
        nbrClusters = c + 1;
    }

    /*
        return all points within P's eps-neighborhood (including P)
        */
    private Set<DBScanDataPoint> regionQuery(DBScanDataPoint p) {
        return neighbors.get(p.getId());
    }

    private void expandCluster(DBScanDataPoint point, Set<DBScanDataPoint> neighborPts, int cluster, int minPts) {
        addToCluster(point, cluster);

        LinkedList<DBScanDataPoint> neighborList = new LinkedList<>();
        neighborList.addAll(neighborPts);
        while (!neighborList.isEmpty()) {
            DBScanDataPoint point2 = neighborList.pop();
            if (!point2.isVisited()) {
                point2.setVisited();
                Set<DBScanDataPoint> neighborPoints2 = regionQuery(point2);
                if (neighborPoints2.size() >= minPts) {
                    neighborList.addAll(neighborPoints2);
                }
            }
            if (!point2.belongsToACluster()) {
                addToCluster(point2, cluster);
            }
        }
    }

    private void addToCluster(DBScanDataPoint point, int cluster) {
        if (!clusters.containsKey(cluster)) {
            clusters.put(cluster, new HashSet<DBScanDataPoint>());
        }
        clusters.get(cluster).add(point);
    }

    public static List<DBScanDataPoint> initFromFile(String fileName) {
        List<DBScanDataPoint> result = new LinkedList<>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            System.out.println("Reading file");
            int index = 0;
            while (line != null) {
                String[] splitLine = line.split(",");
                double x = Double.valueOf(splitLine[0]);
                double y = Double.valueOf(splitLine[1]);
                result.add(new DBScanDataPoint(x, y, index));
                index++;
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Map<Integer, Set<DBScanDataPoint>> calulateNeighbors(List<DBScanDataPoint> dataPoints, double eps) {
        Map<Integer, Set<DBScanDataPoint>> neighbors = new HashMap<>();
        for (int i = 0; i < dataPoints.size(); i++) {
            addNeighbor(i, i, dataPoints, neighbors);
            for (int j = i + 1; j < dataPoints.size(); j++) {
                double distance = dataPoints.get(i).distanceTo(dataPoints.get(j));
                if (distance < eps) {
                    addNeighbor(i, j, dataPoints, neighbors);
                    addNeighbor(j, i, dataPoints, neighbors);
                }
            }
        }
        return neighbors;
    }

    private static void addNeighbor(int i, int j, List<DBScanDataPoint> dataPoints, Map<Integer, Set<DBScanDataPoint>> neighbors) {
        Set<DBScanDataPoint> distMap = neighbors.get(i);
        if (distMap == null) {
            distMap = new HashSet<>();
            neighbors.put(i, distMap);
        }
        distMap.add(dataPoints.get(j));

    }

    private void printClusters() {
        System.out.println("Nbr of clusters: " + nbrClusters);
        for (int i = 0; i < nbrClusters; i++) {
            Set<DBScanDataPoint> cluster = clusters.get(i);
            System.out.println("\nCluster #" + i + " has: " + cluster.size() + " points:");
            for (DBScanDataPoint point : cluster) {
                System.out.println(i + " " + point);
            }
        }
    }

    public Map<Integer, Set<DBScanDataPoint>> getClusters() {
        return clusters;
    }

    public int getNbrClusters() {
        return nbrClusters;
    }

    public static void main(String[] args) {
        int eps = 50;
        int minPontsPerCluster = 10;

        List<DBScanDataPoint> dataPoints = initFromFile("input-kmeans.csv");
        Map<Integer, Set<DBScanDataPoint>> neighbors = calulateNeighbors(dataPoints, eps);
        DBScan scan = new DBScan(dataPoints, neighbors);

        scan.dBScan(minPontsPerCluster);
        scan.printClusters();
    }
}
