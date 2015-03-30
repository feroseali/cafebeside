package com.team.cafebeside.screenMappers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class FoodItem extends Activity{
	private Button b_ordr,_decrease,_increase;
	private TextView fnm,fcat,fprice,_value;
	private EditText spInst;
	public static String f_id,f_price,unm,_stringVal,_instructions;
    private static int _counter = 1;
    public static String formattedDate;
	public ProgressDialog progress;
	private final String _DB_NAME = "CafeBeside.db";
	private SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_cart);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPrefSingleton shpref;
		shpref = SharedPrefSingleton.getInstance();
		shpref.init(getApplicationContext());
		unm = shpref.getLoggedInUserPreference("email");
		
		db = openOrCreateDatabase(_DB_NAME,SQLiteDatabase.CREATE_IF_NECESSARY, null);

        db.setVersion(3);
        //db.execSQL("DROP TABLE IF EXISTS orders");
		db.execSQL("CREATE TABLE IF NOT EXISTS orders(oEmail VARCHAR(30),oDate VARCHAR(12),oFoodid VARCHAR(6),oItmName VARCHAR(30),oCat VARCHAR(30),oQuantity VARCHAR(6),oFprice VARCHAR(10),oInst VARCHAR(100),sTotal INTEGER)");

        
		_decrease = (Button) findViewById(R.id.minus);
        _increase = (Button) findViewById(R.id.plus);
        _value = (TextView) findViewById(R.id.quanity);
        spInst	=	(EditText) findViewById(R.id.spInstructions);       
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		f_id = extras.getString("TAG_FOODID");
		final String f_name = extras.getString("TAG_FNAME");
		final String f_cat = extras.getString("TAG_FCATID");
		f_price = extras.getString("TAG_FPRICE");		
		fnm = (TextView)findViewById(R.id.lbl1);
		fnm.setText(f_name);
		fcat = (TextView)findViewById(R.id.lbl3);
		fcat.setText(f_cat);		
		fprice = (TextView)findViewById(R.id.lbl2);
		fprice.setText(f_price);			
		b_ordr = (Button)findViewById(R.id.btn_ordr); 
		_stringVal="1";
        _decrease.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("src", "Decreasing value...");
                _counter--;
                _stringVal = Integer.toString(_counter);
                if(_counter<1){
                    _value.setText(1);
                }else{
                    _value.setText(_stringVal);
                }
            }
        });

        _increase.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("src", "Increasing value...");
                _counter++;
                _stringVal = Integer.toString(_counter);
                _value.setText(_stringVal);
            }
        });	
		b_ordr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_instructions = spInst.getText().toString();
				if(_instructions.trim().equals("")){
					_instructions = "Nothing";
				}
				int grandTotal = Integer.parseInt(f_price) * Integer.parseInt(_stringVal);
				String stotal = String.valueOf(grandTotal);

				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				formattedDate = df.format(c.getTime());				
				//Toast.makeText(getApplicationContext(),"You Order FoodId: "+ f_id +"\n Your Email: "+ unm +"\n Price: "+f_price, Toast.LENGTH_LONG).show();
				Log.d("Food Item ID : ",f_id);
				Log.d("USER EMAIL : ",unm);
				Log.d("PRICE : ",f_price);
				Log.d("Quantity:",_stringVal);
				Log.d("Instructions:",_instructions);
				Log.d("Category Name",f_cat);
				Log.d("Food Name:",f_name);
				Log.d("Sub Total Int:",stotal);			
				Log.d("Todays Date :",formattedDate);
				
				ContentValues insertValues = new ContentValues();
				insertValues.put("oEmail",unm);
				insertValues.put("oDate",formattedDate);
				insertValues.put("oFoodid",f_id);
				insertValues.put("oItmName",f_name);
				insertValues.put("oCat",f_cat);
				insertValues.put("oQuantity",_stringVal);
				insertValues.put("oFprice",f_price);
				insertValues.put("oInst",_instructions);
				insertValues.put("sTotal",stotal);
				db.insert("orders", null, insertValues);
				Log.d("Data Inserted:","SQLITE INSERT Success");

					Intent checkout	=	new Intent(FoodItem.this,Checkout.class);
					startActivity(checkout);	
					finish();
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
			SharedPrefSingleton shb;
			shb = SharedPrefSingleton.getInstance();
			shb.init(getApplicationContext());
			shb.writePreference("isLoggedIn", false);
			shb.writeSPreference("email","");
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