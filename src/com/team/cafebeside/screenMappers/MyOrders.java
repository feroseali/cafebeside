package com.team.cafebeside.screenMappers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.ServiceHandlers;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class MyOrders extends Activity{
    private ProgressDialog pDialog;
    private static String usrmail;
	private static final String TAG_ORDERS = "orders";
	private static final String TAG_DATE = "date";
	private static final String TAG_AMOUNT = "price";
    private static ListView allordrlist;
/*	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;*/
    ArrayList<HashMap<String, String>> myallorderLists = new ArrayList<HashMap<String, String>>();
	JSONArray orlist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myorders);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPrefSingleton shpref;
		shpref = SharedPrefSingleton.getInstance();
		shpref.init(getApplicationContext());
		usrmail = shpref.getLoggedInUserPreference("email");
	    allordrlist = (ListView) findViewById(R.id.myallorderlist);
        new LoadAllOrders().execute();

/*	    db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(3);*/


	    	/*AlertDialog.Builder builder = new AlertDialog.Builder(MyOrders.this);
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

			builder.show();	     */   
	
	}
	
	
	
	private class LoadAllOrders extends AsyncTask<Void, Void, Void> {
		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyOrders.this);
            pDialog.setMessage("Loading My Orders. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ServiceHandlers serviceClient = new ServiceHandlers(); 
            Log.d("url: ", "> " + ServerConnector.GET_ORDERSTATUS);
			String json = serviceClient.makeServiceCall(ServerConnector.GET_ORDERSTATUS+usrmail,ServiceHandlers.GET);
            Log.d("Get Category response: ", "> " + json);
			if (json != null) {
				try{
                    Log.d("try", "in the try");
		            JSONObject jsonObj = new JSONObject(json);
		            Log.d("jsonObject", "new json Object");
		            orlist = jsonObj.getJSONArray(TAG_ORDERS);
                    Log.d("json aray", "user point array" + orlist);
					int len = orlist.length();
                    Log.d("len", "get array length"+ len);
						for (int i = 0; i < len; i++) {
						    JSONObject c = orlist.getJSONObject(i);
						    Log.d("JSONObject", "jsonObject created "+c);
						    String idate = c.getString(TAG_DATE);
						    Log.d("iDate ", idate);
						    String oamount = c.getString(TAG_AMOUNT);
						    Log.d("Amount in List ", oamount);

						    
						    HashMap<String, String> myoallist = new HashMap<String, String>();

			            	myoallist.put("Date",idate);
			            	myoallist.put("Amount", "Rs."+oamount);  
			            	myallorderLists.add(myoallist);

						}

				}
				catch(Exception e){
	                Log.d("catch", "in the catch");
	                e.printStackTrace();
				}
			}
			else{
                Log.e("JSON Data", "Didn't receive any data from server!");
			}
			
			return null;
		}
		
		 @Override
         protected void onPostExecute(Void result) {
         super.onPostExecute(result);
         pDialog.dismiss();        
         ListAdapter oadapter = new SimpleAdapter(
                 MyOrders.this, myallorderLists,
                 R.layout.allorders, new String[] {"Date","Amount"}, new int[] {
                 R.id.ordate,R.id.ortotal});
 	    allordrlist.setAdapter(oadapter);
 	            
     }
		 
		 
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





