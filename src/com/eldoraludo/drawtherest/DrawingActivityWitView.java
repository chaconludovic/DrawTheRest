package com.eldoraludo.drawtherest;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class DrawingActivityWitView extends Activity implements OnTouchListener {
	private DrawView drawView;
	private DrawingPath currentDrawingPath;
	private DrawingPoint currentDrawingPoint;
	private Paint currentPaint;
	private static File APP_FILE_PATH = new File("/sdcard/external_sd/Temp");
	private int stylePen = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_the_rest_with_view);
		setCurrentPaint();
		drawView = (DrawView) findViewById(R.id.drawView);

		drawView.setOnTouchListener(this);

		final Button colorBlueBtn = (Button) findViewById(R.id.colorBlueBtn);
		colorBlueBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorBlueBtn);
			}
		});
		final Button colorGreenBtn = (Button) findViewById(R.id.colorGreenBtn);
		colorGreenBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorGreenBtn);
			}
		});
		final Button colorRedBtn = (Button) findViewById(R.id.colorRedBtn);
		colorRedBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(colorRedBtn);
			}
		});
		final Button envoyer = (Button) findViewById(R.id.envoyer);
		envoyer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(envoyer);
			}
		});
		final Button boutonNouvellePartie = (Button) findViewById(R.id.boutonNouvellePartie);
		boutonNouvellePartie.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(boutonNouvellePartie);
			}
		});
		final Button swithchStylePen = (Button) findViewById(R.id.swithchStylePen);
		swithchStylePen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				actionButton(swithchStylePen);
			}
		});

	}

	private void setCurrentPaint() {
		currentPaint = new Paint();
		currentPaint.setDither(true);
		currentPaint.setColor(Color.GREEN);
		currentPaint.setStyle(Paint.Style.STROKE);
		currentPaint.setStrokeJoin(Paint.Join.ROUND);
		currentPaint.setStrokeCap(Paint.Cap.ROUND);
		currentPaint.setStrokeWidth(5);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent motionEvent) {
		if (stylePen == 0) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				currentDrawingPath = new DrawingPath();
				currentDrawingPath.paint = currentPaint;
				currentDrawingPath.path = new Path();
				currentDrawingPath.path.moveTo(motionEvent.getX(),
						motionEvent.getY());
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				Log.i("DrawingActivity - onTouch ACTION_DOWN",
						motionEvent.getX() + " " + motionEvent.getY());
			} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				drawView.addDrawingPath(currentDrawingPath);
				Log.i("DrawingActivity - onTouch ACTION_MOVE",
						motionEvent.getX() + " " + motionEvent.getY());
			} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				currentDrawingPath.path.lineTo(motionEvent.getX(),
						motionEvent.getY());
				drawView.addDrawingPath(currentDrawingPath);
				Log.i("DrawingActivity - onTouch ACTION_UP", motionEvent.getX()
						+ " " + motionEvent.getY());
			}
		} else {
			if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				currentDrawingPoint = new DrawingPoint();
				Paint qc = new Paint(currentPaint);
				qc.setStyle(Style.FILL);
				currentDrawingPoint.paint = qc;
				currentDrawingPoint.x = motionEvent.getX();
				currentDrawingPoint.y = motionEvent.getY();

				Log.i("DrawingActivity - onTouch ACTION_DOWN",
						motionEvent.getX() + " " + motionEvent.getY());
			//	drawView.addDrawingPoint(currentDrawingPoint);

			}
		}

		return true;
	}

	private void actionButton(View view) {
		Log.i("DrawingActivity - onClick", "click");
		switch (view.getId()) {
		case R.id.swithchStylePen:
			if (stylePen == 0) {
				stylePen = 1;
			} else {
				stylePen = 0;
			}
			break;
		case R.id.boutonNouvellePartie:
			// drawingSurface.resetImage();
			break;
		case R.id.colorRedBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.RED);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Red");
			break;
		case R.id.colorBlueBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.BLUE);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Blue");
			break;
		case R.id.colorGreenBtn:
			currentPaint = new Paint();
			currentPaint.setDither(true);
			currentPaint.setColor(Color.GREEN);
			currentPaint.setStyle(Paint.Style.STROKE);
			currentPaint.setStrokeJoin(Paint.Join.ROUND);
			currentPaint.setStrokeCap(Paint.Cap.ROUND);
			currentPaint.setStrokeWidth(3);
			Log.i("DrawingActivity - onClick", "Green");
			break;
		case R.id.envoyer:
			final Activity currentActivity = this;
			Handler saveHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							currentActivity).create();
					alertDialog.setTitle("Saved 1");
					alertDialog.setMessage("Your drawing had been saved :)");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							});
					alertDialog.show();
				}
			};
			// new ExportBitmapToFile(this, saveHandler,
			// drawView.getBitmap()).execute();
			break;
		}
	}

	private class ExportBitmapToFile extends AsyncTask<Intent, Void, Boolean> {
		private Context mContext;
		private Handler mHandler;
		private Bitmap nBitmap;

		public ExportBitmapToFile(Context context, Handler handler,
				Bitmap bitmap) {
			mContext = context;
			nBitmap = bitmap;
			mHandler = handler;
		}

		@Override
		protected Boolean doInBackground(Intent... arg0) {
			try {
				if (!APP_FILE_PATH.exists()) {
					APP_FILE_PATH.mkdirs();
				}
				// TODO
				// http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
				// TODO
				// http://stackoverflow.com/questions/4751609/writing-to-the-internal-private-storage-in-android-updated-2-6-12
				// final FileOutputStream out = new FileOutputStream(new File(
				// APP_FILE_PATH + "/myAwesomeDrawing.png"));
				FileOutputStream out = openFileOutput("myAwesomeDrawing.png",
						Context.MODE_PRIVATE);
				nBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// mHandler.post(completeRunnable);
			return false;
		}

		@Override
		protected void onPostExecute(Boolean bool) {
			super.onPostExecute(bool);
			if (bool) {
				mHandler.sendEmptyMessage(1);
			}
		}
	}
}