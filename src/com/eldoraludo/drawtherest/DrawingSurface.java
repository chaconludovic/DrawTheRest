package com.eldoraludo.drawtherest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements
		SurfaceHolder.Callback {
	private Boolean _run;
	protected DrawThread thread;

	public DrawingSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		thread = new DrawThread(getHolder(), this);
	}

	public void addDrawingPath(DrawingPath drawingPath) {
		thread.addDrawingPath(drawingPath);
	}

	class DrawThread extends Thread {
		private DrawingSurface drawingSurface;
		private SurfaceHolder mSurfaceHolder;
		private List mDrawingPaths;

		public DrawThread(SurfaceHolder surfaceHolder,
				DrawingSurface drawingSurface_) {
			mSurfaceHolder = surfaceHolder;
			drawingSurface = drawingSurface_;
			mDrawingPaths = Collections.synchronizedList(new ArrayList());
		}

		public void addDrawingPath(DrawingPath drawingPath) {
			mDrawingPaths.add(drawingPath);
		}

		public void setRunning(boolean run) {
			_run = run;
		}

		@Override
		public void run() {
			Canvas canvas = null;
			while (_run) {
				try {
					canvas = mSurfaceHolder.lockCanvas(null);
					synchronized (mDrawingPaths) {
						Iterator i = mDrawingPaths.iterator();
						while (i.hasNext()) {
							final DrawingPath drawingPath = (DrawingPath) i
									.next();
							canvas.drawPath(drawingPath.path, drawingPath.paint);
						}
						drawingSurface.onDraw(canvas);
					}
				} finally {
					mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

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