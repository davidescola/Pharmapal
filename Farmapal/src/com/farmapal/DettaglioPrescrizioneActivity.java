package com.farmapal;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class DettaglioPrescrizioneActivity extends Activity {

	private DBHelper db;
	private TextView nomePaziente;
	private TextView nomeFarmaco;
	private TextView editTextGiorni;
	private TextView validita;
	private TextView nomeMedico;
	private TextView razione1;
	private TextView razione2;
	private TextView razione3;
	private TextView razione4;
	private TextView razione5;
	private TextView razione6;
	private String[] giorni;
	private ArrayList<String> giorniRazioni;
	private Button btnElimina;
	private Button btnDettaglioFarmaco;
	private Button btnModifica;
	private int idPrescrizione;
	private int idFarmaco;
	private int qta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_prescrizione);
		db = new DBHelper(this);
		populateActivity();
		addListeners();
	}

	private void addListeners() {
		btnElimina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder confermaEliminazione = new AlertDialog.Builder(v.getContext());
				confermaEliminazione.setTitle("Attenzione");
				confermaEliminazione.setMessage("La prescrizione viene eliminata definitivamente. Confermi l'eliminazione?");
				confermaEliminazione.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deletePrescrizioneFromID(idPrescrizione);
						Intent returnIntent = new Intent();
						setResult(RESULT_OK, returnIntent);
						finish();

					}
				});

				confermaEliminazione.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {


					}
				});
				confermaEliminazione.show();	
			}
		});

		btnDettaglioFarmaco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DettaglioFarmacoActivity.class);
				Bundle b = new Bundle();
				b.putInt("id_farmaco", idFarmaco);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		
		btnModifica.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ModificaPrescrizioneActivity.class);
				Bundle b = new Bundle();
				b.putInt("id_prescrizione", idPrescrizione);
				b.putInt("qta_razioni", qta);
				intent.putExtras(b);
				startActivityForResult(intent, 6);
				
			}
		});

	}

	private void populateActivity() {
		nomePaziente = (TextView) findViewById(R.id.DettaglioPrescrizioneNomePaziente);
		nomeFarmaco = (TextView) findViewById(R.id.DettaglioPrescrizioneFarmaco);
		editTextGiorni = (TextView) findViewById(R.id.DettaglioPrescrizioneFrequenza);
		validita = (TextView) findViewById(R.id.DettaglioPrescrizioneValidita);
		nomeMedico = (TextView) findViewById(R.id.DettaglioPrescrizioneMedico);
		razione1 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione1);
		razione2 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione2);
		razione3 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione3);
		razione4 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione4);
		razione5 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione5);
		razione6 = (TextView) findViewById(R.id.DettaglioPrescrizioneRazione6);
		btnElimina = (Button) findViewById(R.id.DettaglioPrescrizioneElimina);
		btnDettaglioFarmaco = (Button) findViewById(R.id.DettaglioPrescrizioneDettaglioFarmaco);
		btnModifica = (Button) findViewById(R.id.DettaglioPrescrizioneModifica);
		giorni = new String[] {"lunedi","martedi","mercoledi","giovedi","venerdi","sabato","domenica"};
		giorniRazioni = new ArrayList<String>();

		Bundle b = getIntent().getExtras();
		idPrescrizione = Integer.parseInt(b.getString("id"));
		Cursor c = db.getPrescrizioneFromID(idPrescrizione);
		startManagingCursor(c);
		c.moveToFirst();


		int idPaziente = c.getInt(c.getColumnIndex("id_paziente"));
		Cursor cursorAccessorio = db.getPazienteFromID(idPaziente);
		startManagingCursor(cursorAccessorio);
		cursorAccessorio.moveToFirst();
		nomePaziente.setText("Paziente: " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("nome")));
		idFarmaco = c.getInt(c.getColumnIndex("id_farmaco"));
		cursorAccessorio = db.getFarmacoFromID(idFarmaco);
		cursorAccessorio.moveToFirst();
		nomeFarmaco.setText("Farmaco: " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("nome")) +
				" " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("peso")) + 
				" " + cursorAccessorio.getString(cursorAccessorio.getColumnIndex("tipo")));

		for(String giorno : giorni) {
			int flag = c.getInt(c.getColumnIndex(giorno));
			if(flag == 1) 
				giorniRazioni.add(giorno);
			
		}
		String s = new String("");
		for(int i = 0; i < giorniRazioni.size(); i++) {
			if(i != giorniRazioni.size() - 1)
				s = s + giorniRazioni.get(i) + ", ";
			else
				s = s + giorniRazioni.get(i);
		}

		editTextGiorni.setText("da assumere " + c.getInt(c.getColumnIndex("quantita")) + " volta/e nei giorni di " + s);
		validita.setText("dal " + c.getString(c.getColumnIndex("data_inizio")) + " al " + c.getString(c.getColumnIndex("data_fine")));
		nomeMedico.setText("Prescritto da: " + c.getString(c.getColumnIndex("medico")));

		switch(c.getInt(c.getColumnIndex("quantita"))) {

		case 0:
			break;
		case 1:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			qta = 1;
			break;
		case 2:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			razione2.setText("Razione 2 da assumere alle ore " + c.getString(c.getColumnIndex("ora2")));
			razione2.setVisibility(View.VISIBLE);
			qta = 2;
			break;
		case 3:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			razione2.setText("Razione 2 da assumere alle ore " + c.getString(c.getColumnIndex("ora2")));
			razione2.setVisibility(View.VISIBLE);
			razione3.setText("Razione 3 da assumere alle ore " + c.getString(c.getColumnIndex("ora3")));
			razione3.setVisibility(View.VISIBLE);
			qta = 3;
			break;
		case 4:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			razione2.setText("Razione 2 da assumere alle ore " + c.getString(c.getColumnIndex("ora2")));
			razione2.setVisibility(View.VISIBLE);
			razione3.setText("Razione 3 da assumere alle ore " + c.getString(c.getColumnIndex("ora3")));
			razione3.setVisibility(View.VISIBLE);
			razione4.setText("Razione 4 da assumere alle ore " + c.getString(c.getColumnIndex("ora4")));
			razione4.setVisibility(View.VISIBLE);
			qta = 4;
			break;
		case 5:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			razione2.setText("Razione 2 da assumere alle ore " + c.getString(c.getColumnIndex("ora2")));
			razione2.setVisibility(View.VISIBLE);
			razione3.setText("Razione 3 da assumere alle ore " + c.getString(c.getColumnIndex("ora3")));
			razione3.setVisibility(View.VISIBLE);
			razione4.setText("Razione 4 da assumere alle ore " + c.getString(c.getColumnIndex("ora4")));
			razione4.setVisibility(View.VISIBLE);
			razione5.setText("Razione 5 da assumere alle ore " + c.getString(c.getColumnIndex("ora5")));
			razione5.setVisibility(View.VISIBLE);
			qta = 5;
			break;
		case 6:
			razione1.setText("Razione 1 da assumere alle ore " + c.getString(c.getColumnIndex("ora1")));
			razione1.setVisibility(View.VISIBLE);
			razione2.setText("Razione 2 da assumere alle ore " + c.getString(c.getColumnIndex("ora2")));
			razione2.setVisibility(View.VISIBLE);
			razione3.setText("Razione 3 da assumere alle ore " + c.getString(c.getColumnIndex("ora3")));
			razione3.setVisibility(View.VISIBLE);
			razione4.setText("Razione 4 da assumere alle ore " + c.getString(c.getColumnIndex("ora4")));
			razione4.setVisibility(View.VISIBLE);
			razione5.setText("Razione 5 da assumere alle ore " + c.getString(c.getColumnIndex("ora5")));
			razione5.setVisibility(View.VISIBLE);
			razione6.setText("Razione 6 da assumere alle ore " + c.getString(c.getColumnIndex("ora6")));
			razione6.setVisibility(View.VISIBLE);
			qta = 6;
		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dettaglio_prescrizione, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 6) {
			if(resultCode == RESULT_OK) {
				finish();
				startActivity(getIntent());
			}
		}
	}
	
	


}
