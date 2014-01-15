package com.farmapal.service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.farmapal.database.DBHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;


public class NotificaAssunzione extends Service {

	private BackgroundThread backgroundThread;
	public boolean medicineTaken=false;
	private NotificationManager notificationManager;
	private Intent intent;
	private DBHelper db;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		db = new DBHelper(this);
		// Otteniamo il riferimento al NotificationManager
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Facciamo partire il BackgroundThread
		backgroundThread = new BackgroundThread();
		backgroundThread.start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void checkForNotification() {
		//Dati Prescrizioni
		Cursor cursorAllPrescrizioni = db.getAllPrescrizioni();
		while(cursorAllPrescrizioni.moveToNext()){
			//Se razione_presa=0 allora devo ancora prenderla, ottengo i dati e in caso invio la notifica 
			if(cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_1"))==0
					|| cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_2"))==0
					|| cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_3"))==0
					|| cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_4"))==0
					|| cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_5"))==0
					|| cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("razione_presa_6"))==0)
			{
				//Formato: HH:MM:SS.mmm
				String[] ora=null, orario_razione=null, HourFormat, temp;
				String secondFormat;
				int hour, minute, second, milliSecond;
				
				String[] HourFormatCorrente, tempCorrente;
				String dataCorrente, secondFormatCorrente;
				int hourCorrente, minuteCorrente, secondCorrente, milliSecondCorrente;
				
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.mmm");
				//Data corrente
				Date date = new Date();
				System.out.println(dateFormat.format(date));
				dataCorrente=dateFormat.format(date);
				
				if (dataCorrente.contains(":")) {
					//Split dataCorrente
					HourFormatCorrente = dataCorrente.split("-");
					hourCorrente = Integer.parseInt(HourFormatCorrente[0]);
					minuteCorrente = Integer.parseInt(HourFormatCorrente[1]);
					secondFormatCorrente = HourFormatCorrente[2];
					
					if(secondFormatCorrente.contains(".")){
						tempCorrente = secondFormatCorrente.split("\\.");
						secondCorrente = Integer.parseInt(tempCorrente[0]);
						milliSecondCorrente= Integer.parseInt(tempCorrente[1]);
					}
					else
					{
					    throw new IllegalArgumentException("String " + dataCorrente + " non contiene .");
					}
				}
				else
				{
				    throw new IllegalArgumentException("String " + dataCorrente + " non contiene -");
				}
				
				//Scandisco e Controllo Orari di Razione
				for(int i=0 ; i<6 ; i++){
					ora[i] = cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_"+(i+1)));
					
					if (ora[i].contains(":")) {
						//Split orari razione
						HourFormat = ora[i].split("-");
						hour = Integer.parseInt(HourFormat[0]);
						minute = Integer.parseInt(HourFormat[1]);
						orario_razione[i] = HourFormat[0]+HourFormat[1];
						secondFormat = HourFormat[2];
						
						if(secondFormat.contains(".")){
							temp = secondFormat.split("\\.");
							second = Integer.parseInt(temp[0]);
							milliSecond= Integer.parseInt(temp[1]);
						}
						else
						{
						    throw new IllegalArgumentException("String " + ora[i] + " non contiene .");
						}
					}
					else
					{
					    throw new IllegalArgumentException("String " + ora[i] + " non contiene -");
					}
					
					//Controllo orario
					if(hour - hourCorrente ==0 ){
						if(minute - minuteCorrente < 10 /*minutePref se voglio fare dei Setting con le Shared Preferences*/){
							
							//ID Paziente
							int idPaziente = cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("id_paziente"));
							//ID Farmaco
							int idFarmaco = cursorAllPrescrizioni.getInt(cursorAllPrescrizioni.getColumnIndex("id_farmaco"));
							
							sendNotification(cursorAllPrescrizioni, i, idPaziente, idFarmaco, orario_razione[i]);
						}
					}
				}
//				
//				ora[0] = cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_1"));
//				String ora_2=cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_2"));
//				String ora_3=cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_3"));
//				String ora_4=cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_4"));
//				String ora_5=cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_5"));
//				String ora_6=cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("ora_6"));

				
			}
			
			//String data_inizio = cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("data_inizio"));
			
			//data_fine - data attuale -> Giorni rimanenti alla fine
			String data_fine = cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("data_fine"));
			
			
			cursorAllPrescrizioni.getString(cursorAllPrescrizioni.getColumnIndex("medico"));
		}
		
	}


	private void sendNotification(Cursor cursorPrescrizioni, int oraNum, int idPaziente, int idFarmaco, String orario_razione){
		
		//Dati Prescrizione
		String quantita=cursorPrescrizioni.getString(cursorPrescrizioni.getColumnIndex("quantita"));
		
		//Dati Farmaco
		Cursor cursorFarmaco = db.getFarmacoFromID(idFarmaco);
		cursorFarmaco.moveToFirst();
		String nomeFarmaco = cursorFarmaco.getString(cursorFarmaco.getColumnIndex("nome"));
		cursorFarmaco.getString(cursorFarmaco.getColumnIndex("peso"));
		String tipoFarmaco = cursorFarmaco.getString(cursorFarmaco.getColumnIndex("tipo"));
		
		//Dati Paziente
		Cursor cursorPaziente = db.getPazienteFromID(idPaziente);
		cursorPaziente.moveToFirst();
		cursorPaziente.getString(cursorPaziente.getColumnIndex("nome"));
		
		String msgText = "Ricordati di prendere: "
				+ nomeFarmaco + "entro le "
				+ orario_razione + "(" + quantita + " di tipo " + tipoFarmaco;

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//Intent all'Activity di destinazione
		//BISOGNA METTERE UN PARAMETRO NEL DB: PRESA NON PRESA (TRUE,FALSE)
		intent = new Intent(this, ConfermaAssunzioneDialog.class);
		
		//pIntent = PendingIntent.getActivity(this, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pi = PendingIntent.getActivity(this, oraNum, intent, 0);
		
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle("Big text Notofication")
		.setContentText("Big text Notification")
		.setSmallIcon(android.R.drawable.sym_def_app_icon)
		.setAutoCancel(true)
		.setPriority(Notification.PRIORITY_HIGH)
		.addAction(android.R.drawable.ic_menu_recent_history,
				"Conferma Assunzione", pi);
		//msgText da sistemare con i rispettivi valori
		Notification notification = new Notification.BigTextStyle(builder)
		.bigText(msgText).build();

		notificationManager.notify(0, notification);
		
		/*otification.setLatestEventInfo(this, "Allarme di Umidita'",	"Area: "+patientAreaAttuale[i], pIntent);
		// La lanciamo attraverso il Notification Manager
		notificationManager.notify(HUMIDITY_NOTIFICATION_ID+patientId[i], notification);*/
	}

	public final class BackgroundThread extends Thread {

		public boolean running= true;
		private String TAG="BackgroundThread";

		// ********************************************************		
		@Override
		public void run() {
			Log.i(TAG, "BackgroundThread Started");			
			while(running && !medicineTaken){
				
				checkForNotification();

			}
			// Al termine del metodo run terminiamo il servizio
			stopSelf();
		}
	}

}
