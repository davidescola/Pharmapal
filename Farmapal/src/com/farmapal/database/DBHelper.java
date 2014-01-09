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
	
	public DBHelper (Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
		DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/"; 
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
		try {
			createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public Cursor getNomiFarmaci() {
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery("SELECT nome FROM farmaco", null);
	
		return c;
	}

	public Cursor getAllFarmaci() {
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery("SELECT * FROM farmaco", null);
		
		return c;
	}

	public Cursor getAllPazienti() {
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery("SELECT * FROM paziente", null);
		
		return c;
	}

	public long insertPaziente(String nome, int index) {
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		
		ContentValues cvs = new ContentValues();
		cvs.put("_id", index);
		cvs.put("nome", nome);
		long check = db.insert("paziente", null, cvs);
		
		return check;

	}
	
	public Cursor getAllFarmacie(){
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery("SELECT * FROM farmacia", null);
		   
		return c;
	}
	
	public Cursor getDatiFarmacia(int id_farmacia){
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		Cursor c = db.rawQuery("SELECT * FROM farmacia WHERE _id=" + id_farmacia, null);
		   
		return c;
	}

}

