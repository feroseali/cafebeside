package com.team.cafebeside.screenMappers;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class Checkout extends Activity {
	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;
	boolean doubleBackToExitPressedOnce = false;
 	private TextView tv_total=null;
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    ArrayList<HashMap<String, String>> myorderLists = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
	    HashMap<String, String> myolist = new HashMap<String, String>();
	    ListView olistView = (ListView) findViewById(R.id.olist);
        Button bmore = (Button) findViewById(R.id.btn_more);
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
					Intent intent = new Intent(ACTION_SCAN);
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
				} catch (ActivityNotFoundException anfe) {
					showDialog(Checkout.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
				}
			}
		});
		db = openOrCreateDatabase(_DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        //db.execSQL("DELETE FROM orders"); //delete all rows in a table
		/*Calendar ca = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formDate = df.format(ca.getTime());	*/
        
        Log.d("Format Date:","From Previous :" + FoodItem.formattedDate);
        Log.d("Food ID:","From Previous :" + FoodItem.unm);
	    tv_total = (TextView) findViewById(R.id.grtotal);

        //String selectQuery = "SELECT *,sum(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";
        // String selectQuery = "SELECT * FROM orders where oDate ='"+fit.formattedDate;
       String selectQuery = "SELECT oEmail,oDate,oFoodid,oItmName,oCat,oQuantity,oFprice,oInst,sTotal,sum(sTotal) as gtotal FROM orders where oDate = '" +FoodItem.formattedDate + "' and oEmail='"+FoodItem.unm+"'";

        Cursor c = db.rawQuery(selectQuery,null);
        /*        String cnt = cc.getString(0);
        Log.d("Row Count:",cnt);*/
        
        c.moveToFirst();
        if (c != null) {
        do {
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
        	Log.d("Frm SQLITE:","GrandTotal: "+c.getString(c.getColumnIndex("gtotal")));

            myolist.put("Item", c.getString(c.getColumnIndex("oItmName")));
            myolist.put("Quantity", c.getString(c.getColumnIndex("oQuantity")));
            myolist.put("Price", "Rs."+c.getString(c.getColumnIndex("oFprice")));
            myolist.put("Subtotal", "Rs."+c.getString(c.getColumnIndex("sTotal")));
            tv_total.setText(c.getString(c.getColumnIndex("gtotal")));
            myorderLists.add(myolist);
    	    ListAdapter oadapter = new SimpleAdapter(
                    Checkout.this, myorderLists,
                    R.layout.orderlist, new String[] {"Item","Quantity","Price","Subtotal"}, new int[] {
        R.id.tv3,R.id.tv4,R.id.itot2,R.id.stot});
    	    olistView.setAdapter(oadapter);
        	
        	
         } while (c.moveToNext());
        
       // tv_total.setText(c.getString(c.getColumnIndex("gtotal")));

        }
        c.close(); 
        
        

        
        
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

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();

			}
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
	
}





