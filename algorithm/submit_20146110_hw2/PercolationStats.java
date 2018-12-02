/**
 * Homework Assignment #4: Percolation
 *
 * - PercolationStats for simulation of percolation
 *
 * @ Student ID : 20146110
 * @ Name       : moon tae hyun
 **/

import java.util.Random;

public class PercolationStats {

	private int mT; // number of experiments to run
	private double mMean; // mean of percolation threshold
	private double mStddev; // standard deviation of percolation threshold

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {

		if (N <= 0 || T <= 0)
			throw new java.lang.IllegalArgumentException("N and T must be greater than 1");
		mT = T;

		double[] result = new double[mT];
		
		Random random = new Random(System.currentTimeMillis());
		
		int col, row;
		int openednum;

		for (int i = 0; i < mT; i++) {
			openednum = 0;
			Percolation per = new Percolation(N);
		
			while (!per.percolates()) {
				col = random.nextInt(N) + 1;
				row = random.nextInt(N) + 1;

				if (!per.isOpen(col, row)) {
					per.open(col, row);
					openednum++;
				}
			}
			result[i] = (double) openednum / (N * N);
		}// get percolation probability

		double sum = 0;
		for (int i = 0; i < mT; i++) {
			sum = sum + result[i];
		}
		mMean = sum / mT; // an estimate of the percolation threshold

		double nresult = 0;
		double variance = 0;
		for (int i = 0; i < mT; i++) {
			nresult = nresult + (result[i] - mMean) * (result[i] - mMean);
		}
		variance = nresult /(mT - 1);
		mStddev = Math.sqrt(variance);// get standard deviation
	}

	// sample mean of percolation threshold
	public double mean() {
		return mMean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return mStddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mMean - 1.96 * mStddev / Math.sqrt(mT);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mMean + 1.96 * mStddev / Math.sqrt(mT);
	}

	public static void main(String[] args) // test client(described below)
	{
		PercolationStats percStats;
		int N = 0;
		int T = 0;
		long start = System.currentTimeMillis();
		double time;

		// you must get two inputs N and T
		if (args.length > 0) {
			try {
				N = Integer.parseInt(args[0]); // get N
				T = Integer.parseInt(args[1]); // get T
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " and " + args[1] + " must be integers.");
				return;
			}
		}

		// run experiment
		percStats = new PercolationStats(N, T);

		// print result
		System.out.println("mean                    = " + percStats.mean());
		System.out.println("stddev                  = " + percStats.stddev());
		System.out.println("95% confidence interval = " + percStats.confidenceLo() + ", " + percStats.confidenceHi());

		time = (System.currentTimeMillis() - start) / 1000.0;
		System.out.printf("total time = %f sec (N = %d, T = %d)\n", time, N, T);
	}
}
