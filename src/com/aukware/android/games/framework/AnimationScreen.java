package com.aukware.android.games.framework;

public abstract class AnimationScreen {
	protected final Game game;

	public AnimationScreen(Game game) {
		this.game = game;
	}

	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
}
