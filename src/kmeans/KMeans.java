package kmeans;

import common.DataPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class KMeans {

    public static HashMap<Integer, LinkedList<DataPoint>> cluster_points(
            List<DataPoint> X, List<DataPoint> mu) {
        HashMap<Integer, LinkedList<DataPoint>> clusters = new HashMap<>();
        for (DataPoint x : X) {
            DataPoint[] dataPoints = new DataPoint[mu.size()];
            for (int i = 0; i < mu.size(); i++) {
                DataPoint muTup = mu.get(i);
                dataPoints[i] = new DataPoint(i, (x.sub(muTup)).abs());
            }
            int bestmukey = (int) Util.getMinByVal(dataPoints).getFirst();
            try {
                clusters.get(bestmukey).add(x);
            } catch (Exception e) {
                LinkedList<DataPoint> list = new LinkedList<>();
                list.add(x);
                clusters.put(bestmukey, list);
            }
        }
        return clusters;
    }

    public static LinkedList<DataPoint> reevaluate_centers(List<DataPoint> mu,
                                                       HashMap<Integer, LinkedList<DataPoint>> clusters) {
        LinkedList<DataPoint> newmu = new LinkedList<DataPoint>();

        Set<Integer> keySet = clusters.keySet();

        List<Integer> keys = new LinkedList<>();
        keys.addAll(keySet);
        Collections.sort(keys);

        for (Integer k : keys) {
            newmu.add(Util.meanByCol(clusters.get(k)));
        }
        return newmu;
    }

    public static MuAndClusters find_centers(List<DataPoint> X, int K) {
        List<DataPoint> oldmu = Util.sample(X, K);
        List<DataPoint> mu = Util.sample(X, K);

        HashMap<Integer, LinkedList<DataPoint>> clusters = null;
        do {
            oldmu = mu;
            // # Assign all points in X to clusters
            clusters = cluster_points(X, mu);
            // # Reevaluate centers
            mu = reevaluate_centers(oldmu, clusters);
        } while (!has_converged(mu, oldmu));
        return new MuAndClusters(mu, clusters);
    }

    public static boolean has_converged(List<DataPoint> mu, List<DataPoint> oldmu) {
        Set<DataPoint> fst = new HashSet<>();
        fst.addAll(mu);
        Set<DataPoint> snd = new HashSet<>();
        snd.addAll(oldmu);
        boolean converged = fst.equals(snd);

        return converged;
    }

    public static double Wk(List<DataPoint> mu,
                            HashMap<Integer, LinkedList<DataPoint>> clusters) {
        int K = mu.size();
        double sum = 0;
        for (int i = 0; i < K; i++) {
            LinkedList<DataPoint> c = clusters.get(i);
            List<DataPoint> diff = mu.get(i).sub(c);
            double normVal = Util.norm(diff);
            normVal = normVal * normVal;
            normVal = normVal / (2.0 * c.size());
            sum += normVal;
        }
        return sum;
    }

    public static BoundingBox bounding_box(List<DataPoint> X) {
        double xmin = Util.getMinFirst(X);
        double xmax = Util.getMaxFirst(X);
        double ymin = Util.getMinSecond(X);
        double ymax = Util.getMaxSecond(X);
        return new BoundingBox(xmin, xmax, ymin, ymax);
    }

    public static double uniform(double a, double b) {
        return a + (b - a) * Math.random();
    }

    public static GapStatistics gap_statistic(List<DataPoint> X) {
        BoundingBox boundingBox = bounding_box(X);
        double xmin = boundingBox.xmin;
        double xmax = boundingBox.xmax;
        double ymin = boundingBox.ymin;
        double ymax = boundingBox.ymax;

        // # Dispersion for real distribution
        int start = 1;
        int finish = 10;
        int size = finish - start;

        List<Double> Wks = new ArrayList(size);
        List<Double> Wkbs = new ArrayList(size);
        List<Double> sk = new ArrayList(size);

        int B = 10;
        int indk = 0;
        for (int k = start; k < finish; k++) {
            System.out.println("k: " + k);
            MuAndClusters muAndClusters = find_centers(X, k);
            List<DataPoint> mu = muAndClusters.mu;
            HashMap<Integer, LinkedList<DataPoint>> clusters = muAndClusters.clusters;
            Wks.add(Math.log(Wk(mu, clusters)));

            // Create B reference datasets
            List<Double> BWkbs = new ArrayList(B);
            for (int i = 0; i < B; i++) {
                List<DataPoint> Xb = new LinkedList<>();
                for (int n = 0; n < X.size(); n++) {
                    Xb.add(new DataPoint(uniform(xmin, xmax), uniform(ymin, ymax)));
                }
                muAndClusters = find_centers(Xb, k);
                mu = muAndClusters.mu;
                clusters = muAndClusters.clusters;
                BWkbs.add(Math.log(Wk(mu, clusters)));

            }
            Wkbs.add(Util.sum(BWkbs) / B);
            List<Double> subVal = Util.sub(BWkbs, Wkbs.get(indk));
            subVal = Util.square(subVal);
            double sum = Util.sum(subVal);
            sk.add(Math.sqrt(sum / B));
            indk++;
        }
        sk = Util.mul(sk, Math.sqrt(1 + 1 / B));
        return new GapStatistics(new DataPoint(start, finish), Wks, Wkbs, sk);
    }

    public static List<DataPoint> init_from_file(String fileName) {
        List<DataPoint> result = new LinkedList<>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            System.out.println("Reading file");
            while (line != null) {
                String[] splitLine = line.split(",");
                double x = Double.valueOf(splitLine[0]);
                double y = Double.valueOf(splitLine[1]);
                result.add(new DataPoint(x, y));
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        List<DataPoint> X = init_from_file("input-kmeans.csv");
        GapStatistics gs = gap_statistic(X);
        System.out.println(gs);
    }
}
