package com.farmapal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.farmapal.database.DBHelper;

public class DescrizioneFarmaciaActivity extends Activity {

	private static final int defaultValue = 0;
	private static final String tag = "DescrizioneFarmacia";
	private DBHelper db;
	private TextView tx_view_nome_farmacia;
	private TextView tx_view_indirizzo_farmacia;
	private TextView tx_view_citta_farmacia;
	private TextView tx_view_provincia_farmacia;
	private TextView tx_view_telefono_farmacia;
	private TextView tx_view_sito_farmacia;
	private TextView tx_view_email_farmacia;
	private Cursor c;
	private int id_farmacia_selezionata;
	private TextView tx_view_cap_farmacia;
	private Button buttonRaggiungici;
	private String indirizzo;
	private String email_farmacia;
	private String telefono_farmacia;
	private String sito_farmacia;
	private ShareActionProvider mShareActionProvider;
	private Button buttonCondividiFarmacia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descrizione_farmacia);
		
		caricaViews();
		ottieniDati();
		visualizzaDati();
		setListeners();
	}
	
	private void caricaViews(){
		tx_view_nome_farmacia = (TextView) findViewById(R.id.textViewInfoFarmacia);
		tx_view_indirizzo_farmacia = (TextView) findViewById(R.id.textViewInfoIndirizzoFarmacia);
		tx_view_citta_farmacia = (TextView) findViewById(R.id.textViewInfoCittaFarmacia);
		tx_view_cap_farmacia = (TextView) findViewById(R.id.textViewInfoCapFarmacia);
		tx_view_provincia_farmacia = (TextView) findViewById(R.id.textViewInfoProvinciaFarmacia);
		tx_view_telefono_farmacia = (TextView) findViewById(R.id.textViewInfoTelefonoFarmacia);
		tx_view_sito_farmacia = (TextView) findViewById(R.id.textViewInfoSitoFarmacia);
		tx_view_email_farmacia = (TextView) findViewById(R.id.textViewInfoEmailFarmacia);
		buttonRaggiungici = (Button) findViewById(R.id.buttonNavigazioneMaps);
		buttonCondividiFarmacia = (Button) findViewById(R.id.buttonCondividiFarmacia); 
	}
	
	private void ottieniDati(){
		//Ottengo l'id dell'intent da ListaFarmacie
		Bundle extras = getIntent().getExtras();
		id_farmacia_selezionata=extras.getInt("id", defaultValue);
		Log.w(tag, ""+id_farmacia_selezionata);
		//Istanzio il DB
		db = DBHelper.getInstance(getApplicationContext());
		//Ottengo Dati Farmacia selezionata
		c=db.getDatiFarmacia(id_farmacia_selezionata);
		c.moveToFirst();
	}
	
	private void visualizzaDati(){
		tx_view_nome_farmacia.setText(c.getString(c.getColumnIndex("nome")));
		
		//Formato Indirizzo: via giovanni, 2, 23880 Casatenovo
		indirizzo=c.getString(c.getColumnIndex("via")) + ", " + c.getString(c.getColumnIndex("numero"));
		tx_view_indirizzo_farmacia.setText(indirizzo);
		
		String cap = String.valueOf(c.getInt(c.getColumnIndex("cap")));
		tx_view_cap_farmacia.setText(cap);
		
		tx_view_citta_farmacia.setText(c.getString(c.getColumnIndex("citta")));
		
		tx_view_provincia_farmacia.setText(c.getString(c.getColumnIndex("provincia")));
		
//		telefono_farmacia = String.valueOf(c.getString(c.getColumnIndex("n_telefono")));
		telefono_farmacia = c.getString(c.getColumnIndex("n_telefono"));
		tx_view_telefono_farmacia.setText(telefono_farmacia);
		tx_view_telefono_farmacia.setTextColor(Color.BLUE);
		
		sito_farmacia = c.getString(c.getColumnIndex("sito"));
		tx_view_sito_farmacia.setText(sito_farmacia);
		tx_view_sito_farmacia.setTextColor(Color.BLUE);
		
		email_farmacia = c.getString(c.getColumnIndex("email"));
		tx_view_email_farmacia.setText(email_farmacia);
		tx_view_email_farmacia.setTextColor(Color.BLUE);
		
	}
	
	private void setListeners() {
		openPhoneCall();
		openWebSite();
		openEmail();
		showMap();	
		condividiFarmacia();
	}
	
	private void openPhoneCall(){
		
		tx_view_telefono_farmacia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
			    intent.setData(Uri.parse("tel:" + telefono_farmacia));
			    if (intent.resolveActivity(getPackageManager()) != null) {
			        startActivity(intent);
			    }
			}
		});
		
	}
	
	private void openWebSite(){
		
		tx_view_sito_farmacia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			    Uri webpage = Uri.parse(sito_farmacia);
			    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
			    //Controllo che esista un'App capace di risolvere il mio Intent (il tipo di richiesta)
			    if (intent.resolveActivity(getPackageManager()) != null) {
			        startActivity(intent);
			    }
			    //Controllo se nel DB e' stato salvato un indirizzo Web senza il prefisso "http"
			    else
			    {
			    	webpage = Uri.parse("http://"+sito_farmacia);
				    intent = new Intent(Intent.ACTION_VIEW, webpage);
				    if (intent.resolveActivity(getPackageManager()) != null) {
				        startActivity(intent);
				    }
				    else{
				    	Log.e(tag, "indirizzo web non valido");
				    }
			    }
			}
		});
		
	}
	
	private void openEmail(){
		//Azione per qualsiasi App
//		Intent intent = new Intent(Intent.ACTION_SEND);
//	    intent.setType("*/*");
		//intent.putExtra(Intent.EXTRA_STREAM, attachment);
		tx_view_email_farmacia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SENDTO);
			    intent.setData(Uri.parse("mailto:")); // Solo per email
			    intent.putExtra(Intent.EXTRA_EMAIL, email_farmacia);
			    intent.putExtra(Intent.EXTRA_SUBJECT, "");	    
			    if (intent.resolveActivity(getPackageManager()) != null) {
			        startActivity(intent);
			    }
			}
		});
		
	}
	
	private void showMap() {
		buttonRaggiungici.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String geoLocation="geo:0,0?q=";
				//Creo GeoLoc in forma   geo:0,0?q=my+street+address
				geoLocation += indirizzo;
				Uri myUri = Uri.parse(geoLocation);
				Intent intent = new Intent(Intent.ACTION_VIEW);
			    intent.setData(myUri);
			    if (intent.resolveActivity(getPackageManager()) != null) {
			        startActivity(intent);
			    }
			}
		});
	}	
	
	public void condividiFarmacia(){
		buttonCondividiFarmacia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getDefaultShareIntent();
			    if (intent.resolveActivity(getPackageManager()) != null) {
			        startActivity(intent);
			    }
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
 
        /* Inflate dell'activity con il menu: res/menu/items.xml */
        getMenuInflater().inflate(R.menu.items, menu);
 
        /* ottengo l'actionProivder il cui Item e' "condividi" */
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.condividi).getActionProvider();
 
        /** Metto l'intent di Condivisione come Impostazione */
        mShareActionProvider.setShareIntent(getDefaultShareIntent());
 
        return super.onCreateOptionsMenu(menu);
	}
	
    /** Returns a share intent */
    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,sito_farmacia);
        return intent;
    }

}
