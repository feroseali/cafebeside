package com.team.cafebeside.screenMappers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.team.cafebeside.R;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.AsyncResponse;
import com.team.cafebeside.networkEngine.AsyncWorker;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class Checkout extends Activity implements AsyncResponse{
	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;
	boolean doubleBackToExitPressedOnce = false;
 	private TextView tv_total=null;
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	private AsyncWorker orderAsyncWrkr = new AsyncWorker(this);
	public ProgressDialog progress;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		orderAsyncWrkr.delegate = this;		
		setContentView(R.layout.checkout);
	    HashMap<String, String> myolist = new HashMap<String, String>();
	    ListView olistView = (ListView) findViewById(R.id.olist);
        Button bmore = (Button) findViewById(R.id.btn_more);
		db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(3);
    	final JSONObject orderJObject = new JSONObject();
        ArrayList<HashMap<String, String>> myorderLists = new ArrayList<HashMap<String, String>>();

        bmore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntnt = new Intent(getApplicationContext(),MenuList.class);
				startActivity(mIntnt);
				finish();
			}
		});
        
        Button bcontinue = (Button) findViewById(R.id.btn_continue);
        bcontinue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					/*Intent intent = new Intent(ACTION_SCAN);
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);*/
					final IntentIntegrator mIntegrate = new IntentIntegrator(Checkout.this);
			        mIntegrate.isScannerExists();
					IntentResult iRslt = new IntentResult();
					String qrreslt = iRslt.getContents();
					Log.d("QR Content","QRCODE "+qrreslt);
				    
					String selectallQry = "SELECT oEmail,oDate,oFoodid,oItmName,oCat,oQuantity,oFprice,oInst,sTotal,sum(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";
					Cursor c = db.rawQuery(selectallQry,null);
					c.moveToFirst();
			        if (c != null) {
			        do {
			        	orderJObject.put("email", c.getString(c.getColumnIndex("oEmail")));
			        	orderJObject.put("date", c.getString(c.getColumnIndex("oDate")));
			        	orderJObject.put("foodid", c.getString(c.getColumnIndex("oFoodid")));
			        	orderJObject.put("price", c.getString(c.getColumnIndex("oFprice")));
			        	orderJObject.put("tableid", qrreslt);
			        	orderJObject.put("foodname", c.getString(c.getColumnIndex("oItmName")));
			        	orderJObject.put("category", c.getString(c.getColumnIndex("oCat")));
			        	orderJObject.put("quantity", c.getString(c.getColumnIndex("oQuantity")));
			        	orderJObject.put("sinstructions", c.getString(c.getColumnIndex("oInst")));
			        	orderJObject.put("subtotal", c.getString(c.getColumnIndex("sTotal")));
			        	orderJObject.put("grandtotal", c.getInt(c.getColumnIndex("gtotal")));
	        	
			         } while (c.moveToNext());
			        
			       // tv_total.setText(c.getString(c.getColumnIndex("gtotal")));

			        }
			        c.close(); 					
					orderAsyncWrkr = new AsyncWorker(v.getContext());
					orderAsyncWrkr.delegate=Checkout.this;
					orderAsyncWrkr.execute(ServerConnector.POST_ORDERINFO, orderJObject.toString());
					Log.d("Json String all orders : ",orderJObject.toString());

				} catch (Exception anfe) {
					showDialog(Checkout.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
				}
			}
		});

              
        Log.d("Format Date:","From Previous :" + FoodItem.formattedDate);
        Log.d("Food ID:","From Previous :" + FoodItem.unm);
	    tv_total = (TextView) findViewById(R.id.grtotal);

	    
       /* Cursor cc = db.rawQuery("select SUM(sTotal) as gtotal from orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'",null);
	    
        int cown = cc.getInt(cc.getColumnIndex("gtotal"));
	    String connt = String.valueOf(cown);
	    Log.d("TBL ROW COUNT :",connt);
	    cc.close();*/
        //String selectQuery = "SELECT *,sum(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";
        // String selectQuery = "SELECT * FROM orders where oDate ='"+fit.formattedDate;
       String selectQuery = "SELECT * ,SUM(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";

        Cursor c = db.rawQuery(selectQuery,null);
        int cown = c.getInt(c.getColumnIndex("gtotal"));
	    String connt = String.valueOf(cown);
	    Log.d("TBL ROW COUNT :",connt);
        
	    int cnt = c.getCount();
	    String cnnt = String.valueOf(cnt);
	    Log.d("SQLITE TBL ROW COUNT :",cnnt);
	    if(cnt>0){    
		        if (c != null && c.moveToFirst()){
		        while(c.moveToNext()){
		        	Log.d("..................",".............................");
		        	Log.d("Frm SQLITE:","Email: "+c.getString(c.getColumnIndex("oEmail")));
		        	Log.d("Frm SQLITE:","Date: "+c.getString(c.getColumnIndex("oDate")));
		        	Log.d("Frm SQLITE:","Fid: "+c.getString(c.getColumnIndex("oFoodid")));
		        	Log.d("Frm SQLITE:","Item: "+c.getString(c.getColumnIndex("oItmName")));
		        	Log.d("Frm SQLITE:","Cat: "+c.getString(c.getColumnIndex("oCat")));
		        	Log.d("Frm SQLITE:","Quantity: "+c.getString(c.getColumnIndex("oQuantity")));
		        	Log.d("Frm SQLITE:","Price: "+c.getString(c.getColumnIndex("oFprice")));
		        	Log.d("Frm SQLITE:","Special: "+c.getString(c.getColumnIndex("oInst")));
		        	Log.d("Frm SQLITE:","STotal: "+c.getString(c.getColumnIndex("sTotal")));
		        	Log.d("Frm SQLITE:","GrandTotal: "+connt);
		
		            myolist.put("Item", c.getString(c.getColumnIndex("oItmName")));
		            myolist.put("Quantity", c.getString(c.getColumnIndex("oQuantity")));
		            myolist.put("Price", "Rs."+c.getString(c.getColumnIndex("oFprice")));
		            myolist.put("Subtotal", "Rs."+c.getString(c.getColumnIndex("sTotal")));
		            tv_total.setText(connt);
		            myorderLists.add(myolist);
		    	    ListAdapter oadapter = new SimpleAdapter(
		                    Checkout.this, myorderLists,
		                    R.layout.orderlist, new String[] {"Item","Quantity","Price","Subtotal"}, new int[] {
		                    R.id.tv3,R.id.tv4,R.id.itot2,R.id.stot});
		    	    olistView.setAdapter(oadapter);
		    	    
		    	    Log.d("Item frm list:",myolist.get("Item"));
		    	    Log.d("Item frm list:",myolist.get("Quantity"));
		    	    Log.d("Item frm list:",myolist.get("Price"));
		    	    Log.d("Item frm list:",myolist.get("Subtotal"));
		        	
		         }
		        
		       // tv_total.setText(c.getString(c.getColumnIndex("gtotal")));
		        c.close(); 

		        }
	    }
	    else{
	    	AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
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
        

        
        
	}
	



	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {

				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});
		return downloadDialog.show();
	}

/*	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();

			}
		}
	}*/
	
	
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
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		if (output.contains("-")) {
		    // Split it.
		String[] parts = output.split("-");
		String part1 = parts[0]; // success			
		String part2 = parts[1]; // total
		if(part1.trim().equals("success")){
			AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
			builder.setTitle("CafeBeside Info");
			builder.setMessage("Thank you!,\nYour orders placed successfully!\nPlease wait for a while!\n You have to pay : Rs."+part2);
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
		else{
			Toast.makeText(getApplicationContext(), "Order placement failed,Try again!", Toast.LENGTH_SHORT).show();	

		}
	}	
	
}





