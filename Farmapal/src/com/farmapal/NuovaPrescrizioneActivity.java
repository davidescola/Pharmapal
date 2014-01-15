package com.farmapal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class NuovaPrescrizioneActivity extends Activity implements OnDateChangedListener{

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
		Cursor maxIDPaziente = db.getMaxIDPaziente();
		startManagingCursor(maxIDPaziente);

		while(pazienti.moveToNext()) {
			listPazienti.add(pazienti.getString(pazienti.getColumnIndex("nome")));

		}
		maxIDPaziente.moveToFirst();
		countPazienti = maxIDPaziente.getInt(0);
		listPazienti.add("nuovo paziente");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPazienti);
		final Spinner spinner = (Spinner) findViewById(R.id.spinnerPazienti);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				EditText nuovoPaziente = (EditText)findViewById(R.id.editTextNuovoPaziente);
				if(spinner.getSelectedItem().toString().equals("nuovo paziente"))
					nuovoPaziente.setVisibility(View.VISIBLE);
				else
					nuovoPaziente.setVisibility(View.GONE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//non fare nulla
			}
		});


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
		EditText editTextNuovoPaziente = (EditText) findViewById(R.id.editTextNuovoPaziente);
		Spinner spinnerQta = (Spinner) findViewById(R.id.spinnerQta);
		Spinner spinnerFrequenza = (Spinner) findViewById(R.id.spinnerFrequenza);
		final DatePicker pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
		final DatePicker pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
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
		LinearLayout layoutFarmaco = (LinearLayout) findViewById(R.id.layoutResultFarmaco);
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);
		int curMonth = cal.get(Calendar.MONTH);
		int curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);


		pickerDal.init(curYear, curMonth, curDayOfMonth, this);

		pickerAl.init(curYear, curMonth, curDayOfMonth, this);


		if(editTextNuovoPaziente.getVisibility() == View.VISIBLE && editTextNuovoPaziente.getText().toString().equals("")) {
			AlertDialog.Builder noNuovoPaziente = new AlertDialog.Builder(this);
			noNuovoPaziente.setTitle("Attenzione");
			noNuovoPaziente.setMessage("Non hai inserito il nome del nuovo paziente");
			noNuovoPaziente.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//non fare nulla

				}
			});

			noNuovoPaziente.show();
		}
		else if(editTextMedico.getText().toString().equals(""))
		{
			AlertDialog.Builder noMedico = new AlertDialog.Builder(this);
			noMedico.setTitle("Attenzione");
			noMedico.setMessage("Non hai inserito il nome del medico");
			noMedico.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//non fare nulla
				}
			});

			noMedico.show();
		}
		else if(layoutFarmaco.getVisibility() == View.GONE) {
			AlertDialog.Builder noFarmaco = new AlertDialog.Builder(this);
			noFarmaco.setTitle("Attenzione");
			noFarmaco.setMessage("Non hai selezionato alcun farmaco");
			noFarmaco.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//non fare nulla
				}
			});

			noFarmaco.show();
		}

		else{
			if(spinnerPazienti.getSelectedItem().toString().equals("nuovo paziente")) {
				++countPazienti;
				String nomePaziente = editTextNuovoPaziente.getText().toString();
				long checkPazienti = db.insertPaziente(nomePaziente, countPazienti);
				if (checkPazienti == -1) {
					Toast.makeText(getApplicationContext(), "paziente esistente! aggiungo la prescrizione al paziente " + nomePaziente, Toast.LENGTH_LONG).show();
					--countPazienti;
				}
				else
					Toast.makeText(getApplicationContext(), "paziente " + nomePaziente + " creato", Toast.LENGTH_SHORT).show();
			}
			String nomeMedico = editTextMedico.getText().toString();
			String stringDataInizio = "" + pickerDal.getYear() + "-" + pickerDal.getMonth() + 1 + "-" + pickerDal.getDayOfMonth();
			String stringDataFine = "" + pickerAl.getYear() + "-" + pickerAl.getMonth() + 1 + "-" + pickerAl.getDayOfMonth();
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

			String nomePaziente;
			if(spinnerPazienti.getSelectedItem().toString().equals("nuovo paziente"))
				nomePaziente = editTextNuovoPaziente.getText().toString();
			else
				nomePaziente = spinnerPazienti.getSelectedItem().toString();

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

			Cursor cursorMaxIDPrescrizioni = db.getMaxIDPrescrizione();
			startManagingCursor(cursorMaxIDPrescrizioni);
			cursorMaxIDPrescrizioni.moveToFirst();
			countPrescrizioni = cursorMaxIDPrescrizioni.getInt(0);
			long checkPrescrizioni;

			switch(qta) {

			case 1:
				hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
				minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
				razione1 = hour1 + ":" + minute1 + ":00.000";
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
				break;
			case 2:
				hour1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
				minute1 = NuovaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
				razione1 = hour1 + ":" + minute1 + ":00.000";
				hour2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
				minute2 = NuovaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
				razione2 = hour2 + ":" + minute2 + ":00.000";
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
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
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
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
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
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
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
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
				checkPrescrizioni = db.insertPrescrizione(++countPrescrizioni, 
						nomeMedico, stringDataInizio, stringDataFine, qta, freq, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5, razione6});
				if (checkPrescrizioni == -1)
					Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this, "prescrizione salvata", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			db.close();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent	);
			finish();
		}
	}


	private static String adjustTime(String string) {
		if(string.length() == 1)
			string = "0" + string;
		return string;
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

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		//NON PARTE IL LISTENER!!!! PERCHE?????
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		formatter.setLenient(false);
//		if(view.getId() == R.id.datePickerDal) {
//			DatePicker pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
//			String dalString = "" + year + "-" + monthOfYear+1 + "-" + dayOfMonth;
//			try {
//				Date dalDate = formatter.parse(dalString);
//				long dalMillis = dalDate.getTime();
//				pickerAl.setMinDate(dalMillis);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		if(view.getId() == R.id.datePickerAl) {
//			DatePicker pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
//			String alString = "" + year + "-" + monthOfYear+1 + "-" + dayOfMonth;
//			try {
//				Date alDate = formatter.parse(alString);
//				long alMillis = alDate.getTime();
//				pickerDal.setMaxDate(alMillis);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
		Toast.makeText(getApplicationContext(), "ciao", Toast.LENGTH_LONG).show();
	}
}