package com.aukware.android.games;

/*
 * This class is used to store the row/column location of any node.
 * 
 */

public class Location {
	
	// Simple class to store the location
	//
	private int row;
	private int col;
	
	public Location () {
	}
	
	public Location (int x, int y) {
		this.row = x;
		this.col = y;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}

}
