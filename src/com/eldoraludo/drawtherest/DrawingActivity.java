package com.eldoraludo.drawtherest;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

/**
 * http://www.tutorialforandroid.com/2010/11/drawing-with-canvas-in-android-
 * renewed.html
 * 
 * @author ludovic
 * 
 */
public class DrawingActivity extends Activity implements OnTouchListener {
	private DrawingSurface drawingSurface;
	private DrawingPath currentDrawingPath;
	private Paint currentPaint;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_the_rest);
		setCurrentPaint();
		drawingSurface = (DrawingSurface) findViewById(R.id.drawingSurface);
		drawingSurface.setOnTouchListener(this);

		final Button colorBlueBtn = (Button) findViewById(R.id.colorBlueBtn);
		colorBlueBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				changementCouleur(colorBlueBtn);
			}
		});
		final Button colorGreenBtn = (Button) findViewById(R.id.colorGreenBtn);
		colorBlueBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				changementCouleur(colorGreenBtn);
			}
		});
		final Button colorRedBtn = (Button) findViewById(R.id.colorRedBtn);
		colorRedBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				changementCouleur(colorRedBtn);
			}
		});
	}

	private void setCurrentPaint() {
		currentPaint = new Paint();
		currentPaint.setDither(true);
		currentPaint.setColor(0xFFFFFF00);
		currentPaint.setStyle(Paint.Style.STROKE);
		currentPaint.setStrokeJoin(Paint.Join.ROUND);
		currentPaint.setStrokeCap(Paint.Cap.ROUND);
		currentPaint.setStrokeWidth(3);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent motionEvent) {

		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			currentDrawingPath = new DrawingPath();
			currentDrawingPath.paint = currentPaint;
			currentDrawingPath.path = new Path();
			currentDrawingPath.path.moveTo(motionEvent.getX(),
					motionEvent.getY());
			currentDrawingPath.path.lineTo(motionEvent.getX(),
					motionEvent.getY());
			Log.i("DrawingActivity - onTouch ACTION_DOWN", motionEvent.getX()
					+ " " + motionEvent.getY());
		} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			currentDrawingPath.path.lineTo(motionEvent.getX(),
					motionEvent.getY());
			Log.i("DrawingActivity - onTouch ACTION_MOVE", motionEvent.getX()
					+ " " + motionEvent.getY());
		} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			currentDrawingPath.path.lineTo(motionEvent.getX(),
					motionEvent.getY());
			drawingSurface.addDrawingPath(currentDrawingPath);
			Log.i("DrawingActivity - onTouch ACTION_UP", motionEvent.getX()
					+ " " + motionEvent.getY());
		}
		return true;
	}

	public void changementCouleur(View view) {
		Log.i("DrawingActivity - onClick", "click");
		switch (view.getId()) {
		case R.id.colorRedBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(0xFFFF0000);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Red");
			break;
		case R.id.colorBlueBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(0xFF00FF00);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Blue");
			break;
		case R.id.colorGreenBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(0xFF0000FF);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Green");
			break;
		}
	}

}