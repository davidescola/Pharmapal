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
	private Cursor c;
	private Context context;
	private ArrayList<CheckBox> listCheckbox;
	private ArrayList<TextView> listTextSomministrazione;
	private ArrayList<TextView> listTextPeso;
	private ArrayList<TextView> listTextTipo;
	private String retFarmaco;
	private String retSomministrazione;
	private String retPeso;
	private String retTipo;

	public FarmaciAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		this.c = c;
		this.context = context;
		listCheckbox = new ArrayList<CheckBox>();
		listTextSomministrazione = new ArrayList<TextView>();
		listTextPeso = new ArrayList<TextView>();
		listTextTipo = new ArrayList<TextView>();
		retFarmaco = new String();
		retSomministrazione = new String();
		retPeso = new String();
		retTipo = new String();
		for (int i = 0; i < this.getCount(); i++) {
			itemChecked.add(i, false); // initializes all items value with false
		}
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

		checkFarmaco.setTag(c.getPosition());
		textSomministrazione.setTag(c.getPosition());
		textPeso.setTag(c.getPosition());
		textTipo.setTag(c.getPosition());
		listCheckbox.add(checkFarmaco);
		listTextSomministrazione.add(textSomministrazione);
		listTextPeso.add(textPeso);
		listTextTipo.add(textTipo);
		itemChecked.add(false);

		checkFarmaco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v.findViewById(R.id.checkFarmaco);
//				Toast.makeText(v.getContext(), "cliccata checkbox " + cb.getText() 
//						+ " somm: " + listTextSomministrazione.get((Integer)cb.getTag()).getText()
//						+ " tipo: " + listTextTipo.get((Integer)cb.getTag()).getText()
//						+ " peso: " + listTextPeso.get((Integer)cb.getTag()).getText(),
//						Toast.LENGTH_SHORT).show();
				
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

	//	@Override
	//	public View getView(int position, View inView, ViewGroup parent) {
	//		
	//		final int pos = position;
	//		if (inView == null) {
	//	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//	        inView = inflater.inflate(R.layout.item_list_farmaci_for_result, null);
	//	    }
	//
	//	    final CheckBox cBox = (CheckBox) inView.findViewById(R.id.checkFarmaco); 
	//	    cBox.setOnClickListener(new OnClickListener() {
	//
	//	        public void onClick(View v) {
	//
	//	            CheckBox cb = (CheckBox) v.findViewById(R.id.checkFarmaco);
	//
	//	            if (cb.isChecked()) {
	//	                itemChecked.set(pos, true);
	//	                for(int i = 0; i < itemChecked.size(); i++) {
	//	                	if(itemChecked.get(i) && i != pos)
	//	                		itemChecked.set(i, false);
	//	                }
	//	                
	//	            } else if (!cb.isChecked()) {
	//	                itemChecked.set(pos, false);
	//	                
	//	            }
	//	        }
	//	    });
	//	    cBox.setChecked(itemChecked.get(pos)); // this will Check or Uncheck the
	//	    // CheckBox in ListView
	//	    // according to their original
	//	    // position and CheckBox never
	//	    // loss his State when you
	//	    // Scroll the List Items.
	//	    return inView;
	//	}



}
