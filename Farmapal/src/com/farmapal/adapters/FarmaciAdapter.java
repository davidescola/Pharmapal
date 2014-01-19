package com.farmapal.adapters;


import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.farmapal.R;

public class FarmaciAdapter extends CursorAdapter {

	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
//	private Cursor c;
//	private Context context;
	private ArrayList<CheckBox> listCheckbox;
	private ArrayList<TextView> listTextSomministrazione;
	private ArrayList<TextView> listTextPeso;
	private ArrayList<TextView> listTextTipo;
	private String retFarmaco;
	private String retSomministrazione;
	private String retPeso;
	private String retTipo;
	private int iDFarmacoPrecedente;

	public int getIDFarmacoPrecedente() {
		return iDFarmacoPrecedente;
	}

	public void setIDFarmacoPrecedente(int iDFarmacoPrecedente) {
		this.iDFarmacoPrecedente = iDFarmacoPrecedente;
	}

	public FarmaciAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
//		this.c = c;
//		this.context = context;
		listCheckbox = new ArrayList<CheckBox>();
		listTextSomministrazione = new ArrayList<TextView>();
		listTextPeso = new ArrayList<TextView>();
		listTextTipo = new ArrayList<TextView>();
		retFarmaco = new String();
		retSomministrazione = new String();
		retPeso = new String();
		retTipo = new String();
		for (int i = 0; i < this.getCount(); i++) {
			itemChecked.add(i, false);
		}
	}

	public void setRetFarmaco(String retFarmaco) {
		this.retFarmaco = retFarmaco;
	}

	public void setRetSomministrazione(String retSomministrazione) {
		this.retSomministrazione = retSomministrazione;
	}

	public void setRetPeso(String retPeso) {
		this.retPeso = retPeso;
	}

	public void setRetTipo(String retTipo) {
		this.retTipo = retTipo;
	}

	public String getRetFarmaco() {
		return retFarmaco;
	}

	public String getRetSomministrazione() {
		return retSomministrazione;
	}

	public String getRetPeso() {
		return retPeso;
	}

	public String getRetTipo() {
		return retTipo;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox checkFarmaco = (CheckBox) view.findViewById(R.id.checkFarmaco);
		TextView textSomministrazione = (TextView) view.findViewById(R.id.item_somministrazione_for_result);
		TextView textPeso = (TextView) view.findViewById(R.id.item_peso_for_result);
		TextView textTipo = (TextView) view.findViewById(R.id.item_tipo_for_result);

		checkFarmaco.setText(cursor.getString(cursor.getColumnIndex("nome")));
		textSomministrazione.setText(cursor.getString(cursor.getColumnIndex("somministrazione")));
		textPeso.setText(cursor.getString(cursor.getColumnIndex("peso")));
		textTipo.setText(cursor.getString(cursor.getColumnIndex("tipo")));

		checkFarmaco.setTag(cursor.getPosition());
		textSomministrazione.setTag(cursor.getPosition());
		textPeso.setTag(cursor.getPosition());
		textTipo.setTag(cursor.getPosition());
		listCheckbox.add(checkFarmaco);
		listTextSomministrazione.add(textSomministrazione);
		listTextPeso.add(textPeso);
		listTextTipo.add(textTipo);
		itemChecked.add(false);
		if(cursor.getInt(cursor.getColumnIndex("_id")) == iDFarmacoPrecedente) {
			itemChecked.add(cursor.getPosition(), true);
			checkFarmaco.setChecked(true);
		}

		checkFarmaco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v.findViewById(R.id.checkFarmaco);

				if(!cb.isChecked()) {
					itemChecked.set((Integer)cb.getTag(), false);
					cb.setChecked(false);
					retFarmaco = "";
					retSomministrazione = "";
					retTipo = "";
					retPeso = "";

				}
				else {
					itemChecked.set((Integer)cb.getTag(), true);
					cb.setChecked(true);
					retFarmaco = cb.getText().toString();
					retSomministrazione = listTextSomministrazione.get((Integer)cb.getTag()).getText().toString();
					retTipo = listTextTipo.get((Integer)cb.getTag()).getText().toString();
					retPeso = listTextPeso.get((Integer)cb.getTag()).getText().toString();
					resetOtherCheckbox((Integer)cb.getTag());
				}


			}
		});


	}

	protected void resetOtherCheckbox(int position) {
		for (int i = 0; i < listCheckbox.size(); i++) {
			if (i != position && itemChecked.get(i)) {
				listCheckbox.get(i).setChecked(false);
				itemChecked.set(i, false);
			}
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.item_list_farmaci_for_result, parent, false);

		return retView;
	}

	public boolean itemIsChecked() {
		return (!retFarmaco.equals("") && !retSomministrazione.equals("") && !retPeso.equals("") && !retTipo.equals(""));
	}

}
