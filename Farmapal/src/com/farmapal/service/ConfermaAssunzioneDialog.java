package com.farmapal.service;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.farmapal.R;

public class ConfermaAssunzioneDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conferma_assunzione_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conferma_assunzione_dialog, menu);
		return true;
	}

}
