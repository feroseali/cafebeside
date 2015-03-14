package com.team.cafebeside.screenMappers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class FoodItem extends Activity {
	private Button b_ordr;
	private TextView fnm,fcat,fprice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_layout);
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		final String f_id = extras.getString("TAG_FOODID");
		String f_name = extras.getString("TAG_FNAME");
		String f_cat = extras.getString("TAG_FCATID");
		String f_price = extras.getString("TAG_FPRICE");		
       //	b_ordr.setText(f_name);
		fnm = (TextView)findViewById(R.id.fname);
		fnm.setText(f_name);
		fcat = (TextView)findViewById(R.id.fcategory);
		fcat.setText(f_cat);		
		fprice = (TextView)findViewById(R.id.price);
		fprice.setText(f_price);			
		b_ordr = (Button)findViewById(R.id.btnOrder); 
		b_ordr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "You Order FoodId: "+f_id, Toast.LENGTH_LONG).show();
				//Log.d("Food Item ID : ","d"+f_id);
				
			}
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_about) {
	        Toast.makeText(getApplicationContext(), "You Clicked About Menu!", Toast.LENGTH_LONG).show();
	        Log.d("Click","Clicked Action Bar Icon");
			return true;
		}
		else if(id== R.id.logout){	
			Toast.makeText(getApplicationContext(), "You clicked logout button", Toast.LENGTH_LONG).show();
			mlogout();


			
			
			
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void mlogout(){
		SharedPrefSingleton shb;
		shb = SharedPrefSingleton.getInstance();
		shb.init(getApplicationContext());
		shb.writePreference("isLoggedIn", false);
		Intent signinIntent	=	new Intent(this,LoginPage.class);
		startActivity(signinIntent);
		finish();
	}	
	
	
	
}
