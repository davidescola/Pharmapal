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

public class NuovaPrescrizioneActivity extends Activity {

	DBHelper db;
	int countPazienti;
	int countPrescrizioni;
	int[] flagGiorni = new int[] {0,0,0,0,0,0,0};
	String[] giorni = new String[] {"lunedi","martedi","mercoledi","giovedi","venerdi","sabato","domenica"};
	int idFarmaco = -1;

	private Button btnGiorni;
	private TimePicker tp1;
	private TimePicker tp2;
	private TimePicker tp3;
	private TimePicker tp4;
	private TimePicker tp5;
	private TimePicker tp6;
	private Button btnSelezionaFarmaco;
	private Spinner spinnerQta;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private LinearLayout layout3;
	private LinearLayout layout4;
	private LinearLayout layout5;
	private LinearLayout layout6;
	private Spinner spinnerPazienti;
	private EditText editTextMedico;
	private EditText editTextNuovoPaziente;
	private DatePicker pickerDal;
	private DatePicker pickerAl;
	private TextView textFarmaco;
	private TextView textSomministrazione;
	private TextView textPeso;
	private TextView textTipo;
	private LinearLayout layoutFarmaco;
	private TextView textViewRiepilogo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuova_prescrizione);
		initAttributes();
		db = new DBHelper(this);
		populateSpinnerPazienti();
		addListenerSpinnerQta();
		setTimePickers();
		addListenerbtnSelezionaFarmaco();
		addListenerbtnSelezionaGiorni();
		db.close(); 
	}

	private void initAttributes() {
		btnGiorni = (Button) findViewById(R.id.btnGiorni);
		tp1 = (TimePicker) findViewById(R.id.timePicker1);
		tp2 = (TimePicker) findViewById(R.id.timePicker2);
		tp3 = (TimePicker) findViewById(R.id.timePicker3);
		tp4 = (TimePicker) findViewById(R.id.timePicker4);
		tp5 = (TimePicker) findViewById(R.id.timePicker5);
		tp6 = (TimePicker) findViewById(R.id.timePicker6);
		btnSelezionaFarmaco = (Button) findViewById(R.id.buttonSelezionaFarmaco);
		spinnerQta = (Spinner) findViewById(R.id.spinnerQta);
		layout1 = (LinearLayout) findViewById(R.id.layoutRazione1);
		layout2 = (LinearLayout) findViewById(R.id.layoutRazione2);
		layout3 = (LinearLayout) findViewById(R.id.layoutRazione3);
		layout4 = (LinearLayout) findViewById(R.id.layoutRazione4);
		layout5 = (LinearLayout) findViewById(R.id.layoutRazione5);
		layout6 = (LinearLayout) findViewById(R.id.layoutRazione6);
		spinnerPazienti = (Spinner) findViewById(R.id.spinnerPazienti);
		editTextMedico = (EditText) findViewById(R.id.editTextMedico);
		editTextNuovoPaziente = (EditText) findViewById(R.id.editTextNuovoPaziente);
		pickerDal = (DatePicker) findViewById(R.id.datePickerDal);
		pickerAl = (DatePicker) findViewById(R.id.datePickerAl);
		textFarmaco = (TextView) findViewById(R.id.textViewResultNomeFarmaco);
		textSomministrazione = (TextView) findViewById(R.id.textViewResultSomministrazione);
		textPeso = (TextView) findViewById(R.id.textViewResultPeso);
		textTipo = (TextView) findViewById(R.id.textViewResultTipo);
		layoutFarmaco = (LinearLayout) findViewById(R.id.layoutResultFarmaco);
		textViewRiepilogo = (TextView) findViewById(R.id.textViewRiepilogoGiorni);

	}

	private void addListenerbtnSelezionaGiorni() {
		btnGiorni.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SelezioneGiorniActivity.class);
				Bundle b = new Bundle();
				b.putIntArray("giorni_selezionati", flagGiorni);
				intent.putExtras(b);
				startActivityForResult(intent, 3);

			}
		});

	}

	private void setTimePickers() {
		tp1.setIs24HourView(true);
		tp2.setIs24HourView(true);
		tp3.setIs24HourView(true);
		tp4.setIs24HourView(true);
		tp5.setIs24HourView(true);
		tp6.setIs24HourView(true);

	}
	private void addListenerbtnSelezionaFarmaco() {
		btnSelezionaFarmaco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ListaCompletaForResultActivity.class);
				Bundle b = new Bundle();
				b.putInt("IDFarmacoPrecedente", idFarmaco);
				intent.putExtras(b);
				startActivityForResult(intent, 1);
			}
		});

	}

	private void addListenerSpinnerQta() {

		spinnerQta.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String strQta = spinnerQta.getSelectedItem().toString();
				int qta = Integer.parseInt(strQta);


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
		spinnerPazienti.setAdapter(adapter);
		spinnerPazienti.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				EditText nuovoPaziente = (EditText)findViewById(R.id.editTextNuovoPaziente);
				if(spinnerPazienti.getSelectedItem().toString().equals("nuovo paziente"))
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

		else if(textViewRiepilogo.getText().equals("Non hai ancora selezionato i giorni")) {
			AlertDialog.Builder noGiorni = new AlertDialog.Builder(this);
			noGiorni.setTitle("Attenzione");
			noGiorni.setMessage("Non hai selezionato alcun giorno");
			noGiorni.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//non fare nulla
				}
			});

			noGiorni.show();
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
			String stringDataInizio = "" + pickerDal.getDayOfMonth() + "-" + adjustMonth(pickerDal.getMonth()) + "-" + pickerDal.getYear();
			String stringDataFine = "" + pickerAl.getDayOfMonth() + "-" + adjustMonth(pickerAl.getMonth()) + "-" + pickerAl.getYear();
			int qta = Integer.parseInt(spinnerQta.getSelectedItem().toString());
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1});
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1, razione2});
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3});
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4});
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5});
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
						nomeMedico, stringDataInizio, stringDataFine, qta, flagGiorni, idFarmaco, idPaziente, new String [] {razione1, razione2, razione3, razione4, razione5, razione6});
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
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}


	private static String adjustTime(String string) {
		if(string.length() == 1)
			string = "0" + string;
		return string;
	}

	private static String adjustMonth(int mese) {
		mese++;
		String string_mese = "" + mese;
		if(string_mese.length() == 2)
			return string_mese;
		else
			return "0" + string_mese;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				idFarmaco = data.getIntExtra("retID", -1);
				textFarmaco.setText(data.getStringExtra("retFarmaco"));
				textSomministrazione.setText(data.getStringExtra("retSomministrazione"));
				textPeso.setText(data.getStringExtra("retPeso"));
				textTipo.setText(data.getStringExtra("retTipo"));
				layoutFarmaco.setVisibility(View.VISIBLE);
			}

			if(resultCode == RESULT_CANCELED) {
				idFarmaco = -1;
				layoutFarmaco.setVisibility(View.GONE);
			}
		}

		if(requestCode == 3) {
			if(resultCode == RESULT_OK) {
				flagGiorni[0] = data.getIntExtra("flagLun", 0);
				flagGiorni[1] = data.getIntExtra("flagMar", 0);
				flagGiorni[2] = data.getIntExtra("flagMer", 0);
				flagGiorni[3] = data.getIntExtra("flagGio", 0);
				flagGiorni[4] = data.getIntExtra("flagVen", 0);
				flagGiorni[5] = data.getIntExtra("flagSab", 0);
				flagGiorni[6] = data.getIntExtra("flagDom", 0);
				boolean giorniSelezionati = false;
				String s = "";
				ArrayList<String> listGiorni = new ArrayList<String>();

				for(int i = 0; i < flagGiorni.length; i++) {
					if(flagGiorni[i] == 1) {
						giorniSelezionati = true;
						listGiorni.add(giorni[i]);
					}
				}

				if(!giorniSelezionati)
					textViewRiepilogo.setText("Non hai ancora selezionato i giorni");
				else {
					for(int j = 0; j < listGiorni.size(); j++) {
						
							if(j != listGiorni.size() - 1)
								s = s + listGiorni.get(j) + ", ";
							else 
								s = s + listGiorni.get(j);
						
					}
					textViewRiepilogo.setText("Da assumere nei giorni di " + s);
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

	//	@Override
	//	public void onDateChanged(DatePicker view, int year, int monthOfYear,
	//			int dayOfMonth) {
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
	//		Toast.makeText(getApplicationContext(), "ciao", Toast.LENGTH_LONG).show();
	//	}
}