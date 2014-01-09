package com.farmapal.service;

import com.farmapal.R;
import com.farmapal.R.layout;
import com.farmapal.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
