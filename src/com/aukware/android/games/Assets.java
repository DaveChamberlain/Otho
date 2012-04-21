package com.aukware.android.games;

import android.content.Context;

import com.aukware.android.games.framework.Game;
import com.aukware.android.games.framework.Graphics;
import com.aukware.android.games.framework.Graphics.PixmapFormat;
import com.aukware.android.games.framework.Pixmap;
import com.aukware.android.games.framework.Sound;

public class Assets {
	public static Pixmap background;
	public static Pixmap greenSymbol;
	public static Pixmap blueSymbol;

	public static Pixmap recycle;
	public static Pixmap nextBoardType;
	public static Pixmap up;
	public static Pixmap down;

	public static Sound click;
	public static Sound youwin;

	public static boolean isSound = true;
	public static Pixmap sound;
	public static Pixmap nosound;

	public static Pixmap help;
	public static Pixmap helpTxt;
	
	public static Context mainContext;

	enum style {
		PLUS, EX, ASTERISK
	};

	public static style type = style.PLUS;

	public static int level = 1;

	
	public static void init(Game game, style t) {
		Graphics g = game.getGraphics();

		type = t;

		background = g.newPixmap("background.png", PixmapFormat.RGB565);
		recycle = g.newPixmap("recycle.png", PixmapFormat.RGB565);
		down = g.newPixmap("down.png", PixmapFormat.RGB565);
		up = g.newPixmap("up.png", PixmapFormat.RGB565);
		help = g.newPixmap("help.png", PixmapFormat.RGB565);
		helpTxt = g.newPixmap("helptxt.png", PixmapFormat.RGB565);

		sound = g.newPixmap("sound.png", PixmapFormat.RGB565);
		nosound = g.newPixmap("nosound.png", PixmapFormat.RGB565);

		if (type == style.PLUS) {
			nextBoardType = g.newPixmap("ex.png", PixmapFormat.RGB565);
			greenSymbol = g.newPixmap("plus-ball-blue.png", PixmapFormat.RGB565);
			blueSymbol = g.newPixmap("plus-ball-green.png", PixmapFormat.RGB565);
		} else if (type == style.EX) {
			nextBoardType = g.newPixmap("astrix.png", PixmapFormat.RGB565);
			greenSymbol = g.newPixmap("ex-ball-green.png", PixmapFormat.RGB565);
			blueSymbol = g.newPixmap("ex-ball-blue.png", PixmapFormat.RGB565);
		} else if (type == style.ASTERISK) {
			nextBoardType = g.newPixmap("plus.png", PixmapFormat.RGB565);
			greenSymbol = g.newPixmap("astrix-red.png", PixmapFormat.RGB565);
			blueSymbol = g.newPixmap("astrix-blue.png", PixmapFormat.RGB565);
		}
		click = game.getAudio().newSound("click.ogg");
		youwin = game.getAudio().newSound("youwin.ogg");

	}
}
