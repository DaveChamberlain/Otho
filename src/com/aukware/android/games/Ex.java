package com.aukware.android.games;

/*
 * This class extends the standard Node class adding shape-specific elements.
 * 
 * The Ex class links neighbors (in the shape of an X) in the following directions:
 *     North-East
 *     South-East
 *     South-West
 *     North-West
 */

public class Ex extends Node {
	public Ex () {
	}

	public void setNeighbors (int row, int col, int size, Node grid[][]) {
		super.setNeighbors (row, col, size, grid);
		
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
	}
}
