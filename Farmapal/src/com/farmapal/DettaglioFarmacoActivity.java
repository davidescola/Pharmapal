package com.farmapal;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.farmapal.database.DBHelper;

public class DettaglioFarmacoActivity extends Activity {

	private DBHelper db;
	private TextView textViewFarmaco;
	private TextView textViewDescrizione;
	private TextView textViewPrincipiAttivi;
	private TextView textViewIndicazioni;
	private TextView textViewControindicazioni;
	private TextView textViewEffettiCollaterali;
	private int id_farmaco;
	private Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_farmaco);
		db = new DBHelper(getApplicationContext());
		initAttributes();
		setDatiFarmaco(id_farmaco);
	}

	private void setDatiFarmaco(int id_farmaco) {
		c = db.getFarmacoFromID(id_farmaco);
		if(c.moveToFirst()) {
			textViewFarmaco.setText("Nome: " + c.getString(c.getColumnIndex("nome")));
			String descrizione = 
					c.getString(c.getColumnIndex("peso")) + " " + 
					c.getString(c.getColumnIndex("tipo")) + " " +
					c.getString(c.getColumnIndex("peso")) + " " +
					"somministrazione " + c.getString(c.getColumnIndex("somministrazione"));
			textViewDescrizione.setText(descrizione);
			textViewPrincipiAttivi.setText("Principi attivi: " + c.getString(c.getColumnIndex("principi_attivi")));
			textViewIndicazioni.setText("Indicazioni: " + c.getString(c.getColumnIndex("indicazioni")));
			textViewControindicazioni.setText("Controindicazioni: " + c.getString(c.getColumnIndex("controindicazioni")));
			textViewEffettiCollaterali.setText("Effetti collaterali: " + c.getString(c.getColumnIndex("effetti_indesiderati")));
		}

	}

	private void initAttributes() {
		textViewFarmaco = (TextView) findViewById(R.id.DettaglioFarmacoNomeFarmaco);
		textViewDescrizione = (TextView) findViewById(R.id.DettaglioFarmacoDescrizioneFarmaco);
		textViewPrincipiAttivi = (TextView) findViewById(R.id.DettaglioFarmacoPrincipiAttivi);
		textViewIndicazioni = (TextView) findViewById(R.id.DettaglioFarmacoIndicazioni);
		textViewControindicazioni = (TextView) findViewById(R.id.DettaglioFarmacoControindicazioni);
		textViewEffettiCollaterali = (TextView) findViewById(R.id.DettaglioFarmacoEffettiCollaterali);

		Bundle b = getIntent().getExtras();
		id_farmaco = b.getInt("id_farmaco");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dettaglio_farmaco, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if(c != null)
			c.close();
		db.close();
		finish();
	}
	
	

}
