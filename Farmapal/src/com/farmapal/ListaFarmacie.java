package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.farmapal.database.DBHelper;

public class ListaFarmacie extends Activity {
	
	DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_farmacie);
		db = DBHelper.getInstance(getApplicationContext());
		populateListViewFromDB();				
	}

	private void populateListViewFromDB() {
		Cursor cursor = db.getAllFarmacie();

		startManagingCursor(cursor);

		String[] from = new String[] {"nome", "citta"};

		int[] to = new int[] {R.id.nome_farmacia, R.id.dettagli_farmacia };

		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,	R.layout.item_list_farmacie , cursor, from, to);


		//set the adapter for the listview
		ListView myList = (ListView)findViewById(R.id.listFarmacie);
		myList.setAdapter(myCursorAdapter);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//position: posizione nella ListView
				//id: valore di _id del database
				TextView tx_nome_farmacia =(TextView)view.findViewById(R.id.nome_farmacia);
	            String keyword = tx_nome_farmacia.getText().toString();
	            Log.v("View Cliccata", keyword + " Posizione Lista: " + position + " Id del DB: "+ id);
	            Intent i = new Intent(getBaseContext(),DescrizioneFarmaciaActivity.class);
	            i.putExtra("id", (int)id);
	            startActivity(i);
			}
			
		});

	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

}

