package com.team.cafebeside.screenMappers;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.Configuration;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.AsyncResponse;
import com.team.cafebeside.networkEngine.AsyncWorker;
import com.team.cafebeside.workers.CafeNetworkValidator;
import com.team.cafebeside.workers.SharedPrefSingleton;


/**
 * @author Little Adam
 *
 */
public class HomeActivity extends Activity implements OnItemClickListener,AsyncResponse {
	private AsyncWorker mAsyncTsk = new AsyncWorker(this);
	public ProgressDialog progress;
	private String uemail;
	
	static final LauncherIcon[] ICONS = {
        new LauncherIcon(R.drawable.icon_five, "Todays Menu", "icon_five.png"),
        new LauncherIcon(R.drawable.icon_four, "My Orders", "icon_four.png"),
        new LauncherIcon(R.drawable.icon_two, "My Bills", "icon_two.png"),
        new LauncherIcon(R.drawable.icon_seven, "Contact", "icon_seven.png"),
};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Configuration.IS_APP_RUNNING = true;
		
		
		CafeNetworkValidator validator	=	new CafeNetworkValidator();
		boolean isConnected				=	validator.isConnectedToCafeBeside(this);
		
		if(!isConnected){
			showAlert();
		}
		else{
			Toast.makeText(getApplicationContext(), "Welcome To Cafe Beside Network.",Toast.LENGTH_SHORT).show();
		}
		GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(this);
		gridview.setOnTouchListener(new OnTouchListener() {


			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		
		SharedPrefSingleton shpref;
		shpref = SharedPrefSingleton.getInstance();
		shpref.init(getApplicationContext());
		uemail = shpref.getLoggedInUserPreference("email");

	}

	private void showAlert(){
		new AlertDialog.Builder(this)
	    .setTitle("Message")
	    .setMessage("You are not connected to CafeBeside Network !")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            //DO ANY CAFEBESIDE OPERATION
	        	finish();
	        }
	     })
	    
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
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
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	
        switch(position){
        case 0:	Intent i1 = new Intent(this, MenuList.class);
        		startActivity(i1);
        		break;
        case 1: Intent i2 = new Intent(this, MyOrders.class);
				startActivity(i2);	
				break;
        case 2: try {
			    JSONObject mObject = new JSONObject();
			    mObject.put("email", uemail);
			    Log.d("JSON BILL INFO :", mObject.toString());
			    mAsyncTsk = new AsyncWorker(v.getContext());
			    mAsyncTsk.delegate=HomeActivity.this;
			    mAsyncTsk.execute(ServerConnector.GET_BILLSTATUS, mObject.toString());
			    //finish();
		        } catch (Exception ex) {
		    	Log.d("Exception","Exception occur "+ex);
		        }
				break;
        case 3: Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:9400017251"));
		        startActivity(callIntent);	
				break;		
        }
    }

	static class LauncherIcon {
		final String text;
		final int imgId;

		public LauncherIcon(int imgId, String text, String map) {
			super();
			this.imgId = imgId;
			this.text = text;

		}

	}

	static class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return ICONS.length;
		}

		@Override
		public LauncherIcon getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		static class ViewHolder {
			public ImageView icon;
			public TextView text;
		}

		// Create a new ImageView for each item referenced by the Adapter
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder holder;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				v = vi.inflate(R.layout.dashboard_icon, null);
				holder = new ViewHolder();
				holder.text = (TextView) v
						.findViewById(R.id.dashboard_icon_text);
				holder.icon = (ImageView) v
						.findViewById(R.id.dashboard_icon_img);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			holder.icon.setImageResource(ICONS[position].imgId);
			holder.text.setText(ICONS[position].text);

			return v;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Configuration.IS_APP_RUNNING = true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Configuration.IS_APP_RUNNING = false;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

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
		try{	
			if(output.trim().equals("success")){
				//Toast.makeText(getApplicationContext(), "Payment Successfully Completed!", Toast.LENGTH_SHORT).show();
        	    Intent i3 = new Intent(this, MyBills.class);
				startActivity(i3);
				finish();
			 }
			 else{
				 View bills = View.inflate(this, R.layout.bills_layout, null);

				 AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
					builder.setTitle("CafeBeside Info");
					//.setMessage("Thank you!,\nYour Payment Successfully Completed!\nBalance in your card :")
					builder.setView(bills)
					.setCancelable(false);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Log.e("info", "OK");
									//Intent home_intent = new Intent(getApplicationContext(),HomeActivity.class);
									//startActivity(home_intent);
									//finish();
								}
							});

					builder.show();			
				}
		    }
		    catch(Exception ecc){
			Log.d("EXception :","In server response "+ ecc);
		    }
	}
	
}
