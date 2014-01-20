package com.farmapal.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static String DB_NAME = "pharmapal";
	private SQLiteDatabase db;
	private final Context context;
	private String DB_PATH;
	private static DBHelper db_istanza = null;

	private DBHelper (Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context.getApplicationContext();
		DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
//		DB_PATH = context.getFilesDir().getPath() + context.getPackageName() + "/" + "databases/";
		try {
			createDatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);;

	}

	public static DBHelper getInstance(Context context) {
		if(db_istanza == null)
			db_istanza = new DBHelper(context.getApplicationContext());
		return db_istanza;
	}

	public void createDatabase() throws IOException {
		boolean dbExist = checkDatabase();
		if(!dbExist) {
			this.getReadableDatabase();
			try{
				copyDatabase();
			}
			catch (IOException e) {
				throw new Error("Errore durante la copia del db");
			}
		}
	}

	private void copyDatabase() throws IOException{
		InputStream myInput = context.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0 ) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private boolean checkDatabase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


	}

	public Cursor getNomiFarmaci() {
		Cursor c = db.rawQuery("SELECT nome FROM farmaco", null);
		return c;
	}

	public Cursor getAllFarmaci() {
		Cursor c = db.rawQuery("SELECT * FROM farmaco", null);
		return c;
	}

	public Cursor getAllPazienti() {
		Cursor c = db.rawQuery("SELECT * FROM paziente", null);
		return c;
	}

	public Cursor getAllPrescrizioni() {
		Cursor c = db.rawQuery("SELECT * FROM prescrizione", null);
		return c;
	}

	public Cursor getFarmacoFromID(int id) {
		Cursor c = db.rawQuery("SELECT * FROM farmaco WHERE _id = '" + id + "'", null);
		return c;
	}

	public Cursor getPrescrizioneFromID(int id) {
		Cursor c = db.rawQuery("SELECT * FROM prescrizione WHERE _id = '" + id + "'", null);
		return c;
	}

	public Cursor getPazienteFromID(int id) {
		Cursor c = db.rawQuery("SELECT * FROM paziente WHERE _id = '" + id + "'", null);
		return c;
	}

	public Cursor getIDFarmacoFromValori(String nome, String tipo, String peso, String somministrazione) {
		Cursor c = db.rawQuery("SELECT _id FROM farmaco WHERE nome = '" + nome + "'" +
				"AND tipo = '" + tipo + "'" +  
				"AND peso = '" + peso + "'" +
				"AND somministrazione = '" + somministrazione + "'", null);
		return c;
	}

	public long insertPaziente(String nome, int index) {
		ContentValues cvs = new ContentValues();
		cvs.put("_id", index);
		cvs.put("nome", nome);
		long check = db.insert("paziente", null, cvs);
		return check;

	}

	public long insertPrescrizione(int index, String medico, String data_inizio, String data_fine, int quantita, int[] flagGiorni, int id_farmaco, int id_paziente, String[] razioni) {
		ContentValues cvs = new ContentValues();
		cvs.put("_id", index);
		cvs.put("medico", medico);
		cvs.put("data_inizio", data_inizio);
		cvs.put("data_fine", data_fine);
		cvs.put("quantita", quantita);
		cvs.put("lunedi", flagGiorni[0]);
		cvs.put("martedi", flagGiorni[1]);
		cvs.put("mercoledi", flagGiorni[2]);
		cvs.put("giovedi", flagGiorni[3]);
		cvs.put("venerdi", flagGiorni[4]);
		cvs.put("sabato", flagGiorni[5]);
		cvs.put("domenica", flagGiorni[6]);
		cvs.put("id_farmaco", id_farmaco);
		cvs.put("id_paziente", id_paziente);

		if(razioni.length > 6)
			return -1;
		for(int i = 1; i <= razioni.length; i++) {
			cvs.put("ora" + i, razioni[i-1]);
			cvs.put("razione_presa" + i, 0);
		}

		long check = db.insert("prescrizione", null, cvs);

		return check;
	}

	public long insertFarmaco(int index, String nome, String principi_attivi, String peso, String tipo, String somministrazione, String indicazioni, String controindicazioni, String effetti_collaterali) {
		ContentValues cvs = new ContentValues();
		cvs.put("_id", index);
		cvs.put("nome", nome);
		cvs.put("principi_attivi", principi_attivi);
		cvs.put("peso", peso);
		cvs.put("tipo", tipo);
		cvs.put("somministrazione", somministrazione);
		cvs.put("indicazioni", indicazioni);
		cvs.put("controindicazioni", controindicazioni);
		cvs.put("effetti_indesiderati", effetti_collaterali);

		long check = db.insert("farmaco", null, cvs);

		return check;
	}
	public Cursor getAllFarmacie(){
		Cursor c = db.rawQuery("SELECT * FROM farmacia", null);
		return c;
	}

	public Cursor getDatiFarmacia(int id_farmacia){
		Cursor c = db.rawQuery("SELECT * FROM farmacia WHERE _id=" + id_farmacia, null);
		return c;
	}

	public Cursor getIDFarmaco(String nome, String tipo, String peso, String somministrazione) {
		Cursor c = db.rawQuery("SELECT _id FROM farmaco WHERE" +
				" nome = '" + nome + "' AND" +
				" tipo = '" + tipo + "' AND" +
				" peso = '" + peso + "' AND" +
				" somministrazione = '" + somministrazione + "'",null);
		return c;
	}

	public Cursor getIDPaziente(String nome) {
		Cursor c = db.rawQuery("SELECT _id FROM paziente WHERE" +
				" nome = '" + nome + "'", null);
		return c;
	}

	public Cursor getMaxIDPaziente() {
		Cursor c = db.rawQuery("SELECT MAX(_id) FROM paziente", null);
		return c;
	}

	public Cursor getMaxIDPrescrizione() {
		Cursor c = db.rawQuery("SELECT MAX(_id) FROM prescrizione", null);
		return c;
	}

	public Cursor getMaxIDFarmaco() {
		Cursor c = db.rawQuery("SELECT MAX(_id) FROM farmaco", null);
		return c;
	}
	
	public boolean deletePrescrizioneFromID(int id) {
		return db.delete("prescrizione", "_id=" + id, null) > 0;
	}

	public boolean deleteFarmacoFromID(int id) {
		return db.delete("farmaco", "_id=" + id, null) > 0;
	}
	
	public boolean deletePrescrizioniFromIDFarmaco(int id) {
		return db.delete("prescrizione", "id_farmaco=" + id, null) > 0;
	}
	
	public long updatePrescrizioneFromID(int index, String data_inizio, String data_fine, int quantita, int[] flagGiorni, String[] razioni) {
		ContentValues cvs = new ContentValues();
		cvs.put("data_inizio", data_inizio);
		cvs.put("data_fine", data_fine);
		cvs.put("quantita", quantita);
		cvs.put("lunedi", flagGiorni[0]);
		cvs.put("martedi", flagGiorni[1]);
		cvs.put("mercoledi", flagGiorni[2]);
		cvs.put("giovedi", flagGiorni[3]);
		cvs.put("venerdi", flagGiorni[4]);
		cvs.put("sabato", flagGiorni[5]);
		cvs.put("domenica", flagGiorni[6]);

		if(razioni.length > 6)
			return -1;

		for(int i = 1; i <= razioni.length; i++) {
			cvs.put("ora" + i, razioni[i-1]);
			cvs.put("razione_presa" + i, 0);
		}

		for(int i = razioni.length + 1; i <= 6; i++) {
			cvs.putNull("ora" + i);
			cvs.putNull("razione_presa" + i);
		}

		return db.update("prescrizione", cvs, "_id = " + index, null);

	}

}