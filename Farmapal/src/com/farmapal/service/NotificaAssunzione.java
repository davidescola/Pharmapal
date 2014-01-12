package com.farmapal.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotificaAssunzione extends Service {

	private BackgroundThread backgroundThread;
	public boolean medicineTaken=false;
	private NotificationManager notificationManager;
	private Intent intent;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Otteniamo il riferimento al NotificationManager
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Facciamo partire il BackgroundThread
		backgroundThread = new BackgroundThread();
		backgroundThread.start();;

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

	public void checkAssumption() {
		// prendere dal DB il num

		//All'ora X mandare la notifica
		if(true){
			sendNotification();
		}

	}

	private void sendNotification(){
		String msgText = "Jeally Bean Notification example!! "
				+ "where you will see three different kind of notification. "
				+ "you can even put the very long string here.";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//Intent all'Activity di destinazione
		//BISOGNA METTERE UN PARAMETRO NEL DB: PRESA NON PRESA (TRUE,FALSE)
		intent = new Intent(this, ConfermaAssunzioneDialog.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
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
	}

	public final class BackgroundThread extends Thread {

		public boolean running= true;
		private String TAG="BackgroundThread";

		// ********************************************************		
		@Override
		public void run() {
			Log.i(TAG, "BackgroundThread Started");			
			while(running && !medicineTaken){

				checkAssumption();

			}
			// Al termine del metodo run terminiamo il servizio
			stopSelf();
		}
	}

}
