package com.aukware.android.games;

/*
 * This is the main class for this game.  It extends AnimationScreen, which is a class which
 * expects to receive updates through a "wakeup" routine. This method (update) will be called
 * to process queued events, update characters on a screen, and update status of any object
 * being manipulated or monitored.
 * 
 * Similarly, the method "present" will be called to present the new screen to the user. There
 * is no predetermined interval for either of these methods to be called, each will receive the 
 * delta time since the last call and may use that to decide what to do.  For example, flashing an
 * icon that is cycled in 0.10 second intervals will need to check and accumulate time until the
 * desired 'flash' interval has been reached.
 * 
 * Other methods (currently unused) are also available (pause, resume, etc.)
 * 
 */

import java.util.List;

import com.aukware.android.games.framework.Game;
import com.aukware.android.games.framework.Graphics;
import com.aukware.android.games.framework.Input.TouchEvent;
import com.aukware.android.games.framework.AnimationScreen;

public class MainScreen extends AnimationScreen {
	
	//
	// The matrix that contains the grid being used.
	Matrix matrix;
	
	//
	// The number of moves the user has made.
	int moves = 0;
	
	//
	// The last node the user clicked on.  This is used to "flash" the nodes
	Location lastNode;
	
	//
	// A status variable indicating the number of times to flash icons.
	int flipping = 0;
	
	//
	// The total delta time for the item being flipped.  This should be visible
	// for both the update and present methods.
	float flippingDelta = (float) 0.0;
	
	//
	// Whether the current game is complete or not (with a win).  
	boolean gamesWon = false;
	
	//
	// The user's score
	int score = 0;

	//
	// should the update method need to stop updating elements, this is the amount of time
	// that should elapse before it starts updating again.
	float delaytime = (float) 0.0;

	//
	// Constructor.  Initializes the class
	public MainScreen(Game game) {
		super(game);
		Graphics g = game.getGraphics();

		matrix = new Matrix(g);
		matrix.setSize(Assets.level + 3); // (level starts at 1, the min board size is 4)
	}
	
	//
	// If the user wants to replay the level (or they didn't finish in the number of moves)
	private void replay () {
		//
		// reset the moves, recreate the matrix, deduct points from the score and make sure the
		// world knows they didn't win :-)
		moves = 0;
		matrix.setSize(matrix.getSize());
		score -= 50;
		gamesWon = false;
	}
	
	private void calculateScore () {
		/*  Scoring...
		 * Multiple things can adjust the score.  The person receives 25 points * the level (minimum of 100 points)
		 * for completing the puzzle.  The current level is stores in the assets as a range starting at 1, so add
		 * 3 onto it then multiply by 25.
		 * 
		 * You can also get points if the puzzle was completed in fewer moves than expected.  If that is the case,
		 * the points are 100 points for each move less than the anticipated amount.
		 * 
		 * If you get it exactly, you earn an additional 50 points. This is there so that the user can make more moves
		 * than the expected amount.
		 * 
		 * If the user is able to make more moves than the expected amount, deduct 10 points for each additional move.
		 */
		score += 25 * (Assets.level + 3);
		if (moves < matrix.flips)
			score += 100 * (matrix.flips - moves);
		if (moves == matrix.flips)
			score += 50;
		if (moves > matrix.flips)
			score -= (moves - matrix.flips) * 10;		
	}
	
	private void gameEnded () {
		//
		// Calculate the score, play the "winning" sound, and increase the level.
		//
		calculateScore ();
		if (Assets.isSound)
			Assets.youwin.play(1);
		if (Assets.level < 17)
			Assets.level++;
		
		gamesWon = true;
		
		//
		// Request a delay of 3 seconds before showing the new board.
		//
		delaytime = (float) 3.0;
	}

	private void clickedOnNode (TouchEvent event) {
		//
		// A user has clicked on a node.  So... record the node and flip it.
		//
		lastNode = new Location (event.x / matrix.getSpacing(), event.y / matrix.getSpacing());
		matrix.flipNeighbors(lastNode);
		
		//
		// increment the number of moves
		//
		moves++;
		
		//
		// To make the nodes being flipped "flash", set a counter for the number of times to
		// automatically flip them and initialize the delta between flips (it will flash at 0.1 second
		// intervals.
		//
		flipping = 10;
		flippingDelta = (float) 0.0;
		
		//
		// If the game is over, tally things up.  Otherwise, play the "click" sound
		//
		if (matrix.isGameOver()) {
			gameEnded ();
		} else if (Assets.isSound)
			Assets.click.play(1);

	}
	
	private void changeNeighborPattern () {
		/*
		 * There are currently three patterns that neighbors are flipped in.  A PLUS an EX (x) and
		 * an overlay of the X and a + which I'm calling the asterisks.  There is an icon on the
		 * screen that lets the user choose the pattern, it will cycle through with each click. Every
		 * time the pattern is changed, the board must be recreated to make sure it is solvable.
		 */
		if (Assets.type == Assets.style.PLUS)
			Assets.init(game, Assets.style.EX);
		else if (Assets.type == Assets.style.EX)
			Assets.init(game, Assets.style.ASTERISK);
		else if (Assets.type == Assets.style.ASTERISK)
			Assets.init(game, Assets.style.PLUS);
		matrix.setSize(matrix.getSize());

		//
		// If the user is switching patterns during a game, deduct the 50 points (like the ones they would
		// lose during a reset of the board) and set the number of moves back to 0.
		if (moves > 0)
			score -= 50;
		
		moves = 0;
	}
	
	private void toggleSound () {
		Assets.isSound = !Assets.isSound;	
	}
	
	private void showHelp () {
		game.setScreen(new HelpScreen(game));
	}
		
	private boolean inBounds(TouchEvent event, int x, int y, int width,	int height) {
		/*
		 * Check to see if the event click was within a specified bounding box.  This is a simple
		 * true/false, either it is or it isn't.
		 */
		if ((event.x > x && event.x < x + width - 1) // check that the event's x is within the x bounds
				&& (event.y > y	&& event.y < y + height - 1)) //also check the event's y
			return true;
		else
			return false;
	}


	@Override
	public void update(float deltaTime) {
		/*
		 * This function is called whenever an event occurs. The variable "deltatime" shows the amount of
		 * time since the last time it was called.
		 */
		
		//
		// g is the graphics context.  It contains information about the screen
		//
		Graphics g = game.getGraphics();
		
		//
		// The list of events to process
		//
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		//game.getInput().getKeyEvents();

		// 
		// Loop through teach of the events and process it by checking to see where on the screen
		// the "click" (or touch) occurred and calling the corresponding function
		//
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				
				//
				// If there are too many moves, and the user wishes to replay the level, they will
				// have touched someplace (anyplace) on the screen. In that case, call the "replay" 
				// function and stop processing anything here.
				//
				if (moves >= matrix.flips){
					replay ();
				} 
				
				// 
				// otherwise, if the user clicked in the area of the matrix, process it as though they
				// clicked on a node.
				else if (inBounds(event, 0, 0,
						matrix.getSpacing() * matrix.getSize(),
						matrix.getSpacing() * matrix.getSize())) {
					clickedOnNode (event);
				} 
				
				//
				// If they touched the icon to change the pattern used, process that accordingly
				//
				else if (inBounds(event, g.getWidth() - 160, g.getHeight() - 80, 80, 80)) {
					changeNeighborPattern ();
				} 
				
				//
				// They may have just given up on this level and reset it
				//
				else if (inBounds(event, g.getWidth() - 80, g.getHeight() - 80, 80, 80)) {
					replay ();
				} 
				
				//
				// Toggle the sound if necessary
				//
				else if (inBounds(event, g.getWidth() - 80, g.getHeight() - 160, 80, 80)) {
					toggleSound ();
				} 
				
				//
				// Show the help screen
				//
				else if (inBounds(event, g.getWidth() - 160, g.getHeight() - 160, 80, 80)) {
					showHelp ();
				} 
			}
		}
	}
	
	private void flashLastNode (float deltaTime) {
		/*
		 * Flash the last node clicked and its neighbors.  Do this by checking that it isn't flipping
		 * too quickly.  Use the deltatime since the last flash, if it is more than 1/10 of a 
		 * second, then allow it to flash by re-flipping the node (and neighbors).
		 */
		flippingDelta += deltaTime;
		if (flippingDelta >= (float) 0.1) {
			flipping--;
			matrix.flipNeighbors(lastNode);
			flippingDelta = flippingDelta - (float) 0.1;
		}
	
	}

	private void drawScreen (float deltaTime) {
		/*
		 * Handle everything related to drawing the screen with the board. This includes the drawing
		 * of the matrix, control icons, score updates, and flashing nodes.
		 */
		
		//
		// Save the graphics context
		//
		Graphics g = game.getGraphics();

		//
		// Draw the background and place the nodes (with the correct color) on the screen
		//
		g.drawPixmap(Assets.background, 0, 0);
		for (int x = 0; x < matrix.getSize(); x++)
			for (int y = 0; y < matrix.getSize(); y++) {
				//
				// draw the node's icon. Scale it and place it on the screen. The "spacing" determined how far apart
				// each node's icon is placed
				//
				g.drawPixmap(matrix.getGridIcon(x,y), matrix.getScale(), x * matrix.getSpacing(), y * matrix.getSpacing());
			}
		
		//
		// Draw the icons (sound, recycle/replay, change pattern, and help
		//
		if (Assets.isSound)
			g.drawPixmap(Assets.sound, g.getWidth() - 80, g.getHeight() - 160);
		else
			g.drawPixmap(Assets.nosound, g.getWidth() - 80, g.getHeight() - 160);

		g.drawPixmap(Assets.recycle, g.getWidth() - 80, g.getHeight() - 80);
		g.drawPixmap(Assets.nextBoardType, g.getWidth() - 160, g.getHeight() - 80);
		g.drawPixmap(Assets.help, g.getWidth() - 160, g.getHeight() - 160);

		//
		// display the number of moves and anticipated amount, score, and level.
		//
		g.drawText("Moves: " + String.valueOf(moves) + "/" + String.valueOf(matrix.flips), g.getWidth() - 320,
				g.getHeight() - 120, 24, 0xFF000000);
		g.drawText("Score: " + String.valueOf(score), g.getWidth() - 320, g.getHeight() - 90, 24, 0xFF000000);
		g.drawText("Level: " + String.valueOf(Assets.level), g.getWidth() - 320, g.getHeight() - 60, 24, 0xFF000000);		
	}
	
	@Override
	public void present(float deltaTime) {
		/*
		 * This method is called by the system to update the screen.  "deltatime" is the elapsed time since the
		 * last time this was called.
		 */
		
		// 
		// save the graphics context
		Graphics g = game.getGraphics();

		//
		// If a node is being flipped (there are usually 10 flips), then flash it and its neighbors
		if (flipping > 0) {
			flashLastNode (deltaTime);
		}
		
		//
		// draw the new screen, including all icons
		drawScreen (deltaTime);
		
		//
		// determine if there have been too many moves or if the game is over.  If it is, place the appropriate text
	    if (gamesWon) {
	    	g.drawCenteredText("You Won!", g.getWidth() / 2, g.getHeight()/3, 60, 0xFFFF0000);
	    } else if (moves >= matrix.flips) {
	    	g.drawCenteredText("GAME\nOVER", g.getWidth() / 2, g.getHeight()/4, 60, 0xFFFF0000);
	    	g.drawCenteredText("(touch screen\nto replay level)", g.getWidth() / 2, g.getHeight()/2, 40, 0xFFFF0000);
	    }
	
	    //
	    // If a delay was requested, deduct the amount of time between screen refreshes, if there isn't a delay, check
	    // to see if the game is over and change the level.
		if (delaytime > (float) 0.0) {
			delaytime -= deltaTime;
		} else if (matrix.isGameOver()) {
			moves = 0;
			matrix.setSize(Assets.level + 3);
			gamesWon = false;
		}
		
		g.drawCenteredText("Copyright \u00A9 2012 Dave Chamberlain", g.getWidth()/2, g.getHeight() - 7, 10, 0xFF000000);

	}

	@Override
	public void pause() {
		// Settings.save(game.getFileIO());
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
