package com.aukware.android.games;

import java.util.List;

import com.aukware.android.games.framework.Game;
import com.aukware.android.games.framework.Graphics;
import com.aukware.android.games.framework.Input.TouchEvent;
import com.aukware.android.games.framework.AnimationScreen;

public class HelpScreen extends AnimationScreen {
	String lines[] = new String[5];

	public HelpScreen(Game game) {
		super(game);

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (Assets.isSound)
					Assets.click.play(1);
				game.setScreen(new MainScreen(game));
				return;
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.helpTxt, 0, 0);

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
