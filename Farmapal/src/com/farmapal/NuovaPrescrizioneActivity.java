package com.farmapal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NuovaPrescrizioneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_prescrizione);
		populateSpinner();
	}

	private void populateSpinner() {
		
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(this, 
						R.array.array_nuovo_paziente, 
						android.R.layout.simple_spinner_item);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerPazienti);
		spinner.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuova_prescrizione, menu);
		return true;
	}

}
