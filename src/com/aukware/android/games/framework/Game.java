package com.aukware.android.games.framework;

public interface Game {
	public Input getInput();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public Audio getAudio();

	public void setScreen(AnimationScreen screen);

	public AnimationScreen getCurrentScreen();

	public AnimationScreen getStartScreen();
}