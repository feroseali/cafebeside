package com.team.cafebeside.screenMappers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class MyOrders extends Activity {
	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
		
        Toast.makeText(getApplicationContext(), "Now you can see your orders here!", Toast.LENGTH_LONG).show();
		db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        String selectQuery = "SELECT * FROM orders";
        Cursor c = db.rawQuery(selectQuery,null);
        Cursor cc=db.rawQuery("Select count(*) from orders;", null);
        String cnt = cc.getString(0);
        Log.d("Row Count:",cnt);
        
        c.moveToFirst();
        if (c != null) {
        do {
        	Log.d("..................",".............................");
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oEmail")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oDate")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oFoodid")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oItmName")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oCat")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oQuantity")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oFprice")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("oInst")));
        	Log.d("From SQLITE:",c.getString(c.getColumnIndex("sTotal")));
         } while (c.moveToNext());
        }
        c.close();
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





