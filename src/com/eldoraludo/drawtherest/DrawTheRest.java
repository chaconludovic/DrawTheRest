package com.eldoraludo.drawtherest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class DrawTheRest extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_the_rest_with_view);
		// Intent drawIntent = new Intent(this, DrawingActivity.class);
		// startActivity(drawIntent);
		Intent drawIntent = new Intent(this, DrawingActivityWitView.class);
		startActivity(drawIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_draw_the_rest, menu);
		return true;
	}

}
