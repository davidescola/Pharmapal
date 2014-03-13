package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationListener;
import android.view.View;
import android.widget.LinearLayout;

import com.farmapal.service.NotificaAssunzione;

public class MainActivity extends Activity {

	private static final String tag = "MainActivity";
	private Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serviceIntent = new Intent(this, NotificaAssunzione.class);
		startService(serviceIntent);
		setContentView(R.layout.activity_main);
		
	}
	
	private void checkScreenOrientation(Configuration config) {
		int orientation=config.orientation;

		LinearLayout layout=(LinearLayout)findViewById(R.id.MainLinearLayout);
		switch(orientation) {

		case Configuration.ORIENTATION_LANDSCAPE:
			layout.setBackgroundResource(R.drawable.main_screen_landscape);
			break;

		case Configuration.ORIENTATION_PORTRAIT:
			layout.setBackgroundResource(R.drawable.main_screen);
			break;

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		checkScreenOrientation(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void goListaCompleta(View view) {
		Intent intent = new Intent(this, ListaCompletaActivity.class);
		startActivity(intent);
	}
	
	public void goMiePrescrizioni(View view) {
		Intent intent = new Intent(this, MiePrescrizioniActivity.class);
		startActivity(intent);
	}
	
	public void goListaFarmacie(View view) {
		Intent intent = new Intent(this, ListaFarmacie.class);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
//		stopService(serviceIntent);
		//For Debug
//		Log.i(tag, "Service Stopped, App Closed");
		finish();
	}

	

}
