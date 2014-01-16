package com.farmapal;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.farmapal.database.DBHelper;

public class DettaglioPrescrizioneActivity extends Activity {

	private DBHelper db;
	private TextView nomePaziente;
	private TextView nomeFarmaco;
	private TextView frequenza;
	private TextView validita;
	private TextView nomeMedico;
	private TextView razione1;
	private TextView razione2;
	private TextView razione3;
	private TextView razione4;
	private TextView razione5;
	private TextView razione6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_prescrizione);
		db = new DBHelper(this);
		populateActivity();
	}

	private void populateActivity() {
		nomePaziente = (TextView) findViewById(R.id.DettaglioPrescrizioneNomePaziente);
		nomeFarmaco = (TextView) findViewById(R.id.DettaglioPrescrizioneFarmaco);
		frequenza = (TextView) findViewById(R.id.DettaglioPrescrizioneFrequenza);
		validita = (TextView) findViewById(R.id.DettaglioPrescrizioneValidita);
		nomeMedico = (TextView) findViewById(R.id.DettaglioPrescrizioneMedico);
		razione1 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione1);
		razione2 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione2);
		razione3 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione3);
		razione4 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione4);
		razione5 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione5);
		razione6 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione6);
		Bundle b = getIntent().getExtras();
		Cursor c = db.getPrescrizioneFromID(Integer.parseInt(b.getString("id")));
		startManagingCursor(c);
		c.moveToFirst();
		
		//fetch del paziente
		int idPaziente = c.getInt(c.getColumnIndex("id_paziente"));
		Cursor cursorAccessorio = db.getPazienteFromID(idPaziente);
		startManagingCursor(cursorAccessorio);
		cursorAccessorio.moveToFirst();
		nomePaziente.setText("Paziente: " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("nome")));
		//fetch del farmaco
		int idFarmaco = c.getInt(c.getColumnIndex("id_farmaco"));
		cursorAccessorio = db.getFarmacoFromID(idFarmaco);
		cursorAccessorio.moveToFirst();
		nomeFarmaco.setText("Farmaco: " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("nome")) +
				" " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("tipo")) + 
				" " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("peso")));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dettaglio_prescrizione, menu);
		return true;
	}

}
