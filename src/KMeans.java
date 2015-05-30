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

    public static HashMap<Integer, LinkedList<Tuple>> cluster_points(
            List<Tuple> X, List<Tuple> mu) {
        HashMap<Integer, LinkedList<Tuple>> clusters = new HashMap<>();
        for (Tuple x : X) {
            Tuple[] tuples = new Tuple[mu.size()];
            for (int i = 0; i < mu.size(); i++) {
                Tuple muTup = mu.get(i);
                tuples[i] = new Tuple(i, (x.sub(muTup)).abs());
            }
            int bestmukey = (int) Util.getMinByVal(tuples).getFirst();
            try {
                clusters.get(bestmukey).add(x);
            } catch (Exception e) {
                LinkedList<Tuple> list = new LinkedList<>();
                list.add(x);
                clusters.put(bestmukey, list);
            }
        }
        return clusters;
    }

    public static LinkedList<Tuple> reevaluate_centers(List<Tuple> mu,
                                                       HashMap<Integer, LinkedList<Tuple>> clusters) {
        LinkedList<Tuple> newmu = new LinkedList<Tuple>();

        Set<Integer> keySet = clusters.keySet();

        List<Integer> keys = new LinkedList<>();
        keys.addAll(keySet);
        Collections.sort(keys);

        for (Integer k : keys) {
            newmu.add(Util.meanByCol(clusters.get(k)));
        }
        return newmu;
    }

    public static MuAndClusters find_centers(List<Tuple> X, int K) {
        List<Tuple> oldmu = Util.sample(X, K);
        List<Tuple> mu = Util.sample(X, K);

        HashMap<Integer, LinkedList<Tuple>> clusters = null;
        do {
            oldmu = mu;
            // # Assign all points in X to clusters
            clusters = cluster_points(X, mu);
            // # Reevaluate centers
            mu = reevaluate_centers(oldmu, clusters);
        } while (!has_converged(mu, oldmu));
        return new MuAndClusters(mu, clusters);
    }

    public static boolean has_converged(List<Tuple> mu, List<Tuple> oldmu) {
        Set<Tuple> fst = new HashSet<>();
        fst.addAll(mu);
        Set<Tuple> snd = new HashSet<>();
        snd.addAll(oldmu);
        boolean converged = fst.equals(snd);

        return converged;
    }

    public static double Wk(List<Tuple> mu,
                            HashMap<Integer, LinkedList<Tuple>> clusters) {
        int K = mu.size();
        double sum = 0;
        for (int i = 0; i < K; i++) {
            LinkedList<Tuple> c = clusters.get(i);
            List<Tuple> diff = mu.get(i).sub(c);
            double normVal = Util.norm(diff);
            normVal = normVal * normVal;
            normVal = normVal / (2.0 * c.size());
            sum += normVal;
        }
        return sum;
    }

    public static BoundingBox bounding_box(List<Tuple> X) {
        double xmin = Util.getMinFirst(X);
        double xmax = Util.getMaxFirst(X);
        double ymin = Util.getMinSecond(X);
        double ymax = Util.getMaxSecond(X);
        return new BoundingBox(new Tuple(xmin, xmax), new Tuple(ymin, ymax));
    }

    public static double uniform(double a, double b) {
        return a + (b - a) * Math.random();
    }

    public static GapStatistics gap_statistic(List<Tuple> X) {
        BoundingBox boundingBox = bounding_box(X);
        double xmin = boundingBox.xRange.getFirst();
        double xmax = boundingBox.xRange.getSecond();
        double ymin = boundingBox.yRange.getFirst();
        double ymax = boundingBox.yRange.getSecond();

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
            List<Tuple> mu = muAndClusters.mu;
            HashMap<Integer, LinkedList<Tuple>> clusters = muAndClusters.clusters;
            Wks.add(Math.log(Wk(mu, clusters)));

            // Create B reference datasets
            List<Double> BWkbs = new ArrayList(B);
            for (int i = 0; i < B; i++) {
                List<Tuple> Xb = new LinkedList<>();
                for (int n = 0; n < X.size(); n++) {
                    Xb.add(new Tuple(uniform(xmin, xmax), uniform(ymin, ymax)));
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
        return new GapStatistics(new Tuple(start, finish), Wks, Wkbs, sk);
    }

    public static List<Tuple> init_from_file(String fileName) {
        List<Tuple> result = new LinkedList<>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            System.out.println("Reading file");
            while (line != null) {
                String[] splitLine = line.split(",");
                double x = Double.valueOf(splitLine[0]);
                double y = Double.valueOf(splitLine[1]);
                result.add(new Tuple(x, y));
                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        List<Tuple> X = init_from_file("input-kmeans.csv");
        GapStatistics gs = gap_statistic(X);
        System.out.println(gs);
    }
}
