package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.farmapal.adapters.PrescrizioniAdapter;
import com.farmapal.database.DBHelper;

public class MiePrescrizioniActivity extends Activity {

	DBHelper db;
	PrescrizioniAdapter myCursorAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mie_prescrizioni);
		db = new DBHelper(this);
		populateListviewFromDB();
		db.close();
	}

	private void populateListviewFromDB() {
		Cursor cursor = db.getAllPrescrizioni();
		startManagingCursor(cursor);
		myCursorAdapter = new PrescrizioniAdapter(this, cursor, 0);
		ListView list = (ListView)findViewById(R.id.listPrescrizioni);
		list.setAdapter(myCursorAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mie_prescrizioni, menu);
		return true;
	}

	public void goNuovaPrescrizione(View view) {
		Intent intent = new Intent(this, NuovaPrescrizioneActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			if(resultCode == RESULT_OK) {
				finish();
				startActivity(getIntent());
			}
				
		}
	}
	
	

}
