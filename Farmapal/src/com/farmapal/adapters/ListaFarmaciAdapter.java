package com.farmapal.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.farmapal.R;

public class ListaFarmaciAdapter extends CursorAdapter {

	public ListaFarmaciAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView itemNome = (TextView) view.findViewById(R.id.item_nome);
		TextView itemTipo = (TextView) view.findViewById(R.id.item_tipo);
		TextView itemPeso = (TextView) view.findViewById(R.id.item_peso);
		TextView itemSomministrazione = (TextView) view.findViewById(R.id.item_somministrazione);
		
		itemNome.setText(cursor.getString(cursor.getColumnIndex("nome")));
		itemTipo.setText(cursor.getString(cursor.getColumnIndex("tipo")));
		itemPeso.setText(cursor.getString(cursor.getColumnIndex("peso")));
		itemSomministrazione.setText(cursor.getString(cursor.getColumnIndex("somministrazione")));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.item_list_farmaci, parent, false);

		return retView;
	}

}
