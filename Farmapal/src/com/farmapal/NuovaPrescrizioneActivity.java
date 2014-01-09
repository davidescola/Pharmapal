package com.farmapal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class NuovaPrescrizioneActivity extends Activity {

	DBHelper db;
	int countPazienti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_prescrizione);
		//openDB();
		db = new DBHelper(this);
		populateSpinnerPazienti();
		addListenerSpinnerQta();
		addListenerbtnSelezionaFarmaco();
		//closeDB(); TODO pensare a quando fare la close
	}
	private void addListenerbtnSelezionaFarmaco() {
		Button btnSelezionaFarmaco = (Button) findViewById(R.id.buttonSelezionaFarmaco);
		btnSelezionaFarmaco.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ListaCompletaForResultActivity.class);
				startActivityForResult(intent, 1);
				
			}
		});
		
	}

	private void addListenerSpinnerQta() {
		final Spinner spinnerQta = (Spinner) findViewById(R.id.spinnerQta);
		spinnerQta.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String strQta = spinnerQta.getSelectedItem().toString();
				int qta = Integer.parseInt(strQta);
				LinearLayout layout1 = (LinearLayout) findViewById(R.id.layoutRazione1);
				LinearLayout layout2 = (LinearLayout) findViewById(R.id.layoutRazione2);
				LinearLayout layout3 = (LinearLayout) findViewById(R.id.layoutRazione3);
				LinearLayout layout4 = (LinearLayout) findViewById(R.id.layoutRazione4);
				LinearLayout layout5 = (LinearLayout) findViewById(R.id.layoutRazione5);
				LinearLayout layout6 = (LinearLayout) findViewById(R.id.layoutRazione6);

				switch (qta) {

				case 1:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.GONE);
					layout3.setVisibility(View.GONE);
					layout4.setVisibility(View.GONE);
					layout5.setVisibility(View.GONE);
					layout6.setVisibility(View.GONE);
					break;
				case 2:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layout3.setVisibility(View.GONE);
					layout4.setVisibility(View.GONE);
					layout5.setVisibility(View.GONE);
					layout6.setVisibility(View.GONE);
					break;
				case 3:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layout3.setVisibility(View.VISIBLE);
					layout4.setVisibility(View.GONE);
					layout5.setVisibility(View.GONE);
					layout6.setVisibility(View.GONE);
					break;
				case 4:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layout3.setVisibility(View.VISIBLE);
					layout4.setVisibility(View.VISIBLE);
					layout5.setVisibility(View.GONE);
					layout6.setVisibility(View.GONE);
					break;
				case 5:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layout3.setVisibility(View.VISIBLE);
					layout4.setVisibility(View.VISIBLE);
					layout5.setVisibility(View.VISIBLE);
					layout6.setVisibility(View.GONE);
					break;
				case 6:
					layout1.setVisibility(View.VISIBLE);
					layout2.setVisibility(View.VISIBLE);
					layout3.setVisibility(View.VISIBLE);
					layout4.setVisibility(View.VISIBLE);
					layout5.setVisibility(View.VISIBLE);
					layout6.setVisibility(View.VISIBLE);
					break;
				default:
					break;

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// non fare nulla

			}
		});



	}

	private void populateSpinnerPazienti() {

		ArrayList<String> listPazienti = new ArrayList<String>();
		Cursor pazienti = db.getAllPazienti();
		startManagingCursor(pazienti);

		while(pazienti.moveToNext()) {
			listPazienti.add(pazienti.getString(pazienti.getColumnIndex("nome")));

		}
		countPazienti = pazienti.getCount();
		

		listPazienti.add("nuovo paziente");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPazienti);
		Spinner spinner = (Spinner) findViewById(R.id.spinnerPazienti);
		spinner.setAdapter(adapter);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuova_prescrizione, menu);
		return true;
	}

	public void aggiungiPrescrizione(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerPazienti);
		EditText editTextMedico = (EditText) findViewById(R.id.editTextMedico);
		Spinner spinnerQta = (Spinner) findViewById(R.id.spinnerQta);
		Spinner spinnerFrequenza = (Spinner) findViewById(R.id.spinnerFrequenza);
		DatePicker pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
		DatePicker pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
		Button buttonFatto = (Button) findViewById(R.id.buttonOk);

		int qta_giornaliera = Integer.parseInt(spinnerQta.getSelectedItem().toString()); 


		if(spinner.getSelectedItem().toString().equals("nuovo paziente")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Paziente");
			alert.setMessage("inserisci il nome del paziente: ");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					String nome = input.getText().toString();
					long check = db.insertPaziente(nome, ++countPazienti);
					if (check == -1) 
						Toast.makeText(getApplicationContext(), "paziente esistente! scegliere un altro nome", Toast.LENGTH_LONG).show();
					
					else
						Toast.makeText(getApplicationContext(), "paziente " + nome + " creato", Toast.LENGTH_SHORT).show();
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					//TODO annulla
				}
			});

			alert.show();
			//db.close();
		}


		//		Log.d("prova", "prescrizione per il paziente: " + spinner.getSelectedItem().toString()  +
		//				" medico: " + editTextMedico.getText() + 
		//				" quantita: " + editTextQuantita.getText() + 
		//				" frequenza: " + editTextFrequenza.getText() + 
		//				" dal giorno: " + pickerDal.getDayOfMonth() + 
		//				" mese: " + pickerDal.getMonth() + 
		//				" anno: " + pickerDal.getYear() + 
		//				" al giorno: " + pickerAl.getDayOfMonth() + 
		//				" mese: " + pickerAl.getMonth() + 
		//				" anno: " + pickerAl.getYear() 
		//				
		//				);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateDal = new Date();
		Date dateAl = new Date();
		String stringDal = "" + pickerDal.getYear() + "-" + pickerDal.getMonth() + "-" + pickerDal.getDayOfMonth();
		String stringAl = "" + pickerAl.getYear() + "-" + pickerAl.getMonth() + "-" + pickerAl.getDayOfMonth();


	}

	private void openDB() {
		db = new DBHelper(this);
		try {
			db.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void closeDB() {
		db.close();

	}


}
