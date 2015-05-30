package kmeans;

import common.DataPoint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



	public class MuAndClusters {
		public HashMap<Integer, LinkedList<DataPoint>> clusters;
		public List<DataPoint> mu;

		public MuAndClusters(List<DataPoint> mu,
				HashMap<Integer, LinkedList<DataPoint>> clusters2) {
			this.mu = mu;
			this.clusters = clusters2;
		}

	}