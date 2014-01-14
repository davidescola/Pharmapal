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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class NuovaPrescrizioneActivity extends Activity {

	DBHelper db;
	int countPazienti;
	int countPrescrizioni;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_prescrizione);
		//openDB();
		db = new DBHelper(this);
		populateSpinnerPazienti();
		addListenerSpinnerQta();
		setTimePickers();
		addListenerbtnSelezionaFarmaco();
		//closeDB(); TODO pensare a quando fare la close
	}
	private void setTimePickers() {
		TimePicker tp1 = (TimePicker) findViewById(R.id.timePicker1);
		TimePicker tp2 = (TimePicker) findViewById(R.id.timePicker2);
		TimePicker tp3 = (TimePicker) findViewById(R.id.timePicker3);
		TimePicker tp4 = (TimePicker) findViewById(R.id.timePicker4);
		TimePicker tp5 = (TimePicker) findViewById(R.id.timePicker5);
		TimePicker tp6 = (TimePicker) findViewById(R.id.timePicker6);

		tp1.setIs24HourView(true);
		tp2.setIs24HourView(true);
		tp3.setIs24HourView(true);
		tp4.setIs24HourView(true);
		tp5.setIs24HourView(true);
		tp6.setIs24HourView(true);

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
		Spinner spinnerPazienti = (Spinner) findViewById(R.id.spinnerPazienti);
		EditText editTextMedico = (EditText) findViewById(R.id.editTextMedico);
		Spinner spinnerQta = (Spinner) findViewById(R.id.spinnerQta);
		Spinner spinnerFrequenza = (Spinner) findViewById(R.id.spinnerFrequenza);
		DatePicker pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
		DatePicker pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
		TextView textFarmaco = (TextView) findViewById(R.id.textViewResultNomeFarmaco);
		TextView textSomministrazione = (TextView) findViewById(R.id.textViewResultSomministrazione);
		TextView textPeso = (TextView) findViewById(R.id.textViewResultPeso);
		TextView textTipo = (TextView) findViewById(R.id.textViewResultTipo);
		TimePicker tp1 = (TimePicker) findViewById(R.id.timePicker1);
		TimePicker tp2 = (TimePicker) findViewById(R.id.timePicker2);
		TimePicker tp3 = (TimePicker) findViewById(R.id.timePicker3);
		TimePicker tp4 = (TimePicker) findViewById(R.id.timePicker4);
		TimePicker tp5 = (TimePicker) findViewById(R.id.timePicker5);
		TimePicker tp6 = (TimePicker) findViewById(R.id.timePicker6);

		if(spinnerPazienti.getSelectedItem().toString().equals("nuovo paziente")) {
			++countPazienti;
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
					long check = db.insertPaziente(nome, countPazienti);
					if (check == -1) 
						Toast.makeText(getApplicationContext(), "paziente esistente! scegliere un altro nome", Toast.LENGTH_LONG).show();

					else
						Toast.makeText(getApplicationContext(), "paziente " + nome + " creato", Toast.LENGTH_SHORT).show();
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					--countPazienti;
				}
			});

			alert.show();
			//db.close();
		}


		String nomeMedico = editTextMedico.getText().toString();
		String nomePaziente = spinnerPazienti.getSelectedItem().toString();
		String stringDataInizio = "" + pickerDal.getYear() + "-" + pickerDal.getMonth() + "-" + pickerDal.getDayOfMonth();
		String stringDataFine = "" + pickerAl.getYear() + "-" + pickerAl.getMonth() + "-" + pickerAl.getDayOfMonth();
		int qta = Integer.parseInt(spinnerQta.getSelectedItem().toString());
		int freq = Integer.parseInt(spinnerFrequenza.getSelectedItem().toString());
		String farmaco = textFarmaco.getText().toString();
		String somministrazione = textSomministrazione.getText().toString();
		String peso = textPeso.getText().toString();
		String tipo = textTipo.getText().toString();
		int idFarmaco = 0;
		int idPaziente = 0;
		Cursor cursorIDFarmaco = db.getIDFarmaco(farmaco, tipo, peso, somministrazione);
		startManagingCursor(cursorIDFarmaco);
		if(cursorIDFarmaco.getCount() == 1) {
			cursorIDFarmaco.moveToFirst();
			idFarmaco = cursorIDFarmaco.getInt(cursorIDFarmaco.getColumnIndex("_id"));
		}

		Cursor cursorIDPaziente = db.getIDPaziente(nomePaziente);
		startManagingCursor(cursorIDPaziente);
		if(cursorIDPaziente.getCount() == 1) {
			cursorIDPaziente.moveToFirst();
			idPaziente = cursorIDPaziente.getInt(cursorIDPaziente.getColumnIndex("_id"));
		}
		else
			idPaziente = countPazienti;

		String razione1;
		String razione2;
		String razione3;
		String razione4;
		String razione5;
		String razione6;
		String hour1;
		String minute1;
		String hour2;
		String minute2;
		String hour3;
		String minute3;
		String hour4;
		String minute4;
		String hour5;
		String minute5;
		String hour6;
		String minute6;

		Cursor cursorPrescrizioni = db.getAllPrescrizioni();
		countPrescrizioni = cursorPrescrizioni.getCount();
		long check;

		switch(qta) {

		case 1:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		case 2:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
			minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
			razione2 = hour2 + ":" + minute2 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		case 3:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
			minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
			razione2 = hour2 + ":" + minute2 + ":00.000";
			hour3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
			minute3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
			razione3 = hour3 + ":" + minute3 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		case 4:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
			minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
			razione2 = hour2 + ":" + minute2 + ":00.000";
			hour3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
			minute3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
			razione3 = hour3 + ":" + minute3 + ":00.000";
			hour4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
			minute4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
			razione4 = hour4 + ":" + minute4 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		case 5:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
			minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
			razione2 = hour2 + ":" + minute2 + ":00.000";
			hour3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
			minute3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
			razione3 = hour3 + ":" + minute3 + ":00.000";
			hour4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
			minute4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
			razione4 = hour4 + ":" + minute4 + ":00.000";
			hour5 = NuovaPrescrizioneActivity.adjustTime(tp5.getCurrentHour().toString());
			minute5 = NuovaPrescrizioneActivity.adjustTime(tp5.getCurrentMinute().toString());
			razione5 = hour5 + ":" + minute5 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		case 6:
			hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
			minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
			razione1 = hour1 + ":" + minute1 + ":00.000";
			hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
			minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
			razione2 = hour2 + ":" + minute2 + ":00.000";
			hour3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
			minute3 = NuovaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
			razione3 = hour3 + ":" + minute3 + ":00.000";
			hour4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
			minute4 = NuovaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
			razione4 = hour4 + ":" + minute4 + ":00.000";
			hour5 = NuovaPrescrizioneActivity.adjustTime(tp5.getCurrentHour().toString());
			minute5 = NuovaPrescrizioneActivity.adjustTime(tp5.getCurrentMinute().toString());
			razione5 = hour5 + ":" + minute5 + ":00.000";
			hour6 = NuovaPrescrizioneActivity.adjustTime(tp6.getCurrentHour().toString());
			minute6 = NuovaPrescrizioneActivity.adjustTime(tp6.getCurrentMinute().toString());
			razione6 = hour6 + ":" + minute6 + ":00.000";
			check = db.insertPrescrizione(++countPrescrizioni, 
					nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5, razione6});
			if (check == -1)
				Toast.makeText(this, "errore", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "successo", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
		
		finish();
		
	}

	private static String adjustTime(String string) {
		if(string.length() == 1)
			string = "0" + string;
		return string;
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				TextView farmaco = (TextView) findViewById(R.id.textViewResultNomeFarmaco);
				TextView somministrazione = (TextView) findViewById(R.id.textViewResultSomministrazione);
				TextView peso = (TextView) findViewById(R.id.textViewResultPeso);
				TextView tipo = (TextView) findViewById(R.id.textViewResultTipo);

				farmaco.setText(data.getStringExtra("retFarmaco"));
				somministrazione.setText(data.getStringExtra("retSomministrazione"));
				peso.setText(data.getStringExtra("retPeso"));
				tipo.setText(data.getStringExtra("retTipo"));

				LinearLayout layout = (LinearLayout) findViewById(R.id.layoutResultFarmaco);
				layout.setVisibility(View.VISIBLE);
			}
		}


	}


}
