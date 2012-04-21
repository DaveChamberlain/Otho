package com.aukware.android.games;

/*
 * This class extends the standard Node class adding shape-specific elements.
 * 
 * The Asterisk class links neighbors in the following directions:
 *     North
 *     North-East
 *     East
 *     South-East
 *     South
 *     South-West
 *     West
 *     North-West
 */

public class Asterisk extends Node {
	public Asterisk () {
	}

	public void setNeighbors (int row, int col, int size, Node grid[][]) {
		super.setNeighbors (row, col, size, grid);
		
		//
		// First flip the angle neighbors (as with the ex)
		//
		if (row > 0) { // if we're not on the top row
			if (col > 0) // if we're not in the left column (there is one to the top/left of us
				grid[row][col].neighbors.add(grid[row - 1][col - 1]);
			if (col < size - 1) // likewise, if we're not in the last column, there's one to the top right
				grid[row][col].neighbors.add(grid[row - 1][col + 1]);
		}
		if (row < size - 1) { // if we're not on the last row
			if (col > 0) // there might be one below and to the left
				grid[row][col].neighbors.add(grid[row + 1][col - 1]);
			if (col < size - 1) // and to the bottom right
				grid[row][col].neighbors.add(grid[row + 1][col + 1]);
		}
		//
		// Then flip the horizontal/vertical neighbors (as with the plus)
		//
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
