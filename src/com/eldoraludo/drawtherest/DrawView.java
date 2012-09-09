package com.eldoraludo.drawtherest;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
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

	private static final String ETAT_DRAWING = "DRAWING";
	private static final String ETAT_DRAW_LINE = "ETAT_DRAW_LINE";
	private static final String ETAT_RESTAURATION_BITMAP = "RESTAURATION_BITMAP";
	private String etat = ETAT_DRAW_LINE;

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
		if (etat.equals(ETAT_DRAWING)) {
			if (drawingPath != null) {
				canvas.drawPath(drawingPath.path, drawingPath.paint);
			}
			if (drawingPoint != null) {
				// canvas.drawPoint(drawingPoint.x, drawingPoint.y,
				// drawingPoint.paint);
				int tailleOval = 8;
				canvas.drawOval(new RectF(drawingPoint.x - tailleOval,
						drawingPoint.y - tailleOval, drawingPoint.x
								+ tailleOval, drawingPoint.y + tailleOval),
						drawingPoint.paint);
			}
			canvas.drawBitmap(this.bitmap, 0, 0, null);
			c.drawBitmap(this.bitmap, 0, 0, null);
		} else if (etat.equals(ETAT_RESTAURATION_BITMAP)) {
			canvas.drawBitmap(this.bitmap, 0, 0, null);
			c.drawBitmap(this.bitmap, 0, 0, null);
			this.etat = ETAT_DRAWING;
		} else if (etat.equals(ETAT_DRAW_LINE)) {
			Path path = new Path();
			path.moveTo(0, 3 * getMeasuredHeight() / 4);
			path.lineTo(getMeasuredWidth(), 3 * getMeasuredHeight() / 4);
			Paint paint = new Paint();
			paint.setDither(true);
			paint.setColor(Color.YELLOW);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(5);
			canvas.drawPath(path, paint);
			c.drawBitmap(this.bitmap, 0, 0, null);
			this.etat = ETAT_DRAWING;
		}
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
		this.etat = ETAT_DRAW_LINE;
		this.invalidate();
	}

	public void restaurer() {
		if (this.bitmap != null) {
			this.bitmap.recycle();
		}
		canvas = new Canvas();
		this.bitmap = Bitmap.createBitmap(getMeasuredWidth(),
				getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		this.bitmap.copyPixelsFromBuffer(buffer);
		canvas.setBitmap(this.bitmap);
		drawingPath = null;
		drawingPoint = null;
		this.invalidate();

	}

	public void restaurer(Bitmap bitmapToRestaure) {
		// // draw the preserved image, scaling it to a thumbnail first
		// Bitmap createScaledBitmap =
		// Bitmap.createScaledBitmap(bitmapToRestaure,
		// getMeasuredWidth(), getMeasuredHeight(), true);
		this.etat = ETAT_RESTAURATION_BITMAP;
		// if (this.bitmap != null) {
		// this.bitmap.recycle();
		// }
		// this.bitmap = Bitmap.createBitmap(bitmapToRestaure, 0, 0,
		// bitmapToRestaure.getWidth(), bitmapToRestaure.getHeight(),
		// null, true);
		this.bitmap = Bitmap
				.createScaledBitmap(bitmapToRestaure,
						bitmapToRestaure.getWidth(),
						bitmapToRestaure.getHeight(), true);

		Log.i("**************** azerty **************",
				String.valueOf(this.bitmap.isMutable()));
		// TODO PROBLEME ON NE PEUT PLUS MODIFIER APRES LE DESSIN
		this.invalidate();
	}
}