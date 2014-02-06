package com.farmapal.service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.farmapal.database.DBHelper;


public class NotificaAssunzione extends Service {

	private BackgroundThread backgroundThread;
	public boolean medicineTaken=false;
	private NotificationManager notificationManager;
	private Intent intent;
	private DBHelper db;
	private String tag;
	private Context notificationContext;
	private Random r = new Random();
	private String[] HourFormatCorrente, dateFormatCorrente, tempCorrente;
	private String dataCorrente, secondPartFormatCorrente, thirdPartFormatCorrente, dayNameCorrente;
	private int yearCorrente, monthCorrente, dayCorrente, hourCorrente, minuteCorrente, secondCorrente, milliSecondCorrente;
	
	//Creazione lista temporanea delle notifiche in corso
	HashMap<Integer, Integer> hashMapTemporaneaIDprescrizione = new HashMap<Integer,Integer>();
	private int idnotifica;
	private int[] dataParsata;
	private int idPrescrizione;
	private boolean ultimaRazione=false;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE-dd-MM-yyyy-HH-mm-ss-mmm");
	private int checkResetDay=0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		notificationContext=getApplicationContext();
		db = DBHelper.getInstance(getApplicationContext());
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
	public void onDestroy() {
		backgroundThread.running = false;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int[] parseDate(String dataToParse){
		String[] HourFormatToParse, dateFormatToParse, tempCorrente;
		int[] returnDate = new int[7];
		String dataCorrente, secondPartFormatToParse, thirdPartFormatCorrente;
		int yearToParse, monthToParse, dayToParse, hourCorrente, minuteCorrente, secondCorrente, milliSecondCorrente;
		
		if (dataToParse.contains("-")){
			//Split dataCorrente
			dateFormatToParse = dataToParse.split("-");
			returnDate[0] = Integer.parseInt(dateFormatToParse[0]); //DAY
			returnDate[1] = Integer.parseInt(dateFormatToParse[1]); //MONTH
			returnDate[2] = Integer.parseInt(dateFormatToParse[2]); //YEAR
		}
		else
		{
		    throw new IllegalArgumentException("String " + dataToParse + " non contiene -");
		}
		return returnDate;
	}
	
	private void checkCurrentDate(){
		//Data corrente
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		dataCorrente=dateFormat.format(date);
		if (dataCorrente.contains("-")){
			//Split dataCorrente
			dateFormatCorrente = dataCorrente.split("-");
			dayNameCorrente = dateFormatCorrente[0];
			dayCorrente = Integer.parseInt(dateFormatCorrente[1]);
			monthCorrente = Integer.parseInt(dateFormatCorrente[2]);
			yearCorrente = Integer.parseInt(dateFormatCorrente[3]);
			hourCorrente = Integer.parseInt(dateFormatCorrente[4]);
			minuteCorrente = Integer.parseInt(dateFormatCorrente[5]);
			secondCorrente = Integer.parseInt(dateFormatCorrente[6]);
			milliSecondCorrente= Integer.parseInt(dateFormatCorrente[7]);
			//Di esempio
			if(hourCorrente==0 && (minuteCorrente >= 0 & minuteCorrente <40) && checkResetDay==0){
				db.updateResetGiornalieroRazioni();
				checkResetDay=1;
			}//Di esempio
			if(hourCorrente==23 && (minuteCorrente >= 58 & minuteCorrente <59) && checkResetDay==1){
				checkResetDay=0;
			}
		}
		else
		{
		    throw new IllegalArgumentException("String " + dataCorrente + " non contiene -");
		}
	}
	
	public Cursor setDayToControl(){
		Cursor c = null;
		if(dayNameCorrente.contains("Mon")){
			c=db.getLunPrescrizioni();
		}
		if(dayNameCorrente.contains("Tue")){
			c=db.getMarPrescrizioni();
		}
		if(dayNameCorrente.contains("Wed")){
			c=db.getMerPrescrizioni();
		}
		if(dayNameCorrente.contains("Thu")){
			c=db.getGioPrescrizioni();
		}
		if(dayNameCorrente.contains("Fri")){
			c=db.getVenPrescrizioni();
		}
		if(dayNameCorrente.contains("Sat")){
			c=db.getSabPrescrizioni();
		}
		if(dayNameCorrente.contains("Sun")){
			c=db.getDomPrescrizioni();
		}
		return c;
	}
	
	public void checkForNotification() {
		
		checkCurrentDate();

		//Dati Prescrizioni
		//Cursor cursorAllPrescrizioni = db.getAllPrescrizioni();
		
		//Ottengo le Prescrizioni solo del giorno corrente
		Cursor cursorCurrentDayPrescrizioni = setDayToControl();
		//cursorAllPrescrizioni.moveToFirst(); Non serve (parte da -1) e poi moveToNext
		int j=0,count=1;
		Log.e(tag, DatabaseUtils.dumpCursorToString(cursorCurrentDayPrescrizioni));
		while(cursorCurrentDayPrescrizioni.moveToNext()){
			
			if((cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("prima_inizio"))==1)
					  && (cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("dopo_fine"))!=1))
			{
			
				
				//Formato: HH:MM:SS.mmm
				String[] ora = new String[6], orario_razione = new String[6], HourFormat, temp;
				String[] secondFormat = new String[6];
				int[] hour = new int[6], minute = new int[6], second = new int[6], milliSecond = new int[6];
				Integer[] razione_presa = new Integer[6];
				
				//Scandisco e Controllo Orari di Razione
				for(int i=0 ; i<6 ; i++){
					j=i+1;
					String o = "ora"+j;
					int f=cursorCurrentDayPrescrizioni.getColumnIndex(o);
					String o2 = cursorCurrentDayPrescrizioni.getString(cursorCurrentDayPrescrizioni.getColumnIndex(o));
					ora[i] = cursorCurrentDayPrescrizioni.getString(cursorCurrentDayPrescrizioni.getColumnIndex(o));
					try{
						if(cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("razione_presa"+j))==0){
							if (ora[i].contains(":")) {
								//Split orari razione
								HourFormat = ora[i].split(":");
								hour[i] = Integer.parseInt(HourFormat[0]);
								minute[i] = Integer.parseInt(HourFormat[1]);
								orario_razione[i] = HourFormat[0]+":"+HourFormat[1];
								secondFormat[i] = HourFormat[2];
								
								if(secondFormat[i].contains(".")){
									temp=secondFormat[i].split("\\.");
									second[i] = Integer.parseInt(temp[0]);
									milliSecond[i]= Integer.parseInt(temp[1]);
									
									//Controllo orario
									if(hour[i] - hourCorrente ==0 ){
										if(Math.abs(minute[i] - minuteCorrente) <= 25 /*minutePref se voglio fare dei Setting con le Shared Preferences*/){
											
											//ID Paziente
											int idPaziente = cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("id_paziente"));
											//ID Farmaco
											int idFarmaco = cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("id_farmaco"));
											//ID Prescrizione
											int cursorPosition = cursorCurrentDayPrescrizioni.getPosition();
											idPrescrizione = cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("_id"));
											
											razione_presa[i] = cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("razione_presa"+j));
											
											//MiniAlgoritmo per l'assegnamento degli ID delle notifiche in modo che siano univoci
											idnotifica = idPrescrizione*10 + i;
											//sendNotification(cursorAllPrescrizioni, idnotifica, idPaziente, idFarmaco, orario_razione[i]);
											String data_fine;
											int[] dataFineParsata;
											int k=j+1;
											String o3 = "ora"+k;
											String oraNext = cursorCurrentDayPrescrizioni.getString(cursorCurrentDayPrescrizioni.getColumnIndex(o3));
											
											//Controllo se la prossima ora e' NULL (non contiene un orario) allora e' l'ultima della giornata
											if (oraNext==null) {
												data_fine = cursorCurrentDayPrescrizioni.getString(cursorCurrentDayPrescrizioni.getColumnIndex("data_fine"));
												dataFineParsata = parseDate(data_fine);
												//controllo se e' l'ultima razione della Cura
												if( (dataFineParsata[0]==dayCorrente ) //Stesso anno
														&&(dataFineParsata[1]==monthCorrente) //stesso mese
														&&(dataFineParsata[2]==yearCorrente)){
													ultimaRazione = true;
												}
											}
											//Invio la notifica
											sendNotification(cursorCurrentDayPrescrizioni, idnotifica, i, idPaziente, idFarmaco, orario_razione[i], dayNameCorrente);
										}
									}	
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
						}
					}
					catch(Exception e){
						//Log.i(tag, "l'orario "+ i +" e' NULL nel DB");
					}
				}
			}
			else{//Cura (assunzione farmaco) non ancora iniziata
				if(cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("prima_inizio"))!=1){
					//
					String data_inizio = cursorCurrentDayPrescrizioni.getString(cursorCurrentDayPrescrizioni.getColumnIndex("data_inizio"));
					int[] dataParsata = parseDate(data_inizio);
					if( (dataParsata[0]==dayCorrente ) //Stesso anno
							&&(dataParsata[1]==monthCorrente) //stesso mese
							&&(dataParsata[2]==yearCorrente)){ //stesso giorno
						
						//setto prima_inizio a 1 nel DB
						idPrescrizione = cursorCurrentDayPrescrizioni.getInt(cursorCurrentDayPrescrizioni.getColumnIndex("_id"));
						db.updatePrescrizioneStartPeriodNotification(idPrescrizione,1);
					}
					
				}
			}
			
		}
		
	}


	private void sendNotification(Cursor cursorPrescrizioni, int idNotifica, int indexOrario, int idPaziente, int idFarmaco, String orario_razione, String tagDay){

		//Dati Farmaco
		Cursor cursorFarmaco = db.getFarmacoFromID(idFarmaco);
		cursorFarmaco.moveToFirst();
		String nomeFarmaco = cursorFarmaco.getString(cursorFarmaco.getColumnIndex("nome"));
		String pesoFarmaco = cursorFarmaco.getString(cursorFarmaco.getColumnIndex("peso"));
		String tipoFarmaco = cursorFarmaco.getString(cursorFarmaco.getColumnIndex("tipo"));
		
		//Dati Paziente
		Cursor cursorPaziente = db.getPazienteFromID(idPaziente);
		cursorPaziente.moveToFirst();
		String nomePaziente = cursorPaziente.getString(cursorPaziente.getColumnIndex("nome"));
		
		String msgText = "Ricordati di prendere: " + nomeFarmaco + " " + pesoFarmaco + " " + tipoFarmaco + " entro le " 
				+ orario_razione;

		//Intent all'Activity di destinazione
		intent = new Intent(this, ConfermaAssunzioneDialog.class);
		
		Bundle b = new Bundle();
		b.putInt("id_notifica", idNotifica);
		b.putInt("id_prescrizione", idPrescrizione);
		b.putInt("orario_razione_presa", indexOrario);
		b.putString("nome_farmaco", nomeFarmaco);
		b.putString("peso_farmaco", pesoFarmaco);
		b.putString("tipo_farmaco", tipoFarmaco);
		b.putString("nome_paziente", nomePaziente);
		b.putString("orario_razione", orario_razione);
		b.putBoolean("ultima_razione", ultimaRazione);
		b.putString("notitication_tag", tagDay);
		intent.putExtras(b);
		
		PendingIntent pi = PendingIntent.getActivity(this, idNotifica, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification notification = new NotificationCompat.Builder(this)
		.setContentTitle("Avviso Assunzione")
		.setContentText(nomeFarmaco + " " + pesoFarmaco + " " + tipoFarmaco)
		.setSmallIcon(android.R.drawable.ic_dialog_alert)
		.setAutoCancel(true)
		.setContentIntent(pi)
		.setStyle(new NotificationCompat.BigTextStyle().bigText(msgText).setSummaryText("Conferma Assunzione"))
		.build();
		
		notificationManager.notify(tagDay, idNotifica, notification);
		
		ultimaRazione=false;
	}

	public final class BackgroundThread extends Thread {

		public boolean running= true;
		//Attesa di Refresh in Millisecondi
		private int delay=60000;
		private String TAG="BackgroundThread";

		// ********************************************************		
		@Override
		public void run() {
			Log.i(TAG, "BackgroundThread Avviato");			
			while(running && !medicineTaken){
				
				db = DBHelper.getInstance(getApplicationContext());
				try {
					TimeUnit.MILLISECONDS.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try{
					checkForNotification();
				}
				catch(Exception e){
					
				}
				
				db.close();

			}
			// Al termine del metodo run terminiamo il servizio
			stopSelf();
		}
	}

}
