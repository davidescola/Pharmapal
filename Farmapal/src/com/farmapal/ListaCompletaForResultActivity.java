package com.farmapal;

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

public class ListaCompletaForResultActivity extends Activity{

	private DBHelper db;
	private Button btnFatto;
	private FarmaciAdapter myCursorAdapter;
	private int IDFarmacoPrecedente;
	private String nome_precedente = "";
	private String tipo_precedente = "";
	private String peso_precedente = "";
	private String somministrazione_precedente = "";
	private Cursor cursor;
	private Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_completa_for_result);
		db = DBHelper.getInstance(getApplicationContext());
		IDFarmacoPrecedente = getIntent().getExtras().getInt("IDFarmacoPrecedente");
		getFarmacoSelezionato(IDFarmacoPrecedente);
		populateListViewFromDB();
		btnFatto = (Button) findViewById(R.id.btnFattoFarmaciForResult);
		btnFatto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				String retFarmaco = myCursorAdapter.getRetFarmaco();
				String retSomministrazione = myCursorAdapter.getRetSomministrazione();
				String retPeso = myCursorAdapter.getRetPeso();
				String retTipo = myCursorAdapter.getRetTipo();
				int retID;

				if(myCursorAdapter.itemIsChecked()) {
					returnIntent.putExtra("retFarmaco", retFarmaco);
					returnIntent.putExtra("retSomministrazione", retSomministrazione);
					returnIntent.putExtra("retPeso", retPeso);
					returnIntent.putExtra("retTipo", retTipo);
					c = db.getIDFarmacoFromValori(retFarmaco, retTipo, retPeso, retSomministrazione);
					c.moveToFirst();
					retID = c.getInt(c.getColumnIndex("_id"));
					returnIntent.putExtra("retID", retID);
					setResult(RESULT_OK, returnIntent);
					c.close();
					cursor.close();
					finish();
				}

				else
					Toast.makeText(v.getContext(), "nessun elemento selezionato", Toast.LENGTH_LONG).show();

			}
		});
	}

	private void getFarmacoSelezionato(int id) {
		cursor = db.getFarmacoFromID(id);
		if(cursor.moveToFirst()) {
			nome_precedente = cursor.getString(cursor.getColumnIndex("nome"));
			tipo_precedente = cursor.getString(cursor.getColumnIndex("tipo"));
			peso_precedente = cursor.getString(cursor.getColumnIndex("peso"));
			somministrazione_precedente = cursor.getString(cursor.getColumnIndex("somministrazione"));
		}

	}

	private void populateListViewFromDB() {
		cursor = db.getAllFarmaci();
		myCursorAdapter = new FarmaciAdapter(this, cursor, 0);
		int IDFarmacoPrecedente = getIntent().getExtras().getInt("IDFarmacoPrecedente");
		myCursorAdapter.setIDFarmacoPrecedente(IDFarmacoPrecedente);
		myCursorAdapter.setRetFarmaco(nome_precedente);
		myCursorAdapter.setRetTipo(tipo_precedente);
		myCursorAdapter.setRetPeso(peso_precedente);
		myCursorAdapter.setRetSomministrazione(somministrazione_precedente);
		ListView myList = (ListView)findViewById(R.id.listFarmaciForResult);
		myList.setAdapter(myCursorAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_completa, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if(c != null)
			c.close();
		if(cursor != null)
			cursor.close();
		finish();
	}
}
