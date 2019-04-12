package util;

import java.util.Arrays;

public class Statistics {

	protected double[] data;
	protected int size;

	public Statistics() {
		// System.out.println("in statistics section");
	}

	// constructor
	public Statistics(double[] data) {
		this.data = data;
		size = data.length;
	}

	public double getMean() {
		double sum = 0.0;
		for (double a : data)
			sum += a;
		return sum / size;
	}

	public double getVariance() {
		double mean = getMean();
		double temp = 0;
		for (double a : data)
			temp += (a - mean) * (a - mean);
		return temp / (size);
	}

	public double getStdDev() {
		return Math.sqrt(getVariance());
	}

	public double median() {
		Arrays.sort(data);
		if (data.length % 2 == 0)
			return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
		return data[data.length / 2];
	}

	public void setData(double[] data) {
		this.data = data;
		size = data.length;
	}

}