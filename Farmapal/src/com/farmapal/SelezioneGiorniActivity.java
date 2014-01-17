package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SelezioneGiorniActivity extends Activity {
	private Button btnFatto;
	private Button btnSelezionaTutti;
	private Button btnDeselezionaTutti;
	private CheckBox checkLun;
	private CheckBox checkMar;
	private CheckBox checkMer;
	private CheckBox checkGio;
	private CheckBox checkVen;
	private CheckBox checkSab;
	private CheckBox checkDom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selezione_giorni);
		checkLun = (CheckBox) findViewById(R.id.checkBoxLun);
		checkMar = (CheckBox) findViewById(R.id.checkBoxMar);
		checkMer = (CheckBox) findViewById(R.id.checkBoxMer);
		checkGio = (CheckBox) findViewById(R.id.checkBoxGio);
		checkVen = (CheckBox) findViewById(R.id.checkBoxVen);
		checkSab = (CheckBox) findViewById(R.id.checkBoxSab);
		checkDom = (CheckBox) findViewById(R.id.checkBoxDom);
		Bundle b = getIntent().getExtras();
		initCheckBoxes(b.getIntArray("giorni_selezionati"));
		addListenersbtnActions();
		addListenerbtnFatto();
		
	}

	private void addListenersbtnActions() {
		btnSelezionaTutti = (Button) findViewById(R.id.SelezioneGiorniSelezionaTutto);
		btnDeselezionaTutti = (Button) findViewById(R.id.SelezioneGiorniDeselezionaTutto);
		
		btnSelezionaTutti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkLun.setChecked(true);
				checkMar.setChecked(true);
				checkMer.setChecked(true);
				checkGio.setChecked(true);
				checkVen.setChecked(true);
				checkSab.setChecked(true);
				checkDom.setChecked(true);
				
			}
		});
		
		btnDeselezionaTutti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkLun.setChecked(false);
				checkMar.setChecked(false);
				checkMer.setChecked(false);
				checkGio.setChecked(false);
				checkVen.setChecked(false);
				checkSab.setChecked(false);
				checkDom.setChecked(false);
				
			}
		});
		
	}

	private void initCheckBoxes(int[] giorni_selezionati) {
		if(giorni_selezionati[0] == 1)
			checkLun.setChecked(true);
		if(giorni_selezionati[1] == 1)
			checkMar.setChecked(true);
		if(giorni_selezionati[2] == 1)
			checkMer.setChecked(true);
		if(giorni_selezionati[3] == 1)
			checkGio.setChecked(true);
		if(giorni_selezionati[4] == 1)
			checkVen.setChecked(true);
		if(giorni_selezionati[5] == 1)
			checkSab.setChecked(true);
		if(giorni_selezionati[6] == 1)
			checkDom.setChecked(true);
		
	}

	private void addListenerbtnFatto() {
		btnFatto = (Button) findViewById(R.id.SelezioneGiorniFatto);
		btnFatto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				if(checkLun.isChecked()) 
					returnIntent.putExtra("flagLun", 1);
				else 
					returnIntent.putExtra("flagLun", 0);
					
				if(checkMar.isChecked())
					returnIntent.putExtra("flagMar", 1);
				else
					returnIntent.putExtra("flagMar", 0);
				if(checkMer.isChecked())
					returnIntent.putExtra("flagMer", 1);
				else
					returnIntent.putExtra("flagMer", 0);
				if(checkGio.isChecked())
					returnIntent.putExtra("flagGio", 1);
				else
					returnIntent.putExtra("flagGio", 0);
				if(checkVen.isChecked())
					returnIntent.putExtra("flagVen", 1);
				else
					returnIntent.putExtra("flagVen", 0);
				if(checkSab.isChecked())
					returnIntent.putExtra("flagSab", 1);
				else
					returnIntent.putExtra("flagSab", 0);
				if(checkDom.isChecked())
					returnIntent.putExtra("flagDom", 1);
				else
					returnIntent.putExtra("flagDom", 0);
				
				setResult(RESULT_OK, returnIntent);
				finish();
				
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selezione_giorni, menu);
		return true;
	}

}
