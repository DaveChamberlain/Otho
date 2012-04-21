package com.aukware.android.games.framework.impl;


import com.aukware.android.games.framework.Sound;

import android.media.SoundPool;

public class AndroidSound implements Sound {
	int soundId;
	SoundPool soundPool;

	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	// @Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}

	// @Override
	public void dispose() {
		soundPool.unload(soundId);
	}

}
