package common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataPoint {
	@Override
	public String toString() {
		return "common.DataPoint [first=" + first + ", second=" + second + "]";
	}

	private double first;
	private double second;
	
	public DataPoint(double first, double second) {
		this.first = first;
		this.second = second;
	}
	
	public double getFirst(){
		return first;
	}
	public double getSecond(){
		return second;
	}
	public DataPoint sub(DataPoint other){
		return new DataPoint(first - other.first, second - other.second);
	}
	public double abs(){
		return Math.sqrt(squaredValues());
	}
	
	public double squaredValues(){
		return first * first + second * second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(first);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(second);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataPoint other = (DataPoint) obj;
		if (Double.doubleToLongBits(first) != Double.doubleToLongBits(other.first))
			return false;
		if (Double.doubleToLongBits(second) != Double.doubleToLongBits(other.second))
			return false;
		return true;
	}

	public List<DataPoint> sub(LinkedList<DataPoint> c) {
		List<DataPoint> result = new ArrayList<>();
		for(DataPoint t : c){
			result.add(new DataPoint(first - t.getFirst(), second - t.getSecond()));
		}
		return result;
	}
}
