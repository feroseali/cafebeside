package com.team.cafebeside.screenMappers;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.AsyncResponse;
import com.team.cafebeside.networkEngine.AsyncWorker;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class MyBills extends Activity implements AsyncResponse {
	private Button bpay,bhand;
	private TextView tcnum;
	private String cnum,umail;
	private AsyncWorker mAsync = new AsyncWorker(this);
	public ProgressDialog progress;
	public static String TotlAmnt;
	/*private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        bpay = (Button) findViewById(R.id.btncard);
        bhand = (Button) findViewById(R.id.btnhand);
        tcnum = (TextView) findViewById(R.id.tcardnum);
		SharedPrefSingleton shpref;
		shpref = SharedPrefSingleton.getInstance();
		shpref.init(getApplicationContext());
		umail = shpref.getLoggedInUserPreference("email");
		
		/*db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(3);

	    String selectQuery = "SELECT sum(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";
        Cursor c = db.rawQuery(selectQuery,null);
        c.moveToFirst();*/
        //final String Amnt = String.valueOf(c.getInt(c.getColumnIndex("gtotal")));
        //c.close();
		TotlAmnt = String.valueOf(HomeActivity.tAmnt);
        Log.d("Amount : ",TotlAmnt);
        TextView mTotal = (TextView) findViewById(R.id.myTotal);
        mTotal.setText("Rs."+TotlAmnt);
        bhand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MyBills.this);
				builder.setTitle("CafeBeside Info");
				builder.setMessage("Thank you,\nPlease give the cash to bearer or directly to the counter.");
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
			}
		});
        
        
        bpay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cnum = tcnum.getText().toString();
				if(cnum.trim().equals("")){
					Toast.makeText(getApplicationContext(),"Please enter the card details first!", Toast.LENGTH_LONG).show();
				}
				else{
					try {

				        JSONObject mObject = new JSONObject();
						mObject.put("email", umail);
						mObject.put("odate", FoodItem.formattedDate);
						mObject.put("cardnumber", cnum);
						mObject.put("amount", TotlAmnt);
						Log.d("JSON CARD INFO :", mObject.toString());
						mAsync = new AsyncWorker(v.getContext());
						mAsync.delegate=MyBills.this;
						mAsync.execute(ServerConnector.POST_CARDINFO, mObject.toString());
						Log.d("Card Json : ",mObject.toString());
						//finish();
					} catch (Exception ex) {
						Log.d("Exception","Exception occur "+ex);
					}
				}
				
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
		try{
			JSONObject mObject = new JSONObject();
			mObject.put("email", umail);
			mAsync = new AsyncWorker(getApplicationContext());
			mAsync.delegate=MyBills.this;
			mAsync.execute(ServerConnector.LOGOUT,mObject.toString());
			}
			catch(Exception ex){
				Log.d("Exception:",""+ex);
			}
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


	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		
		Log.e("CARD INFO RETURN",output);
		try{
			if (output.contains("-")) {
			    // Split it.
			String[] parts = output.split("-");
			String part1 = parts[0]; // success			
			String part2 = parts[1]; // balance
			if(part1.trim().equals("success")){
				tcnum.setText("");
				//Toast.makeText(getApplicationContext(), "Payment Successfully Completed!", Toast.LENGTH_SHORT).show();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MyBills.this);
				builder.setTitle("CafeBeside Info");
				builder.setMessage("Thank you!,\nYour Payment Successfully Completed!\nBalance in your card : Rs."+part2);
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
			 }
			}
			else if(output.trim().equals("loggedout")){
				SharedPrefSingleton.getInstance().init(getApplicationContext());
				SharedPrefSingleton.getInstance().writePreference("isLoggedIn", false);
				SharedPrefSingleton.getInstance().writeSPreference("email", null);
				Intent homeIntent	=	new Intent(this,LoginPage.class);
				startActivity(homeIntent);
				finish();
			}
			else if(output.trim().equals("loggedoutfailed")){
				Toast.makeText(getApplicationContext(), "LogOut Failed!,Try Again!", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), output, Toast.LENGTH_SHORT).show();
				tcnum.setText("");
			}
		}
		catch(Exception ecc){
			Log.d("EXception :","In server response "+ ecc);
		}
	
	}
	

	
}


