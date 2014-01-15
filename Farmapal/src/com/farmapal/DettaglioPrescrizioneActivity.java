package com.farmapal;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DettaglioPrescrizioneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_prescrizione);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dettaglio_prescrizione, menu);
		return true;
	}

}
