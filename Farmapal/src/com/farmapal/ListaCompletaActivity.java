package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.farmapal.adapters.ListaFarmaciAdapter;
import com.farmapal.database.DBHelper;

public class ListaCompletaActivity extends Activity {

	private DBHelper db;
	private Cursor cursor;
	private ListaFarmaciAdapter adapter;

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
		
//		String[] from = new String[] {"nome", "tipo", "peso", "somministrazione"};
//
//		int[] to = new int[] {R.id.item_nome,R.id.item_tipo,R.id.item_peso,R.id.item_somministrazione };
//
//		SimpleCursorAdapter myCursorAdapter =
//				new SimpleCursorAdapter(this,
//						R.layout.item_list_farmaci,
//						cursor,
//						from, 
//						to,0);
		ListView myList = (ListView)findViewById(R.id.listFarmaci);
		myList.setAdapter(adapter);
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(getApplicationContext(), DettaglioFarmacoActivity.class);
				Bundle b = new Bundle();
				b.putInt("id_farmaco", (int) id);
				intent.putExtras(b);
				startActivity(intent);
				
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

}
