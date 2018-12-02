
/**
 * Homework Assignment #6: "8-Puzzle"
 *
 *  - Board class for solving "8-Puzzle" Programming Assignment
 *
 *  Compilation:  javac Board.java Queue.java
 *
 * @ Student ID : 20146110
 * @ Name       : moon tae hyun
 **/

import java.io.File;
import java.util.Scanner;

public class Board {

	private int[][] tiles;
	private int N;
	private int xindex;
	private int yindex;
	private int setmanhattan;
	private int sethamming;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		if (blocks == null)
			throw new java.lang.NullPointerException();

		N = blocks.length;
		if (N < 2 || N > 128)
			throw new IllegalArgumentException("N must be <= 128");

		tiles = new int[N][N];
		for (int i = 0; i < N; i++)
			System.arraycopy(blocks[i], 0, tiles[i], 0, blocks[i].length);

		// reset manhattan,hamming
		setman();
		setham();
	}

	// board dimension N
	public int dimension() {
		return N;
	}

	// number of tiles outof place
	public int hamming() {
		int numOfDiffBlocks = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] != j + i * N + 1) {
					numOfDiffBlocks++;
				}
			}
		}
		return --numOfDiffBlocks; // exclude the blank block
	}

	// to minimize calculate hamming i made these two functions
	public void setham() {
		this.sethamming = hamming();
	}

	public int getham() {
		return this.sethamming;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int manhattan = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] == 0)
					continue;

				xindex = (int) (Math.ceil(tiles[i][j] / (double) N) - 1);
				yindex = tiles[i][j] - (N * xindex) - 1;

				if (i != xindex || j != yindex) {
					manhattan += (Math.abs(xindex - i) + Math.abs(yindex - j));
				}
			}
		}
		return manhattan;
	}

	// to minimize calculate hamming i made these two functions
	public void setman() {
		this.setmanhattan = manhattan();
	}

	public int getman() {
		return this.setmanhattan;
	}

	// is this board the goal board?
	public boolean isGoal() {
		for (int i = 0; i < (N * N - 1); i++) {
			int row = i / N;
			int column = i % N;

			if (tiles[row][column] != (i + 1))
				return false;
		}
		return true;
	}

	private void swap(int[][] blocks, int r1, int c1, int r2, int c2) {
		if (r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0)
			throw new IndexOutOfBoundsException("row/col index < 0");
		if (r1 >= N || c1 >= N || r2 >= N || c2 >= N)
			throw new IndexOutOfBoundsException("row/col index >= N");

		// swap blocks
		int temp;
		temp = blocks[r1][c1];
		blocks[r1][c1] = blocks[r2][c2];
		blocks[r2][c2] = temp;

	}

	// a board that is obtained by exchanging two adjacent blocks in the same row
	public Board twin() {
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			System.arraycopy(tiles[i], 0, blocks[i], 0, tiles[i].length);

		if (blocks[0][0] * blocks[0][1] == 0) {
			swap(blocks, 1, 0, 1, 1);
		} else {
			swap(blocks, 0, 0, 0, 1);
		}

		return new Board(blocks);
	}

	// does this board equal y?
	public boolean equals(Object y) {

		if (y == this)
			return true;
		if (y == null)
			return false;
		if (y.getClass() != this.getClass())
			return false;

		Board bo = (Board) y;

		if (this.N != bo.N)
			return false;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				if (this.tiles[i][j] != bo.tiles[i][j])
					return false;
		}

		return true;
	}

	private int[][] copy() {

		int[][] copy = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				copy[i][j] = tiles[i][j];
		return copy;
	}// return copy board

	// all neighboring boards
	public Iterable<Board> neighbors() {

		Queue<Board> nbrs = new Queue<Board>();
		Board copy;

		// find blank tile index
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] == 0) {
					xindex = i;
					yindex = j;
				}
			}
		}

		if (yindex < N - 1) {
			copy = new Board(this.copy());
			swap(copy.tiles, xindex, yindex, xindex, yindex + 1);
			nbrs.enqueue(copy);
		} // move right tile

		if (xindex < N - 1) {
			copy = new Board(this.copy());
			swap(copy.tiles, xindex, yindex, xindex + 1, yindex);
			nbrs.enqueue(copy);
		} // move down tile

		if (yindex > 0) {
			copy = new Board(this.copy());
			swap(copy.tiles, xindex, yindex, xindex, yindex - 1);
			nbrs.enqueue(copy);
		} // tile left tile

		if (xindex > 0) {
			copy = new Board(this.copy());
			swap(copy.tiles, xindex, yindex, xindex - 1, yindex);
			nbrs.enqueue(copy);
		} // move upper tile

		// put all neighbor boards into the queue

		return nbrs;
	}

	// string representation of this board (in the output format specified below)
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tiles[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	// unit tests (DO NOT MODIFY)
	public static void main(String[] args) {
		// read the input file
		Scanner in;
		try {
			in = new Scanner(new File(args[0]), "UTF-8");
		} catch (Exception e) {
			System.out.println("invalid or no input file ");
			return;
		}

		// create initial board from file
		int N = in.nextInt();
		int[][] blocks = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				blocks[i][j] = (int) in.nextInt();
				if (blocks[i][j] >= N * N)
					throw new IllegalArgumentException("value must be < N^2");
				if (blocks[i][j] < 0)
					throw new IllegalArgumentException("value must be >= 0");
			}
		}

		Board initial = new Board(blocks);

		System.out.println("\n=== Initial board ===");
		System.out.println(" - manhattan = " + initial.manhattan());
		System.out.println(initial.toString());

		Board twin = initial.twin();

		System.out.println("\n=== Twin board ===");
		System.out.println(" - manhattan = " + twin.manhattan());
		System.out.println(" - equals = " + initial.equals(twin));
		System.out.println(twin.toString());

		System.out.println("\n=== Neighbors ===");
		for (Board board : initial.neighbors())
			System.out.println(board);
	}
}
