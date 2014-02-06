package com.farmapal.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.farmapal.DettaglioPrescrizioneActivity;
import com.farmapal.R;
import com.farmapal.database.DBHelper;

public class ConfermaAssunzioneDialog extends Activity {

	private int idPrescrizione;
	private int idNotifica;
	private int idOrarioRazionePresa;
	private DBHelper db;
	private String nomeFarmaco;
	private String pesoFarmaco;
	private String tipoFarmaco;
	private String nomePaziente;
	private String orario_razione;
	private boolean ultimaRazione;
	private int dopo_fine=0;
	private CharSequence message;
	private String notificationTag;
	private NotificationManager notificationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conferma_assunzione_dialog);
		db = DBHelper.getInstance(getApplicationContext());
		//dettaglioAssunzione = (TextView) findViewById(R.id.textView_dettagli_assunzione);
		getDataAlarm();
		showDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conferma_assunzione_dialog, menu);
		return true;
	}
	
	private void getDataAlarm(){
		Bundle b = getIntent().getExtras();
		idNotifica = b.getInt("id_notifica");
		idPrescrizione = b.getInt("id_prescrizione");
		idOrarioRazionePresa = b.getInt("orario_razione_presa");
		nomeFarmaco = b.getString("nome_farmaco");
		pesoFarmaco = b.getString("peso_farmaco");
		tipoFarmaco = b.getString("tipo_farmaco");
		nomePaziente = b.getString("nome_paziente");
		orario_razione = b.getString("orario_razione");
		ultimaRazione = b.getBoolean("ultima_razione");
		notificationTag = b.getString("notification_tag");
	}
	
	private void showDialog() {
		// Otteniamo il riferimento al Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Impostiamo il titolo, il messaggio ed una icona in chaining
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("NOTIFICA ASSUNZIONE");
		
		if(ultimaRazione==false){
			message="Paziente: "+nomePaziente+ "\nAssunzione del farmaco:\n" 
					+ nomeFarmaco + " "+pesoFarmaco +" "
					+ tipoFarmaco+"\nOrario di assunzione richiesta: " + orario_razione;
			
			builder.setMessage(message);
		}
		else{
			dopo_fine=1;
			message="Paziente: "+nomePaziente+ "\nAssunzione del farmaco:\n" 
					+ nomeFarmaco + " " + pesoFarmaco +" "
					+ tipoFarmaco+"\nOrario di assunzione richiesta: " + orario_razione 
					+ "\nQuesta e' l'ultima razione della cura";
			
			builder.setMessage(message);
		}
		
		
		
//		dettaglioAssunzione.setText(message);
//		builder.setMessage(R.id.textView_dettagli_assunzione);
		
		// Impostiamo il pulsante di Conferma Assunzione con il relativo listener
		builder.setPositiveButton("Conferma Assunzione",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						ConfermaAssunzione();
					}

				});
		// Impostiamo il pulsante Dettagli Prescrizione con il relativo listener
		builder.setNegativeButton("Dettagli",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(getApplicationContext(), DettaglioPrescrizioneActivity.class);
						Bundle bundle = new Bundle();
						String id_presc = Integer.toString(idPrescrizione);
						bundle.putString("id", id_presc);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}

				});
		
		AlertDialog alert = builder.create();
		alert.show();
		
	}
	
	private void ConfermaAssunzione(){
		
		db.updateRazionePresa(idPrescrizione, idOrarioRazionePresa);
		if(ultimaRazione==true){
			dopo_fine=1;
			int checkPrescrizioni = db.updatePrescrizioneEndPeriodNotification(idPrescrizione, dopo_fine);
			if (checkPrescrizioni == -1)
				Toast.makeText(this, "errore durante il salvataggio della prescrizione", Toast.LENGTH_LONG).show();
//			else
//				Toast.makeText(this, "prescrizione modificata", Toast.LENGTH_LONG).show();
		}
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationTag, idNotifica);
		finish();
	}

}
