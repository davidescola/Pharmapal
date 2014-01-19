package com.farmapal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class ModificaPrescrizioneActivity extends Activity {

	private DBHelper db;
	private int idPrescrizione;
	private TextView textViewPaziente;
	private TextView textViewMedico;
	private TextView textViewNomeFarmaco;
	private TextView textViewSomministrazione;
	private TextView textViewPeso;
	private TextView textViewTipo;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private LinearLayout layout3;
	private LinearLayout layout4;
	private LinearLayout layout5;
	private LinearLayout layout6;
	private Spinner spinnerQta;
	private Button btnSelezionaGiorni;
	private TextView textViewRiepilogoGiorni;
	private TimePicker tp1;
	private TimePicker tp2;
	private TimePicker tp3;
	private TimePicker tp4;
	private TimePicker tp5;
	private TimePicker tp6;
	private Button btnFatto;
	private DatePicker pickerDal;
	private DatePicker pickerAl;
	private Cursor c;
	private Cursor c2;
	private String stringDataInizio;
	private String stringDataFine;
	private boolean dateCorrette = true;

	private String[] giorni = new String[] {"lunedi","martedi","mercoledi","giovedi","venerdi","sabato","domenica"};
	private int[] flagGiorni = new int[] {0,0,0,0,0,0,0};
	private ArrayList<String> giorniRazioni = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifica_prescrizione);
		initAttributes();
		setContents();
		addListeners();
	}

	private void addListeners() {
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
				//non fare nulla

			}
		});

		btnSelezionaGiorni.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SelezioneGiorniActivity.class);
				Bundle b = new Bundle();
				b.putIntArray("giorni_selezionati", flagGiorni);
				intent.putExtras(b);
				startActivityForResult(intent, 5);

			}
		});

		btnFatto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(textViewRiepilogoGiorni.getText().equals("Non hai ancora selezionato i giorni")) {
					AlertDialog.Builder noGiorni = new AlertDialog.Builder(v.getContext());
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



				else {
					String stringDataInizio = "" + pickerDal.getDayOfMonth() + "-" + adjustMonth(pickerDal.getMonth()) + "-" + pickerDal.getYear();
					String stringDataFine = "" + pickerAl.getDayOfMonth() + "-" + adjustMonth(pickerAl.getMonth()) + "-" + pickerAl.getYear();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					try {
						long dataInizio = formatter.parse(stringDataInizio).getTime();
						long dataFine = formatter.parse(stringDataFine).getTime();
						if(dataInizio > dataFine)
							dateCorrette = false;
						else
							dateCorrette = true;
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if(!dateCorrette)
						Toast.makeText(getApplicationContext(), "errore", Toast.LENGTH_LONG).show();
					else {
						int qta = Integer.parseInt(spinnerQta.getSelectedItem().toString());
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
						long checkPrescrizioni;

						switch(qta) {

						case 1:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione, 
									stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						case 2:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							hour2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
							minute2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
							razione2 = hour2 + ":" + minute2 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione,	stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1, razione2});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						case 3:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							hour2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
							minute2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
							razione2 = hour2 + ":" + minute2 + ":00.000";
							hour3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
							minute3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
							razione3 = hour3 + ":" + minute3 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione, stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1, razione2, razione3});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						case 4:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							hour2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
							minute2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
							razione2 = hour2 + ":" + minute2 + ":00.000";
							hour3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
							minute3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
							razione3 = hour3 + ":" + minute3 + ":00.000";
							hour4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
							minute4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
							razione4 = hour4 + ":" + minute4 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione, stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1, razione2, razione3, razione4});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						case 5:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							hour2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
							minute2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
							razione2 = hour2 + ":" + minute2 + ":00.000";
							hour3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
							minute3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
							razione3 = hour3 + ":" + minute3 + ":00.000";
							hour4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
							minute4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
							razione4 = hour4 + ":" + minute4 + ":00.000";
							hour5 = ModificaPrescrizioneActivity.adjustTime(tp5.getCurrentHour().toString());
							minute5 = ModificaPrescrizioneActivity.adjustTime(tp5.getCurrentMinute().toString());
							razione5 = hour5 + ":" + minute5 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione, stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1, razione2, razione3, razione4, razione5});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						case 6:
							hour1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentHour().toString());
							minute1 = ModificaPrescrizioneActivity.adjustTime(tp1.getCurrentMinute().toString());
							razione1 = hour1 + ":" + minute1 + ":00.000";
							hour2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentHour().toString());
							minute2 = ModificaPrescrizioneActivity.adjustTime(tp2.getCurrentMinute().toString());
							razione2 = hour2 + ":" + minute2 + ":00.000";
							hour3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentHour().toString());
							minute3 = ModificaPrescrizioneActivity.adjustTime(tp3.getCurrentMinute().toString());
							razione3 = hour3 + ":" + minute3 + ":00.000";
							hour4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentHour().toString());
							minute4 = ModificaPrescrizioneActivity.adjustTime(tp4.getCurrentMinute().toString());
							razione4 = hour4 + ":" + minute4 + ":00.000";
							hour5 = ModificaPrescrizioneActivity.adjustTime(tp5.getCurrentHour().toString());
							minute5 = ModificaPrescrizioneActivity.adjustTime(tp5.getCurrentMinute().toString());
							razione5 = hour5 + ":" + minute5 + ":00.000";
							hour6 = ModificaPrescrizioneActivity.adjustTime(tp6.getCurrentHour().toString());
							minute6 = ModificaPrescrizioneActivity.adjustTime(tp6.getCurrentMinute().toString());
							razione6 = hour6 + ":" + minute6 + ":00.000";
							checkPrescrizioni = db.updatePrescrizioneFromID(idPrescrizione, stringDataInizio, stringDataFine, qta, flagGiorni, new String [] {razione1, razione2, razione3, razione4, razione5, razione6});
							if (checkPrescrizioni == -1)
								Toast.makeText(v.getContext(), "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
							else
								Toast.makeText(v.getContext(), "prescrizione modificata", Toast.LENGTH_LONG).show();
							break;
						default:
							break;
						}

						Intent returnIntent = new Intent();
						setResult(RESULT_OK, returnIntent);
						if(c != null)
							c.close();
						if(c2 != null)
							c2.close();
						finish();
					}
				}

			}
		});

	}

	private void setContents() {
		c = db.getPrescrizioneFromID(idPrescrizione);
		if(c.moveToFirst()) {
			int IDPaziente = c.getInt(c.getColumnIndex("id_paziente"));
			c2 = db.getPazienteFromID(IDPaziente);
			if(c2.moveToFirst())
				textViewPaziente.setText("Paziente: " + c2.getString(c2.getColumnIndex("nome")));
			textViewMedico.setText("Prescritto da: " + c.getString(c.getColumnIndex("medico")));
			int IDFarmaco = c.getInt(c.getColumnIndex("id_farmaco"));
			c2 = db.getFarmacoFromID(IDFarmaco);
			if(c2.moveToFirst()) {
				textViewNomeFarmaco.setText(c2.getString(c2.getColumnIndex("nome")));
				textViewPeso.setText(c2.getString(c2.getColumnIndex("peso")));
				textViewSomministrazione.setText(c2.getString(c2.getColumnIndex("somministrazione")));
				textViewTipo.setText(c2.getString(c2.getColumnIndex("tipo")));
			}

		}


		String s = "";
		for(int i = 0; i < giorniRazioni.size(); i++) {
			if(i != giorniRazioni.size() - 1)
				s = s + giorniRazioni.get(i) + ", ";
			else
				s = s + giorniRazioni.get(i);

			textViewRiepilogoGiorni.setText("da assumere nei giorni di " + s);
		}

		int qta_precedente = getIntent().getExtras().getInt("qta_razioni");
		spinnerQta.setSelection(qta_precedente - 1);

	}

	private void initAttributes() {
		db = DBHelper.getInstance(getApplicationContext());
		idPrescrizione = getIntent().getExtras().getInt("id_prescrizione");
		textViewPaziente = (TextView) findViewById(R.id.ModificaPrescrizioneNomePaziente);
		textViewMedico = (TextView) findViewById(R.id.ModificaPrescrizioneNomeMedico);
		textViewNomeFarmaco = (TextView) findViewById(R.id.ModificaPrescrizioneNomeFarmaco);
		textViewPeso = (TextView) findViewById(R.id.ModificaPrescrizionePeso);
		textViewTipo = (TextView) findViewById(R.id.ModificaPrescrizioneTipo);
		textViewSomministrazione = (TextView) findViewById(R.id.ModificaPrescrizioneSomministrazione);
		layout1 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione1);
		layout2 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione2);
		layout3 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione3);
		layout4 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione4);
		layout5 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione5);
		layout6 = (LinearLayout) findViewById(R.id.ModificaPrescrizionelayoutRazione6);
		spinnerQta = (Spinner) findViewById(R.id.ModificaPrescrizionespinnerQta);
		btnSelezionaGiorni = (Button) findViewById(R.id.ModificaPrescrizionebtnGiorni);
		textViewRiepilogoGiorni = (TextView) findViewById(R.id.ModificaPrescrizionetextViewRiepilogoGiorni);
		tp1 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker1);
		tp2 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker2);
		tp3 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker3);
		tp4 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker4);
		tp5 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker5);
		tp6 = (TimePicker) findViewById(R.id.ModificaPrescrizionetimePicker6);
		btnFatto = (Button) findViewById(R.id.ModificaPrescrizionebuttonOk);
		pickerDal = (DatePicker) findViewById(R.id.ModificaPrescrizionedatePickerDal);
		pickerAl = (DatePicker) findViewById(R.id.ModificaPrescrizionedatePickerAl);
		setTimePickers();

		c = db.getPrescrizioneFromID(idPrescrizione);
		if(c.moveToFirst())
			for(int i = 0; i < giorni.length; i++) {
				int flag = c.getInt(c.getColumnIndex(giorni[i]));
				if (flag == 1)
					flagGiorni[i] = 1;
				else
					flagGiorni[i] = 0;
			}

		for(int j = 0; j < flagGiorni.length; j++) {
			if(flagGiorni[j] == 1)
				giorniRazioni.add(giorni[j]);
		}


	}

	private void setTimePickers() {
		tp1.setIs24HourView(true);
		tp2.setIs24HourView(true);
		tp3.setIs24HourView(true);
		tp4.setIs24HourView(true);
		tp5.setIs24HourView(true);
		tp6.setIs24HourView(true);
		tp1.setIs24HourView(true);
		tp2.setIs24HourView(true);
		tp3.setIs24HourView(true);
		tp4.setIs24HourView(true);
		tp5.setIs24HourView(true);
		tp6.setIs24HourView(true);
		
		Calendar cal=Calendar.getInstance();
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int min=cal.get(Calendar.MINUTE);
		
		tp1.setCurrentHour(hour);
		tp1.setCurrentMinute(min);
		tp2.setCurrentHour(hour);
		tp2.setCurrentMinute(min);
		tp3.setCurrentHour(hour);
		tp3.setCurrentMinute(min);
		tp4.setCurrentHour(hour);
		tp4.setCurrentMinute(min);
		tp5.setCurrentHour(hour);
		tp5.setCurrentMinute(min);
		tp6.setCurrentHour(hour);
		tp6.setCurrentMinute(min);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modifica_prescrizione, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 5) {
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
					textViewRiepilogoGiorni.setText("Non hai ancora selezionato i giorni");
				else {
					for(int j = 0; j < listGiorni.size(); j++) {

						if(j != listGiorni.size() - 1)
							s = s + listGiorni.get(j) + ", ";
						else 
							s = s + listGiorni.get(j);

					}
					textViewRiepilogoGiorni.setText("Da assumere nei giorni di " + s);
				}

			}
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
	public void onBackPressed() {
		if(c != null)
			c.close();
		if(c2 != null)
			c2.close();
		finish();
	}



}
