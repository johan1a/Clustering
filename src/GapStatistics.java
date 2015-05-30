import java.util.ArrayList;
import java.util.List;


public class GapStatistics {
	@Override
	public String toString() {
		return "GapStatistics [logWkbs=" + logWkbs + "\n logWks=" + logWks + "\n ks=" + ks
				+ "\n sk=" + sk + "\n gap=" + gap + "\n diff=" + diff + "]";
	}

	private List<Double> logWkbs;
	private List<Double> logWks;
	private Tuple ks;
	private List<Double> sk;
	private ArrayList<Double> gap;
	private List<Double> diff;

	public GapStatistics(Tuple ks, List<Double> logWks, List<Double> logWkbs,
			List<Double> sk) {
		this.ks = ks;
		this.logWks = logWks;
		this.logWkbs = logWkbs;
		this.sk = sk;
		
		this.gap = new ArrayList<>();
		List<Double> diff = new ArrayList<Double>();
		for(int i = 0; i < logWkbs.size() ; i ++){
			gap.add(logWkbs.get(i) - logWks.get(i));
		}
		for(int i = 0; i < logWkbs.size() - 1; i ++){
			diff.add(gap.get(i)-(gap.get(i+1) - sk.get(i+1)));
		}
		
		this.diff = diff;
	}

	public List<Double> getLogWkbs() {
		return logWkbs;
	}

	public List<Double> getLogWks() {
		return logWks;
	}

	public Tuple getKs() {
		return ks;
	}

	public List<Double> getSk() {
		return sk;
	}

}
