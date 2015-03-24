package com.team.cafebeside.screenMappers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.ServiceHandlers;
import com.team.cafebeside.workers.SharedPrefSingleton;

public class SubMenuList extends ListActivity {
	
    private ProgressDialog pDialog;
	private static final String TAG_ITEMS = "items";
	private static final String TAG_FID = "foodid";
	private static final String TAG_FNAME = "item";
	private static final String TAG_FPRICE = "price";
	private static String categoryID; 
	private static String fcat;
	JSONArray smlist = null;
    ArrayList<HashMap<String, String>> submenuList = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submenulists);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
        // getting attached intent data
        categoryID = i.getStringExtra("TAG_CID");
        // displaying selected product name
		// Call Async task to get the match fixture
        new LoadSubCategories().execute();
        
        // Get listview
        ListView lv = getListView();
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String fudid = ((TextView) view.findViewById(R.id.food_id)).getText().toString();
                String fudname = ((TextView) view.findViewById(R.id.food_item)).getText().toString();
                String fudprice = ((TextView) view.findViewById(R.id.food_price)).getText().toString();

                Log.d("FOODID", "String food id = "+fudid);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),FoodItem.class);
                Bundle extras = new Bundle();
                extras.putString("TAG_FOODID", fudid);
                extras.putString("TAG_FNAME", fudname);
                extras.putString("TAG_FCATID", fcat);
                extras.putString("TAG_FPRICE", fudprice);                
                in.putExtras(extras);
                startActivity(in);
            }

			/*@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				
			}*/
        });        
	}
	
	
	private class LoadSubCategories extends AsyncTask<Void, Void, Void> {
		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SubMenuList.this);
            pDialog.setMessage("Loading todays MENU. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			ServiceHandlers serviceClient = new ServiceHandlers(); 
            Log.d("url: ", "> " + ServerConnector.URI_ITEMS+categoryID);
			String json = serviceClient.makeServiceCall(ServerConnector.URI_ITEMS+categoryID,ServiceHandlers.GET);
            Log.d("Get Category response: ", "> " + json);
			if (json != null) {
				try{
                    Log.d("try", "in the try");
		            JSONObject jsonObj = new JSONObject(json);
		            Log.d("jsonObject", "new json Object");
					smlist = jsonObj.getJSONArray(TAG_ITEMS);
                    Log.d("json aray", "user point array" + smlist);
					int len = smlist.length();
                    Log.d("len", "get array length"+ len);
						for (int i = 0; i < len; i++) {
						    JSONObject c = smlist.getJSONObject(i);
						    Log.d("JSONObject", "jsonObject created "+c);
						    fcat = c.getString("category");
						    Log.d("FoodCategory", fcat);
						    String fdid = c.getString(TAG_FID);
						    Log.d("id", fdid);
						    String fdname = c.getString(TAG_FNAME);
						    Log.d("Item", fdname);
						    String fdprice = c.getString(TAG_FPRICE);
						    Log.d("Price", fdprice);
						    //  hashmap for single match
						    HashMap<String, String> smlist = new HashMap<String, String>();
						    // adding each child node to HashMap key => value
						    smlist.put(TAG_FID, fdid);
						    smlist.put(TAG_FNAME, fdname);
						    smlist.put(TAG_FPRICE, fdprice);
						    submenuList.add(smlist);
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
         ListAdapter adapter = new SimpleAdapter(
                         SubMenuList.this, submenuList,
                         R.layout.sublist_item, new String[] {TAG_FID, TAG_FNAME,TAG_FPRICE}
         , new int[] {
             R.id.food_id,R.id.food_item,R.id.food_price
         });
         setListAdapter(adapter);
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
