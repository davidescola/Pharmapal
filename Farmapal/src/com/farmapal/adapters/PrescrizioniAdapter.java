package com.farmapal.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.farmapal.DettaglioPrescrizioneActivity;
import com.farmapal.R;
import com.farmapal.database.DBHelper;

public class PrescrizioniAdapter extends CursorAdapter {

	private DBHelper db;
	private Activity callerActivity;

	public Activity getCallerActivity() {
		return callerActivity;
	}

	public void setCallerActivity(Activity callerActivity) {
		this.callerActivity = callerActivity;
	}

	public PrescrizioniAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		db = DBHelper.getInstance(context);

		TextView textFarmaco = (TextView) view.findViewById(R.id.miePrescrizioniFarmaco);
		TextView textData = (TextView) view.findViewById(R.id.miePrescrizioniData);
		TextView textPaziente = (TextView) view.findViewById(R.id.miePrescrizioniPaziente);
		TextView textPeso = (TextView) view.findViewById(R.id.miePrescrizioniPeso);
		TextView textTipo = (TextView) view.findViewById(R.id.miePrescrizioniTipo);
		TextView textMedico = (TextView) view.findViewById(R.id.miePrescrizioniMedico);

		callerActivity = (Activity) context;

		int idFarmaco = cursor.getInt(cursor.getColumnIndex("id_farmaco"));
		Cursor cursorIDFarmaco = db.getFarmacoFromID(idFarmaco);
		cursorIDFarmaco.moveToFirst();
		textFarmaco.setText(cursorIDFarmaco.getString(cursorIDFarmaco.getColumnIndex("nome")));
		textPeso.setText(cursorIDFarmaco.getString(cursorIDFarmaco.getColumnIndex("peso")));
		textTipo.setText(cursorIDFarmaco.getString(cursorIDFarmaco.getColumnIndex("tipo")));

		view.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(callerActivity, DettaglioPrescrizioneActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", v.getTag().toString());
				intent.putExtras(bundle);
				//callerActivity.startActivityForResult(intent, 4);
				callerActivity.startActivity(intent);
				callerActivity.finish();

			}
		});

		int idPaziente = cursor.getInt(cursor.getColumnIndex("id_paziente"));
		Cursor cursorIDPaziente = db.getPazienteFromID(idPaziente);
		cursorIDPaziente.moveToFirst();
		textPaziente.setText("paziente: " + cursorIDPaziente.getString(cursorIDPaziente.getColumnIndex("nome")));

		String data_inizio = cursor.getString(cursor.getColumnIndex("data_inizio"));
		String data_fine = cursor.getString(cursor.getColumnIndex("data_fine"));
		textData.setText("dal " + data_inizio + " al " + data_fine);

		textMedico.setText("prescritto da: " + cursor.getString(cursor.getColumnIndex("medico")));
	}

	@Override
	public View newView(final Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.item_list_prescrizioni, parent, false);
		return retView;
	}

}
