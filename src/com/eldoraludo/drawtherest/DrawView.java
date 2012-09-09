package com.eldoraludo.drawtherest;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * voir
 * http://stackoverflow.com/questions/2423327/android-view-ondraw-always-has
 * -a-clean-canvas
 * 
 * une autre voie pour le cache serait setDrawingCacheEnabled
 * 
 * @author ludovic
 * 
 */
public class DrawView extends View {
	Paint paint = new Paint();
	DrawingPath drawingPath;
	DrawingPoint drawingPoint;
	private Bitmap bitmap;
	private Canvas canvas;
	private Buffer buffer;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// setDrawingCacheEnabled(true);
		// canvas = new Canvas();
		// bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		// canvas.setBitmap(bitmap);
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (bitmap != null) {
			bitmap.recycle();
		}
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
	}

	@Override
	public void onDraw(Canvas c) {
		if (drawingPath != null) {
			canvas.drawPath(drawingPath.path, drawingPath.paint);
		}
		if (drawingPoint != null) {
			// canvas.drawPoint(drawingPoint.x, drawingPoint.y,
			// drawingPoint.paint);
			int tailleOval = 8;
			canvas.drawOval(new RectF(drawingPoint.x - tailleOval,
					drawingPoint.y - tailleOval, drawingPoint.x + tailleOval,
					drawingPoint.y + tailleOval), drawingPoint.paint);
		}
		canvas.drawBitmap(bitmap, 0, 0, null);
		c.drawBitmap(bitmap, 0, 0, null);
	}

	public void addDrawingPath(DrawingPath currentDrawingPath) {
		this.drawingPath = currentDrawingPath;
		this.invalidate();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void addDrawingPoint(DrawingPoint currentDrawingPoint) {
		this.drawingPoint = currentDrawingPoint;
		this.invalidate();
	}

	public void resetImage() {
		this.destroy();

	}

	public void destroy() {
		if (bitmap != null) {
			bitmap.recycle();
		}
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
		int size = bitmap.getHeight() * bitmap.getRowBytes();
		buffer = ByteBuffer.allocateDirect(size);
		bitmap.copyPixelsToBuffer(buffer);
		drawingPath = null;
		drawingPoint = null;
		this.invalidate();
	}

	public void restaurer() {
		if (bitmap != null) {
			bitmap.recycle();
		}
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer);
		canvas.setBitmap(bitmap);
		drawingPath = null;
		drawingPoint = null;
		this.invalidate();

	}
}