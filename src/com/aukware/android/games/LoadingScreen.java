package com.aukware.android.games;

import com.aukware.android.games.framework.Game;
import com.aukware.android.games.framework.AnimationScreen;

public class LoadingScreen extends AnimationScreen {
	public LoadingScreen(Game game) {
		super(game);
		
		Assets.init(game, Assets.style.PLUS);
	}

	@Override
	public void update(float deltaTime) {
		// Settings.load(game.getFileIO());
		game.setScreen(new MainScreen(game));
	}

	@Override
	public void present(float deltaTime) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}