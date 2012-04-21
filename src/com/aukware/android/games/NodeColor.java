package com.aukware.android.games;

//
// This class maintains the color of the current node.
//
public class NodeColor {
	public enum color {BLUE, GREEN};
	
	private color sideUp;

	public NodeColor (color sideUp) {
		this.sideUp = sideUp;
	}
	
	public color getSideUp() {
		return sideUp;
	}

	public void setSideUp(color sideUp) {
		this.sideUp = sideUp;
	}

	public void flip () {
		sideUp = (sideUp == color.BLUE) ? color.GREEN : color.BLUE; 
	}
}
