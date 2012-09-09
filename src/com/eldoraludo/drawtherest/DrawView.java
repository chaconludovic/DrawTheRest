package com.eldoraludo.drawtherest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
	Paint paint = new Paint();
	DrawingPath drawingPath;
	private Bitmap bitmap;
	private Canvas canvas;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		canvas = new Canvas();
		bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitmap);
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
		// super.onDraw(canvas);
		if (drawingPath != null) {
			canvas.drawPath(drawingPath.path, drawingPath.paint);
		}
		canvas.drawBitmap(bitmap, 0, 0, null);
		c.drawBitmap(bitmap, 0, 0, null);
	}

	public void addDrawingPath(DrawingPath currentDrawingPath) {
		drawingPath = currentDrawingPath;
		this.invalidate();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

}