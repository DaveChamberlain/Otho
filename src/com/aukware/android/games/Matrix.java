package com.aukware.android.games;

import java.util.Random;

import com.aukware.android.games.framework.Graphics;
import com.aukware.android.games.framework.Pixmap;


/*
 * This class manages the grid matrix.
 *  
 */

public class Matrix {
	
	//
	// Size of the grid
	private int size;
	
	//
	// graphic context, used for node spacing calculations
	private Graphics g;
	
	//
	// The grid itself
	private Node[][] grid;
	
	//
	// Number of flips being made
	public int flips;

	// Constructor, just store the graphics context
	public Matrix(Graphics graphics) {
		g = graphics;
	}

	//
	// Getters/Setters for members that need to be available outside
	public int getSize() {
		return size;
	}

	public double getScale() {
		return (double) getSpacing() / (double) Assets.blueSymbol.getWidth();

	}

	//
	// The matrix manager needs to know the spacing of the nodes.  This is done by
	// seeing how many nodes are going to be used and how wide (or tall) the screen
	// is.  For example, a screen of 300 wide by 400 tall with 6 nodes will have spacing
	// every 50 pixels.
	public int getSpacing() {

		// 
		// determine which is smaller, the width or height then divide it by the number
		// of nodes.
		int smaller = g.getWidth() > g.getHeight() ? g.getHeight() : g.getWidth();

		return smaller / getSize();
	}

	//
	// Randomize will flip nodes in the matrix to create the initial board.  This will
	// be done using a card shuffling method. This will make it impossible for the same
	// node to be flipped twice (at least as the target node).
	//
	public void randomize() {
		Random r = new Random();

		int[] numList = new int[size * size];

		//
		// Create a 1D array and place the numbers in it, each number representing one
		// node.
		for (int i = 0; i < size * size; i++)
			numList[i] = i;

		//
		// Loop 1000 times picking two random numbers.  Flip those numbers around (like swapping
		// two cards within a deck).
		for (int i = 0; i < 1000; i++) {
			int r1 = r.nextInt(size * size);
			int r2 = r.nextInt(size * size);

			int temp = numList[r1];
			numList[r1] = numList[r2];
			numList[r2] = temp;
		}

		//
		// Determine the number of nodes to flip. This will be a random number based on the
		// size of the board the player is using.
		if (size <= 5)
			flips = r.nextInt(2) + (size + 1);
		else if (size <= 7)
			flips = r.nextInt(4) + (size + 4);
		else 
			flips = r.nextInt(size) + (size + 8);
		
		//
		// flip that many nodes on the board
		for (int i = 0; i < flips; i++)
			flipNeighbors(new Location (numList[i] / size, numList[i] % size));

	}

	//
	// Set the size of the board.  This will create the initial board and place nodes
	// in it.  This is the only time we care about the way the neighbors will be flipped.
	// In other words, the grid will contain a node based on a specific shape.
	public void setSize(int size) {
		this.size = size;

		grid = new Node[size][size];  // Create the matrix that holds the individual nodes
		
		//
		// Loop twice, the first time creating each node
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				//
				// Create the specific type of node.
				//
				if (Assets.type == Assets.style.PLUS)
					grid[row][col] = new Plus();
				else if (Assets.type == Assets.style.EX)
					grid[row][col] = new Ex();
				else if (Assets.type == Assets.style.ASTERISK)
					grid[row][col] = new Asterisk();
			}
		
		//
		// Initialization can't be completely done until all nodes have been created, so
		// link them together here.
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				//
				// Initialize the location of the node
				grid[row][col].setLocation(row, col);
				
				//
				// The node will then find its neighbors (based on what type of node it is)
				grid[row][col].setNeighbors (row, col, size, grid);
			}

		//
		// After the grid is complete, start randomly flipping nodes.  This will guarantee that
		// the puzzle can be solved.
		randomize();
	}

	//
	// Return the current icon for this spot in the matrix.  The "primary" icon is the one showing,
	// the secondary is the one that will be used when it has been flipped.
	public Pixmap getGridIcon(int row, int col) {
		return grid[row][col].getPrimaryIcon();
	}

	public boolean isGameOver() {
		
		//
		// Loop through the entire grid comparing nodes with the first node.  If there
		// is a difference, then the game isn't over so exit immediately. Otherwise keep
		// looking.  If they're all the same, the game has been won.
		NodeColor.color firstNodeColor = grid[0][0].getColor();
		
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (grid[row][col].getColor() != firstNodeColor)
					return false;
			}

		return true;
	}

	public void flipNeighbors(Location node) {
		// flip the current node
		grid[node.getRow()][node.getCol()].flip();
		
		// also flip the neighbors.  This will flip the neighbors based on the type of
		// shape flipping going on.
		grid[node.getRow()][node.getCol()].flipNeighbors();
	}
}
