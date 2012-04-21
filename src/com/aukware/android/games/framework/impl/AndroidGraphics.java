package com.aukware.android.games.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import com.aukware.android.games.framework.Graphics;
import com.aukware.android.games.framework.Pixmap;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	// @Override
	public Pixmap newPixmap(String fileName, PixmapFormat format) {
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		if (bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;

		return new AndroidPixmap(bitmap, format);
	}

	// @Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	// @Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	// @Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	// @Override
		public void drawRect(int x, int y, int width, int height, int color) {
			paint.setColor(color);
			paint.setStyle(Style.FILL);
			canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
		}
		
	// @Override
		public void drawCircle (int x, int y, int radius, int color) {
			paint.setColor(color);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(5);
			canvas.drawCircle(x, y, radius, paint);
		}

	// @Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;

		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
				null);
	}

	// @Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
	}

	// @Override
	public void drawPixmap(Pixmap pixmap, double scale, int x, int y) {
		srcRect.left = 0;
		srcRect.top = 0;
		srcRect.right = pixmap.getWidth();
		srcRect.bottom = pixmap.getHeight();
		;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + (int) (pixmap.getWidth() * scale + 0.5);
		dstRect.bottom = y + (int) (pixmap.getWidth() * scale + 0.5);

		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
				null);
	}

	// @Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	// @Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}

	// @Override
	public void drawText(String text, int x, int y, int size, int color) {
		String[] lines = text.split("\n");
		int yOffset = 0;
		Rect bounds = new Rect();
		
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(size);
		for (int i = 0; i < lines.length; i++) {
			canvas.drawText(lines[i], x, y + yOffset, paint);
			paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
			yOffset += bounds.height() + (bounds.height() / 4);
		}
	}
	public void drawCenteredText(String text, int x, int y, int size, int color) {
		String[] lines = text.split("\n");
		int yOffset = 0;
		Rect bounds = new Rect();
		
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(size);
		for (int i = 0; i < lines.length; i++) {
			paint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
			canvas.drawText(lines[i], x - (bounds.width()/2), y + yOffset, paint);
			yOffset += bounds.height() + (bounds.height() / 4);
		}
	}
}
