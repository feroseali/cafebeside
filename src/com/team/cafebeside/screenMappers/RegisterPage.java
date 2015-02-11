package com.team.cafebeside.screenMappers;


import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.networkEngine.HttpRequestWorker;

public class RegisterPage extends Activity {
	
	public ProgressDialog progress;
	private EditText uname,uemail,umobile,upass,ucpass;
	private Button submit_button;
	private String unm,umail,umob,upwd,ucpwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		
		Toast.makeText(getApplicationContext(), "Sign up your account",Toast.LENGTH_LONG ).show();
		
		uname = (EditText) findViewById(R.id.uname);
		unm = uname.getText().toString();
		uemail = (EditText) findViewById(R.id.uemail);
		umail = uemail.getText().toString();
		umobile = (EditText) findViewById(R.id.umobile);
		umob = umobile.getText().toString();
		upass = (EditText) findViewById(R.id.upass);
		upwd = upass.getText().toString();
		ucpass = (EditText) findViewById(R.id.ucpass);
		ucpwd = ucpass.getText().toString();
		
		submit_button = (Button) findViewById(R.id.signup);
	
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(unm.length()>1 && umail.length()>1 && umob.length()>1 && upwd.length()>1 && ucpwd.length()>1){			
					// out of range
					Log.e("Start AsyncTask","Async Task Started now");
					MyAsyncTask mm = new MyAsyncTask();		
					mm.execute();
				}else{
					Toast.makeText(getApplicationContext(), "please enter something", Toast.LENGTH_LONG).show();
					Log.e("Async","Async Task Not Started");

				}			

			}
		});
		
	}
	
	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	//super.onBackPressed();
	    	new AlertDialog.Builder(this)
	        .setTitle("Alert")
	        .setMessage("Are you sure you want exit ?")
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            	System.exit(0);
	            }
	         })
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // do nothing
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert)
	         .show();
	    	
	    }
	 
	 
	/***** Async Task inside Main Class ******/  
	 
	public class MyAsyncTask extends AsyncTask<String, String, String>{

        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);


            progress = new ProgressDialog(RegisterPage.this);
            progress.setTitle("Processing");
            progress.setMessage("Wait while loading...");
            progress.setIndeterminate(false);
            progress.setCancelable(true);
            progress.show();
        }		
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
        	try{
            	JSONObject mObject = new JSONObject();
            	mObject.put("Name",uname);
            	mObject.put("Mail",uemail);
            	mObject.put("Mob",umobile);
            	mObject.put("Password",ucpass);
            	String mString = mObject.toString();

            	HttpRequestWorker mWorker = new HttpRequestWorker();
            	 response = mWorker.PostRequest("http://192.168.0.2/cafebeside/reg.php", mString, false);
                
            	
            	 return response;
            	
                }catch(Exception e){
                	return null;
                }
		}
 
		protected void onPostExecute(String result){
			//pb.setVisibility(View.GONE);
            progress.dismiss();
			Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
			//pb.setProgress(progress[0]);
		}
 
 
	}	
	

}