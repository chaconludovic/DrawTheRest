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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawSurfaceWithRefresh extends SurfaceView implements
		SurfaceHolder.Callback {
	private Boolean _run = false;
	protected DrawThread thread;
	private Bitmap mBitmap;
	private int stylePen = 0;

	public DrawSurfaceWithRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		thread = new DrawThread(getHolder(), this);
	}

	public void addDrawingPath(DrawingPath drawingPath) {
		thread.setActualiser();
		thread.addDrawingPath(drawingPath);

	}

	public void addDrawingPoint(DrawingPoint drawingPoint) {
		thread.setActualiser();
		thread.addDrawingPoint(drawingPoint);

	}

	public void resetImage() {
		thread.reset();
	}

	// public void addDrawing(DrawingPath drawingPath) {
	// thread.addDrawing(drawingPath);
	// }

	class DrawThread extends Thread {
		private DrawSurfaceWithRefresh drawingSurface;
		private SurfaceHolder mSurfaceHolder;
		private List<DrawingPath> mDrawingPaths;
		private List<DrawingPoint> mDrawingPoints;
		private Canvas canvas;
		private boolean actualiser = false;

		public DrawThread(SurfaceHolder surfaceHolder,
				DrawSurfaceWithRefresh drawingSurface_) {
			mSurfaceHolder = surfaceHolder;
			drawingSurface = drawingSurface_;
			mDrawingPaths = Collections
					.synchronizedList(new ArrayList<DrawingPath>());
			mDrawingPoints = Collections
					.synchronizedList(new ArrayList<DrawingPoint>());
			canvas = mSurfaceHolder.lockCanvas();
			drawingSurface.onDraw(canvas);
		}

		public void setActualiser() {
			actualiser = true;

		}

		public void addDrawingPath(DrawingPath drawingPath) {
			mDrawingPaths.add(drawingPath);
		}

		public void addDrawingPoint(DrawingPoint drawingPoint) {
			mDrawingPoints.add(drawingPoint);

		}

		public void reset() {
			mDrawingPaths = Collections
					.synchronizedList(new ArrayList<DrawingPath>());
			mDrawingPoints = Collections
					.synchronizedList(new ArrayList<DrawingPoint>());
		}

		// public void addDrawing(DrawingPath drawingPath) {
		// canvas.drawPoint(drawingPath.x, drawingPath.y, drawingPath.paint);
		// }

		public void setRunning(boolean run) {
			_run = run;
		}

		@Override
		public void run() {
			if (actualiser) {
				Canvas canvas = null;
			}
			while (_run) {
				if (actualiser) {
					try {
						canvas = mSurfaceHolder.lockCanvas(null);
						if (mBitmap == null) {
							mBitmap = Bitmap.createBitmap(1, 1,
									Bitmap.Config.ARGB_8888);

						}
						canvas.drawColor(Color.WHITE);
						synchronized (mDrawingPaths) {
							Iterator i = mDrawingPaths.iterator();

							while (i.hasNext()) {
								final DrawingPath drawingPath = (DrawingPath) i
										.next();

								canvas.drawPath(drawingPath.path,
										drawingPath.paint);

							}
							drawingSurface.onDraw(canvas);

							canvas.drawBitmap(mBitmap, 0, 0, null);
						}

						synchronized (mDrawingPoints) {
							Iterator i = mDrawingPoints.iterator();

							while (i.hasNext()) {
								final DrawingPoint drawingPoint = (DrawingPoint) i
										.next();
								// canvas.drawPoint(drawingPoint.x,
								// drawingPoint.y,
								// drawingPoint.paint);
								int tailleOval = 5;
								canvas.drawOval(new RectF(drawingPoint.x
										- tailleOval, drawingPoint.y
										- tailleOval, drawingPoint.x
										+ tailleOval, drawingPoint.y
										+ tailleOval), drawingPoint.paint);
							}
							drawingSurface.onDraw(canvas);

							canvas.drawBitmap(mBitmap, 0, 0, null);
						}
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
						actualiser = false;
					}
				}
			}

		}
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
		thread.setRunning(true);
		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}

	}

}