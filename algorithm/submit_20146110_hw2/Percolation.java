
/**
 * Homework Assignment #4: Percolation
 *
 *  - Percolation data structure
 *
 * @ Student ID : 20146110
 * @ Name       : moon tae hyun
 **/

import java.util.Scanner;
import java.io.File;

public class Percolation {

	private static final boolean SITE_BLOCKED = false;
	private static final boolean SITE_OPEN = true;
	private boolean[] sites; // sites[i] = state of site i
	private int mN; // remember the input N
	private int topIdx; // idx of the special top
	private int bottomIdx; // idx of the speical bottom
	private WeightedQuickUnionUF uf1;
	private WeightedQuickUnionUF uf2;

	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0)
			throw new IllegalArgumentException("N must be >0");
		
		mN = N;

		sites = new boolean[N * N];
		// Sets all the sites to be blocked
		for (int i = 0; i < N * N; i++)
			sites[i] = SITE_BLOCKED;

		uf1 = new WeightedQuickUnionUF(N * N + 1);// uf for full or not
		uf2 = new WeightedQuickUnionUF(N * N + 2);// uf for percolates or not
		// I made two uf for PercolationVisualizer
		topIdx = N * N;
		bottomIdx = N * N + 1;
	}

	private void checkIndex(int i, int j) {
		if (i < 1 || i > mN)
			throw new IndexOutOfBoundsException("row index i out of bounds");
		if (j < 1 || j > mN)
			throw new IndexOutOfBoundsException("column index j out of bounds");
	}

	// open site(row i, column j) if it is not open already
	public void open(int i, int j) {

		checkIndex(i, j);

		int findNodeIdx = (mN) * (i - 1) + j - 1;

		if (!isOpen(i, j)) {
			sites[findNodeIdx] = SITE_OPEN;
		}// open site

		if (i == 1) {
			uf1.union(findNodeIdx, topIdx);
			uf2.union(findNodeIdx, topIdx);
		}//union top sites with topidx

		if (i == mN) {
			uf2.union(findNodeIdx, bottomIdx);
		}//union bottodm sites with bottomidx
 
		if (i + 1 <= mN && isOpen(i + 1, j)) { 
			uf1.union(findNodeIdx, findNodeIdx + mN);
			uf2.union(findNodeIdx, findNodeIdx + mN);
		}

		if (i - 1 > 0 && isOpen(i - 1, j)) {
			uf1.union(findNodeIdx, findNodeIdx - mN);
			uf2.union(findNodeIdx, findNodeIdx - mN);
		}

		if (j + 1 <= mN && isOpen(i, j + 1)) { 
			uf1.union(findNodeIdx, findNodeIdx + 1);
			uf2.union(findNodeIdx, findNodeIdx + 1);
		}

		if (j - 1 > 0 && isOpen(i, j - 1)) { 
			uf1.union(findNodeIdx, findNodeIdx - 1);
			uf2.union(findNodeIdx, findNodeIdx - 1);

		}
		//open site and union adjacent opened site
	}

	// is site(row i, column j) open?
	public boolean isOpen(int i, int j) {

		checkIndex(i, j);

		return sites[mN * (i - 1) + j - 1]; // check open or not
	}

	// is site(row i, column j) full?
	public boolean isFull(int i, int j) {

		checkIndex(i, j);

		return uf1.connected(topIdx, (i - 1) * (mN) + j - 1); // check full or not
	}

	// does the system percolate?
	public boolean percolates() {
		
		return uf2.connected(topIdx, bottomIdx); // check percolates or not
	}

	// test client(optional)
	public static void main(String[] args) {
		Scanner in;
		int N = 0;
		long start = System.currentTimeMillis();

		try {
			// get input file from argument
			in = new Scanner(new File(args[0]), "UTF-8");
		} catch (Exception e) {
			System.out.println("invalid or no input file ");
			return;
		}

		N = in.nextInt(); // N-by-N percolation system
		System.out.printf("N = %d\n", N);

		// repeatedly read in sites to open and draw resulting system
		Percolation perc = new Percolation(N);
		
		while (in.hasNext()) {
			int i = in.nextInt(); // get i for open site (i,j)
			int j = in.nextInt(); // get j for open site (i,j)
			perc.open(i, j); // open site (i,j)
			System.out.printf("open(%d, %d)\n", i, j);
		}
		if (perc.percolates()) {
			System.out.println("This system percolates");
		} else {
			System.out.println("This system does NOT percolate");
		}

		double time = (System.currentTimeMillis() - start) / 1000.0;
		System.out.println("running time = " + time + " sec");
	}
}
