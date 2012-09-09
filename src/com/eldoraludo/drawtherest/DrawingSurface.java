package com.eldoraludo.drawtherest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements
		SurfaceHolder.Callback {
	private Boolean _run = false;
	// protected DrawThread thread;
	private Bitmap mBitmap;
	private int stylePen = 0;
	private SurfaceHolder mSurfaceHolder;
	private List<DrawingPath> mDrawingPaths;
	private List<DrawingPoint> mDrawingPoints;
	private Canvas canvas;

	public DrawingSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		this.setBackgroundColor(Color.WHITE);

		mSurfaceHolder = getHolder();
		mDrawingPaths = Collections
				.synchronizedList(new ArrayList<DrawingPath>());
		mDrawingPoints = Collections
				.synchronizedList(new ArrayList<DrawingPoint>());
		// canvas = mSurfaceHolder.lockCanvas();
		canvas = new Canvas();

		mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

		// Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

		canvas.drawBitmap(mBitmap, 0, 0, null);

		this.onDraw(canvas);

	}

	public void addDrawingPath(DrawingPath drawingPath) {
		canvas = mSurfaceHolder.lockCanvas(null);
		// canvas.drawColor(0xFFFFFFFF);ca efface tout
		mDrawingPaths.add(drawingPath);
		// mBitmap.eraseColor(Color.WHITE);
		canvas.drawPath(drawingPath.path, drawingPath.paint);
		// Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());

		canvas.drawBitmap(mBitmap, 0, 0, null);
		this.onDraw(canvas);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	public void addDrawingPoint(DrawingPoint drawingPoint) {
		mDrawingPoints.add(drawingPoint);

	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// thread.setRunning(true);
		// thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// boolean retry = true;
		// thread.setRunning(false);
		// while (retry) {
		// try {
		// thread.join();
		// retry = false;
		// } catch (InterruptedException e) {
		// // we will try it again and again...
		// }
		// }
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

}