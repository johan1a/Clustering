import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



	public class MuAndClusters {
		public HashMap<Integer, LinkedList<Tuple>> clusters;
		public List<Tuple> mu;

		public MuAndClusters(List<Tuple> mu,
				HashMap<Integer, LinkedList<Tuple>> clusters2) {
			this.mu = mu;
			this.clusters = clusters2;
		}

	}