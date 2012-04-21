package com.aukware.android.games;

import java.util.ArrayList;

import com.aukware.android.games.framework.Pixmap;

public class Node {
	//
	// Class to maintain each node on the screen.
	//
	// Each node has a location and graphic to show which side is up.  Which side
	// is currently up is (for the moment) only Blue or Green
	//
	private Location location;
	private Pixmap primaryIcon;
	private Pixmap secondaryIcon;
	private NodeColor color;
	
	//
	// The list of neighbors must be visible here as well as in any derived classes
	protected ArrayList<Node> neighbors;
	
	//
	// Constructor, initializes the location, sets the node to "blue" by default.
	public Node () {
		location = new Location ();
		
		setPrimaryIcon(Assets.blueSymbol);
		setSecondaryIcon(Assets.greenSymbol);
	
		color = new NodeColor(NodeColor.color.BLUE);
	}
	
	//
	// Flip this node.
	public void flip () {
		Pixmap temp = primaryIcon;
		primaryIcon = secondaryIcon;
		secondaryIcon = temp;
		
		color.flip();
	}

	//
	// Loop through each neighbor and flip it.
	public void flipNeighbors () {
		for (int x = 0; x < neighbors.size(); x++) {
			neighbors.get(x).flip();
		}
	}

	//
	// The Setters and Getters for various members that may need to be accessed from outside
	public Location getLocation() {
		return location;
	}

	public void setLocation(int row, int col) {
		this.location.setRow(row);
		this.location.setCol(col);
	}

	public NodeColor.color getColor() {
		return color.getSideUp();
	}

	public void setColor(NodeColor color) {
		this.color = color;
	}

	public Pixmap getPrimaryIcon() {
		return primaryIcon;
	}
	
	public Pixmap getSecondaryIcon() {
		return secondaryIcon;
	}

	public void setPrimaryIcon(Pixmap icon) {
		this.primaryIcon = icon;
	}
	
	public void setSecondaryIcon(Pixmap icon) {
		this.secondaryIcon = icon;
	}
	
	public void setNeighbors (int row, int col, int size, Node grid[][]) {
		neighbors = new ArrayList<Node>();
	}
	

}
