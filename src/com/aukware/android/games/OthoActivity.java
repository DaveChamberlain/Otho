package com.aukware.android.games;

import com.aukware.android.games.framework.AnimationScreen;
import com.aukware.android.games.framework.impl.AndroidGame;

public class OthoActivity extends AndroidGame {
	public AnimationScreen getStartScreen() {

		Assets.mainContext = this.getApplicationContext();
		return new LoadingScreen(this);
	}
}
