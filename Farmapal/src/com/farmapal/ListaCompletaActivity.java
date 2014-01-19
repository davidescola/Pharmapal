package com.farmapal;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.ListView;

import com.farmapal.database.DBHelper;

public class ListaCompletaActivity extends Activity {

	DBHelper db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_completa);
		db = new DBHelper(this);
		populateListViewFromDB();
	}

	private void populateListViewFromDB() {
		cursor = db.getAllFarmaci();
		String[] from = new String[] {"nome", "tipo", "peso", "somministrazione"};

		int[] to = new int[] {R.id.item_nome,R.id.item_tipo,R.id.item_peso,R.id.item_somministrazione };

		SimpleCursorAdapter myCursorAdapter =
				new SimpleCursorAdapter(this,
						R.layout.item_list_farmaci,
						cursor,
						from, 
						to,0);
		ListView myList = (ListView)findViewById(R.id.listFarmaci);
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
		if(cursor != null)
			cursor.close();
		db.close();
		finish();
	}

}
