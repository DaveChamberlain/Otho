package com.aukware.android.games;

/*
 * This class extends the standard Node class adding shape-specific elements.
 * 
 * The Plus class links neighbors (in the shape of a +) in the following directions:
 *     North
 *     South
 *     South
 *     North
 */

public class Plus extends Node {
	
	public Plus () {
	}

	public void setNeighbors (int row, int col, int size, Node grid[][]) {
		super.setNeighbors (row, col, size, grid);
		
		if (row > 0) // if we're not on the first row, there is a neighbor above us
			grid[row][col].neighbors.add(grid[row-1][col]);
		if (row < size-1) // if we're not on the last row, there is a neighbor below us
			grid[row][col].neighbors.add(grid[row+1][col]);
		if (col < size-1) // if we're not on the last column, there is a neighbor to the right
			grid[row][col].neighbors.add(grid[row][col+1]);
		if (col > 0) // if we're not on the first column, there is a neighbor to the left
			grid[row][col].neighbors.add(grid[row][col-1]);
	}
	
}
