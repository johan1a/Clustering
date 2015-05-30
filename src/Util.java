import java.util.*;

/**
 * Created by Johan on 2015-05-30.
 */
public class Util {
    public static void printList(Tuple[] tuples) {
        for(Tuple t : tuples){
            System.out.print (t + ", ");
        }
    }

    public static Tuple meanByCol(List<Tuple> tuples) {
        double xMean = 0;
        double yMean = 0;
        for (Tuple tuple : tuples) {
            xMean += tuple.getFirst();
            yMean += tuple.getSecond();
        }
        xMean /= tuples.size();
        yMean /= tuples.size();
        return new Tuple(xMean, yMean);
    }

    public static List<Tuple> sample(List<Tuple> list, int K) {
        List<Tuple> sampled = new LinkedList<Tuple>();
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

    public static double norm(List<Tuple> tuples) {
        double sum = 0;
        for (Tuple tuple : tuples) {
            sum += tuple.squaredValues();
        }
        return Math.sqrt(sum);
    }

    public static double getMinFirst(List<Tuple> tuples) {
        double min = tuples.get(0).getFirst();
        for (Tuple tup : tuples) {
            if (tup.getFirst() < min) {
                min = tup.getFirst();
            }
        }
        return min;
    }

    public static double getMaxFirst(List<Tuple> tuples) {
        double min = tuples.get(0).getFirst();
        for (Tuple tup : tuples) {
            if (tup.getFirst() > min) {
                min = tup.getFirst();
            }
        }
        return min;
    }

    public static double getMinSecond(List<Tuple> tuples) {
        double min = tuples.get(0).getSecond();
        for (Tuple tup : tuples) {
            if (tup.getSecond() < min) {
                min = tup.getSecond();
            }
        }
        return min;
    }

    public static double getMaxSecond(List<Tuple> tuples) {
        double min = tuples.get(0).getSecond();
        for (Tuple tup : tuples) {
            if (tup.getSecond() > min) {
                min = tup.getSecond();
            }
        }
        return min;
    }

    public static Tuple getMinByVal(Tuple[] tuples) {
        double min = tuples[0].getSecond();
        Tuple minTuple = tuples[0];
        for (Tuple tup : tuples) {
            if (tup.getSecond() < min) {
                minTuple = tup;
                min = minTuple.getSecond();
            }
        }
        return minTuple;
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
