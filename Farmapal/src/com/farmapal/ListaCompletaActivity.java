package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.farmapal.adapters.ListaFarmaciAdapter;
import com.farmapal.database.DBHelper;

public class ListaCompletaActivity extends Activity {

	private DBHelper db;
	private Cursor cursor;
	private ListaFarmaciAdapter adapter;
	private Button btnAggiungiFarmaco;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_completa);
		db = DBHelper.getInstance(getApplicationContext());
		populateListViewFromDB();
	}

	private void populateListViewFromDB() {
		cursor = db.getAllFarmaci();
		adapter = new ListaFarmaciAdapter(getApplicationContext(), cursor, 0);
		
		ListView myList = (ListView)findViewById(R.id.listFarmaci);
		myList.setAdapter(adapter);
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(getApplicationContext(), DettaglioFarmacoActivity.class);
				Bundle b = new Bundle();
				b.putInt("id_farmaco", (int) id);
				b.putBoolean("flag_btnElimina", true);
				intent.putExtras(b);
				startActivityForResult(intent, 8);
				
			}
		});
		
		btnAggiungiFarmaco = (Button) findViewById(R.id.btnAggiungiFarmaco);
		btnAggiungiFarmaco.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), NuovoFarmacoActivity.class);
				startActivityForResult(intent, 7);
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_completa, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if(cursor != null)
			cursor.close();
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 7 || requestCode == 8) {
			if (resultCode == RESULT_OK) {
				finish();
				startActivity(getIntent());
			}
		}
	}
	
	

}
