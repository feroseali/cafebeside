package com.team.cafebeside.screenMappers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class MyBills extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bills_layout);
		
        Toast.makeText(getApplicationContext(), "Now you can see your bills here!", Toast.LENGTH_LONG).show();
       
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


