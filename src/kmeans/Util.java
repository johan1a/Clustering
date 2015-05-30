package kmeans;

import common.DataPoint;

import java.util.*;

/**
 * Created by Johan on 2015-05-30.
 */
public class Util {
    public static void printList(DataPoint[] dataPoints) {
        for(DataPoint t : dataPoints){
            System.out.print (t + ", ");
        }
    }

    public static DataPoint meanByCol(List<DataPoint> dataPoints) {
        double xMean = 0;
        double yMean = 0;
        for (DataPoint dataPoint : dataPoints) {
            xMean += dataPoint.getFirst();
            yMean += dataPoint.getSecond();
        }
        xMean /= dataPoints.size();
        yMean /= dataPoints.size();
        return new DataPoint(xMean, yMean);
    }

    public static List<DataPoint> sample(List<DataPoint> list, int K) {
        List<DataPoint> sampled = new LinkedList<DataPoint>();
        Random random = new Random();
        Set<Integer> taken = new HashSet<>();
        int index = random.nextInt(list.size());
        for (int i = 0; i < K; i++) {
            while (taken.contains(index)) {
                index = random.nextInt(list.size());
            }
            taken.add(index);
            sampled.add(list.get(index));
        }
        return sampled;
    }

    public static double norm(List<DataPoint> dataPoints) {
        double sum = 0;
        for (DataPoint dataPoint : dataPoints) {
            sum += dataPoint.squaredValues();
        }
        return Math.sqrt(sum);
    }

    public static double getMinFirst(List<DataPoint> dataPoints) {
        double min = dataPoints.get(0).getFirst();
        for (DataPoint tup : dataPoints) {
            if (tup.getFirst() < min) {
                min = tup.getFirst();
            }
        }
        return min;
    }

    public static double getMaxFirst(List<DataPoint> dataPoints) {
        double min = dataPoints.get(0).getFirst();
        for (DataPoint tup : dataPoints) {
            if (tup.getFirst() > min) {
                min = tup.getFirst();
            }
        }
        return min;
    }

    public static double getMinSecond(List<DataPoint> dataPoints) {
        double min = dataPoints.get(0).getSecond();
        for (DataPoint tup : dataPoints) {
            if (tup.getSecond() < min) {
                min = tup.getSecond();
            }
        }
        return min;
    }

    public static double getMaxSecond(List<DataPoint> dataPoints) {
        double min = dataPoints.get(0).getSecond();
        for (DataPoint tup : dataPoints) {
            if (tup.getSecond() > min) {
                min = tup.getSecond();
            }
        }
        return min;
    }

    public static DataPoint getMinByVal(DataPoint[] dataPoints) {
        double min = dataPoints[0].getSecond();
        DataPoint minDataPoint = dataPoints[0];
        for (DataPoint tup : dataPoints) {
            if (tup.getSecond() < min) {
                minDataPoint = tup;
                min = minDataPoint.getSecond();
            }
        }
        return minDataPoint;
    }

    public static List<Double> mul(List<Double> list, double d) {
        List<Double> result = new ArrayList<>();
        for (Double a : list) {
            result.add(a * d);
        }
        return result;
    }

    public static List<Double> square(List<Double> list) {
        List<Double> result = new ArrayList<>();
        for (Double a : list) {
            result.add(a * a);
        }
        return result;
    }

    public static List<Double> sub(List<Double> list, double d) {
        List<Double> result = new ArrayList<>();
        for (Double a : list) {
            result.add(a - d);
        }
        return result;
    }

    public static double sum(List<Double> list) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum;
    }
}
