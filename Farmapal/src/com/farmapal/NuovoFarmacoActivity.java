package com.farmapal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farmapal.database.DBHelper;

public class NuovoFarmacoActivity extends Activity {

	private EditText editTextFarmaco;
	private EditText editTextPrincipiAttivi;
	private EditText editTextPeso;
	private EditText editTextTipo;
	private EditText editTextSomministrazione;
	private EditText editTextIndicazioni;
	private EditText editTextControindicazioni;
	private EditText editTextEffettiCollaterali;
	private Button btnFatto;
	private DBHelper db;
	private Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuovo_farmaco);
		db = DBHelper.getInstance(getApplicationContext());
		initComponents();
		addListeners();
	}

	private void addListeners() {
		btnFatto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(editTextFarmaco.getText().toString().equals("")) {
					AlertDialog.Builder noNomeFarmaco = new AlertDialog.Builder(v.getContext());
					noNomeFarmaco.setTitle("Attenzione");
					noNomeFarmaco.setMessage("Non hai inserito il nome del farmaco");
					noNomeFarmaco.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noNomeFarmaco.show();
				}
				
				else if(editTextPrincipiAttivi.getText().toString().equals("")) {
					AlertDialog.Builder noPrincipiAttivi = new AlertDialog.Builder(v.getContext());
					noPrincipiAttivi.setTitle("Attenzione");
					noPrincipiAttivi.setMessage("Non hai inserito alcun principio attivo");
					noPrincipiAttivi.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noPrincipiAttivi.show();
				}
				
				else if(editTextPeso.getText().toString().equals("")) {
					AlertDialog.Builder noPeso = new AlertDialog.Builder(v.getContext());
					noPeso.setTitle("Attenzione");
					noPeso.setMessage("Non hai inserito il peso");
					noPeso.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noPeso.show();
				}
				
				else if(editTextTipo.getText().toString().equals("")) {
					AlertDialog.Builder noTipo = new AlertDialog.Builder(v.getContext());
					noTipo.setTitle("Attenzione");
					noTipo.setMessage("Non hai inserito il tipo del farmaco");
					noTipo.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noTipo.show();
				}
				
				else if(editTextSomministrazione.getText().toString().equals("")) {
					AlertDialog.Builder noSomministrazione = new AlertDialog.Builder(v.getContext());
					noSomministrazione.setTitle("Attenzione");
					noSomministrazione.setMessage("Non hai inserito la somministrazione del farmaco");
					noSomministrazione.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noSomministrazione.show();
				}
				
				else if(editTextIndicazioni.getText().toString().equals("")) {
					AlertDialog.Builder noIndicazioni = new AlertDialog.Builder(v.getContext());
					noIndicazioni.setTitle("Attenzione");
					noIndicazioni.setMessage("Non hai inserito le indicazioni del farmaco");
					noIndicazioni.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noIndicazioni.show();
				}
				
				else if(editTextControindicazioni.getText().toString().equals("")) {
					AlertDialog.Builder noControindicazioni = new AlertDialog.Builder(v.getContext());
					noControindicazioni.setTitle("Attenzione");
					noControindicazioni.setMessage("Non hai inserito le controindicazioni del farmaco");
					noControindicazioni.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					noControindicazioni.show();
				}
				
				else if(editTextEffettiCollaterali.getText().toString().equals("")) {
					AlertDialog.Builder noEffettiCollaterali = new AlertDialog.Builder(v.getContext());
					noEffettiCollaterali.setTitle("Attenzione");
					noEffettiCollaterali.setMessage("Non hai inserito gli effetti collaterali del farmaco");
					noEffettiCollaterali.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						
						}
					});
					noEffettiCollaterali.show();
				}
				
				else {
					String nome = editTextFarmaco.getText().toString();
					String principi = editTextPrincipiAttivi.getText().toString();
					String peso = editTextPeso.getText().toString();
					String tipo = editTextTipo.getText().toString();
					String somministrazione = editTextSomministrazione.getText().toString();
					String indicazioni = editTextIndicazioni.getText().toString();
					String controindicazioni = editTextControindicazioni.getText().toString();
					String effetti = editTextEffettiCollaterali.getText().toString();
					
					c = db.getMaxIDFarmaco();
					int max_id;
					if(c.moveToFirst())
						max_id = c.getInt(0);
					else max_id = 0;
					
					long check = db.insertFarmaco(++max_id, nome, principi, peso, tipo, somministrazione, indicazioni, controindicazioni, effetti);
					if(check < 0)
						Toast.makeText(getApplicationContext(), "Errore nel salvataggio del farmaco", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getApplicationContext(), "Farmaco salvato", Toast.LENGTH_SHORT).show();
					
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);
					c.close();
					finish();
				}
				
			}
		});
		
	}

	private void initComponents() {
		editTextFarmaco = (EditText) findViewById(R.id.NuovoFarmacoeditTextFarmaco);
		editTextPrincipiAttivi = (EditText) findViewById(R.id.NuovoFarmacoeditTextPrincipiAttivi);
		editTextPeso = (EditText) findViewById(R.id.NuovoFarmacoeditTextPeso);
		editTextTipo = (EditText) findViewById(R.id.NuovoFarmacoeditTextTipo);
		editTextSomministrazione = (EditText) findViewById(R.id.NuovoFarmacoeditTextSomministrazione);
		editTextIndicazioni = (EditText) findViewById(R.id.NuovoFarmacoeditTextIndicazioni);
		editTextControindicazioni = (EditText) findViewById(R.id.NuovoFarmacoeditTextControindicazioni);
		editTextEffettiCollaterali = (EditText) findViewById(R.id.NuovoFarmacoeditTextEffettiCollaterali);
		btnFatto = (Button) findViewById(R.id.NuovoFarmacobuttonFatto);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nuovo_farmaco, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if(c != null)
			c.close();
		finish();
	}

	
}
