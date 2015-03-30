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


public class MenuList extends ListActivity {
    private ProgressDialog pDialog;
	private static final String TAG_CATEGORIES = "categories";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "category";
	private static final String TAG_COUNT = "catcount";
	JSONArray mlist = null;
    ArrayList<HashMap<String, String>> menuList = new ArrayList<HashMap<String, String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menulists);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Call Async task to get the match fixture
        new LoadAllCategories().execute();
        
     // Get listview
        ListView lv = getListView();
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String cid = ((TextView) view.findViewById(R.id.cat_id)).getText().toString();
                Log.d("CATID", "String cid = "+cid);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),SubMenuList.class);
                // sending cid to next activity
                in.putExtra("TAG_CID", cid);
 
                // starting new activity and expecting some response back
               // startActivityForResult(in, 100);
                startActivity(in);
            }

			/*@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				
			}*/
        });
        
        
	}
	
	private class LoadAllCategories extends AsyncTask<Void, Void, Void> {
		/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuList.this);
            pDialog.setMessage("Loading todays MENU. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			ServiceHandlers serviceClient = new ServiceHandlers(); 
            Log.d("url: ", "> " + ServerConnector.GET_CATEGORY_URL);
			String json = serviceClient.makeServiceCall(ServerConnector.GET_CATEGORY_URL,ServiceHandlers.GET);
            Log.d("Get Category response: ", "> " + json);
			if (json != null) {
				try{
                    Log.d("try", "in the try");
		            JSONObject jsonObj = new JSONObject(json);
		            Log.d("jsonObject", "new json Object");
					mlist = jsonObj.getJSONArray(TAG_CATEGORIES);
                    Log.d("json aray", "user point array" + mlist);
					int len = mlist.length();
                    Log.d("len", "get array length"+ len);
						for (int i = 0; i < len; i++) {
						    JSONObject c = mlist.getJSONObject(i);
						    Log.d("JSONObject", "jsonObject created "+c);
						    String id = c.getString(TAG_ID);
						    Log.d("id", id);
						    String category = c.getString(TAG_NAME);
						    Log.d("category", category);
						    String catcount = c.getString(TAG_COUNT);
						    Log.d("catcount", catcount);
						    //  hashmap for single match
						    HashMap<String, String> mlist = new HashMap<String, String>();
						    // adding each child node to HashMap key => value
						    mlist.put(TAG_ID, id);
						    mlist.put(TAG_NAME, category);
						    mlist.put(TAG_COUNT, catcount);
						    menuList.add(mlist);
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
                         MenuList.this, menuList,
                         R.layout.row, new String[] {TAG_ID, TAG_NAME,TAG_COUNT}
         , new int[] {
             R.id.cat_id,R.id.textViewName,R.id.cat_count
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
