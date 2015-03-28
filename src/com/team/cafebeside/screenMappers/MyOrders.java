package com.team.cafebeside.screenMappers;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class MyOrders extends Activity {
	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;
    ArrayList<HashMap<String, String>> myallorderLists = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myorders);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPrefSingleton shpref;
		shpref = SharedPrefSingleton.getInstance();
		shpref.init(getApplicationContext());
		String usrmail = shpref.getLoggedInUserPreference("email");
	    HashMap<String, String> myoallist = new HashMap<String, String>();
	    ListView allordrlist = (ListView) findViewById(R.id.myallorderlist);
	   /* String[] values = new String[] { "Android List View", 
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android", 
                "Android Example", 
                "List View Source Code", 
                "List View Array Adapter", 
                "Android Example List View" 
               };*/
	    db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(3);
       // String selectQuery1 = "DELETE FROM orders";
        //db.execSQL("delete from orders");
       // db.rawQuery(selectQuery1,null);
        Log.d("User Email :","From Preference: "+usrmail);
        //ArrayAdapter<String> ladapter = new ArrayAdapter<String>(this,R.layout.allorders,R.id.ordate, values);
		String selectQuery = "SELECT oDate,SUM(sTotal) as gTotal FROM orders where oEmail='"+usrmail+"'";
		
		Cursor c = db.rawQuery(selectQuery,null);
	    int cnt = c.getCount();
	    String cnnt = String.valueOf(cnt);
	    Log.d("SQLITE TBL ROW COUNT :",cnnt);
	    if(cnt>0){
        c.moveToFirst();
        if (c != null) {
            do {
            	Log.d("oDate in select","Query :"+c.getString(c.getColumnIndex("oDate")));
            	Log.d("Amount in select","Query :"+c.getInt(c.getColumnIndex("gTotal")));

            	myoallist.put("Date", c.getString(c.getColumnIndex("oDate")));
            	myoallist.put("Amount", "Rs."+c.getInt(c.getColumnIndex("gTotal")));
            	myallorderLists.add(myoallist);
         	     ListAdapter oadapter = new SimpleAdapter(
                         MyOrders.this, myallorderLists,
                         R.layout.allorders, new String[] {"Date","Amount"}, new int[] {
                         R.id.ordate,R.id.ortotal});
         	    allordrlist.setAdapter(oadapter);
            }
            while(c.moveToNext());
        }
        c.close();
	    }
	    else{
	    	AlertDialog.Builder builder = new AlertDialog.Builder(MyOrders.this);
			builder.setTitle("CafeBeside Info");
			builder.setMessage("You didn't make any orders yet!\nOrder something first!!!");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							Log.e("info", "OK");
							Intent home_intent = new Intent(getApplicationContext(),HomeActivity.class);
							startActivity(home_intent);
							finish();
						}
					});

			builder.show();	        
			c.close();
	    }
        // Assign adapter to ListView
	    //allordrlist.setAdapter(ladapter); 
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
	        Log.d("Click","Clicked Action Bar Icon");
			return true;
		}
		else if(id== R.id.logout){	
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		new AlertDialog.Builder(this)
				.setTitle("Alert")
				.setMessage("Are you sure you want exit ?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								System.exit(0);
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}
	
}





