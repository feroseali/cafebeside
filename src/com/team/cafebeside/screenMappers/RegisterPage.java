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
import com.team.cafebeside.networkEngine.PostMan;

public class RegisterPage extends Activity {
	
	public ProgressDialog progress;
	private EditText uname,uemail,umobile,upass,ucpass;
	private Button submit_button;
	public String unm,umail,umob,upwd,ucpwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
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
		
		Log.e("Name","Name:"+ unm);
		Log.e("Mail","mail:"+ umail);
		Log.e("Mob","mob:"+ umob);
		Log.e("Pwd","pass:"+ ucpwd);

		
		Toast.makeText(getApplicationContext(), "Sign up your account",Toast.LENGTH_LONG ).show();

		submit_button = (Button)findViewById(R.id.regbtn);
	
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
/*				if((unm.length()==0) || (umail.length()==0) || (umob.length()==0) || (upwd.length()==0) || (ucpwd.length()==0) ){			
					Toast.makeText(getApplicationContext(), "please enter something", Toast.LENGTH_LONG).show();
					Log.e("Async","Async Task Not Started");
				}*/
				//else{		
				Log.e("Name",unm);
				Log.e("Mail",umail);
				Log.e("Mob",umob);
				Log.e("Pwd",ucpwd);
					Log.e("Start AsyncTask","Async Task Started now");
					new MyAsyncTask().execute();
				//}			

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
	 
	public class MyAsyncTask extends AsyncTask<String, Integer, String>{

        String resp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
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
            	mObject.put("Name",unm);
            	mObject.put("Mail",umail);
            	mObject.put("Mob",umob);
            	mObject.put("Password",ucpwd);

            	String json_string=mObject.toString();
				PostMan mSender=new PostMan();
				resp=mSender.post(json_string,"http://feroseali.in/cafebeside/reg.php");
				if(resp.contains("Success")){
					Toast.makeText(getBaseContext(),"Registered Succesfully", Toast.LENGTH_LONG).show();					
				}
				else{
					Toast.makeText(getBaseContext(),"Failed", Toast.LENGTH_LONG).show();
				}          	
            	 return resp;
            	
                }catch(Exception e){
                	return null;
                }
		}
 
		protected void onPostExecute(String result){
			//pb.setVisibility(View.GONE);
            progress.dismiss();
			Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
		    super.onPostExecute(result);

		}
		protected void onProgressUpdate(Integer... values){
			//pb.setProgress(progress[0]);
		    Log.d("MyAsyncTask","onProgressUpdate");
		    super.onProgressUpdate(values);
		}
 
 
	}	
	

}