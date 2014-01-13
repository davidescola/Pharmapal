package com.farmapal;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.farmapal.adapters.FarmaciAdapter;
import com.farmapal.database.DBHelper;

public class ListaCompletaForResultActivity extends Activity implements OnClickListener{

	DBHelper db;
	Button btnFatto;
	FarmaciAdapter myCursorAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_completa_for_result);
		db = new DBHelper(this);
		openDB();
		populateListViewFromDB();
		btnFatto = (Button) findViewById(R.id.btnFattoFarmaciForResult);
		btnFatto.setOnClickListener(this);
		closeDB();


	}

	private void populateListViewFromDB() {
		Cursor cursor = db.getAllFarmaci();

		startManagingCursor(cursor);

		myCursorAdapter = new FarmaciAdapter(this, cursor, 0);
		//set the adapter for the listview
		ListView myList = (ListView)findViewById(R.id.listFarmaciForResult);
		myList.setAdapter(myCursorAdapter);

	}

	private void openDB() {
		db = new DBHelper(this);
		try {
			db.createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void closeDB() {
		db.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_completa, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent returnIntent = new Intent();
		String retFarmaco = myCursorAdapter.getRetFarmaco();
		String retSomministrazione = myCursorAdapter.getRetSomministrazione();
		String retPeso = myCursorAdapter.getRetPeso();
		String retTipo = myCursorAdapter.getRetTipo();
		
		if(myCursorAdapter.itemIsChecked()) {
		Toast.makeText(v.getContext(), "farmaco: " + retFarmaco 
				+ " somministrazione: " + retSomministrazione
				+ " peso: " + retPeso
				+ " tipo: " + retTipo, Toast.LENGTH_LONG).show();
		}
		
		else
			Toast.makeText(v.getContext(), "nessun elemento selezionato", Toast.LENGTH_LONG).show();
	}

}
