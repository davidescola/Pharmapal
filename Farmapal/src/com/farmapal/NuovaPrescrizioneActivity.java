package com.farmapal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.farmapal.database.DBHelper;

public class NuovaPrescrizioneActivity extends Activity {
	
	DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_prescrizione);
		db = new DBHelper(this);
		populateSpinner();
	}

	private void populateSpinner() {

		ArrayList<String> listPazienti = new ArrayList<String>();
		DBHelper db = new DBHelper(this);


		//ciclo for sui ris del db

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

	public void aggiungiPaziente(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.spinnerPazienti);
		EditText editTextMedico = (EditText) findViewById(R.id.editTextMedico);
		EditText editTextQuantita = (EditText) findViewById(R.id.editTextQuantita);
		EditText editTextFrequenza = (EditText) findViewById(R.id.editTextFrequenza);
		DatePicker pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
		DatePicker pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
		Button buttonFatto = (Button) findViewById(R.id.buttonOk);


		if(spinner.getSelectedItem().toString().equals("nuovo paziente")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Paziente");
			alert.setMessage("inserisci il nome del paziente: ");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString();
					//db.insertPaziente(value);  //TODO NON FUNZIONA: CONTROLLARE
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});

			alert.show();
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

}
